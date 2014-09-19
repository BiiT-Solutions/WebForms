package com.biit.webforms.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class DateUtils {

	private static final String PATTERN = "dd-MM-yyyy HH:mm:ss";

	public static String getDateString(Timestamp time){
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
		return sdf.format(time);
	}
}
