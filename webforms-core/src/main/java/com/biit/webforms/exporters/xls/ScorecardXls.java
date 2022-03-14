package com.biit.webforms.exporters.xls;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.logger.XlsExporterLog;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ScorecardXls {
    private final static String[] COLUMNS_NAMES = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private final static String[] TITLES_NAMES = {"", "xpath", "category", "question", "answer", "score"};
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

    private final List<HSSFRow> scoreRows = new ArrayList<>();

    private Locale locale = Locale.getDefault();

    private transient ApplicationContext applicationContext;

    public void createXlsDocument(HSSFWorkbook workbook, ApplicationContext applicationContext, Form form,
                                  Locale locale) {
        this.applicationContext = applicationContext;
        setLocale(locale);
        // Override colors
        setColor(workbook, IndexedColors.RED, (byte) 0xEE, (byte) 0xAA, (byte) 0xAA);
        setColor(workbook, IndexedColors.ORANGE, (byte) 0xF8, (byte) 0x99, (byte) 0x30);
        setColor(workbook, IndexedColors.LIGHT_ORANGE, (byte) 0xFF, (byte) 0xCC, (byte) 0x7F);

        createFormTables(workbook, form);


    }

    private void createFormTables(HSSFWorkbook workbook, Form form) {
        HSSFSheet sheet = getSheet(workbook);

        // Create title
        createFormTitle(workbook, sheet);

        for (TreeObject category : form.getChildren()) {
            if (category instanceof Category) {
                for (Question question : category.getAllChildrenInHierarchy(Question.class)) {
                    //Input text or text areas
                    if (Objects.equals(question.getAnswerType(), AnswerType.INPUT)) {
                        createRow(workbook, sheet, (Category) category, question, null);
                    } else {
                        for (Answer answer : question.getAllChildrenInHierarchy(Answer.class)) {
                            createRow(workbook, sheet, (Category) category, question, answer);
                        }
                    }
                }
            }
        }

        // Hide Xpath column
        // sheet.setColumnWidth(XPATH_COLUMN, 1);
        sheet.setColumnHidden(XPATH_COLUMN, true);
    }

    private void createFormTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow titleRow = sheet.createRow(TITLE_ROW);

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

        if (answer != null) {
            scoreRow.createCell(XPATH_COLUMN).setCellValue(question.getPathName() + TreeObject.DEFAULT_PATH_SEPARATOR + answer.getValue());
        } else {
            scoreRow.createCell(XPATH_COLUMN).setCellValue(question.getPathName());
        }
        scoreRow.getCell(XPATH_COLUMN).setCellStyle(getContentStyle(workbook));
        scoreRow.createCell(CATEGORY_COLUMN).setCellValue(category.getLabel());
        scoreRow.getCell(CATEGORY_COLUMN).setCellStyle(getContentStyle(workbook));
        scoreRow.createCell(QUESTION_COLUMN).setCellValue(question.getLabel());
        scoreRow.getCell(QUESTION_COLUMN).setCellStyle(getContentStyle(workbook));
        if (answer != null) {
            scoreRow.createCell(ANSWER_COLUMN).setCellValue(answer.getLabel());
        } else {
            scoreRow.createCell(ANSWER_COLUMN);
        }
        scoreRow.getCell(ANSWER_COLUMN).setCellStyle(getContentStyle(workbook));
        scoreRow.createCell(SCORE_COLUMN).setCellValue("");
        scoreRow.getCell(SCORE_COLUMN).setCellStyle(getScoreStyle(workbook));
    }

    private CellStyle getTitleStyle(HSSFWorkbook workbook) {
        if (titleStyle == null) {
            titleStyle = workbook.createCellStyle();

            // Border
            titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            titleStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            titleStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            titleStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            titleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

            // Background Color
            titleStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

            // Alignment
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            // Font
            Font headerFont = workbook.createFont();
            headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
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
        contentStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // Alignment
        contentStyle.setAlignment(CellStyle.ALIGN_LEFT);
        contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        // Border
        contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        contentStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        return contentStyle;
    }

    private HSSFCellStyle createScoreStyle(HSSFWorkbook workbook) {
        HSSFCellStyle contentStyle = workbook.createCellStyle();

        // Background Color
        contentStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        contentStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // Alignment
        contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
        contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        // Border
        contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        contentStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        return contentStyle;
    }

    private HSSFSheet getSheet(HSSFWorkbook workbook) {
        if (sheet == null) {
            sheet = workbook.createSheet(parseInvalidCharacters());
            sheet.setDefaultRowHeight((short) (20 * LABEL_FONT_SIZE * 1.5));
        }
        return sheet;
    }

    private String parseInvalidCharacters() {
        // Sheets does not allows this characters.
        return ScorecardXls.SHEET_NAME.replace(":", "").replace("\\", "-").replace("/", "-").replace("*", "").replace("?", "")
                .replace("[", "(").replace("]", ")");
    }

    private HSSFColor setColor(HSSFWorkbook workbook, IndexedColors color, byte r, byte g, byte b) {
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
