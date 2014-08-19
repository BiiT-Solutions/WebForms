package com.biit.webforms.authentication;

import com.biit.webforms.persistence.entity.Form;

public class ApplicationController {

	private Form form;
	
	public void setForm(Form form){
		this.form = form;
	}
	
	public Form getForm() {
		return form;
	}

	
	
}
