package com.biit.webforms.gui.common.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.common.theme.IThemeIcon;
import com.biit.webforms.gui.popover.Popover;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class UpperMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = 3501103183357307175L;
	public static final String BUTTON_WIDTH = "100px";
	public static final String SEPARATOR_WIDTH = "10px";

	public static final String CLASSNAME_HORIZONTAL_BUTTON_WRAPPER = "v-horizontal-button-group-wrapper";
	private static final String SEPARATOR_STYLE = "v-menu-separator";
	private static final String UPPER_MENU_HEIGHT = "70px";

	private HorizontalLayout upperRootLayout;
	private HorizontalLayout oldRootLayoutContainer;
	private List<IconButton> rightButtons;
	private HashMap<IconButton, List<IconButton>> subMenuButtons;

	public UpperMenu() {
		super();
		defineUpperMenu();
		this.setContractIcons(true, BUTTON_WIDTH);
		rightButtons = new ArrayList<IconButton>();
		subMenuButtons = new HashMap<>();
	}

	@Override
	protected void initHorizontalButtonGroup() {
		super.initHorizontalButtonGroup();

		upperRootLayout = new HorizontalLayout();
		upperRootLayout.setSizeFull();

		oldRootLayoutContainer = new HorizontalLayout();
		oldRootLayoutContainer.setSizeFull();
		oldRootLayoutContainer.setStyleName(CLASSNAME_HORIZONTAL_BUTTON_WRAPPER);

		Component currentRootLayout = getCompositionRoot();

		// First we change the composition root (vaadin component must be not in
		// the composition tree before adding it to another component)
		setCompositionRoot(upperRootLayout);
		oldRootLayoutContainer.addComponent(currentRootLayout);

		upperRootLayout.addComponent(oldRootLayoutContainer);
		upperRootLayout.setExpandRatio(oldRootLayoutContainer, 1.0f);

		setSizeFull();
	}

	private void addSeparator() {
		CssLayout separator = new CssLayout();
		separator.setHeight("100%");
		separator.setWidth(SEPARATOR_WIDTH);
		separator.setStyleName(SEPARATOR_STYLE);

		upperRootLayout.addComponent(separator);
		upperRootLayout.setExpandRatio(separator, 0.0f);
	}

	protected void addRightFixedButton(IconButton button) {
		if (rightButtons.isEmpty()) {
			addSeparator();
		}
		rightButtons.add(button);

		upperRootLayout.addComponent(button);
		upperRootLayout.setExpandRatio(button, 0.0f);
	}

	private void defineUpperMenu() {
		this.setWidth("100%");
		this.setHeight(UPPER_MENU_HEIGHT);
		setStyleName("upper-menu v-horizontal-button-group");
	}

	/**
	 * 
	 * @param icon
	 * @param caption
	 * @param tooltip
	 * @param buttons
	 * @return
	 */
	public IconButton addSubMenu(IThemeIcon icon, ILanguageCode caption, ILanguageCode tooltip, IconButton... buttons) {
		IconButton subMenu = generateSubMenu(icon, caption, tooltip, buttons);
		addIconButton(subMenu);
		return subMenu;
	}

	public IconButton generateSubMenu(IThemeIcon icon, ILanguageCode caption, ILanguageCode tooltip,
			final IconButton... buttons) {
		for (IconButton button : buttons) {
			if (button != null) {
				button.addStyleName("v-popover-upper-submenu");
			}
		}

		final IconButton subMenu = new IconButton(caption, icon, tooltip, IconSize.BIG);
		subMenu.addStyleName("opens-popover-menu");
		subMenu.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 9175409158532169878L;

			@Override
			public void buttonClick(ClickEvent event) {
				VerticalLayout rootLayout = new VerticalLayout();
				rootLayout.setSizeUndefined();

				final Popover popover = new Popover(rootLayout);
				popover.setClosable(true);

				for (final IconButton button : buttons) {
					if (button != null) {
						button.setWidth(getIconSize());
						button.setHeight(UPPER_MENU_HEIGHT);
						rootLayout.addComponent(button);
						button.addClickListener(new ClickListener() {
							private static final long serialVersionUID = -2214568128797434177L;

							@Override
							public void buttonClick(ClickEvent event) {
								// Close original popover. XForms preview must have the menu open until the view is
								// created.
								if (!button.getCaption().equals(LanguageCodes.CAPTION_PREVIEW_XFORMS.translation())) {
									popover.close();
								}
							}
						});
					}
				}
				popover.showRelativeTo(subMenu);
			}
		});
		subMenuButtons.put(subMenu, new ArrayList<IconButton>(Arrays.asList(buttons)));
		return subMenu;
	}

	public void hideButton(IconButton submenu, IconButton button, boolean visible) {
		List<IconButton> buttons = subMenuButtons.get(submenu);
		if (buttons != null) {
			for (IconButton buttonInSubmenu : buttons) {
				if (buttonInSubmenu.equals(button)) {
					buttonInSubmenu.setVisible(visible);
				}
			}
		}
	}
}
