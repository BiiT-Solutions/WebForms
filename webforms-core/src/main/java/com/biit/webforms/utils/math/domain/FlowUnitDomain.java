package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.condition.parser.expressions.WebformsExpression;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.flow.FormWalker;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDateUnitForQuestions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;
import com.biit.webforms.utils.math.domain.exceptions.RedundantLogic;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;

/**
 * Flow unit domain Is the domain that exists at every question It can be
 * created only if the union of flow domain restrictions is complete.
 * 
 */
public class FlowUnitDomain {

	public FlowUnitDomain(Form form, BaseQuestion from) throws BadFormedExpressions, IncompleteLogic, RedundantLogic,
			DifferentDateUnitForQuestions {
		Set<Flow> flows = form.getFlowsFrom(from);
		checkSameDateUnitForQuestion(flows);
		List<IDomain> domains = getFlowDomains(flows);
		
		checkUnicity(form, from, domains);
		if (!containFlowOthers(flows)) {
			checkCompleteness(domains);
		}
	}

	private boolean containFlowOthers(Set<Flow> flows) {
		for(Flow flow: flows){
			if(flow.isOthers()){
				return true;
			}
		}
		return false;
	}

	private void checkSameDateUnitForQuestion(Set<Flow> flows)
			throws DifferentDateUnitForQuestions {
		HashMap<WebformsBaseQuestion, AnswerSubformat> questionDateUnit = new HashMap<>();
		Set<WebformsBaseQuestion> questionsAffected = new HashSet<>();
		for (Flow flow : flows) {
			List<Token> tokens = flow.getConditionSimpleTokens();
			for (Token token : tokens) {
				if (token instanceof TokenComparationValue) {
					TokenComparationValue tokenValue = (TokenComparationValue) token;
					WebformsBaseQuestion question = tokenValue.getQuestion();
					if (question.getAnswerFormat() != null
							&& question.getAnswerFormat() == AnswerFormat.DATE) {
						if (!questionDateUnit.containsKey(question)) {
							questionDateUnit.put(question,
									((TokenComparationValue) token)
											.getSubformat());
						} else {
							if (questionDateUnit.get(question) != tokenValue
									.getSubformat()) {
								questionsAffected.add(question);
							}
						}
					}
				}
			}
		}
		if (!questionsAffected.isEmpty()) {
			throw new DifferentDateUnitForQuestions(questionsAffected);
		}
	}

	private void checkUnicity(Form form, BaseQuestion baseQuestion, List<IDomain> domains) throws RedundantLogic {
		for (int i = 0; i < domains.size(); i++) {
			for (int j = i + 1; j < domains.size(); j++) {
				IDomain intersection = domains.get(i).intersect(domains.get(j));
				if (!intersection.isEmpty()) {
					if(simpleDependencyCheck(form, baseQuestion, intersection)){
						continue;
					}
					throw new RedundantLogic();
				}
			}
		}
	}
	
	private static boolean simpleDependencyCheck(Form form, BaseQuestion baseQuestion, IDomain intersection){
		if(intersection instanceof DomainSetIntersection){
			DomainSetIntersection checkedIntersection = (DomainSetIntersection) intersection;
			if(!checkedIntersection.getDomainQuestions().isEmpty() && checkedIntersection.getDomainsets().isEmpty()){
				return simpleDependencyCheckSimpleCase(form,baseQuestion, checkedIntersection);
			}			
		}
		return false;
	}

	private static boolean simpleDependencyCheckSimpleCase(Form form, BaseQuestion baseQuestion, DomainSetIntersection checkedIntersection) {
		//Get the earliest question of simple domain elements.
		BaseQuestion earliestQuestion = null;
		for(IDomainQuestion domainQuestion : checkedIntersection.getDomainQuestions()){
			if(earliestQuestion ==null || earliestQuestion.compareTo(domainQuestion.getQuestion())==1){
				earliestQuestion = domainQuestion.getQuestion();
			}
		}
		for(IDomainQuestion domainQuestion : checkedIntersection.getDomainQuestions()){
			if(domainQuestion.getQuestion().equals(earliestQuestion)){
				continue;
			}
			if(FormWalker.allPathsFromOriginToDestinyPassThrough(form, earliestQuestion, baseQuestion, domainQuestion.getQuestion())){
				return false;
			}			
		}
		return true;
	}

	private void checkCompleteness(List<IDomain> flowDomains)
			throws IncompleteLogic {
		IDomain unionflowDomain = null;

		if (flowDomains.isEmpty()) {
			// There are no domains which means this is complete (automatic
			// generated rule)
			return;
		}

		for (IDomain flowDomain : flowDomains) {
			if (unionflowDomain == null) {
				unionflowDomain = flowDomain;
			} else {
				unionflowDomain = unionflowDomain.union(flowDomain);
			}

		}
		if (!unionflowDomain.isComplete()) {
			throw new IncompleteLogic();
		}
	}

	private List<IDomain> getFlowDomains(Set<Flow> flows)
			throws BadFormedExpressions {

		List<IDomain> domains = new ArrayList<>();

		List<Flow> badFormedExpressions = new ArrayList<Flow>();
		for (Flow flow : flows) {
			try {
				IDomain flowDomain = getDomain(flow);
				if (flowDomain != null) {
					domains.add(flowDomain);
				}
			} catch (BadFormedExpressions e) {
				badFormedExpressions.add(flow);
			}
		}

		if (!badFormedExpressions.isEmpty()) {
			throw new BadFormedExpressions(badFormedExpressions);
		}

		return domains;
	}

	private IDomain getDomain(Flow flow) throws BadFormedExpressions {
		WebformsParser parser = new WebformsParser(flow
				.getConditionSimpleTokens().iterator());
		try {
			WebformsExpression expression = ((WebformsExpression) parser
					.parseExpression());

			if (expression != null) {
				return expression.getDomain();
			}
			return null;
		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException
				| IncompleteBinaryOperatorException
				| MissingParenthesisException | EmptyParenthesisException e) {
			throw new BadFormedExpressions(flow);
		}
	}

}
