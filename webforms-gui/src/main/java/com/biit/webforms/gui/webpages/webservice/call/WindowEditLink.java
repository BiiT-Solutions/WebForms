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
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.biit.webforms.webservices.WebservicePort;
import com.vaadin.data.Container.Filter;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class WindowEditLink extends WindowAcceptCancel {
	private static final long serialVersionUID = -8577100119556278102L;

	private static final String WINDOW_WIDTH = "500px";
	private static final String WINDOW_HEIGHT = "200px";
	private static final String SEARCH_FORM_ELEMENT_WIDTH = "400px";

	private WebserviceCallLink link;
	private final FormLayout rootLayout;
	private final SearchFormElementField searchFormElement;

	private Filter filter;

	public WindowEditLink() {
		super();
		rootLayout = new FormLayout();
		searchFormElement = new SearchFormElementField(Form.class, Category.class, Group.class, BaseQuestion.class);
		configure();
		setContent(generateContent());
	}

	protected Component generateContent() {
		searchFormElement.setCaption(LanguageCodes.CAPTION_SELECT_FORM_ELEMENT.translation());
		searchFormElement.setWidth(SEARCH_FORM_ELEMENT_WIDTH);
		searchFormElement.setSelectableFilter(SystemField.class, Question.class);
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
		getAcceptButton().setEnabled(searchFormElement.getValue() != null);
	}

	protected void configure() {
		setDraggable(true);
		setModal(true);
		setResizable(false);

		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
	}

	public void setValue(WebserviceCallLink value, WebservicePort port) {
		this.link = value;
		searchFormElement.setTreeObject(value.getFormElement());
		if (filter != null) {
			searchFormElement.removeFilter(filter);
		}
		if (port != null) {
			filter = new FormElementCompatibilityFilter(port.getType(), port.getFormat(), port.getSubformat());
			searchFormElement.addFilter(filter);
		}

		updateAcceptButton();
	}

	public WebserviceCallLink getLink() {
		return link;
	}

	public FormLayout getRootLayout() {
		return rootLayout;
	}

	public void updateValue() {
		getLink().setFormElement((BaseQuestion) searchFormElement.getValue());
	}

	protected SearchFormElementField getSearchFormElement() {
		return searchFormElement;
	}

	public void addFilter(Filter filter) {
		searchFormElement.addFilter(filter);
	}

	public void removeFilter(Filter filter) {
		searchFormElement.removeFilter(filter);
	}
}
