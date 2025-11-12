package com.biit.webforms.utils.parser.exceptions;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
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

import com.biit.webforms.utils.parser.ITokenType;

public class ExpectedTokenNotFound extends Exception {
	private static final long serialVersionUID = 3489582832259193623L;
	private ITokenType expected;
	private ITokenType type;

	public ExpectedTokenNotFound(ITokenType expected, ITokenType type) {
		super("Expected token " + expected + " and found " + type);
		this.expected = expected;
		this.type = type;
	}

	public ExpectedTokenNotFound(String message) {
		super(message);
	}

	public ITokenType getExpected() {
		return expected;
	}

	public void setExpected(ITokenType expected) {
		this.expected = expected;
	}

	public ITokenType getType() {
		return type;
	}

	public void setType(ITokenType type) {
		this.type = type;
	}
}
