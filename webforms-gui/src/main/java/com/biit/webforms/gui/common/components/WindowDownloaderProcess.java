package com.biit.webforms.gui.common.components;

import java.io.InputStream;

public abstract class WindowDownloaderProcess {

	private WindowDownloader downloader;

	public void registerWindowDownloader(WindowDownloader downloader) {
		this.downloader = downloader;
	}

	public abstract InputStream getInputStream();

	public void updateProgress(float value) {
		downloader.updateProgress(value);
	}

}