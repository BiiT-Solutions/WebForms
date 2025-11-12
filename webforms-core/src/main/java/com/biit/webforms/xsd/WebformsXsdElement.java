package com.biit.webforms.xsd;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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

import com.biit.form.entity.BaseGroup;
import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.xml.XmlUtils;

public class WebformsXsdElement extends XsdElement {

	public WebformsXsdElement(TreeObject element) {
		super(element.getName());
		if (!element.isHiddenElement()) {
			if (element instanceof Form) {
				putName(XmlUtils.normalizeNodeName(element.getLabel()));
				putType(XmlUtils.normalizeNodeName(element.getLabel()));
			} else {
				if (element instanceof BaseGroup) {
					putType(element.getPathName().replace('/', '.'));
					if (!(element instanceof BaseRepeatableGroup && (((BaseRepeatableGroup) element).isRepeatable()))) {
						putMaxOccurs(1);
					}
				} else {
					putMaxOccurs(1);
					if (element instanceof Question) {
						Question question = (Question) element;
						if (!question.containsDynamicAnswer()
								&& (question.getAnswerType() == AnswerType.SINGLE_SELECTION_LIST
										|| question.getAnswerType() == AnswerType.SINGLE_SELECTION_RADIO || question.getAnswerType() == AnswerType.SINGLE_SELECTION_SLIDER)) {
							addChild(new XsdSimpleType(new WebformsXsdQuestionAnswerRestriction(question)));
						}
					} else {
						putType(XsdElementType.STRING);
					}
				}
				putMinOccurs(0);
			}
		}
	}

}
