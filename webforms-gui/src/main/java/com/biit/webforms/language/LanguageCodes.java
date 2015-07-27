package com.biit.webforms.language;

import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.common.language.ServerTranslate;

public enum LanguageCodes implements ILanguageCode {
	//Common
	APPLICATION_NAME("application.name"),
	COMMON_CAPTION_NAME("caption.name"),
	COMMON_CAPTION_DESCRIPTION("caption.description"),
	COMMON_CAPTION_EDIT_DESCRIPTION("caption.edit.description"),
	COMMON_CAPTION_SETTINGS("caption.settings"),
	COMMON_CAPTION_FORM_MANAGER("caption.form.manager"),
	COMMON_CAPTION_BUILDING_BLOCK_MANAGER("caption.buildingBlock.manager"),
	COMMON_CAPTION_OTHER_ELEMENTS_SUBMENU("caption.other.elements.submenu"),
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
	COMMON_CAPTION_LINK_BLOCK("caption.link.block"),
	COMMON_CAPTION_GROUP("common.caption.group"),
	COMMON_CAPTION_SHOW("caption.show"),
	COMMON_CAPTION_HIDE("caption.hide"),
	CAPTION_EDIT_FORM_DESIGN("caption.edit.form.design"),
	CAPTION_EDIT_FORM_FLOW("caption.edit.form.flow"),
	CAPTION_VALIDATE_CONDITIONS("caption.validate.conditions"),
	CAPTION_VALIDATE_FORM("caption.validate.form"),
	CAPTION_IMPACT_ANALYSIS("caption.impact.analysis"),
	IMPACT_ANALYSIS_TEXT("impact.analysis.text"),
	CAPTION_SETTINGS_TITLE("caption.settings.title"),
	CAPTION_SETTINGS_LOG_OUT("caption.setting.log.out"),
	CAPTION_PROCEED_LOG_OUT("caption.proceed.log.out"),
	CAPTION_ABOUT_US("caption.about.us"),
	CAPTION_SETTINGS_CLOSE("caption.settings.close"),
	CAPTION_SEARCH("caption.search"),
	CAPTION_DOWNLOAD_FILE("caption.download.file"),
	CAPTION_DOWNLOAD_FILE_DESCRIPTION("caption.download.file.description"),
	CAPTION_PREVIEW_FILE("caption.preview.file"),
	CAPTION_PREVIEW_FILE_DESCRIPTION("caption.preview.file.description"),
	CAPTION_PUBLISH_FILE("caption.publish.file"),
	CAPTION_PUBLISH_FILE_DESCRIPTION("caption.publish.file.description"),
	CAPTION_PROCEED_LOSE_DATA("caption.proceed.lose.data"),
	CAPTION_SETTINGS_CLEAR_CACHE("caption.settings.clear.cache"),	
	INFO_CACHE_CLEARED("info.cache.cleared"),
	WARNING_CLEAR_CACHE("warning.clear.cache"),
	
	ERROR_MESSAGE_FILES_UPLOAD_NOT_COMPLETED("error.message.files.upload.not.completed"),
	
	CAPTION_PROCEED_MODIFY_STATUS("caption.proceed.modify.status"),
	
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
	CAPTION_ANSWER_SUBFORMAT_AMOUNT("caption.answer.subformat.amount"),
	CAPTION_ANSWER_SUBFORMAT_DATE("caption.answer.subformat.date"), 
	CAPTION_ANSWER_SUBFORMAT_DATE_PAST("caption.answer.subformat.date.past"),
	CAPTION_ANSWER_SUBFORMAT_DATE_FUTURE("caption.answer.subformat.date.future"),
	CAPTION_ANSWER_SUBFORMAT_POSTAL_CODE("caption.answer.subformat.postal.code"),
	CAPTION_ANSWER_SUBFORMAT_DATE_PERIOD("caption.answer.subformat.date.period"),
	
	CAPTION_FORM_WORK_STATUS_DESIGN("caption.form.work.status.design"),
	CAPTION_FORM_WORK_STATUS_FINAL_DESIGN("caption.form.work.status.final.design"),
	CAPTION_FORM_WORK_STATUS_DEVELOPMENT("caption.form.work.status.development"),
	CAPTION_FORM_WORK_STATUS_TEST("caption.form.work.status.test"),
	CAPTION_FORM_WORK_STATUS_PRODUCTION("caption.form.work.status.production"),
	
	CAPTION_YEAR("caption.year"),
	CAPTION_MONTH("caption.month"),
	CAPTION_DAY("caption.day"),
	
	CAPTION_CLOSE("caption.close"),
	ABOUT_US_TITLE("about.us.title"),
	ABOUT_US_TOOL_NAME("about.us.tool.name"),
	ABOUT_US_TOOL_PURPOUSE("about.us.tool.purpouse"),
	ABOUT_US_BIIT("about.us.biit"),
	ABOUT_US_REPORT("about.us.report"),
	
