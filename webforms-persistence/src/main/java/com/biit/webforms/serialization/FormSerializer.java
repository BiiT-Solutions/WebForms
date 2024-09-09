package com.biit.webforms.serialization;


import com.biit.webforms.persistence.entity.Form;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class FormSerializer extends FormElementSerializer<Form> {

    @Override
    public void serialize(Form src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
    }

}
