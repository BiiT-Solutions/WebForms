package com.biit.webforms.gui.common.components;

import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseCategory;
import com.biit.form.entity.BaseForm;
import com.biit.form.entity.BaseGroup;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.TableTreeObject.TreeObjectTableProperties;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

/**
 * Cell Style generator for FormTreeTable. Adds style for column name in each row depending on the itemId.
 * 
 */
public class TreeObjectTableCellStyleGenerator implements CellStyleGenerator {
	private static final long serialVersionUID = 2280270027613134315L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		String styles = "";
		if (itemId instanceof TreeObject) {
			if (((TreeObject) itemId).isReadOnly()) {
				styles += "tree-cell-disabled ";
			}
		}
		if (propertyId == TreeObjectTableProperties.ELEMENT_NAME) {
			if (itemId instanceof BaseForm) {
				styles += "tree-cell-form";
			} else if (itemId instanceof BaseCategory) {
				styles += "tree-cell-category";
			} else if (itemId instanceof BaseGroup) {
				styles += "tree-cell-group";
			} else if (itemId instanceof BaseQuestion) {
				styles += "tree-cell-question";
			} else if (itemId instanceof BaseAnswer) {
				styles += "tree-cell-answer";
			}
		}
		return styles;
	}

}
