package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.IconProvider;
import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.theme.ThemeIcons;

public class IconProviderTreeObjectWebforms extends IconProvider<TreeObject> {

	@Override
	public IThemeIcon getIcon(TreeObject object) {
		if (object instanceof Question) {
			Question question = (Question) object;
			switch (question.getAnswerType()) {
			case MULTIPLE_SELECTION:
				return ThemeIcons.DESIGNER_QUESTION_TYPE_CHECKLIST;
			case SINGLE_SELECTION_LIST:
				return ThemeIcons.DESIGNER_QUESTION_TYPE_DROPDOWN;
			case SINGLE_SELECTION_RADIO:
				return ThemeIcons.DESIGNER_QUESTION_TYPE_RADIOBUTTON;
			case INPUT:
				if (question.getAnswerFormat() != null) {
					switch (question.getAnswerFormat()) {
					case DATE:
						return ThemeIcons.DESIGNER_QUESTION_TYPE_DATE;
					case NUMBER:
						return ThemeIcons.DESIGNER_QUESTION_TYPE_NUMBER;
					case POSTAL_CODE:
						return ThemeIcons.DESIGNER_QUESTION_TYPE_POSTALCODE;
					case TEXT:
						return ThemeIcons.DESIGNER_QUESTION_TYPE_TEXT;
					}
				}
			case TEXT_AREA:
				return ThemeIcons.DESIGNER_QUESTION_TYPE_TEXT;
			}
		} else if (object instanceof Group) {
			Group group = (Group) object;
			if (group.isRepeatable()) {
				return CommonThemeIcon.TREE_OBJECT_GROUP_LOOP;
			}
		} else if (object instanceof Text) {
			return ThemeIcons.DESIGNER_INFO_TEXT;
		} else if (object instanceof SystemField) {
			return ThemeIcons.DESIGNER_SYSTEM_FIELD;
		} else if (object instanceof DynamicAnswer){
			return ThemeIcons.DESIGNER_DYNAMIC_ANSWER;
		}
		return null;
	}

}
