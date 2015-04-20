package com.biit.webforms.gui.webpages.formmanager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.exporters.json.BaseFormMetadataExporter;

public class WindowDownloaderBaseFormMetadataJson extends WindowDownloader {

	private static final long serialVersionUID = 1327278107256149670L;

	public WindowDownloaderBaseFormMetadataJson(final Form form, String filename) {
		super(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					return new ByteArrayInputStream(BaseFormMetadataExporter.exportFormMetadata(form).getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					WebformsLogger.errorMessage(this.getClass().getName(), e);
					
					return null;
				}
			}
		});
		setIndeterminate(true);
		setFilename(filename);
		showCentered();
	}
}