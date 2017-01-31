package com.tretinichenko.oleksii.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static Date parseStringDate(String dateString) throws ParseException{
		return new SimpleDateFormat(DATE_PATTERN).parse(dateString);
	}
	
}
