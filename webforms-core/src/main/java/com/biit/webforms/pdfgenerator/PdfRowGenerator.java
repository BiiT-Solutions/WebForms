package com.biit.webforms.pdfgenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.AttachedFiles;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.ElementWithDescription;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.lowagie.text.pdf.BaseField;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RadioCheckField;

/**
 * Utility class to generate pdf row elements.
 *
 */
public class PdfRowGenerator {

	protected static final int RADIO_FIELD_ROW = 1;
	private static final int RADIO_FIELD_COL = 1;
	private static int TEXT_FIELD_ROW = 1;
	private static int TEXT_FIELD_COL = 2;
	private final static float PADDING = 20;
	private static final int ANSWER_DESCRIPTION_ROW = 1;
	private static final int ANSWER_DESCRIPTION_COL = 1;
	private static final int CHECK_FIELD_ROW = 1;
	private static final int CHECK_FIELD_COL = 1;
	private static final int QUESTION_DESCRIPTION_ROW = 1;
	private static final int QUESTION_DESCRIPTION_COL = 2;

	public static PdfRow generateAnnexAnswer(BaseAnswer answer) {
		PdfPCell labelCell = PdfPCellGenerator.generateLabelCell(answer);
		labelCell.setColspan(2);
		PdfPCell nameCell = PdfPCellGenerator.generateNameCell(answer);
		nameCell.setColspan(2);

		// Annex answer have one column less than the questions.
		PdfRow answerRow = new PdfRow(PdfBlockGenerator.ANNEX_QUESTION_ROWS, PdfBlockGenerator.ANNEX_COLS - 1);
		try {
			answerRow.addCell(labelCell);
			answerRow.addCell(nameCell);
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return answerRow;
	}

	public static PdfRow generateAnnexQuestion(Question question) {
		PdfRow row = new PdfRow(PdfBlockGenerator.ANNEX_QUESTION_ROWS, PdfBlockGenerator.ANNEX_COLS);

		PdfPCell cellLabel = PdfPCellGenerator.generateLabelCell(question);
		PdfPCell cellName = PdfPCellGenerator.generateNameCell(question);

		try {
			row.addCell(cellLabel);
			row.addCell(cellName);
			cellName.setColspan(2);
			PdfPCell cellAnswerFormat = PdfPCellGenerator.generateAnswerFormatParagraph(question);
			row.addCell(cellAnswerFormat);
			PdfPCell cellAnswerSubformat = PdfPCellGenerator.generateAnswerSubformatParagraph(question);
			row.addCell(cellAnswerSubformat);

		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}

		return row;
	}

	public static PdfRow generateAnnexQuestion(Text infoText) {
		PdfRow row = new PdfRow(PdfBlockGenerator.ANNEX_QUESTION_ROWS, PdfBlockGenerator.ANNEX_COLS);

		PdfPCell cellLabel = PdfPCellGenerator.generateLabelCell(infoText);
		PdfPCell cellName = PdfPCellGenerator.generateNameCell(infoText);

		try {
			row.addCell(cellLabel);
			cellName.setColspan(4);
			row.addCell(cellName);
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}

		return row;
	}

	public static PdfRow generateTextFieldRow(PdfWriter writer, Question question) throws BadBlockException {
		PdfRow row = new PdfRow(TEXT_FIELD_ROW, TEXT_FIELD_COL);
		row.addCell(PdfPCellGenerator.generateFormQuestionNameCell(question));
		row.addCell(PdfPCellGenerator.generateInputFieldCell(writer, question));
		return row;
	}

	public static PdfRow generateAttachedFilesRow(PdfWriter writer, AttachedFiles attachedFiles) throws BadBlockException {
		PdfRow row = new PdfRow(TEXT_FIELD_ROW, TEXT_FIELD_COL);
		row.addCell(PdfPCellGenerator.generateNameCell(attachedFiles));
		row.addCell(PdfPCellGenerator.generateAttachedFilesCell(writer, attachedFiles));
		return row;
	}

	/**
	 * Generates the list of rows needed to generate a Radio button field
	 * 
	 * @param writer
	 * @param question
	 * @return List of PdfRow of sizes RADIO_FIELD_ROW = 1; RADIO_FIELD_COL = 1
	 * @throws BadBlockException
	 */
	public static List<PdfRow> generateRadioFieldRows(PdfWriter writer, Question question) throws BadBlockException {
		List<PdfRow> rows = new ArrayList<PdfRow>();

		RadioCheckField bt = new RadioCheckField(writer, null, question.getComparationId(), "");
		bt.setCheckType(RadioCheckField.TYPE_CIRCLE);
		bt.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
		bt.setBorderColor(Color.black);
		bt.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
		PdfFormField radioGroup = bt.getRadioGroup(false, true);

		for (TreeObject answer : question.getChildren()) {
			List<PdfRow> newRows = generateRadioFieldRows(writer, radioGroup, question, (BaseAnswer) answer);
			rows.addAll(newRows);
		}

		return rows;
	}

	public static List<PdfRow> generateRadioFieldRows(PdfWriter writer, PdfFormField radioGroup, Question question, BaseAnswer baseAnswer)
			throws BadBlockException {
		List<PdfRow> rows = new ArrayList<PdfRow>();

		PdfRow row = new PdfRow(RADIO_FIELD_ROW, RADIO_FIELD_COL);

		String answerText = null;
		if (baseAnswer instanceof Answer) {
			answerText = baseAnswer.getLabel();
		} else {
			answerText = ((DynamicAnswer) baseAnswer).getReferenceName();
		}

		PdfPCell field = PdfPCellGenerator.generateText(answerText, RADIO_FIELD_COL);
		// Its parent it's not an answer then they are first level. If not they
		// are subanswers.
		if (!(baseAnswer.getParent() instanceof Answer)) {
			field.setPaddingLeft(PADDING);
			field.setCellEvent(new FormRadioField(writer, question.getComparationId(), baseAnswer.getComparationId(), radioGroup, 0));
		} else {
			field.setPaddingLeft(PADDING * 2);
			field.setCellEvent(new FormRadioField(writer, question.getComparationId(), baseAnswer.getComparationId(), radioGroup, PADDING));
		}
		field.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
		row.addCell(field);
		rows.add(row);

		// Add answer description if it has
		if (baseAnswer instanceof Answer) {
			Answer answer = (Answer) baseAnswer;
			if (answer.getDescription() != null && !answer.getDescription().isEmpty()) {
				rows.add(generateAnswerDescriptionRow(answer));
			}
		}

		for (TreeObject subanswer : baseAnswer.getChildren()) {
			List<PdfRow> newRows = generateRadioFieldRows(writer, radioGroup, question, (Answer) subanswer);
			rows.addAll(newRows);
		}

		return rows;
	}

	/**
	 * Generates the list of rows needed to generate a Check Field
	 * 
	 * @param writer
	 * @param question
	 * @return List of PdfRow of sizes CHECK_FIELD_ROW = 1; CHECK_FIELD_COL = 1
	 * @throws BadBlockException
	 */
	public static List<PdfRow> generateCheckFieldRows(PdfWriter writer, Question question) throws BadBlockException {
		List<PdfRow> rows = new ArrayList<PdfRow>();

		for (TreeObject answer : question.getChildren()) {
			List<PdfRow> newRows = generateCheckFieldRows(writer, question, (BaseAnswer) answer);
			rows.addAll(newRows);
		}
		return rows;
	}

	public static List<PdfRow> generateCheckFieldRows(PdfWriter writer, Question question, BaseAnswer baseAnswer) throws BadBlockException {
		List<PdfRow> rows = new ArrayList<PdfRow>();

		PdfRow row = new PdfRow(CHECK_FIELD_ROW, CHECK_FIELD_COL);

		String answerText = null;
		if (baseAnswer instanceof Answer) {
			answerText = baseAnswer.getLabel();
		} else {
			answerText = ((DynamicAnswer) baseAnswer).getReferenceName();
		}

		PdfPCell field = PdfPCellGenerator.generateText(answerText, CHECK_FIELD_COL);
		if (!(baseAnswer.getParent() instanceof Answer)) {
			field.setPaddingLeft(PADDING);
			field.setCellEvent(new FormCheckField(writer, baseAnswer.getComparationId(), 0));
		} else {
			field.setPaddingLeft(PADDING * 2);
			field.setCellEvent(new FormCheckField(writer, baseAnswer.getComparationId(), PADDING));
		}
		field.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
		row.addCell(field);
		rows.add(row);

		if (baseAnswer instanceof Answer) {
			Answer answer = (Answer) baseAnswer;
			// Add answer description if it has
			if (answer.getDescription() != null && !answer.getDescription().isEmpty()) {
				rows.add(generateAnswerDescriptionRow(answer));
			}
		}

		for (TreeObject subanswer : baseAnswer.getChildren()) {
			List<PdfRow> newRows = generateCheckFieldRows(writer, question, (Answer) subanswer);
			rows.addAll(newRows);
		}

		return rows;
	}

	private static PdfRow generateAnswerDescriptionRow(Answer answer) throws BadBlockException {
		PdfRow answerDescriptionRow = new PdfRow(ANSWER_DESCRIPTION_ROW, ANSWER_DESCRIPTION_COL);

		PdfPCell descriptionField = PdfPCellGenerator.generateDescription(answer.getDescription(), ANSWER_DESCRIPTION_COL);
		if (answer.getChildren().isEmpty()) {
			descriptionField.setPaddingLeft(PADDING);
		} else {
			descriptionField.setPaddingLeft(PADDING * 2);
		}
		descriptionField.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);

		answerDescriptionRow.addCell(descriptionField);
		return answerDescriptionRow;
	}

