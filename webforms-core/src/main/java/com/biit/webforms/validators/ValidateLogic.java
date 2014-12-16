package com.biit.webforms.validators;

import java.util.LinkedHashSet;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.FlowUnitDomain;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDateUnitForQuestions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;
import com.biit.webforms.utils.math.domain.exceptions.RedundantLogic;
import com.biit.webforms.validators.reports.DifferentDateUnitForQuestionsReport;
import com.biit.webforms.validators.reports.IncompleteLogicReport;
import com.biit.webforms.validators.reports.NotValidCondition;
import com.biit.webforms.validators.reports.RedundantLogicReport;

/**
 * Validator for flow logic.
 * 
 * Required steps to validate flow logic.
 * 
 * -First condition must be compilable
 * 
 * -Second any condition that affects the same question must use the same unit.
 * 
 * -Logic validation by range checking
 * 
 */
public class ValidateLogic extends SimpleValidator<Form> {

	public ValidateLogic() {
		super(Form.class);
	}

	@Override
	protected void validateImplementation(Form form) {
		LinkedHashSet<TreeObject> elements = form.getAllChildrenInHierarchy(BaseQuestion.class);
		for (TreeObject element : elements) {
			try {
				new FlowUnitDomain(form, (BaseQuestion) element);
			} catch (BadFormedExpressions e) {
				assertTrue(false, new NotValidCondition(e.getBadFormedExpression()));
			} catch (IncompleteLogic e) {
				assertTrue(false, new IncompleteLogicReport(element));
			} catch (RedundantLogic e) {
				assertTrue(false, new RedundantLogicReport(element));
			} catch (DifferentDateUnitForQuestions e) {
				assertTrue(false, new DifferentDateUnitForQuestionsReport(element,e.getQuestionsAffected()));
			}
		}
	}

}
