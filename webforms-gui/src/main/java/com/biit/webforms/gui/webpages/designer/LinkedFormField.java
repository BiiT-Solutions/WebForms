package com.biit.webforms.gui.webpages.designer;

import com.biit.abcd.persistence.entity.Form;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.OpenSearchComponentListener;
import com.biit.webforms.gui.common.components.SearchButtonField;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.webpages.formmanager.WindowAbcdForms;

public class LinkedFormField extends SearchButtonField {
	private static final long serialVersionUID = -5545041774905086634L;

	public LinkedFormField(String caption) {
		setCaption(caption);

		addOpenSearchComponentListener(new OpenSearchComponentListener() {

			@Override
			public void openSearchComponent(Object value) {
				openWindowAbcdForms();
			}
		});
	}

	protected void openWindowAbcdForms() {
		final WindowAbcdForms abcdForms = new WindowAbcdForms(UserSessionHandler.getController().getTreeTableAbcdFormsProvider());
		abcdForms.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				Form abcdForm = abcdForms.getForm();
				setValue(abcdForm,abcdForm.getLabel()+" "+abcdForm.getVersion());
			}
		});
		abcdForms.showCentered();
	}
}