	public static PdfRow generateQuestionDescription(ElementWithDescription question) throws BadBlockException {
		PdfRow row = new PdfRow(QUESTION_DESCRIPTION_ROW, QUESTION_DESCRIPTION_COL);
		row.addCell(PdfPCellGenerator.generateDescription(question.getDescription(), QUESTION_DESCRIPTION_COL));
		return row;
	}

	public static PdfRow generateIsHorizontalBlock(int rowSpan, int colSpan) throws BadBlockException {
		PdfRow row = new PdfRow(rowSpan, colSpan);
		row.addCell(PdfPCellGenerator.generateDescription("Note: Horizontal Layout.", colSpan));
		return row;
	}

	public static PdfRow createTextRow(String description, int textBlockRow, int textBlockCol) throws BadBlockException {
		PdfRow row = new PdfRow(textBlockRow, textBlockCol);
		row.addCell(PdfPCellGenerator.generateText(description, textBlockCol));
		return row;
	}

	public static PdfRow generateSelectionListRow(PdfWriter writer, Question question, int selectionListBlockRow, int selectionListBlockCol)
			throws BadBlockException {
		PdfRow row = new PdfRow(selectionListBlockRow, selectionListBlockCol);
		row.addCell(PdfPCellGenerator.generateFormQuestionNameCell(question));
		row.addCell(PdfPCellGenerator.generateComboBoxQuestion(writer, question));
		return row;
	}
}
