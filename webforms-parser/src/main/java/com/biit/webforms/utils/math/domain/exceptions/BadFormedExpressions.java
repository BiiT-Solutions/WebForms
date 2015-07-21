package com.biit.webforms.utils.math.domain.exceptions;

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
