package com.biit.webforms.gui.components;

import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.TableWithSearch;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.language.ILanguageCode;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.Component;

/**
 * Accept cancel window with a TableTreeObject and a search box to filter
 * content. This window also accepts a filter class at creation time to set the
 * limits when loading the hierarchy and a selection filter for the enable state
 * of accept button
 * 
 */
public class WindowTreeObject extends WindowAcceptCancel {
	private static final long serialVersionUID = -1341380408900400222L;
	private static final String width = "640px";
	private static final String height = "480px";
	private TableTreeObjectLabel formTable;
	private TableWithSearch tableWithSearch;
	private Class<?>[] selectFilters;

	public WindowTreeObject(ILanguageCode code, BaseForm form, Class<?>... loadfilter) {
		super();
		setCaption(code.translation());
		setContent(generateContent(form, loadfilter));
		setResizable(false);
		setDraggable(false);
		setClosable(true);
		setModal(true);
		setWidth(width);
		setHeight(height);
		addFocusListener(new FocusListener() {
			private static final long serialVersionUID = 8456604937045435061L;

			@Override
			public void focus(FocusEvent event) {
				tableWithSearch.focus();
			}
		});
	}

	private Component generateContent(BaseForm form, Class<?>... loadFilter) {
		formTable = new TableTreeObjectLabel();
		formTable.setSizeFull();
		formTable.setSelectable(true);
		formTable.loadTreeObject(form, null, loadFilter);
		formTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -6136068342027536453L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateAcceptButtonState();
			}
		});

		tableWithSearch = new TableWithSearch(formTable, new FilterTreeObjectTableContainsNameLabel());
		tableWithSearch.setSizeFull();

		return tableWithSearch;
	}

	private void updateAcceptButtonState() {
		if (isSelectAllowed()) {
			getAcceptButton().setEnabled(true);
		} else {
			getAcceptButton().setEnabled(false);
		}
	}

	private boolean isSelectAllowed() {
		if (selectFilters == null || selectFilters.length == 0) {
			return true;
		}
		if (formTable.getValue() == null) {
			return false;
		}
		for (Class<?> filterClass : selectFilters) {
			if (filterClass.isInstance(formTable.getValue())) {
				return true;
			}
		}
		return false;
	}

	public TreeObject getSelectedTreeObject() {
		return formTable.getSelectedRow();
	}

	/**
	 * This function sets the filter classes to determine the enable state of
	 * the accept button.
	 * 
	 * @param selectFilters
	 */
	public void setSelectableFilers(Class<?>... selectFilters) {
		this.selectFilters = selectFilters;
		updateAcceptButtonState();
	}

	public void addFilter(Filter filter) {
		Filterable container = (Filterable) formTable.getContainerDataSource();
		container.addContainerFilter(filter);
	}

	public void setValue(TreeObject value) {
		formTable.setValue(value);
	}

	@Override
	public void showCentered() {
		super.showCentered();
		focus();

	}
}
