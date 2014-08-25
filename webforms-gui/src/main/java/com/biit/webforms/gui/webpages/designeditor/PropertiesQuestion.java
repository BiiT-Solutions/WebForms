package com.biit.webforms.gui.webpages.designeditor;

import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.persistence.entity.Question;

public class PropertiesQuestion extends StorableObjectProperties<Question>{
	private static final long serialVersionUID = 7572463216386081265L;

	public PropertiesQuestion() {
		super(Question.class);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub
		
	}

}
