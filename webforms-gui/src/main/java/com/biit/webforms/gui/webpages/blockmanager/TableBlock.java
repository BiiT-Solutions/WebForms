package com.biit.webforms.gui.webpages.blockmanager;

import java.util.Collections;
import java.util.List;

import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.DateManager;
import com.biit.webforms.gui.common.utils.LiferayServiceAccess;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.entity.Block;
import com.liferay.portal.model.User;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Table;

public class TableBlock extends Table{

	private static final long serialVersionUID = 2995535101037124681L;

	private IBlockDao blockDao;
	
	enum TreeTableBlockProperties {
		BLOCK_NAME, USED_BY, CREATED_BY, CREATION_DATE, MODIFIED_BY, MODIFICATION_DATE;
	};
	
	public TableBlock() {
		// Add Vaadin conext to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		blockDao = (IBlockDao) helper.getBean("blockDao");

		initContainerProperties();
		initializeBlockTable();
	}
	
	private void initializeBlockTable() {
		List<Block> blocks = blockDao.getAll();
		Collections.sort(blocks, new TreeObjectUpdateDateComparator());
		
		for(Block block: blocks){
			addRow(block);
		}
	}

	@SuppressWarnings("unchecked")
	public void addRow(Block block) {
		if(block!=null){
			Item item = addItem(block);
			item.getItemProperty(TreeTableBlockProperties.BLOCK_NAME).setValue(block.getName());

			User userOfForm = UiAccesser.getUserIfFormIsInUse(block);
			if (userOfForm != null) {
				item.getItemProperty(TreeTableBlockProperties.USED_BY).setValue(userOfForm.getEmailAddress());
			} else {
				item.getItemProperty(TreeTableBlockProperties.USED_BY).setValue("");
			}

			try {
				item.getItemProperty(TreeTableBlockProperties.CREATED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(block.getCreatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException e) {
				item.getItemProperty(TreeTableBlockProperties.CREATED_BY).setValue("");
			}
			item.getItemProperty(TreeTableBlockProperties.CREATION_DATE).setValue(
					(DateManager.convertDateToString(block.getCreationTime())));
			try {
				item.getItemProperty(TreeTableBlockProperties.MODIFIED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(block.getUpdatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException e) {
				item.getItemProperty(TreeTableBlockProperties.MODIFIED_BY).setValue("");
			}
			item.getItemProperty(TreeTableBlockProperties.MODIFICATION_DATE).setValue(
					(DateManager.convertDateToString(block.getUpdateTime())));
		}
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setNullSelectionAllowed(true);
		setSizeFull();

		addContainerProperty(TreeTableBlockProperties.BLOCK_NAME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(TreeTableBlockProperties.USED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_USEDBY), null, Align.CENTER);

		addContainerProperty(TreeTableBlockProperties.CREATED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_CREATEDBY), null, Align.CENTER);

		addContainerProperty(TreeTableBlockProperties.CREATION_DATE, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_CREATIONDATE), null, Align.CENTER);

		addContainerProperty(TreeTableBlockProperties.MODIFIED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_MODIFIEDBY), null, Align.CENTER);

		addContainerProperty(TreeTableBlockProperties.MODIFICATION_DATE, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_MODIFICATIONDATE), null, Align.CENTER);


		setColumnExpandRatio(TreeTableBlockProperties.BLOCK_NAME, 3);
		setColumnExpandRatio(TreeTableBlockProperties.USED_BY, 1);
		setColumnExpandRatio(TreeTableBlockProperties.CREATED_BY, 1.2f);
		setColumnExpandRatio(TreeTableBlockProperties.CREATION_DATE, 1);
		setColumnExpandRatio(TreeTableBlockProperties.MODIFIED_BY, 1.2f);
		setColumnExpandRatio(TreeTableBlockProperties.MODIFICATION_DATE, 1);
	}
}
