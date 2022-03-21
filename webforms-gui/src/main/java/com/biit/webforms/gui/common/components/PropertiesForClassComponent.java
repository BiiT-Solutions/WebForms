package com.biit.webforms.gui.common.components;

import com.biit.webforms.gui.UserSession;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class PropertiesForClassComponent<T> extends CustomComponent {
	private static final long serialVersionUID = 4900379725073491238L;

	private Class<?> type;
	private AccordionMultiple rootAccordion;
	private List<PropertieUpdateListener> propertyUpdateListeners;
	private List<ElementAddedListener> newElementListeners;
	protected TextField createdBy, creationTime, updatedBy, updateTime;

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
		Iterator<Component> itr = container.iterator();
		while (itr.hasNext()) {
			Component component = itr.next();
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

	public void addPropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.add(listener);
	}

	public void addNewElementListener(ElementAddedListener listener) {
		newElementListeners.add(listener);
	}

	public void removePropertyUpdateListener(PropertieUpdateListener listener) {
		propertyUpdateListeners.remove(listener);
	}

	public void removeNewElementListener(ElementAddedListener listener) {
		newElementListeners.remove(listener);
	}

	protected void firePropertyUpdateListener(Object element) {
		for (PropertieUpdateListener listener : propertyUpdateListeners) {
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

		private AbstractField<?> field;
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
	};
}
