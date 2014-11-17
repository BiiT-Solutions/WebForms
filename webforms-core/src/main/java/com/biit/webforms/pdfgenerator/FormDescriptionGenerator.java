package com.biit.webforms.pdfgenerator;

import java.util.Arrays;
import java.util.List;

import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.DateManager;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

/**
 * Generator for Form Description.
 * @author joriz_000
 *
 */
public class FormDescriptionGenerator extends DocumentGenerator{
	
	private Form form;
	
	public FormDescriptionGenerator(Form form) {
		this.form = form;
	}

	@Override
	protected void generateDocumentContent(Document document) throws DocumentException {
		ParagraphGenerator.generateAndAddTitle(document, form.getLabel(), PdfAlign.ALIGN_CENTER);

		String updateDate = DateManager.convertDateToString(form.getUpdateTime());
		ParagraphGenerator.generateAndAddSubtitle(document, "Version: " + form.getVersion() + " - " + updateDate,
				PdfAlign.ALIGN_CENTER);

		List<String> textParagraphs = Arrays.asList(form.getDescription().split("\n"));
		for (String textParagraph : textParagraphs) {
			if (textParagraph.length() > 0) {
				document.add(ParagraphGenerator.generateTextParagraph(textParagraph));
			} else {
				document.add(Chunk.NEWLINE);
			}
		}
	}

}
