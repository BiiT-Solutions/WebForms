package com.biit.webforms.gui.webpages.formmanager;

import com.biit.form.entity.IBaseFormView;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.TreeTableBaseForm;
import com.biit.webforms.gui.common.components.TreeTableProvider;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.CancelActionListener;
import com.biit.webforms.gui.common.components.WindowProceedAction;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.exceptions.NotEnoughRightsToChangeStatusException;
import com.biit.webforms.language.FormWorkStatusUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.security.WebformsActivity;
import com.biit.webforms.security.WebformsBasicAuthorizationService;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

public class TreeTableFormVersion extends TreeTableBaseForm<SimpleFormView> {
	private static final long serialVersionUID = -7776688515497328826L;

	enum TreeTableFormVersionProperties {
		ACCESS, USED_BY, STATUS, LINKED_FORM, LINKED_ORGANIZATION, LINKED_VERSIONS;
	};

	public TreeTableFormVersion(TreeTableProvider<SimpleFormView> formProvider, IconProviderFormLinked iconProviderFormLinked) {
		super(formProvider, iconProviderFormLinked);
		configureContainerProperties();
		setImmediate(true);
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
				TreeTableFormVersionProperties.ACCESS, TreeTableFormVersionProperties.USED_BY,
				TreeTableFormVersionProperties.STATUS, TreeTableBaseFormProperties.ORGANIZATION,
				TreeTableFormVersionProperties.LINKED_FORM, TreeTableFormVersionProperties.LINKED_ORGANIZATION,
				TreeTableFormVersionProperties.LINKED_VERSIONS, TreeTableBaseFormProperties.CREATED_BY,
				TreeTableBaseFormProperties.CREATION_DATE, TreeTableBaseFormProperties.MODIFIED_BY,
				TreeTableBaseFormProperties.MODIFICATION_DATE });
	}

	@SuppressWarnings("unchecked")
	@Override
	public Item updateRow(IBaseFormView form) {
		Item item = super.updateRow(form);
		item.getItemProperty(TreeTableFormVersionProperties.ACCESS).setValue(
				getFormPermissionsTag((SimpleFormView) form));

		User userOfForm = UiAccesser.getUserUsingForm((IWebformsFormView) form);
		if (userOfForm != null) {
			item.getItemProperty(TreeTableFormVersionProperties.USED_BY).setValue(userOfForm.getEmailAddress());
		} else {
			item.getItemProperty(TreeTableFormVersionProperties.USED_BY).setValue("");
		}

		// Status
		item.getItemProperty(TreeTableFormVersionProperties.STATUS).setValue(
				generateStatusComboBox((IWebformsFormView) form));

		// Linked parameters
		item.getItemProperty(TreeTableFormVersionProperties.LINKED_FORM).setValue(
				((IWebformsFormView) form).getLinkedFormLabel());

		if (((IWebformsFormView) form).getLinkedFormOrganizationId() != null) {
			Organization linkedOrganization = WebformsBasicAuthorizationService.getInstance().getOrganization(
					UserSessionHandler.getUser(), ((IWebformsFormView) form).getLinkedFormOrganizationId());
			if (linkedOrganization != null) {
				item.getItemProperty(TreeTableFormVersionProperties.LINKED_ORGANIZATION).setValue(
						linkedOrganization.getName());
			}
		}

		item.getItemProperty(TreeTableFormVersionProperties.LINKED_VERSIONS).setValue(
				((IWebformsFormView) form).getLinkedFormVersions().toString().replace("[", "").replace("]", ""));

		return item;
	}

	private Component generateStatusComboBox(final IWebformsFormView form) {
		final ComboBox statusComboBox = new ComboBox();
		statusComboBox.setNullSelectionAllowed(false);
		for (FormWorkStatusUi formStatus : FormWorkStatusUi.values()) {
			statusComboBox.addItem(formStatus.getFormWorkStatus());
			statusComboBox.setItemCaption(formStatus.getFormWorkStatus(), formStatus.getLanguageCode().translation());
		}
		statusComboBox.setValue(form.getStatus());
		statusComboBox.setWidth("100%");
		statusComboBox.setImmediate(true);

		boolean userCanUpgradeStatus = WebformsBasicAuthorizationService.getInstance().isAuthorizedActivity(
				UserSessionHandler.getUser(), form, WebformsActivity.FORM_STATUS_DOWNGRADE);

		statusComboBox.setEnabled(userCanUpgradeStatus);
		statusComboBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6270860285995563296L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				if (form.getStatus().equals(statusComboBox.getValue())) {
					// Its the same status. Don't do anything.
					return;
				}

				new WindowProceedAction(LanguageCodes.CAPTION_PROCEED_MODIFY_STATUS, new AcceptActionListener() {

					@Override
					public void acceptAction(WindowAcceptCancel window) {
						changeStatus(form, statusComboBox, (FormWorkStatus) statusComboBox.getValue());
						form.setStatus((FormWorkStatus) statusComboBox.getValue());
						updateRow(form);
					}
				}, new CancelActionListener() {

					@Override
					public void cancelAction(WindowAcceptCancel window) {
						statusComboBox.setValue(form.getStatus());
					}
				});

			}
		});

		return statusComboBox;
	}

	private void changeStatus(IWebformsFormView form, ComboBox statusComboBox, FormWorkStatus value) {
		updateRow(form);
		try {
			UserSessionHandler.getController().changeFormStatus(form, value);
		} catch (NotEnoughRightsToChangeStatusException e) {
			statusComboBox.setValue(form.getStatus());
			MessageManager.showWarning(LanguageCodes.ERROR_CAPTION_NOT_ALLOWED,
					LanguageCodes.ERROR_DESCRIPTION_NOT_ENOUGH_RIGHTS);
		} catch (ElementCannotBePersistedException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
					LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
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
	 * This function returns an string with read only if the form can't be edited by the user
	 * 
	 * @param form
	 * @return
	 */
	private String getFormPermissionsTag(SimpleFormView form) {
		if (UiAccesser.getUserUsingForm(form) != null
				&& !UiAccesser.getUserUsingForm(form).equals(UserSessionHandler.getUser())) {
			return LanguageCodes.CAPTION_IN_USE.translation();
		}

		if (!WebformsBasicAuthorizationService.getInstance().isAuthorizedToForm(form, UserSessionHandler.getUser())
				&& form.getStatus().equals(FormWorkStatus.DESIGN)) {
			return LanguageCodes.CAPTION_READ_ONLY.translation();
		}
		return "";
	}

	public void selectForm(IBaseFormView form) {
		if (form == null || form.getName() == null) {
			setValue(null);
		} else {
			SimpleFormView simpleFormView = SimpleFormView.getSimpleFormView((IWebformsFormView) form);
			setValue(simpleFormView);
		}
	}

}
