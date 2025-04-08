package com.biit.webforms.serialization;


import com.biit.form.jackson.serialization.BaseRepeatableGroupSerializer;
import com.biit.webforms.persistence.entity.Group;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class GroupSerializer extends BaseRepeatableGroupSerializer<Group> {

    private static final long serialVersionUID = -6382690305218725067L;

    @Override
    public void serialize(Group src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeBooleanField("isTable", src.isShownAsTable());
        jgen.writeNumberField("numberOfColumn", src.getNumberOfColumns());
        if (src.getTotalAnswersValue() != null) {
            jgen.writeNumberField("totalAnswersValue", src.getTotalAnswersValue());
        }

        serializeMap("descriptionTranslations", src.getDescriptionTranslations(), jgen);
    }
}
