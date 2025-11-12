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

import com.biit.webforms.utils.math.domain.Closure;

/**
 * Limits for real number ranges.
 *
 * @param <T>
 */
public class RealLimit<T extends Comparable<T>> implements Comparable<RealLimit<T>> {

	private final T limit;
	private final Closure closure;

	protected RealLimit() {
		limit = null;
		closure = null;
	}
	
	public RealLimit(T limit, Closure closure) {
		this.limit = limit;
		this.closure = closure;
	}

	public T getLimit() {
		return limit;
	}

	public Closure getClosure() {
		return closure;
	}

	public int compareTo(RealLimit<T> arg0) {
		return getLimit().compareTo(arg0.getLimit());
	}

	public boolean isSingleElement() {
		return closure == Closure.SINGLE_VALUE;
	}

	public RealLimit<T> generateCopy(){
		return new RealLimit<T>(getLimit(), getClosure());
	}
	
	@Override
	public String toString(){
		return limit+" "+closure;
	}

	public RealLimit<T> inverse(){
		return new RealLimit<T>(getLimit(), getClosure().inverse());
	}
	
}
