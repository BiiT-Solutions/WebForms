package com.biit.webforms.pdfgenerator;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.Text;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Utility class to generate pdfptables depending on form elements.
 *
 */
public class PdfTableGenerator {

	private final static float[] textColumnRatios = { 1.0f };
	private final static float[] formColumnRatios = { 0.5f, 0.5f };
	private final static float[] annexFormColumnRatios = { 0.34f, 0.18f, 0.16f, 0.16f, 0.16f };
	private final static int BORDER = Rectangle.NO_BORDER;
	private static final float QUESTION_TABLE_PADDING = 20;

	public static PdfPTable generateTable(float relativeWidths[], List<PdfTableBlock> tableBlocks) throws BadBlockException {
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

	public static void generate(Document document, float relativeWidths[], List<PdfTableBlock> tableBlocks) throws DocumentException {
		PdfPTable elementTable = new PdfPTable(relativeWidths);
		elementTable.setSplitRows(false);

	}

	private static boolean checkUniformity(int number, List<PdfTableBlock> tableBlocks) {
		for (PdfTableBlock block : tableBlocks) {
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

	public static PdfPTable generateQuestionTable(PdfWriter writer, Question question) throws BadBlockException {
		PdfPTable questionTable = generateTable(formColumnRatios, PdfBlockGenerator.generateFormQuestionTableBlocks(writer, question));
		questionTable.getDefaultCell().setBorder(BORDER);
		questionTable.setKeepTogether(true);
		questionTable.setSpacingAfter(QUESTION_TABLE_PADDING);

		return questionTable;
	}

	public static PdfPTable generateTextTable(Text text) throws BadBlockException {
		PdfTableBlock block = PdfBlockGenerator.generateTextBlocks(text);
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();
		blocks.add(block);

		PdfPTable questionTable = generateTable(textColumnRatios, blocks);
		questionTable.getDefaultCell().setBorder(BORDER);
		questionTable.setKeepTogether(true);
		questionTable.setSpacingAfter(QUESTION_TABLE_PADDING);
		return questionTable;
	}
}
