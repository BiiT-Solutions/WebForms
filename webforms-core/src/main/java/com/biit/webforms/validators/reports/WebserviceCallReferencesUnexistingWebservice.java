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

import com.biit.utils.validation.Report;
import com.biit.utils.validation.ReportLevel;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;

public class WebserviceCallReferencesUnexistingWebservice extends Report {

	private final WebserviceCall call;

	public WebserviceCallReferencesUnexistingWebservice(WebserviceCall call) {
		super(ReportLevel.ERROR, generateMessage(call));
		this.call = call;
	}

	private static String generateMessage(WebserviceCall call) {
		return "Webservice call '"+call.getName()+"' references unexisting webservice '"+call.getWebserviceName()+"'.";
	}

	public WebserviceCall getCall() {
		return call;
	}

}
