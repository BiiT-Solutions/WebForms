package com.biit.webforms.gui.webpages.formmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.gui.common.components.IconOnlyButton;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.DateManager;
import com.biit.webforms.gui.common.utils.LiferayServiceAccess;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.components.EditInfoListener;
import com.biit.webforms.gui.components.utils.RootForm;
import com.biit.webforms.language.FormWorkStatusUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.enumerations.FormWorkStatus;
import com.biit.webforms.theme.ThemeIcons;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TreeTable;

public class TreeTableFormVersion extends TreeTable {
	private static final long serialVersionUID = -7776688515497328826L;

	private IFormDao formDao;
	private HashMap<String, List<Form>> formMap;
	private List<EditInfoListener> editInfoListeners;

	enum TreeTableFormVersionProperties {
		FORM_NAME, VERSION, INFO, ACCESS, ORGANIZATION, USED_BY, STATUS, CREATED_BY, CREATION_DATE, MODIFIED_BY, MODIFICATION_DATE;
	};

	public TreeTableFormVersion() {
		// Add Vaadin conext to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");

		editInfoListeners = new ArrayList<EditInfoListener>();

		initContainerProperties();
		initializeFormTable();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setNullSelectionAllowed(true);
		setSizeFull();

		addContainerProperty(TreeTableFormVersionProperties.FORM_NAME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(TreeTableFormVersionProperties.VERSION, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_VERSION), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.INFO, IconOnlyButton.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_INFO), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.ORGANIZATION, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_ORGANIZATION), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.ACCESS, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_ACCESS), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.USED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_USEDBY), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.STATUS, ComboBox.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_STATUS), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.CREATED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_CREATEDBY), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.CREATION_DATE, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_CREATIONDATE), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.MODIFIED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_MODIFIEDBY), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.MODIFICATION_DATE, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_MODIFICATIONDATE), null, Align.CENTER);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(TreeTableFormVersionProperties.FORM_NAME, false);
		setColumnCollapsible(TreeTableFormVersionProperties.VERSION, false);
		setColumnCollapsible(TreeTableFormVersionProperties.ORGANIZATION, true);
		setColumnCollapsible(TreeTableFormVersionProperties.ACCESS, true);
		setColumnCollapsible(TreeTableFormVersionProperties.USED_BY, true);
		setColumnCollapsible(TreeTableFormVersionProperties.STATUS, true);
		setColumnCollapsible(TreeTableFormVersionProperties.CREATED_BY, true);
		setColumnCollapsible(TreeTableFormVersionProperties.CREATION_DATE, true);
		setColumnCollapsible(TreeTableFormVersionProperties.MODIFIED_BY, true);
		setColumnCollapsible(TreeTableFormVersionProperties.MODIFICATION_DATE, true);
		setColumnCollapsed(TreeTableFormVersionProperties.CREATED_BY, true);
		setColumnCollapsed(TreeTableFormVersionProperties.CREATION_DATE, true);

		setColumnExpandRatio(TreeTableFormVersionProperties.FORM_NAME, 3);
		setColumnExpandRatio(TreeTableFormVersionProperties.VERSION, 0.5f);
		setColumnExpandRatio(TreeTableFormVersionProperties.ORGANIZATION, 1f);
		setColumnExpandRatio(TreeTableFormVersionProperties.ACCESS, 1);
		setColumnExpandRatio(TreeTableFormVersionProperties.USED_BY, 1);
		setColumnExpandRatio(TreeTableFormVersionProperties.STATUS, 1.2f);
		setColumnExpandRatio(TreeTableFormVersionProperties.CREATED_BY, 1.2f);
		setColumnExpandRatio(TreeTableFormVersionProperties.CREATION_DATE, 1);
		setColumnExpandRatio(TreeTableFormVersionProperties.MODIFIED_BY, 1.2f);
		setColumnExpandRatio(TreeTableFormVersionProperties.MODIFICATION_DATE, 1);
	}

	/**
	 * This function adds a row to the table only if the list of forms is not
	 * empty.
	 * 
	 * @param forms
	 */
	@SuppressWarnings("unchecked")
	private void addRow(Form form) {
		if (form != null) {
			Item item = addItem(form);
			item.getItemProperty(TreeTableFormVersionProperties.FORM_NAME).setValue(form.getName());
			item.getItemProperty(TreeTableFormVersionProperties.VERSION).setValue(form.getVersion() + "");
			item.getItemProperty(TreeTableFormVersionProperties.INFO).setValue(createInfoButton(form));

			Organization organization = WebformsAuthorizationService.getInstance().getOrganization(
					UserSessionHandler.getUser(), form.getOrganizationId());
			if (organization != null) {
				item.getItemProperty(TreeTableFormVersionProperties.ORGANIZATION).setValue(organization.getName());
			}

			item.getItemProperty(TreeTableFormVersionProperties.ACCESS).setValue(getFormPermissionsTag(form));

			User userOfForm = UiAccesser.getUserUsingForm(form);
			if (userOfForm != null) {
				item.getItemProperty(TreeTableFormVersionProperties.USED_BY).setValue(userOfForm.getEmailAddress());
			} else {
				item.getItemProperty(TreeTableFormVersionProperties.USED_BY).setValue("");
			}

			// Status
			item.getItemProperty(TreeTableFormVersionProperties.STATUS).setValue(generateStatusComboBox(form));

			try {
				item.getItemProperty(TreeTableFormVersionProperties.CREATED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(form.getCreatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException e) {
				item.getItemProperty(TreeTableFormVersionProperties.CREATED_BY).setValue("");
			}
			item.getItemProperty(TreeTableFormVersionProperties.CREATION_DATE).setValue(
					(DateManager.convertDateToString(form.getCreationTime())));
			try {
				item.getItemProperty(TreeTableFormVersionProperties.MODIFIED_BY).setValue(
						LiferayServiceAccess.getInstance().getUserById(form.getUpdatedBy()).getEmailAddress());
			} catch (com.vaadin.data.Property.ReadOnlyException | UserDoesNotExistException e) {
				item.getItemProperty(TreeTableFormVersionProperties.MODIFIED_BY).setValue("");
			}
			item.getItemProperty(TreeTableFormVersionProperties.MODIFICATION_DATE).setValue(
					(DateManager.convertDateToString(form.getUpdateTime())));
		}
	}

	private Component generateStatusComboBox(final Form form) {
		final ComboBox statusComboBox = new ComboBox();
		statusComboBox.setNullSelectionAllowed(false);
		for (FormWorkStatusUi formStatus : FormWorkStatusUi.values()) {
			statusComboBox.addItem(formStatus.getFormWorkStatus());
			statusComboBox.setItemCaption(formStatus.getFormWorkStatus(), formStatus.getLanguageCode().translation());
		}
		statusComboBox.setValue(form.getStatus());
		statusComboBox.setWidth("100%");

		// Status can change if you are not in DESIGN phase and can advance form
		// status
		boolean formIsNotDesign = form.getStatus() != FormWorkStatus.DESIGN;
		boolean userCanUpgradeStatus = WebformsAuthorizationService.getInstance().isAuthorizedActivity(
				UserSessionHandler.getUser(), form, WebformsActivity.FORM_STATUS_UPGRADE);
		final boolean userCanDowngradeStatus = WebformsAuthorizationService.getInstance().isAuthorizedActivity(
				UserSessionHandler.getUser(), form, WebformsActivity.FORM_STATUS_DOWNGRADE);
		// Or if you have admin rights.
		final boolean userIsAdmin = WebformsAuthorizationService.getInstance().isAuthorizedActivity(
				UserSessionHandler.getUser(), form, WebformsActivity.ADMIN_RIGHTS);

		statusComboBox.setEnabled((formIsNotDesign && userCanUpgradeStatus) || userIsAdmin);
		statusComboBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6270860285995563296L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				if (form.getStatus().equals(statusComboBox.getValue())) {
					// Its the same status. Don't do anything.
					return;
				}
				if (!form.getStatus().isMovingForward((FormWorkStatus) statusComboBox.getValue())) {
					if (!(userCanDowngradeStatus || userIsAdmin)) {
						// If you can't downgrade nor user is admin then, reset
						// comboBox to previous value, throw a warning and exit.
						statusComboBox.setValue(form.getStatus());
						MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_NOT_ALLOWED,
								LanguageCodes.WARNING_DESCRIPTION_NOT_ENOUGH_RIGHTS);
						return;
					}
				}
				form.setStatus((FormWorkStatus) statusComboBox.getValue());
				UserSessionHandler.getController().saveForm(form);
			}
		});

		return statusComboBox;
	}

	private IconOnlyButton createInfoButton(final Form form) {
		IconOnlyButton icon = new IconOnlyButton(ThemeIcons.ELEMENT_EDIT.getThemeResource());
		icon.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 896514404248078435L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireEditInfo(form);
			}
		});
		return icon;
	}

	protected void fireEditInfo(Form value) {
		for (EditInfoListener listener : editInfoListeners) {
			listener.editInfo(value);
		}
	}

	@SuppressWarnings("unchecked")
	private void addRow(RootForm form) {
		if (form != null) {
			Item item = addItem(form);
			item.getItemProperty(TreeTableFormVersionProperties.FORM_NAME).setValue(form.getName());
		}
	}

	public void addForm(Form form) {
		RootForm parent = getFormRoot(form);
		if (parent == null) {
			parent = new RootForm(form.getName(), form.getOrganizationId());
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
		sort();
	}

	/**
	 * Gets the Form Root of a form if exists.
	 * 
	 * @param form
	 * @return
	 */
	private RootForm getFormRoot(Form form) {
		for (Object item : getItemIds()) {
			if (item instanceof RootForm) {
				if (((RootForm) item).getName().equals(form.getName())
						&& ((RootForm) item).getOrganizationId().equals(form.getOrganizationId())) {
					return (RootForm) item;
				}
			}
		}
		return null;
	}

	private void initializeFormTable() {
		formMap = initializeFormData();
		for (List<Form> forms : formMap.values()) {
			for (Form form : forms) {
				addForm(form);
			}
		}

		setSortContainerPropertyId(TreeTableFormVersionProperties.FORM_NAME);
		setSortAscending(true);
		sort();
	}

	/**
	 * This function loads from database all form elements and groups them by
	 * name. At the end it orders each form list by version number.
	 * 
	 * @return
	 * @throws NotConnectedToDatabaseException
	 */
	private HashMap<String, List<Form>> initializeFormData() {
		HashMap<String, List<Form>> formData = new HashMap<>();
		List<Form> forms = new ArrayList<>();

		List<Organization> userOrganizations = WebformsAuthorizationService.getInstance()
				.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), WebformsActivity.READ);
		for (Organization organization : userOrganizations) {
			forms.addAll(formDao.getAll(Form.class, organization));
		}
		for (Form form : forms) {
			if (!formData.containsKey(form.getName())) {
				// First form with this name
				List<Form> listFormsForName = new ArrayList<Form>();
				listFormsForName.add(form);
				formData.put(form.getName(), listFormsForName);
			} else {
				formData.get(form.getName()).add(form);
			}
		}

		for (List<Form> formList : formData.values()) {
			Collections.sort(formList, new FormVersionComparator());
		}

		return formData;
	}

	/**
	 * This is a form comparator that sorts by version number. It is used to
	 * sort the lists of forms that we have created for each different form
	 * name.
	 * 
	 */
	private class FormVersionComparator implements Comparator<Form> {
		@Override
		public int compare(Form arg0, Form arg1) {
			return arg0.getVersion().compareTo(arg1.getVersion());
		}
	}

	/**
	 * This function selects the last form used by the user or the first.
	 */
	public void selectLastUsedForm() {
		try {
			if (UserSessionHandler.getController().getLastEditedForm() != null) {
				// Update form with new object if the form has change.
				selectForm(UserSessionHandler.getController().getLastEditedForm());
			} else {
				// Select default one.
				selectFirstRow();
			}
		} catch (Exception e) {
			// Select default one.
			selectFirstRow();
		}
	}

	/**
	 * This function selects a form from the table.
	 */
	public void selectForm(Form form) {
		if (form != null && !containsId(form)) {
			// uncollapseForm(form);
			setCollapsed(form, false);
		}
		for (Object itemId : getItemIds()) {
			if (itemId instanceof Form) {
				Form tableForm = (Form) itemId;
				if (tableForm.getId() != null && tableForm.getId().equals(form.getId())) {
					setValue(tableForm);
				}
			}
		}
	}

	/**
	 * Selects the first row.
	 */
	private void selectFirstRow() {
		setValue(firstItemId());
	}

	/**
	 * This function returns an string with read only if the form can't be
	 * edited by the user
	 * 
	 * @param form
	 * @return
	 */
	private String getFormPermissionsTag(Form form) {
		String permissions = "";

		if (WebformsAuthorizationService.getInstance().isFormReadOnly(form, UserSessionHandler.getUser())) {
			permissions = LanguageCodes.CAPTION_READ_ONLY.translation();
		}
		return permissions;
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return new ArrayList<>(Arrays.asList(TreeTableFormVersionProperties.FORM_NAME));
	}

	public void addEditInfoListener(EditInfoListener listener) {
		editInfoListeners.add(listener);
	}

	public void removeEditInfoListener(EditInfoListener listener) {
		editInfoListeners.remove(listener);
	}

	public RootForm getSelectedRootForm() {
		if (getValue() instanceof RootForm) {
			return (RootForm) getValue();
		} else {
			return (RootForm) getParent(getValue());
		}
	}
}
