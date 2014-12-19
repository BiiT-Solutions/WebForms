package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.condition.parser.expressions.WebformsExpression;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
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

	public FlowUnitDomain(Form form, BaseQuestion from) throws BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions {
		Set<Flow> flows = form.getFlowsFrom(from);
		System.out.println(flows);
		checkSameDateUnitForQuestion(flows);
		List<IDomain> domains = getFlowDomains(flows);
		System.out.println("Check unicity");
		checkUnicity(domains);
		System.out.println("Check completeness");
		checkCompleteness(domains);
		System.out.println("End flow");
	}

	private void checkSameDateUnitForQuestion(Set<Flow> flows) throws DifferentDateUnitForQuestions {
		HashMap<Question, AnswerSubformat> questionDateUnit = new HashMap<>();
		Set<Question> questionsAffected = new HashSet<>();
		for(Flow flow: flows){
			List<Token> tokens = flow.getConditionSimpleTokens();
			for(Token token: tokens){
				if(token instanceof TokenComparationValue){
					TokenComparationValue tokenValue = (TokenComparationValue) token;
					Question question = tokenValue.getQuestion();
					if(question.getAnswerFormat()!=null && question.getAnswerFormat()==AnswerFormat.DATE){
						if(!questionDateUnit.containsKey(question)){
							questionDateUnit.put(question, ((TokenComparationValue) token).getSubformat());
						}else{
							if(questionDateUnit.get(question)!=tokenValue.getSubformat()){
								questionsAffected.add(question);
							}
						}
					}
				}
			}
		}
		if(!questionsAffected.isEmpty()){
			throw new DifferentDateUnitForQuestions(questionsAffected);
		}
	}

	private void checkUnicity(List<IDomain> domains) throws RedundantLogic {
		for (int i = 0; i < domains.size(); i++) {
			System.out.println("Domain: "+domains.get(i));
			for (int j = i + 1; j < domains.size(); j++) {
				System.out.println("Intersect with domain: "+domains.get(j));
				IDomain intersection = domains.get(i).intersect(domains.get(j));
				
				if (!intersection.isEmpty()) {
					throw new RedundantLogic();
				}
			}
		}
	}

	private void checkCompleteness(List<IDomain> flowDomains) throws IncompleteLogic {
		IDomain unionflowDomain = null;

		if (flowDomains.isEmpty()) {
			// There are no domains which means this is complete (automatic
			// generated rule)
			return;
		}

		for (IDomain flowDomain : flowDomains) {
			System.out.println("union "+flowDomain);
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

	private List<IDomain> getFlowDomains(Set<Flow> flows) throws BadFormedExpressions {

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
		WebformsParser parser = new WebformsParser(flow.getConditionSimpleTokens().iterator());
		try {
			WebformsExpression expression = ((WebformsExpression) parser.parseExpression());
			
			if (expression != null) {
				return expression.getDomain();
			}
			return null;
		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
				| MissingParenthesisException | EmptyParenthesisException e) {
			throw new BadFormedExpressions(flow);
		}
	}

}
