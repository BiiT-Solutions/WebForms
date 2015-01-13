package com.biit.webforms.gui.components;

import java.io.IOException;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.common.components.UpperMenu;
import com.biit.webforms.gui.common.components.WindowAboutUs;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowProceedAction;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuWebforms extends UpperMenu {
	private static final long serialVersionUID = -8064834304555531002L;

	private IconButton formManagerButton;
	private IconButton blockManagerButton;
	private IconButton settingsButton;
	private boolean confirmationNeeded;
	private IconButton logoutButton;

	public UpperMenuWebforms() {
		super();
		confirmationNeeded = false;
		addRightIcons();
	}

	private void addRightIcons() {
		formManagerButton = new IconButton(LanguageCodes.COMMON_CAPTION_FORM_MANAGER, ThemeIcons.PAGE_FORM_MANAGER,
				LanguageCodes.COMMON_TOOLTIP_FORM_MANAGER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 8754692681745450679L;

					@Override
					public void buttonClick(ClickEvent event) {
						if (confirmationNeeded && UserSessionHandler.getController().existsUnsavedFormChanges()) {
							new WindowProceedAction(LanguageCodes.CAPTION_PROCEED_LOSE_DATA,
									new AcceptActionListener() {

										@Override
										public void acceptAction(WindowAcceptCancel window) {
											ApplicationUi.navigateTo(WebMap.FORM_MANAGER);
										}
									});
						} else {
							ApplicationUi.navigateTo(WebMap.FORM_MANAGER);
						}
					}
				});
		formManagerButton.setHeight("100%");
		formManagerButton.setWidth(BUTTON_WIDTH);

		blockManagerButton = new IconButton(LanguageCodes.COMMON_CAPTION_BUILDING_BLOCK_MANAGER,
				ThemeIcons.PAGE_BUILDING_BLOCK_MANAGER, LanguageCodes.COMMON_TOOLTIP_BUILDING_BLOCK_MANAGER,
				IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 8754692681745450679L;

					@Override
					public void buttonClick(ClickEvent event) {
						if (confirmationNeeded && UserSessionHandler.getController().existsUnsavedFormChanges()) {
							new WindowProceedAction(LanguageCodes.CAPTION_PROCEED_LOSE_DATA,
									new AcceptActionListener() {

										@Override
										public void acceptAction(WindowAcceptCancel window) {
											ApplicationUi.navigateTo(WebMap.BLOCK_MANAGER);
										}
									});
						} else {
							ApplicationUi.navigateTo(WebMap.BLOCK_MANAGER);
						}
					}
				});
		blockManagerButton.setHeight("100%");
		blockManagerButton.setWidth(BUTTON_WIDTH);

		// Settings menu.
		IconButton aboutUsButton = new IconButton(LanguageCodes.CAPTION_ABOUT_US, ThemeIcons.ABOUT_US,
				LanguageCodes.TOOLTIP_ABOUT_US, new ClickListener() {
					private static final long serialVersionUID = -4996751752953783384L;

					@Override
					public void buttonClick(ClickEvent event) {
						(new WindowAboutUs()).showCentered();
					}
				});

		IconButton clearCacheButton = new IconButton(LanguageCodes.CAPTION_SETTINGS_CLEAR_CACHE,
				ThemeIcons.CLEAR_CACHE, LanguageCodes.TOOLTIP_SETTINGS_CLEAR_CACHE, new ClickListener() {
					private static final long serialVersionUID = -1121572145945309858L;

					@Override
					public void buttonClick(ClickEvent event) {
						new WindowProceedAction(LanguageCodes.WARNING_CLEAR_CACHE, new AcceptActionListener() {
							@Override
							public void acceptAction(WindowAcceptCancel window) {
								UserSessionHandler.getController().evictAllCache();
								ApplicationUi.navigateTo(WebMap.FORM_MANAGER);
								WebformsLogger.info(this.getClass().getName(), "User '"
										+ UserSessionHandler.getUser().getEmailAddress()
										+ "' has cleared all the 2nd level cache.");
								MessageManager.showInfo(LanguageCodes.INFO_CACHE_CLEARED);
							}
						});
					}
				});
		// Clear cache for admin users.
		try {
			if (WebformsAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
					WebformsActivity.EVICT_CACHE)) {
				clearCacheButton.setVisible(true);
				clearCacheButton.setWidth("100%");
			} else {
				clearCacheButton.setVisible(false);
			}
		} catch (IOException | AuthenticationRequired e) {
			clearCacheButton.setVisible(false);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		logoutButton = new IconButton(LanguageCodes.CAPTION_SETTINGS_LOG_OUT, ThemeIcons.LOG_OUT,
				LanguageCodes.TOOLTIP_SETTINGS_LOG_OUT, new ClickListener() {
					private static final long serialVersionUID = -1121572145945309858L;

					@Override
					public void buttonClick(ClickEvent event) {
						new WindowProceedAction(LanguageCodes.CAPTION_PROCEED_LOG_OUT, new AcceptActionListener() {

							@Override
							public void acceptAction(WindowAcceptCancel window) {
								UserSessionHandler.getController().logOut();
							}
						});
					}
				});

		settingsButton = generateSubMenu(ThemeIcons.SETTINGS, LanguageCodes.COMMON_CAPTION_SETTINGS,
				LanguageCodes.COMMON_TOOLTIP_SETTINGS, aboutUsButton, clearCacheButton, logoutButton);

		settingsButton.setHeight("100%");
		settingsButton.setWidth(BUTTON_WIDTH);

		addRightFixedButton(formManagerButton);
		addRightFixedButton(blockManagerButton);
		addRightFixedButton(settingsButton);
	}

	public boolean isConfirmationNeeded() {
		return confirmationNeeded;
	}

	public void setConfirmationNeeded(boolean confirmationNeeded) {
		this.confirmationNeeded = confirmationNeeded;
	}
	
	public void hideLogoutButton(boolean hide){
		hideButton(settingsButton, logoutButton, !hide);
	}
}