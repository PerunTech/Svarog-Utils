package com.prtech.svarog.utils;

import java.sql.Date;

public class SvarogUtils {

	public static Long toLong(Object value) {
		if (value == null) {
			return null;
		}
		return value instanceof Number ? ((Number) value).longValue() : Long.valueOf(value.toString());
	}

	public static Integer toInt(Object value) {
		if (value == null) {
			return null;
		}
		return value instanceof Number ? ((Number) value).intValue() : Integer.valueOf(value.toString());
	}

	public static Date toDate(Object value) {
		if (value == null) {
			return null;
		}
		return value instanceof Date ? (Date) value : Date.valueOf(value.toString());
	}
}