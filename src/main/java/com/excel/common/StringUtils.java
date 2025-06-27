package com.excel.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class StringUtils {

	@Getter
	private static String localTime;

	public static void setLocalTime(String localTime) {
		StringUtils.localTime = localTime;
	}

	public static String localTimeToFormat(String format) {
		try {
			return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
		} catch (DateTimeParseException e) {
			e.printStackTrace();
		}
		return "";
	}
}
