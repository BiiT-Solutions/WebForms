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
 * Definition of elements that can occupy several elements in a row
 */
public class PdfRow implements IPdfTableBlock {

    private int numberRows;
    private int numberCols;

    private List<PdfPCell> cells;

    public PdfRow(int numberRows, int numberCols) {
        this.numberRows = numberRows;
        this.numberCols = numberCols;
        cells = new ArrayList<>();
    }

    public void addCell(PdfPCell cell) throws BadBlockException {
        if (cell.getRowspan() != numberRows || ((cell.getColspan() + getCurrentCols()) <= numberCols)) {
            cells.add(cell);
        } else {
            throw new BadBlockException();
        }
    }

    public int getCurrentCols() {
        int currentCols = 0;
        for (PdfPCell cell : cells) {
            currentCols += cell.getColspan();
        }
        return currentCols;
    }

    public int getNumberCols() {
        return numberCols;
    }

    public int getNumberRows() {
        return numberRows;
    }

    @Override
    public boolean isWellFormatted() {
        return (getCurrentCols() == numberCols) && (!cells.isEmpty());
    }

    @Override
    public List<PdfPCell> getCells() {
        return cells;
    }
}
