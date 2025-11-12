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

import com.biit.webforms.persistence.entity.condition.Token;

public class IncompleteBinaryOperatorException extends Exception {
	private static final long serialVersionUID = -6226709058899098687L;

	private Token token;
	
	public IncompleteBinaryOperatorException(Token token) {
		super("Binary operator "+token+" at "+ token+" is incomplete");
		this.setToken(token);
	}

	public Token getToken() {
		return token;
	}

	private void setToken(Token token) {
		this.token = token;
	}
}
