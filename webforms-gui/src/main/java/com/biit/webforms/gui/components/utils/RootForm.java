package com.biit.webforms.gui.components.utils;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.persistence.entity.Form;

public class RootForm {

	private String name;
	private List<Form> childForms;

	public RootForm(String name) {
		this.name = name;
		childForms = new ArrayList<Form>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Form getLastFormVersion() {
		int numVersion = 0;
		Form lastVersion = null;
		for (Form form : getChildForms()) {
			if (form.getVersion() > numVersion) {
				lastVersion = form;
			}
		}
		return lastVersion;
	}

	public List<Form> getChildForms() {
		return childForms;
	}

	public void addChildForm(Form form) {
		childForms.add(form);
	}
}
