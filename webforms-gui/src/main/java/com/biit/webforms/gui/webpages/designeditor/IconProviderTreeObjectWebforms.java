package com.biit.webforms.gui.webpages.designeditor;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.IconProvider;
import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.theme.ThemeIcons;


public class IconProviderTreeObjectWebforms extends IconProvider<TreeObject>{

	@Override
	public IThemeIcon getIcon(TreeObject object) {
		if (object instanceof Question) {
			Question question = (Question) object;
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
		} else if (object instanceof Group) {
			Group group = (Group) object;
			if (group.isRepetable()) {
				return CommonThemeIcon.TREE_OBJECT_GROUP_LOOP;
			}
		}
		return null;
	}
	
}	
