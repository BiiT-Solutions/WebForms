package com.biit.webforms.gui.webpages.floweditor;

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

import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.language.LanguageCodes;

public enum DatePeriodUnitUi {

	YEAR(DatePeriodUnit.YEAR, LanguageCodes.CAPTION_YEAR, "Y"),

	MONTH(DatePeriodUnit.MONTH, LanguageCodes.CAPTION_MONTH, "M"),

	DAY(DatePeriodUnit.DAY, LanguageCodes.CAPTION_DAY, "D");

	private DatePeriodUnit datePeriodUnit;
	private LanguageCodes code;
	private String representation;

	DatePeriodUnitUi(DatePeriodUnit datePeriodUnit, LanguageCodes code, String representation) {
		this.datePeriodUnit = datePeriodUnit;
		this.code = code;
		this.representation = representation;
	}

	public DatePeriodUnit getDatePeriodUnit() {
		return datePeriodUnit;
	}

	String getTranslation() {
		return code.translation();
	}

	public String getRepresentation() {
		return representation;
	}

	public static DatePeriodUnitUi get(DatePeriodUnit datePeriodUnit) {
		for (DatePeriodUnitUi value : values()) {
			if (value.getDatePeriodUnit().equals(datePeriodUnit)) {
				return value;
			}
		}
		return null;
	}
}
