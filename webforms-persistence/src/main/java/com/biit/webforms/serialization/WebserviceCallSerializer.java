package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WebserviceCallSerializer extends StorableObjectSerializer<WebserviceCall> {

    @Override
    public void serialize(WebserviceCall src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("name", src.getName());
        jgen.writeStringField("webserviceName", src.getWebserviceName());

        if (src.getFormElementTrigger() != null) {
            jgen.writeFieldName("formElementTrigger_id");
            jgen.writeStartArray("formElementTrigger_id");
            for (String reference : src.getFormElementTrigger().getPath()) {
                jgen.writeString(reference);
            }
            jgen.writeEndArray();
        }

        jgen.writeObjectField("inputLinks", src.getInputLinks());
        jgen.writeObjectField("outputLinks", src.getOutputLinks());
    }

}
