package com.biit.webforms.serialization;

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

import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TokenBetweenDeserializer extends TokenWithQuestionDeserializer<TokenBetween> {

    @Override
    public void deserialize(TokenBetween element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        element.setSubformat(AnswerSubformat.from(parseString("subformat", jsonObject)));
        element.setDatePeriodUnit(DatePeriodUnit.from(parseString("datePeriodUnit", jsonObject)));
        element.setValueStart(parseString("valueStart", jsonObject));
        element.setValueEnd(parseString("valueEnd", jsonObject));

        super.deserialize(element, jsonObject, context);
    }

}
