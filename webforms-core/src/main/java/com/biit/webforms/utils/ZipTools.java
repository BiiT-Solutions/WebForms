package com.biit.webforms.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTools {

	/**
	 * Compress and pack a set of files represented as String into one ZIP file.
	 * 
	 * @param filesToZip
	 * @return
	 * @throws IOException
	 */
	public static byte[] zipFiles(List<String> filesToZip) throws IOException {
		List<String> fileNames = new ArrayList<>();

		int digits = (int) (Math.log10(filesToZip.size()) + 1);

		for (int i = 0; i < filesToZip.size(); i++) {
			String currentFile = String.format("%0" + digits + "d", i);
			fileNames.add("export_" + currentFile + ".xml");
		}
		return zipFiles(filesToZip, fileNames);
	}

	public static byte[] zipFiles(List<String> filesToZip, List<String> fileNames) throws IOException,
			IllegalArgumentException {

		if (filesToZip == null || fileNames == null || filesToZip.size() < fileNames.size()) {
			throw new IllegalArgumentException("The number of files and the number of file's names must be the same!");
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		for (int i = 0; i < filesToZip.size(); i++) {
			addFileToZip(zos, filesToZip.get(i).getBytes(Charset.forName("UTF-8")), fileNames.get(i));
		}

		zos.closeEntry();
		zos.close();
		return baos.toByteArray();
	}

	private static void addFileToZip(ZipOutputStream zos, byte[] data, String name) throws IOException {
		ZipEntry entry = new ZipEntry(name);
		entry.setSize(data.length);
		zos.putNextEntry(entry);
		zos.write(data);
	}

}
