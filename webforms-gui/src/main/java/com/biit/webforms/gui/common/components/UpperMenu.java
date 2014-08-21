package com.biit.webforms.gui.common.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

public class UpperMenu extends HorizontalButtonGroup {
	private static final long serialVersionUID = 3501103183357307175L;
	public static final String BUTTON_WIDTH = "100px";
	public static final String SEPARATOR_WIDTH = "10px";

	public static final String CLASSNAME_HORIZONTAL_BUTTON_WRAPPER = "v-horizontal-button-group-wrapper";
	private static final String SEPARATOR_STYLE = "v-menu-separator";

	private HorizontalLayout upperRootLayout;
	private HorizontalLayout oldRootLayoutContainer;
	private List<IconButton> rightButtons;

	public UpperMenu() {
		super();
		defineUpperMenu();
		this.setContractIcons(true, BUTTON_WIDTH);
		rightButtons = new ArrayList<IconButton>();
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
		this.setHeight("70px");
		setStyleName("upper-menu v-horizontal-button-group");
	}
}
