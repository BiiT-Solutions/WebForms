package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.CustomDeserializer;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class WebserviceCallInputLinkErrorsDeserializer extends CustomDeserializer<WebserviceCallInputLinkErrors> {

    @Override
    public void deserialize(WebserviceCallInputLinkErrors element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setErrorCode(parseString("errorCode", jsonObject));
        element.setErrorMessage(parseString("errorMessage", jsonObject));
    }

}
