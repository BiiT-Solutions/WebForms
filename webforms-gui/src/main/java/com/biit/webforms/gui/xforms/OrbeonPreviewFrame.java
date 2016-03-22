package com.biit.webforms.gui.xforms;

import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;

/**
 * Frame to open a preview of an Orbeon form in a new window.
 */
public class OrbeonPreviewFrame extends UI {
	public final static String FORM_PARAMETER_TAG = "form_param";
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
		this.form = request.getParameter(FORM_PARAMETER_TAG);
		this.application = request.getParameter(APPLICATION_PARAMETER_TAG);
		this.version = request.getParameter(FORM_VERSION_PARAMETER_TAG);

		WebformsLogger.debug(this.getClass().getName(), "Opening URL: " + WebformsConfigurationReader.getInstance().getOrbeonFormRunnerUrl() + "/"
				+ application + "/" + form + "/new");
		BrowserFrame browser = new BrowserFrame(null, new ExternalResource(WebformsConfigurationReader.getInstance().getOrbeonFormRunnerUrl() + "/"
				+ application + "/" + form + "/new"));

		browser.setImmediate(true);
		browser.setSizeFull();

		setContent(browser);
	}

}
