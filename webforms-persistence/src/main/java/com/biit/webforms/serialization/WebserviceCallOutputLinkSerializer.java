package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WebserviceCallOutputLinkSerializer extends WebserviceCallLinkSerializer<WebserviceCallOutputLink> {

    @Override
    public void serialize(WebserviceCallOutputLink src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeBooleanField("isEditable", src.isEditable());
    }

}