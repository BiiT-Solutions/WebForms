package com.biit.webforms.utils.conversor.abcd.exporter;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.conversor.abcd.importer.ConversorTreeObject;

/**
 * Conversor of answers from abcd to webforms.
 *
 */
public class ConversorAnswerToAbcdAnswer extends ConversorTreeObject<com.biit.webforms.persistence.entity.Answer, Answer> {

	@Override
	public Answer createDestinyInstance() {
		return new Answer();
	}

	@Override
	public void copyData(com.biit.webforms.persistence.entity.Answer origin, Answer destiny) {
		// Copy base data
		super.copyData(origin, destiny);

		// Convert and assign children
		for (TreeObject child : origin.getChildren()) {
			if (child instanceof com.biit.webforms.persistence.entity.Answer) {
				BaseAnswer convertedChild = convert((com.biit.webforms.persistence.entity.Answer) child);
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
