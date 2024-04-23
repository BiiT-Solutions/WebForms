package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Block;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class BlockDeserializer extends FormElementDeserializer<Block> {

    @Override
    public void deserialize(Block element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }
}
