package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class WebserviceCallInputLinkDeserializer extends WebserviceCallLinkDeserializer<WebserviceCallInputLink> {


    @Override
    public void deserialize(JsonElement json, JsonDeserializationContext context, WebserviceCallInputLink element) {
        super.deserialize(element, jsonObject, context);
        element.setErrors(parseErrors("errors", form, jsonObject, context));
        element.setValidationXpath(parseString("validationXpath", jsonObject, context));
    }

    private Set<WebserviceCallInputLinkErrors> parseErrors(String name, Form form, JsonObject jsonObject, JsonDeserializationContext context) {
        HashSet<WebserviceCallInputLinkErrors> parsedErrors = new HashSet<>();

        JsonElement valuesJson = jsonObject.get(name);
        if (valuesJson != null) {
            Type listType = new TypeToken<Set<WebserviceCallInputLinkErrors>>() {
            }.getType();
            @SuppressWarnings("unchecked")
            Set<WebserviceCallInputLinkErrors> errors = (Set<WebserviceCallInputLinkErrors>) context.deserialize(valuesJson, listType);
            if (errors != null) {
                for (WebserviceCallInputLinkErrors error : errors) {
                    parsedErrors.add(error);
                }
            }
        }

        return parsedErrors;
    }

}