package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDomainQuestionOperationException;
import com.biit.webforms.utils.math.domain.exceptions.IncompatibleDomainException;

public class QuestionValueDomain implements IDomain {

	private final Question question;
	private final RealRange completeDomain;
	private final List<RealRange> ranges;

	public QuestionValueDomain(Question question, RealRange completeDomain) {
		this.question = question;
		this.completeDomain = completeDomain;
		this.ranges = new ArrayList<RealRange>();
	}

	public QuestionValueDomain(TokenComparationValue token) {
		this.question = token.getQuestion();
		this.completeDomain = new RealRange();
		this.ranges = generateValues(token);
	}

	private List<RealRange> generateValues(TokenComparationValue token) {
		List<RealRange> values = new ArrayList<>();

		switch (token.getType()) {
		case EQ:
			throw new RuntimeException("Not implemented");
		case NE:
			throw new RuntimeException("Not implemented");
		case GT:
			values.add(RealRange.gt(getValueConverted(token)));
			return values;
		case GE:
			values.add(RealRange.ge(getValueConverted(token)));
			return values;
		case LT:
			values.add(RealRange.lt(getValueConverted(token)));
			return values;
		case LE:
			values.add(RealRange.le(getValueConverted(token)));
			return values;
		default:
			// Unexpected
			throw new RuntimeException("Unexpected default action at switch");
		}
	}

	private Float getValueConverted(TokenComparationValue token) {
		switch (token.getSubformat()) {
		case FLOAT:
		case NUMBER:
			return Float.parseFloat(token.getValue());
		default:
			throw new RuntimeException("Not implemented");
		}
	}

	public boolean add(RealRange range) {
		throw new RuntimeException("Not implemented");
//		if (completeDomain.contains(range)) {
//			for (RealRange value : ranges) {
//				if (value.compareTo(range) == 0) {
//					// Intersects
//					return false;
//				}
//			}
//			ranges.add(range);
//			Collections.sort(ranges);
//			return true;
//		}
//		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Complete Domain: ");
		sb.append(completeDomain);
		sb.append(" Current value: ");
		sb.append(ranges);
		return sb.toString();
	}

	@Override
	public Question getQuestion() {
		return question;
	}

	@Override
	public IDomain union(IDomain domain) throws IncompatibleDomainException, DifferentDomainQuestionOperationException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public IDomain intersect(IDomain domain) throws DifferentDomainQuestionOperationException,
			IncompatibleDomainException {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public IDomain inverse() {
		QuestionValueDomain inverse = new QuestionValueDomain(question,completeDomain);
		
		if(ranges.isEmpty()){
			inverse.add(completeDomain);
			return inverse;
		}
		
		List<RealRange> inverseRanges = new ArrayList<RealRange>();
		RealRange lastCreatedRange = null;
		
		//Create first element		
//		List<Float> limitList 
//		
//		
//		for(RealRange range: ranges){
//			if()
//		}
		throw new RuntimeException("Not implemented");
	}
	
	@Override
	public boolean isComplete() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public boolean isEmpty() {
		throw new RuntimeException("Not implemented");
	}
}
