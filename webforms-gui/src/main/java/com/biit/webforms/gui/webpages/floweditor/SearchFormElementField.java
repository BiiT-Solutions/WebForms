package com.biit.webforms.gui.webpages.floweditor;

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

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.OpenSearchComponentListener;
import com.biit.webforms.gui.common.components.SearchButtonField;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.components.WindowTreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Container.Filter;

import java.util.ArrayList;
import java.util.List;

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
	 * This function configures the select treeObject window to enable accept
	 * button only when the selected element is from any class contained in the
	 * filter.
	 *
	 * @param selectfilter
	 */
	public void setSelectableFilter(Class<?>... selectfilter) {
		this.selectFilter = selectfilter;
	}

	protected void openSearchFormElementWindow(TreeObject currentValue) {
		final WindowTreeObject windowTreeObject = new WindowTreeObject(LanguageCodes.CAPTION_WINDOW_SELECT_FORM_ELEMENT, ApplicationUi
				.getController().getCompleteFormView(), filterClasses);
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