	COMMON_TOOLTIP_SETTINGS("tooltip.settings"),
	COMMON_TOOLTIP_FORM_MANAGER("tooltip.form.manager"),
	COMMON_TOOLTIP_BUILDING_BLOCK_MANAGER("tooltip.buildingBlock.manager"),
	COMMON_TOOLTIP_OTHER_ELEMENTS_SUBMENU("tooltip.other.elements.submenu"),
	COMMON_TOOLTIP_DESIGN("tooltip.design"),
	COMMON_TOOLTIP_FLOW("tooltip.flow"),
	COMMON_TOOLTIP_SAVE("tooltip.save"),
	COMMON_TOOLTIP_VALIDATE("tooltip.validate"),
	COMMON_TOOLTIP_FINISH("tooltip.finish"),
	COMMON_TOOLTIP_MOVE("tooltip.move"),
	COMMON_TOOLTIP_DELETE("tooltip.delete"),
	COMMON_TOOLTIP_UP("tooltip.up"),
	COMMON_TOOLTIP_DOWN("tooltip.down"),
	COMMON_TOOLTIP_SHOW("tooltip.show"),
	COMMON_TOOLTIP_HIDE("tooltip.hide"),
	COMMON_TOOLTIP_EXPORT_TO_PDF("tooltip.exportToPdf"),
	COMMON_TOOLTIP_INSERT_BLOCK("tooltip.insert.block"),
	COMMON_TOOLTIP_LINK_BLOCK("tooltip.link.block"),
	TOOLTIP_EDIT_FORM_DESIGN("tooltip.edit.form.design"),
	TOOLTIP_EDIT_FORM_FLOW("tooltip.edit.form.flow"),
	TOOLTIP_VALIDATE_FORM("tooltip.validate.form"),
	TOOLTIP_IMPACT_ANALISYS("tooltip.impact.analisys"),
	TOOLTIP_CLOSE("tooltip.close"),
	TOOLTIP_DOWNLOAD_FILE("tooltip.download.file"),
	TOOLTIP_ABOUT_US("tooltip.about.us"),
	TOOLTIP_SETTINGS_CLEAR_CACHE("tooltip.settings.clear.cache"),
	TOOLTIP_SETTINGS_LOG_OUT("tooltip.settings.log.out"),
	
	COMMON_WARNING_TITLE_FORM_NOT_CREATED("common.warning.title.form.not.created"),
	COMMON_WARNING_DESCRIPTION_FORM_NEEDS_NAME("common.warning.description.form.needs.name"),
	COMMON_WARNING_TITLE_BLOCK_NOT_CREATED("common.warning.title.block.not.created"),
	COMMON_WARNING_DESCRIPTION_BLOCK_NEEDS_NAME("common.warning.description.block.needs.name"),
	
	COMMON_ERROR_FIELD_TOO_LONG("common.error.field.too.long"),
	COMMON_ERROR_NAME_IS_IN_USE("common.error.name.is.in.use"),
	COMMON_ERROR_NAME_IS_IN_USE_DESCRIPTION("common.error.name.is.in.use.description"),
	COMMON_ERROR_ELEMENT_NOT_DELETED("common.error.element.not.deleted"),
	COMMON_ERROR_UNEXPECTED_ERROR("common.error.unexpected.error"),
	ERROR_FILE_TOO_LARGE("error.file.too.large"),
	
	//Manager
	CAPTION_NEW_FORM("caption.new.form"),
	CAPTION_NEW_FORM_VERSION("caption.new.form.version"),
	CAPTION_READ_ONLY("caption.read.only"),
	CAPTION_IN_USE("caption.in.use"),
	CAPTION_IMPORT_ABCD_FORM("caption.import.abcd.form"),
	CAPTION_IMPORT_JSON_FORM("caption.import.json.form"),  
	CAPTION_LINK_ABCD_FORM("caption.link.abcd.form"),
	CAPTION_LINK_WEBFORMS_FORM("caption.link.webforms.form"),
	CAPTION_EXPORT_XSD("caption.link.export.xsd"),
	CAPTION_EXPORT_JSON("caption.export.json"),
	CAPTION_EXPORT_FORM_METADATA("caption.export.form.metadata"),
	CAPTION_GENERATING_FILE("caption.generating.file"),
	CAPTION_GENERATED_FILE("caption.generated.file"),
	CAPTION_COMPARE_CONTENT("caption.compare.content"), 
	CAPTION_REMOVE_FORM("caption.form.remove"),
	CAPTION_REMOVE_BLOCK("caption.block.remove"),

	CAPTION_WAITING_VALIDATION("caption.waiting.validation"),	
	CAPTION_CONTENT_ANALYSIS("caption.content.analysis"),
	
	TITLE_DOWNLOAD_FILE("title.download.file"),
	
	TOOLTIP_NEW_FORM("tooltip.new.form"),
	TOOLTIP_NEW_FORM_VERSION("tooltip.new.form.version"),
	TOOLTIP_IMPORT_ABCD_FORM("tooltip.import.abcd.form"),
	TOOLTIP_IMPORT_JSON_FORM("tooltip.import.json.form"),
	TOOLTIP_LINK_ABCD_FORM("tooltip.link.abcd.form"),
	TOOLTIP_LINK_WEBFORMS_FORM("tooltip.link.webforms.form"),
	TOOLTIP_EXPORT_XSD("caption.link.export.xsd"),
	TOOLTIP_EXPORT_JSON("tooltip.export.json"),
	TOOLTIP_EXPORT_FORM_METADATA("tooltip.export.form.metadata"),
	TOOLTIP_COMPARE_CONTENT("tooltip.compare.content"), 
	TOOLTIP_CONTENT_ANALYSIS("tooltip.content.analysis"),
	
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
	FORM_TABLE_COLUMN_LINKED_FORM("caption.form.table.linked.form"),
	FORM_TABLE_COLUMN_LINKED_ORGANIZATION("caption.form.table.linked.organization"),
	FORM_TABLE_COLUMN_LINKED_VERSIONS("caption.form.table.linked.versions"),
	
