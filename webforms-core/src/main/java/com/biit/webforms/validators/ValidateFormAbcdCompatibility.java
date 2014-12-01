package com.biit.webforms.validators;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.BaseCategory;
import com.biit.form.BaseForm;
import com.biit.form.BaseGroup;
import com.biit.form.BaseQuestion;
import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.validators.reports.LinkedFormAbcdAnswerNotFound;
import com.biit.webforms.validators.reports.LinkedFormAbcdElementIsBaseGroupNotBaseQuestion;
import com.biit.webforms.validators.reports.LinkedFormAbcdElementIsBaseQuestionNotBaseGroup;
import com.biit.webforms.validators.reports.LinkedFormAbcdElementNotFound;
import com.biit.webforms.validators.reports.LinkedFormAbcdGroupRepeatableStatusIsDifferent;

public class ValidateFormAbcdCompatibility extends SimpleValidator<Form> {

	private final com.biit.webforms.persistence.entity.Form form;

	public ValidateFormAbcdCompatibility(com.biit.webforms.persistence.entity.Form form) {
		super(Form.class);
		this.form = form;
	}

	@Override
	protected void validateImplementation(Form abcdForm) {
		validateStructure(form, abcdForm);
	}

	private void validateStructure(BaseForm webformsForm, BaseForm abcdForm) {
		for (TreeObject abcdChild : abcdForm.getChildren()) {
			TreeObject webformsChild = webformsForm.getChild(abcdChild.getName());
			if (webformsChild == null) {
				assertTrue(false, new LinkedFormAbcdElementNotFound(form, abcdForm, abcdChild));
				continue;
			}
			validateStructure(abcdForm, (BaseCategory) webformsChild, (BaseCategory) abcdChild);
		}
	}

	private void validateStructure(BaseForm abcdForm, BaseGroup webformsElement, BaseGroup abcdElement) {
		if (abcdElement instanceof BaseRepeatableGroup && webformsElement instanceof BaseRepeatableGroup) {
			if (((BaseRepeatableGroup) abcdElement).isRepeatable() != ((BaseRepeatableGroup) webformsElement)
					.isRepeatable()) {
				assertTrue(false, new LinkedFormAbcdGroupRepeatableStatusIsDifferent(form, abcdForm, abcdElement));
			}
		}

		for (TreeObject abcdChild : abcdElement.getChildren()) {
			TreeObject webformsChild = webformsElement.getChild(abcdChild.getName());
			if (webformsChild == null) {
				assertTrue(false, new LinkedFormAbcdElementNotFound(form, abcdForm, abcdChild));
				continue;
			}
			if (abcdChild instanceof BaseQuestion && webformsChild instanceof BaseGroup) {
				assertTrue(false, new LinkedFormAbcdElementIsBaseQuestionNotBaseGroup(form, abcdForm, abcdChild));
				continue;
			}
			if (abcdChild instanceof BaseGroup && webformsChild instanceof BaseQuestion) {
				assertTrue(false, new LinkedFormAbcdElementIsBaseGroupNotBaseQuestion(form, abcdForm, abcdChild));
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
				checkCompatibility((com.biit.webforms.persistence.entity.Question) webformsQuestion,
						(Question) abcdQuestion), new QuestionCompatibilityError(
						(com.biit.webforms.persistence.entity.Question) webformsQuestion, (Form) abcdForm,
						(Question) abcdQuestion));

		for (TreeObject abcdChild : abcdQuestion.getChildren()) {
			TreeObject webformsChild = webformsQuestion.getChild(abcdChild.getName());
			if (webformsChild == null) {
				assertTrue(false, new LinkedFormAbcdAnswerNotFound(form, abcdForm, abcdChild));
				continue;
			}
		}
	}

	private boolean checkCompatibility(com.biit.webforms.persistence.entity.Question question, Question abcdQuestion) {
		switch (question.getAnswerType()) {
		case INPUT:
			return checkCompatibilityAnswerFormat(question, abcdQuestion);
		case SINGLE_SELECTION_LIST:
		case SINGLE_SELECTION_RADIO:
			if (abcdQuestion.getAnswerType()!=null && abcdQuestion.getAnswerType().equals(AnswerType.RADIO)) {
				return true;
			}
			return false;
		case MULTIPLE_SELECTION:
			if (abcdQuestion.getAnswerType()!=null && abcdQuestion.getAnswerType().equals(AnswerType.MULTI_CHECKBOX)) {
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
			if (abcdQuestion.getAnswerFormat()!=null && abcdQuestion.getAnswerFormat().equals(AnswerFormat.DATE)) {
				return true;
			}
			return false;
		case NUMBER:
			if (abcdQuestion.getAnswerFormat()!=null && abcdQuestion.getAnswerFormat().equals(AnswerFormat.NUMBER)) {
				return true;
			}
			return false;
		case TEXT:
			if (abcdQuestion.getAnswerFormat()!=null && abcdQuestion.getAnswerFormat().equals(AnswerFormat.TEXT)) {
				return true;
			}
			return false;
		case POSTAL_CODE:
			if (abcdQuestion.getAnswerFormat()!=null && abcdQuestion.getAnswerFormat().equals(AnswerFormat.POSTAL_CODE)) {
				return true;
			}
			return false;
		}
		return false;
	}

}
