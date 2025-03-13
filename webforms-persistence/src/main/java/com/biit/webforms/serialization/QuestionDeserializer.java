package com.biit.webforms.serialization;

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
        element.setAnswersValuesOnTooltip(parseBoolean("answerValuesOnTooltip", jsonObject));
        element.setDescription(parseString("description", jsonObject));
        element.setMaxAnswersSelected(parseInteger("maxAnswersSelected", jsonObject));
        element.setConsecutiveAnswers(parseBoolean("consecutiveAnswers", jsonObject));

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
