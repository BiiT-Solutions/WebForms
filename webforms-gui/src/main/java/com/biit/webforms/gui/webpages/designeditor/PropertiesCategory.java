package com.biit.webforms.gui.webpages.designeditor;

import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.persistence.entity.Category;

public class PropertiesCategory extends StorableObjectProperties<Category> {
	private static final long serialVersionUID = 766903215139261772L;

	public PropertiesCategory() {
		super(Category.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub
		
	}

}
