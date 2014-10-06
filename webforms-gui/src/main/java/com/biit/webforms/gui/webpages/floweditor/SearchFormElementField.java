package com.biit.webforms.gui.webpages.floweditor;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.SearchButtonField;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.components.WindowTreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SearchFormElementField extends SearchButtonField {
	private static final long serialVersionUID = -2774946945994290636L;

	private Class<?>[] filterClasses;
	private Class<?>[] selectFilter;

	public interface SearchFormElementChanged {
		public void currentElement(Object object);
	}

	public SearchFormElementField(final Class<?>... filterClases) {
		super();
		this.filterClasses = filterClases;
		addSearchButtonListener(new ClickListener() {
			private static final long serialVersionUID = 7567983186702232872L;

			@Override
			public void buttonClick(ClickEvent event) {
				openSearchFormElementWindow();
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

	protected void openSearchFormElementWindow() {
		final WindowTreeObject windowTreeObject = new WindowTreeObject(
				LanguageCodes.CAPTION_WINDOW_SELECT_FORM_ELEMENT, UserSessionHandler.getController().getFormInUse(),
				filterClasses);
		windowTreeObject.setSelectableFilers(selectFilter);

		windowTreeObject.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				TreeObject reference = windowTreeObject.getSelectedTreeObject();
				setValue(reference, reference.getPathName());
				windowTreeObject.close();
			}
		});
		windowTreeObject.showCentered();
	}

	public TreeObject getTreeObject() {
		return (TreeObject) getValue();
	}

	public void setTreeObject(TreeObject treeObject) {
		if (treeObject != null) {
			setValue(treeObject, treeObject.getName());
		} else {
			clear();
		}
	}
}
