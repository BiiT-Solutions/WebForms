package com.biit.webforms.pdfgenerator;

import java.util.List;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfconversor.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.Form;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class PdfTableGenerator {

	private final static float[] annexFormColumnRatios = { 0.5f, 0.165f, 0.165f, 0.17f };

	public static PdfPTable generateTable(float relativeWidths[], List<PdfTableBlock> tableBlocks)
			throws BadBlockException {
		PdfPTable table = new PdfPTable(relativeWidths);
		table.setSplitRows(false);

		// Check uniformity on table.
		if (!checkUniformity(relativeWidths.length, tableBlocks)) {
			throw new BadBlockException();
		}

		for (PdfTableBlock block : tableBlocks) {
			List<PdfPCell> cells = block.getCells();
			for (PdfPCell cell : cells) {
				table.addCell(cell);
			}
		}

		return table;
	}

	public static void generate(Document document, float relativeWidths[], List<PdfTableBlock> tableBlocks)
			throws DocumentException {
		PdfPTable elementTable = new PdfPTable(relativeWidths);
		elementTable.setSplitRows(false);

	}

	private static boolean checkUniformity(int number, List<PdfTableBlock> tableBlocks) {
		System.out.println("check uniformity");
		for (PdfTableBlock block : tableBlocks) {
			System.out.println(block.isWellFormatted()+" "+number+" "+block.getNumberCols());
			if (!block.isWellFormatted() || (number != block.getNumberCols())) {
				return false;
			}
		}
		return true;
	}

	public static PdfPTable generateAnnexFormTable(Form form) {
		PdfPTable table = null;
		try {
			table = generateTable(annexFormColumnRatios, PdfBlockGenerator.generateAnnexFormTableBlocks(form));
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfTableGenerator.class.getName(), e);
		}
		return table;
	}
}
