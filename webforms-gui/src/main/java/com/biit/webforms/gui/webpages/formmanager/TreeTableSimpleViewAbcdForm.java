package com.biit.webforms.gui.webpages.formmanager;

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.webforms.gui.common.components.TreeTableBaseForm;
import com.biit.webforms.gui.common.components.TreeTableProvider;

public class TreeTableSimpleViewAbcdForm extends TreeTableBaseForm<SimpleFormView> {
	private static final long serialVersionUID = 2299045917723812839L;

	public TreeTableSimpleViewAbcdForm(TreeTableProvider<SimpleFormView> dataProvider) {
		super(dataProvider);
	}

}
