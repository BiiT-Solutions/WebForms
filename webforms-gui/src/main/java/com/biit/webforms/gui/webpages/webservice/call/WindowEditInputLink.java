package com.biit.webforms.gui.webpages.webservice.call;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.biit.webforms.webservices.WebservicePort;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WindowEditInputLink extends WindowEditLink{
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
	public void updateValue(){
		super.updateValue();
		HashMap<String, String> errorValues = new HashMap<>();
		Iterator<Component> itr = errorMessages.iterator();
		while(itr.hasNext()){
			TextField field = (TextField)itr.next();
			errorValues.put(field.getCaption(),field.getValue());
		}
		WebserviceCallInputLink link = (WebserviceCallInputLink) getLink();
		for(WebserviceCallInputLinkErrors error: link.getErrors()){
			error.setErrorMessage(errorValues.get(error.getErrorCode()));
		}
	}
	
	@Override
	public void setValue(WebserviceCallLink value, WebservicePort port) {
		super.setValue(value,port);
		WebserviceCallInputLink link = (WebserviceCallInputLink) value;
		
		List<WebserviceCallInputLinkErrors> orderedErrors = new ArrayList<>();
		orderedErrors.addAll(link.getErrors());
		Collections.sort(orderedErrors, new Comparator<WebserviceCallInputLinkErrors>() {

			@Override
			public int compare(WebserviceCallInputLinkErrors o1, WebserviceCallInputLinkErrors o2) {
				return o1.getErrorCode().compareTo(o2.getErrorCode());
			}
		});		
		
		for(WebserviceCallInputLinkErrors error: orderedErrors){
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
