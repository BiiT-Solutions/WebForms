package com.biit.webforms.validators.reports;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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

import com.biit.form.entity.BaseForm;
import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;

public class FormGroupRepeatableStatusIsDifferent extends Report {

	private BaseForm webform;
	private BaseForm abcdForm;
	private TreeObject abcdChild;

	public FormGroupRepeatableStatusIsDifferent(BaseForm form, BaseForm abcdForm, TreeObject abcdChild) {
		super(ReportLevel.ERROR, generateReport(form, abcdForm, abcdChild));
		this.webform = form;
		this.abcdChild = abcdChild;
		this.abcdForm = abcdForm;
	}

	private static String generateReport(BaseForm form, BaseForm abcdForm, TreeObject abcdChild) {
		StringBuilder sb = new StringBuilder();
		sb.append("Form '");
		sb.append(abcdForm.getLabel());
		sb.append("' element '");
		sb.append(abcdChild.getPathName());
		sb.append("' has repeatable flag with a different value that the compared version.");
		return sb.toString();
	}

	public BaseForm getAbcdForm() {
		return abcdForm;
	}

	public TreeObject getAbcdChild() {
		return abcdChild;
	}

	public BaseForm getWebform() {
		return webform;
	}
}
