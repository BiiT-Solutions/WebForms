package com.biit.webforms.serialization;

import com.biit.form.json.serialization.BaseRepeatableGroupDeserializer;
import com.biit.webforms.persistence.entity.Group;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GroupDeserializer extends BaseRepeatableGroupDeserializer<Group> {

    public GroupDeserializer() {
        super(Group.class);
    }

    public void deserialize(JsonElement json, JsonDeserializationContext context, Group element) {
        JsonObject jobject = (JsonObject) json;

        try {
            element.setShownAsTable((Boolean) context.deserialize(jobject.get("isTable"), Boolean.class));
        } catch (Exception npe) {
            // Not defined.
        }

        try {
            element.setNumberOfColumns((Integer) context.deserialize(jobject.get("numberOfColumn"), Integer.class));
        } catch (Exception npe) {
            // Not defined.
            element.setNumberOfColumns(1);
        }

        super.deserialize(json, context, element);
    }

}