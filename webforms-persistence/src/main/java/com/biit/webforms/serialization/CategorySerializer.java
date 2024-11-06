package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.Category;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class CategorySerializer extends TreeObjectSerializer<Category> {

    @Override
    public void serialize(Category src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
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
