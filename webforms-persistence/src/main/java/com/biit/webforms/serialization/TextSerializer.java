package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.Text;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TextSerializer extends TreeObjectSerializer<Text> {

    @Override
    public void serialize(Text src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("description", src.getDescription());
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
