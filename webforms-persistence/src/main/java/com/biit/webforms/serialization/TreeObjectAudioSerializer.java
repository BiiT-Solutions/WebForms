package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.biit.webforms.persistence.entity.TreeObjectAudio;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class TreeObjectAudioSerializer extends StorableObjectSerializer<TreeObjectAudio> {

    @Override
    public void serialize(TreeObjectAudio src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("audioUrl", src.getUrl());
    }
}
