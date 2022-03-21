package com.biit.webforms.gui.webpages.blockmanager;

import com.biit.webforms.persistence.entity.Form;

import java.util.Comparator;

/**
 * Comparator for form derived objects that orders the elements by update date.
 *
 */
public class TreeObjectUpdateDateComparator implements Comparator<Form> {

	@Override
	public int compare(Form arg0, Form arg1) {
		return arg0.getUpdateTime().compareTo(arg1.getUpdateTime());
	}

}
