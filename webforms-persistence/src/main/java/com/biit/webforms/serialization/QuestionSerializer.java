package com.biit.webforms.serialization;

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
        jgen.writeBooleanField("description_always_visible", src.isDescriptionAlwaysVisible());
        jgen.writeStringField("description", src.getDescription());
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
    }

}
