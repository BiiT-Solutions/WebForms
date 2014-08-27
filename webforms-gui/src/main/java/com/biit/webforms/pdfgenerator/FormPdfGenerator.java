package com.biit.webforms.pdfgenerator;

import com.biit.webforms.gui.common.utils.DateManager;
import com.biit.webforms.persistence.entity.Form;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

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

		// for (TreeObject children : formData.getChildren()) {
		// generate(document, children);
		// }

		document.newPage();

		// Annex page generation
		AnnexGenerator.generateAndAdd(document, form);

	}

}
