package com.biit.webforms.enumerations;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
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

import com.biit.webforms.configuration.WebformsConfigurationReader;

import java.util.regex.Pattern;

public enum AnswerSubformat {

    // Text subtypes
    TEXT("", WebformsConfigurationReader.getInstance().getRegexText()),

    EMAIL("something@domain.ext", WebformsConfigurationReader.getInstance().getRegexEmail()),

    PHONE("(+NN) NNN NNN NNN", WebformsConfigurationReader.getInstance().getRegexPhone()),

    IBAN("LLNN [L/N](1-34)", WebformsConfigurationReader.getInstance().getRegexIban()),

    BSN("#9", WebformsConfigurationReader.getInstance().getRegexBsn()),

    //Number subtypes
    NUMBER("[0-9]+", WebformsConfigurationReader.getInstance().getRegexNumber()),

    FLOAT("[0-9]+\\.[0-9]*", WebformsConfigurationReader.getInstance().getRegexFloat()),

    POSITIVE_NUMBER("\\++[0-9]+", WebformsConfigurationReader.getInstance().getRegexPositiveNumber()),

    NEGATIVE_NUMBER("-[0-9]+", WebformsConfigurationReader.getInstance().getRegexNegativeNumber()),

    POSITIVE_FLOAT("\\++[0-9]+\\.[0-9]*", WebformsConfigurationReader.getInstance().getRegexPositiveFloat()),

    NEGATIVE_FLOAT("-[0-9]+\\.[0-9]*", WebformsConfigurationReader.getInstance().getRegexNegativeFloat()),

    AMOUNT("[0-9]+\\.[0-9]*", WebformsConfigurationReader.getInstance().getRegexAmount()),

    // Date subtypes
    DATE("dd/mm/yyyy", WebformsConfigurationReader.getInstance().getRegexDate()),

    DATE_PAST("31/12/2000", WebformsConfigurationReader.getInstance().getRegexDate()),

    DATE_FUTURE("31/12/2000", WebformsConfigurationReader.getInstance().getRegexDate()),

    DATE_PERIOD("N", WebformsConfigurationReader.getInstance().getRegexDatePeriod()),

    DATE_BIRTHDAY("31/12/2000", WebformsConfigurationReader.getInstance().getRegexDateBirthday()),

    // Postal code
    POSTAL_CODE("0000AA", WebformsConfigurationReader.getInstance().getRegexPostalCode());

    private final String hint;
    private final Pattern regex;

    AnswerSubformat(String hint, String regexPattern) {
        this.hint = hint;
        this.regex = Pattern.compile(regexPattern);
    }

    public String getHint() {
        return hint;
    }

    public Pattern getRegex() {
        return regex;
    }

    public AnswerFormat getAnswerFormat() {
        for (AnswerFormat answerFormat : AnswerFormat.values()) {
            if (answerFormat.isSubformat(this)) {
                return answerFormat;
            }
        }
        return null;
    }

    public static AnswerSubformat from(String name) {
        for (AnswerSubformat answerSubformat : AnswerSubformat.values()) {
            if (answerSubformat.name().equalsIgnoreCase(name)) {
                return answerSubformat;
            }
        }
        return null;
    }
}