	ERROR_CAPTION_NOT_ALLOWED("error.caption.not.allowed"),
	ERROR_CAPTION_IMPORT_FAILED("error.caption.import.failed"),
	
	ERROR_DESCRIPTION_NOT_ENOUGH_RIGHTS("error.description.not.enough.rights"),
	ERROR_DESCRIPTION_NOT_VALID_ABCD_FORM("error.description.not.valid.abcd.form"),	
	ERROR_DESCRIPTION_NAME_NOT_VALID("error.description.name.not.valid"), 
	ERROR_DESCRIPTION_NULL_ORGANIZATION("error.description.null.organization"),
	
	ERROR_ACCESSING_DATABASE("error.database.access"),
	ERROR_ACCESSING_DATABASE_DESCRIPTION("error.database.description"),

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
	CAPTION_NEW_SUBANSWER("caption.new.subanswer"),
	CAPTION_NAME("caption.name"),
	REFERENCE_CAPTION_NAME("reference.caption.name"),
	CAPTION_TECHNICAL_NAME("caption.technical.name"),
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
	REFERENCE_CAPTION_VERSION("reference.caption.version"),
	CAPTION_PROPERTIES_TEXT("caption.properties.text"),
	CAPTION_PROPERTIES_ANSWER("caption.properties.answer"),
	CAPTION_PROPERTIES_QUESTION("caption.properties.question"),
	CAPTION_PROPERTIES_GROUP("caption.properties.group"),
	CAPTION_PROPERTIES_SUBCATEGORY("caption.properties.subcategory"),
	CAPTION_PROPERTIES_CATEGORY("caption.properties.category"),
	CAPTION_PROPERTIES_FORM("caption.properties.form"),
	CAPTION_PROPERTIES_SYSTEM_FIELD("caption.properties.system.field"),
	CAPTION_PROPERTIES_DYNAMIC_QUESTION("caption.properties.dynamic.question"),
	CAPTION_SAVE_AS_BLOCK("caption.save.as.block"),
	CAPTION_INSERT_NEW_BLOCK("caption.insert.new.block"),
	CAPTION_LINK_BLOCK("caption.link.block"),
	CAPTION_WINDOW_MOVE("caption.window.move"),
	CAPTION_ORGANIZATION("caption.organization"),
	CAPTION_VALIDATE_DUPLICATE_NAME("caption.validate.duplicate.name"),
	CAPTION_VALIDATE_DUPLICATE_ANSWER_VALUE("caption.validate.duplicate.answer.value"),
	CAPTION_NAME_TOO_LARGE("caption.name.too.large"),
	CAPTION_LINKED_FORM("caption.linked.form"),
	CAPTION_NEW_DYNAMIC_ANSWER("caption.new.dynamic.answer"),
	TOOLTIP_NEW_DYNAMIC_ANSWER("tooltip.new.dynamic.answer"),
	
	TOOLTIP_NEW_CATEGORY("tooltip.new.category"),
	TOOLTIP_NEW_SUBCATEGORY("tooltip.new.subcategory"),
	TOOLTIP_NEW_GROUP("tooltip.new.group"),
	TOOLTIP_NEW_QUESTION("tooltip.new.question"),
	TOOLTIP_NEW_TEXT("tooltip.new.text"),
	TOOLTIP_NEW_SYSTEM_FIELD("tooltip.new.systemField"),
	TOOLTIP_NEW_ANSWER("tooltip.new.answer"),
	TOOLTIP_NEW_SUBANSWER("tooltip.new.subanswer"),
	TOOLTIP_SAVE_AS_BLOCK("tooltip.save.as.block"),
	
	ERROR_CATEGORY_NOT_INSERTED_IN_BLOCK("error.category.not.inserted.in.block"),
	ERROR_SUBCATEGORY_NOT_INSERTED("error.subcategory.not.inserted"),
	ERROR_GROUP_NOT_INSERTED("error.group.not.inserted"),
	ERROR_QUESTION_NOT_INSERTED("error.question.not.inserted"),
	ERROR_TEXT_NOT_INSERTED("error.text.not.inserted"),
	ERROR_ANSWER_NOT_INSERTED("error.answer.not.inserted"),
	ERROR_SYSTEM_FIELD_NOT_INSERTED("error.system.field.not.inserted"),
	ERROR_FORM_NOT_VALID("error.form.not.valid"),
	ERROR_TREE_OBJECT_FLOW_DEPENDENCY("error.tree.object.flow.dependency"),
	
	VALIDATE_FORM("validate.form"),
	
