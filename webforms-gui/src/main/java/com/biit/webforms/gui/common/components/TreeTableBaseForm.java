package com.biit.webforms.gui.common.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.biit.form.BaseForm;
import com.biit.form.persistence.dao.IBaseFormDao;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.DateManager;
import com.biit.webforms.gui.common.utils.LiferayServiceAccess;
import com.biit.webforms.gui.components.utils.RootForm;
import com.biit.webforms.language.LanguageCodes;
import com.liferay.portal.model.Organization;
import com.vaadin.data.Item;
import com.vaadin.ui.TreeTable;

public class TreeTableBaseForm<T extends BaseForm> extends TreeTable {
	private static final long serialVersionUID = -3992941497136685462L;

	protected enum TreeTableBaseFormProperties {
		FORM_LABEL, VERSION, ORGANIZATION, CREATED_BY, CREATION_DATE, MODIFIED_BY, MODIFICATION_DATE;
	};

	private final IBaseFormDao<T> baseFormDao;

	public TreeTableBaseForm(IBaseFormDao<T> baseFormDao) {
		super();

		this.baseFormDao = baseFormDao;

		configure();
		configureContainerProperties();
		init();
	}

	private void configure() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setNullSelectionAllowed(true);
		setSizeFull();
	}

	/**
	 * Function to configure container properties
	 */
	protected void configureContainerProperties() {
		addContainerProperty(TreeTableBaseFormProperties.FORM_LABEL, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(TreeTableBaseFormProperties.VERSION, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_VERSION), null, Align.CENTER);

		addContainerProperty(TreeTableBaseFormProperties.ORGANIZATION, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_ORGANIZATION), null, Align.CENTER);

		addContainerProperty(TreeTableBaseFormProperties.CREATED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_CREATEDBY), null, Align.CENTER);

		addContainerProperty(TreeTableBaseFormProperties.CREATION_DATE, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_CREATIONDATE), null, Align.CENTER);

		addContainerProperty(TreeTableBaseFormProperties.MODIFIED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_MODIFIEDBY), null, Align.CENTER);

		addContainerProperty(TreeTableBaseFormProperties.MODIFICATION_DATE, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_MODIFICATIONDATE), null, Align.CENTER);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(TreeTableBaseFormProperties.FORM_LABEL, false);
		setColumnCollapsible(TreeTableBaseFormProperties.VERSION, false);
		setColumnCollapsible(TreeTableBaseFormProperties.ORGANIZATION, true);
		setColumnCollapsible(TreeTableBaseFormProperties.CREATED_BY, true);
		setColumnCollapsible(TreeTableBaseFormProperties.CREATION_DATE, true);
		setColumnCollapsible(TreeTableBaseFormProperties.MODIFIED_BY, true);
		setColumnCollapsible(TreeTableBaseFormProperties.MODIFICATION_DATE, true);
		setColumnCollapsed(TreeTableBaseFormProperties.CREATED_BY, true);
		setColumnCollapsed(TreeTableBaseFormProperties.CREATION_DATE, true);

		setColumnExpandRatio(TreeTableBaseFormProperties.FORM_LABEL, 3);
		setColumnExpandRatio(TreeTableBaseFormProperties.VERSION, 0.5f);
		setColumnExpandRatio(TreeTableBaseFormProperties.ORGANIZATION, 1f);
		setColumnExpandRatio(TreeTableBaseFormProperties.CREATED_BY, 1.2f);
		setColumnExpandRatio(TreeTableBaseFormProperties.CREATION_DATE, 1);
		setColumnExpandRatio(TreeTableBaseFormProperties.MODIFIED_BY, 1.2f);
		setColumnExpandRatio(TreeTableBaseFormProperties.MODIFICATION_DATE, 1);
	}

	@SuppressWarnings("unchecked")
	protected Item addRow(BaseForm form) {
		if (form != null) {
			Item item = addItem(form);
			item.getItemProperty(TreeTableBaseFormProperties.FORM_LABEL).setValue(form.getLabel());
			item.getItemProperty(TreeTableBaseFormProperties.VERSION).setValue(form.getVersion() + "");

			Organization organization = WebformsAuthorizationService.getInstance().getOrganization(
					UserSessionHandler.getUser(), form.getOrganizationId());
			if (organization != null) {
				item.getItemProperty(TreeTableBaseFormProperties.ORGANIZATION).setValue(organization.getName());
			}

			try {
				item.getItemProperty(TreeTableBaseFormProperties.CREATED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(form.getCreatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException e) {
				item.getItemProperty(TreeTableBaseFormProperties.CREATED_BY).setValue("");
			}
			item.getItemProperty(TreeTableBaseFormProperties.CREATION_DATE).setValue(
					(DateManager.convertDateToString(form.getCreationTime())));
			try {
				item.getItemProperty(TreeTableBaseFormProperties.MODIFIED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(form.getUpdatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException e) {
				item.getItemProperty(TreeTableBaseFormProperties.MODIFIED_BY).setValue("");
			}
			item.getItemProperty(TreeTableBaseFormProperties.MODIFICATION_DATE).setValue(
					(DateManager.convertDateToString(form.getUpdateTime())));
			return item;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected void addRow(RootForm form) {
		if (form != null) {
			Item item = addItem(form);
			item.getItemProperty(TreeTableBaseFormProperties.FORM_LABEL).setValue(form.getName());
		}
	}

	/**
	 * Gets the Form Root of a form if exists.
	 * 
	 * @param form
	 * @return
	 */
	public RootForm getFormRoot(BaseForm form) {
		for (Object item : getItemIds()) {
			if (item instanceof RootForm) {
				if (((RootForm) item).getName().equals(form.getLabel())
						&& ((RootForm) item).getOrganizationId().equals(form.getOrganizationId())) {
					return (RootForm) item;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return new ArrayList<>(Arrays.asList(TreeTableBaseFormProperties.FORM_LABEL));
	}

	/**
	 * Selects the first row.
	 */
	public void selectFirstRow() {
		setValue(firstItemId());
	}

	private void init() {
		List<BaseForm> forms = new ArrayList<>();

		List<Organization> userOrganizations = WebformsAuthorizationService.getInstance()
				.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), WebformsActivity.READ);
		for (Organization organization : userOrganizations) {
			forms.addAll(baseFormDao.getAll(organization.getOrganizationId()));
		}

		for (BaseForm form : forms) {
			addForm(form);
		}

		setSortContainerPropertyId(TreeTableBaseFormProperties.FORM_LABEL);
		setSortAscending(true);
		sort();
	}

	public void addForm(BaseForm form) {
		addForm(form, false);
	}

	public void addForm(BaseForm form, boolean sorted) {
		RootForm parent = getFormRoot(form);
		if (parent == null) {
			parent = new RootForm(form.getLabel(), form.getOrganizationId());
			addRow(parent);
		}
		if (form != null) {
			parent.addChildForm(form);

			addRow(form);
			setChildrenAllowed(parent, true);
			setParent(form, parent);
			setCollapsed(parent, false);
			setValue(form);
			setChildrenAllowed(form, false);
		}

		if (sorted) {
			sort();
		}
	}
	
	public RootForm getSelectedRootForm() {
		if (getValue() instanceof RootForm) {
			return (RootForm) getValue();
		} else {
			return (RootForm) getParent(getValue());
		}
	}
	
	public void refreshTableData() {
		removeAllItems();
		init();
	}
}
