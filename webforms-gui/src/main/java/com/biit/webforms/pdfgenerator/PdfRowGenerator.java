package com.biit.webforms.pdfgenerator;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.AnswerType;
import com.biit.webforms.persistence.entity.Question;
import com.lowagie.text.pdf.PdfPCell;

public class PdfRowGenerator {

	public static PdfRow generateAnnexAnswer(Answer answer) {
		PdfPCell labelCell = PdfPCellGenerator.generateLabelCell(answer);
		labelCell.setColspan(2);
		PdfPCell nameCell = PdfPCellGenerator.generateNameCell(answer);

		PdfRow answerRow = new PdfRow(1, 3);
		try {
			answerRow.addCell(labelCell);
			answerRow.addCell(nameCell);
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return answerRow;
	}

	public static PdfRow generateAnnexQuestion(Question question) {
		PdfRow row = new PdfRow(1, 4);

		PdfPCell cellLabel = PdfPCellGenerator.generateLabelCell(question);
		PdfPCell cellName = PdfPCellGenerator.generateNameCell(question);

		try {
			row.addCell(cellLabel);
			row.addCell(cellName);

			if (question.getAnswerType() == AnswerType.INPUT) {
				cellName.setColspan(2);

				PdfPCell cellAnswerFormat = PdfPCellGenerator.generateAnswerFormatParagraph(question);
				row.addCell(cellAnswerFormat);
			} else {
				cellName.setColspan(3);
			}

		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}

		return row;
	}
}
