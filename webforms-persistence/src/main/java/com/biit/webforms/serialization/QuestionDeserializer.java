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

import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.TreeObjectAudio;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.entity.TreeObjectVideo;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class QuestionDeserializer extends TreeObjectDeserializer<Question> {

    @Override
    public void deserialize(Question element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        if (jsonObject.get("answerType") != null) {
            element.setAnswerType(AnswerType.from(jsonObject.get("answerType").textValue()));
        }

        if (jsonObject.get("answerFormat") != null) {
            try {
                element.setAnswerFormat(AnswerFormat.from(jsonObject.get("answerFormat").textValue()));
            } catch (InvalidAnswerFormatException e) {
                throw new JsonGenerationException(e, null);
            }
        }

        if (jsonObject.get("answerSubformat") != null) {
            try {
                element.setAnswerSubformat(AnswerSubformat.from(jsonObject.get("answerSubformat").textValue()));
            } catch (InvalidAnswerSubformatException e) {
                throw new JsonGenerationException(e, null);
            }
        }

        element.setAbbreviation(parseString("abbreviation", jsonObject));
        element.setAlias(parseString("alias", jsonObject));
        element.setMandatory(parseBoolean("mandatory", jsonObject));
        element.setHorizontal(parseBoolean("horizontal", jsonObject));
        element.setInverseAnswerOrder(parseBoolean("inverseAnswerOrder", jsonObject));
        element.setDescriptionAlwaysVisible(parseBoolean("descriptionAlwaysVisible", jsonObject));
        element.setAnswersDescriptionAlwaysVisible(parseBoolean("answerDescriptionAlwaysVisible", jsonObject));
        element.setAnswersValuesOnTooltip(parseBoolean("answersValuesOnTooltip", jsonObject));
        element.setDescription(parseString("description", jsonObject));
        element.setMaxAnswersSelected(parseInteger("maxAnswersSelected", jsonObject));
        element.setConsecutiveAnswers(parseBoolean("consecutiveAnswers", jsonObject));

        element.setDescriptionTranslations(parseMap("descriptionTranslations", jsonObject));

        if ((jsonObject.get("image") != null)) {
            element.setImage(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("image").toString(), TreeObjectImage.class));
        }
        if (jsonObject.get("video") != null) {
            element.setVideo(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("video").toString(), TreeObjectVideo.class));
        }
        if (jsonObject.get("audio") != null) {
            element.setAudio(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("audio").toString(), TreeObjectAudio.class));
        }

        element.setDefaultValueString(parseString("defaultValueString", jsonObject));
        element.setDefaultValueTime(parseTimestamp("defaultValueTime", jsonObject));
        String answerValue = parseString("defaultValueAnswer", jsonObject);

        if (answerValue != null && !answerValue.isEmpty()) {
            element.setDefaultValueAnswer(element.getAnswer(answerValue));
        }
    }

}
