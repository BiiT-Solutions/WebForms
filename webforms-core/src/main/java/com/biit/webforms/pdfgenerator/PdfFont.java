package com.biit.webforms.pdfgenerator;

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
