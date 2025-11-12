package com.biit.webforms.gui.components.utils;

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

import com.biit.form.entity.IBaseFormView;
import com.biit.webforms.persistence.entity.Form;

import java.util.ArrayList;
import java.util.List;

public class RootForm extends Form {
	private static final long serialVersionUID = -3232630711812754521L;
	private String label;
	private List<IBaseFormView> childForms;

	public RootForm(String label, Long organizationId) {
		this.label = label;
		childForms = new ArrayList<>();
		setOrganizationId(organizationId);
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	public IBaseFormView getLastFormVersion() {
		Integer numVersion = null;
		IBaseFormView lastVersion = null;
		for (IBaseFormView form : getChildForms()) {
			if (lastVersion == null || form.getVersion() > numVersion) {
				lastVersion = form;
				numVersion = form.getVersion();
			}
		}
		return lastVersion;
	}

	public List<IBaseFormView> getChildForms() {
		return childForms;
	}

	public void addChildForm(IBaseFormView form) {
		childForms.add(form);
	}
}
