package com.biit.webforms.gui.webpages.blockmanager;

import java.util.Comparator;

import com.biit.webforms.persistence.entity.Form;


public class TreeObjectUpdateDateComparator implements Comparator<Form> {

	@Override
	public int compare(Form arg0, Form arg1) {
		return arg0.getUpdateTime().compareTo(arg1.getUpdateTime());
	}

}
