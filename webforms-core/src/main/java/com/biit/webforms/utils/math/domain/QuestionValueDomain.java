package com.biit.webforms.utils.math.domain;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;

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
		switch (question.getAnswerFormat()) {
		case DATE:
		case NUMBER:
		case POSTAL_CODE:
			return RealLimitPair.realRange();
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
		case DATE:
		case DATE_PERIOD:
			throw new RuntimeException("Not implemented");
		case POSTAL_CODE:
			return ParseValueUtils.parsePostalCode(token.getValue());
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
	public IDomain union(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			if (this.question.equals(((IDomainQuestion) domain).getQuestion())) {
				new QuestionValueDomain(question, completeDomain,this.value.union(((QuestionValueDomain)domain).value));
			}else{
				return new DomainSetUnion(this, (IDomainQuestion)domain);
			}
		}
		if (domain instanceof DomainSet) {
			return domain.union(this);
		}
		return null;
	}

	@Override
	public IDomain intersect(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			if (this.question.equals(((IDomainQuestion) domain).getQuestion())) {
				new QuestionValueDomain(question, completeDomain,this.value.intersection(((QuestionValueDomain)domain).value));
			} else {
				return new DomainSetIntersection(this, (IDomainQuestion)domain);
			}
		}
		if (domain instanceof DomainSet) {
			return domain.intersect(this);
		}
		return null;
	}

	@Override
	public IDomain inverse() {
		return new QuestionValueDomain(question, completeDomain, value.inverse(completeDomain));
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
