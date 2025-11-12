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

import com.biit.form.entity.TreeObject;
import com.biit.webforms.persistence.entity.Form;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;

/**
 * Utility method to generate the form annex pdf.
 *
 */
public class AnnexGenerator {

	public static void generateAndAdd(Document document, TreeObject treeObject) throws DocumentException {
		ParagraphGenerator.generateAndAddTitle(document, "Appendix A", PdfAlign.ALIGN_CENTER);
		ParagraphGenerator.generateAndAddAnnexTitle(document, "Element List", PdfAlign.ALIGN_CENTER);

		// Generate table
		PdfPTable annexTable = PdfTableGenerator.generateAnnexFormTable((Form) treeObject);

		document.add(annexTable);
	}

}
