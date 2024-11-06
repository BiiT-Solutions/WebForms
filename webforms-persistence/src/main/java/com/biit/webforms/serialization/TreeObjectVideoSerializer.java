package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.biit.webforms.persistence.entity.TreeObjectVideo;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TreeObjectVideoSerializer extends StorableObjectSerializer<TreeObjectVideo> {

    @Override
    public void serialize(TreeObjectVideo src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("videoUrl", src.getUrl());
        jgen.writeNumberField("width", src.getWidth());
        jgen.writeNumberField("height", src.getHeight());
    }
}
