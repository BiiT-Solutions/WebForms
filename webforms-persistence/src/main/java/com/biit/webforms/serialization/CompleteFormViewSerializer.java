package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.CompleteFormView;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class CompleteFormViewSerializer extends FormElementSerializer<CompleteFormView> {

    private static final long serialVersionUID = -5980879594915158233L;

    @Override
    public void serialize(CompleteFormView src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        //jgen.writeObjectField("form", src.getForm());
    }

}
