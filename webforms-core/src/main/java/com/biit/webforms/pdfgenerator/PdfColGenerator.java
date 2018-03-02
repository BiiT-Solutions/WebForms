package com.biit.webforms.pdfgenerator;

import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.Question;
import com.lowagie.text.pdf.PdfPCell;

/**
 * Class to encapsulate in a regular column elements of the form.
 *
 */
public class PdfColGenerator {

	private static final int RADIO_FIELD_NAME_COL = 1;

	public static PdfCol generateMultiFieldNameColumn(Question question,int rowSpan) throws BadBlockException {

		PdfCol nameCol = new PdfCol(rowSpan, RADIO_FIELD_NAME_COL);
		
		PdfPCell nameCell = PdfPCellGenerator.generateFormQuestionNameCell(question);
		nameCell.setRowspan(rowSpan);
		nameCol.setCell(nameCell);
		
		return nameCol;
	}

}
