package com.biit.webforms.gui.webpages.designeditor;

import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.persistence.entity.Subcategory;

public class PropertiesSubcategory extends StorableObjectProperties<Subcategory> {
	private static final long serialVersionUID = -859153918941072265L;

	public PropertiesSubcategory() {
		super(Subcategory.class);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub

	}

}
