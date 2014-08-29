package com.biit.webforms.gui.webpages.designer;

import java.util.Collections;
import java.util.List;

import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.webpages.blockmanager.TreeObjectUpdateDateComparator;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.entity.Block;
import com.vaadin.server.VaadinServlet;

public class BlockTreeTable extends TreeObjectTableDesigner {

	private static final long serialVersionUID = 2428935033753125285L;

	private IBlockDao blockDao;

	public BlockTreeTable() {
		super();
		// Add Vaadin conext to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		blockDao = (IBlockDao) helper.getBean("blockDao");
		
		initializeBlockTable();
	}

	private void initializeBlockTable() {
		List<Block> blocks = blockDao.getAll();
		Collections.sort(blocks, new TreeObjectUpdateDateComparator());
		
		for(Block block: blocks){
			loadTreeObject(block,null);
			setCollapsed(block, true);
		}
	}
}
