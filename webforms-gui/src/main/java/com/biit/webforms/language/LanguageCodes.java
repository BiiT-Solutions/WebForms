package com.biit.webforms.language;

import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.common.language.ServerTranslate;

public enum LanguageCodes implements ILanguageCode{
	//Common
	COMMON_CAPTION_NAME("caption.name"),
	COMMON_CAPTION_DESCRIPTION("caption.description"),
	COMMON_CAPTION_EDIT_DESCRIPTION("caption.edit.description"),
	COMMON_CAPTION_SETTINGS("caption.settings"),
	COMMON_CAPTION_FORM_MANAGER("caption.form.manager"),
	COMMON_CAPTION_BUILDING_BLOCK_MANAGER("caption.buildingBlock.manager"),
	COMMON_CAPTION_DESIGN("caption.design"),
	COMMON_CAPTION_FLOW("caption.flow"),
	COMMON_CAPTION_SAVE("caption.save"),
	COMMON_CAPTION_VALIDATE("caption.validate"),
	COMMON_CAPTION_FINISH("caption.finish"),
	COMMON_CAPTION_MOVE("caption.move"),
	COMMON_CAPTION_DELETE("caption.delete"),
	COMMON_CAPTION_UP("caption.up"),
	COMMON_CAPTION_DOWN("caption.down"),
	COMMON_CAPTION_EXPORT_TO_PDF("caption.exportToPdf"),
	
	CAPTION_ANSWER_FORMAT_TEXT("caption.answerFormat.text"),
	CAPTION_ANSWER_FORMAT_NUMBER("caption.answerFormat.number"),
	CAPTION_ANSWER_FORMAT_DATE("caption.answerFormat.date"),
	CAPTION_ANSWER_FORMAT_POSTAL_CODE("caption.answerFormat.postalCode"),
	CAPTION_ANSWER_TYPE_INPUT_FIELD("caption.answerType.inputField"),
	CAPTION_ANSWER_TYPE_RADIO_BUTTON("caption.answerType.radioButton"),
	CAPTION_ANSWER_TYPE_MULTI_CHECKBOX("caption.answerType.multiCheckbox"),
	
	COMMON_TOOLTIP_SETTINGS("tooltip.settings"),
	COMMON_TOOLTIP_FORM_MANAGER("tooltip.form.manager"),
	COMMON_TOOLTIP_BUILDING_BLOCK_MANAGER("tooltip.buildingBlock.manager"),
	COMMON_TOOLTIP_DESIGN("tooltip.design"),
	COMMON_TOOLTIP_FLOW("tooltip.flow"),
	COMMON_TOOLTIP_SAVE("tooltip.save"),
	COMMON_TOOLTIP_VALIDATE("tooltip.validate"),
	COMMON_TOOLTIP_FINISH("tooltip.finish"),
	COMMON_TOOLTIP_MOVE("tooltip.move"),
	COMMON_TOOLTIP_DELETE("tooltip.delete"),
	COMMON_TOOLTIP_UP("tooltip.up"),
	COMMON_TOOLTIP_DOWN("tooltip.down"),
	COMMON_TOOLTIP_EXPORT_TO_PDF("tooltip.exportToPdf"),
	
	COMMON_WARNING_TITLE_FORM_NOT_CREATED("common.warning.title.form.not.created"),
	COMMON_WARNING_DESCRIPTION_FORM_NEEDS_NAME("common.warning.description.form.needs.name"),
	
	COMMON_ERROR_FIELD_TOO_LONG("common.error.field.too.long"),
	COMMON_ERROR_NAME_IS_IN_USE("common.error.name.is.in.use"),
	COMMON_ERROR_ELEMENT_NOT_DELETED("common.error.element.not.deleted"),
	
	//Manager
	CAPTION_NEW_FORM("caption.new.form"),
	CAPTION_NEW_FORM_VERSION("caption.new.form.version"),
	
	TOOLTIP_NEW_FORM("tooltip.new.form"),
	TOOLTIP_NEW_FORM_VERSION("tooltip.new.form.version"),
	
