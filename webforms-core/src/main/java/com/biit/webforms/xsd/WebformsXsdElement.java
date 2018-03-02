package com.biit.webforms.xsd;

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
