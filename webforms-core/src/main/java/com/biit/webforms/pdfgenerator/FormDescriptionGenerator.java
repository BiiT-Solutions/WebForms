package com.biit.webforms.pdfgenerator;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.Arrays;
import java.util.List;

import com.biit.utils.date.DateManager;
import com.biit.webforms.persistence.entity.Form;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

/**
 * Generator for Form Description.
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
