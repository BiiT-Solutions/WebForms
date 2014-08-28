package com.biit.webforms.pdfgenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.DocumentException;

public class NeoFormGeneratorPDF {

	public static void generatePDF(String filename, DocumentGenerator generator) throws IOException,
			FileNotFoundException, DocumentException {
		if (generator != null) {
			byte[] data = generator.generate();
			// convert array of bytes into file
			FileOutputStream fileOuputStream = new FileOutputStream(filename);
			fileOuputStream.write(data);
			fileOuputStream.close();
		}
	}
}
