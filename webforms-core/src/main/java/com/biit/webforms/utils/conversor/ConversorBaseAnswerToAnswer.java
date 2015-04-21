package com.biit.webforms.utils.conversor;

import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;

public class ConversorBaseAnswerToAnswer extends ConversorTreeObject<BaseAnswer, Answer> {

	@Override
	public Answer createDestinyInstance() {
		return new Answer();
	}

	@Override
	public void copyData(BaseAnswer origin, Answer destiny) {
		// Copy base data
		super.copyData(origin, destiny);

		// Convert and assign children
		for (TreeObject child : origin.getChildren()) {
			if (child instanceof BaseAnswer) {
				BaseAnswer convertedChild = convert((BaseAnswer) child);
				try {
					destiny.addChild(convertedChild);
				} catch (NotValidChildException | ElementIsReadOnly e) {
					// Impossible
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
			} else {
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
			}
		}
	}
}
