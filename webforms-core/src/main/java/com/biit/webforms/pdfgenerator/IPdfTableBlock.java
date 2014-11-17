package com.biit.webforms.pdfgenerator;

import java.util.List;

import com.lowagie.text.pdf.PdfPCell;

public interface IPdfTableBlock {

	public int getNumberCols();
	public int getNumberRows();
	public boolean isWellFormatted();
	public List<PdfPCell> getCells();
}
