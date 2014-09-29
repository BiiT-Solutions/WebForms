package com.biit.webforms.gui.common.components;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;

import com.biit.security.DESEncryptor;
import com.biit.security.exceptions.DESEncryptorException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WindowAboutUs extends Window {
	private static final long serialVersionUID = -3852811369262597168L;
	private static final String width = "500px";
	// Height is mandatory to delimit the position of the elements
	private static final String height = "500px";
	private static final String LOGIN_PARAM = "param1";
	private static final String PASSWORD_PARAM = "param2";

	private VerticalLayout navigationView;

	public WindowAboutUs() {
		setModal(true);

		setWidth(width);
		setHeight(height);
		setContent(navigationView);

		setContent(createContent());
		setCaption(LanguageCodes.ABOUT_US_TITLE.translation());
		setClosable(false);
		setResizable(false);
		setDraggable(false);
	}

	private Component createCancelButton() {
		IconButton cancelButton = new IconButton(LanguageCodes.CAPTION_CLOSE, CommonThemeIcon.CANCEL,
				LanguageCodes.TOOLTIP_CLOSE, new ClickListener() {
					private static final long serialVersionUID = -2391267793996787730L;

					@Override
					public void buttonClick(ClickEvent event) {
						close();
					}
				});

		return cancelButton;
	}

	private Component generateText() {
		ServletContext context = VaadinServlet.getCurrent().getServletContext();
		Manifest manifest;
		String version = null;
		try {
			manifest = new Manifest(context.getResourceAsStream("/META-INF/MANIFEST.MF"));
			Attributes attributes = manifest.getMainAttributes();
			version = attributes.getValue("Implementation-Version");
		} catch (IOException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		Label nameLabel = new Label("<h3><center>" + LanguageCodes.ABOUT_US_TOOL_NAME.translation() + "</center></h3>"
				+ "<center><p align=\"justify\">" + LanguageCodes.ABOUT_US_TOOL_PURPOUSE.translation()
				+ "</p></center>", ContentMode.HTML);
		nameLabel.setWidth("100%");
		Label versionLabel = new Label("Version: " + version);
		Label textLabel = new Label("<center><p align=\"justify\">" + LanguageCodes.ABOUT_US_BIIT.translation()
				+ "</p></center>", ContentMode.HTML);
		textLabel.setWidth("100%");

		String linkUrl = getIssueUrl();

		Label linkLabel = null;
		if (linkUrl != null && !linkUrl.isEmpty()) {
			linkLabel = new Label("<center><a href=\"" + linkUrl + "\" target=\"_blank\">"
					+ LanguageCodes.ABOUT_US_REPORT.translation() + "</a></center>", ContentMode.HTML);
		}

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		rootLayout.addComponent(nameLabel);
		rootLayout.setComponentAlignment(nameLabel, Alignment.MIDDLE_CENTER);
		versionLabel.setSizeUndefined();
		rootLayout.addComponent(versionLabel);
		rootLayout.setComponentAlignment(versionLabel, Alignment.MIDDLE_CENTER);
		rootLayout.addComponent(textLabel);
		rootLayout.setComponentAlignment(textLabel, Alignment.MIDDLE_CENTER);
		if (linkLabel != null) {
			rootLayout.addComponent(linkLabel);
			rootLayout.setComponentAlignment(linkLabel, Alignment.MIDDLE_CENTER);
		}

		return rootLayout;
	}

	private Component createContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		Component textLayout = generateText();
		Component cancelButton = createCancelButton();

		rootLayout.addComponent(textLayout);
		rootLayout.addComponent(cancelButton);

		rootLayout.setComponentAlignment(textLayout, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_CENTER);

		rootLayout.setExpandRatio(textLayout, 1.0f);

		return rootLayout;
	}

	private String getIssueUrl() {
		String separatorChar;
		String issueManagerUrl = WebformsConfigurationReader.getInstance().getIssueManagerUrl();
		if (issueManagerUrl == null || issueManagerUrl.isEmpty()) {
			return null;
		}
		// Starts param definition
		if (!issueManagerUrl.contains("?")) {
			separatorChar = "?";
		} else {
			// Exists another parameteres.
			separatorChar = "&";
		}

		String desEncryptedString;
		try {
			desEncryptedString = DESEncryptor.encrypt(UserSessionHandler.getUser().getPassword());
		} catch (DESEncryptorException e) {
			WebformsLogger.errorMessage(WindowAboutUs.class.getName(), e);
			MessageManager.showError(e.getMessage());
			desEncryptedString = "error";
		}

		return issueManagerUrl + separatorChar + LOGIN_PARAM + "=" + UserSessionHandler.getUser().getEmailAddress()
				+ "&" + PASSWORD_PARAM + "=" + desEncryptedString;
	}

	public void showCentered() {
		center();
		UI.getCurrent().addWindow(this);
	}
}
