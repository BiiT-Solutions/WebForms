package com.biit.webforms.gui.components.utils;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.BaseForm;
import com.biit.webforms.persistence.entity.Form;

public class RootForm extends Form{

	private String name;
	private List<BaseForm> childForms;

	public RootForm(String label, Long organizationId) {
		this.name = label;
		childForms = new ArrayList<BaseForm>();
		setOrganizationId(organizationId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BaseForm getLastFormVersion() {
		int numVersion = 0;
		BaseForm lastVersion = null;
		for (BaseForm form : getChildForms()) {
			if (form.getVersion() > numVersion) {
				lastVersion = form;
			}
		}
		return lastVersion;
	}

	public List<BaseForm> getChildForms() {
		return childForms;
	}

	public void addChildForm(BaseForm form) {
		childForms.add(form);
	}
}
