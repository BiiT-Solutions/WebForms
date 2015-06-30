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
		return zipFiles(filesToZip, fileNames, null);
	}

	/**
	 * Compress and pack a set of files represented as String into one ZIP file. The user can define the name of the
	 * files and the directory of the form.
	 * 
	 * @param filesToZip
	 *            List of files content as String.
	 * @param fileNames
	 *            A list of file names. Must have the same length than the filesToZip.
	 * @param directory
	 *            Directory inside the zip file where the files will be inserted.
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static byte[] zipFiles(List<String> filesToZip, List<String> fileNames, String directory)
			throws IOException, IllegalArgumentException {

		List<byte[]> filesInBytes = new ArrayList<>();

		for (int i = 0; i < filesToZip.size(); i++) {
			filesInBytes.add(filesToZip.get(i).getBytes(Charset.forName("UTF-8")));
		}

		return zipFilesInByte(filesInBytes, fileNames, directory);
	}

	public static byte[] zipFilesInByte(List<byte[]> filesToZip, List<String> fileNames, String directory)
			throws IOException, IllegalArgumentException {
		if (filesToZip == null || fileNames == null || filesToZip.size() < fileNames.size()) {
			throw new IllegalArgumentException("The number of files and the number of file's names must be the same!");
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		for (int i = 0; i < filesToZip.size(); i++) {
			addFileToZip(zos, filesToZip.get(i), fileNames.get(i), directory);
		}

		zos.closeEntry();
		zos.close();
		return baos.toByteArray();
	}

	private static void addFileToZip(ZipOutputStream zos, byte[] data, String name, String folder) throws IOException {
		ZipEntry entry;
		if (folder == null) {
			entry = new ZipEntry(name);
		} else {
			entry = new ZipEntry(folder + "/" + name);
		}
		entry.setSize(data.length);
		zos.putNextEntry(entry);
		zos.write(data);
	}

}
