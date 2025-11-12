package com.biit.webforms.gui.webpages.webservice.call;

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

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.biit.webforms.webservices.WebservicePort;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.*;

public class WindowEditInputLink extends WindowEditLink {
	private static final long serialVersionUID = -8577100119556278102L;
	private static final String WINDOW_WIDTH = "600px";
	private static final String WINDOW_HEIGHT = "280px";

	private static final String ERROR_MESSAGES_LAYOUT = "v-error-messages-layout";

	private FormLayout errorMessages;
	private VerticalLayout errorMessagesBorder;

	public WindowEditInputLink() {
		super();
	}

	@Override
	protected Component generateContent() {
		super.generateContent();
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);

		rootLayout.addComponent(getSearchFormElement());

		errorMessages = new FormLayout();
		errorMessages.setSpacing(true);
		errorMessages.setMargin(true);
		errorMessages.setWidth("100%");
		errorMessages.setHeightUndefined();

		errorMessagesBorder = new VerticalLayout();
		errorMessagesBorder.addStyleName(ERROR_MESSAGES_LAYOUT);
		errorMessagesBorder.setCaption(LanguageCodes.CAPTION_WEBSERVICE_CALL_VALIDATION_LINK.translation());
		errorMessagesBorder.setSizeFull();

		rootLayout.addComponent(errorMessagesBorder);
		rootLayout.setExpandRatio(errorMessagesBorder, 1.0f);

		errorMessagesBorder.addComponent(errorMessages);

		return rootLayout;
	}

	@Override
	public void updateValue() {
		super.updateValue();
		HashMap<String, String> errorValues = new HashMap<>();
		Iterator<Component> itr = errorMessages.iterator();
		while (itr.hasNext()) {
			TextField field = (TextField) itr.next();
			errorValues.put(field.getCaption(), field.getValue());
		}
		WebserviceCallInputLink link = (WebserviceCallInputLink) getLink();
		for (WebserviceCallInputLinkErrors error : link.getErrors()) {
			error.setErrorMessage(errorValues.get(error.getErrorCode()));
		}
	}

	@Override
	public void setValue(WebserviceCallLink value, WebservicePort port) {
		super.setValue(value, port);
		WebserviceCallInputLink link = (WebserviceCallInputLink) value;

		List<WebserviceCallInputLinkErrors> orderedErrors = new ArrayList<>();
		orderedErrors.addAll(link.getErrors());
		Collections.sort(orderedErrors, new Comparator<WebserviceCallInputLinkErrors>() {

			@Override
			public int compare(WebserviceCallInputLinkErrors o1, WebserviceCallInputLinkErrors o2) {
				return o1.getErrorCode().compareTo(o2.getErrorCode());
			}
		});

		for (WebserviceCallInputLinkErrors error : orderedErrors) {
			TextField newField = new TextField();
			newField.setCaption(error.getErrorCode());
			newField.setValue(error.getErrorMessage());
			newField.setWidth("100%");
			errorMessages.addComponent(newField);
		}
	}

	@Override
	protected void configure() {
		super.configure();

		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);

		setCaption(LanguageCodes.WINDOW_EDIT_INPUT_LINK.translation());
	}
}
