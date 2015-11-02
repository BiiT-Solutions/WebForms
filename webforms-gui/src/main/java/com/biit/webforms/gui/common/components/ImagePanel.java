package com.biit.webforms.gui.common.components;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.biit.webforms.gui.components.ZoomChangedListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Panel;

/**
 * Panel that contains an image with zoom.
 */
public abstract class ImagePanel extends Panel {
	private static final long serialVersionUID = 1199493059375434311L;
	public static final double MIN_AUGMENT = 1.0f;
	public static final double MAX_AUGMENT = 50.0f;

	private Image image = null;
	private double resize = MIN_AUGMENT;
	private StreamResource.StreamSource imagesource;
	private HorizontalLayout imageLayout;
	private List<ZoomChangedListener> listeners;

	public ImagePanel(float resize) {
		this.listeners = new ArrayList<ZoomChangedListener>();
		setResize(resize);
		init();
	}

	public void addZoomChangedListener(ZoomChangedListener listener) {
		listeners.add(listener);
	}

	public void removeZoomChangedListener(ZoomChangedListener listener) {
		listeners.remove(listener);
	}

	public void fireZoomChangedListeners(double zoom) {
		for (ZoomChangedListener listener : listeners) {
			listener.zoomChanged(zoom);
		}
	}

	private void init() {
		setId("ImagePreviewPanel");
		setImmediate(true);

		imageLayout = new HorizontalLayout();
		imageLayout.setId("ImagePreviewLayout");
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
		setContent(imageLayout);
		setSizeFull();
	}

	public void homeZoom() {
		setResizeFactor(MIN_AUGMENT);
	}

	/**
	 * Creates an unique name for the image.
	 * 
	 * @return
	 */
	protected String getImageFilename() {
		// Time stamp is used on image to force a reload on the client
		// browser.
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return "image_" + df.format(new Date());
	}

	protected abstract StreamResource.StreamSource getImage();

	public void redraw() {
		imagesource = getImage();

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
	 * setter
	 */
	protected void setResize(double resizePercentage) {
		resizePercentage = Math.max(resizePercentage, MIN_AUGMENT);
		resizePercentage = Math.min(resizePercentage, MAX_AUGMENT);
		this.resize = resizePercentage;

		fireZoomChangedListeners(resize);
	}

	private void setResizeFactor(double resize) {
		setResize(resize);
		if (imagesource != null) {
			addImage();
		}
	}

	public void setZoom(double zoomFactor) {
		zoomInOut(zoomFactor);
	}

	private void zoomInOut(final double resizeFactor) {
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
				"getElementAndZoom(document.getElementById('" + this.getId() + "').clientWidth,document.getElementById('" + this.getId() + "').clientHeight);");
	}

	private void zoomInOut(final int x, final int y, final double resizeFactor) {

		JavaScript.getCurrent().addFunction("getElementAndZoom", new JavaScriptFunction() {
			private static final long serialVersionUID = 6587969690665052777L;

			@Override
			public void call(final JSONArray arguments) throws JSONException {

				if (resize == MIN_AUGMENT && resizeFactor <= 1.0f) {
					return;
				}
				if (resize == MAX_AUGMENT && resizeFactor >= 1.0f) {
					return;
				}

				double newResizeFactor = resize * resizeFactor;
				double tempResizeFactor = resizeFactor;
				if (newResizeFactor > MAX_AUGMENT) {
					tempResizeFactor = MAX_AUGMENT / resize;
					newResizeFactor = MAX_AUGMENT;
				}

				if (newResizeFactor < MIN_AUGMENT) {
					tempResizeFactor = MIN_AUGMENT / resize;
					newResizeFactor = MIN_AUGMENT;
				}

				int panelX = arguments.getInt(0);
				int panelY = arguments.getInt(1);

				int newClickSizeX = (int) (x * tempResizeFactor);
				int newClickSizeY = (int) (y * tempResizeFactor);
				int halfPanelX = (int) (panelX / 2.0f);
				int halfPanelY = (int) (panelY / 2.0f);
				int positionX = newClickSizeX - halfPanelX;
				int positionY = newClickSizeY - halfPanelY;
				positionX = Math.max(positionX, 0);
				positionY = Math.max(positionY, 0);

				setResizeFactor(newResizeFactor);
				setScrollLeft(positionX);
				setScrollTop(positionY);
			}
		});
		JavaScript.getCurrent().execute(
				"getElementAndZoom(document.getElementById('" + this.getId() + "').clientWidth,document.getElementById('" + this.getId() + "').clientHeight);");
	}

	private void addImage() {
		setScrollLeft(0);
		setScrollTop(0);

		imageLayout.removeAllComponents();
		imageLayout.setWidth((float) (100.0f * resize), Unit.PERCENTAGE);
		imageLayout.setHeight((float) (100.0f * resize), Unit.PERCENTAGE);
		imageLayout.addComponent(image);

		image.markAsDirty();
	}
}
