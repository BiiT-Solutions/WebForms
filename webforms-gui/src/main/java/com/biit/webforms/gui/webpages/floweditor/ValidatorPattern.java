package com.biit.webforms.gui.webpages.floweditor;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
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

import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Validator;

import java.util.regex.Pattern;

public class ValidatorPattern implements Validator {
	private static final long serialVersionUID = -3158239634237872250L;

	private Pattern[] patterns;

	public ValidatorPattern(Pattern... patterns) {
		this.patterns = patterns;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (value != null) {
			for (Pattern pattern : patterns) {
				if (pattern.matcher((String) value).matches()) {
					// Validated
					return;
				}
			}
		}
		String patternString = patterns[0].pattern();
		for (int i = 1; i < patterns.length; i++) {
			patternString += " or " + patterns[i].pattern();
		}
		throw new InvalidValueException(LanguageCodes.VALIDATOR_ERROR_PATTERN.translation() + " " + patternString);
	}

}
