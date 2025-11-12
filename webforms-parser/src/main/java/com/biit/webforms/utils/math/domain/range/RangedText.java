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
		if (text != null) {
			// Put the text
			for (int i = 0; i < text.length(); i++) {
				if (i >= BUFFER_MAX_SIZE) {
					// Cut large text at left.
					break;
				}
				buffer[BUFFER_MAX_SIZE - i] = text.charAt(text.length() - 1 - i);
			}
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
