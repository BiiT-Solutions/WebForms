package com.biit.webforms.gui.components;

import java.util.Iterator;
import java.util.Set;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.security.IWebformsSecurityService;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinServlet;
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
	private IActivity[] exclusivePermissionFilter;

	private IWebformsSecurityService webformsSecurityService;

	public WindowNameGroup(String inputFieldCaption, String groupCaption, IActivity[] exclusivePermissionFilter) {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
		this.exclusivePermissionFilter = exclusivePermissionFilter;
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

	@SuppressWarnings("unchecked")
	public IGroup<Long> getOrganization() {
		return (IGroup<Long>) organizationField.getValue();
	}

	private Component generateContent(String inputFieldCaption, String groupCaption) {
		textField = new TextField(inputFieldCaption);
		textField.focus();
		textField.setWidth("100%");
		// textField.addValidator(new
		// ValidatorTreeObjectName(BaseForm.NAME_ALLOWED));
		// textField.addValidator(new ValidatorTreeObjectNameLength());

		textField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4953347262492851075L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				textField.isValid();
			}
		});

		organizationField = new ComboBox(groupCaption);
		organizationField.setNullSelectionAllowed(false);
		organizationField.setWidth("100%");
		try {
			Set<IGroup<Long>> organizations = webformsSecurityService.getUserOrganizations(UserSession.getUser());
			Iterator<IGroup<Long>> itr = organizations.iterator();
			while (itr.hasNext()) {
				IGroup<Long> organization = itr.next();
				for (IActivity activity : exclusivePermissionFilter) {
					// If the user doesn't comply to all activities in the
					// filter in the group, then exit
					if (!webformsSecurityService.isAuthorizedActivity(UserSession.getUser(), organization, activity)) {
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

	public boolean isValid() {
		return textField.isValid();
	}
}
