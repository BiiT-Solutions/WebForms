package com.biit.webforms.gui.webpages.blockmanager;

import com.biit.webforms.persistence.entity.SimpleBlockView;

import java.util.Comparator;


public class SimpleBlockViewUpdateDateComparator implements Comparator<SimpleBlockView> {

	@Override
	public int compare(SimpleBlockView arg0, SimpleBlockView arg1) {
		return arg0.getUpdateTime().compareTo(arg1.getUpdateTime());
	}

}
