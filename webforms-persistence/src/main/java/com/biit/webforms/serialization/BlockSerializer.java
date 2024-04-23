package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.Block;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class BlockSerializer extends FormElementSerializer<Block> {

    @Override
    public void serialize(Block src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
    }

}
