package com.biit.webforms.theme;

import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.vaadin.server.ThemeResource;

public enum ThemeIcons implements IThemeIcon{
		
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
	
	FORM_MANAGER_ADD_FORM("form.create.svg"),
	FORM_MANAGER_NEW_VERSION("form.new.version.svg"),
	
	FORM_SAVE("form.save.svg"),
	FORM_VALIDATE("form.validate.svg"),
	FORM_FINISH("form.protect.svg"),
	
	BUILDING_BLOCK_ADD("building.block.add.svg"),
	BUILDING_BLOCK_SAVE("building.block.save.svg"),
	
	DESIGNER_QUESTION_TYPE_CHECKLIST("question.checkmark.svg"),
	DESIGNER_QUESTION_TYPE_RADIOBUTTON("question.radiobutton.svg"),
	DESIGNER_QUESTION_TYPE_DATE("question.calendar.31.svg"),
	DESIGNER_QUESTION_TYPE_NUMBER("question.textbox.numbers.svg"),
	DESIGNER_QUESTION_TYPE_POSTALCODE("question.postcode.svg"),
	DESIGNER_QUESTION_TYPE_TEXT("question.textbox.letters.svg"),
	DESIGNER_QUESTION_TYPE_DROPDOWN("question.dropdown.svg"),
	
	DESIGNER_NEW_CATEGORY("form.category.add.svg"),
	DESIGNER_NEW_SUBCATEGORY("form.subcategory.add.svg"),
	DESIGNER_NEW_GROUP("form.group.add.svg"),
	DESIGNER_NEW_QUESTION("form.question.add.svg"),
	DESIGNER_NEW_SYSTEM_FIELD("form.systemfield.add.svg"),
	DESIGNER_NEW_INFOTEXT("form.infotext.add.svg"),
	DESIGNER_NEW_ANSWER("form.answer.add.svg"),
	DESIGNER_NEW_SUBANSWER("alert.svg"), //TODO
	DESIGNER_MOVE("element.move.svg"),
	
	EXPORT_FORM_TO_PDF("file.pdf.svg"), 
	
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
	
	CONDITION_HELPER_FORM_REFERENCE("flow.helper.form.svg"),
	CONDITION_HELPER_CONTROLS("flow.helper.control.svg"), 
	
	FORM_MANAGER_IMPORT_ABCD_FORM("form.import.svg"),
	FORM_MANAGER_LINK_ABCD_FORM("form.link.svg"),
	
	COMPLETE_VALIDATION("alert.svg"),//TODO
	VALIDATE_STRUCTURE("alert.svg"), //TODO
	VALIDATE_FLOW("alert.svg"), //TODO
	VALIDATE_ABCD_LINK("alert.svg"),  //TODO
		
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
