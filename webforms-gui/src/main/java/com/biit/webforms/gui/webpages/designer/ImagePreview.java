package com.biit.webforms.gui.webpages.designer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.biit.webforms.gui.common.components.ImagePanel;
import com.biit.webforms.gui.image.ImageTools;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

public class ImagePreview extends ImagePanel {
	private static final long serialVersionUID = -2464554146921469606L;
	private static ByteArrayInputStream defaultImage = null;

	public ImagePreview(float resize) {
		super(resize);
	}

	@Override
	protected StreamSource getImage() {
		return new ImageSource();
	}

	/**
	 * Create a StreamSource from a form flow image as byte array.
	 */
	private class ImageSource implements StreamResource.StreamSource {
		private static final long serialVersionUID = -324235946884266131L;
		private ByteArrayInputStream inputStream = null;

		public ImageSource() {
		}

		@Override
		public InputStream getStream() {
			// Return a stream from the buffer.
			try {
				if (inputStream == null) {
					// byte[] imageData = GraphvizApp.generateImage(getForm(),
					// filter, imgType);
					// inputStream = new ByteArrayInputStream(imageData);
				}
				return inputStream;
			} catch (Exception e) {
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}

			if (defaultImage == null) {
				defaultImage = new ByteArrayInputStream(ImageTools.createDefaultImage((int) getWidth(),
						(int) getHeight(), LanguageCodes.IMAGE_NOT_EXISTING_TEXT.translation()));
			}
			return defaultImage;
		}
	}

}
