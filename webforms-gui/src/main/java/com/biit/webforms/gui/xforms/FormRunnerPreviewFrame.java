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
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;

/**
 * Frame to open a preview of a FormRunner form in a new window.
 */
public class FormRunnerPreviewFrame extends UI {
    public static final String FORM_PARAMETER_TAG = "form";
    public static final String FORM_VERSION_PARAMETER_TAG = "version";
    public static final String FORM_ORGANIZATION_PARAMETER_TAG = "organizationId";
    private static final long serialVersionUID = -4957704029911591631L;


    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Preview");
        final String formName = request.getParameter(FORM_PARAMETER_TAG) != null ? request.getParameter(FORM_PARAMETER_TAG).trim() : null;
        final String formVersion = request.getParameter(FORM_VERSION_PARAMETER_TAG);
        final String organization = request.getParameter(FORM_ORGANIZATION_PARAMETER_TAG);

        if (formName == null || formVersion == null || organization == null) {
            MessageManager.showWarning(LanguageCodes.WARNING_FORM_VALIDATION, LanguageCodes.WARNING_FORM_VALIDATION_BODY);
            this.close();
        } else {
            final String requestUrl = "preview?form=" + formName + "&version=" + formVersion + "&organizationId=" + organization;

            String url = WebformsConfigurationReader.getInstance().getFormRunnerUrl();

            WebformsLogger.debug(this.getClass().getName(), "Opening URL: " + url + "/" + requestUrl);
            BrowserFrame browser = new BrowserFrame(null, new ExternalResource(url + "/" + requestUrl));

            browser.setImmediate(true);
            browser.setSizeFull();

            setContent(browser);
        }
    }

}
