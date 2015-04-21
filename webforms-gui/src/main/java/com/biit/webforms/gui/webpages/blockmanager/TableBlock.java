package com.biit.webforms.gui.webpages.blockmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.IBaseFormView;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.LiferayServiceAccess;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.dao.ISimpleBlockViewDao;
import com.biit.webforms.persistence.entity.IWebformsBlockView;
import com.biit.webforms.persistence.entity.SimpleBlockView;
import com.biit.webforms.utils.DateManager;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Table;

public class TableBlock extends Table {

	private static final long serialVersionUID = 2995535101037124681L;

	private ISimpleBlockViewDao simpleBlockDao;

	enum TreeTableBlockProperties {
		BLOCK_LABEL, ORGANIZATION, ACCESS, USED_BY, CREATED_BY, CREATION_DATE, MODIFIED_BY, MODIFICATION_DATE;
	};

	public TableBlock() {
		// Add Vaadin conext to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		simpleBlockDao = (ISimpleBlockViewDao) helper.getBean("simpleBlockViewDao");

		initContainerProperties();
		initializeBlockTable();
	}

	private void initializeBlockTable() {
		List<SimpleBlockView> blocks = new ArrayList<>();

		Set<Organization> userOrganizations = WebformsAuthorizationService.getInstance()
				.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), WebformsActivity.READ);
		try {
			for (Organization organization : userOrganizations) {
				blocks.addAll(simpleBlockDao.getAll(organization.getOrganizationId()));
			}

			// Collections.sort(blocks, new SimpleBlockViewUpdateDateComparator());

			for (SimpleBlockView block : blocks) {
				addRow(block);
			}

			defaultSort();
		} catch (Exception e) {
			e.printStackTrace();
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}
	}

	@SuppressWarnings("unchecked")
	public void addRow(SimpleBlockView block) {
		if (block != null) {
			Item item = addItem(block);
			item.getItemProperty(TreeTableBlockProperties.BLOCK_LABEL).setValue(block.getLabel());

			Organization organization = WebformsAuthorizationService.getInstance().getOrganization(
					UserSessionHandler.getUser(), block.getOrganizationId());
			if (organization != null) {
				item.getItemProperty(TreeTableBlockProperties.ORGANIZATION).setValue(organization.getName());
			}

			item.getItemProperty(TreeTableBlockProperties.ACCESS).setValue(getFormPermissionsTag(block));

			User userOfForm = UiAccesser.getUserUsingForm(block);
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

	/**
	 * This function returns an string with read only if the form can't be edited by the user
	 * 
	 * @param form
	 * @return
	 */
	private String getFormPermissionsTag(SimpleBlockView form) {
		String permissions = "";

		if (WebformsAuthorizationService.getInstance().isFormReadOnly(form, UserSessionHandler.getUser())) {
			permissions = LanguageCodes.CAPTION_READ_ONLY.translation();
		}
		return permissions;
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setNullSelectionAllowed(true);
		setSizeFull();

		addContainerProperty(TreeTableBlockProperties.BLOCK_LABEL, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(TreeTableBlockProperties.ORGANIZATION, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_ORGANIZATION), null, Align.LEFT);

		addContainerProperty(TreeTableBlockProperties.ACCESS, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_ACCESS), null, Align.CENTER);

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

		setColumnExpandRatio(TreeTableBlockProperties.BLOCK_LABEL, 3);
		setColumnExpandRatio(TreeTableBlockProperties.USED_BY, 1);
		setColumnExpandRatio(TreeTableBlockProperties.CREATED_BY, 1.2f);
		setColumnExpandRatio(TreeTableBlockProperties.CREATION_DATE, 1);
		setColumnExpandRatio(TreeTableBlockProperties.MODIFIED_BY, 1.2f);
		setColumnExpandRatio(TreeTableBlockProperties.MODIFICATION_DATE, 1);
	}

	public void refreshTableData() {
		removeAllItems();
		initializeBlockTable();
	}

	/**
	 * Selects the first row.
	 */
	public void selectFirstRow() {
		setValue(firstItemId());
	}

	/**
	 * This function selects the last form used by the user or the first.
	 */
	public void selectLastUsedBlock() {
		try {
			if (UserSessionHandler.getController().getLastEditedForm() != null) {
				// Update form with new object if the form has change.
				selectBlock(UserSessionHandler.getController().getLastEditedForm());
			} else {
				// Select default one.
				selectFirstRow();
			}
		} catch (Exception e) {
			// Select default one.
			selectFirstRow();
		}
	}

	public void selectBlock(IBaseFormView block) {
		if (block == null || block.getName() == null) {
			setValue(null);
		} else {
			SimpleBlockView simpleFormView = SimpleBlockView.getSimpleBlockView((IWebformsBlockView) block);

			setValue(simpleFormView);
		}
	}

	/**
	 * Overridden version of sort for this table. Sorts by name ascendency.
	 */
	public void defaultSort() {
		sort(new Object[] { TreeTableBlockProperties.BLOCK_LABEL }, new boolean[] { true });
	}

}
