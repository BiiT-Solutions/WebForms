package com.biit.webforms.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;

import com.biit.form.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.GraphvizApp;
import com.biit.webforms.utils.GraphvizApp.ImgType;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Scrollable;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * A layout that contains an image representing the flow of a form.
 * 
 * Extends panel to allow the use of the {@link Scrollable} interface to allow
 * the move through the image.
 */
public class FormFlowViewer extends Panel {
	private final static long serialVersionUID = -4866123421361857895L;
	private static final float MIN_AUGMENT = 1.0f;
	private VerticalLayout layout = new VerticalLayout();
	private Image image = null;
	private float resize = MIN_AUGMENT;
	private FlowImageSource imagesource;
	private static ByteArrayInputStream defaultImage = null;
	private HorizontalLayout imageLayout;
	private Form form;
	private TreeObject filter;
	private ImgType imgType;

	public FormFlowViewer(GraphvizApp.ImgType imgType, float resize) {
		this.imgType = imgType;
		setResize(resize);
		init();
	}

	private void init() {
		setId("FormFlowPanel");
		setImmediate(true);
		layout.setSizeFull();
		layout.setMargin(false);
		layout.setImmediate(true);
		setContent(layout);
		setSizeFull();
	}

	public void homeZoom() {
		setResizeFactor(MIN_AUGMENT);
	}

	public void setFormAndFilter(Form form, TreeObject filter) {
		homeZoom();
		this.form = form;
		this.filter = filter;

		redraw();
	}

	/**
	 * Creates an unique name for the image.
	 * 
	 * @return
	 */
	private String getImageFilename() {
		// Time stamp is used on image to force a reload on the client
		// browser.
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return form.getName() + "_" + df.format(new Date()) + "." + imgType.getType();
	}

	public void redraw() {
		imagesource = new FlowImageSource();

		String imageName = getImageFilename();
		StreamResource resource = new StreamResource(imagesource, imageName);
		resource.setFilename(getImageFilename());
		// Instruct browser not to cache the image
		resource.setCacheTime(0);

		image = new Image();
		image.setSource(resource);
		image.setSizeFull();

		addImage();
	}

	/**
	 * Resize the image.
	 * 
	 * @param resizePercentage
	 *            % of the new image.
	 */
	private void setResize(float resizePercentage) {
		if (resizePercentage >= MIN_AUGMENT) {
			this.resize = resizePercentage;
		} else {
			this.resize = 1.0f;
		}
	}

	private void setResizeFactor(float resize) {
		setResize(resize);
		if (imagesource != null) {
			addImage();
		}
	}

	public void setZoom(float zoomFactor) {
		zoomInOut(zoomFactor);
	}

	private void zoomInOut(final float resizeFactor) {

		JavaScript.getCurrent().addFunction("getElementAndZoom", new JavaScriptFunction() {
			private static final long serialVersionUID = 6587969690665052777L;

			@Override
			public void call(final JSONArray arguments) throws JSONException {
				int panelX = arguments.getInt(0);
				int panelY = arguments.getInt(1);

				int halfPanelX = (int) (panelX / 2.0f);
				int halfPanelY = (int) (panelY / 2.0f);
				int x = (int) ((getScrollLeft() + halfPanelX) / resize);
				int y = (int) ((getScrollTop() + halfPanelY) / resize);
				int newClickSizeX = (int) (x * resizeFactor);
				int newClickSizeY = (int) (y * resizeFactor);
				int positionX = newClickSizeX - halfPanelX;
				int positionY = newClickSizeY - halfPanelY;
				positionX = Math.max(positionX, 0);
				positionY = Math.max(positionY, 0);

				setResizeFactor(resizeFactor);
				setScrollLeft(positionX);
				setScrollTop(positionY);
			}
		});
		JavaScript.getCurrent().execute(
				"getElementAndZoom(document.getElementById('" + this.getId()
						+ "').clientWidth,document.getElementById('" + this.getId() + "').clientHeight);");
	}

