package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.CustomSerializer;
import com.biit.webforms.webservices.WebserviceValidatedPort;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WebserviceValidatedPortSerializer extends CustomSerializer<WebserviceValidatedPort> {

    @Override
    public void serialize(WebserviceValidatedPort src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("name", src.getName());
        jgen.writeStringField("xpath", src.getXpath());
        jgen.writeStringField("validationXpath", src.getValidationXpath());
        if (src.getType() != null) {
            jgen.writeStringField("type", src.getType().name());
        }
        if (src.getFormat() != null) {
            jgen.writeStringField("format", src.getFormat().name());
        }
        if (src.getSubformat() != null) {
            jgen.writeStringField("subformat", src.getSubformat().name());
        }

        if (src.getErrorCodes() != null) {
            jgen.writeFieldName("errorCodes");
            jgen.writeStartArray("errorCodes");
            for (String reference : src.getErrorCodes()) {
                jgen.writeString(reference);
            }
            jgen.writeEndArray();
        }
    }

}
