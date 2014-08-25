package com.biit.webforms.gui.webpages.designeditor;

import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.persistence.entity.Text;

public class PropertiesText extends StorableObjectProperties<Text> {
	private static final long serialVersionUID = 3545367878977339439L;

	public PropertiesText() {
		super(Text.class);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

	}

}