	INFO_MESSAGE_FORM_CAPTION_SAVE("info.message.form.caption.save"),
	INFO_MESSAGE_FORM_DESCRIPTION_SAVE("info.message.form.description.save"),
	INFO_MESSAGE_BLOCK_CAPTION_SAVE("info.message.block.caption.save"),
	INFO_MESSAGE_BLOCK_DESCRIPTION_SAVE("info.message.block.description.save"),
	INFO_MESSAGE_FORM_IS_READ_ONLY("info.message.form.is.read.only"),
	
	TEXT_PROCEED_FORM_CLOSE("text.proceed.form.close"),
	
	WARNING_CAPTION_NOT_VALID("warning.caption.not.valid"), 
	WARNING_DESCRIPTION_NOT_VALID("warning.description.not.valid"),
	WARNING_CAPTION_SAME_ORIGIN("warning.caption.same.origin"), 
	WARNING_DESCRIPTION_SAME_ORIGIN("warning.description.same.origin"), 
	WARNING_DESCRIPTION_NEW_VERSION_WHEN_DESIGN("warning.description.new.version.when.design"),
	WARNING_DESCRIPTION_ORIGIN_INCLUDED_IN_DESTINY("warning.description.origin.included.in.destiny"), 
	WARNING_DESCRIPTION_REPEATED_CATEGORY_NAME("warning.description.repeated.category.name"),
	WARNING_REMOVE_ELEMENT("warning.element.remove"),
	
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
	CAPTION_CLEAN_FLOW("caption.clean.flow"),
	CAPTION_INSERT_QUESTION_REFENCE("caption.insert.question.reference"),
	CAPTION_INSERT_ANSWER_REFENCE("caption.insert.answer.reference"),
	CAPTION_OTHERS("caption.others"),
	CAPTION_VALIDATE_CONDITION("caption.validate.condition"),
	CAPTION_OK_EMPTY_EXPRESSION("caption.ok.empty.expression"),
	CAPTION_OK_VALID_EXPRESSION("caption.ok.valid.expression"),
	CAPTION_OPERATORS("caption.operators"),
	CAPTION_PRINT_FLOW("caption.print.flow"),
	CAPTION_TO_XFORMS("caption.export.xforms"),
	CAPTION_DATE_PERIOD_NULL("caption.date.period.null"),
	
	TOOLTIP_NEW_RULE("tooltip.new.rule"),
	TOOLTIP_EDIT_RULE("tooltip.edit.rule"),
	TOOLTIP_SEARCH_ORIGIN("tooltip.search.origin"),
	TOOLTIP_REDRAW("tooltip.redraw"),
	TOOLTIP_CLONE_RULE("tooltip.clone.rule"),
	TOOLTIP_REMOVE_RULE("tooltip.remove.rule"),
	TOOLTIP_CLEAN_FLOW("tooltip.clean.flow"),
	TOOLTIP_INSERT_QUESTION_REFENCE("tooltip.insert.question.reference"),
	TOOLTIP_INSERT_ANSWER_REFENCE("tooltip.insert.answer.reference"),
	TOOLTIP_PRINT_FLOW("tooltip.print.flow"),
	TOOLTIP_TO_XFORMS("tooltip.export.xforms"),
	
	WARNING_CAPTION_RULE_ORIGIN_INVALID("warning.caption.rule.origin.invalid"),
	ERROR_CAPTION_RULE_NOT_CORRECT("warning.caption.rule.not.correct"),
	
	WARNING_DESCRIPTION_CAN_ONLY_SELECT_QUESTIONS("warning.description.can.only.select.questions"),
	ERROR_DESCRIPTION_DESTINY_IS_NULL("warning.description.destiny.is.null"), 
	ERROR_DESCRIPTION_ORIGIN_IS_NULL("warning.description.origin.is.null"),
	ERROR_DESCRIPTION_SAME_ORIGIN_AND_DESTINY("warning.description.same.origin.and.destiny"),
	ERROR_DESCRIPTION_DESTINY_IS_BEFORE_ORIGIN("warning.description.destiny.is.before.origin"),
	ERROR_DESCRIPTION_RULE_BAD_FORMED("warning.description.rule.bad.formed"),
	ERROR_DESCRIPTION_CONDITION_BAD_FORMED("warning.description.condition.bad.formed"),
	
	ERROR_MESSAGE_VALUE_HAS_WRONG_FORMAT("error.message.value.has.wrong.format"),
	ERROR_MESSAGE_FIELDS_ARE_NOT_FILLED_CORRECTLY("error.message.fields.are.not.filled.correctly"),
	
	VALIDATOR_ERROR_PATTERN("validator.error.pattern"),
	
	NULL_VALUE_SEARCH_ORIGIN("null.value.search.origin"),
	NULL_VALUE_SEARCH_DESTINY("null.value.search.destiny"),
	
	ERROR_USER_SERVICE("error.userService"), 
	
	//Validation
	CAPTION_COMPLETE_VALIDATION("caption.complete.validation"),
	CAPTION_VALIDATE_STRUCTURE("caption.validation.structure"),
	CAPTION_VALIDATE_FLOW("caption.validation.flow"),
	CAPTION_VALIDATE_ABCD_LINK("caption.validation.abcd.link"),
	CAPTION_COMPARE_ABCD_LINK("caption.compare.abcd.link"),	
	
