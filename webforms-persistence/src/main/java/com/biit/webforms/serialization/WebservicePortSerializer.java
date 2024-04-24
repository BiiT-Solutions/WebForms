package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.CustomSerializer;
import com.biit.webforms.webservices.WebservicePort;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WebservicePortSerializer extends CustomSerializer<WebservicePort> {

    @Override
    public void serialize(WebservicePort src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("name", src.getName());
        jgen.writeStringField("xpath", src.getXpath());
        if (src.getType() != null) {
            jgen.writeStringField("type", src.getType().name());
        }
        if (src.getFormat() != null) {
            jgen.writeStringField("format", src.getFormat().name());
        }
        if (src.getSubformat() != null) {
            jgen.writeStringField("subformat", src.getSubformat().name());
        }
    }

}
