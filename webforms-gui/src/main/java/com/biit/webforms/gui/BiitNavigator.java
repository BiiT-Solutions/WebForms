package com.biit.webforms.gui;

import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;

public class BiitNavigator extends Navigator {
	private static final long serialVersionUID = -7473037204402054370L;

	public BiitNavigator(UI ui, ComponentContainer container) {
		super(ui, container);
	}

	public BiitNavigator(UI ui, SingleComponentContainer container) {
		super(ui, container);
	}

	public BiitNavigator(UI ui, ViewDisplay display) {
		super(ui, display);
	}

	public BiitNavigator(UI ui, NavigationStateManager stateManager, ViewDisplay display) {
		super(ui, stateManager, display);
	}

	public void setState(String navigationState) {
		getStateManager().setState(navigationState);
	}
}
