package com.biit.webforms.gui.components;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.ComponentCellTreeObject;
import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.theme.ThemeIcons;

public class ComponentCellTreeObjectWebforms extends ComponentCellTreeObject{
	private static final long serialVersionUID = -2764788075561682119L;

	@Override
	protected IThemeIcon getIcon(TreeObject element) {
		if (element instanceof Question) {
			Question question = (Question) element;
			switch (question.getAnswerType()) {
			case MULTI_CHECKBOX:
				return ThemeIcons.DESIGNER_QUESTION_CHECKLIST;
			case RADIO:
				return ThemeIcons.DESIGNER_QUESTION_RADIOBUTTON;
			case INPUT:
				if (question.getAnswerFormat() != null) {
					switch (question.getAnswerFormat()) {
					case DATE:
						return ThemeIcons.DESIGNER_QUESTION_DATE;
					case NUMBER:
						return ThemeIcons.DESIGNER_QUESTION_NUMBER;
					case POSTAL_CODE:
						return ThemeIcons.DESIGNER_QUESTION_POSTALCODE;
					case TEXT:
						return ThemeIcons.DESIGNER_QUESTION_TEXT;
					}
				}
			}
		} else if (element instanceof Group) {
			Group group = (Group) element;
			if (group.isRepetable()) {
				return CommonThemeIcon.TREE_OBJECT_GROUP_LOOP;
			}
		}
		return null;
	}	
}
