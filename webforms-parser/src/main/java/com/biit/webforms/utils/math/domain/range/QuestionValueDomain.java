package com.biit.webforms.utils.math.domain.range;

import java.util.HashMap;

import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.utils.math.domain.DomainSet;
import com.biit.webforms.utils.math.domain.DomainSetIntersection;
import com.biit.webforms.utils.math.domain.DomainSetUnion;
import com.biit.webforms.utils.math.domain.IDomain;
import com.biit.webforms.utils.math.domain.IDomainQuestion;
import com.biit.webforms.utils.math.domain.ParseValueUtils;

/**
 * Generic domain holder for different types of question - numeric value.
 *
 * @param <T>
 */
public class QuestionValueDomain<T extends Comparable<T>> implements IDomainQuestion {

	private final WebformsBaseQuestion question;
	private final RealLimitPair<T> completeDomain;
	private final RealRange<T> value;

	private QuestionValueDomain(WebformsBaseQuestion question, RealLimitPair<T> completeDomain, RealRange<T> value) {
		this.question = question;
		this.completeDomain = completeDomain;
		this.value = value;
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
	public WebformsBaseQuestion getQuestion() {
		return question;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IDomain union(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			if (this.question.equals(((IDomainQuestion) domain).getQuestion())) {
				return new QuestionValueDomain<T>(question, completeDomain, this.value.union(((QuestionValueDomain<T>) domain).value));
			} else {
				return new DomainSetUnion(this, (IDomainQuestion) domain);
			}
		}
		if (domain instanceof DomainSet) {
			return domain.union(this);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IDomain intersect(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			if (this.question.equals(((IDomainQuestion) domain).getQuestion())) {
				return new QuestionValueDomain<T>(question, completeDomain,
						this.value.intersection(((QuestionValueDomain<T>) domain).value));
			} else {
				return new DomainSetIntersection(this, (IDomainQuestion) domain);
			}
		}
		if (domain instanceof DomainSet) {
			return domain.intersect(this);
		}
		return null;
	}

	@Override
	public IDomain inverse() {
		return new QuestionValueDomain<T>(question, completeDomain, value.inverse(completeDomain));
	}

	@Override
	public boolean isComplete() {
		return value.isComplete(completeDomain);
	}

	@Override
	public boolean isEmpty() {
		return value.isEmpty();
	}

	public static QuestionValueDomain<?> generateQuestionValueDomain(TokenComparationValue token) {
		Float floatValue = null;
		Integer intValue = null;
		Long longValue = null;

		switch (token.getSubformat()) {
		case FLOAT:
			floatValue = Float.parseFloat(token.getValue());
			return generateFloatQuestionValueDomain(token, floatValue);
		case POSITIVE_FLOAT:
			floatValue = Float.parseFloat(token.getValue());
			return generateFloatQuestionValuePositiveDomain(token, floatValue);
		case NEGATIVE_FLOAT:
			floatValue = Float.parseFloat(token.getValue());
			return generateFloatQuestionValueNegativeDomain(token, floatValue);
		case DATE:
			longValue = ParseValueUtils.parseDate(token.getValue());
			return generateLongQuestionValueDomain(token, longValue);
		case POSTAL_CODE:
			return generatePostalCodeQuestionValueDomain(token, token.getValue());
		case DATE_PERIOD:
			intValue = ParseValueUtils.parseDatePeriod(token.getDatePeriodUnit(), token.getValue());
			return generateIntQuestionValueDomain(token, intValue);
		case AMOUNT:
		case NUMBER:
			intValue = Integer.parseInt(token.getValue());
			return generateIntQuestionValueDomain(token, intValue);
		case POSITIVE_NUMBER:
			intValue = Integer.parseInt(token.getValue());
			return generateIntQuestionValuePositiveDomain(token, intValue);
		case NEGATIVE_NUMBER:
			intValue = Integer.parseInt(token.getValue());
			return generateIntQuestionValueNegativeDomain(token, intValue);
		case BSN:
		case EMAIL:
		case IBAN:
		case PHONE:
		case TEXT:
			return generateRangedTextDomain(token, token.getValue());
		default:
			return null;
		}
	}

	private static QuestionValueDomain<RangedText> generateRangedTextDomain(TokenComparationValue token, String value) {
		RealRange<RangedText> generator = new RealStringRange();
		return new QuestionValueDomain<RangedText>(token.getQuestion(), generator.domain(), generator.generateValue(token.getType(),
				new RangedText(value)));
	}

	private static QuestionValueDomain<PostalCode> generatePostalCodeQuestionValueDomain(TokenComparationValue token, String value) {
		RealRange<PostalCode> generator = new RealRangePostalCode();
		return new QuestionValueDomain<PostalCode>(token.getQuestion(), generator.domain(), generator.generateValue(token.getType(),
				new PostalCode(value)));
	}

	private static QuestionValueDomain<Float> generateFloatQuestionValueDomain(TokenComparationValue token, Float value) {
		RealRange<Float> generator = new RealRangeFloat();
		return new QuestionValueDomain<Float>(token.getQuestion(), generator.domain(), generator.generateValue(token.getType(), value));
	}

	private static QuestionValueDomain<Float> generateFloatQuestionValuePositiveDomain(TokenComparationValue token, Float value) {
		RealRange<Float> generator = new RealRangePositiveFloat();
		return new QuestionValueDomain<Float>(token.getQuestion(), generator.domain(), generator.generateValue(token.getType(), value));
	}

	private static QuestionValueDomain<Float> generateFloatQuestionValueNegativeDomain(TokenComparationValue token, Float value) {
		RealRange<Float> generator = new RealRangeNegativeFloat();
		return new QuestionValueDomain<Float>(token.getQuestion(), generator.domain(), generator.generateValue(token.getType(), value));
	}

	private static QuestionValueDomain<Long> generateLongQuestionValueDomain(TokenComparationValue token, Long value) {
		RealRange<Long> generator = new RealRangeLong();
		return new QuestionValueDomain<Long>(token.getQuestion(), generator.domain(), generator.generateValue(token.getType(), value));
	}

	private static QuestionValueDomain<Integer> generateIntQuestionValueDomain(TokenComparationValue token, Integer value) {
		RealRange<Integer> generator = new RealRangeInteger();
		return new QuestionValueDomain<Integer>(token.getQuestion(), generator.domain(), generator.generateValue(token.getType(), value));
	}

	private static QuestionValueDomain<Integer> generateIntQuestionValueNegativeDomain(TokenComparationValue token, Integer value) {
		RealRange<Integer> generator = new RealRangeNegativeInteger();
		return new QuestionValueDomain<Integer>(token.getQuestion(), generator.domain(), generator.generateValue(token.getType(), value));
	}

	private static QuestionValueDomain<Integer> generateIntQuestionValuePositiveDomain(TokenComparationValue token, Integer value) {
		RealRange<Integer> generator = new RealRangePositiveInteger();
		return new QuestionValueDomain<Integer>(token.getQuestion(), generator.domain(), generator.generateValue(token.getType(), value));
	}

	@Override
	public HashMap<WebformsBaseQuestion, String> generateRandomValue() {
		HashMap<WebformsBaseQuestion, String> randomValue = new HashMap<>();
		randomValue.put(question, value.generateRandomValue().toString());
		return randomValue;
	}
}
