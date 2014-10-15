package com.biit.webforms.gui.webpages.formmanager;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.webforms.gui.common.components.TreeTableBaseForm;

public class TreeTableAbcdForm extends TreeTableBaseForm<Form> {
	private static final long serialVersionUID = 2299045917723812839L;

	public TreeTableAbcdForm(IFormDao formDao) {
		super(formDao);
	}

}
