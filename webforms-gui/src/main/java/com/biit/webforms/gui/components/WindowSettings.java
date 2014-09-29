package com.biit.webforms.gui.components;

import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.WindowAboutUs;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowProceedAction;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WindowSettings extends Window {
	private static final long serialVersionUID = -3387583340066127391L;

	private static final String width = "300px";

	public WindowSettings() {
		setClosable(true);
		setResizable(false);
		setDraggable(false);
		setModal(true);
		center();
		setWidth(width);
		setHeight(null);
		setContent(generateContent());
		setCaption(LanguageCodes.CAPTION_SETTINGS_TITLE.translation());
	}

	public Component generateContent() {

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);

		Button aboutUsButton = new Button(LanguageCodes.CAPTION_ABOUT_US.translation(), new ClickListener() {
			private static final long serialVersionUID = -4996751752953783384L;

			@Override
			public void buttonClick(ClickEvent event) {
				(new WindowAboutUs()).showCentered();
				close();
			}
		});

		Button logoutButton = new Button(LanguageCodes.CAPTION_SETTINGS_LOG_OUT.translation(), new ClickListener() {
			private static final long serialVersionUID = -1121572145945309858L;

			@Override
			public void buttonClick(ClickEvent event) {
				new WindowProceedAction(LanguageCodes.CAPTION_PROCEED_LOG_OUT, new AcceptActionListener() {

					@Override
					public void acceptAction(WindowAcceptCancel window) {
						UserSessionHandler.getController().logOut();
						closeSettingsWindow();
					}
				});
			}
		});

		Button closeButton = new Button(LanguageCodes.CAPTION_SETTINGS_CLOSE.translation(), new ClickListener() {
			private static final long serialVersionUID = 6644941451552762983L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});

		aboutUsButton.setWidth("100%");
		logoutButton.setWidth("100%");
		closeButton.setWidth("100%");

		rootLayout.addComponent(aboutUsButton);
		rootLayout.addComponent(logoutButton);
		rootLayout.addComponent(closeButton);

		return rootLayout;
	}

	public void showRelativeToComponent(IconButton settingsButton) {
		center();
		UI.getCurrent().addWindow(this);
	}

	private void closeSettingsWindow() {
		close();
	}
}
