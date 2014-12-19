package com.biit.webforms.xsd;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.BaseGroup;
import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.xml.XmlNodeProducer;
import com.biit.webforms.xml.XmlUtils;

public class WebformsXsdComplexType extends XsdComplexType {

	public WebformsXsdComplexType(Form form) {
		super(XmlUtils.normalizeNodeName(form.getLabel()));
		addChild(new XsdAll(generateXsdElements(form.getChildren())));
	}

	public WebformsXsdComplexType(BaseGroup group) {
		super(group.getPathName().replace('/', '.'));
		addChild(new XsdAll(generateXsdElements(group.getChildren())));
	}

	private XmlNodeProducer[] generateXsdElements(List<TreeObject> children) {
		List<XmlNodeProducer> childNodes = new ArrayList<XmlNodeProducer>();
		for (TreeObject child : children) {
			if (child instanceof Text || child instanceof SystemField) {
				continue;
			}
			childNodes.add(new WebformsXsdElement(child));
		}
		return childNodes.toArray(new XmlNodeProducer[] {});
	}

}
