package com.biit.webforms.serialization;

import com.biit.form.json.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.BlockReference;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

public class BlockReferenceSerializer extends TreeObjectSerializer<BlockReference> {

    @Override
    public JsonElement serialize(BlockReference src, Type typeOfSrc, JsonSerializationContext context) {
        if (src.getReference() != null) {
            return new BlockSerializer().serialize(src.getReference(), typeOfSrc, context);
        }
        return new JsonObject();
    }

}
