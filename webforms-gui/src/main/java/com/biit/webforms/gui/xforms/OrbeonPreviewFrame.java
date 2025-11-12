package com.biit.webforms.gui.xforms;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
	public static final String FORM_PARAMETER_TAG = "form_param";
	public static final String APPLICATION_PARAMETER_TAG = "application_param";
	public static final String FORM_VERSION_PARAMETER_TAG = "form_version_param";
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
