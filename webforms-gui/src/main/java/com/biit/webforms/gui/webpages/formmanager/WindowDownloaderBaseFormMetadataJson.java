package com.biit.webforms.gui.webpages.formmanager;

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

import com.biit.webforms.exporters.json.BaseFormMetadataExporter;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.persistence.entity.Form;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class WindowDownloaderBaseFormMetadataJson extends WindowDownloader {

	private static final long serialVersionUID = 1327278107256149670L;

	public WindowDownloaderBaseFormMetadataJson(final Form form, String filename) {
		super(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					return new ByteArrayInputStream(BaseFormMetadataExporter.exportFormMetadata(form).getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);

					return null;
				}
			}
		});
		setIndeterminate(true);
		setFilename(filename);
		showCentered();
	}
}
