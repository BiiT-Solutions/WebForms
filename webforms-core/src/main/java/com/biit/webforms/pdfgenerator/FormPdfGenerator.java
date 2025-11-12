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
import com.biit.utils.date.DateManager;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.exceptions.BadBlockException;
import com.biit.webforms.persistence.entity.AttachedFiles;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;

/**
 * iText pdf generator for the form design data. It contains all the functions
 * to generate the general sections of a form.
 */
public class FormPdfGenerator extends DocumentGenerator {

    private Form form;

    public FormPdfGenerator(Form form) {
        this.form = form;

        FormPageEvent formPageEvent = new FormPageEvent();
        formPageEvent.setHeader(form.getLabel());

        setPageEvent(formPageEvent);

    }

    @Override
    protected void generateDocumentContent(Document document) throws DocumentException {
        ParagraphGenerator.generateAndAddFormTitle(document, form.getLabel(), PdfAlign.ALIGN_CENTER);
        String updateDate = DateManager.convertDateToString(form.getUpdateTime());
        ParagraphGenerator.generateAndAddSubtitle(document, "Version: " + form.getVersion() + " - " + updateDate, PdfAlign.ALIGN_CENTER);

        for (TreeObject child : form.getChildren()) {
            if (!child.isHiddenElement()) {
                generateAndAddCategory(document, (Category) child);
            }
        }

        document.newPage();

        // Annex page generation
        AnnexGenerator.generateAndAdd(document, form);

        // Localization page generation
        LocalizationContentGenerator.generateAndAdd(document, form);
    }

    private void generateAndAddTreeObject(Document document, TreeObject object) throws DocumentException {
        // Determine the type of element anf generate with the appropiate class.
        if (!object.isHiddenElement()) {
            if (object instanceof Category) {
                generateAndAddCategory(document, (Category) object);
            } else if (object instanceof Group) {
                generateAndAddGroup(document, (Group) object);
            } else if (object instanceof Question) {
                generateAndAddQuestion(document, (Question) object);
            } else if (object instanceof Text) {
                generateAndAddText(document, (Text) object);
            } else if (object instanceof SystemField) {
                // System fields are not added to PDF. Only in the annex table.
            } else if (object instanceof AttachedFiles) {
                generateAndAddAttachedFiles(document, (AttachedFiles) object);
            } else {
                throw new DocumentException("Structure not recognized");
            }
        }
    }

    private void generateAndAddText(Document document, Text text) throws DocumentException {
        PdfPTable questionTable;
        try {
            questionTable = PdfTableGenerator.generateTextTable(text);
            document.add(questionTable);
        } catch (BadBlockException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }
    }

    private void generateAndAddQuestion(Document document, Question question) throws DocumentException {
        try {
            PdfPTable questionTable = PdfTableGenerator.generateQuestionTable(getWriter(), question);
            document.add(questionTable);
        } catch (BadBlockException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }
    }

    private void generateAndAddAttachedFiles(Document document, AttachedFiles attachedFiles) throws DocumentException {
        try {
            PdfPTable questionTable = PdfTableGenerator.generateAttachedFileTable(getWriter(), attachedFiles);
            document.add(questionTable);
        } catch (BadBlockException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }
    }

    private void generateAndAddGroup(Document document, Group group) throws DocumentException {
        ParagraphGenerator.generateAndAddSubtitle(document, group.getLabel(), PdfAlign.ALIGN_LEFT);
        if (group.isRepeatable()) {
            document.add(ParagraphGenerator.generateRepeatableParagraph());
        }

        for (TreeObject element : group.getChildren()) {
            generateAndAddTreeObject(document, element);
        }
    }

    private void generateAndAddCategory(Document document, Category category) throws DocumentException {
        ParagraphGenerator.generateAndAddTitle(document, category.getLabel(), PdfAlign.ALIGN_LEFT);

        for (TreeObject child : category.getChildren()) {
            generateAndAddTreeObject(document, child);
        }
    }

}
