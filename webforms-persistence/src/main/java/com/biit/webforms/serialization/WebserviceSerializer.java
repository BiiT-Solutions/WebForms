package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.CustomSerializer;
import com.biit.webforms.webservices.Webservice;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WebserviceSerializer extends CustomSerializer<Webservice> {

    @Override
    public void serialize(Webservice src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("name", src.getName());
        jgen.writeStringField("description", src.getDescription());
        jgen.writeStringField("protocol", src.getProtocol());
        jgen.writeStringField("host", src.getHost());
        jgen.writeStringField("port", src.getPort());
        jgen.writeStringField("path", src.getPath());
        jgen.writeObjectField("inputPorts", src.getInputPorts());
        jgen.writeObjectField("outputPorts", src.getOutputPorts());
    }

}
