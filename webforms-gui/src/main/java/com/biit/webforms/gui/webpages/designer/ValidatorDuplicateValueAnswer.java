package com.biit.webforms.gui.webpages.designer;

import com.biit.form.TreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Answer;
import com.vaadin.data.Validator;

public class ValidatorDuplicateValueAnswer implements Validator {
	private static final long serialVersionUID = -5138910904884591785L;

	private Answer answer;

	public ValidatorDuplicateValueAnswer(Answer instance) {
		this.answer = instance;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		String newName = (String) value;
		TreeObject parent = answer.getParent();

		if (parent != null) {
			for (TreeObject child : parent.getChildren()) {
				if (child.equals(answer)) {
					// If this child is treeObject, ignore.
					continue;
				}
				if (child.getName().equals(newName)) {
					throw new InvalidValueException(LanguageCodes.CAPTION_VALIDATE_DUPLICATE_ANSWER_VALUE.translation());
				}
			}
		}
	}

}
