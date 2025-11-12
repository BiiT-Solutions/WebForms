package com.biit.webforms.theme;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
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

import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.vaadin.server.ThemeResource;

public enum ThemeIcons implements IThemeIcon {

	SETTINGS("menu.lines.horizontal.svg"),

	ELEMENT_EDIT("element.edit.svg"), 
	ELEMENT_DELETE("element.delete.svg"), 
	ELEMENT_MOVE_UP("element.move.up.svg"), 
	ELEMENT_MOVE_DOWN("element.move.down.svg"),

	PAGE_FORM_MANAGER("page.form.manager.svg"), 
	PAGE_BUILDING_BLOCK_MANAGER("page.building.block.manager.svg"), 
	PAGE_FORM_EDITOR("page.form.edit.svg"), 
	PAGE_FORM_FLOW_EDITOR("page.flow.editor.svg"), 
	PAGE_BUILDING_BLOCK_EDITOR("page.building.block.edit.svg"), 
	PAGE_BUILDING_BLOCK_FLOW_EDITOR("page.flow.editor.svg"),

	FORM_MANAGER_ADD_FORM("form.page.svg"), 
	FORM_MANAGER_NEW_VERSION("form.new.version.svg"),

	FORM_SAVE("form.save.svg"), 
	FORM_VALIDATE("form.validate.svg"), 
	FORM_FINISH("form.protect.svg"),

	BUILDING_BLOCK_ADD("building.block.add.svg"),
	BUILDING_BLOCK_LINK("building.block.link.svg"),
	BUILDING_BLOCK_SAVE("building.block.save.svg"),

	DESIGNER_QUESTION_TYPE_CHECKLIST("question.checkmark.svg"), 
	DESIGNER_QUESTION_TYPE_RADIOBUTTON("question.radiobutton.svg"), 
	DESIGNER_QUESTION_TYPE_DATE("question.calendar.31.svg"), 
	DESIGNER_QUESTION_TYPE_NUMBER("question.textbox.numbers.svg"), 
	DESIGNER_QUESTION_TYPE_POSTALCODE("question.postcode.svg"), 
	DESIGNER_QUESTION_TYPE_TEXT("question.textbox.letters.svg"), 
	DESIGNER_QUESTION_TYPE_DROPDOWN("question.dropdown.svg"),
	DESIGNER_QUESTION_TYPE_SLIDER("question.slider.svg"),
	DESIGNER_GROUP_TABLE("group.table.svg"),
	DESIGNER_GROUP_TABLE_ROW("group.table.row.svg"),
	DESIGNER_GROUP_TABLE_COLUMN("group.table.column.svg"),
	DESIGNER_CATEGORY("category.svg"),
	DESIGNER_GROUP("group.svg"),
	DESIGNER_GROUP_LOOP("group.loop.svg"),
	DESIGNER_NEW_DYNAMIC_ANSWER("form.dynamic.element.svg"),
	DESIGNER_NEW_RANGED_ANSWERS("form.ranged.answer.svg"),
	DESIGNER_DYNAMIC_ANSWER("form.dynamic.element.svg"),
	DESIGNER_INFO_TEXT("infotext.svg"),
	DESIGNER_ATTACHED_FILES("attached.file.svg"),
	DESIGNER_SYSTEM_FIELD("systemfield.svg"),
	DESIGNER_NEW_CATEGORY("form.category.add.svg"),
	DESIGNER_NEW_SUBCATEGORY("form.subcategory.add.svg"),
	DESIGNER_NEW_GROUP("form.group.add.svg"),
	DESIGNER_NEW_QUESTION("form.question.add.svg"), 
	DESIGNER_NEW_SYSTEM_FIELD("form.systemfield.add.svg"), 
	DESIGNER_NEW_INFOTEXT("form.infotext.add.svg"),
	DESIGNER_NEW_ATTACHED_FILES("form.attached.file.svg"),
	DESIGNER_NEW_ANSWER("form.answer.add.svg"), 
	DESIGNER_NEW_SUBANSWER("form.subanswer.add.svg"),
	DESIGNER_MOVE("element.move.svg"),

	EXPORT_FORM_TO_PDF("file.pdf.svg"), 
	EXPORT_FORM_TO_XFORMS("form.xforms.svg"),

