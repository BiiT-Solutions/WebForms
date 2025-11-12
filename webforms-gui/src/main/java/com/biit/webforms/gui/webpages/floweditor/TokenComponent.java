package com.biit.webforms.gui.webpages.floweditor;

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

import com.biit.webforms.gui.webpages.floweditor.listeners.TokenDoubleClickListener;
import com.biit.webforms.gui.webpages.floweditor.listeners.TokenSingleClickListener;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

import java.util.ArrayList;
import java.util.List;

public class TokenComponent extends CustomComponent {

	private static final long serialVersionUID = -3166853150904431898L;
	private static final String FOCUSED = "token-component-focused";
	private static final String CLASSNAME = "token-component";
	private static final String CLASSNAME_LAYOUT = "token-component-layout";
	private static final String TOKEN_HEIGHT = "23px";
	private CssLayout rootLayout;

	private Token token;
	private Label label;

	private List<TokenSingleClickListener> singleClickListeners;
	private List<TokenDoubleClickListener> doubleClickListeners;

	public TokenComponent(Token token) {
		super();
		this.token = token;

		singleClickListeners = new ArrayList<TokenSingleClickListener>();
		doubleClickListeners = new ArrayList<TokenDoubleClickListener>();

		setStyleName(CLASSNAME);
		setHeight(TOKEN_HEIGHT);
		setWidth(null);
		setCompositionRoot(generate());
		initialize();
	}

	private Component generate() {
		rootLayout = new CssLayout();
		rootLayout.setStyleName(CLASSNAME_LAYOUT);
		rootLayout.setHeight(TOKEN_HEIGHT);
		rootLayout.setWidth(null);

		rootLayout.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 887512468581098021L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				if (event.isDoubleClick()) {
					fireDoubleClickLayoutListener();
				} else {
					fireSingleClickLayoutListener();
				}
			}
		});

		label = new Label();
		label.setImmediate(true);

		rootLayout.addComponent(label);

		return rootLayout;
	}

	private void initialize() {
		refresh();
	}

	public void loseFocus() {
		removeStyleName(FOCUSED);
		rootLayout.removeStyleName(FOCUSED);
	}

	@Override
	public void focus() {
		addStyleName(FOCUSED);
		rootLayout.addStyleName(FOCUSED);
	}

	public void addTokenSingleClick(TokenSingleClickListener listener) {
		singleClickListeners.add(listener);
	}

	public void addTokenDoubleClick(TokenDoubleClickListener listener) {
		doubleClickListeners.add(listener);
	}

	public void removeTokenSingleClick(TokenSingleClickListener listener) {
		singleClickListeners.remove(listener);
	}

	public void removeTokenDoubleClick(TokenDoubleClickListener listener) {
		doubleClickListeners.remove(listener);
	}

	protected void fireSingleClickLayoutListener() {
		for (TokenSingleClickListener listener : singleClickListeners) {
			listener.singleClick(this);
		}
	}

	protected void fireDoubleClickLayoutListener() {
		for (TokenDoubleClickListener listener : doubleClickListeners) {
			listener.doubleClick(this);
		}
	}

	public Token getToken() {
		return token;
	}

	public void refresh() {
		if (token instanceof TokenComparationValue && ((TokenComparationValue) token).getDatePeriodUnit() != null) {
			String unitLocalization = DatePeriodUnitUi.get(((TokenComparationValue) token).getDatePeriodUnit())
					.getRepresentation();

			label.setValue(((TokenComparationValue) token).getLocalizedString(unitLocalization));
		} else {
			label.setValue(token.getExpressionEditorRepresentation());
		}
	}

	public LineDisplay getLine() {
		if (getParent() != null && getParent().getParent() != null) {
			return (LineDisplay) (getParent().getParent());
		}
		return null;
	}
}
