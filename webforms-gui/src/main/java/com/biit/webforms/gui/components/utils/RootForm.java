package com.biit.webforms.gui.components.utils;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.IBaseFormView;
import com.biit.webforms.persistence.entity.Form;

public class RootForm extends Form {

	private String label;
	private List<IBaseFormView> childForms;

	public RootForm(String label, Long organizationId) {
		this.label = label;
		childForms = new ArrayList<IBaseFormView>();
		setOrganizationId(organizationId);
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	public IBaseFormView getLastFormVersion() {
		Integer numVersion = null;
		IBaseFormView lastVersion = null;
		for (IBaseFormView form : getChildForms()) {
			if (lastVersion == null || form.getVersion() > numVersion) {
				lastVersion = form;
				numVersion = form.getVersion();
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
