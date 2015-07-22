package com.biit.webforms.utils.math.domain.range;

/**
 * Text elements that can be compared and can be ranged between two elements.
 * The buffer for the size of text if fixed with BUFFER_MAX_SIZE
 *
 */
public class RangedText implements Comparable<RangedText> {

	private static final int BUFFER_MAX_SIZE = 4;

	// The buffer has one extra bit to signal infinite
	private char[] buffer = new char[BUFFER_MAX_SIZE + 1];

	public RangedText() {
		for (int i = 0; i < BUFFER_MAX_SIZE; i++) {
			buffer[i] = ' ';
		}
	}

	public RangedText(String text) {
		for (int i = 0; i <= BUFFER_MAX_SIZE; i++) {
			buffer[i] = ' ';
		}
		// Put the text
		for (int i = 0; i < text.length(); i++) {
			if (i >= BUFFER_MAX_SIZE) {
				// Cut large text at left.
				break;
			}
			buffer[BUFFER_MAX_SIZE - i] = text.charAt(text.length() - 1 - i);
		}
	}

	public static RangedText infiniteRangedText() {
		RangedText empty = new RangedText();
		empty.buffer[0] = 'A';
		return empty;
	}

	@Override
	public int compareTo(RangedText o) {
		return (new String(buffer)).compareTo(new String(o.buffer));
	}

	@Override
	public String toString() {
		return "'" + new String(buffer) + "'";
	}
}
