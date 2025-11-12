package com.biit.webforms.gui.webpages.designer;

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

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.IconProvider;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.persistence.entity.*;
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
			case SINGLE_SELECTION_SLIDER:
				return ThemeIcons.DESIGNER_QUESTION_TYPE_SLIDER;
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
			if (group.isShownAsTable()) {
				return ThemeIcons.DESIGNER_GROUP_TABLE;
				// Is a row of the table.
			} else if (group.getParent() != null && group.getParent() instanceof Group && ((Group) group.getParent()).isShownAsTable()) {
				return ThemeIcons.DESIGNER_GROUP_TABLE_ROW;
			} else {
				return ThemeIcons.DESIGNER_GROUP;
			}
		} else if (object instanceof Text) {
			return ThemeIcons.DESIGNER_INFO_TEXT;
		} else if (object instanceof SystemField) {
			return ThemeIcons.DESIGNER_SYSTEM_FIELD;
		} else if (object instanceof DynamicAnswer) {
			return ThemeIcons.DESIGNER_DYNAMIC_ANSWER;
		} else if (object instanceof Category) {
			return ThemeIcons.DESIGNER_CATEGORY;
		} else if (object instanceof AttachedFiles) {
			return ThemeIcons.DESIGNER_ATTACHED_FILES;
		}
		return null;
	}

}
