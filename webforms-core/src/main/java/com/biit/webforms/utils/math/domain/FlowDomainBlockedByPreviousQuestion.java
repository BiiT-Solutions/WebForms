package com.biit.webforms.utils.math.domain;

import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.condition.parser.expressions.WebformsExpression;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.ITokenQuestion;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.utils.math.domain.exceptions.FlowDomainBlocked;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.ExpressionNotWellFormedException;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;

/**
 * Class to check if the domain of a question can be blocked by a previous
 * question.
 * 
 * A domain can be blocked when the flow condition minterms only reference
 * questions that can't be accessed from current flow or won't be accessed in
 * all the possible flows that arrive to current question
 * 
 *
 */
public class FlowDomainBlockedByPreviousQuestion {

	public FlowDomainBlockedByPreviousQuestion(Form form, BaseQuestion element) throws FlowDomainBlocked {
		Set<Flow> flowsOfElement = form.getFlowsFrom(element);
		if(flowsOfElement.isEmpty()){
			//Automatic flow. This can't block the user ever.
			return;
		}
		
		//This element has more than one exit flow.
		boolean isBlocked = false;
		for(Flow flow: flowsOfElement){
			if(flow.isOthers()){
				if(flowsOfElement.size()==1){
					//One flow only (OTHERS)
					return;
				}
				continue;
			}
			//Get only the condition simple tokens.
			List<Token> condition = flow.getConditionSimpleTokens();
			if(checkAllMinTermsAreFromOriginQuestion(element, condition)){
				continue;
			}
			
			try {
				WebformsExpression  expression = (WebformsExpression) new WebformsParser(condition.iterator()).parseCompleteExpression();
				isBlocked = isBlocked ||expression.checkBlockByMinTerms(form, element);
			} catch (ParseException | ExpectedTokenNotFound
					| NoMoreTokensException | IncompleteBinaryOperatorException
					| MissingParenthesisException
					| ExpressionNotWellFormedException
					| EmptyParenthesisException e) {
				//This should never happen it should have been checked already.
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		}
		
		if(isBlocked){
			throw new FlowDomainBlocked();
		}
	}

	/**
	 * Check if all minterms (Tokens Q=A) are from
	 * @param element
	 * @param condition
	 * @return
	 */
	private boolean checkAllMinTermsAreFromOriginQuestion(BaseQuestion element,
			List<Token> condition) {
		for(Token token: condition){
			if(token instanceof ITokenQuestion){
				if(!((ITokenQuestion) token).getQuestion().equals(element)){
					return false;
				}
			}
		}
		return true;
	}
	
}
