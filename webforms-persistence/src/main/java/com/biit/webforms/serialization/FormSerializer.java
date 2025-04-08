package com.biit.webforms.serialization;


import com.biit.webforms.persistence.entity.Form;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class FormSerializer extends FormElementSerializer<Form> {

    private static final long serialVersionUID = -5707427130193301264L;

    @Override
    public void serialize(Form src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        serializeMap("descriptionTranslations", src.getDescriptionTranslations(), jgen);
    }

}
