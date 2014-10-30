package com.biit.webforms.utils.math.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.biit.form.BaseQuestion;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.logger.WebformsLogger;
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

	private HashMap<BaseQuestion, Object> questionDominions;  
	
	public FlowDomain(Flow flow) throws BadFormedExpressions {
		WebformsParser parser = new WebformsParser(flow.getCondition().iterator());
		try {
			Expression expression = parser.parseExpression();
			initializeDomain(expression.flatten());
		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
				| MissingParenthesisException | EmptyParenthesisException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			throw new BadFormedExpressions();
		}
	}

	private void initializeDomain(Expression expression) {
		System.out.println("Expression: " + expression);
		List<Token> tokenQuestionAnswer = expression.getAllTokens(TokenComparationAnswer.class);
		List<Token> tokenQuestionValue = expression.getAllTokens(TokenComparationValue.class);
		System.out.println("Answer: " + tokenQuestionAnswer);
		//TODO do the token question values
		System.out.println("Values: " + tokenQuestionValue);
		
		
	}
	
	
	

}
