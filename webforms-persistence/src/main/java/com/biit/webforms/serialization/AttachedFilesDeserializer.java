package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.AttachedFiles;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class AttachedFilesDeserializer extends TreeObjectDeserializer<AttachedFiles> {

    @Override
    public void deserialize(AttachedFiles element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setMandatory(parseBoolean("mandatory", jsonObject));
        element.setEditionDisabled(parseBoolean("editionDisabled", jsonObject));
    }
}
