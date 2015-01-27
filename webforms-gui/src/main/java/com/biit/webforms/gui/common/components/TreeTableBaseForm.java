package com.biit.webforms.gui.common.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.biit.form.IBaseFormView;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.LiferayServiceAccess;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.utils.RootForm;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.utils.DateManager;
import com.liferay.portal.model.Organization;
import com.vaadin.data.Item;
import com.vaadin.ui.TreeTable;

/**
 * Base tree table. This generic table needs a dataProvider to initialize the data.
 * 
 * @param <T>
 */
public class TreeTableBaseForm<T extends IBaseFormView> extends TreeTable {
	private static final long serialVersionUID = -3992941497136685462L;

	protected enum TreeTableBaseFormProperties {
		FORM_LABEL, VERSION, ORGANIZATION, CREATED_BY, CREATION_DATE, MODIFIED_BY, MODIFICATION_DATE;
	};

	private final TreeTableProvider<T> dataProvider;

	public TreeTableBaseForm(TreeTableProvider<T> dataProvider) {
		super();

		this.dataProvider = dataProvider;

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

	protected Item addRow(IBaseFormView form) {
		if (form != null) {
			Item item = addItem(form);
			updateRow(form);
			return item;
		}
		return null;
	}

	protected Item addRow(RootForm form) {
		if (form != null) {
			Item item = addItem(form);
			updateRow(form);
			return item;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Item updateRow(IBaseFormView form) {
		Item item = getItem(form);
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

	@SuppressWarnings("unchecked")
	public void updateRow(RootForm form) {
		Item item = getItem(form);
		item.getItemProperty(TreeTableBaseFormProperties.FORM_LABEL).setValue(form.getLabel());
	}

	/**
	 * Gets the Form Root of a form if exists.
	 * 
	 * @param form
	 * @return
	 */
	public RootForm getFormRoot(IBaseFormView form) {
		for (Object item : getItemIds()) {
			if (item instanceof RootForm) {
				if (((RootForm) item).getLabel().equals(form.getLabel())
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
		List<IBaseFormView> forms = new ArrayList<>();

		try {
			forms.addAll(dataProvider.getAll());

			for (IBaseFormView form : forms) {
				addForm(form);
			}

			defaultSort();
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}
	}

	/**
	 * Overridden version of sort for this table. Sorts by name ascendenly and version descendenly.
	 */
	public void defaultSort() {
		sort(new Object[] { TreeTableBaseFormProperties.FORM_LABEL, TreeTableBaseFormProperties.VERSION },
				new boolean[] { true, false });
	}

	public void addForm(IBaseFormView form) {
		addForm(form, false);
	}

	/**
	 * Add a new element to the table. If sort is true, the table is reordered after inserting the value.
	 * 
	 * @param form
	 * @param sort
	 */
	public void addForm(IBaseFormView form, boolean sort) {
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

		if (sort) {
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

	public IBaseFormView getForm() {
		if (getValue() == null) {
			return null;
		}
		if (getValue() instanceof RootForm) {
			RootForm rootForm = (RootForm) getValue();
			return rootForm.getLastFormVersion();
		} else {
			return (IBaseFormView) getValue();
		}
	}

	public void refreshTableData() {
		removeAllItems();
		init();
	}
}
