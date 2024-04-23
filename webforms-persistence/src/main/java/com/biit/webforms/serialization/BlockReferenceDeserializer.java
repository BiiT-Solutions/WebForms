package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.BlockReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class BlockReferenceDeserializer extends TreeObjectDeserializer<BlockReference> {

    @Override
    public void deserialize(BlockReference element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        if (jsonObject.get("blockReferencedId") != null) {
            element.setBlockReferencedId(parseLong("blockReferencedId", jsonObject));
        }
    }

}
