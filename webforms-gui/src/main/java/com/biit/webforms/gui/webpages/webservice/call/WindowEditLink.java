package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField.SearchFormElementChanged;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.WebserviceCallLink;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class WindowEditLink extends WindowAcceptCancel{
	private static final long serialVersionUID = -8577100119556278102L;

	private static final String WINDOW_WIDTH = "300px";
	private static final String WINDOW_HEIGHT = "200px";
	
	private WebserviceCallLink value;
	private final FormLayout rootLayout;
	private final SearchFormElementField searchFormElement;
	
	public WindowEditLink() {
		super();
		rootLayout = new FormLayout();
		searchFormElement = new SearchFormElementField(Form.class, Category.class, Group.class,SystemField.class,Question.class);
		configure();
		setContent(generateContent());
	}

	protected Component generateContent() {
		searchFormElement.setCaption(LanguageCodes.CAPTION_SELECT_FORM_ELEMENT.translation());
		searchFormElement.setWidth("100%");
		searchFormElement.setSelectableFilter(SystemField.class,Question.class);
		searchFormElement.addValueChangeListener(new SearchFormElementChanged() {
			
			@Override
			public void currentElement(Object object) {
				updateAcceptButton();
			}
		});
		
		updateAcceptButton();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);
		rootLayout.addComponent(searchFormElement);
		
		return rootLayout;
	}

	protected void updateAcceptButton() {
		getAcceptButton().setEnabled(searchFormElement.getValue()!=null);
	}

	protected void configure() {
		setDraggable(true);
		setModal(true);
		setResizable(false);

		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
	}

	public void setValue(WebserviceCallLink value) {
		this.value = value;
		searchFormElement.setTreeObject(value.getFormElement());
		updateAcceptButton();
	}

	public WebserviceCallLink getValue() {
		return value;
	}

	public FormLayout getRootLayout() {
		return rootLayout;
	}
	
	public void updateValue(){
		getValue().setFormElement((BaseQuestion) searchFormElement.getValue());
	}

	protected SearchFormElementField getSearchFormElement() {
		return searchFormElement;
	}
	
}
