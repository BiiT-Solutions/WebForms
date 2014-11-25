package com.biit.webforms.gui.common.utils;

import java.io.ByteArrayOutputStream;

/**
 * Struct like class to pass the data of a file that has been uploaded to the
 * server.
 * 
 */
public class UploadedFile {

	private final String fileName;
	private final ByteArrayOutputStream stream;

	public UploadedFile(String filename, ByteArrayOutputStream stream) {
		this.fileName = filename;
		this.stream = stream;
	}

	public String getFileName() {
		return fileName;
	}

	public ByteArrayOutputStream getStream() {
		return stream;
	}

}
