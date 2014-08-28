package com.biit.webforms.pdfgenerator;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.lowagie.text.pdf.PdfPCell;

public class PdfCol implements IPdfTableBlock{
	
	private int numberRows;
	private int numberCols;

	private List<PdfPCell> cells;

	public PdfCol(int numberRows,int numberCols) {
		this.numberRows = numberRows;
		this.numberCols = numberCols;
		cells = new ArrayList<PdfPCell>();
	}

	public void setCell(PdfPCell cell) throws BadBlockException {
		if(cell.getRowspan()!=numberRows || cell.getColspan() !=numberCols){
			throw new BadBlockException();
		}else{
			cells.clear();
			cells.add(cell);
		}
	}
	
	public boolean isWellFormatted(){
		return (!cells.isEmpty()) && cells.get(0).getRowspan()==numberRows && cells.get(0).getColspan() ==numberCols;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public int getNumberCols() {
		return numberCols;
	}
	
	@Override
	public List<PdfPCell> getCells() {
		return cells;
	}
	
	public static PdfCol generateWhiteCol(int numberRows,int numberCols){
		PdfCol col = new PdfCol(numberRows, numberCols);
		PdfPCell cell= new PdfPCell();
		cell.setRowspan(numberRows);
		cell.setColspan(numberCols);
		try {
			col.setCell(cell);
		} catch (BadBlockException e) {
			// Impossible
			WebformsLogger.errorMessage(PdfCol.class.getName(), e);
		}
		return col;		
	}
}
