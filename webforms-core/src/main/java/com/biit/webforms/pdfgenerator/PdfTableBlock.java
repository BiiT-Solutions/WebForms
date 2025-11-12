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

import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.lowagie.text.pdf.PdfPCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Container to handle the composition of row and col elements to ensure they
 * are always a consistent block.
 */
public class PdfTableBlock implements IPdfTableBlock {

    private int numberCols;
    private int numberRows;

    private List<IPdfTableBlock> tableBlocks;

    public PdfTableBlock(int numberRows, int numberCols) {
        this.numberCols = numberCols;
        this.numberRows = numberRows;
        tableBlocks = new ArrayList<>();
    }

    public void insertRow(PdfRow row) throws BadBlockException {
        if (!row.isWellFormatted() || (row.getNumberCols() != getRemainingCols()) || (row.getNumberRows() > getRemainingRows())) {
            throw new BadBlockException();
        }
        tableBlocks.add(row);
    }

    public void insertColumn(PdfCol col) throws BadBlockException {
        if (!col.isWellFormatted() || (col.getNumberRows() != getRemainingRows()) || (col.getNumberCols() > getRemainingCols())) {
            throw new BadBlockException();
        }
        tableBlocks.add(col);
    }

    public int getNumberCols() {
        return numberCols;
    }

    public int getNumberRows() {
        return numberRows;
    }

    private int getRemainingCols() {
        int remainingCols = numberCols;
        for (IPdfTableBlock block : tableBlocks) {
            if (block instanceof PdfCol) {
                remainingCols -= block.getNumberCols();
            }
        }
        return remainingCols;
    }

    private int getRemainingRows() {
        if (numberRows < 0) {
            return Integer.MAX_VALUE;
        }
        int remainingRows = numberRows;
        for (IPdfTableBlock block : tableBlocks) {
            if (block instanceof PdfRow) {
                remainingRows -= block.getNumberRows();
            }
        }
        return remainingRows;
    }

    @Override
    public boolean isWellFormatted() {
        return (getRemainingCols() == 0) || (getRemainingRows() == 0);
    }

    @Override
    public List<PdfPCell> getCells() {
        List<PdfPCell> cells = new ArrayList<PdfPCell>();
        for (IPdfTableBlock block : tableBlocks) {
            cells.addAll(block.getCells());
        }
        return cells;
    }
}
