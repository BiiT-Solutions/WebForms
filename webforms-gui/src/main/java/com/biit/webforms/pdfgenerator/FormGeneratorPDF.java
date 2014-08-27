package com.biit.webforms.pdfgenerator;


public class FormGeneratorPDF {
//
//	private final static float PADDING = 20;
//	// Document configurable properties
//	private final static float MARGIN_LEFT = 50;
//	private final static float MARGIN_RIGHT = 50;
//	private final static float MARGIN_TOP = 75;
//	private final static float MARGIN_BOTTON = 50;
//	private final static String DEFAULT_DOCUMENT_NAME = "Form document PDF";
//	private final static String DEFAULT_DOCUMENT_SUBJECT = "Form document PDF";
//	private final static String DOCUMENT_AUTHOR = "BiiT";
//	private final static String DOCUMENT_CREATOR = "BiiT";
//	// Document fonts
//	private final static Font FORM_TITLE_FONT = new Font(Font.TIMES_ROMAN, 30, Font.BOLD | Font.UNDERLINE);
//	private final static Font TITLE_FONT = new Font(Font.TIMES_ROMAN, 22, Font.BOLD | Font.UNDERLINE);
//	private final static Font SUBTITLE_FONT = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
//	private final static Font NORMAL_FONT = new Font(Font.TIMES_ROMAN, 12);
//	private final static Font DESCRIPTION_FONT = new Font(Font.TIMES_ROMAN, 10, Font.ITALIC, Color.GRAY);
//	private final static Font SMALL_FONT = new Font(Font.TIMES_ROMAN, 8, Font.ITALIC);
//	private final static Font ANNEX_TITLE_FONT = new Font(Font.TIMES_ROMAN, 14, Font.BOLD | Font.UNDERLINE);
//	private static PdfWriter writer;
//
//	private final static int BORDER = Rectangle.NO_BORDER;
//
//	private static void addBlankRow(PdfPTable table) {
//		PdfPCell row = new PdfPCell();
//		row.setColspan(2);
//		row.setMinimumHeight(15);
//		row.setBorder(BORDER);
//		table.addCell(row);
//	}
//
//	private static void addCheckFieldToTable(PdfPTable table, String fieldName, Paragraph nameDescriptionParagraph,
//			List<ElementAnswer> listElementAnswer) {
//		PdfPCell cell = new PdfPCell(new Phrase(nameDescriptionParagraph));
//		cell.setBorder(BORDER);
//		// Get number of ElementAnswers with groups
//		int numElementAnswerTree = getTotalCountOfElementAnswers(listElementAnswer);
//		cell.setRowspan(numElementAnswerTree);
//		table.addCell(cell);
//
//		List<String> prefix = new ArrayList<String>();
//		for (ElementAnswer answer : listElementAnswer) {
//			if (!prefix.equals(answer.getPrefix())) {
//				if (!answer.getPrefix().isEmpty()) {
//					PdfPCell field = new PdfPCell(new Phrase(answer.getPrefix().get(0)));
//					field.setPaddingLeft(PADDING);
//					field.setBorder(BORDER);
//					field.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
//					field.setCellEvent(new FormCheckField(writer, answer.getName() + "-cat", 0));
//					table.addCell(field);
//				}
//			}
//
//			PdfPCell field = new PdfPCell(new Phrase(answer.getLabelWithoutPrefix()));
//			if (answer.getPrefix().isEmpty()) {
//				field.setPaddingLeft(PADDING);
//				field.setCellEvent(new FormCheckField(writer, answer.getName(), 0));
//			} else {
//				field.setPaddingLeft(PADDING * 2);
//				field.setCellEvent(new FormCheckField(writer, answer.getName(), PADDING));
//			}
//			field.setBorder(BORDER);
//			field.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
//			table.addCell(field);
//			
//			if (answer.getDescription() != null && answer.getDescription().length() > 0) {
//				Paragraph elementDescription = new Paragraph("Hint: " + answer.getDescription(), DESCRIPTION_FONT);
//				PdfPCell descriptionField = new PdfPCell(elementDescription);
//				descriptionField.setBorder(BORDER);
//				if (answer.getPrefix().isEmpty()) {
//					descriptionField.setPaddingLeft(PADDING);
//				} else {
//					descriptionField.setPaddingLeft(PADDING * 2);
//				}
//				descriptionField.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
//				table.addCell(descriptionField);
//			}
//
//			prefix = answer.getPrefix();
//		}
//	}
//
//	private static void addComboBoxToTable(PdfPTable table, String fieldName, Paragraph nameDescriptionParagraph,
//			List<TreeObject> options) {
//		table.addCell(nameDescriptionParagraph);
//		PdfPCell cell = new PdfPCell();
//		cell.setBorder(BORDER);
//		cell.setCellEvent(new FormComboBox(writer, fieldName, options));
//		table.addCell(cell);	
//	}
//
//	private static void addInfoFieldToTable(PdfPTable table, String text) {
//		PdfPCell cell = new PdfPCell(new Phrase(text));
//		cell.setBorder(BORDER);
//		cell.setColspan(2);
//		table.addCell(cell);
//	}
//
//	private static void addMetaData(Document document) {
//		document.addTitle(DEFAULT_DOCUMENT_NAME);
//		document.addSubject(DEFAULT_DOCUMENT_SUBJECT);
//		document.addAuthor(DOCUMENT_AUTHOR);
//		document.addCreator(DOCUMENT_CREATOR);
//	}
//
//	/**
//	 * This function returns the number of elements that exist between real elementAnswers and group names for
//	 * elementAnswers.
//	 * 
//	 * @return
//	 */
//	private static int getTotalCountOfElementAnswers(List<ElementAnswer> options) {
//
//		int numElements = 0;
//
//		List<String> prefix = new ArrayList<String>();
//		for (ElementAnswer answer : options) {
//			if (answer.getPrefix().isEmpty()) {
//				numElements++;
//			} else {
//				if (answer.getPrefix().equals(prefix)) {
//					numElements++;
//				} else {
//					numElements += 2;
//				}
//				prefix = answer.getPrefix();
//			}
//			if (answer.getDescription() != null && answer.getDescription().length() > 0) {
//				numElements++;
//			}
//		}
//
//		return numElements;
//	}
//
//	private static void addRadioFieldToTable(PdfPTable table, String fieldName, Paragraph nameDescriptionParagraph,
//			List<ElementAnswer> listElementAnswer) {
//		RadioCheckField bt = new RadioCheckField(writer, null, fieldName, "");
//		bt.setCheckType(RadioCheckField.TYPE_CIRCLE);
//		bt.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
//		bt.setBorderColor(Color.black);
//		bt.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
//		PdfFormField radioGroup = bt.getRadioGroup(false, true);
//
//		// Get number of ElementAnswers with groups
//		int numElementAnswerTree = getTotalCountOfElementAnswers(listElementAnswer);
//		PdfPCell cell = new PdfPCell(new Phrase(nameDescriptionParagraph));
//		cell.setBorder(BORDER);
//		cell.setRowspan(numElementAnswerTree);
//		table.addCell(cell);
//
//		List<String> prefix = new ArrayList<String>();
//		for (ElementAnswer answer : listElementAnswer) {
//			if (!prefix.equals(answer.getPrefix())) {
//				if (!answer.getPrefix().isEmpty()) {
//					Paragraph elementPrefix = new Paragraph(answer.getPrefix().get(0), NORMAL_FONT);
//					PdfPCell field = new PdfPCell(elementPrefix);
//					field.setBorder(BORDER);
//					field.setPaddingLeft(PADDING);
//					field.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
//					field.setCellEvent(new FormRadioField(writer, fieldName, answer.getName() + "-cat", radioGroup, 0));
//					table.addCell(field);
//				}
//			}
//			PdfPCell field = new PdfPCell(new Phrase(answer.getLabelWithoutPrefix()));
//			field.setBorder(BORDER);
//			if (answer.getPrefix().isEmpty()) {
//				field.setPaddingLeft(PADDING);
//				field.setCellEvent(new FormRadioField(writer, fieldName, answer.getName(), radioGroup, 0));
//			} else {
//				field.setPaddingLeft(PADDING * 2);
//				field.setCellEvent(new FormRadioField(writer, fieldName, answer.getName(), radioGroup, PADDING));
//			}
//			field.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
//			table.addCell(field);
//
//			if (answer.getDescription() != null && answer.getDescription().length() > 0) {
//				Paragraph elementDescription = new Paragraph("Hint: " + answer.getDescription(), DESCRIPTION_FONT);
//				PdfPCell descriptionField = new PdfPCell(elementDescription);
//				descriptionField.setBorder(BORDER);
//				if (answer.getPrefix().isEmpty()) {
//					descriptionField.setPaddingLeft(PADDING);
//				} else {
//					descriptionField.setPaddingLeft(PADDING * 2);
//				}
//				descriptionField.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
//				table.addCell(descriptionField);
//			}
//
//			prefix = answer.getPrefix();
//		}
//	}
//
//	// private static void addSystemFieldToTable(PdfPTable table, String text) {
//	// PdfPCell cell = new PdfPCell(new Phrase(text));
//	// cell.setColspan(2);
//	// table.addCell(cell);
//	// }
//
//	private static void addTextAreaToTable(PdfPTable table, String fieldName, Paragraph nameDescriptionParagraph) {
//		table.addCell(nameDescriptionParagraph);
//		PdfPCell cell = new PdfPCell();
//		cell.setBorder(BORDER);
//		FormTextArea textArea = new FormTextArea(writer, fieldName);
//		cell.setCellEvent(textArea);
//		cell.setFixedHeight(textArea.getHeight());
//		table.addCell(cell);
//	}
//
//	private static void addTextFieldToTable(PdfPTable table, String fieldName, Paragraph nameDescriptionParagraph) {
//		table.addCell(nameDescriptionParagraph);
//		PdfPCell cell = new PdfPCell();
//		cell.setBorder(BORDER);
//		cell.setCellEvent(new FormTextField(writer, fieldName));
//		table.addCell(cell);
//	}
//
//	private static void addWhiteLine(Paragraph paragraph, int numLines) {
//		for (int i = 0; i < numLines; i++) {
//			paragraph.add(new Chunk(" ", NORMAL_FONT));
//		}
//	}
//
//	private static void generate(Document document, Category category) {
//
//		Paragraph catTitle = new Paragraph();
//
//		catTitle.add(new Paragraph(category.getName(), TITLE_FONT));
//		addWhiteLine(catTitle, 1);
//
//		try {
//			document.add(catTitle);
//		} catch (DocumentException e1) {
//			e1.printStackTrace();
//		}
//
//		for (TreeObject subElement : category.getChildren()) {
//			if (subElement instanceof Element) {
//				generateAndAdd(document, (Element) subElement);
//			} else {
//				generateAndAdd(document, (Subcategory) subElement);
//			}
//		}
//	}
//
//	private static void generate(Document document, Element element) {
//
//		PdfPTable table = new PdfPTable(2);
//		table.setSplitRows(false);
//		table.getDefaultCell().setBorder(BORDER);
//		table.setKeepTogether(true);
//
//		// Add the element.
//		addElementCell(table, element);
//
//		// Add description to document.
//		addDescriptionCell(table, element);
//
//		// Add visibility cell.
//		addVisibilityCell(table, element);
//
//		// Separate elements.
//		addBlankRow(table);
//
//		try {
//			document.add(table);
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void addElementCell(PdfPTable table, Element element) {
//		switch (element.getElementType()) {
//		case INFO:
//			addInfoFieldToTable(table, element.getLabel());
//			break;
//		case SYSTEM:
//			// addSystemFieldToTable(table,element.getLabel());
//			break;
//		case QUESTION:
//			String label = element.isMandatory() && element.getAnswerType() != AnswerType.MULTI_CHECKBOX ? element
//					.getLabel() + "*" : element.getLabel();
//			Paragraph name = new Paragraph(label, NORMAL_FONT);
//			switch (element.getAnswerType()) {
//			case INPUT:
//				addTextFieldToTable(table, element.getName(), name);
//				break;
//			case TEXTAREA:
//				addTextAreaToTable(table, element.getName(), name);
//				break;
//			case SELECT:
//				addComboBoxToTable(table, element.getName(), name, element.getChildren());
//				break;
//			case RADIO:
//				addRadioFieldToTable(table, element.getName(), name, element.getElementAnswers());
//				break;
//			case MULTI_CHECKBOX:
//				addCheckFieldToTable(table, element.getName(), name, element.getElementAnswers());
//				break;
//			default:
//				break;
//			}
//			break;
//		}
//	}
//
//	/**
//	 * Add a row with the description of the element.
//	 * 
//	 * @param table
//	 * @param element
//	 */
//	private static void addDescriptionCell(PdfPTable table, Element element) {
//		String text = element.getDescription();
//		if (text.length() > 0) {
//			text = "Hint: " + text;
//			PdfPCell cell = new PdfPCell(new Paragraph(text, DESCRIPTION_FONT));
//			cell.setColspan(2);
//			cell.setBorder(BORDER);
//			cell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_TOP);
//			table.addCell(cell);
//		}
//	}
//
//	/**
//	 * Add extra information about the why that an element must be visualized.
//	 * 
//	 * @param table
//	 * @param element
//	 */
//	private static void addVisibilityCell(PdfPTable table, Element element) {
//		String text = "";
//		switch (element.getAnswerVisibility()) {
//		case NULL:
//			return;
//		case HORIZONTAL:
//			text += "Horizontal Layout.";
//		}
//		if (text.length() > 0) {
//			text = "Note: " + text;
//		}
//		PdfPCell cell = new PdfPCell(new Paragraph(text, DESCRIPTION_FONT));
//		cell.setColspan(2);
//		cell.setBorder(BORDER);
//		cell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_TOP);
//		table.addCell(cell);
//	}
//
//	private static void generate(Document document, Subcategory subCategory) {
//
//		Paragraph subCatTitle = new Paragraph();
//
//		subCatTitle.add(new Paragraph(subCategory.getName(), SUBTITLE_FONT));
//		addWhiteLine(subCatTitle, 1);
//
//		try {
//			document.add(subCatTitle);
//		} catch (DocumentException e1) {
//			e1.printStackTrace();
//		}
//
//		for (TreeObject element : subCategory.getChildren()) {
//			generateAndAdd(document, (Element) element);
//		}
//	}
//
//	
//
//	
}
