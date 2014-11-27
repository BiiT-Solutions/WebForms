package com.biit.webforms.validators;

import java.util.List;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.validators.reports.QuestionNotFound;

public class ValidateFormAbcdCompatibility extends SimpleValidator<Form> {

	private final com.biit.webforms.persistence.entity.Form form;

	public ValidateFormAbcdCompatibility(com.biit.webforms.persistence.entity.Form form) {
		super(Form.class);
		this.form = form;
	}

	@Override
	protected void validateImplementation(Form abcdForm) {
		assertTrue(form.isSubset(abcdForm), new LinkedFormStructureNotCompatible(form, abcdForm));

		List<TreeObject> elements = abcdForm.getAll(BaseQuestion.class);
		for (TreeObject element : elements) {
			Question abcdQuestion = (Question) element;

			TreeObject child = form.getChild(abcdQuestion.getPath());
			assertTrue(child != null, new QuestionNotFound(form, abcdQuestion));
			if (child != null) {
				if (child instanceof com.biit.webforms.persistence.entity.Question) {
					com.biit.webforms.persistence.entity.Question question = (com.biit.webforms.persistence.entity.Question) child;
					assertTrue(checkCompatibility(question, abcdQuestion), new QuestionCompatibilityError(question,
							abcdForm, abcdQuestion));
				}else{
					assertTrue(false, new QuestionNotFound(form,abcdQuestion));
				}
			}
		}

	}

	private boolean checkCompatibility(com.biit.webforms.persistence.entity.Question question, Question abcdQuestion) {
		switch (question.getAnswerType()) {
		case INPUT:
			return checkCompatibilityAnswerFormat(question, abcdQuestion);
		case SINGLE_SELECTION_LIST:
		case SINGLE_SELECTION_RADIO:
			if (abcdQuestion.getAnswerType().equals(AnswerType.RADIO)) {
				return true;
			}
			return false;
		case MULTIPLE_SELECTION:
			if (abcdQuestion.getAnswerType().equals(AnswerType.MULTI_CHECKBOX)) {
				return true;
			}
			return false;
		case TEXT_AREA:
			return false;
		}
		return false;
	}

	private boolean checkCompatibilityAnswerFormat(com.biit.webforms.persistence.entity.Question question,
			Question abcdQuestion) {
		switch (question.getAnswerFormat()) {
		case DATE:
			if (abcdQuestion.getAnswerFormat().equals(AnswerFormat.DATE)) {
				return true;
			}
			return false;
		case NUMBER:
			if (abcdQuestion.getAnswerFormat().equals(AnswerFormat.NUMBER)) {
				return true;
			}
			return false;
		case TEXT:
			if (abcdQuestion.getAnswerFormat().equals(AnswerFormat.TEXT)) {
				return true;
			}
			return false;
		case POSTAL_CODE:
			if (abcdQuestion.getAnswerFormat().equals(AnswerFormat.POSTAL_CODE)) {
				return true;
			}
			return false;
		}
		return false;
	}

}