	NULL_VALUE_NEW_FORM("null.value.new.form"),
		
	FORM_TABLE_COLUMN_NAME("caption.form.table.name"),
	FORM_TABLE_COLUMN_VERSION("caption.form.table.version"),
	FORM_TABLE_COLUMN_INFO("caption.form.table.info"),
	FORM_TABLE_COLUMN_ACCESS("caption.form.table.access"),
	FORM_TABLE_COLUMN_USEDBY("caption.form.table.usedBy"),
	FORM_TABLE_COLUMN_STATUS("caption.form.table.status"),
	FORM_TABLE_COLUMN_CREATEDBY("caption.form.table.createdBy"),
	FORM_TABLE_COLUMN_CREATIONDATE("caption.form.table.creationDate"),
	FORM_TABLE_COLUMN_MODIFIEDBY("caption.form.table.modifiedBy"),
	FORM_TABLE_COLUMN_MODIFICATIONDATE("caption.form.table.modificationDate"),

	//BlockManager
	CAPTION_NEW_BLOCK("caption.new.block"),
	TOOLTIP_NEW_BLOCK("caption.new.block"),
	
	//Designer
	CAPTION_NEW_CATEGORY("caption.new.category"),
	CAPTION_NEW_SUBCATEGORY("caption.new.subcategory"),
	CAPTION_NEW_GROUP("caption.new.group"),
	CAPTION_NEW_QUESTION("caption.new.question"),
	CAPTION_NEW_TEXT("caption.new.text"),
	CAPTION_NEW_ANSWER("caption.new.answer"),
	CAPTION_NAME("caption.name"),
	CAPTION_VALUE("caption.value"),
	CAPTION_LABEL("caption.label"),
	CAPTION_DESCRIPTION("caption.description"),
	CAPTION_MANDATORY("caption.mandatory"),
	CAPTION_ANSWER_TYPE("caption.answer.type"),
	CAPTION_ANSWER_FORMAT("caption.answer.format"),
	CAPTION_HORIZONTAL("caption.horizontal"),
	CAPTION_REPETABLE("caption.repetable"),
	CAPTION_TEXT("caption.text"),
	CAPTION_VERSION("caption.version"),
	CAPTION_PROPERTIES_TEXT("caption.properties.text"),
	CAPTION_PROPERTIES_ANSWER("caption.properties.answer"),
	CAPTION_PROPERTIES_QUESTION("caption.properties.question"),
	CAPTION_PROPERTIES_GROUP("caption.properties.group"),
	CAPTION_PROPERTIES_SUBCATEGORY("caption.properties.subcategory"),
	CAPTION_PROPERTIES_CATEGORY("caption.properties.category"),
	CAPTION_PROPERTIES_FORM("caption.properties.form"),
	
	TOOLTIP_NEW_CATEGORY("tooltip.new.category"),
	TOOLTIP_NEW_SUBCATEGORY("tooltip.new.subcategory"),
	TOOLTIP_NEW_GROUP("tooltip.new.group"),
	TOOLTIP_NEW_QUESTION("tooltip.new.question"),
	TOOLTIP_NEW_TEXT("tooltip.new.text"),
	TOOLTIP_NEW_ANSWER("tooltip.new.answer"),
	
	ERROR_SUBCATEGORY_NOT_INSERTED("error.subcategory.not.inserted"),
	ERROR_GROUP_NOT_INSERTED("error.group.not.inserted"),
	ERROR_QUESTION_NOT_INSERTED("error.question.not.inserted"),
	ERROR_TEXT_NOT_INSERTED("error.text.not.inserted"),
	ERROR_ANSWER_NOT_INSERTED("error.answer.not.inserted"), 
	
	INFO_MESSAGE_CAPTION("info.message.caption"),
	INFO_MESSAGE_DESCRIPTION("info.message.description"),   
	
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

	@Override
	public String translation() {
		return ServerTranslate.translate(value);
	}
}
