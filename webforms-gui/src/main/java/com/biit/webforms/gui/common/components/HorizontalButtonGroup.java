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

import com.biit.webforms.gui.WebformsUiLogger;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import java.util.Iterator;

public class HorizontalButtonGroup extends CustomComponent {

	private static final long serialVersionUID = 4862986305501412362L;
	private static String CLASSNAME = "v-horizontal-button-group";
	protected HorizontalLayout rootLayout;
	private String size;
	private boolean contractIcons;

	public HorizontalButtonGroup() {
		super();
		initHorizontalButtonGroup();
		setIconSizeWithAttachListener();
	}

	protected void initHorizontalButtonGroup() {
		setStyleName(CLASSNAME);

		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(false);
		setCompositionRoot(rootLayout);
		setSizeFull();

		contractIcons = false;
	}

	protected void setIconSizeWithAttachListener() {
		addAttachListener(new AttachListener() {
			private static final long serialVersionUID = -2513076537414804598L;

			@Override
			public void attach(AttachEvent event) {
				setIconSize();
			}
		});
	}

	public void addIconButton(IconButton button) {
		rootLayout.addComponent(button);
		button.setSizeFull();
	}

	public void replaceIconButton(IconButton newButton, IconButton buttonToBeReplaced) {
		int index = rootLayout.getComponentIndex(buttonToBeReplaced);
		if (index >= 0) {
			rootLayout.removeComponent(buttonToBeReplaced);
			rootLayout.addComponent(newButton, index);
			newButton.setSizeFull();
		} else {
			WebformsUiLogger.warning(this.getClass().getName(), "Trying to replace a non existing element.");
		}
	}

	public void setContractIcons(boolean contractIcons) {
		this.contractIcons = contractIcons;
		this.size = null;
		rootLayout.setWidth(null);
	}

	public void setContractIcons(boolean contractIcons, String size) {
		this.contractIcons = contractIcons;
		this.size = size;
		rootLayout.setWidth(null);
	}

	private void setIconSize() {
		Iterator<Component> itr = rootLayout.iterator();
		if (contractIcons) {
			rootLayout.setWidth(null);
		}

		while (itr.hasNext()) {
			Component component = itr.next();
			rootLayout.setExpandRatio(component, 0.0f);
			if (contractIcons) {
				component.setWidth(size);
			} else {
				component.setWidth("100%");
			}
		}

		markAsDirtyRecursive();
	}

	protected String getIconSize() {
		return size;
	}

}
