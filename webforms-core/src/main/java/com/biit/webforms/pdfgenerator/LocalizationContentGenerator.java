package com.biit.webforms.pdfgenerator;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.ElementWithDescription;
import com.biit.webforms.persistence.entity.ElementWithTranslation;
import com.biit.webforms.persistence.entity.Form;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class LocalizationContentGenerator {

    public static final int PADDING = 10;
    public static final int COLUMNS = 1;
    public static final int ROWS = 1;

    private LocalizationContentGenerator() {

    }

    public static void generateAndAdd(Document document, Form form) throws DocumentException {
        ParagraphGenerator.generateAndAddTitle(document, "Appendix B", PdfAlign.ALIGN_CENTER);
        ParagraphGenerator.generateAndAddAnnexTitle(document, "Localization", PdfAlign.ALIGN_CENTER);

        // Generate table
        final List<PdfTableBlock> localizationBlocks = generateAnnexFormTableBlocks(form);
        for (PdfTableBlock block : localizationBlocks) {
            List<PdfPCell> cells = block.getCells();
            final PdfPTable table = new PdfPTable(COLUMNS);
            table.setSplitRows(false);
            table.setKeepTogether(true);
            for (PdfPCell cell : cells) {
                table.addCell(cell);
            }
            document.add(table);
        }
    }


    public static List<PdfTableBlock> generateAnnexFormTableBlocks(Form form) {
        final List<PdfTableBlock> blocks = new ArrayList<>();
        generateTreeObjectElement(form, blocks, 0);
        return blocks;
    }

    private static void generateTreeObjectElement(TreeObject parent, List<PdfTableBlock> blocks, float parentPadding) {
        blocks.add(generateTreeObjectElementDescription(parent, parentPadding));
        for (TreeObject treeObject : parent.getChildren()) {
            float childPadding = parentPadding + PADDING;
            generateTreeObjectElement(treeObject, blocks, childPadding);
        }
    }

    private static PdfTableBlock generateTreeObjectElementDescription(TreeObject treeObject, float padding) {
        PdfTableBlock block = null;
        try {
            block = new PdfTableBlock(-1, COLUMNS);
            List<PdfRow> rows = generateLocalizationRow(treeObject, padding);
            for (PdfRow row : rows) {
                block.insertRow(row);
            }
        } catch (BadBlockException e) {
            WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
        }
        return block;
    }

    private static List<PdfRow> generateLocalizationRow(TreeObject treeObject, float padding) throws BadBlockException {
        final List<PdfRow> rows = new ArrayList<>();
        final PdfRow row = new PdfRow(-1, COLUMNS);
        final StringBuilder title = new StringBuilder(treeObject.getLabel());
        if (treeObject instanceof ElementWithDescription) {
            title.append(" [").append(((ElementWithDescription) treeObject).getDescription()).append("] ");
        }
        final PdfPCell labelCell = new PdfPCell(new Paragraph(title.toString(), PdfFont.SMALL_BOLD_FONT.getFont()));
        labelCell.setPaddingLeft(padding);
        row.addCell(labelCell);
        rows.add(row);
        for (Map.Entry<String, String> translation : treeObject.getLabelTranslations().entrySet()) {
            final PdfRow rowTranslation = new PdfRow(ROWS, COLUMNS);
            final StringBuilder text = new StringBuilder("(" + translation.getKey() + ") " + translation.getValue());
            if (treeObject instanceof ElementWithTranslation) {
                final ElementWithTranslation elementWithTranslation = (ElementWithTranslation) treeObject;
                if (elementWithTranslation.getDescriptionTranslations().get(translation.getKey()) != null) {
                    text.append(" [").append(elementWithTranslation.getDescriptionTranslations().get(translation.getKey())).append("] ");
                }
            }
            final PdfPCell translationCell = new PdfPCell(new Paragraph(text.toString(), PdfFont.SMALL_FONT.getFont()));
            translationCell.setPaddingLeft(padding);
            rowTranslation.addCell(translationCell);
            rows.add(rowTranslation);
        }
        return rows;
    }
}
