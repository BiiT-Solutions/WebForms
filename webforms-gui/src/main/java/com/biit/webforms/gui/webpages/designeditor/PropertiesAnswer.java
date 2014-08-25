package com.biit.webforms.gui.webpages.designeditor;

import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.persistence.entity.Answer;

public class PropertiesAnswer extends StorableObjectProperties<Answer> {
	private static final long serialVersionUID = 8035711998129559199L;

	public PropertiesAnswer() {
		super(Answer.class);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

	}

}
