package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class DynamicAnswerSerializer extends TreeObjectSerializer<DynamicAnswer> {

    @Override
    public void serialize(DynamicAnswer src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeFieldName("reference");
        jgen.writeStartArray("reference");
        for (String reference : src.getReference().getPath()) {
            jgen.writeString(reference);
        }
        jgen.writeEndArray();
    }

}
