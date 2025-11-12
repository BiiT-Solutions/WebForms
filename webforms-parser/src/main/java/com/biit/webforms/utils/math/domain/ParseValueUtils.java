package com.biit.webforms.utils.math.domain;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Expression parser)
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Utilities to parse values from string data.
 *
 */
public class ParseValueUtils {

	public static Long parseDate(String value) {
		SimpleDateFormat formatter = new SimpleDateFormat();
		// Convert String to date.
		formatter.applyPattern(WebformsConfigurationReader.getInstance().getDatePattern());
		try {
			Date date = formatter.parse(value);
			return date.getTime();
		} catch (ParseException e) {
			WebformsLogger.errorMessage(ParseValueUtils.class.getName(), e);
		}
		return null;
	}

	public static Integer parseDatePeriod(DatePeriodUnit unit, String datePeriod) {
		Integer period = Integer.parseInt(datePeriod);
		switch (unit) {
		case DAY:
			return period;
		case MONTH:
			return period * 30;
		case YEAR:
			return period * 365;
		}
		return null;
	}

	public static Long parsePostalCode(String postalCode) {
		String numericalPart = postalCode.substring(0, 4);
		numericalPart += ((int) postalCode.charAt(4)) + "";
		numericalPart += ((int) postalCode.charAt(5)) + "";
		return Long.parseLong(numericalPart);
	}
}
