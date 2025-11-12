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

import com.biit.webforms.gui.UserSession;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.List;

public abstract class PropertiesForClassComponent<T> extends CustomComponent {
	private static final long serialVersionUID = 4900379725073491238L;

	private Class<?> type;
	private AccordionMultiple rootAccordion;
	private List<PropertyUpdateListener> propertyUpdateListeners;
	private List<ElementAddedListener> newElementListeners;

	public PropertiesForClassComponent(Class<? extends T> type) {
		this.type = type;
		propertyUpdateListeners = new ArrayList<>();
		newElementListeners = new ArrayList<>();

		rootAccordion = new AccordionMultiple();
		rootAccordion.setWidth("100%");
		rootAccordion.setHeight(null);

		setCompositionRoot(rootAccordion);
		setWidth("100%");
		setHeight(null);

		addDetachListener(new DetachListener() {
			private static final long serialVersionUID = -1283647887712898710L;

			@Override
			public void detach(DetachEvent event) {
				focus();
				updateAndExit();
			}
		});
	}

	public void addTab(Component component, String caption, boolean toggle) {
		rootAccordion.addTab(component, caption, toggle);
		inspectComponentAndAddValueChangeListeners(component);
	}

	public void addTab(Component component, String caption, boolean toggle, int index) {
		rootAccordion.addTab(component, caption, toggle, index);
		inspectComponentAndAddValueChangeListeners(component);
	}

	private void inspectComponentAndAddValueChangeListeners(Component component) {
		if (component instanceof AbstractComponentContainer) {
			addValueChangeListenerToFieldsInContainer((AbstractComponentContainer) component);
		} else {
			if (component instanceof AbstractField<?>) {
				addValueChangeListenerToField((AbstractField<?>) component);
			}
		}
	}

	private void addValueChangeListenerToFieldsInContainer(AbstractComponentContainer container) {
        for (Component component : container) {
            if (component instanceof AbstractComponentContainer) {
                addValueChangeListenerToFieldsInContainer((AbstractComponentContainer) component);
            } else {
                if (component instanceof AbstractField<?>) {
                    addValueChangeListenerToField((AbstractField<?>) component);
                }
            }
        }
	}

	private void addValueChangeListenerToField(AbstractField<?> component) {
		component.setImmediate(true);
		component.addValueChangeListener(new FieldValueChangeListener(component));
	}

	@SuppressWarnings("unchecked")
	public void setElement(Object element) {
		if (type.isInstance(element)) {
			setElementAbstract((T) element);
		}
	}

	protected abstract void setElementAbstract(T element);

	protected abstract void updateElement();

	protected abstract void firePropertyUpdateOnExitListener();

	private void updateAndExit() {
		// Check UI is different of null due to the detach is also triggered
		// when UI.close() is called.
		if (UI.getCurrent() != null && UserSession.getUser() != null) {
			firePropertyUpdateOnExitListener();
		}
	}

	public void addPropertyUpdateListener(PropertyUpdateListener listener) {
		propertyUpdateListeners.add(listener);
	}

	public void addNewElementListener(ElementAddedListener listener) {
		newElementListeners.add(listener);
	}

	public void removePropertyUpdateListener(PropertyUpdateListener listener) {
		propertyUpdateListeners.remove(listener);
	}

	public void removeNewElementListener(ElementAddedListener listener) {
		newElementListeners.remove(listener);
	}

	protected void firePropertyUpdateListener(Object element) {
		for (PropertyUpdateListener listener : propertyUpdateListeners) {
			listener.propertyUpdate(element);
		}
	}

	protected void fireExpressionAddedListener(Object newElement) {
		for (ElementAddedListener listener : newElementListeners) {
			listener.elementAdded(newElement);
		}
	}

	public Class<?> getUnderlyingClass() {
		return type;
	}

	private class FieldValueChangeListener implements ValueChangeListener {
		private static final long serialVersionUID = -5503553212373718399L;

		private final AbstractField<?> field;
		private Object value = null;

		public FieldValueChangeListener(AbstractField<?> field) {
			this.field = field;
		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (field.isAttached() && field.isEnabled() && field.getValue() != null && !field.getValue().equals(value)) {
				updateElement();
			}
			value = field.getValue();
		}
	}
}
