package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;

public class QuestionValueDomain {

	private final Question question;
	private final RealRange completeDomain;
	private final List<RealRange> values;

	public QuestionValueDomain(Question question) {
		this.question = question;
		this.completeDomain = new RealRange();
		this.values = new ArrayList<RealRange>();
	}

	public QuestionValueDomain(TokenComparationValue token) {
		this.question = token.getQuestion();
		this.completeDomain = new RealRange();
		this.values = generateValues(token);
	}

	private List<RealRange> generateValues(TokenComparationValue token) {
		switch (token.getType()) {
		case EQ:
			break;
		case NE:
			break;
		case GT:
			break;
		case GE:
			break;
		case LT:
			break;
		case LE:
			break;
		default:
			//Unexpected
			return null;
		}
		return null;
	}

	public boolean add(RealRange range) {
		if (completeDomain.contains(range)) {
			for (RealRange value : values) {
				if (value.compareTo(range) == 0) {
					// Intersects
					return false;
				}
			}
			values.add(range);
			Collections.sort(values);
			return true;
		}
		return false;
	}
	
}