	TOOLTIP_COMPLETE_VALIDATION("caption.complete.validation"),
	TOOLTIP_VALIDATE_STRUCTURE("caption.validation.structure"),
	TOOLTIP_VALIDATE_FLOW("caption.validation.flow"),
	TOOLTIP_VALIDATE_ABCD_LINK("caption.validation.abcd.link"), 
	TOOLTIP_VALIDATE_CONDITIONS("tooltip.validate.conditions"),
	TOOLTIP_COMPARE_ABCD_LINK("tooltip.compare.abcd.link"),
	
	MESSAGE_VALIDATION_FINISHED_CORRECTLY("message.validation.finished.correctly"), 
	MESSAGE_VALIDATION_NO_ABCD_FORMS_LINKED("message.validation.no.abcd.forms.linked"), 
	MESSAGE_VALIDATION_All_LINKED_FORMS_CORRECT("message.validation.all.linked.forms.correct"),
	MESSAGE_VALIDATION_FLOW_NOT_PASSED_CORRECTLY("message.validation.flow.not.passed.correctly"),
	
	ERROR_FORM_STRUCTURE_COULD_NOT_BE_VALIDATED("error.form.struture.could.not.be.validated"),
	ERROR_ABCD_FORM_LINKED_NOT_FOUND("error.abcd.form.linked.not.found"),
	
	//Compare Structure
	CAPTION_COMPARE_STRUCTURE("caption.compare.structure"),	
	CAPTION_UPLOAD("caption.upload"), 
	CAPTION_REMOVE("caption.remove"),
	CAPTION_REMOVE_ALL("caption.remove.all"),  
	CAPTION_VALIDATE("caption.validate"), 
	CAPTION_VALIDATE_ALL("caption.validate.all"),
	CAPTION_FILE_CONTENT("caption.file.content"),
	CAPTION_FILE_RESULT("caption.file.result"),
	
	TOOLTIP_COMPARE_STRUCTURE("tooltip.compare.structure"),
	TOOLTIP_UPLOAD("tooltip.upload"), 
	TOOLTIP_REMOVE("tooltip.remove"), 
	TOOLTIP_REMOVE_ALL("tooltip.remove.all"),
	TOOLTIP_VALIDATE("tooltip.validate"),
	TOOLTIP_VALIDATE_ALL("tooltip.validate.all"),
	
	XSD_XML_VALIDATION_ALL_OK("xsd.xml.validation.all.ok"),
	XSD_XML_VALIDATION_COULD_NOT_BE_DONE("xsd.xml.validation.could.not.be.done"),
	
	WARNING_VALIDATE_XML_STRUCTURE_NOT_SELECTED("warning.validate.xml.structure.not.selected"),
	
	ERROR_MESSAGE_CURRENT_FORM_STRUCTURE_IS_NOT_VALID("error.message.current.form.structure.is.not.valid"),
	
	// Compare content
	CAPTION_COMPARE_XML_CONTENT("caption.compare.xml.content"),
	CAPTION_UPLOAD_XML_ORIGINAL("caption.upload.xml.original"),
	CAPTION_UPLOAD_XML_PROCESSED("caption.upload.xml.processed"),
	CAPTION_COMPARATION("caption.comparation"),
	CAPTION_REMOVE_XML("caption.remove.xml"),
	CAPTION_CLEAN_XML("caption.clean.xml"),

	TOOLTIP_COMPARE_XML_CONTENT("tooltip.compare.xml.content"),
	TOOLTIP_UPLOAD_XML_ORIGINAL("tooltip.upload.xml.original"),
	TOOLTIP_UPLOAD_XML_PROCESSED("tooltip.upload.xml.processed"),
	TOOLTIP_REMOVE_XML("tooltip.remove.xml"),
	TOOLTIP_CLEAN_XML("tooltip.clean.xml"), 
	
	WARNING_DESCRIPTION_NUMBER_OF_ORIGINAL_AND_PROCESSED_FILES_DOESNT_MATCH("warning.description.number.of.original.and.processed.files.doesnt.match"),
	
	ERROR_UNEXPECTED_ERROR_IN_COMPARATION("error.unexpected.error.in.comparation"),
	
	//Orbeon
	ORBEON_INVALID_FORMAT_EMAIL("orbeon.invalid.format.email"),
	ORBEON_INVALID_FORMAT_PHONE("orbeon.invalid.format.phone"),
	ORBEON_INVALID_FORMAT_POSTALCODE("orbeon.invalid.format.postalcode"),
	ORBEON_INVALID_FORMAT_IBAN("orbeon.invalid.format.iban"),
	ORBEON_INVALID_FORMAT_BSN("orbeon.invalid.format.bsn"),
	ORBEON_INVALID_FORMAT_DATE_PAST("orbeon.invalid.format.date.past"),
	ORBEON_INVALID_FORMAT_DATE_FUTURE("orbeon.invalid.format.date.future"),
	ORBEON_INVALID_FORMAT_DATE_BIRTHDAY("orbeon.invalid.format.date.birthday"),
	
	GRAPHVIZ_EXEC_NOT_FOUND("graphviz.exec.not.found"),
	GRAPHVIZ_EXEC_NOT_FOUND_DESCRIPTION("graphviz.exec.not.found.description"),
	
	EXPRESSION_CHECKER_VALID("expression.checker.valid"),
	EXPRESSION_CHECKER_INVALID("expression.checker.invalid"),
	
