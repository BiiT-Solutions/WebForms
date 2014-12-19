package com.biit.webforms.xsd;

import com.biit.form.BaseGroup;
import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.xml.XmlUtils;

public class WebformsXsdElement extends XsdElement {

	public WebformsXsdElement(TreeObject element) {
		super(element.getName());
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
				putType(XsdElementType.STRING);
				putMaxOccurs(1);
			}
			putMinOccurs(0);
		}
	}

}
