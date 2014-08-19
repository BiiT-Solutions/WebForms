package com.biit.webforms.language;

import com.biit.webforms.gui.common.language.ILanguageCode;

public enum LanguageCodes implements ILanguageCode{
	CAPTION_ANSWER_FORMAT_TEXT("caption.answerFormat.text"),
	CAPTION_ANSWER_FORMAT_NUMBER("caption.answerFormat.number"),
	CAPTION_ANSWER_FORMAT_DATE("caption.answerFormat.date"),
	CAPTION_ANSWER_FORMAT_POSTAL_CODE("caption.answerFormat.postalCode"),
	CAPTION_ANSWER_TYPE_INPUT_FIELD("caption.answerType.inputField"),
	CAPTION_ANSWER_TYPE_RADIO_BUTTON("caption.answerType.radioButton"),
	CAPTION_ANSWER_TYPE_MULTI_CHECKBOX("caption.answerType.multiCheckbox"), 
	
	FORM_TABLE_COLUMN_NAME("caption.form.table.name"),
	FORM_TABLE_COLUMN_VERSION("caption.form.table.version"),
	FORM_TABLE_COLUMN_ACCESS("caption.form.table.access"),
	FORM_TABLE_COLUMN_USEDBY("caption.form.table.usedBy"),
	FORM_TABLE_COLUMN_CREATEDBY("caption.form.table.createdBy"),
	FORM_TABLE_COLUMN_CREATIONDATE("caption.form.table.creationDate"),
	FORM_TABLE_COLUMN_MODIFIEDBY("caption.form.table.modifiedBy"),
	FORM_TABLE_COLUMN_MODIFICATIONDATE("caption.form.table.modificationDate")
	;

	private String value;

	private LanguageCodes(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public String getCode() {
		return value;
	}
}
