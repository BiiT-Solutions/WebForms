package com.biit.webforms.pdfgenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
