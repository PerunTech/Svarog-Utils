package com.prtech.svarog.utils;

import java.sql.Date;

public class SvarogUtils {

	/**
	 * Converts a raw value to {@link Long} without an unnecessary String round-trip
	 * when the value already is a {@link Number}.
	 *
	 * @param value raw value from JSON, form data, or database conversion
	 * @return converted value
	 */
	public static Long toLong(Object value) {
		if (value == null) {
			return null;
		}
		return value instanceof Number ? ((Number) value).longValue() : Long.valueOf(value.toString());
	}

	/**
	 * Converts a raw value to {@link Integer} without an unnecessary String
	 * round-trip when the value already is a {@link Number}.
	 *
	 * @param value raw value from JSON, form data, or database conversion
	 * @return converted value
	 */
	public static Integer toInt(Object value) {
		if (value == null) {
			return null;
		}
		return value instanceof Number ? ((Number) value).intValue() : Integer.valueOf(value.toString());
	}

	/**
	 * Converts a raw value to {@link Date} without an unnecessary String round-trip
	 * when the value already is a {@link Date}.
	 *
	 * @param value raw value from JSON, form data, or database conversion
	 * @return converted value
	 */
	public static Date toDate(Object value) {
		if (value == null) {
			return null;
		}
		return value instanceof Date ? (Date) value : Date.valueOf(value.toString());
	}
}