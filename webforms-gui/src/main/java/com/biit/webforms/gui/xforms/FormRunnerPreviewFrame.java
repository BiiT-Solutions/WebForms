package com.biit.webforms.gui.xforms;

import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;

/**
 * Frame to open a preview of a FormRunner form in a new window.
 */
public class FormRunnerPreviewFrame extends UI {
	public final static String FORM_PARAMETER_TAG = "form_param";
	public final static String FORM_NAME_TAG = "form_name";
	public final static String APPLICATION_PARAMETER_TAG = "application_param";
	public final static String FORM_VERSION_PARAMETER_TAG = "form_version_param";
	private static final long serialVersionUID = -4957704029911500631L;
	private String form;
	private String application;
	@SuppressWarnings("unused")
	private String version;

	public String getApplication() {
		return application;
	}

	public String getForm() {
		return form;
	}

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Preview");
		String form_name = request.getParameter(FORM_NAME_TAG).trim();
		String parsedFormName = parseName(form_name);
		this.form = "?preview=" + parsedFormName;
		// this.application = request.getParameter(APPLICATION_PARAMETER_TAG);
		this.application = "formrunner";
		// this.version = request.getParameter(FORM_VERSION_PARAMETER_TAG);
		// String url3 = "https://testing.biit-solutions.com";// put in your url
		String url = WebformsConfigurationReader.getInstance().getFormrunnerJSRestService();

		WebformsLogger.debug(this.getClass().getName(), "Opening URL: " + url + "/" + form);
		BrowserFrame browser = new BrowserFrame(null, new ExternalResource(url + "/" + form));

		browser.setImmediate(true);
		browser.setSizeFull();

		setContent(browser);
	}

	private String parseName(String formName) {
		String[] splittedName = formName.split(" ");
		String parsedName = "";
		for (int i = 0; i < splittedName.length; i++) {
			parsedName = parsedName + splittedName[i];
			if (i < splittedName.length - 1) {
				parsedName = parsedName + "_";
			}
		}
		return parsedName;
	}

}
