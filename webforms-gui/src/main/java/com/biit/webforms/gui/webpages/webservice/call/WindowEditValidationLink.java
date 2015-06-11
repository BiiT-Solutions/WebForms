package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.WebserviceCallLink;
import com.biit.webforms.persistence.entity.WebserviceCallValidationLink;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class WindowEditValidationLink extends WindowEditInputLink{
	private static final long serialVersionUID = -4344499824863702272L;
	private static final String WINDOW_WIDTH = "350px";
	private static final String WINDOW_HEIGHT = "200px";
	
	private TextField message;
	
	public WindowEditValidationLink() {
		super();
	}

	@Override
	protected Component generateContent() {
		FormLayout rootLayout = (FormLayout) super.generateContent();
		
		message = new TextField();
		message.setCaption(LanguageCodes.CAPTION_VALIDATION_LINK_ERROR_MESSAGE.translation());
		message.setRequired(true);
		message.setImmediate(true);
		message.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6347342305412231512L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateAcceptButton();
			}
		});
		message.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = -2838456599128660687L;

			@Override
			public void textChange(TextChangeEvent event) {
				message.setValue(event.getText());
			}
		});
		message.setBuffered(false);
		message.setWidth("100%");
		message.setTextChangeEventMode(TextChangeEventMode.EAGER);
		
		getAcceptButton().setEnabled(false);
		
		rootLayout.addComponent(message);
			
		return rootLayout;
	}
	
	@Override
	protected void updateAcceptButton() {
		getAcceptButton().setEnabled(getSearchFormElement().getValue()!=null && message.getValue() != null && !message.getValue().isEmpty());
	}
	
	@Override
	protected void configure() {
		super.configure();
		setCaption(LanguageCodes.CAPTION_WINDOW_EDIT_VALIDATION_LINK.translation());
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
	}
	
	@Override
	public void updateValue(){
		super.updateValue();
		((WebserviceCallValidationLink)getValue()).setErrorMessage(getMessage());
	}
	
	@Override
	public void setValue(WebserviceCallLink value) {
		message.setValue(((WebserviceCallValidationLink)value).getErrorMessage());
		super.setValue(value);
	}

	public String getMessage() {
		return message.getValue();
	}

	protected boolean acceptAction() {
		return message.isValid();
	}

}
