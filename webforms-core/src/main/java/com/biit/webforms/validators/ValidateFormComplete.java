package com.biit.webforms.validators;

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

import java.util.Set;

import com.biit.form.validators.ValidateBaseForm;
import com.biit.utils.validation.CompositeValidator;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.webservices.Webservice;

public class ValidateFormComplete extends CompositeValidator<Form> {

	public ValidateFormComplete() {
		super(Form.class);
		configure();
	}

	public ValidateFormComplete(Set<Webservice> webservices) {
		super(Form.class);
		configure();
		setExtraData(webservices);
	}

	private void configure() {
		add(ValidateBaseForm.class);
		add(ValidateFormStructure.class);
		add(ValidateFormFlows.class);
		add(ValidateLogic.class);
		add(ValidateWebserviceCalls.class);
		setStopOnFail(true);
	}

}