	ERROR_FORM_NOT_PUBLISHED("error.form.not.published"),
	ERROR_FORM_ALREADY_EXISTS("error.form.already.exist"),	
	ERROR_XFORMS_USER_INVALID("error.form.user.invalid"),
	
	XFORM_PUBLISHED("form.published.correctly"), 
	
	CAPTION_DRAG_AND_DROP_FILES("caption.drag.and.drop.files"), 
	
	CAPTION_EXPORT("caption.export"),
	CAPTION_RULES("caption.rules"),
	TOOLTIP_EXPORT("tooltip.export"), 
	TOOLTIP_RULES("tooltip.rules"), 
	CAPTION_PREVIEW_XFORMS("caption.preview.xforms"), 
	CAPTION_PUBLISH_XFORMS("caption.publish.xforms"),
	CAPTION_DOWNLOAD_XFORMS("caption.download.xforms"),
	CAPTION_DOWNLOAD_XFORMS_MULTIPLE("caption.download.xforms.multiple"),
	TOOLTIP_PREVIEW_XFORMS("tooltip.preview.xforms"),
	TOOLTIP_PUBLISH_XFORMS("tooltip.publish.xforms"),
	TOOLTIP_DOWNLOAD_XFORMS("tooltip.download.xforms"),
	TOOLTIP_DOWNLOAD_XFORMS_MULTIPLE("tooltip.download.xforms.multiple"), 
	
	CAPTION_NEW("caption.new"), 
	TOOLTIP_NEW("tooltip.new"), 
	
	WARNING_ELEMENT_WTIH_SAME_NAME_EXIST("warning.element.same.name"), 
	
	CAPTION_EXPORT_XML("caption.export.xml"),
	TOOLTIP_EXPORT_XML("tooltip.export.xml"), 
	
	WARNING_NUMBER_OF_GENERATED_XML_NOT_VALID("warning.number.of.generated.xml.not.valid"), 
	CAPTION_SELECT_AMMOUNT_OF_XML_TO_GENERATE("caption.select.ammount.of.xml.to.generate"), 
	WARNING_DESCRIPTION_EMPTY_BLOCK("warning.description.empty.block"),
	
	INFO_USER_SESSION_EXPIRED("info.usersession.expired"),
	
	
	VALIDATION_DUPLICATED_NAMES("validation.duplicated.nested.name"),
	VALIDATION_DUPLICATED_NAMES_WITH_CHILDS("validation.duplicated.nested.name.with.childs"),
	VALIDATION_INVALID_ELEMENT_NAME("validation.invalid.element.name"),
	VALIDATION_BACKWARD_FLOW("validation.backward.flow"),
	VALIDATION_INVALID_DATE_UNIT("validation.invalid.date.unit"),
	VALIDATION_FLOW_ORIGIN_NOT_MANDATORY("validation.flow.origin.not.mandatory"),
	VALIDATION_INCOMPLETE_LOGIN_REPORT("validation.incomplete.login.report"),
	VALIDATION_INVALID_FLOW_CONDITION("validation.invalid.flow.condition"),	
	VALIDATION_INVALID_FLOW_SUBFORMAT("validation.invalid.flow.subformat"),
	VALIDATION_LINKED_FORM_ABCD_ELEMENT_NOT_FOUND("validation.linked.form.abcd.element.not.found"),
	VALIDATION_LINKED_FORM_ABCD_ANSWER_NOT_FOUND("validation.linked.form.abcd.answer.not.found"),
	VALIDATION_LINKED_FORM_ABCD_ELEMENT_IS_GROUP_NOT_QUESTION("validation.linked.form.abcd.element.is.group.not.question"),
	VALIDATION_LINKED_FORM_ABCD_ELEMENT_IS_QUESTION_NOT_GROUP("validation.linked.form.abcd.element.is.question.not.group"),
	VALIDATION_LINKED_FORM_ABCD_GROUP_REPEATABLE_STATUS_IS_DIFFERENT("validation.linked.form.abcd.group.repeatable.status.is.different"),
	VALIDATION_LINKED_FORM_STRUCTURE_NOT_COMPATIBLE("validation.linked.form.structure.not.compatible"),
	VALIDATION_MULTIPLE_END_FORMS_FROM_SAME_ELEMENT("validation.multiple.end.forms.from.same.element"),
	VALIDATION_MULTIPLE_END_LOOPS_FROM_SAME_ELEMENT("validation.multiple.end.loops.from.same.element"),
	VALIDATION_MULTIPLE_FLOWS_WITH_SAME_ORIGIN_AND_DESTINY("validation.multiple.flows.with.same.origin.and.destiny"),
	VALIDATION_NO_SUBANSWERS_ALLOWED("validation.no.subanswers.allowed"),
	VALIDATION_NOT_VALID_CONDITION("valdiation.not.valid.condition"),
	VALIDATION_OTHERS_UNICITY_BROKEN("validation.others.unicity.broken"),
	VALIDATION_OTHERS_ORPHAN("validation.others.orphan"),
	VALIDATION_ELEMENT_NO_FLOW_IN("validation.element.no.flow.in"),
	VALIDATION_QUESTION_NOT_FOUND("validation.question.not.found"),
	VALIDATION_REDUNDANT_LOGIC("validation.redundant.logic"),
	VALIDATION_TOKEN_USES_NON_FINAL_ANSWER("validation.token.uses.non.final.answer"),
	VALIDATION_NULL_VALUE("validation.null.value"),
	VALIDATION_CONDITION_WITH_NOT_MANDATORY_QUESTION("validation.condition.with.not.mandatory.question"),
	VALIDATION_FLOW_BLOCKED_IN_QUESTION("validation.flow.blocked.in.question"),
	VALIDATION_MULTIPLE_DYNAMIC_ANSWERS_REFERENCE_SAME_QUESTION("validation.multiple.dynamic.answers.reference.same.question"),
	VALIDATION_DYNAMIC_ANSWER_NULL_VALUE("validation.dynamic.answer.null.value"), 
	VALIDATION_DYNAMIC_ANSWER_REFERENCE_INVALID("validation.dynamic.answer.reference.invalid"),
	VALIDATION_EMPTY_FLOW_IS_NOT_ALONE("validation.empty.flow.is.not.alone"),
	VALIDATION_CONDITION_IS_USELESS("validation.condition.is.useless"),
	
