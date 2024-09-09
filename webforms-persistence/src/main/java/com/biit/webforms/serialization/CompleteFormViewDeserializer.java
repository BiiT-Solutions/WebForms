package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.CompleteFormView;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CompleteFormViewDeserializer extends FormElementDeserializer<CompleteFormView> {

    @Override
    public void deserialize(CompleteFormView element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }

//    @Override
//    public CompleteFormView deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
//        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
//
//        final CompleteFormView completeFormView = new CompleteFormView();
//        completeFormView.setForm(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.toString(), Form.class));
//        return completeFormView;
//    }
}
