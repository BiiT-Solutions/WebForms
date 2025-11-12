package com.biit.webforms.utils.math.domain.exceptions;

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

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.persistence.entity.Flow;

public class BadFormedExpressions extends Exception {
	private static final long serialVersionUID = -6351956600675780494L;

	private final List<Flow> badFormedExpressions;
	
	public BadFormedExpressions(Flow badFormedExpression) {
		badFormedExpressions = new ArrayList<>();
		badFormedExpressions.add(badFormedExpression);
	}
	
	public BadFormedExpressions(List<Flow> badFormedExpressions) {
		this.badFormedExpressions = badFormedExpressions;
	}
	
	public List<Flow> getBadFormedExpression(){
		return badFormedExpressions;
	}
}
