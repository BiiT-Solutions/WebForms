package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Form;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class FormDeserializer extends FormElementDeserializer<Form> {

    @Override
    public void deserialize(Form element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setDescriptionTranslations(parseMap("descriptionTranslations", jsonObject));
    }
}
