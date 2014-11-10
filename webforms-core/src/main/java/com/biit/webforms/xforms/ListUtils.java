package com.biit.webforms.xforms;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biit.webforms.enumerations.TokenTypes;

public class ListUtils {

	/**
	 * Return a list of string without any duplicated.
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> removeDuplicates(List<String> list) {
		List<String> result = new ArrayList<>();
		Set<String> s = new LinkedHashSet<String>(list);
		result.addAll(s);
		return result;
	}

	/**
	 * Covert a list of strings in one string of rules separated by "or".
	 * 
	 * @param list
	 * @return
	 */
	public static String getRuleListAsString(List<String> list) {
		return getRuleListAsString(list, TokenTypes.OR);
	}

	/**
	 * Covert a list of strings in one string of rules separed by an operator.
	 * 
	 * @param list
	 * @return
	 */
	public static String getRuleListAsString(List<String> list, TokenTypes operator) {
		String result = "";
		for (String element : list) {
			if (element.length() > 0) {
				// Exist a previous rule
				if (result.length() > 0) {
					result += " " + operator.toString().toLowerCase() + " ";
				}
				result += element;
			}
		}
		return result;
	}
}
