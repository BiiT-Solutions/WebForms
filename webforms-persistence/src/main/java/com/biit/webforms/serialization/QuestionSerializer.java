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

import com.biit.form.jackson.serialization.CustomDeserializer;
import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.Question;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class QuestionSerializer extends TreeObjectSerializer<Question> {

    @Override
    public void serialize(Question src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("abbreviation", src.getAbbreviation());
        jgen.writeStringField("alias", src.getAlias());
        if (src.getAnswerType() != null) {
            jgen.writeStringField("answerType", src.getAnswerType().name());
        }
        if (src.getAnswerFormat() != null) {
            jgen.writeStringField("answerFormat", src.getAnswerFormat().name());
        }
        if (src.getAnswerSubformat() != null) {
            jgen.writeStringField("answerSubformat", src.getAnswerSubformat().name());
        }
        jgen.writeBooleanField("mandatory", src.isMandatory());
        if (src.getMaxAnswersSelected() != null) {
            jgen.writeNumberField("maxAnswersSelected", src.getMaxAnswersSelected());
        }
        jgen.writeBooleanField("consecutiveAnswers", src.isConsecutiveAnswers());
        jgen.writeBooleanField("horizontal", src.isHorizontal());
        jgen.writeBooleanField("inverseAnswerOrder", src.isInverseAnswerOrder());
        jgen.writeBooleanField("descriptionAlwaysVisible", src.isDescriptionAlwaysVisible());
        jgen.writeBooleanField("answerDescriptionAlwaysVisible", src.isAnswersDescriptionAlwaysVisible());
        jgen.writeBooleanField("answersValuesOnTooltip", src.isAnswersValuesOnTooltip());
        jgen.writeStringField("description", src.getDescription());
        serializeMap("descriptionTranslations", src.getDescriptionTranslations(), jgen);
        jgen.writeStringField("defaultValueString", src.getDefaultValueString());
        if (src.getDefaultValueAnswer() != null) {
            jgen.writeStringField("defaultValueAnswer", src.getDefaultValueAnswer().getValue());
        }
        if (src.getDefaultValueTime() != null) {
            jgen.writeStringField("defaultValueTime", CustomDeserializer.TIMESTAMP_FORMATTER.format(src.getDefaultValueTime().toLocalDateTime()));
        }
        if (src.getImage() != null) {
            jgen.writeObjectField("image", src.getImage());
        }
        if (src.getVideo() != null) {
            jgen.writeObjectField("video", src.getVideo());
        }
        if (src.getAudio() != null) {
            jgen.writeObjectField("audio", src.getAudio());
        }
    }

}
