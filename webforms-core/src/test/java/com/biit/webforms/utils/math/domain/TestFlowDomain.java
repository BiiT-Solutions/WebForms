package com.biit.webforms.utils.math.domain;

import java.util.List;
import java.util.Set;

import org.testng.annotations.Test;

import com.biit.form.BaseQuestion;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;
import com.biit.webforms.utils.parser.Expression;
import com.biit.webforms.utils.parser.Parser;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;

@Test(groups = { "testFlowDomain" })
public class TestFlowDomain {
	
	public void getDomains(List<Token> condition){
		//TODO simplify this thing
		for(Token token: condition){
			if(token instanceof TokenComparationAnswer){
				
			}
			if(token instanceof TokenComparationValue){
				
			}
		}
	}
	
	public void getDomains(Set<Flow> flows){
		for(Flow flow: flows){
			WebformsParser parser = new WebformsParser(flow.getCondition().iterator());
			try {
				Expression expression = parser.parseExpression();
				System.out.println("Expression: "+expression);
			} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
					| MissingParenthesisException | EmptyParenthesisException e) {
				WebformsLogger.errorMessage(TestFlowDomain.class.getName(), e);
			}
		}
	}
	
	public boolean isLogicComplete(Form form, BaseQuestion from){
		
		Set<Flow> flowQ1 =form.getFlowsFrom(from);
		System.out.println(flowQ1);
		getDomains(flowQ1);		
		
		return true;
	}

	@Test
	public void testFlowDomain() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException, BadFlowContentException, FlowWithoutSource, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {
		System.out.println("testDiscreteDomain");

		Form form = FormTestUtilities.createFormTest1();
		System.out.println(form.getFlows());
		
		System.out.println(isLogicComplete(form, (BaseQuestion) form.getChild("cat1","qu1")));
				
	}
	
}
