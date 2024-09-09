package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.SystemField;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class SystemFieldSerializer extends TreeObjectSerializer<SystemField> {

    @Override
    public void serialize(SystemField src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeStringField("fieldName", src.getFieldName());
    }

}