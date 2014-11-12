package com.biit.webforms.xsd;

import java.util.List;

import com.biit.form.BaseGroup;
import com.biit.form.TreeObject;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.xml.XmlUtils;

public class WebformsXsdForm extends XsdSchema {

	public WebformsXsdForm(Form form) {
		super(getXMLNS(form), getNamespace(form));

		// Get all base groups and generate the complex types xsd.
		List<TreeObject> groups = form.getAll(BaseGroup.class);
		for (TreeObject group : groups) {
			addChild(new WebformsXsdComplexType((BaseGroup)group));
		}
		
		addChild(new WebformsXsdComplexType(form));
		addChild(new WebformsXsdElement(form));
	}

	private static String getNamespace(Form form) {
		return WebformsConfigurationReader.getInstance().getXsdNamespace()+"/"+XmlUtils.normalizeNodeName(form.getLabel());
	}

	private static String getXMLNS(Form form) {
		return WebformsConfigurationReader.getInstance().getXsdXmlns()+"/"+XmlUtils.normalizeNodeName(form.getLabel());
	}
}
