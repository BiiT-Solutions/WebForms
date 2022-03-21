package com.biit.webforms.gui.webpages.formmanager;

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