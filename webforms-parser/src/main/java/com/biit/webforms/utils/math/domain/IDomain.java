package com.biit.webforms.utils.math.domain;

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

import java.util.HashMap;

import com.biit.webforms.persistence.entity.WebformsBaseQuestion;

/**
 * Interface for classes that express the domain of a question. It holds the
 * basic operation needed by a domain.
 *
 */
public interface IDomain {

	/**
	 * Returns if the represented domain is complete (holds all the possible
	 * elements) or not.
	 *
	 * @return
	 */
	public boolean isComplete();

	/**
	 * Generates a new domain element with the union (OR) of the two domains.
	 *
	 * @param domain
	 * @return
	 */
	public IDomain union(IDomain domain);

	/**
	 * Generates a new domain element with the intersection (AND) of the two
	 * domains.
	 *
	 * @param domain
	 * @return
	 */
	public IDomain intersect(IDomain domain);

	/**
	 * Returns the inverse of the current represented domain.
	 *
	 * @return
	 */
	public IDomain inverse();

	/**
	 *
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * Generates a hash map with random values for each question in the domain.
	 *
	 * @return
	 */
	public HashMap<WebformsBaseQuestion, String> generateRandomValue();
}
