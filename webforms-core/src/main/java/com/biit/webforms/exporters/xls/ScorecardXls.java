package com.biit.webforms.exporters.xls;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.context.ApplicationContext;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.webforms.exporters.xls.exceptions.InvalidXlsElementException;
import com.biit.webforms.logger.XlsExporterLog;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;

public class ScorecardXls {
	private final static String[] COLUMNS_NAMES = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
			"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private final static String[] TITLES_NAMES = { "", "xpath", "category", "question", "answer", "score" };
	private final static int XPATH_COLUMN = 1;
	private final static int CATEGORY_COLUMN = 2;
	private final static int QUESTION_COLUMN = 3;
	private final static int ANSWER_COLUMN = 4;
	private final static int SCORE_COLUMN = 5;
	private final static String LanguageLocalizationPrefix = "form.xls.";
	private final static String SHEET_NAME = "Form";
	private final static int LABEL_FONT_SIZE = 12;
	private final static int TITLE_ROW = 1;
	private final static int TITLE_FONT_SIZE = 14;

	private HSSFSheet sheet;
	private HSSFCellStyle titleStyle = null;
	private HSSFCellStyle contentStyle = null;
	private HSSFCellStyle scoreStyle = null;

	private List<HSSFRow> scoreRows = new ArrayList<>();

	private Locale locale = Locale.getDefault();

	private transient ApplicationContext applicationContext;

	public void createXlsDocument(HSSFWorkbook workbook, ApplicationContext applicationContext, Form form,
			Locale locale) throws InvalidXlsElementException {
		this.applicationContext = applicationContext;
		setLocale(locale);
		// Override colors
		setColor(workbook, HSSFColor.HSSFColorPredefined.RED, (byte) 0xEE, (byte) 0xAA, (byte) 0xAA);

		createFormTables(workbook, form);
	}

	private void createFormTables(HSSFWorkbook workbook, Form form) {
		HSSFSheet sheet = getSheet(workbook);

		// Create title
		createFormTitle(workbook, sheet);

		for (Category category : form.getAllChildrenInHierarchy(Category.class)) {
			for (Question question : category.getAllChildrenInHierarchy(Question.class)) {
				for (Answer answer : question.getAllChildrenInHierarchy(Answer.class)) {
					createRow(workbook, sheet, category, question, answer);
				}
			}
		}

		// Hide Xpath column
		// sheet.setColumnWidth(XPATH_COLUMN, 1);
		sheet.setColumnHidden(XPATH_COLUMN, true);
	}

	private void createFormTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
		HSSFRow titleRow = sheet.createRow(TITLE_ROW);

		// sheet.autoSizeColumn(QUESTION_LABEL_COLUMN);
		// sheet.setColumnWidth(APPOINTMENT_LABEL_COLUMN, 256 * 50);

