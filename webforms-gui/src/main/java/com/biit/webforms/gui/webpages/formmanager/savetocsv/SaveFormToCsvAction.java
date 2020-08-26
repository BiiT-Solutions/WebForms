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

	private Form current_form;

	public SaveFormToCsvAction(Form form) {
		this.current_form = form;
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
			String formToCsvDataString = "Form name: "+current_form.getLabel()+
					"\n\nCategory technical name;Category label;Question technical name;Question label;Answer value;Answer label;Score\n";
			if (current_form != null) {
				for (TreeObject category : current_form.getChildren()) {
					String category_technical_name = category.getName();
					String category_label = category.getLabel();
					for (TreeObject question : category.getChildren()) {
						String question_technical_name = question.getName();
						String question_label = question.getLabel();
						if (question.getChildren() != null){
							for (TreeObject answer : question.getChildren()) {
								String answer_technical_name = answer.getName();
								String answer_value = answer.getLabel();
								formToCsvDataString += category_technical_name+";"+category_label+";"+
										question_technical_name+";"+question_label+";"+answer_technical_name+";"+answer_value+"\n";
							}
						} else {
							formToCsvDataString += category_technical_name+";"+category_label+";"+
									question_technical_name+";"+question_label+"\n";
						}
					}
				}
				return formToCsvDataString.getBytes();
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