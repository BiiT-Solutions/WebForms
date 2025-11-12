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

import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import java.util.HashSet;
import java.util.Set;

public class WindowAcceptCancel extends Window {

	private static final long serialVersionUID = 8796193085149771811L;
	private Set<AcceptActionListener> acceptListeners;
	private Set<NotAcceptedActionListener> notAcceptedListeners;
	private Set<CancelActionListener> cancelListeners;
	protected IconButton acceptButton;
	protected IconButton cancelButton;
	private Component contentComponent;

	public interface AcceptActionListener {
		public void acceptAction(WindowAcceptCancel window);
	}

	public interface NotAcceptedActionListener {
		public void notAcceptedAction(WindowAcceptCancel window);
	}

	public interface CancelActionListener {
		public void cancelAction(WindowAcceptCancel window);
	}

	public WindowAcceptCancel() {
		super();
		setId(this.getClass().getName());
		setModal(true);
		acceptListeners = new HashSet<AcceptActionListener>();
		cancelListeners = new HashSet<CancelActionListener>();
		notAcceptedListeners = new HashSet<NotAcceptedActionListener>();
	}

	public WindowAcceptCancel(Component content) {
		super("", content);
		setId(this.getClass().getName());
		acceptListeners = new HashSet<WindowAcceptCancel.AcceptActionListener>();
		cancelListeners = new HashSet<WindowAcceptCancel.CancelActionListener>();
		notAcceptedListeners = new HashSet<NotAcceptedActionListener>();
	}

	@Override
	public void setContent(Component content) {
		// NOTE Vaadin. Super initialization will call this function
		// even if no content is passed.
		this.contentComponent = content;
		generateLayout(contentComponent);
	}

	@Override
	public Component getContent() {
		return contentComponent;
	}

	protected void generateAcceptCancelButton() {
		acceptButton = new IconButton(CommonComponentsLanguageCodes.ACCEPT_CANCEL_WINDOW_CAPTION_ACCEPT,
				CommonThemeIcon.ACCEPT, CommonComponentsLanguageCodes.ACCEPT_CANCEL_WINDOW_TOOLTIP_ACCEPT,
				IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = 6785334478985006998L;

					@Override
					public void buttonClick(ClickEvent event) {
						if (acceptAction()) {
							fireAcceptActionListeners();
						} else {
							fireNotAcceptedActionListeners();
						}
					}
				});
		cancelButton = new IconButton(CommonComponentsLanguageCodes.ACCEPT_CANCEL_WINDOW_CAPTION_CANCEL,
				CommonThemeIcon.CANCEL, CommonComponentsLanguageCodes.ACCEPT_CANCEL_WINDOW_TOOLTIP_CANCEL,
				IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = -6302237054661116415L;

					@Override
					public void buttonClick(ClickEvent event) {
						fireCancelActionListeners();
						close();
					}
				});
	}

	protected void generateLayout(Component content) {
		generateAcceptCancelButton();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(null);
		buttonLayout.setSpacing(true);

		buttonLayout.addComponent(acceptButton);
		buttonLayout.addComponent(cancelButton);

		if (content != null) {
			rootLayout.addComponent(content);
			rootLayout.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
			rootLayout.setExpandRatio(content, 1.0f);
		}
		rootLayout.addComponent(buttonLayout);
		rootLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(buttonLayout, 0.0f);

		addCloseListener(new CloseListener() {
			private static final long serialVersionUID = 2148083623407046384L;

			@Override
			public void windowClose(CloseEvent e) {
				fireCancelActionListeners();
			}
		});

		setKeys(rootLayout);

		super.setContent(rootLayout);
	}

	private void setKeys(VerticalLayout rootLayout) {
		rootLayout.addShortcutListener(new ShortcutListener("Enter as Accept", ShortcutAction.KeyCode.ENTER, null) {
			private static final long serialVersionUID = -9055249857540860785L;

			@Override
			public void handleAction(Object sender, Object target) {
				fireAcceptActionListeners();
			}
		});

		rootLayout.addShortcutListener(new ShortcutListener("Esc as cancel", ShortcutAction.KeyCode.ESCAPE, null) {
			private static final long serialVersionUID = -9055249857540860785L;

			@Override
			public void handleAction(Object sender, Object target) {
				fireCancelActionListeners();
				close();
			}
		});
	}

	public void addAcceptActionListener(AcceptActionListener listener) {
		acceptListeners.add(listener);
	}

	public void addNotAcceptedActionListener(NotAcceptedActionListener listener) {
		notAcceptedListeners.add(listener);
	}

	public void removeAcceptActionListener(AcceptActionListener listener) {
		acceptListeners.remove(listener);
	}

	public void addCancelActionListener(CancelActionListener listener) {
		cancelListeners.add(listener);
	}

	public void removeAcceptActionListener(CancelActionListener listener) {
		cancelListeners.remove(listener);
	}

	private void fireAcceptActionListeners() {
		for (AcceptActionListener listener : acceptListeners) {
			listener.acceptAction(this);
		}
	}

	private void fireNotAcceptedActionListeners() {
		for (NotAcceptedActionListener listener : notAcceptedListeners) {
			listener.notAcceptedAction(this);
		}
	}

	private void fireCancelActionListeners() {
		for (CancelActionListener listener : cancelListeners) {
			listener.cancelAction(this);
		}
	}

	public void showCentered() {
		center();
		UI.getCurrent().addWindow(this);
		focus();
	}

	/**
	 * This function will be called before firing the accept listeners and can be overriden by child classes. By default
	 * we accept the accept action and fire listener. This can be changed to reject the action if the user has generated
	 * incorrect data returning false.
	 */
	protected boolean acceptAction() {
		// DO nothing
		return true;
	}

	protected IconButton getAcceptButton() {
		return acceptButton;
	}

	protected IconButton getCancelButton() {
		return cancelButton;
	}
}
