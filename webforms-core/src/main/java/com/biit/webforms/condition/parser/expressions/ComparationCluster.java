package com.biit.webforms.condition.parser.expressions;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.flow.FormWalker;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.ITokenQuestion;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.utils.math.domain.IDomain;
import com.biit.webforms.utils.math.domain.QuestionAnswerDomain;
import com.biit.webforms.utils.math.domain.range.QuestionValueDomain;
import com.biit.webforms.utils.parser.Expression;

public class ComparationCluster extends Expression implements WebformsExpression {

	private Token token;

	public ComparationCluster(Token token) {
		this.token = token;
	}

	@Override
	public void getString(StringBuilder builder) {
		builder.append(token.toString());
	}

	@Override
	public Expression flatten() {
		// Comparation cluster can't be flattened is already a simple
		// expression.
		return new ComparationCluster(token);
	}

	@Override
	public Expression negate() {
		return new ComparationCluster(token.inverse());
	}

	@Override
	public List<Expression> getAll(Class<?> arg0) {
		List<Expression> retrieved = new ArrayList<Expression>();
		if (arg0.isInstance(this)) {
			retrieved.add(this);
		}
		return retrieved;
	}

	@Override
	public List<Token> getAllTokens(Class<? extends Token> arg0) {
		List<Token> retrieved = new ArrayList<Token>();
		if (arg0.isInstance(token)) {
			retrieved.add(token);
		}
		return retrieved;
	}

	// @Override
	// public FlowDomain getDomain() {
	// FlowDomain flowDomain = new FlowDomain();
	// flowDomain.put(token);
	// return flowDomain;
	// }

	@Override
	public IDomain getDomain() {
		if (token instanceof TokenComparationAnswer) {
			return new QuestionAnswerDomain((TokenComparationAnswer) token);
		}
		if (token instanceof TokenComparationValue) {
			return QuestionValueDomain.generateQuestionValueDomain((TokenComparationValue) token);
		}
		return null;
	}

	@Override
	public boolean checkBlockByMinTerms(Form form, BaseQuestion element) {
		if(token instanceof ITokenQuestion){
			Question question = ((ITokenQuestion) token).getQuestion();
			if (FormWalker.anyPathFromOriginDoesntPassThrough(form, null, element, question)){
				return true;
			}
		}
		return false;
	}
}
