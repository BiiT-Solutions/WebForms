package com.biit.webforms.gui.webpages.designer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.utils.image.ImageTools;
import com.biit.webforms.gui.common.components.ImagePanel;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ImagePreview extends ImagePanel {
	private static final long serialVersionUID = -2464554146921469606L;
	private static ByteArrayInputStream defaultImage = null;
	private StreamSource streamSource = null;

	public ImagePreview(float resize) {
		super(resize);
	}

	@Override
	protected StreamSource getImage() {
		if (streamSource != null) {
			return streamSource;
		}
		return new DefaultImageSource();
	}

	/**
	 * Create a StreamSource from a form flow image as byte array.
	 */
	private class DefaultImageSource implements StreamResource.StreamSource {
		private static final long serialVersionUID = -324235946884266131L;

		public DefaultImageSource() {
		}

		@Override
		public InputStream getStream() {
			if (defaultImage == null) {
				defaultImage = new ByteArrayInputStream(ImageTools.createDefaultImage((int) getWidth(), (int) getHeight(),
						LanguageCodes.IMAGE_NOT_EXISTING_TEXT.translation()));
			}
			defaultImage.reset();
			return defaultImage;

		}
	}

	public void setStreamSource(StreamSource streamSource) {
		this.streamSource = streamSource;
		// Reset zoom when change the image.
		setResize(1f);
		redraw();
	}

}
