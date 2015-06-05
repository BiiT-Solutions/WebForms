package com.biit.webforms.gui.webpages.validation;

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.webpages.formmanager.WindowLinkAbcdForm;
import com.biit.webforms.persistence.entity.Form;

public class WindowCompareAbcdForm extends WindowLinkAbcdForm {
	private static final long serialVersionUID = 1088922096291591229L;
	private Form form;

	public WindowCompareAbcdForm(Form form) {
		this.form = form;
	}

	@Override
	protected void updateVersionList() {
		getVersionList().removeAllItems();
		getVersionList().setValue(null);
		if (getAbcdFormsTable().getValue() != null) {
			for (SimpleFormView simpleFormView : UserSessionHandler.getController().getLinkedSimpleFormViewsFromAbcd(form)) {
				addToVersionList(simpleFormView);
			}
		}
	}
}
