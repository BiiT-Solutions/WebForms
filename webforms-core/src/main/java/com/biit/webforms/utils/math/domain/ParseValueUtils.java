package com.biit.webforms.utils.math.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.logger.WebformsLogger;

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
