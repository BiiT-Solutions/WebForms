package com.biit.webforms.validators;

import com.biit.form.entity.BaseCategory;
import com.biit.form.entity.BaseForm;
import com.biit.form.entity.BaseGroup;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.FormAnswerNotFound;
import com.biit.webforms.validators.reports.FormElementIsBaseGroupNotBaseQuestion;
import com.biit.webforms.validators.reports.FormElementIsBaseQuestionNotBaseGroup;
import com.biit.webforms.validators.reports.FormElementNotFound;
import com.biit.webforms.validators.reports.FormGroupRepeatableStatusIsDifferent;
import com.biit.webforms.validators.reports.QuestionCompatibilityError;

public class ValidateFormAbcdCompatibility extends SimpleValidator<com.biit.abcd.persistence.entity.Form> {

	private final Form webforms;

	public ValidateFormAbcdCompatibility(Form form) {
		super(com.biit.abcd.persistence.entity.Form.class);
		this.webforms = form;
	}

	@Override
	protected void validateImplementation(com.biit.abcd.persistence.entity.Form abcdForm) {
		if (webforms != null) {
			validateStructure(webforms, abcdForm);
		}
	}

	private void validateStructure(BaseForm webformsForm, BaseForm abcdForm) {
		for (TreeObject abcdChild : abcdForm.getChildren()) {
			TreeObject webformsChild = webformsForm.getChild(abcdChild.getName());
			if (webformsChild == null) {
				assertTrue(false, new FormElementNotFound(abcdForm, webforms, abcdChild));
				continue;
			}
			validateStructure(abcdForm, (BaseCategory) webformsChild, (BaseCategory) abcdChild);
		}
	}

	private void validateStructure(BaseForm abcdForm, BaseGroup webformsElement, BaseGroup abcdElement) {
		if (abcdElement instanceof BaseRepeatableGroup && webformsElement instanceof BaseRepeatableGroup) {
			if (((BaseRepeatableGroup) abcdElement).isRepeatable() != ((BaseRepeatableGroup) webformsElement)
					.isRepeatable()) {
				assertTrue(false, new FormGroupRepeatableStatusIsDifferent(abcdForm, webforms, abcdElement));
			}
		}

		for (TreeObject abcdChild : abcdElement.getChildren()) {
			TreeObject webformsChild = webformsElement.getChild(abcdChild.getName());
			if (webformsChild == null) {
				assertTrue(false, new FormElementNotFound(abcdForm, webforms, abcdChild));
				continue;
			}
			if (abcdChild instanceof BaseQuestion && webformsChild instanceof BaseGroup) {
				assertTrue(false, new FormElementIsBaseQuestionNotBaseGroup(abcdForm, webforms, abcdChild));
				continue;
			}
			if (abcdChild instanceof BaseGroup && webformsChild instanceof BaseQuestion) {
				assertTrue(false, new FormElementIsBaseGroupNotBaseQuestion(abcdForm, webforms, abcdChild));
				continue;
			}

			if (abcdChild instanceof BaseQuestion) {
				validateStructure(abcdForm, (BaseQuestion) webformsChild, (BaseQuestion) abcdChild);
			} else {
				validateStructure(abcdForm, (BaseGroup) webformsChild, (BaseGroup) abcdChild);
			}
		}
	}

	private void validateStructure(BaseForm abcdForm, BaseQuestion webformsQuestion, BaseQuestion abcdQuestion) {
		assertTrue(
				checkCompatibility((Question) webformsQuestion,
						(com.biit.abcd.persistence.entity.Question) abcdQuestion), new QuestionCompatibilityError(
						(Question) webformsQuestion, (com.biit.abcd.persistence.entity.Form) abcdForm,
						(com.biit.abcd.persistence.entity.Question) abcdQuestion));

		for (TreeObject abcdChild : abcdQuestion.getChildren()) {
			TreeObject webformsChild = webformsQuestion.getChild(abcdChild.getName());
			if (webformsChild == null) {
				assertTrue(false, new FormAnswerNotFound(abcdForm, webforms, abcdChild));
				continue;
			}

			// Also subanswers
			for (TreeObject abcdGrandChild : abcdChild.getChildren()) {
				TreeObject webformsGrandChild = webformsQuestion.getChild(abcdChild.getName()
						+ TreeObject.DEFAULT_PATH_SEPARATOR + abcdGrandChild.getName());
				if (webformsGrandChild == null) {
					assertTrue(false, new FormAnswerNotFound(abcdForm, webforms, abcdGrandChild));
					continue;
				}
			}
		}
	}

	private boolean checkCompatibility(Question question, com.biit.abcd.persistence.entity.Question abcdQuestion) {
		switch (question.getAnswerType()) {
		case INPUT:
			return checkCompatibilityAnswerFormat(question, abcdQuestion);
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
