package com.biit.webforms.gui.components;

import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.common.components.UpperMenu;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenuWebforms extends UpperMenu {
	private static final long serialVersionUID = -8064834304555531002L;

	private IconButton formManagerButton;
	private IconButton blockManagerButton;
	private IconButton settingsButton;

	public UpperMenuWebforms() {
		super();

		addRightIcons();
	}

	private void addRightIcons() {
		formManagerButton = new IconButton(LanguageCodes.COMMON_CAPTION_FORM_MANAGER, ThemeIcons.PAGE_FORM_MANAGER,
				LanguageCodes.COMMON_TOOLTIP_FORM_MANAGER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 8754692681745450679L;

					@Override
					public void buttonClick(ClickEvent event) {
						ApplicationUi.navigateTo(WebMap.FORM_MANAGER);
					}
				});
		formManagerButton.setHeight("100%");
		formManagerButton.setWidth(BUTTON_WIDTH);
		
		blockManagerButton = new IconButton(LanguageCodes.COMMON_CAPTION_BUILDING_BLOCK_MANAGER, ThemeIcons.PAGE_BUILDING_BLOCK_MANAGER,
				LanguageCodes.COMMON_TOOLTIP_BUILDING_BLOCK_MANAGER, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 8754692681745450679L;

					@Override
					public void buttonClick(ClickEvent event) {
						ApplicationUi.navigateTo(WebMap.BLOCK_MANAGER);
					}
				});
		blockManagerButton.setHeight("100%");
		blockManagerButton.setWidth(BUTTON_WIDTH);
		
		settingsButton = new IconButton(LanguageCodes.COMMON_CAPTION_SETTINGS, ThemeIcons.SETTINGS,
				LanguageCodes.COMMON_TOOLTIP_SETTINGS, IconSize.BIG, new ClickListener() {
					private static final long serialVersionUID = 8754692681745450679L;

					@Override
					public void buttonClick(ClickEvent event) {
						// SettingsWindow settings = new SettingsWindow();
						// settings.showRelativeToComponent(settingsButton);
					}
				});
		settingsButton.setHeight("100%");
		settingsButton.setWidth(BUTTON_WIDTH);
		
		addRightFixedButton(formManagerButton);
		addRightFixedButton(blockManagerButton);
		addRightFixedButton(settingsButton);
	}
}

// TODO Auto-generated method stub
// Add FormManager button.
// formManagerButton = new IconButton(LanguageCodes.BOTTOM_MENU_FORM_MANAGER,
// ThemeIcon.FORM_MANAGER_PAGE,
// LanguageCodes.BOTTOM_MENU_FORM_MANAGER, IconSize.BIG, new ClickListener() {
// private static final long serialVersionUID = 4002268252434768032L;
//
// @Override
// public void buttonClick(ClickEvent event) {
// final AlertMessageWindow windowAccept = new AlertMessageWindow(
// LanguageCodes.WARNING_LOST_UNSAVED_DATA);
// windowAccept.addAcceptActionListener(new AcceptActionListener() {
// @Override
// public void acceptAction(AcceptCancelWindow window) {
// ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
// windowAccept.close();
// }
// });
//
// }
// });
// formManagerButton.setEnabled(true);
// formManagerButton.setHeight("100%");
// formManagerButton.setWidth(BUTTON_WIDTH);
//