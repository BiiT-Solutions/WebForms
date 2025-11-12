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
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator for combo box list.
 *
 */
class FormComboBox extends FormTextField {

	private List<TreeObject> options;

	public FormComboBox(PdfWriter writer, String fieldName, List<TreeObject> options) {
		super(writer, fieldName);
		this.options = options;
	}

	@Override
	public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
		TextField tf = createTextField(position);

		List<String> choices = new ArrayList<>();
		for (TreeObject option : options) {
			if(option instanceof Answer){
				Answer answer = (Answer) option;
				choices.add(answer.getLabel());
			}else{
				DynamicAnswer answer = (DynamicAnswer) option;
				choices.add(answer.getReferenceName());
			}
		}

		tf.setChoices(choices.toArray(new String[choices.size()]));
		// tf.setChoiceExports(EXPORTVALUES);
		tf.setChoiceSelection(0);

		try {
			PdfFormField field = tf.getComboField();
			writer.addAnnotation(field);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
