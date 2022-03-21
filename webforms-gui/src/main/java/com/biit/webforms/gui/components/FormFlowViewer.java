package com.biit.webforms.gui.components;

import com.biit.form.entity.TreeObject;
import com.biit.utils.image.ImageTools;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.ImagePanel;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.GraphvizApp;
import com.biit.webforms.utils.GraphvizApp.ImgType;
import com.vaadin.server.Scrollable;
import com.vaadin.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A layout that contains an image representing the flow of a form.
 * 
 * Extends panel to allow the use of the {@link Scrollable} interface to allow
 * the move through the image.
 */
public class FormFlowViewer extends ImagePanel {
	private final static long serialVersionUID = -4866123421361857895L;
	private static ByteArrayInputStream defaultImage = null;
	private final ImgType imgType;
	private TreeObject filter;
	private Form form;

	public FormFlowViewer(GraphvizApp.ImgType imgType, float resize) {
		super(resize);
		this.imgType = imgType;
	}

	/**
	 * Creates an unique name for the image.
	 * 
	 * @return
	 */
	@Override
	protected String getImageFilename() {
		// Time stamp is used on image to force a reload on the client
		// browser.
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return getForm().getLabel() + "_" + df.format(new Date()) + "." + imgType.getType();
	}

	public TreeObject getFilter() {
		return filter;
	}

	@Override
	protected StreamResource.StreamSource getImage() {
		return new FlowImageSource();
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
				if (inputStream == null) {
					byte[] imageData = GraphvizApp.generateImage(getForm(), filter, imgType);
					inputStream = new ByteArrayInputStream(imageData);
				}
				return inputStream;
			} catch (IOException ioe) {
				MessageManager.showError(LanguageCodes.GRAPHVIZ_EXEC_NOT_FOUND, LanguageCodes.GRAPHVIZ_EXEC_NOT_FOUND_DESCRIPTION);
				WebformsUiLogger.errorMessage(this.getClass().getName(), ioe);
			} catch (Exception e) {
				WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			}

			if (defaultImage == null) {
				defaultImage = new ByteArrayInputStream(ImageTools.createDefaultImage((int) getWidth(), (int) getHeight(),
						LanguageCodes.IMAGE_NOT_CREATED_TEXT.translation()));
			}
			return defaultImage;
		}
	}

	public void setFormAndFilter(Form form, TreeObject filter) {
		homeZoom();
		setForm(form);
		this.filter = filter;

		redraw();
	}

	public Form getForm() {
		return form;
	}

	protected void setForm(Form form) {
		this.form = form;
	}

}
