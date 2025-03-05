package com.biit.webforms.gui.common.components;

import com.biit.form.entity.IBaseFormView;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.utils.date.DateManager;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.components.utils.RootForm;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.security.IWebformsSecurityService;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.TreeTable;

import javax.ws.rs.ProcessingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base tree table. This generic table needs a dataProvider to initialize the
 * data.
 * 
 * @param <T>
 */
public class TreeTableBaseForm<T extends IBaseFormView> extends TreeTable {
	private static final long serialVersionUID = -3992941497136685462L;

	private IconProvider<IBaseFormView> iconProvider;

	private IWebformsSecurityService webformsSecurityService;

	protected enum TreeTableBaseFormProperties {
		FORM_LABEL, VERSION, ORGANIZATION, CREATED_BY, CREATION_DATE, MODIFIED_BY, MODIFICATION_DATE;
	};

	private final TreeTableProvider<T> dataProvider;

	public TreeTableBaseForm(TreeTableProvider<T> dataProvider, IconProvider<IBaseFormView> iconProvider) {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");

		this.dataProvider = dataProvider;

		if (iconProvider == null) {
			this.iconProvider = new IconProviderFormDefault();
		} else {
			this.iconProvider = iconProvider;
		}

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
		addContainerProperty(TreeTableBaseFormProperties.FORM_LABEL, Component.class, null,
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

	public Item addRow(IBaseFormView form) {
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
		// item.getItemProperty(TreeTableBaseFormProperties.FORM_LABEL).setValue(form.getLabel());

		Object treeObjectIcon = createElementWithIcon(form);
		item.getItemProperty(TreeTableBaseFormProperties.FORM_LABEL).setValue(treeObjectIcon);

		item.getItemProperty(TreeTableBaseFormProperties.VERSION).setValue(form.getVersion() + "");

		IGroup<Long> organization = webformsSecurityService.getOrganization(UserSession.getUser(), form.getOrganizationId());
		if (organization != null) {
			item.getItemProperty(TreeTableBaseFormProperties.ORGANIZATION).setValue(organization.getUniqueName());
		}

		try {
			item.getItemProperty(TreeTableBaseFormProperties.CREATED_BY).setValue(
					webformsSecurityService.getUserById(form.getCreatedBy()).getEmailAddress());
		} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException | NullPointerException e) {
			item.getItemProperty(TreeTableBaseFormProperties.CREATED_BY).setValue("");
		}
		item.getItemProperty(TreeTableBaseFormProperties.CREATION_DATE).setValue((DateManager.convertDateToString(form.getCreationTime())));
		try {
			item.getItemProperty(TreeTableBaseFormProperties.MODIFIED_BY).setValue(
					webformsSecurityService.getUserById(form.getUpdatedBy()).getEmailAddress());
		} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException | NullPointerException e) {
			item.getItemProperty(TreeTableBaseFormProperties.MODIFIED_BY).setValue("");
		}
		item.getItemProperty(TreeTableBaseFormProperties.MODIFICATION_DATE).setValue(
				(DateManager.convertDateToString(form.getUpdateTime())));
		return item;
	}

	@SuppressWarnings("unchecked")
	public Item updateRow(RootForm form) {
		if (form != null) {
			Item item = getItem(form);
			Object treeObjectIcon = createElementWithIcon(form);
			item.getItemProperty(TreeTableBaseFormProperties.FORM_LABEL).setValue(treeObjectIcon);
			return item;
		}
		return null;
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
		return new ArrayList<>(Collections.singletonList(TreeTableBaseFormProperties.FORM_LABEL));
	}

	/**
	 * Selects the first row.
	 */
	public void selectFirstRow() {
		setValue(firstItemId());
	}

	private void init() {

        try {
            List<IBaseFormView> forms = new ArrayList<>(dataProvider.getAll());

			for (IBaseFormView form : forms) {
				addForm(form);
			}

			defaultSort();
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		} catch (ProcessingException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_WEBSERVICES, LanguageCodes.ERROR_ACCESSING_WEBSERVICES_DESCRIPTION);
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	/**
	 * Overridden version of sort for this table. Sorts by name ascendenly and
	 * version descendenly.
	 */
	public void defaultSort() {
		sort(new Object[] { TreeTableBaseFormProperties.FORM_LABEL, TreeTableBaseFormProperties.VERSION }, new boolean[] { true, false });
	}

	public void addForm(IBaseFormView form) {
		addForm(form, false);
	}

	/**
	 * Add a new element to the table. If sort is true, the table is reordered
	 * after inserting the value.
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

	public void setIconProvider(IconProvider<IBaseFormView> iconProvider) {
		this.iconProvider = iconProvider;
	}

	public IconProvider<IBaseFormView> getIconProvider() {
		return iconProvider;
	}

	protected Object createElementWithIcon(final IBaseFormView element) {
		ComponentCellForm cell = new ComponentCellForm(getIconProvider());
		cell.update(element);
		cell.registerTouchCallBack(this, element);

		return cell;
	}

	public IWebformsSecurityService getWebformsSecurityService() {
		return webformsSecurityService;
	}
}
