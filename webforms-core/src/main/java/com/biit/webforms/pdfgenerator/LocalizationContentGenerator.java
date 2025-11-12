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

import com.biit.form.entity.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.ElementWithDescription;
import com.biit.webforms.persistence.entity.ElementWithTranslation;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Text;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class LocalizationContentGenerator {
    private static final String MISSING_LOCALIZATION_TEXT = "<<missing localization text>>";
    private static final int BORDER = Rectangle.NO_BORDER;
    public static final String CLOSE_CURLY_QUOTE = "’";
    public static final String OPEN_CURLY_QUOTE = "‘";
    public static final String EM_DASH = "—";
    public static final String EN_DASH = "–";
    public static final String QUOTE = "'";
    public static final int PADDING = 10;
    public static final int COLUMNS = 1;
    public static final int ROWS = 1;

    private static Set<String> localizationLanguages = new HashSet<>();

    private LocalizationContentGenerator() {

    }

    public static void generateAndAdd(Document document, Form form) throws DocumentException {
        setLocalizationLanguages(form);

        if (!localizationLanguages.isEmpty()) {
            document.newPage();
            ParagraphGenerator.generateAndAddTitle(document, "Appendix B", PdfAlign.ALIGN_CENTER);

            final List<String> languages = new ArrayList<>(localizationLanguages);
            Collections.sort(languages);
            for (int i = 0; i < languages.size(); i++) {
                ParagraphGenerator.generateAndAddAnnexTitle(document, "Localization (" + languages.get(i) + ")", PdfAlign.ALIGN_CENTER);

                // Generate table
                final List<PdfTableBlock> localizationBlocks = generateAnnexFormTableBlocks(form, languages.get(i));
                for (PdfTableBlock block : localizationBlocks) {
                    List<PdfPCell> cells = block.getCells();
                    final PdfPTable table = new PdfPTable(COLUMNS);
                    table.setSplitRows(false);
                    table.setKeepTogether(true);
                    for (PdfPCell cell : cells) {
                        cell.setBorder(BORDER);
                        table.addCell(cell);
                    }
                    document.add(table);
                }
                if (i < languages.size() - 1) {
                    document.newPage();
                }
            }
        }
    }

    private static void setLocalizationLanguages(TreeObject element) {
        element.getAllChildrenInHierarchy().forEach(child -> {
            if (child.getLabelTranslations() != null) {
                for (Map.Entry<String, String> translations : child.getLabelTranslations().entrySet()) {
                    if (translations.getValue() != null && !translations.getValue().trim().isEmpty()) {
                        localizationLanguages.add(translations.getKey());
                    }
                }
            }
        });
    }


    public static List<PdfTableBlock> generateAnnexFormTableBlocks(Form form, String localization) {
        final List<PdfTableBlock> blocks = new ArrayList<>();
        generateTreeObjectElement(form, blocks, localization, 0);
        return blocks;
    }

    private static void generateTreeObjectElement(TreeObject parent, List<PdfTableBlock> blocks, String localization, float parentPadding) {
        blocks.add(generateTreeObjectElementDescription(parent, localization, parentPadding));
        for (TreeObject treeObject : parent.getChildren()) {
            float childPadding = parentPadding + PADDING;
            generateTreeObjectElement(treeObject, blocks, localization, childPadding);
        }
    }

    private static PdfTableBlock generateTreeObjectElementDescription(TreeObject treeObject, String localization, float padding) {
        PdfTableBlock block = null;
        try {
            block = new PdfTableBlock(-1, COLUMNS);
            List<PdfRow> rows = generateLocalizationRow(treeObject, localization, padding);
            for (PdfRow row : rows) {
                block.insertRow(row);
            }
        } catch (BadBlockException e) {
            WebformsLogger.errorMessage(PdfRowGenerator.class.getName(), e);
        }
        return block;
    }

    private static List<PdfRow> generateLocalizationRow(TreeObject treeObject, String localization, float padding) throws BadBlockException {
        final List<PdfRow> rows = new ArrayList<>();
        final PdfRow row = new PdfRow(-1, COLUMNS);
        final StringBuilder title = new StringBuilder();
        if (treeObject instanceof Text) {
            title.append(sanitize(((Text) treeObject).getDescription()));
        } else {
            title.append(sanitize(treeObject.getLabel()));
        }
        if (treeObject instanceof ElementWithDescription) {
            if (((ElementWithDescription) treeObject).getDescription() != null && !((ElementWithDescription) treeObject).getDescription().trim().isEmpty()) {
                title.append(" [").append(sanitize(((ElementWithDescription) treeObject).getDescription())).append("] ");
            }
        }
        final PdfPCell labelCell = new PdfPCell(new Phrase(title.toString().trim(), PdfFont.SMALL_BOLD_FONT.getFont()));
        labelCell.setBorder(1);
        labelCell.setPaddingLeft(padding);
        row.addCell(labelCell);
        rows.add(row);
        if (treeObject.getLabelTranslations() != null ||
                (treeObject instanceof ElementWithTranslation && ((ElementWithTranslation) treeObject).getDescriptionTranslations() != null
                        && !((ElementWithTranslation) treeObject).getDescriptionTranslations().isEmpty())) {
            final StringBuilder text = new StringBuilder();
            if (treeObject.getLabelTranslations() != null) {
                String translatedLabel = treeObject.getLabelTranslations().get(localization);
                boolean isNumber = false;
                try {
                    Double.parseDouble(treeObject.getLabel());
                    isNumber = true;
                } catch (Exception e) {
                    //It is not a number
                }
                if (translatedLabel == null && !treeObject.getLabel().isEmpty() && !isNumber) {
                    translatedLabel = MISSING_LOCALIZATION_TEXT;
                }
                if (translatedLabel == null) {
                    //Numbers does not need translations.
                    return rows;
                }
                text.append(translatedLabel.trim());
            }
            if (treeObject instanceof ElementWithTranslation) {
                final ElementWithTranslation elementWithTranslation = (ElementWithTranslation) treeObject;
                final boolean hasLabelTranslation = text.length() > 0;
                if (elementWithTranslation.getDescriptionTranslations().get(localization) != null
                        && !elementWithTranslation.getDescriptionTranslations().get(localization).trim().isEmpty()) {
                    text.append(hasLabelTranslation ? " [" : "").append(sanitize(elementWithTranslation.getDescriptionTranslations().get(localization)))
                            .append(hasLabelTranslation ? "] " : "");
                }
            }
            final PdfRow rowTranslation = new PdfRow(ROWS, COLUMNS);
            final PdfPCell translationCell = new PdfPCell(new Phrase(text.toString().trim(), PdfFont.SMALL_FONT.getFont()));
            translationCell.setPaddingLeft(padding);
            rowTranslation.addCell(translationCell);
            rows.add(rowTranslation);
        }
        return rows;
    }

    private static String sanitize(String text) {
        return text.replace(OPEN_CURLY_QUOTE, QUOTE).replace(CLOSE_CURLY_QUOTE, QUOTE).replace(EM_DASH, "-")
                .replace(EN_DASH, "-").trim();
    }
}
