package com.biit.webforms.gui.components.utils;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.interfaces.IBaseFormView;
import com.biit.webforms.persistence.entity.Form;

public class RootForm extends Form {

	private String name;
	private List<IBaseFormView> childForms;

	public RootForm(String label, Long organizationId) {
		this.name = label;
		childForms = new ArrayList<IBaseFormView>();
		setOrganizationId(organizationId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IBaseFormView getLastFormVersion() {
		int numVersion = 0;
		IBaseFormView lastVersion = null;
		for (IBaseFormView form : getChildForms()) {
			if (form.getVersion() > numVersion) {
				lastVersion = form;
			}
		}
		return lastVersion;
	}

	public List<IBaseFormView> getChildForms() {
		return childForms;
	}

	public void addChildForm(IBaseFormView form) {
		childForms.add(form);
	}
}
