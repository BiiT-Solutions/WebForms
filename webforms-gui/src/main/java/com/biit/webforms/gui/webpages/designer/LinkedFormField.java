package com.biit.webforms.gui.webpages.designer;

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.OpenSearchComponentListener;
import com.biit.webforms.gui.common.components.SearchButtonField;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.webpages.formmanager.WindowSimpleViewAbcdForms;

@Deprecated
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
		final WindowSimpleViewAbcdForms abcdForms = new WindowSimpleViewAbcdForms(UserSessionHandler.getController()
				.getTreeTableSimpleAbcdFormsProvider());
		abcdForms.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				SimpleFormView abcdForm = abcdForms.getForm();
				validateCompatibility(abcdForm);
				window.close();
			}
		});
		
		abcdForms.setValue(getValue());
		abcdForms.showCentered();
	}

	protected void validateCompatibility(SimpleFormView abcdForm) {
		// TODO check compatibility with current form
		UserSessionHandler.getController().validateCompatibility(UserSessionHandler.getController().getFormInUse(),
				abcdForm);

		setValue(abcdForm);
	}

	public void setValue(SimpleFormView linkedSimpleAbcdForm) {
		if (linkedSimpleAbcdForm == null) {
			clear();
		} else {
			setValue(linkedSimpleAbcdForm, linkedSimpleAbcdForm.getName() + " " + linkedSimpleAbcdForm.getVersion());
		}
	}

	@Override
	public SimpleFormView getValue() {
		return (SimpleFormView) super.getValue();
	}
}
