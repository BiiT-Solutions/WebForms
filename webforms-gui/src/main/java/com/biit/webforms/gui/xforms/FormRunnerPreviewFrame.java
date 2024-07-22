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
	public final static String FORM_PARAMETER_TAG = "form";
	public final static String FORM_VERSION_PARAMETER_TAG = "version";
	public final static String FORM_ORGANIZATION_PARAMETER_TAG = "organization";
	private static final long serialVersionUID = -4957704029911591631L;


	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Preview");
		final String formName = request.getParameter(FORM_PARAMETER_TAG).trim();
		final String formVersion = request.getParameter(FORM_VERSION_PARAMETER_TAG);
		final String organization = request.getParameter(FORM_ORGANIZATION_PARAMETER_TAG);
        final String requestUrl = "preview?form=" + formName + "&version=" + formVersion + "&organization=" + organization;

		String url = WebformsConfigurationReader.getInstance().getFormRunnerUrl();

		WebformsLogger.debug(this.getClass().getName(), "Opening URL: " + url + "/" + requestUrl);
		BrowserFrame browser = new BrowserFrame(null, new ExternalResource(url + "/" + requestUrl));

		browser.setImmediate(true);
		browser.setSizeFull();

		setContent(browser);
	}

}
