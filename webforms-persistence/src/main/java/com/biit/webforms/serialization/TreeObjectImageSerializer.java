package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TreeObjectImageSerializer extends StorableObjectSerializer<TreeObjectImage> {

    @Override
    public void serialize(TreeObjectImage src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("fileName", src.getFileName());
        jgen.writeNumberField("width", src.getWidth());
        jgen.writeNumberField("height", src.getHeight());
        jgen.writeStringField("data", src.toBase64());
        jgen.writeStringField("imageUrl", src.getUrl());
    }
}
