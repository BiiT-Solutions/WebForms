package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.AttachedFiles;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class AttachedFilesSerializer extends TreeObjectSerializer<AttachedFiles> {

    @Override
    public void serialize(AttachedFiles src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeBooleanField("mandatory", src.isMandatory());
        jgen.writeBooleanField("editionDisabled", src.isEditionDisabled());
    }

}
