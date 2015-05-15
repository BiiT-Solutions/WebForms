package com.biit.webforms.validators;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.validators.reports.FormElementWithoutFlowIn;

public class AllBaseQuestionHaveFlowInValidator extends SimpleValidator<Form> {

	public AllBaseQuestionHaveFlowInValidator() {
		super(Form.class);
	}

	@Override
	protected void validateImplementation(Form form) {
		ComputedFlowView flows = form.getComputedFlowsView();

		LinkedHashSet<TreeObject> baseQuestions = form.getAllChildrenInHierarchy(BaseQuestion.class);

		// Ignore hidden questions.
		if (form instanceof CompleteFormView) {
			Set<BlockReference> blockReferences = ((CompleteFormView) form).getAllBlockReferences();
			for (BlockReference blockReference : blockReferences) {
				baseQuestions.removeAll(blockReference.getAllElementsToHide());
			}
		}

		if (!baseQuestions.isEmpty()) {
			Iterator<TreeObject> itr = baseQuestions.iterator();
			itr.next();
			while (itr.hasNext()) {
				TreeObject element = itr.next();

				Set<Flow> flowToElement = flows.getFlowsByDestiny(element);
				assertTrue(flowToElement != null && !flowToElement.isEmpty(), new FormElementWithoutFlowIn(element));
			}
		}
	}

}