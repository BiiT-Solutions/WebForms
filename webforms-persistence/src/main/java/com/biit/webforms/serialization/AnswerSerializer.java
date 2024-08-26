package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.Answer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class AnswerSerializer extends TreeObjectSerializer<Answer> {

    @Override
    public void serialize(Answer src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("description", src.getDescription());
        if (src.getImage() != null) {
            jgen.writeObjectField("image", src.getImage());
        }
    }

}
