package com.biit.webforms.utils.math.domain.range;

public class PostalCode implements Comparable<PostalCode> {

	public static final String MIN_VALUE_STRING = "0000AA";
	public static final String MAX_VALUE_STRING = "9999ZZ";

	public static final PostalCode MIN_VALUE = new PostalCode(MIN_VALUE_STRING);
	public static final PostalCode MAX_VALUE = new PostalCode(MAX_VALUE_STRING);

	private String postalCode;

	public PostalCode() {
		postalCode = MIN_VALUE_STRING;
	}

	public PostalCode(String postalCode) {
		this.postalCode = postalCode;
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
		System.out.println("getNextPostalCode");

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
		System.out.println(newPostalCode);
		return newPostalCode;
	}
}
