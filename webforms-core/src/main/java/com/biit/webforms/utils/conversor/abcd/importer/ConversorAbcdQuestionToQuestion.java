package com.biit.webforms.utils.conversor.abcd.importer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.persistence.entity.Question;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Conversion from Abcd question to Webforms question. Not all types of abcd
 * questions are compatibles with webforms questions. To determine a compatible
 * type and format use the appropiate conversors. The subformat is decided by
 * default from the answer format if it applies.
 *
 */
public class ConversorAbcdQuestionToQuestion extends ConversorTreeObject<Question, com.biit.webforms.persistence.entity.Question> {

	private ConversorAnswerTypeAbcdToAnswerType conversorAnswerType = new ConversorAnswerTypeAbcdToAnswerType();
	private ConversorAnswerFormatAbcdToAnswerFormat conversorAnswerFormat = new ConversorAnswerFormatAbcdToAnswerFormat();
	private ConversorBaseAnswerToAnswer conversorAnswer = new ConversorBaseAnswerToAnswer();

	@Override
	public com.biit.webforms.persistence.entity.Question createDestinyInstance() {
		return new com.biit.webforms.persistence.entity.Question();
	}

	@Override
	public void copyData(Question origin, com.biit.webforms.persistence.entity.Question destiny) {
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
			if (child instanceof BaseAnswer) {
				BaseAnswer convertedChild = conversorAnswer.convert((BaseAnswer) child);
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
