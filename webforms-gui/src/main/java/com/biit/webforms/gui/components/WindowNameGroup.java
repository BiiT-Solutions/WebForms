package com.biit.webforms.gui.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.liferay.portal.model.Organization;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class WindowNameGroup extends WindowAcceptCancel {
	private static final long serialVersionUID = -5944303129765599933L;
	private static final String width = "640px";
	private static final String height = "180px";

	private TextField textField;
	private ComboBox organizationField;
	private IActivity[] exclusivePermisssionFilter;

	public WindowNameGroup(String inputFieldCaption, String groupCaption, IActivity[] exclusivePermisssionFilter) {
		super();
		this.exclusivePermisssionFilter = exclusivePermisssionFilter;
		setContent(generateContent(inputFieldCaption, groupCaption));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	public void setDefaultValue(String nullValue) {
		textField.setValue(nullValue);
	}

	public String getValue() {
		return textField.getValue();
	}

	public Organization getOrganization() {
		return (Organization) organizationField.getValue();
	}

	private Component generateContent(String inputFieldCaption, String groupCaption) {
		textField = new TextField(inputFieldCaption);
		textField.focus();
		textField.setWidth("100%");

		organizationField = new ComboBox(groupCaption);
		organizationField.setNullSelectionAllowed(false);
		organizationField.setWidth("100%");
		try {
			List<Organization> organizations = WebformsAuthorizationService.getInstance().getUserOrganizations(
					UserSessionHandler.getUser());
			Iterator<Organization> itr = organizations.iterator();
			while(itr.hasNext()){
				Organization organization = itr.next();
				for(IActivity activity: exclusivePermisssionFilter){
					//If the user doesn't comply to all activities in the filter in the group, then exit  
					if(!WebformsAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(), organization, activity)){
						itr.remove();
						break;
					}
				}				
			}
			for (Organization organization : organizations) {
				organizationField.addItem(organization);
				organizationField.setItemCaption(organization, organization.getName());
			}
			if (!organizations.isEmpty()) {
				organizationField.setValue(organizations.get(0));
			}
			if (organizations.size() <= 1) {
				organizationField.setEnabled(false);
			}
		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
		}

		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		rootLayout.addComponent(textField);
		rootLayout.addComponent(organizationField);
		rootLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(organizationField, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(textField, 0.6f);
		rootLayout.setExpandRatio(organizationField, 0.4f);
		return rootLayout;
	}

	public void setValue(String value) {
		textField.setValue(value);
	}
}
