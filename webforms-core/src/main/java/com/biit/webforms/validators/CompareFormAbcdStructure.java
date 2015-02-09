package com.biit.webforms.validators;

import com.biit.form.BaseCategory;
import com.biit.form.BaseForm;
import com.biit.form.BaseGroup;
import com.biit.form.BaseQuestion;
import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.LinkedFormAbcdAnswerNotFound;
import com.biit.webforms.validators.reports.LinkedFormAbcdElementIsBaseGroupNotBaseQuestion;
import com.biit.webforms.validators.reports.LinkedFormAbcdElementIsBaseQuestionNotBaseGroup;
import com.biit.webforms.validators.reports.LinkedFormAbcdElementNotFound;
import com.biit.webforms.validators.reports.LinkedFormAbcdGroupRepeatableStatusIsDifferent;

/**
 * Compares the differences between webforms form and abcd. Return a list of elements that exists in webforms and does
 * not exists in abcd.
 */
public class CompareFormAbcdStructure extends SimpleValidator<com.biit.abcd.persistence.entity.Form> {

	private final Form form;

	public CompareFormAbcdStructure(Form form) {
		super(com.biit.abcd.persistence.entity.Form.class);
		this.form = form;
	}

	@Override
	protected void validateImplementation(com.biit.abcd.persistence.entity.Form abcdForm) {
		validateStructure(abcdForm, form);
	}

	private void validateStructure(BaseForm abcdForm, BaseForm webformsForm) {
		for (TreeObject webformsChild : webformsForm.getChildren()) {
			TreeObject abcdChild = abcdForm.getChild(webformsChild.getName());
			if (abcdChild == null) {
				assertTrue(false, new LinkedFormAbcdElementNotFound(form, webformsForm, webformsChild));
				continue;
			}
			validateStructure(webformsForm, (BaseCategory) abcdChild, (BaseCategory) webformsChild);
		}
	}

	private void validateStructure(BaseForm webformsForm, BaseGroup abcdElement, BaseGroup webformsElement) {
		if (webformsElement instanceof BaseRepeatableGroup && abcdElement instanceof BaseRepeatableGroup) {
			if (((BaseRepeatableGroup) webformsElement).isRepeatable() != ((BaseRepeatableGroup) abcdElement)
					.isRepeatable()) {
				assertTrue(false, new LinkedFormAbcdGroupRepeatableStatusIsDifferent(form, webformsForm,
						webformsElement));
			}
		}

		for (TreeObject webformsChild : webformsElement.getChildren()) {
			TreeObject abcdChild = abcdElement.getChild(webformsChild.getName());
			if (abcdChild == null) {
				assertTrue(false, new LinkedFormAbcdElementNotFound(form, webformsForm, webformsChild));
				continue;
			}
			if (webformsChild instanceof BaseQuestion && abcdChild instanceof BaseGroup) {
				assertTrue(false,
						new LinkedFormAbcdElementIsBaseQuestionNotBaseGroup(form, webformsForm, webformsChild));
				continue;
			}
			if (webformsChild instanceof BaseGroup && abcdChild instanceof BaseQuestion) {
				assertTrue(false,
						new LinkedFormAbcdElementIsBaseGroupNotBaseQuestion(form, webformsForm, webformsChild));
				continue;
			}

			if (webformsChild instanceof BaseQuestion) {
				validateStructure(webformsForm, (BaseQuestion) abcdChild, (BaseQuestion) webformsChild);
			} else {
				validateStructure(webformsForm, (BaseGroup) abcdChild, (BaseGroup) webformsChild);
			}
		}
	}

	private void validateStructure(BaseForm webformsForm, BaseQuestion abcdQuestion, BaseQuestion webformsQuestion) {
		assertTrue(
				checkCompatibility((Question) webformsQuestion,
						(com.biit.abcd.persistence.entity.Question) abcdQuestion), new QuestionCompatibilityError(
						(com.biit.abcd.persistence.entity.Question) abcdQuestion, webformsForm,
						(Question) webformsQuestion));

		for (TreeObject webformsChild : webformsQuestion.getChildren()) {
			TreeObject abcdChild = abcdQuestion.getChild(webformsChild.getName());
			if (abcdChild == null) {
				assertTrue(false, new LinkedFormAbcdAnswerNotFound(form, webformsForm, webformsChild));
				continue;
			}
		}
	}

	private boolean checkCompatibility(Question webformsQuestion, com.biit.abcd.persistence.entity.Question abcdQuestion) {
		switch (webformsQuestion.getAnswerType()) {
		case INPUT:
			return checkCompatibilityAnswerFormat(webformsQuestion, abcdQuestion);
		case SINGLE_SELECTION_LIST:
		case SINGLE_SELECTION_RADIO:
			if (abcdQuestion.getAnswerType()!=null && abcdQuestion.getAnswerType().equals(com.biit.abcd.persistence.entity.AnswerType.RADIO)) {
				return true;
			}
			return false;
		case MULTIPLE_SELECTION:
			if (abcdQuestion.getAnswerType()!=null && abcdQuestion.getAnswerType().equals(com.biit.abcd.persistence.entity.AnswerType.MULTI_CHECKBOX)) {
				return true;
			}
			return false;
		case TEXT_AREA:
			return false;
		}
		return false;
	}

	private boolean checkCompatibilityAnswerFormat(Question question,
			com.biit.abcd.persistence.entity.Question abcdQuestion) {
		switch (question.getAnswerFormat()) {
		case DATE:
			if (abcdQuestion.getAnswerFormat() != null && abcdQuestion.getAnswerFormat().equals(com.biit.abcd.persistence.entity.AnswerFormat.DATE)) {
				return true;
			}
			return false;
		case NUMBER:
			if (abcdQuestion.getAnswerFormat() != null && abcdQuestion.getAnswerFormat().equals(com.biit.abcd.persistence.entity.AnswerFormat.NUMBER)) {
				return true;
			}
			return false;
		case TEXT:
			if (abcdQuestion.getAnswerFormat() != null && abcdQuestion.getAnswerFormat().equals(com.biit.abcd.persistence.entity.AnswerFormat.TEXT)) {
				return true;
			}
			return false;
		case POSTAL_CODE:
			if (abcdQuestion.getAnswerFormat() != null
					&& abcdQuestion.getAnswerFormat().equals(com.biit.abcd.persistence.entity.AnswerFormat.POSTAL_CODE)) {
				return true;
			}
			return false;
		}
		return false;
	}

}
