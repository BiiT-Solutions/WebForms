package com.biit.webforms.gui;

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
