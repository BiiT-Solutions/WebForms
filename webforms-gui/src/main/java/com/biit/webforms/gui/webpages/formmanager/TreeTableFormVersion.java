package com.biit.webforms.gui.webpages.formmanager;

import com.biit.form.interfaces.IBaseFormView;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.gui.common.components.TreeTableBaseForm;
import com.biit.webforms.gui.common.components.TreeTableProvider;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.FormWorkStatusUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

public class TreeTableFormVersion extends TreeTableBaseForm<Form> {
	private static final long serialVersionUID = -7776688515497328826L;

	enum TreeTableFormVersionProperties {
		ACCESS, USED_BY, STATUS, LINKED_FORM, LINKED_ORGANIZATION, LINKED_VERSIONS;
	};

	public TreeTableFormVersion(TreeTableProvider<Form> formProvider) {
		super(formProvider);
		configureContainerProperties();
	}

	@Override
	protected void configureContainerProperties() {
		super.configureContainerProperties();

		addContainerProperty(TreeTableFormVersionProperties.ACCESS, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_ACCESS), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.USED_BY, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_USEDBY), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.STATUS, ComboBox.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_STATUS), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.LINKED_FORM, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_LINKED_FORM), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.LINKED_ORGANIZATION, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_LINKED_ORGANIZATION), null, Align.CENTER);

		addContainerProperty(TreeTableFormVersionProperties.LINKED_VERSIONS, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_LINKED_VERSIONS), null, Align.CENTER);

		setColumnCollapsible(TreeTableFormVersionProperties.ACCESS, true);
		setColumnCollapsible(TreeTableFormVersionProperties.USED_BY, true);
		setColumnCollapsible(TreeTableFormVersionProperties.STATUS, true);
		setColumnCollapsible(TreeTableFormVersionProperties.LINKED_FORM, true);
		setColumnCollapsible(TreeTableFormVersionProperties.LINKED_ORGANIZATION, true);
		setColumnCollapsible(TreeTableFormVersionProperties.LINKED_VERSIONS, true);

		setColumnExpandRatio(TreeTableFormVersionProperties.ACCESS, 1);
		setColumnExpandRatio(TreeTableFormVersionProperties.USED_BY, 1);
		setColumnExpandRatio(TreeTableFormVersionProperties.STATUS, 1.2f);
		setColumnExpandRatio(TreeTableFormVersionProperties.LINKED_FORM, 1.2f);
		setColumnExpandRatio(TreeTableFormVersionProperties.LINKED_VERSIONS, 1.0f);

		setColumnCollapsed(TreeTableFormVersionProperties.LINKED_ORGANIZATION, true);

		// Set new visibility order
		setVisibleColumns(new Object[] { TreeTableBaseFormProperties.FORM_LABEL, TreeTableBaseFormProperties.VERSION,
				TreeTableFormVersionProperties.USED_BY, TreeTableFormVersionProperties.STATUS,
				TreeTableBaseFormProperties.ORGANIZATION, TreeTableFormVersionProperties.LINKED_FORM,
				TreeTableFormVersionProperties.LINKED_ORGANIZATION, TreeTableFormVersionProperties.LINKED_VERSIONS,
				TreeTableBaseFormProperties.CREATED_BY, TreeTableBaseFormProperties.CREATION_DATE,
				TreeTableBaseFormProperties.MODIFIED_BY, TreeTableBaseFormProperties.MODIFICATION_DATE });
	}

	@SuppressWarnings("unchecked")
	@Override
	public Item updateRow(IBaseFormView form) {
		Item item = super.updateRow(form);
		item.getItemProperty(TreeTableFormVersionProperties.ACCESS).setValue(getFormPermissionsTag((Form) form));

		User userOfForm = UiAccesser.getUserUsingForm((Form) form);
		if (userOfForm != null) {
			item.getItemProperty(TreeTableFormVersionProperties.USED_BY).setValue(userOfForm.getEmailAddress());
		} else {
			item.getItemProperty(TreeTableFormVersionProperties.USED_BY).setValue("");
		}

		// Status
		item.getItemProperty(TreeTableFormVersionProperties.STATUS).setValue(generateStatusComboBox((Form) form));

		// Linked parameters
		item.getItemProperty(TreeTableFormVersionProperties.LINKED_FORM).setValue(((Form) form).getLinkedFormLabel());

		if (((Form) form).getLinkedFormOrganizationId() != null) {
			Organization linkedOrganization = WebformsAuthorizationService.getInstance().getOrganization(
					UserSessionHandler.getUser(), ((Form) form).getLinkedFormOrganizationId());
			if (linkedOrganization != null) {
				item.getItemProperty(TreeTableFormVersionProperties.LINKED_ORGANIZATION).setValue(linkedOrganization.getName());
			}
		}

		item.getItemProperty(TreeTableFormVersionProperties.LINKED_VERSIONS).setValue(
				((Form) form).getLinkedFormVersions()+"");

		return item;
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

	/**
	 * This function selects the last form used by the user or the first.
	 */
	public void selectLastUsedForm() {
		try {
			if (UserSessionHandler.getController().getLastEditedForm() != null) {
				// Update form with new object if the form has change.
				setValue(UserSessionHandler.getController().getLastEditedForm());
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
}
