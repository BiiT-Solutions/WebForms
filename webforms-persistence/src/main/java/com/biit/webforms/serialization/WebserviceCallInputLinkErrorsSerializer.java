package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.CustomSerializer;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.gson.JsonObject;

import java.io.IOException;

public class WebserviceCallInputLinkErrorsSerializer extends CustomSerializer<WebserviceCallInputLinkErrors> {

    @Override
    public void serialize(WebserviceCallInputLinkErrors src, JsonGenerator jgen) throws IOException {
        final JsonObject jsonObject = new JsonObject();
        jgen.writeStringField("errorCode", src.getErrorCode());
        jgen.writeStringField("errorMessage", src.getErrorMessage());
    }

}