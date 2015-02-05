package com.biit.webforms.gui.webpages.formmanager;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.liferay.portal.model.Organization;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WindowImportJson extends WindowAcceptCancel {
	private static final long serialVersionUID = 8945502504922675754L;
	private static final String WINDOW_WIDTH = "800px";
	private static final String WINDOW_HEIGHT = "600px";
	private TextField textField;
	private ComboBox organizationField;
	private TextArea textArea;

	public WindowImportJson() {
		super();
		configure();
		setContent(generate());
	}

	private Component generate() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();
		
		textArea = new TextArea();
		textArea.setSizeFull();

		Component component = nameOrganization(
				LanguageCodes.COMMON_CAPTION_NAME.translation(),
				LanguageCodes.COMMON_CAPTION_GROUP.translation());

		rootLayout.addComponent(textArea);
		rootLayout.addComponent(component);

		rootLayout.setExpandRatio(textArea, 1.0f);
		return rootLayout;
	}

	private Component nameOrganization(String inputFieldCaption,
			String groupCaption) {
		textField = new TextField(inputFieldCaption);
		textField.focus();
		textField.setWidth("100%");

		organizationField = new ComboBox(groupCaption);
		organizationField.setNullSelectionAllowed(false);
		organizationField.setWidth("100%");

		IActivity[] exclusivePermissionFilter = new IActivity[] { WebformsActivity.FORM_EDITING };
		try {
			Set<Organization> organizations = WebformsAuthorizationService
					.getInstance().getUserOrganizations(
							UserSessionHandler.getUser());
			Iterator<Organization> itr = organizations.iterator();
			while (itr.hasNext()) {
				Organization organization = itr.next();
				for (IActivity activity : exclusivePermissionFilter) {
					// If the user doesn't comply to all activities in the
					// filter in the group, then exit
					if (!WebformsAuthorizationService.getInstance()
							.isAuthorizedActivity(UserSessionHandler.getUser(),
									organization, activity)) {
						itr.remove();
						break;
					}
				}
			}
			for (Organization organization : organizations) {
				organizationField.addItem(organization);
				organizationField.setItemCaption(organization,
						organization.getName());
			}
			if (!organizations.isEmpty()) {
				Iterator<Organization> organizationsIterator = organizations
						.iterator();
				organizationField.setValue(organizationsIterator.next());
			}
			if (organizations.size() <= 1) {
				organizationField.setEnabled(false);
			}
		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager
					.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
		}

		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);

		rootLayout.addComponent(textField);
		rootLayout.addComponent(organizationField);
		rootLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(organizationField,
				Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(textField, 0.6f);
		rootLayout.setExpandRatio(organizationField, 0.4f);
		return rootLayout;
	}

	private void configure() {
		setDraggable(true);
		setResizable(false);
		setModal(true);
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
		addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				try {
					UserSessionHandler.getController().importFormFromJson(
							textArea.getValue(),
							textField.getValue(),
							((Organization) organizationField.getValue())
									.getOrganizationId());
				} catch (UnexpectedDatabaseException e) {
					MessageManager
							.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
				} catch (FormWithSameNameException e) {
					MessageManager
							.showError(LanguageCodes.ERROR_FORM_ALREADY_EXISTS);
				} catch (FieldTooLongException e) {
					MessageManager
					.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
							LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
	}

}
