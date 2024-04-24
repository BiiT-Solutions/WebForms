package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.BlockReference;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class BlockReferenceSerializer extends TreeObjectSerializer<BlockReference> {

    @Override
    public void serialize(BlockReference src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getReference() != null) {
            jgen.writeObjectField("blockReferencedId", src.getReference().getId());
        }
    }

}
