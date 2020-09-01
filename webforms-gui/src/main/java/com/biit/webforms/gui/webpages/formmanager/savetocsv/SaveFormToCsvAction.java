package com.biit.webforms.gui.webpages.formmanager.savetocsv;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Transforms a table rule into a csv file and allows the user to download it
 * 
 */
public class SaveFormToCsvAction implements SaveAction {

	private Form currentForm;
	private final static String CSV_SEPARATOR = ";";

	public SaveFormToCsvAction(Form form) {
		this.currentForm = form;
	}

	@Override
	public String getMimeType() {
		return "text/csv";
	}

	@Override
	public String getExtension() {
		return "csv";
	}

	@Override
	public byte[] getInformationData() {
		try {
			//store form children data -> Categories->Questions->Answers
			StringBuilder formToCsvDataStringBuilder = new StringBuilder();
			formToCsvDataStringBuilder.append("Form name: "+ currentForm.getLabel()+
					"\n\nCategory technical name"+CSV_SEPARATOR+"Category label"+CSV_SEPARATOR+
					"Question technical name"+CSV_SEPARATOR+"Question label"+CSV_SEPARATOR+"Answer value"+CSV_SEPARATOR+"Answer label"+CSV_SEPARATOR+"Score\n");
			if (currentForm != null) {
				for (TreeObject category : currentForm.getChildren()) {
					String categoryTechnicalName = category.getName();
					String categoryLabel = category.getLabel();
					for (TreeObject question : category.getChildren()) {
						String questionTechnicalName = question.getName();
						String questionLabel = question.getLabel();
						if (question.getChildren() != null){
							for (TreeObject answer : question.getChildren()) {
								String answerTechnicalName = answer.getName();
								String answerValue = answer.getLabel();
								formToCsvDataStringBuilder.append(categoryTechnicalName+CSV_SEPARATOR+categoryLabel+CSV_SEPARATOR+
										questionTechnicalName+CSV_SEPARATOR+questionLabel+CSV_SEPARATOR+answerTechnicalName+CSV_SEPARATOR+answerValue+"\n");
							}
						} else {
							formToCsvDataStringBuilder.append(categoryTechnicalName+CSV_SEPARATOR+categoryLabel+CSV_SEPARATOR+
									questionTechnicalName+CSV_SEPARATOR+questionLabel+"\n");
						}
					}
				}
				return formToCsvDataStringBuilder.toString().getBytes();
			}
		} catch (Exception e) {
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			WebformsLogger.errorMessage("SaveTableToCsvAction", e);
		}
		return null;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getFileName() {
		return "test." + getExtension();
	}
}