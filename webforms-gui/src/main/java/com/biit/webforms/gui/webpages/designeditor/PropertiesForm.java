package com.biit.webforms.gui.webpages.designeditor;

import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.persistence.entity.Form;

public class PropertiesForm extends StorableObjectProperties<Form>{
	private static final long serialVersionUID = -7053263006728113569L;

	public PropertiesForm() {
		super(Form.class);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub
	}
}
