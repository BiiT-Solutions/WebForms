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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

/**
 * This class serves to generate a pdf document. Stores the default
 * configuration of the pdf page.
 *
 */
public abstract class DocumentGenerator {
	private static final float MARGIN_LEFT = 50;
	private static final float MARGIN_RIGHT = 50;
	private static final float MARGIN_TOP = 75;
	private static final float MARGIN_BOTTON = 50;

	private static final String DEFAULT_DOCUMENT_NAME = "Form document PDF";
	private static final String DEFAULT_DOCUMENT_SUBJECT = "Form document PDF";
	private static final String DOCUMENT_AUTHOR = "BiiT";
	private static final String DOCUMENT_CREATOR = "BiiT";

	private PdfPageEvent pageEvent;
	private PdfWriter writer;

	protected static void addMetaData(Document document) {
		document.addTitle(DEFAULT_DOCUMENT_NAME);
		document.addSubject(DEFAULT_DOCUMENT_SUBJECT);
		document.addAuthor(DOCUMENT_AUTHOR);
		document.addCreator(DOCUMENT_CREATOR);
	}

	protected static Document generateDocument() {
		return new Document(PageSize.A4, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTON);
	}

	protected abstract void generateDocumentContent(Document document) throws DocumentException;

	public void setPageEvent(PdfPageEvent event) {
		this.pageEvent = event;
	}

	public final byte[] generate() throws DocumentException {
		Document document = generateDocument();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writer = PdfWriter.getInstance(document, baos);

		if (writer == null) {
			return null;
		}

		if (pageEvent != null) {
			writer.setPageEvent(pageEvent);
		}

		document.open();
		addMetaData(document);

		generateDocumentContent(document);

		document.close();
		writer = null;
		return baos.toByteArray();
	}

	public PdfWriter getWriter() {
		return writer;
	}
}
