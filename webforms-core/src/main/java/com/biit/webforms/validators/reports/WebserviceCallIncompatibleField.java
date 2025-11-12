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
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;

public class WebserviceCallIncompatibleField extends Report {

	private final WebserviceCall call;
	private final WebserviceCallLink link;

	public WebserviceCallIncompatibleField(WebserviceCall call,WebserviceCallLink link) {
		super(ReportLevel.ERROR, generateMessage(call,link));
		this.call = call;
		this.link = link;
	}

	private static String generateMessage(WebserviceCall call, WebserviceCallLink link) {
		return "Selected form question for port '"+link.getWebservicePort()+"' of Webservice call '"+call.getName()+"' is incompatible.";
	}

	public WebserviceCall getCall() {
		return call;
	}

	public WebserviceCallLink getLink() {
		return link;
	}

}
