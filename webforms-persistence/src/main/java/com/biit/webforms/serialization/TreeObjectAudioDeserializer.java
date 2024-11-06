package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.persistence.entity.TreeObjectAudio;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TreeObjectAudioDeserializer extends StorableObjectDeserializer<TreeObjectAudio> {

    @Override
    public void deserialize(TreeObjectAudio element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setUrl(parseString("audioUrl", jsonObject));
    }
}
