package com.biit.webforms.utils.math.domain.range;

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

import java.util.Random;

/**
 * Ranges definition for postal codes This code should be more generic to
 * support more definitions of postal codes.
 *
 */
public class PostalCode implements Comparable<PostalCode> {

	public static final String MIN_VALUE_STRING = "0000AA";
	public static final String MAX_VALUE_STRING = "9999ZZ";

	public static final PostalCode MIN_VALUE = new PostalCode(MIN_VALUE_STRING);
	public static final PostalCode MAX_VALUE = new PostalCode(MAX_VALUE_STRING);
	public static final Random random = new Random();

	private String postalCode;

	public PostalCode() {
		postalCode = MIN_VALUE_STRING;
	}

	public PostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public PostalCode(int intPart, char firstChar, char secondChar) {
		if (intPart > 9999) {
			postalCode = MAX_VALUE_STRING;
		} else {
			if (intPart < 0) {
				postalCode = MIN_VALUE_STRING;
			} else {
				postalCode = String.format("%04d", intPart) + ((char) firstChar) + ((char) secondChar);
			}
		}
	}

	@Override
	public int compareTo(PostalCode o) {
		return postalCode.compareTo(o.postalCode);
	}

	@Override
	public String toString() {
		return postalCode;
	}

	public PostalCode getNextPostalCode() {
		int number = Integer.parseInt(postalCode.substring(0, 4));
		int firstChar = (int) postalCode.charAt(5);
		int secondChar = (int) postalCode.charAt(5);

		secondChar++;
		if (secondChar > (int) ('Z')) {
			secondChar = (int) 'A';
			firstChar++;
			if (firstChar > (int) ('Z')) {
				firstChar = (int) 'A';
				number++;
				if (number > 9999) {
					number = 9999;
				}
			}
		}

		String nextPostalCode = String.format("%04d", number) + ((char) firstChar) + ((char) secondChar);

		PostalCode newPostalCode = new PostalCode(nextPostalCode);
		return newPostalCode;
	}

	public static PostalCode random() {
		return random(MIN_VALUE, MAX_VALUE);
	}

	public static PostalCode random(PostalCode limit, PostalCode limit2) {
		if (limit.compareTo(limit2) == 0) {
			return new PostalCode(limit.postalCode);
		}

		int limit1Int = limit.getIntPart();
		int limit2Int = limit2.getIntPart();
		int fistChar1Int = limit.getFirstChar();
		int fistChar2Int = limit2.getFirstChar();
		int secondChar1Int = limit.getSecondChar();
		int secondChar2Int = limit2.getSecondChar();

		int randomInt = (random.nextInt(Integer.MAX_VALUE) % (limit2Int - limit1Int)) + limit1Int;
		int randomFirstChar = (random.nextInt(Integer.MAX_VALUE) % (fistChar1Int - fistChar2Int)) + fistChar1Int;
		int randomSecondChar = (random.nextInt(Integer.MAX_VALUE) % (secondChar1Int - secondChar2Int)) + secondChar1Int;

		return new PostalCode(randomInt, (char) randomFirstChar, (char) randomSecondChar);
	}

	private int getSecondChar() {
		return (int) postalCode.charAt(5);
	}

	private int getFirstChar() {
		return (int) postalCode.charAt(4);
	}

	private int getIntPart() {
		return Integer.parseInt(postalCode.substring(0, 4));
	}
}
