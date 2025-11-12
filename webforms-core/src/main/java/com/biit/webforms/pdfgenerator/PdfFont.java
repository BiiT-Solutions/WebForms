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

import com.lowagie.text.Font;

import java.awt.Color;

/**
 * Enum with definitions of the forms used for the generation of the pdf.
 *
 */
public enum PdfFont {

	FORM_TITLE_FONT(new Font(Font.TIMES_ROMAN, 30, Font.BOLD | Font.UNDERLINE)),

	ANNEX_TITLE_FONT(new Font(Font.TIMES_ROMAN, 14, Font.BOLD | Font.UNDERLINE)),

	TITLE_FONT(new Font(Font.TIMES_ROMAN, 22, Font.BOLD | Font.UNDERLINE)),

	SUBTITLE_FONT(new Font(Font.TIMES_ROMAN, 14, Font.BOLD)),

	NORMAL_FONT(new Font(Font.TIMES_ROMAN, 12)),

	BOLD_FONT(new Font(Font.TIMES_ROMAN, 12, Font.BOLD)),

	SMALL_BOLD_FONT(new Font(Font.TIMES_ROMAN, 10, Font.BOLD)),

	DESCRIPTION_FONT(new Font(Font.TIMES_ROMAN, 10, Font.ITALIC, Color.GRAY)),

	SMALL_FONT(new Font(Font.TIMES_ROMAN, 8, Font.ITALIC)),

	;

	private Font font;

	private PdfFont(Font font) {
		this.font = font;
	}

	public Font getFont() {
		return font;
	}

}
