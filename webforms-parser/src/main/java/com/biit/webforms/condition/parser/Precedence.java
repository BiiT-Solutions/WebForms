package com.biit.webforms.condition.parser;

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
 * Order of preference of the token on the PRATT parser
 *
 */
public class Precedence {
	public static final int ASSIGNMENT = 1;
	public static final int OR = 2;
	public static final int AND = 3;
	public static final int CONDITIONAL = 4;
	public static final int SUM = 5;
	public static final int PRODUCT = 6;
	public static final int EXPONENT = 7;
	public static final int PREFIX = 8;
	public static final int POSTFIX = 9;
	public static final int CALL = 10;
}
