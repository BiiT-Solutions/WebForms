package com.biit.webforms.gui.webpages.formmanager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.persistence.entity.Form;

public class WindowDownloaderJson extends WindowDownloader {

	private static final long serialVersionUID = 1327278107256149670L;

	public WindowDownloaderJson(final Form form, String filename) {
		super(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				return new ByteArrayInputStream(form.toJson().getBytes());
			}
		});
		setIndeterminate(true);
		setFilename(filename);
		showCentered();
	}
}