	WARNING_ABCD_FORM_LINKED_NOT_VALID("abcd.form.linked.not.valid"),
	WARNING_ABCD_FORM_LINKED_NOT_VALID_DESCRIPTION("abcd.form.linked.not.valid.description"),
	
	INPUT_PROMPT_FLOAT("input.prompt.float"),
	INPUT_PROMPT_NUMBER("input.prompt.number"),
	INPUT_PROMPT_AMOUNT("input.prompt.number"),
	INPUT_PROMPT_DATE_PERIOD("input.prompt.number"),
	INPUT_PROMPT_BSN("input.prompt.bsn"),
	INPUT_PROMPT_DATE("input.prompt.date"),
	INPUT_PROMPT_TEXT("input.prompt.text"),
	INPUT_PROMPT_EMAIL("input.prompt.email"),
	INPUT_PROMPT_POSTAL_CODE("input.prompt.postalcode"),
	INPUT_PROMPT_IBAN("input.prompt.iban"),
	INPUT_PROMPT_PHONE("input.prompt.phone"),
	
	CLEAN_FLOW_REPORT_TITLE("clean.flow.report.title"),
	CLEAN_FLOW_REPORT_CAPTION("clean.flow.report.caption"),
	CLEAN_FLOW_REPORT_OTHERS_RULE("clean.flow.report.others.rule"),
	CLEAN_FLOW_REPORT_USELESS_FLOW("clean.flow.report.useless.flow"),
	CLEAN_FLOW_REPORT_NO_RULES_CHANGED("clean.flow.report.no.rules.changed"),
	
	ERROR_LINK_BLOCK_NOT_COMPLETE("error.link.block.not.complete"),
	ERROR_READ_ONLY_ELEMENT("error.read.only.element"),
	
	ERROR_ELEMENT_CANNOT_BE_REMOVED_TITLE("error.element.cannot.be.removed.title"),
	ERROR_ELEMENT_CANNOT_BE_REMOVED_LINKED_FORM_DESCRIPTION("error.element.cannot.be.removed.linked.form.description"),
	ERROR_ELEMENT_CANNOT_BE_REMOVED_LINKED_BLOCK_DESCRIPTION("error.element.cannot.be.removed.linked.block.description"),
	ERROR_ELEMENT_CANNOT_BE_REMOVED_BLOCK_ELEMENT_DESCRIPTION("error.element.cannot.be.removed.linked.block.element.description"),
	ERROR_ELEMENT_CANNOT_BE_HIDDEN_TITLE("error.element.cannot.be.hidden.title"),
	ERROR_ELEMENT_CANNOT_BE_HIDDEN_DESCRIPTION("error.element.cannot.be.removed.hidden.description"),
	
	ERROR_FORM_WITH_BLOCK_IS_IN_USE("error.form.with.block.is.in.use"),
	ERROR_FORM_WITH_BLOCK_IS_IN_USE_DESCRIPTION("error.form.with.block.is.in.use.description"),
	
	ERROR_ELEMENT_CANNOT_BE_SAVED("error.element.cannot.be.saved"),
	ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION("error.element.cannot.be.saved.description"), 
	
	ERROR_INVALID_CONDITION("error.invalid.condition"),
	
	PAGE_NOT_FOUND("page.not.found"),
	PAGE_ERROR("page.error"), 
	
	WARNING_CANNOT_SHOW_ELEMENT_DUE_TO_HIDDEN_PARENT("warning.cannot.show.element"), 
	
	CAPTION_ADD_REFERENCE("caption.add.reference"), 
	CAPTION_REMOVE_REFERENCE("caption.remove.reference"), 
	TOOLTIP_ADD_REFERENCE("tooltip.add.reference"), 
	TOOLTIP_REMOVE_REFERENCE("tooltip.remove.reference"), 
	
	CAPTION_SEARCH_DYNAMIC_REFERENCE("caption.search.dynamic.reference"), 
	NULL_VALUE_SEARCH_DYNAMIC_REFERENCE("null.value.search.dynamic.reference"), 
	DYNAMIC_ANSWER_LABEL("dynamic.answer.label"), 
	
