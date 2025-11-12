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

import java.awt.Color;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseField;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RadioCheckField;

/**
 * Generator for a check field in the pdf. 
 *
 */
public class FormCheckField implements PdfPCellEvent {
	private static final float SIZE = 16.0f;
	private PdfWriter writer;
	private String fieldname;
	private float leftIndent;

	public FormCheckField(PdfWriter writer, String fieldname, float leftIndent) {
		this.writer = writer;
		this.fieldname = fieldname;
		this.leftIndent = leftIndent;
	}

	@Override
	public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {

		float minHeight = Math.min(SIZE, position.getTop() - position.getBottom());
		float yCoordinate = (position.getTop() + position.getBottom())/2.0f;

		Rectangle newPosition = new Rectangle(position.getLeft() + leftIndent, yCoordinate - minHeight/2.0f, position.getLeft() + minHeight	+ leftIndent, yCoordinate + minHeight/2.0f);
		
		RadioCheckField bt = new RadioCheckField(writer, newPosition, fieldname, "Yes");
		bt.setCheckType(RadioCheckField.TYPE_CHECK);
		bt.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
		bt.setBorderColor(Color.BLACK);

		PdfFormField field;
		try {
			field = bt.getCheckField();
			writer.addAnnotation(field);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}
}
