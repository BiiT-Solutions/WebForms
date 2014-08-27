package com.biit.webforms.pdfgenerator;

import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Question;
import com.lowagie.text.pdf.PdfPCell;

public class PdfPCellGenerator {

	public static PdfPCell generateLabelCell(TreeObject object) {
		return new PdfPCell(ParagraphGenerator.generateLabelParagraph(object));
	}

	public static PdfPCell generateNameCell(TreeObject object) {
		return new PdfPCell(ParagraphGenerator.generateNameParagraph(object));
	}

	public static PdfPCell generateAnswerFormatParagraph(Question question) {
		return new PdfPCell(ParagraphGenerator.generateAnswerFormatParagraph(question));
	}

}
