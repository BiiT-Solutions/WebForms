package com.biit.webforms.serialization;

import com.biit.form.json.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.BlockReference;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BlockReferenceDeserializer extends TreeObjectDeserializer<BlockReference> {

    public BlockReferenceDeserializer() {
        super(BlockReference.class);
    }

    @Override
    public BlockReference deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        BlockReference blockReference = new BlockReference();
        Long blockReferencedId = context.deserialize(jsonObject.get("blockReferencedId"), Long.class);
        blockReference.setBlockReferencedId(blockReferencedId);
        //We can only get the ID here. The content must be obtained from a DAO.
        return blockReference;
    }

}
