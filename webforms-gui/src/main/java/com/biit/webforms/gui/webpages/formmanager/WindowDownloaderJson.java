package com.biit.webforms.gui.webpages.formmanager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.persistence.entity.CompleteFormView;

public class WindowDownloaderJson extends WindowDownloader {
	private static final long serialVersionUID = 1327278107256149670L;

	public WindowDownloaderJson(final CompleteFormView completeFormView, String filename) {
		super(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					return new ByteArrayInputStream(completeFormView.toJson().getBytes("UTF-8"));
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

	public WindowDownloaderJson(final com.biit.abcd.persistence.entity.Form form, String filename) {
		super(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					return new ByteArrayInputStream(form.toJson().getBytes("UTF-8"));
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