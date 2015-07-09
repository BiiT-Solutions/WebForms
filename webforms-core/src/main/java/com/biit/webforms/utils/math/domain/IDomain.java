package com.biit.webforms.utils.math.domain;

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
