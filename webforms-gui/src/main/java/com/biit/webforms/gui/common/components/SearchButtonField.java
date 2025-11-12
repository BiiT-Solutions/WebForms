package com.biit.webforms.gui.common.components;

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

import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField.SearchFormElementChanged;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import java.util.ArrayList;
import java.util.List;

public class SearchButtonField extends CustomComponent {
	private static final long serialVersionUID = -2865532209097063977L;
	private static final String CLASSNAME = "v-search-button-field";
	private static final String TEXT_FIELD_FULL_WIDTH = "100%";
	private static final String LABEL_HEIGHT = "25px";
	private static final String LABEL_BOX_STYLE = "v-label-box";

	private IconOnlyButton searchButton;
	private IconOnlyButton removeButton;
	private Label label;
	private HorizontalLayout labelBox;
	private Object value;
	private List<SearchFormElementChanged> valueChangeListeners;
	private List<OpenSearchComponentListener> openSearchComponentListeners;

	public SearchButtonField() {
		super();
		valueChangeListeners = new ArrayList<SearchFormElementChanged>();
		openSearchComponentListeners = new ArrayList<>();
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

		labelBox = new HorizontalLayout();
		labelBox.setStyleName(LABEL_BOX_STYLE);
		labelBox.setMargin(false);
		labelBox.setWidth(TEXT_FIELD_FULL_WIDTH);
		labelBox.setHeight(LABEL_HEIGHT);
		labelBox.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 6542167138988021658L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if (event.getButton() == MouseButton.LEFT) {
					fireOpenSearchComponentListener();
				}
			}
		});

		label = new Label();
		label.setWidth(null);

		labelBox.addComponent(label);

		clear();

		rootLayout.addComponent(searchButton);
		rootLayout.addComponent(labelBox);
		rootLayout.addComponent(removeButton);

		rootLayout.setExpandRatio(labelBox, 1.0f);

		searchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 6896065935607888633L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireOpenSearchComponentListener();
			}
		});
		removeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7850712589452231075L;

			@Override
			public void buttonClick(ClickEvent event) {
				clear();
			}
		});

		return rootLayout;
	}

	public void addOpenSearchComponentListener(OpenSearchComponentListener listener) {
		openSearchComponentListeners.add(listener);
	}

	public void removeOpenSearchComponentListener(OpenSearchComponentListener listener) {
		openSearchComponentListeners.remove(listener);
	}

	protected void fireOpenSearchComponentListener() {
		for (OpenSearchComponentListener listener : openSearchComponentListeners) {
			listener.openSearchComponent(getValue());
		}
	}

	public void setValue(Object value, String valueLabel) {
		this.value = value;
		if (value != null) {
			label.setValue(valueLabel);
			label.setCaption(valueLabel);
			removeButton.setEnabled(true);
		} else {
			label.setValue("");
			label.setCaption("");
			removeButton.setEnabled(false);
		}
		fireValueChangeListeners();
	}

	public Object getValue() {
		return value;
	}

	public void clear() {
		setValue(null, null);
	}

	public void setNullCaption(ILanguageCode languageCode) {
		label.setValue(languageCode.translation());
	}

	public void addValueChangeListener(SearchFormElementChanged listener) {
		valueChangeListeners.add(listener);
	}

	public void removeValueChangeListener(SearchFormElementChanged listener) {
		valueChangeListeners.remove(listener);
	}

	private void fireValueChangeListeners() {
		for (SearchFormElementChanged listener : valueChangeListeners) {
			listener.currentElement(getValue());
		}
	}
}
