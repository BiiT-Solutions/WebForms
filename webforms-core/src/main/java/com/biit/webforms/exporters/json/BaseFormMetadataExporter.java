package com.biit.webforms.exporters.json;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.persistence.entity.Form;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Exporter to json of the extra version data information of the form.
 */
public class BaseFormMetadataExporter {

    public static String exportFormMetadata(Form form) {
        try {
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(form.getFormMetadata());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
