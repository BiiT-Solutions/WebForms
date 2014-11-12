package com.biit.webforms.gui.components;

import java.io.IOException;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.WindowAboutUs;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowProceedAction;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.vaadin.server.VaadinServlet;
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

	private IFormDao formDao;

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

		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
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
		aboutUsButton.setWidth("100%");
		rootLayout.addComponent(aboutUsButton);

		// Clear cache for admin users.
		try {
			if (WebformsAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
					WebformsActivity.EVICT_CACHE)) {
				Button clearCacheButton = new Button(
						ServerTranslate.translate(LanguageCodes.CAPTION_SETTINGS_CLEAR_CACHE), new ClickListener() {
							private static final long serialVersionUID = -1121572145945309858L;

							@Override
							public void buttonClick(ClickEvent event) {
								new WindowProceedAction(LanguageCodes.WARNING_CLEAR_CACHE, new AcceptActionListener() {
									@Override
									public void acceptAction(WindowAcceptCancel window) {
										formDao.evictAllCache();
										ApplicationUi.navigateTo(WebMap.FORM_MANAGER);
										WebformsLogger.info(this.getClass().getName(), "User '"
												+ UserSessionHandler.getUser().getEmailAddress()
												+ "' has cleared all the 2nd level cache.");
										MessageManager.showInfo(LanguageCodes.INFO_CACHE_CLEARED);
										closeSettingsWindow();
									}
								});
							}
						});
				clearCacheButton.setWidth("100%");
				rootLayout.addComponent(clearCacheButton);
			}
		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

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
		logoutButton.setWidth("100%");
		rootLayout.addComponent(logoutButton);

		Button closeButton = new Button(LanguageCodes.CAPTION_SETTINGS_CLOSE.translation(), new ClickListener() {
			private static final long serialVersionUID = 6644941451552762983L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		closeButton.setWidth("100%");
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
