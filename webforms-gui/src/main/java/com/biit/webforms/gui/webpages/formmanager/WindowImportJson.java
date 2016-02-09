package com.biit.webforms.gui.webpages.formmanager;

import java.util.Iterator;
import java.util.Set;

import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.exceptions.FormWithSameNameException;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.security.IWebformsSecurityService;
import com.biit.webforms.security.WebformsActivity;
import com.vaadin.server.VaadinServlet;
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

	private IWebformsSecurityService webformsSecurityService;

	public WindowImportJson() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
		configure();
		setContent(generate());
	}

	private Component generate() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		textArea = new TextArea();
		textArea.setSizeFull();

		Component component = nameOrganization(LanguageCodes.COMMON_CAPTION_NAME.translation(),
				LanguageCodes.COMMON_CAPTION_GROUP.translation());

		rootLayout.addComponent(textArea);
		rootLayout.addComponent(component);

		rootLayout.setExpandRatio(textArea, 1.0f);
		return rootLayout;
	}

	private Component nameOrganization(String inputFieldCaption, String groupCaption) {
		textField = new TextField(inputFieldCaption);
		textField.focus();
		textField.setWidth("100%");

		organizationField = new ComboBox(groupCaption);
		organizationField.setNullSelectionAllowed(false);
		organizationField.setWidth("100%");

		IActivity[] exclusivePermissionFilter = new IActivity[] { WebformsActivity.FORM_EDITING };
		try {
			Set<IGroup<Long>> organizations = webformsSecurityService.getUserOrganizations(UserSessionHandler.getUser());
			Iterator<IGroup<Long>> itr = organizations.iterator();
			while (itr.hasNext()) {
				IGroup<Long> organization = itr.next();
				for (IActivity activity : exclusivePermissionFilter) {
					// If the user doesn't comply to all activities in the
					// filter in the group, then exit
					if (!webformsSecurityService.isAuthorizedActivity(UserSessionHandler.getUser(), organization, activity)) {
						itr.remove();
						break;
					}
				}
			}
			for (IGroup<Long> organization : organizations) {
				organizationField.addItem(organization);
				organizationField.setItemCaption(organization, organization.getUniqueName());
			}
			if (!organizations.isEmpty()) {
				Iterator<IGroup<Long>> organizationsIterator = organizations.iterator();
				organizationField.setValue(organizationsIterator.next());
			}
			if (organizations.size() <= 1) {
				organizationField.setEnabled(false);
			}
		} catch (UserManagementException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
		}

		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);

		rootLayout.addComponent(textField);
		rootLayout.addComponent(organizationField);
		rootLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(organizationField, Alignment.MIDDLE_CENTER);
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
	}

	@SuppressWarnings("unchecked")
	protected boolean acceptAction() {
		try {
			UserSessionHandler.getController().importFormFromJson(textArea.getValue(), textField.getValue(),
					((IGroup<Long>) organizationField.getValue()).getId());
			return true;
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
		} catch (FormWithSameNameException e) {
			MessageManager.showError(LanguageCodes.ERROR_FORM_ALREADY_EXISTS);
		} catch (FieldTooLongException e) {
			MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
		} catch (ElementCannotBePersistedException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
		return false;
	}

}
