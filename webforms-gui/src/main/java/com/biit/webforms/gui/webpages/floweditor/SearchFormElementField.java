package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.OpenSearchComponentListener;
import com.biit.webforms.gui.common.components.SearchButtonField;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.components.WindowTreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Container.Filter;

public class SearchFormElementField extends SearchButtonField {
	private static final long serialVersionUID = -2774946945994290636L;

	private Class<?>[] filterClasses;
	private Class<?>[] selectFilter;

	private List<Filter> filters;

	public interface SearchFormElementChanged {
		public void currentElement(Object object);
	}

	public SearchFormElementField(final Class<?>... filterClases) {
		super();
		this.filterClasses = filterClases;
		filters = new ArrayList<>();
		addOpenSearchComponentListener(new OpenSearchComponentListener() {

			@Override
			public void openSearchComponent(Object value) {
				openSearchFormElementWindow((TreeObject) value);
			}
		});
	}

	/**
	 * This function configures the select treeObject window to enable accept button only when the selected element is
	 * from any class contained in the filter.
	 * 
	 * @param selectfilter
	 */
	public void setSelectableFilter(Class<?>... selectfilter) {
		this.selectFilter = selectfilter;
	}

	protected void openSearchFormElementWindow(TreeObject currentValue) {
		final WindowTreeObject windowTreeObject = new WindowTreeObject(
				LanguageCodes.CAPTION_WINDOW_SELECT_FORM_ELEMENT, UserSessionHandler.getController().getCompleteFormView(),
				filterClasses);
		windowTreeObject.setSelectableFilers(selectFilter);
		for (Filter filter : filters) {
			windowTreeObject.addFilter(filter);
		}
		windowTreeObject.setValue(currentValue);

		windowTreeObject.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				TreeObject reference = windowTreeObject.getSelectedTreeObject();
				if (reference != null) {
					setValue(reference, reference.getPathName());
					windowTreeObject.close();
				}
			}
		});
		windowTreeObject.showCentered();
	}

	public TreeObject getTreeObject() {
		return (TreeObject) getValue();
	}

	public void setTreeObject(TreeObject treeObject) {
		if (treeObject != null) {
			setValue(treeObject, treeObject.getPathName());
		} else {
			clear();
		}
	}

	public void addFilter(Filter filter) {
		filters.add(filter);
	}

	public void removeFilter(Filter filter) {
		filters.remove(filter);
	}
}
