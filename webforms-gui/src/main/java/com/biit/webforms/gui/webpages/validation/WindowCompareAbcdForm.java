package com.biit.webforms.gui.webpages.validation;

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

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.webforms.gui.ApplicationUi;
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
			for (SimpleFormView simpleFormView : ApplicationUi.getController().getLinkedSimpleFormViewsFromAbcd(form)) {
				addToVersionList(simpleFormView);
			}
		}
	}
}
