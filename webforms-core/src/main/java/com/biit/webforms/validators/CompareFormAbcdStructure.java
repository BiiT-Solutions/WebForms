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
import com.biit.webforms.validators.reports.FormAnswerNotFound;
import com.biit.webforms.validators.reports.FormElementIsBaseGroupNotBaseQuestion;
import com.biit.webforms.validators.reports.FormElementIsBaseQuestionNotBaseGroup;
import com.biit.webforms.validators.reports.FormElementNotFound;
import com.biit.webforms.validators.reports.FormGroupRepeatableStatusIsDifferent;
import com.biit.webforms.validators.reports.QuestionCompatibilityError;

/**
 * Compares the differences between webforms form and abcd. Return a list of elements that exists in webforms and does
 * not exists in abcd.
 */
public class CompareFormAbcdStructure extends SimpleValidator<com.biit.abcd.persistence.entity.Form> {

	private final Form webform;

	public CompareFormAbcdStructure(Form form) {
		super(com.biit.abcd.persistence.entity.Form.class);
		this.webform = form;
	}

	@Override
	protected void validateImplementation(com.biit.abcd.persistence.entity.Form abcdForm) {
		validateStructure(abcdForm, webform);
	}

	private void validateStructure(BaseForm abcdForm, BaseForm webformsForm) {
		for (TreeObject webformsChild : webformsForm.getChildren()) {
			TreeObject abcdChild = abcdForm.getChild(webformsChild.getName());
			if (abcdChild == null) {
				assertTrue(false, new FormElementNotFound(webform, abcdForm, webformsChild));
				continue;
			}
			validateStructure(webformsForm, abcdForm, (BaseCategory) abcdChild, (BaseCategory) webformsChild);
		}
	}

	private void validateStructure(BaseForm webformsForm, BaseForm abcdForm, BaseGroup abcdElement,
			BaseGroup webformsElement) {
		if (webformsElement instanceof BaseRepeatableGroup && abcdElement instanceof BaseRepeatableGroup) {
			if (((BaseRepeatableGroup) webformsElement).isRepeatable() != ((BaseRepeatableGroup) abcdElement)
					.isRepeatable()) {
				assertTrue(false, new FormGroupRepeatableStatusIsDifferent(webform, abcdForm, webformsElement));
			}
		}

		for (TreeObject webformsChild : webformsElement.getChildren()) {
			TreeObject abcdChild = abcdElement.getChild(webformsChild.getName());
			if (abcdChild == null) {
				assertTrue(false, new FormElementNotFound(webform, abcdForm, webformsChild));
				continue;
			}
			if (webformsChild instanceof BaseQuestion && abcdChild instanceof BaseGroup) {
				assertTrue(false, new FormElementIsBaseQuestionNotBaseGroup(webform, abcdForm, webformsChild));
				continue;
			}
			if (webformsChild instanceof BaseGroup && abcdChild instanceof BaseQuestion) {
				assertTrue(false, new FormElementIsBaseGroupNotBaseQuestion(webform, abcdForm, webformsChild));
				continue;
			}

			if (webformsChild instanceof BaseQuestion) {
				validateStructure(webformsForm, abcdForm, (BaseQuestion) abcdChild, (BaseQuestion) webformsChild);
			} else {
				validateStructure(webformsForm, abcdForm, (BaseGroup) abcdChild, (BaseGroup) webformsChild);
			}
		}
	}

	private void validateStructure(BaseForm webformsForm, BaseForm abcdForm, BaseQuestion abcdQuestion,
			BaseQuestion webformsQuestion) {
		assertTrue(
				checkCompatibility((Question) webformsQuestion,
						(com.biit.abcd.persistence.entity.Question) abcdQuestion), new QuestionCompatibilityError(
						(com.biit.abcd.persistence.entity.Question) abcdQuestion, webformsForm,
						(Question) webformsQuestion));

		for (TreeObject webformsChild : webformsQuestion.getChildren()) {
			TreeObject abcdChild = abcdQuestion.getChild(webformsChild.getName());
			if (abcdChild == null) {
				assertTrue(false, new FormAnswerNotFound(webform, abcdForm, webformsChild));
				continue;
			}
			// Also subanswers
			for (TreeObject webformsGrandChild : webformsChild.getChildren()) {
				TreeObject abcdGrandChild = abcdQuestion.getChild(webformsChild.getName()
						+ TreeObject.DEFAULT_PATH_SEPARATOR + webformsGrandChild.getName());
				if (abcdGrandChild == null) {
					assertTrue(false, new FormAnswerNotFound(webform, abcdForm, webformsGrandChild));
					continue;
				}
			}
		}
	}

	private boolean checkCompatibility(Question webformsQuestion, com.biit.abcd.persistence.entity.Question abcdQuestion) {
		switch (webformsQuestion.getAnswerType()) {
		case INPUT:
			return checkCompatibilityAnswerFormat(webformsQuestion, abcdQuestion);
		case SINGLE_SELECTION_LIST:
		case SINGLE_SELECTION_RADIO:
			if (abcdQuestion.getAnswerType() != null
					&& abcdQuestion.getAnswerType().equals(com.biit.abcd.persistence.entity.AnswerType.RADIO)) {
				return true;
			}
			return false;
		case MULTIPLE_SELECTION:
			if (abcdQuestion.getAnswerType() != null
					&& abcdQuestion.getAnswerType().equals(com.biit.abcd.persistence.entity.AnswerType.MULTI_CHECKBOX)) {
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
			if (abcdQuestion.getAnswerFormat() != null
					&& abcdQuestion.getAnswerFormat().equals(com.biit.abcd.persistence.entity.AnswerFormat.DATE)) {
				return true;
			}
			return false;
		case NUMBER:
			if (abcdQuestion.getAnswerFormat() != null
					&& abcdQuestion.getAnswerFormat().equals(com.biit.abcd.persistence.entity.AnswerFormat.NUMBER)) {
				return true;
			}
			return false;
		case TEXT:
			if (abcdQuestion.getAnswerFormat() != null
					&& abcdQuestion.getAnswerFormat().equals(com.biit.abcd.persistence.entity.AnswerFormat.TEXT)) {
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