	EDIT_FORM_DESIGN("page.form.edit.svg"), 
	EDIT_FORM_FLOW("page.flow.editor.svg"), 
	VALIDATE_FORM("form.validate.svg"), 
	IMPACT_ANALYSIS("form.impact.analysis.svg"),

	ALERT("alert.svg"),

	SEARCH("search.svg"),

	RULE_ADD("flow.rule.add.svg"), 
	RULE_EDIT("flow.rule.edit.svg"), 
	RULE_CLONE("flow.rule.duplicate.svg"), 
	RULE_REMOVE("flow.rule.remove.svg"), 
	RULE_DIAGRAM_REDRAW("flow.diagram.refresh.svg"),
	RULE_CLEAN_FLOW("flow.clean.svg"),

	CONDITION_HELPER_FORM_REFERENCE("flow.helper.form.svg"), 
	CONDITION_HELPER_CONTROLS("flow.helper.control.svg"),

	FORM_MANAGER_IMPORT_ABCD_FORM("form.import.svg"), 
	FORM_MANAGER_LINK_ABCD_FORM("form.link.svg"),
	FORM_MANAGER_LINK_WEBFORMS_FORM("form.link.svg"),
	
	COMPLETE_VALIDATION("validate.document.svg"),
	VALIDATE_STRUCTURE("validate.structure.svg"),
	VALIDATE_FLOW("validate.flow.svg"),
	VALIDATE_ABCD_LINK("validate.link.svg"),
	COMPARE_ABCD_LINK("form.compare.abcd.svg"),
	
	EXPORT_XSD("form.xsd.svg"),

	COMPARE_XML_CONTENT("page.compare.svg"),
	UPLOAD_XML_ORIGINAL("document.upload.svg"),
	REMOVE_XML("appbar.table.delete.row.svg"),
	CLEAN_XML("appbar.table.clean.svg"),
	UPLOAD_XML_PROCESSED("document.upload.svg"), 
	
	EXPORT_XML("form.xml.svg"), 
	
	UPLOAD("document.upload.svg"),  
	REMOVE("element.delete.svg"), 
	REMOVE_ALL("element.delete.svg"), 
	VALIDATE("validate.document.svg"), 
	VALIDATE_ALL("validate.documents.svg"), 
	
	COMPARE_STRUCTURE("page.xml.svg"), 
	
	COMPARE_CONTENT("page.compare.svg"), 
	
	EXPORT("form.export.svg"), 
	PREVIEW_XFORMS("form.preview.svg"),
	PUBLISH_XFORMS("form.xforms.svg"),
	DOWNLOAD_XFORMS("form.download.svg"),
	DOWNLOAD_XFORMS_MULTIPLE("form.download.multiple.svg"),
	BUILDING_BLOCK_MENU("page.building.block.manager.svg"), 
	OTHER_ELEMENTS_MENU("form.design.others.svg"), 
	
	ABOUT_US("about.window.svg"), 
	CLEAR_CACHE("cache.clear.svg"), 
	LOG_OUT("user.logout.svg"), 
	
	NEW("form.create.svg"), 
	
	EXPORT_JSON("file.json.svg"), 
	
	VALIDATE_CONDITIONS("alert.svg"), 
	
	FORM_MANAGER_IMPORT_JSON_FORM("file.json.svg"), 
	
	DELETE_FORM("element.delete.svg"),	
	DELETE_BUILDING_BLOCK("element.delete.svg"), 
	
	PAGE_ERROR("page.error.svg"),
	PAGE_NOT_FOUND("page.not.found.svg"),
	
	ELEMENT_SHOW("element.show.svg"),
	ELEMENT_HIDE("element.hide.svg"),  
	
	FORM_MANAGER_FORM_LINKED("form.link.svg"),
	
	EDIT_WEBSERVICE_CALL("webservice.svg"), 
	
	ADD_WEBSERVICE_CALL("webservice.add.svg"), 
	REMOVE_WEBSERVICE_CALL("webservice.remove.svg"), 
	
	EDIT_WEBSERVICE_PORT("webservice.edit.svg"),
	REMOVE_WEBSERVICE_PORT("webservice.clear.svg"), 
	
	IMAGE("image.svg"),
	
	XML_SCORECARD("xml.scorecard.svg"),
	
	;

	private String value;

	ThemeIcons(String value) {
		this.value = value;
	}

	@Override
	public ThemeResource getThemeResource() {
		return new ThemeResource(value);
	}

	@Override
	public String getFile() {
		return value;
	}

}
