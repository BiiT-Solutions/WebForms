package com.biit.webforms.gui.webpages.floweditor;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.SearchButtonField;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.WindowTreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Question;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SearchFormElementField extends SearchButtonField {
	private static final long serialVersionUID = -2774946945994290636L;

	public SearchFormElementField() {
		super();
		addSearchButtonListener(new ClickListener() {
			private static final long serialVersionUID = 7567983186702232872L;

			@Override
			public void buttonClick(ClickEvent event) {
				openSearchFormElementWindow();
			}
		});
	}

	protected void openSearchFormElementWindow() {
		final WindowTreeObject windowTreeObject = new WindowTreeObject(
				LanguageCodes.CAPTION_WINDOW_SELECT_FORM_ELEMENT, UserSessionHandler.getController().getFormInUse());

		windowTreeObject.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (windowTreeObject.getSelectedTreeObject() instanceof Question) {
					TreeObject reference = windowTreeObject.getSelectedTreeObject();
					setValue(reference,reference.getName());
					windowTreeObject.close();
				} else {
					MessageManager.showInfo(LanguageCodes.WARNING_DESCRIPTION_CAN_ONLY_SELECT_QUESTIONS);
				}
			}
		});
		windowTreeObject.showCentered();
	}

	public TreeObject getTreeObject() {
		return (TreeObject) getValue();
	}
	
	public void setTreeObject(TreeObject treeObject){
		if(treeObject!=null){
			setValue(treeObject, treeObject.getName());
		}else{
			clear();
		}
	}
}
