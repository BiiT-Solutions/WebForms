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
	COMMON_CAPTION_INSERT_BLOCK("caption.insert.block"),
	COMMON_CAPTION_GROUP("common.caption.group"),
	CAPTION_EDIT_FORM_DESIGN("caption.edit.form.design"),
	CAPTION_EDIT_FORM_FLOW("caption.edit.form.flow"),
	CAPTION_VALIDATE_FORM("caption.validate.form"),
	CAPTION_IMPACT_ANALYSIS("caption.impact.analysis"),
//	TOOLTIP_EDIT_FORM_DESIGN,
//	TOOLTIP_EDIT_FORM_FLOW,
//	TOOLTIP_VALIDATE_FORM,
//	TOOLTIP_IMPACT_ANALISYS,
	
	CAPTION_ANSWER_TYPE_INPUT_FIELD("caption.answerType.inputField"),
	CAPTION_ANSWER_TYPE_SINGLE_SELECT_LIST("caption.answerType.singleSelectList"),
	CAPTION_ANSWER_TYPE_SINGLE_SELECT_RADIO("caption.answerType.singleSelectRadio"),
	CAPTION_ANSWER_TYPE_MULTI_SELECT("caption.answerType.multiSelect"),
	CAPTION_ANSWER_TYPE_TEXT_AREA("caption.answer.type.text.area"),
	
	CAPTION_ANSWER_FORMAT_TEXT("caption.answerFormat.text"),
	CAPTION_ANSWER_FORMAT_NUMBER("caption.answerFormat.number"),
	CAPTION_ANSWER_FORMAT_DATE("caption.answerFormat.date"),
	CAPTION_ANSWER_FORMAT_POSTAL_CODE("caption.answerFormat.postalCode"),
	
	CAPTION_ANSWER_SUBFORMAT_TEXT("caption.answer.subformat.text"),
	CAPTION_ANSWER_SUBFORMAT_EMAIL("caption.answer.subformat.email"),
	CAPTION_ANSWER_SUBFORMAT_PHONE("caption.answer.subformat.phone"),
	CAPTION_ANSWER_SUBFORMAT_IBAN("caption.answer.subformat.iban"),
	CAPTION_ANSWER_SUBFORMAT_BSN("caption.answer.subformat.bsn"),
	CAPTION_ANSWER_SUBFORMAT_AMMOUNT("caption.answer.subformat.ammount"),
	CAPTION_ANSWER_SUBFORMAT_NUMBER("caption.answer.subformat.number"),
	CAPTION_ANSWER_SUBFORMAT_FLOAT("caption.answer.subformat.float"),
	CAPTION_ANSWER_SUBFORMAT_DATE("caption.answer.subformat.date"), 
	CAPTION_ANSWER_SUBFORMAT_DATE_PAST("caption.answer.subformat.date.past"),
	CAPTION_ANSWER_SUBFORMAT_DATE_FUTURE("caption.answer.subformat.date.future"),
	CAPTION_ANSWER_SUBFORMAT_POSTAL_CODE("caption.answer.subformat.postal.code"),
	
	CAPTION_FORM_WORK_STATUS_DESIGN("caption.form.work.status.design"),
	CAPTION_FORM_WORK_STATUS_FINAL_DESIGN("caption.form.work.status.final.design"),
	CAPTION_FORM_WORK_STATUS_DEVELOPMENT("caption.form.work.status.development"),
	CAPTION_FORM_WORK_STATUS_TEST("caption.form.work.status.test"),
	CAPTION_FORM_WORK_STATUS_PRODUCTION("caption.form.work.status.production"),
	
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
	COMMON_TOOLTIP_INSERT_BLOCK("tooltip.insert.block"),
	TOOLTIP_EDIT_FORM_DESIGN("tooltip.edit.form.design"),
	TOOLTIP_EDIT_FORM_FLOW("tooltip.edit.form.flow"),
	TOOLTIP_VALIDATE_FORM("tooltip.validate.form"),
	TOOLTIP_IMPACT_ANALISYS("tooltip.impact.analisys"),
	
	COMMON_WARNING_TITLE_FORM_NOT_CREATED("common.warning.title.form.not.created"),
	COMMON_WARNING_DESCRIPTION_FORM_NEEDS_NAME("common.warning.description.form.needs.name"),
	COMMON_WARNING_TITLE_BLOCK_NOT_CREATED("common.warning.title.block.not.created"),
	COMMON_WARNING_DESCRIPTION_BLOCK_NEEDS_NAME("common.warning.description.block.needs.name"),
	
	COMMON_ERROR_FIELD_TOO_LONG("common.error.field.too.long"),
	COMMON_ERROR_NAME_IS_IN_USE("common.error.name.is.in.use"),
	COMMON_ERROR_ELEMENT_NOT_DELETED("common.error.element.not.deleted"),
	COMMON_ERROR_UNEXPECTED_ERROR("common.error.unexpected.error"),
	
	//Manager
	CAPTION_NEW_FORM("caption.new.form"),
	CAPTION_NEW_FORM_VERSION("caption.new.form.version"),
	CAPTION_READ_ONLY("caption.read.only"),
	
	TOOLTIP_NEW_FORM("tooltip.new.form"),
	TOOLTIP_NEW_FORM_VERSION("tooltip.new.form.version"),
	
	NULL_VALUE_NEW_FORM("null.value.new.form"),
	NULL_VALUE_NEW_BLOCK("null.value.new.block"),
		
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
	FORM_TABLE_COLUMN_ORGANIZATION("caption.form.table.organization"),
	
	WARNING_CAPTION_NOT_ALLOWED("warning.caption.not.allowed"),
	WARNING_DESCRIPTION_NOT_ENOUGH_RIGHTS("warning.description.not.enough.rights"),

	//BlockManager
	CAPTION_NEW_BLOCK("caption.new.block"),
	TOOLTIP_NEW_BLOCK("caption.new.block"),
	
	//Designer
	CAPTION_NEW_CATEGORY("caption.new.category"),
	CAPTION_NEW_SUBCATEGORY("caption.new.subcategory"),
	CAPTION_NEW_GROUP("caption.new.group"),
	CAPTION_NEW_QUESTION("caption.new.question"),
	CAPTION_NEW_TEXT("caption.new.text"),
	CAPTION_NEW_SYSTEM_FIELD("caption.new.systemField"),
	CAPTION_NEW_ANSWER("caption.new.answer"),
	CAPTION_NAME("caption.name"),
	CAPTION_VALUE("caption.value"),
	CAPTION_LABEL("caption.label"),
	CAPTION_FIELD("caption.field"),
	CAPTION_DESCRIPTION("caption.description"),
	CAPTION_MANDATORY("caption.mandatory"),
	CAPTION_ANSWER_TYPE("caption.answer.type"),
	CAPTION_ANSWER_FORMAT("caption.answer.format"),
	CAPTION_ANSWER_SUBFORMAT("caption.answer.subformat"),
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
	CAPTION_PROPERTIES_SYSTEM_FIELD("caption.properties.system.field"),
	CAPTION_SAVE_AS_BLOCK("caption.save.as.block"),
	CAPTION_INSERT_NEW_BLOCK("caption.insert.new.block"),
	CAPTION_WINDOW_MOVE("caption.window.move"),
	CAPTION_ORGANIZATION("caption.organization"),
	CAPTION_VALIDATE_DUPLICATE_NAME("caption.validate.duplicate.name"),
	CAPTION_VALIDATE_DUPLICATE_ANSWER_VALUE("caption.validate.duplicate.answer.value"),
	
	TOOLTIP_NEW_CATEGORY("tooltip.new.category"),
	TOOLTIP_NEW_SUBCATEGORY("tooltip.new.subcategory"),
	TOOLTIP_NEW_GROUP("tooltip.new.group"),
	TOOLTIP_NEW_QUESTION("tooltip.new.question"),
	TOOLTIP_NEW_TEXT("tooltip.new.text"),
	TOOLTIP_NEW_SYSTEM_FIELD("tooltip.new.systemField"),
	TOOLTIP_NEW_ANSWER("tooltip.new.answer"),
	TOOLTIP_SAVE_AS_BLOCK("tooltip.save.as.block"),
	
	ERROR_SUBCATEGORY_NOT_INSERTED("error.subcategory.not.inserted"),
	ERROR_GROUP_NOT_INSERTED("error.group.not.inserted"),
	ERROR_QUESTION_NOT_INSERTED("error.question.not.inserted"),
	ERROR_TEXT_NOT_INSERTED("error.text.not.inserted"),
	ERROR_ANSWER_NOT_INSERTED("error.answer.not.inserted"),
	ERROR_SYSTEM_FIELD_NOT_INSERTED("error.system.field.not.inserted"),
	
	INFO_MESSAGE_CAPTION_SAVE("info.message.caption.save"),
	INFO_MESSAGE_DESCRIPTION_SAVE("info.message.description.save"),
	INFO_MESSAGE_FORM_IS_READ_ONLY("info.message.form.is.read.only"),
	
	TEXT_PROCEED_FORM_CLOSE("text.proceed.form.close"),
	
	WARNING_CAPTION_NOT_VALID("warning.caption.not.valid"), 
	WARNING_DESCRIPTION_NOT_VALID("warning.description.not.valid"),
	WARNING_CAPTION_SAME_ORIGIN("warning.caption.same.origin"), 
	WARNING_DESCRIPTION_SAME_ORIGIN("warning.description.same.origin"), 
	WARNING_DESCRIPTION_NEW_VERSION_WHEN_DESIGN("warning.description.new.version.when.design"),
	WARNING_DESCRIPTION_ORIGIN_INCLUDED_IN_DESTINY("warning.description.origin.included.in.destiny"), 
	WARNING_DESCRIPTION_REPEATED_CATEGORY_NAME("warning.description.repeated.category.name"),
	
	//Flow
	TABLE_RULE_TITLE_ORIGIN("table.rule.title.origin"),
	TABLE_RULE_TITLE_TYPE("table.rule.title.type"),
	TABLE_RULE_TITLE_DESTINY("table.rule.title.destiny"),
	TABLE_RULE_TITLE_CONDITION("table.rule.title.condition"),
	TABLE_RULE_TITLE_CREATION_DATE("table.rule.title.creation.date"),
	TABLE_RULE_TITLE_UPDATE_DATE("table.rule.title.update.date"),
	
	CAPTION_NEW_RULE("caption.new.rule"), 
	CAPTION_EDIT_RULE("caption.edit.rule"),
	CAPTION_SEARCH_ORIGIN("caption.search.origin"),
	CAPTION_WINDOW_SELECT_FORM_ELEMENT("caption.window.select.form.element"),
	CAPTION_FROM("caption.from"),
	CAPTION_TO("caption.to"),
	CAPTION_RULE_TYPE_NORMAL("caption.rule.type.normal"), 
	CAPTION_RULE_TYPE_OTHERS("caption.rule.type.others"),
	CAPTION_RULE_TYPE_LOOP("caption.rule.type.loop"), 
	CAPTION_RULE_TYPE_END_LOOP("caption.rule.type.end.loop"), 
	CAPTION_RULE_TYPE_END_FORM("caption.rule.type.end.form"),
	CAPTION_RULE_TYPE("caption.rule.type"),
	CAPTION_FILTER("caption.filter"), 
	CAPTION_FILTER_ORIGIN("caption.filter.origin"),
	CAPTION_FILTER_DESTINY("caption.filter.destiny"),
	CAPTION_ZOOM_SLIDER("caption.zoom.slider"),
	CAPTION_REDRAW("caption.redraw"),
	CAPTION_CLONE_RULE("caption.clone.rule"),
	CAPTION_REMOVE_RULE("caption.remove.rule"),
	CAPTION_INSERT_QUESTION_REFENCE("caption.insert.question.reference"),
	CAPTION_INSERT_ANSWER_REFENCE("caption.insert.answer.reference"),
	CAPTION_OTHERS("caption.others"),
	CAPTION_VALIDATE_CONDITION("caption.validate.condition"),
	CAPTION_OK_EMPTY_EXPRESSION("caption.ok.empty.expression"),
	CAPTION_OK_VALID_EXPRESSION("caption.ok.valid.expression"),
	CAPTION_OPERATORS("caption.operators"),
	
	TOOLTIP_NEW_RULE("tooltip.new.rule"),
	TOOLTIP_EDIT_RULE("tooltip.edit.rule"),
	TOOLTIP_SEARCH_ORIGIN("tooltip.search.origin"),
	TOOLTIP_REDRAW("tooltip.redraw"),
	TOOLTIP_CLONE_RULE("tooltip.clone.rule"),
	TOOLTIP_REMOVE_RULE("tooltip.remove.rule"),
	TOOLTIP_INSERT_QUESTION_REFENCE("tooltip.insert.question.reference"),
	TOOLTIP_INSERT_ANSWER_REFENCE("tooltip.insert.answer.reference"),
	
	WARNING_CAPTION_RULE_ORIGIN_INVALID("warning.caption.rule.origin.invalid"),
	WARNING_CAPTION_RULE_NOT_CORRECT("warning.caption.rule.not.correct"),
	
	WARNING_DESCRIPTION_CAN_ONLY_SELECT_QUESTIONS("warning.description.can.only.select.questions"),
	WARNING_DESCRIPTION_DESTINY_IS_NULL("warning.description.destiny.is.null"), 
	WARNING_DESCRIPTION_ORIGIN_IS_NULL("warning.description.origin.is.null"),
	WARNING_DESCRIPTION_SAME_ORIGIN_AND_DESTINY("warning.description.same.origin.and.destiny"),
	WARNING_DESCRIPTION_DESTINY_IS_BEFORE_ORIGIN("warning.description.destiny.is.before.origin"),
	WARNING_DESCRIPTION_RULE_BAD_FORMED("warning.description.rule.bad.formed"),
	
	NULL_VALUE_SEARCH_ORIGIN("null.value.search.origin"),
	NULL_VALUE_SEARCH_DESTINY("null.value.search.destiny"),
		
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
