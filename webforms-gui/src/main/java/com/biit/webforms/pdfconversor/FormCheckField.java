package com.biit.webforms.pdfconversor;

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
