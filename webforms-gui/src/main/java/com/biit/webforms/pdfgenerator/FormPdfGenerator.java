package com.biit.webforms.pdfgenerator;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.utils.DateManager;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.Subcategory;
import com.biit.webforms.persistence.entity.Text;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;

public class FormPdfGenerator extends DocumentGenerator {

	private Form form;

	public FormPdfGenerator(Form form) {
		this.form = form;

		FormPageEvent formPageEvent = new FormPageEvent();
		formPageEvent.setHeader(form.getName());

		setPageEvent(formPageEvent);

	}

	@Override
	protected void generateDocumentContent(Document document) throws DocumentException {

		ParagraphGenerator.generateAndAddFormTitle(document, form.getName(), PdfAlign.ALIGN_CENTER);
		String updateDate = DateManager.convertDateToString(form.getUpdateTime());
		ParagraphGenerator.generateAndAddSubtitle(document, "Version: " + form.getVersion() + " - " + updateDate,
				PdfAlign.ALIGN_CENTER);

		for (TreeObject child : form.getChildren()) {
			generateAndAddCategory(document, (Category) child);
		}

		document.newPage();

		// Annex page generation
		AnnexGenerator.generateAndAdd(document, form);

	}
	
	private void generateAndAddTreeObject(Document document, TreeObject object) throws DocumentException{
		if(object instanceof Category){
			generateAndAddCategory(document, (Category) object);
		}else if (object instanceof Subcategory) {
			generateAndAddSubcategory(document, (Subcategory) object);
		}else if (object instanceof Group) {
			generateAndAddGroup(document, (Group) object);
		}else if (object instanceof Question) {
			generateAndAddQuestion(document, (Question) object);
		}else if (object instanceof Text){
			generateAndAddText(document, (Text) object);
		}else{
			throw new DocumentException("Structure not recognized");
		}
		//TODO add system fields
	}

	private void generateAndAddText(Document document, Text text) throws DocumentException {
		PdfPTable questionTable;
		try {
			questionTable = PdfTableGenerator.generateTextTable(text);
			document.add(questionTable);
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private void generateAndAddQuestion(Document document, Question question) throws DocumentException {
		try {
			PdfPTable questionTable = PdfTableGenerator.generateQuestionTable(getWriter(), question);
			document.add(questionTable);
		} catch (BadBlockException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private void generateAndAddGroup(Document document, Group group) throws DocumentException {
		ParagraphGenerator.generateAndAddSubtitle(document, group.getLabel(), PdfAlign.ALIGN_LEFT);
		if(group.isRepeatable()){
			document.add(ParagraphGenerator.generateRepeatableParagraph());
		}

		for (TreeObject element : group.getChildren()) {
			generateAndAddTreeObject(document, element);
		}
	}

	private void generateAndAddSubcategory(Document document, Subcategory subcategory) throws DocumentException {
		ParagraphGenerator.generateAndAddSubtitle(document, subcategory.getLabel(), PdfAlign.ALIGN_LEFT);

		for (TreeObject element : subcategory.getChildren()) {
			generateAndAddTreeObject(document, element);
		}
	}

	private void generateAndAddCategory(Document document, Category category) throws DocumentException {
		ParagraphGenerator.generateAndAddTitle(document, category.getLabel(), PdfAlign.ALIGN_LEFT);

		for (TreeObject child : category.getChildren()) {
			generateAndAddTreeObject(document,child);
		}
	}

}
