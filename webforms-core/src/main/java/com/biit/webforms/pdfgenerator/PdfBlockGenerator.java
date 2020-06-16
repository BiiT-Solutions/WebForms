package com.biit.webforms.pdfgenerator;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.AttachedFiles;
import com.biit.webforms.persistence.entity.ElementWithDescription;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This class generates PdfTableBlock from different kinds of elements in the
 * form
 *
 */
public class PdfBlockGenerator {

	private static final int DESCRIPTION_BLOC_ROW = 1;
	private static final int DESCRIPTION_BLOC_COL = 2;
	private static final int IS_HORIZONTAL_BLOC_ROW = 1;
	private static final int IS_HORIZONTAL_BLOC_COL = 2;
	private static final int TEXT_BLOCK_ROW = 1;
	private static final int TEXT_BLOCK_COL = 1;
	private static final int FORM_LIST_ROW = 1;
	private static final int FORM_LIST_COL = 2;
	public static final int ANNEX_COLS = 5;
	public static final int ANNEX_QUESTION_ROWS = 1;

	private static final int FORM_SINGLE_SELECTION_ROW = 1;
	private static final int FORM_QUESTION_COLUMN = 2;

	public static PdfTableBlock generateAnnexQuestionTableBlock(Question question) {
		PdfTableBlock block = null;
		try {
			block = new PdfTableBlock(ANNEX_QUESTION_ROWS + question.getAllChildrenInHierarchy(BaseAnswer.class).size(),
					ANNEX_COLS);

			block.insertRow(PdfRowGenerator.generateAnnexQuestion(question));

			if (!question.getChildren().isEmpty()) {
				block.insertColumn(
						PdfCol.generateWhiteCol(question.getAllChildrenInHierarchy(BaseAnswer.class).size(), 1));
				for (TreeObject child : question.getChildren()) {
					// They are all answers
					block.insertRow(PdfRowGenerator.generateAnnexAnswer((BaseAnswer) child));
					for (TreeObject subChild : child.getChildren()) {
						block.insertRow(PdfRowGenerator.generateAnnexAnswer((BaseAnswer) subChild));
					}
				}
			}
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return block;
	}

	public static PdfTableBlock generateAnnexQuestionTableBlock(Text infoText) {
		PdfTableBlock block = null;
		try {
			block = new PdfTableBlock(ANNEX_QUESTION_ROWS, ANNEX_COLS);
			block.insertRow(PdfRowGenerator.generateAnnexQuestion(infoText));
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return block;
	}

	public static PdfTableBlock generateAnnexQuestionTableBlock(AttachedFiles attachedFiles) {
		PdfTableBlock block = null;
		try {
			block = new PdfTableBlock(ANNEX_QUESTION_ROWS, ANNEX_COLS);
			block.insertRow(PdfRowGenerator.generateAnnexQuestion(attachedFiles));
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return block;
	}

	public static List<PdfTableBlock> generateAnnexFormTableBlocks(Form form) {
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();

		List<BaseQuestion> treeObjects = new ArrayList<>(form.getAll(BaseQuestion.class));

		for (BaseQuestion object : treeObjects) {
			if (!object.isHiddenElement() && !(object instanceof SystemField)) {
				if (object instanceof Text) {
					blocks.add(generateAnnexQuestionTableBlock((Text) object));
				} else if (object instanceof AttachedFiles) {
					blocks.add(generateAnnexQuestionTableBlock((AttachedFiles) object));
				} else if (object instanceof Question) {
					blocks.add(generateAnnexQuestionTableBlock((Question) object));
				} else {
					WebformsLogger.warning(PdfBlockGenerator.class.getName(), "Ignoring element '" + object + "'.");
				}
			}
		}

		return blocks;
	}

	public static PdfTableBlock generateFormQuestionElement(PdfWriter writer, Question question)
			throws BadBlockException {
		switch (question.getAnswerType()) {
		case MULTIPLE_SELECTION:
			return generateMultipleSelectionBlock(writer, question);
		case SINGLE_SELECTION_RADIO:
		case SINGLE_SELECTION_SLIDER:
			return generateSingleSelectionBlock(writer, question);
		case SINGLE_SELECTION_LIST:
			return generateSingleSelectionListBlock(writer, question);
		case INPUT:
		case TEXT_AREA:
			return generateInputFieldBlock(writer, question);
		}
		return null;
	}

	private static PdfTableBlock generateAttachedFilesdBlock(PdfWriter writer, AttachedFiles attachedFiles)
			throws BadBlockException {
		PdfTableBlock block = new PdfTableBlock(FORM_SINGLE_SELECTION_ROW, FORM_QUESTION_COLUMN);
		block.insertRow(PdfRowGenerator.generateAttachedFilesRow(writer, attachedFiles));
		return block;
	}

	private static PdfTableBlock generateInputFieldBlock(PdfWriter writer, Question question) throws BadBlockException {
		PdfTableBlock block = new PdfTableBlock(FORM_SINGLE_SELECTION_ROW, FORM_QUESTION_COLUMN);
		block.insertRow(PdfRowGenerator.generateTextFieldRow(writer, question));
		return block;
	}

	private static PdfTableBlock generateSingleSelectionBlock(PdfWriter writer, Question question)
			throws BadBlockException {
		List<PdfRow> rows = PdfRowGenerator.generateRadioFieldRows(writer, question);

		int numberRows = getRowSizeOfRows(rows);
		PdfTableBlock block = new PdfTableBlock(getRowSizeOfRows(rows), FORM_QUESTION_COLUMN);

		// C|r
		// --
		// |r
		// Insert name column
		block.insertColumn(PdfColGenerator.generateMultiFieldNameColumn(question, numberRows));
		// Insert option rows
		for (PdfRow row : rows) {
			block.insertRow(row);
		}
		return block;
	}

	private static PdfTableBlock generateSingleSelectionListBlock(PdfWriter writer, Question question)
			throws BadBlockException {
		PdfRow row = PdfRowGenerator.generateSelectionListRow(writer, question, FORM_LIST_ROW, FORM_LIST_COL);

		int numberRows = row.getNumberRows();
		PdfTableBlock block = new PdfTableBlock(numberRows, FORM_LIST_COL);
		block.insertRow(row);

		return block;
	}

	private static PdfTableBlock generateMultipleSelectionBlock(PdfWriter writer, Question question)
			throws BadBlockException {
		List<PdfRow> rows = PdfRowGenerator.generateCheckFieldRows(writer, question);

		int numberRows = getRowSizeOfRows(rows);
		PdfTableBlock block = new PdfTableBlock(numberRows, FORM_QUESTION_COLUMN);

		// C|r
		// --
		// |r
		// Insert name column
		block.insertColumn(PdfColGenerator.generateMultiFieldNameColumn(question, numberRows));
		// Insert option rows
		for (PdfRow row : rows) {
			block.insertRow(row);
		}
		return block;
	}

	public static List<PdfTableBlock> generateFormQuestionTableBlocks(PdfWriter writer, Question question)
			throws BadBlockException {
		List<PdfTableBlock> tableBlocks = new ArrayList<PdfTableBlock>();

		tableBlocks.add(generateFormQuestionElement(writer, question));

		// Add description to document.
		if (question.getDescription() != null && !question.getDescription().isEmpty()) {
			tableBlocks.add(generateQuestionDescriptionBlock(question));
		}

		// Add visibility cell.
		if (question.isHorizontal()) {
			tableBlocks.add(generateIsHorizontalBlock());
		}

		return tableBlocks;
	}

	public static List<PdfTableBlock> generateFormAttachedFilesTableBlocks(PdfWriter writer,
			AttachedFiles attachedFiles) throws BadBlockException {
		List<PdfTableBlock> tableBlocks = new ArrayList<PdfTableBlock>();

		tableBlocks.add(generateAttachedFilesdBlock(writer, attachedFiles));

		// Add description to document.
		if (attachedFiles.getDescription() != null && !attachedFiles.getDescription().isEmpty()) {
			tableBlocks.add(generateQuestionDescriptionBlock(attachedFiles));
		}

		return tableBlocks;
	}

	private static PdfTableBlock generateIsHorizontalBlock() throws BadBlockException {
		PdfTableBlock block = new PdfTableBlock(IS_HORIZONTAL_BLOC_ROW, IS_HORIZONTAL_BLOC_COL);
		block.insertRow(PdfRowGenerator.generateIsHorizontalBlock(IS_HORIZONTAL_BLOC_ROW, IS_HORIZONTAL_BLOC_COL));
		return block;

	}

	private static PdfTableBlock generateQuestionDescriptionBlock(ElementWithDescription question)
			throws BadBlockException {
		PdfTableBlock block = new PdfTableBlock(DESCRIPTION_BLOC_ROW, DESCRIPTION_BLOC_COL);
		block.insertRow(PdfRowGenerator.generateQuestionDescription(question));
		return block;
	}

	private static int getRowSizeOfRows(List<PdfRow> rows) {
		int size = 0;
		for (PdfRow row : rows) {
			size += row.getNumberRows();
		}
		return size;
	}

	public static PdfTableBlock generateTextBlocks(Text text) throws BadBlockException {
		PdfTableBlock block = new PdfTableBlock(TEXT_BLOCK_ROW, TEXT_BLOCK_COL);
		block.insertRow(PdfRowGenerator.createTextRow(text.getDescription(), TEXT_BLOCK_ROW, TEXT_BLOCK_COL));
		return block;
	}
}
