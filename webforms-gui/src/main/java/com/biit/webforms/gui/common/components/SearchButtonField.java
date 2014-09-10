package com.biit.webforms.gui.common.components;

import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class SearchButtonField extends CustomComponent {
	private static final long serialVersionUID = -2865532209097063977L;
	private static final String CLASSNAME = "v-search-button-field";
	private static final String TEXT_FIELD_FULL_WIDTH = "100%";

	private IconOnlyButton searchButton;
	private IconOnlyButton removeButton;
	private TextField textField;
	private Object value;

	public SearchButtonField() {
		super();
		setCompositionRoot(generateComponent());
		setWidth(TEXT_FIELD_FULL_WIDTH);
		setStyleName(CLASSNAME);
	}

	private Component generateComponent() {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setMargin(false);
		rootLayout.setWidth(TEXT_FIELD_FULL_WIDTH);

		searchButton = new IconOnlyButton(CommonThemeIcon.SEARCH.getThemeResource());
		removeButton = new IconOnlyButton(CommonThemeIcon.REMOVE.getThemeResource());
		searchButton.setWidth("20px");
		removeButton.setWidth("20px");
		textField = new TextField();
		textField.setEnabled(false);
		textField.setWidth(TEXT_FIELD_FULL_WIDTH);
		clear();

		rootLayout.addComponent(searchButton);
		rootLayout.addComponent(textField);
		rootLayout.addComponent(removeButton);
		
		rootLayout.setExpandRatio(textField, 1.0f);

		removeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7850712589452231075L;

			@Override
			public void buttonClick(ClickEvent event) {
				clear();
			}
		});

		return rootLayout;
	}

	public void setValue(Object value, String valueLabel) {
		this.value = value;
		if (value != null) {
			textField.setValue(valueLabel);
			removeButton.setEnabled(true);
		} else {
			textField.setValue("");
			removeButton.setEnabled(false);
		}
	}

	public Object getValue() {
		return value;
	}

	public void clear() {
		setValue(null, null);
	}

	public void addSearchButtonListener(ClickListener listener) {
		searchButton.addClickListener(listener);
	}

	public void setNullCaption(ILanguageCode languageCode) {
		textField.setInputPrompt(languageCode.translation());
	}
}
