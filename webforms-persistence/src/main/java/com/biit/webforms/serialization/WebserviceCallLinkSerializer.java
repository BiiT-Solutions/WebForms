package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WebserviceCallLinkSerializer<T extends WebserviceCallLink> extends StorableObjectSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("webservicePort", src.getWebservicePort());

        if (src.getFormElement() != null) {
            jgen.writeFieldName("formElement_id");
            jgen.writeStartArray("formElement_id");
            for (String reference : src.getFormElement().getPath()) {
                jgen.writeString(reference);
            }
            jgen.writeEndArray();
        }
    }

}