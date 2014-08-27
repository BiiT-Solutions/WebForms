package com.biit.webforms.pdfconversor;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Question;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;

public class ParagraphGenerator {

	private final static float PADDING = 20;

	public static Paragraph generate(String content, PdfFont font, PdfAlign align, float spacing)
			throws DocumentException {
		Paragraph annexTitle = new Paragraph(content, font.getFont());
		annexTitle.setAlignment(align.getAlignment());
		annexTitle.setSpacingAfter(spacing);
		return annexTitle;
	}

	public static void generateAndAddTitle(Document document, String content, PdfAlign align) throws DocumentException {
		document.add(generate(content, PdfFont.FORM_TITLE_FONT, align, PADDING));
	}

	public static void generateAndAddSubtitle(Document document, String content, PdfAlign align)
			throws DocumentException {
		document.add(generate(content, PdfFont.SUBTITLE_FONT, align, PADDING * 2));
	}

	public static void generateAndAddFormTitle(Document document, String content, PdfAlign align)
			throws DocumentException {
		Paragraph title = new Paragraph(new Paragraph(content, PdfFont.FORM_TITLE_FONT.getFont()));
		title.setAlignment(align.getAlignment());
		title.setSpacingAfter(PADDING);
		document.add(title);
	}

	public static void generateAndAddAnnexTitle(Document document, String content, PdfAlign align)
			throws DocumentException {
		document.add(generate(content, PdfFont.ANNEX_TITLE_FONT, align, PADDING));
	}

	public static Paragraph generateLabelParagraph(TreeObject object) {
		return new Paragraph(object.getLabel(), PdfFont.NORMAL_FONT.getFont());
	}

	public static Paragraph generateNameParagraph(TreeObject object) {
		return new Paragraph("<" + object.getName() + ">", PdfFont.SMALL_FONT.getFont());
	}

	public static Paragraph generateAnswerFormatParagraph(Question object) {
		// TODO string text
		return new Paragraph("(" + object.getAnswerFormat().toString() + ")", PdfFont.SMALL_FONT.getFont());
	}

	public static Paragraph generateTextParagraph(String textParagraph){
		Paragraph paragraph = new Paragraph(textParagraph, PdfFont.NORMAL_FONT.getFont());
		paragraph.setAlignment(PdfAlign.ALIGN_JUSTIFIED.getAlignment());
		return paragraph;
	}

	public static List<Paragraph> generateTextParagraphs(List<String> textParagraphs) {
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		for(String text : textParagraphs){
			paragraphs.add(generateTextParagraph(text));
		}
		return paragraphs;
	}
}
