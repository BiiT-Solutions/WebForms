package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.biit.webforms.persistence.entity.condition.Token;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TokenSerializer<T extends Token> extends StorableObjectSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getType() != null) {
            jgen.writeStringField("type", src.getType().name());
        }
    }

}
