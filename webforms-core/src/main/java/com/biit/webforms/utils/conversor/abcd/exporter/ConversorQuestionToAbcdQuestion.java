package com.biit.webforms.utils.conversor.abcd.exporter;

import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.conversor.abcd.importer.ConversorTreeObject;

/**
 * Conversion from Abcd question to Webforms question. Not all types of abcd
 * questions are compatibles with webforms questions. To determine a compatible
 * type and format use the appropiate conversors. The subformat is decided by
 * default from the answer format if it applies.
 *
 */
public class ConversorQuestionToAbcdQuestion extends ConversorTreeObject<com.biit.webforms.persistence.entity.Question, Question> {

	private ConversorAnswerTypeAbcdToAnswerType conversorAnswerType = new ConversorAnswerTypeAbcdToAnswerType();
	private ConversorAnswerFormatToAbcdAnswerFormat conversorAnswerFormat = new ConversorAnswerFormatToAbcdAnswerFormat();
	private ConversorAnswerToAbcdAnswer conversorAnswer = new ConversorAnswerToAbcdAnswer();

	@Override
	public Question createDestinyInstance() {
		return new Question();
	}

	@Override
	public void copyData(com.biit.webforms.persistence.entity.Question origin, Question destiny) {
		// Copy base data
		super.copyData(origin, destiny);

		// Convert and copy answer type and format.
		destiny.setAnswerType(conversorAnswerType.convert(origin.getAnswerType()));
		try {
			destiny.setAnswerFormat(conversorAnswerFormat.convert(origin.getAnswerFormat()));
		} catch (InvalidAnswerFormatException e) {
			// Controlled by ABCD.
		}

		// Convert and assign children
		for (TreeObject child : origin.getChildren()) {
			if (child instanceof com.biit.webforms.persistence.entity.Answer) {
				TreeObject convertedChild = conversorAnswer.convert((com.biit.webforms.persistence.entity.Answer) child);
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
