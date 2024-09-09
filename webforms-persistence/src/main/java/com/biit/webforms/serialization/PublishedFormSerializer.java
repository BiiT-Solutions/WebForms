package com.biit.webforms.serialization;


import com.biit.form.jackson.serialization.BaseStorableObjectSerializer;
import com.biit.webforms.persistence.entity.PublishedForm;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class PublishedFormSerializer extends BaseStorableObjectSerializer<PublishedForm> {

    @Override
    public void serialize(PublishedForm src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getLabel() != null) {
            jgen.writeStringField("label", src.getLabel());
        }

        if (src.getVersion() != null) {
            jgen.writeNumberField("version", src.getVersion());
        }

        if (src.getOrganizationId() != null) {
            jgen.writeNumberField("organizationId", src.getOrganizationId());
        }

        if (src.getJsonCode() != null) {
            jgen.writeStringField("jsonCode", src.getJsonCode());
        }
    }

}
