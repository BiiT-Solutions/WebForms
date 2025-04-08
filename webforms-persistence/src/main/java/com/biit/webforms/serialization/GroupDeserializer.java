package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.BaseRepeatableGroupDeserializer;
import com.biit.webforms.persistence.entity.Group;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class GroupDeserializer extends BaseRepeatableGroupDeserializer<Group> {

    private static final long serialVersionUID = -72886343605932274L;

    @Override
    public void deserialize(Group element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setShownAsTable(parseBoolean("isTable", jsonObject));
        element.setTotalAnswersValue(parseInteger("totalAnswersValue", jsonObject));
        if (parseInteger("numberOfColumn", jsonObject) != null) {
            element.setNumberOfColumns(parseInteger("numberOfColumn", jsonObject));
        } else {
            element.setNumberOfColumns(1);
        }

        element.setDescriptionTranslations(parseMap("descriptionTranslations", jsonObject));
    }

}