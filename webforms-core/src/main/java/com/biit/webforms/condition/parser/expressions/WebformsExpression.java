package com.biit.webforms.condition.parser.expressions;

import com.biit.form.BaseQuestion;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.IDomain;

public interface WebformsExpression {

	// public FlowDomain getDomain();

	public IDomain getDomain();

	public boolean checkBlockByMinTerms(Form form, BaseQuestion element);

}