		for (int i = 1; i < TITLES_NAMES.length; i++) {
			// Create titles
			titleRow.createCell(i).setCellValue(applicationContext
					.getMessage(LanguageLocalizationPrefix + TITLES_NAMES[i].toLowerCase(), null, getLocale()));

			titleRow.getCell(i).setCellStyle(getTitleStyle(workbook));
			sheet.setColumnWidth(i, 150 * 50);
		}
	}

	private void createRow(HSSFWorkbook workbook, HSSFSheet sheet, Category category, Question question,
			Answer answer) {
		HSSFRow scoreRow = sheet.createRow(TITLE_ROW + scoreRows.size() + 1);
		scoreRows.add(scoreRow);

		scoreRow.createCell(XPATH_COLUMN).setCellValue(answer.getXPath());
		scoreRow.getCell(XPATH_COLUMN).setCellStyle(getContentStyle(workbook));
		scoreRow.createCell(CATEGORY_COLUMN).setCellValue(category.getLabel());
		scoreRow.getCell(CATEGORY_COLUMN).setCellStyle(getContentStyle(workbook));
		scoreRow.createCell(QUESTION_COLUMN).setCellValue(question.getLabel());
		scoreRow.getCell(QUESTION_COLUMN).setCellStyle(getContentStyle(workbook));
		scoreRow.createCell(ANSWER_COLUMN).setCellValue(answer.getLabel());
		scoreRow.getCell(ANSWER_COLUMN).setCellStyle(getContentStyle(workbook));
		scoreRow.createCell(SCORE_COLUMN).setCellValue("");
		scoreRow.getCell(SCORE_COLUMN).setCellStyle(getScoreStyle(workbook));
	}

	private CellStyle getTitleStyle(HSSFWorkbook workbook) {
		if (titleStyle == null) {
			titleStyle = workbook.createCellStyle();

			// Border
			titleStyle.setBorderRight(BorderStyle.THIN);
			titleStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			titleStyle.setBorderBottom(BorderStyle.THIN);
			titleStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			titleStyle.setBorderLeft(BorderStyle.THIN);
			titleStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			titleStyle.setBorderTop(BorderStyle.THIN);
			titleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

			// Background Color
			titleStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.ORCHID.getIndex());
			titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// Alignment
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			// Font
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) TITLE_FONT_SIZE);
			headerFont.setColor(IndexedColors.WHITE.getIndex());
			titleStyle.setFont(headerFont);
		}
		return titleStyle;
	}

	private HSSFCellStyle getContentStyle(HSSFWorkbook workbook) {
		if (contentStyle == null) {
			contentStyle = createContentStyle(workbook);
		}
		return contentStyle;
	}

	private HSSFCellStyle getScoreStyle(HSSFWorkbook workbook) {
		if (scoreStyle == null) {
			scoreStyle = createScoreStyle(workbook);
		}
		return scoreStyle;
	}

	private HSSFCellStyle createContentStyle(HSSFWorkbook workbook) {
		HSSFCellStyle contentStyle = workbook.createCellStyle();

		// Background Color
		contentStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.CORAL.getIndex());
		contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Alignment
		contentStyle.setAlignment(HorizontalAlignment.LEFT);
		contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Border
		contentStyle.setBorderBottom(BorderStyle.THIN);
		contentStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		return contentStyle;
	}

	private HSSFCellStyle createScoreStyle(HSSFWorkbook workbook) {
		HSSFCellStyle contentStyle = workbook.createCellStyle();

		// Background Color
		contentStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
		contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Alignment
		contentStyle.setAlignment(HorizontalAlignment.CENTER);
		contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Border
		contentStyle.setBorderBottom(BorderStyle.THIN);
		contentStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		return contentStyle;
	}

	private HSSFSheet getSheet(HSSFWorkbook workbook) {
		if (sheet == null) {
			sheet = workbook.createSheet(parseInvalidCharacters(SHEET_NAME));
			sheet.setDefaultRowHeight((short) (20 * LABEL_FONT_SIZE * 1.5));
		}
		return sheet;
	}

	private String parseInvalidCharacters(String text) {
		// Sheets does not allows this characters.
		return text.replace(":", "").replace("\\", "-").replace("/", "-").replace("*", "").replace("?", "")
				.replace("[", "(").replace("]", ")");
	}

	private HSSFColor setColor(HSSFWorkbook workbook, HSSFColorPredefined color, byte r, byte g, byte b) {
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor hssfColor = null;
		try {
			hssfColor = palette.findColor(r, g, b);
			if (hssfColor == null) {
				palette.setColorAtIndex(color.getIndex(), r, g, b);
				hssfColor = palette.getColor(color.getIndex());
			}
		} catch (Exception e) {
			XlsExporterLog.errorMessage(this.getClass().getName(), e);
		}

		return hssfColor;
	}

	public static String getColumnName(int index) {
		if (index < COLUMNS_NAMES.length) {
			return COLUMNS_NAMES[index];
		} else {
			return COLUMNS_NAMES[index / COLUMNS_NAMES.length - 1] + COLUMNS_NAMES[index % COLUMNS_NAMES.length];
		}
	}

	private Locale getLocale() {
		return locale;
	}

	private void setLocale(Locale locale) {
		this.locale = locale;
	}
}
