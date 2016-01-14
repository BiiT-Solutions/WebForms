package com.biit.webforms.validators;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.InvalidDefaultValue;

public class ValidateDefaultValues extends SimpleValidator<TreeObject> {

	public ValidateDefaultValues() {
		super(TreeObject.class);
	}

	@Override
	protected void validateImplementation(TreeObject element) {
		for (TreeObject child : element.getAllChildrenInHierarchy(Question.class)) {
			Question question = ((Question) child);
			if (question.getDefaultValueString() != null && question.getAnswerFormat() != null) {
				if (question.getAnswerFormat().equals(AnswerFormat.NUMBER)) {
					if (question.getAnswerSubformat().equals(AnswerSubformat.NUMBER)) {
						assertTrue(question.getDefaultValueString().matches(WebformsConfigurationReader.getInstance().getRegexNumber()),
								new InvalidDefaultValue(question));
					} else if (question.getAnswerSubformat().equals(AnswerSubformat.FLOAT)) {
						assertTrue(question.getDefaultValueString().matches(WebformsConfigurationReader.getInstance().getRegexFloat()),
								new InvalidDefaultValue(question));
					}
				} else if (question.getAnswerFormat().equals(AnswerFormat.DATE)) {
					assertTrue(question.getDefaultValueString().matches(WebformsConfigurationReader.getInstance().getRegexDate()), new InvalidDefaultValue(
							question));
				} else if (question.getAnswerFormat().equals(AnswerFormat.POSTAL_CODE)) {
					assertTrue(question.getDefaultValueString().matches(WebformsConfigurationReader.getInstance().getRegexPostalCode()),
							new InvalidDefaultValue(question));
				}
			}
		}
	}
}