	ERROR_DYNAMIC_ANSWER_DEPENDENCY("error.dynamic.answer.dependency"), 
	
	CAPTION_EDIT_WEBSERVICE_CALL("caption.edit.webservice.call"),
	TOOLTIP_EDIT_WEBSERVICE_CALL("tooltip.edit.webservice.call"), 
	
	WEBSERVICE_CALL_TABLE_NAME("webservice.call.table.name"), 
	CAPTION_WEBSERVICE_CALL_NAME("caption.webservice.call.name"), 
	CAPTION_WEBSERVICE_NAME("caption.webservice.name"), 
	CAPTION_WEBSERVICE_CALL_TRIGGER("caption.webservice.call.trigger"), 
	
	CAPTION_ADD_WEBSERVICE_CALL("caption.add.webservice.call"), 
	TOOLTIP_ADD_WEBSERVICE_CALL("tooltip.add.webservice.call"), 
	CAPTION_REMOVE_WEBSERVICE_CALL("caption.remove.webservice.call"), 
	TOOLTIP_REMOVE_WEBSERVICE_CALL("tooltip.remove.webservice.call"), 
	
	WEBSERVICES_TABLE_NAME("webservices.table.name"), 
	WEBSERVICES_TABLE_DESCRIPTION("webservices.table.description"), 
	WEBSERVICES_TABLE_URL("webservices.table.url"), 
	
	PORT_TABLE_NAME("port.table.name"), 
	PORT_TABLE_FORM_ELEMENT("port.table.form.element"), 
	
	CAPTION_EDIT_WEBSERVICE_LINK("caption.edit.webservice.link"), 
	TOOLTIP_EDIT_WEBSERVICE_LINK("tooltip.edit.webservice.link"), 
	CAPTION_REMOVE_WEBSERVICE_LINK("caption.remove.webservice.link"), 
	TOOLTIP_REMOVE_WEBSERVICE_LINK("caption.remove.webservice.link"), 
	
	TABLE_OUTPUT_LINK_IS_EDITABLE("table.output.link.is.editable"), 
	IS_EDITABLE("is.editable"), 
	IS_NOT_EDITABLE("is.not.editable"), 
	
	CAPTION_SELECT_FORM_ELEMENT("caption.select.form.element"), 
	CAPTION_IS_EDITABLE("caption.is.editable"), 
	
	CAPTION_WEBSERVICE_CALL_INPUT_LINK("caption.webservice.call.input.link"),
	CAPTION_WEBSERVICE_CALL_OUTPUT_LINK("caption.webservice.call.output.link"),
	CAPTION_WEBSERVICE_CALL_VALIDATION_LINK("caption.webservice.call.validation.link"), 
	
	WINDOW_EDIT_INPUT_LINK("window.edit.input.link"),
	WINDOW_EDIT_OUTPUT_LINK("window.edit.output.link"), 
	
	TABLE_VALIDATION_LINK_ERROR_CODE("table.validation.link.error.code"), 
	TABLE_VALIDATION_LINK_MESSAGE("table.validation.link.message"), 
	
	CAPTION_VALIDATION_LINK_ERROR_MESSAGE("caption.validation.link.error.message"),
	CAPTION_WINDOW_EDIT_VALIDATION_LINK("caption.window.edit.validation.link"), 
	
	PORT_TABLE_ELEMENT_TYPE("caption.port.table.element.type"), 
	PORT_TABLE_ELEMENT_FORMAT("caption.port.table.element.format"), 
	PORT_TABLE_ELEMENT_SUBFORMAT("caption.port.table.element.subformat"), 
	
	WEBSERVICE_CALL_CORRUPTION("webservice.call.corruption"), 
	WEBSERVICE_CALL_INCOMPATIBLE_FIELD("webservice.call.incompatible.field"), 
	WEBSERVICE_CALL_INPUT_AFTER_TRIGGER("webservice.call.input.after.trigger"), 
	WEBSERVICE_CALL_INPUT_NULL("webservice.call.input.null"), 
	WEBSERVICE_CALL_OUTPUT_AFTER_TRIGGER("webservice.call.output.after.trigger"), 
	WEBSERVICE_CALL_REFERENCES_UNEXISTING_WEBSERVICE("webservice.call.references.unexisting.webservice"),
	WEBSERVICE_CALL_TRIGGER_NULL("webservice.call.trigger.null"), 
	
	WEBSERVICE_ERROR_MESSAGE_NAME_NOT_FILLED("webservice.error.message.fields.are.not.filled.correctly"),
	
	ERROR_TREE_OBJECT_WEBSERVICE_CALL_DEPENDENCY("error.tree.object.webservice.call.dependency"),
	
	ERROR_INVALID_FORM_FOR_XFORMS("error.invalid.form.for.xforms"),
	ERROR_INVALID_FORM_FOR_XFORMS_DESCRIPTION("error.invalid.form.for.xforms.description"), 
	WARNING_WRONG_WEBSERVICE_CONFIGURATION("warning.wrong.webservice.configuration"), 
	
	ERROR_SELECT_BLOCK("error.select.block"), 
	
	CAPTION_DEFAULT_VALUE("caption.default.value"),
	
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
