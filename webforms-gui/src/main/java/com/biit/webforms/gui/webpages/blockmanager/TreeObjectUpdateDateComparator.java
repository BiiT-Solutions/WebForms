package com.biit.webforms.gui.webpages.blockmanager;

import java.util.Comparator;

import com.biit.webforms.persistence.entity.Block;


public class TreeObjectUpdateDateComparator implements Comparator<Block> {

	@Override
	public int compare(Block arg0, Block arg1) {
		return arg0.getUpdateTime().compareTo(arg1.getUpdateTime());
	}

}
