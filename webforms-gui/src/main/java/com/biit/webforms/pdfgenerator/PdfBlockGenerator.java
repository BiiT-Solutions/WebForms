package com.biit.webforms.pdfgenerator;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;

public class PdfBlockGenerator {

	public static PdfTableBlock generateAnnexQuestionTableBlock(Question question){
		PdfTableBlock block = null;
		try {
			block = new PdfTableBlock(1 + question.getChildren().size(),4);
			
			block.insertRow(PdfRowGenerator.generateAnnexQuestion(question));

			if (!question.getChildren().isEmpty()) {
				block.insertCol(PdfCol.generateWhiteCol(question.getChildren().size(), 1));
				for (TreeObject child : question.getChildren()) {
					// They are all answers
					//TODO change for nested answers.
					block.insertRow(PdfRowGenerator.generateAnnexAnswer((Answer) child));
				}
			}
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
		}
		return block;
		//TODO clean when finished.
		// if (!element.getChildren().isEmpty()) {
		// PdfPCell cellWhite = new PdfPCell();
		// cellWhite.setRowspan(element.getChildren().size());
		// elementTable.addCell(cellWhite);
		// }
		// String prevChildNameCat = null;
		// for (TreeObject child : element.getChildren()) {
		// List<String> childName = Arrays.asList(((ElementAnswer)
		// child).getLabel().split("->"));
		// if (childName.size() == 1 || element.getAnswerType() ==
		// AnswerType.SELECT) {
		// // If its a selection IT SHOULD NOT HAVE sub elements.
		// PdfPCell childLabel = new PdfPCell(new
		// Paragraph(childName.get(0), NORMAL_FONT));
		// childLabel.setColspan(2);
		// elementTable.addCell(childLabel);
		// elementTable.addCell(new Paragraph(((ElementAnswer)
		// child).getName(), SMALL_FONT));
		// } else {
		// if (prevChildNameCat == null ||
		// !prevChildNameCat.equals(childName.get(0))) {
		// PdfPCell childLabel1 = new PdfPCell(new
		// Paragraph(childName.get(0), NORMAL_FONT));
		// elementTable.addCell(childLabel1);
		// } else {
		// PdfPCell childLabel1 = new PdfPCell();
		// elementTable.addCell(childLabel1);
		// }
		// PdfPCell childLabel2 = new PdfPCell(new
		// Paragraph(childName.get(1), NORMAL_FONT));
		// elementTable.addCell(childLabel2);
		// elementTable.addCell(new Paragraph(((ElementAnswer)
		// child).getName(), SMALL_FONT));
		// prevChildNameCat = childName.get(0);
		// }
		// }
	}
	
	public static List<PdfTableBlock>  generateAnnexFormTableBlocks(Form form){
		List<PdfTableBlock> blocks = new ArrayList<PdfTableBlock>();

		List<TreeObject> treeObjects = new ArrayList<>(form.getAll(Question.class));

		for (TreeObject object : treeObjects) {
			blocks.add(generateAnnexQuestionTableBlock((Question) object));
		}

		return blocks;
	}
	
}
