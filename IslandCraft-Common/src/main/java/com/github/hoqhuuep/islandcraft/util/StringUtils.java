package com.github.hoqhuuep.islandcraft.util;

public class StringUtils {
	public static String join(String[] array, String sepeator) {
		if (array.length == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(array[0]);
		for (int i = 1; i < array.length; ++i) {
			result.append(sepeator);
			result.append(array[i]);
		}
		return result.toString();
	}

	public static boolean equals(String a, String b) {
		return (a == null && b == null) || (a != null && a.equals(b));
	}
}
