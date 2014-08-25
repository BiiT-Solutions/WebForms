package com.biit.webforms.gui.webpages.designeditor;

import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.persistence.entity.Group;

public class PropertiesGroup extends StorableObjectProperties<Group> {
	private static final long serialVersionUID = 2409507883007287631L;

	public PropertiesGroup() {
		super(Group.class);
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub
		
	}

}