	private void zoomInOut(final int x, final int y, final float resizeFactor) {

		JavaScript.getCurrent().addFunction("getElementAndZoom", new JavaScriptFunction() {
			private static final long serialVersionUID = 6587969690665052777L;

			@Override
			public void call(final JSONArray arguments) throws JSONException {
				int panelX = arguments.getInt(0);
				int panelY = arguments.getInt(1);

				int newClickSizeX = (int) (x * resizeFactor);
				int newClickSizeY = (int) (y * resizeFactor);
				int halfPanelX = (int) (panelX / 2.0f);
				int halfPanelY = (int) (panelY / 2.0f);
				int positionX = newClickSizeX - halfPanelX;
				int positionY = newClickSizeY - halfPanelY;
				positionX = Math.max(positionX, 0);
				positionY = Math.max(positionY, 0);

				if (resize * resizeFactor >= 1.0) {
					setResizeFactor(resize * resizeFactor);
					setScrollLeft(positionX);
					setScrollTop(positionY);
				}
			}
		});
		JavaScript.getCurrent().execute(
				"getElementAndZoom(document.getElementById('" + this.getId()
						+ "').clientWidth,document.getElementById('" + this.getId() + "').clientHeight);");
	}

	private void addImage() {
		setScrollLeft(0);
		setScrollTop(0);

		imageLayout = new HorizontalLayout();
		imageLayout.setId("flowImageLayout");
		imageLayout.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 4564788374245664728L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if (event.isDoubleClick()) {
					return;
				}
				if (event.getButton() == MouseButton.LEFT) {
					zoomInOut(event.getRelativeX(), event.getRelativeY(), 2.0f);
				}
				if (event.getButton() == MouseButton.RIGHT) {
					zoomInOut(event.getRelativeX(), event.getRelativeY(), 1.0f / 2.0f);
				}
			}
		});
		imageLayout.setWidth(100.0f * resize, Unit.PERCENTAGE);
		imageLayout.setHeight(100.0f * resize, Unit.PERCENTAGE);
		imageLayout.addComponent(image);

		// Add image to layout.
		layout.removeAllComponents();
		layout.addComponent(imageLayout);
		layout.setComponentAlignment(imageLayout, Alignment.MIDDLE_CENTER);
		layout.markAsDirtyRecursive();
		image.markAsDirty();
	}

	/**
	 * Create a StreamSource from a form flow image as byte array.
	 */
	private class FlowImageSource implements StreamResource.StreamSource {
		private static final long serialVersionUID = -324235946884266131L;
		private ByteArrayInputStream inputStream = null;

		public FlowImageSource() {
		}

		@Override
		public InputStream getStream() {
			// Return a stream from the buffer.
			try {
				// TODO remove
				if (inputStream == null) {
					byte[] imageData = GraphvizApp.generateImage(form, filter, imgType);
					inputStream = new ByteArrayInputStream(imageData);
				}
				return inputStream;
			} catch (Exception e) {
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}

			if (defaultImage == null) {
				defaultImage = new ByteArrayInputStream(createDefaultImage());
			}
			return defaultImage;
		}
	}

	public Form getForm() {
		return form;
	}

	public TreeObject getFilter() {
		return filter;
	}

	/**
	 * Creates a default image for forms without flow rules.
	 * 
	 * @return
	 */

	private byte[] createDefaultImage() {
		int MIN_SIZE = 200;
		ByteArrayOutputStream imagebuffer = null;
		// No data (because there is not any flow rule).
		// HEIGHT discount the top margin of panel.
		int width = (int) getWidth() < MIN_SIZE ? MIN_SIZE : (int) getWidth();
		int height = (int) getHeight() < MIN_SIZE ? MIN_SIZE : (int) getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics drawable = image.getGraphics();
		drawable.setColor(Color.WHITE);
		drawable.fillRect(0, 0, (int) width, height);
		drawable.setColor(Color.black);
		drawable.drawString("Image cannot be created.", width / 2 - 75, height / 2);

		try {
			// Write the image to a buffer.
			imagebuffer = new ByteArrayOutputStream();
			ImageIO.write(image, "png", imagebuffer);

			// Return a stream from the buffer.
			return imagebuffer.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}
}
