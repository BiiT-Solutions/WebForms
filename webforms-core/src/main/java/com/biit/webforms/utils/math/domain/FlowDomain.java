package com.biit.webforms.utils.math.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.condition.parser.expressions.WebformsExpression;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;

public class FlowDomain {

	private final HashMap<BaseQuestion, QuestionAnswerDomain> dominions;

	public FlowDomain() {
		dominions = new HashMap<>();
	}

	public static FlowDomain flowDomain(Flow flow) throws BadFormedExpressions {

		try {
			WebformsParser parser = new WebformsParser(flow.getCondition().iterator());
			Expression expression = parser.parseExpression();
			if (expression == null) {
				return new FlowDomain();
			}

			return ((WebformsExpression) expression).getDomain();

		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
				| MissingParenthesisException | EmptyParenthesisException e) {
			throw new BadFormedExpressions();
		}

	}

	private void initializeDomain(Expression expression) {
		System.out.println("Expression: " + expression);
		List<Token> tokenQuestionAnswer = expression.getAllTokens(TokenComparationAnswer.class);
		List<Token> tokenQuestionValue = expression.getAllTokens(TokenComparationValue.class);
		initializeQuestionAnswerDomains(tokenQuestionAnswer);
		initializeQuestionValueDomains(tokenQuestionValue);
	}

	private void initializeQuestionAnswerDomains(List<Token> tokensQuestionAnswer) {
		// TODO Auto-generated method stub
		System.out.println("Answer: " + tokensQuestionAnswer);
		for (Token token : tokensQuestionAnswer) {
			TokenComparationAnswer tokenQuestionAnswer = (TokenComparationAnswer) token;

			QuestionAnswerDomain domain = new QuestionAnswerDomain(tokenQuestionAnswer);
			dominions.put(tokenQuestionAnswer.getQuestion(), domain);

			System.out.println("Domain: " + domain);
		}
	}

	private void initializeQuestionValueDomains(List<Token> tokenQuestionValue) {
		// TODO Auto-generated method stub
		System.out.println("Values: " + tokenQuestionValue);
	}

	public boolean isComplete() {
		for (QuestionAnswerDomain dominion : dominions.values()) {
			if (!dominion.isComplete()) {
				return false;
			}
		}
		return true;
	}

	public boolean isEmpty() {
		return dominions.isEmpty();
	}

	public void put(QuestionAnswerDomain domain) {
		dominions.put(domain.getQuestion(), domain);
	}

	public void put(Token token) {
		if (token instanceof TokenComparationAnswer) {
			put(new QuestionAnswerDomain((TokenComparationAnswer) token));
			return;
		}
		if (token instanceof TokenComparationValue) {
			new QuestionValueDomain((TokenComparationValue) token);
			// TODO
			System.out.println("Not implemented");
			return;
		}
	}

	public FlowDomain union(FlowDomain flowDomain) {
		FlowDomain union = new FlowDomain();

		Set<BaseQuestion> questions = new HashSet<>();
		questions.addAll(getAllQuestions());
		questions.addAll(flowDomain.getAllQuestions());

		for (BaseQuestion question : questions) {

			QuestionAnswerDomain domainA = dominions.get(question);
			QuestionAnswerDomain domainB = flowDomain.dominions.get(question);

			if (domainA == null) {
				domainA = new QuestionAnswerDomain(domainB.getQuestion());
			}
			if (domainB == null) {
				domainB = new QuestionAnswerDomain(domainA.getQuestion());
			}
			union.put(domainA.union(domainB));
		}
		return union;
	}

	private Set<BaseQuestion> getAllQuestions() {
		return dominions.keySet();
	}

	public FlowDomain intersection(FlowDomain flowDomain) {
		FlowDomain intersection = new FlowDomain();

		Set<BaseQuestion> questions = new HashSet<>();
		questions.addAll(getAllQuestions());
		questions.addAll(flowDomain.getAllQuestions());

		for (BaseQuestion question : questions) {

			QuestionAnswerDomain domainA = dominions.get(question);
			QuestionAnswerDomain domainB = flowDomain.dominions.get(question);

			// TODO check if this holds true.

			if (domainA != null && domainB != null) {
				QuestionAnswerDomain domainIntersection = domainA.intersect(domainB);
				if (!domainIntersection.isEmpty()) {
					intersection.put(domainIntersection);
				}
			}
		}
		return intersection;
	}

	public FlowDomain inverse() {
		FlowDomain inverse = new FlowDomain();

		for (QuestionAnswerDomain domain : dominions.values()) {
			inverse.put(domain.inverse());
		}

		return inverse;
	}

	@Override
	public String toString() {
		return "FlowDomain: " + dominions.values().toString();
	}
}
