package com.biit.webforms.utils.math.domain;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDomainQuestionOperationException;
import com.biit.webforms.utils.math.domain.exceptions.IncompatibleDomainException;

public class QuestionValueDomain implements IDomainQuestion {

	private final Question question;
	private final RealLimitPair completeDomain;
	private final RealRange value;

	public QuestionValueDomain(Question question, RealLimitPair completeDomain) {
		this.question = question;
		this.completeDomain = completeDomain;
		this.value = RealRange.empty();
	}
	
	public QuestionValueDomain(Question question, RealLimitPair completeDomain, RealRange value) {
		this.question = question;
		this.completeDomain = completeDomain;
		this.value = value;
	}

	public QuestionValueDomain(TokenComparationValue token) {
		this.question = token.getQuestion();
		this.completeDomain = generateDomain(token.getQuestion());
		this.value = generateValues(token);
	}

	private RealLimitPair generateDomain(Question question) {
		switch (question.getAnswerFormat()){
		case DATE:
		case NUMBER:
			return RealLimitPair.realRange();
		case POSTAL_CODE:
		case TEXT:
			WebformsLogger.errorMessage(this.getClass().getName(), new RuntimeException("Not implemented"));
			return RealLimitPair.realRange();
		}
		return null;
	}

	private RealRange generateValues(TokenComparationValue token) {
		switch (token.getType()) {
		case EQ:
			return RealRange.eq(getValueConverted(token));
		case NE:
			return RealRange.ne(getValueConverted(token));
		case GT:
			return RealRange.gt(getValueConverted(token));
		case GE:
			return RealRange.ge(getValueConverted(token));
		case LT:
			return RealRange.lt(getValueConverted(token));
		case LE:
			return RealRange.le(getValueConverted(token));
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Domain '");
		sb.append(completeDomain);
		sb.append("' value '");
		sb.append(value);
		sb.append("'");
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
		return new QuestionValueDomain(question,completeDomain,value.inverse(completeDomain));
	}
	
	@Override
	public boolean isComplete() {
		return value.isComplete(completeDomain);
	}

	@Override
	public boolean isEmpty() {
		return value.isEmpty();
	}
}
