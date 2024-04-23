package com.biit.webforms.serialization;


import com.biit.form.jackson.serialization.BaseFormSerializer;
import com.biit.persistence.entity.BaseStorableObject;
import com.biit.webforms.persistence.entity.Form;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.stream.Collectors;

public class FormElementSerializer<T extends Form> extends BaseFormSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("description", src.getDescription());
        jgen.writeObjectField("flows", src.getFlows());
        jgen.writeObjectField("webserviceCalls", src.getWebserviceCalls());
        if (src.getStatus() != null) {
            jgen.writeStringField("status", src.getStatus().name());
        }
        jgen.writeStringField("linkedFormLabel", src.getLinkedFormLabel());
        jgen.writeObjectField("linkedFormVersions", src.getLinkedFormVersions());
        jgen.writeNumberField("linkedFormOrganizationId", src.getLinkedFormOrganizationId());
        jgen.writeNumberField("formReferenceId", src.getFormReferenceId());
        jgen.writeObjectField("elementsToHide", src.getElementsToHide().stream()
                .map(BaseStorableObject::getId).collect(Collectors.toSet()));
        if (src.getImage() != null) {
            jgen.writeObjectField("image", src.getImage());
        }
    }

}
