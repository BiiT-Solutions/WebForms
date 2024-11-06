package com.biit.webforms.serialization;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.TreeObjectAudio;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.entity.TreeObjectVideo;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class CategoryDeserializer extends TreeObjectDeserializer<Category> {

    @Override
    public void deserialize(Category element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if ((jsonObject.get("image") != null)) {
            element.setImage(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("image").toString(), TreeObjectImage.class));
        }
        if (jsonObject.get("video") != null) {
            element.setVideo(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("video").toString(), TreeObjectVideo.class));
        }
        if (jsonObject.get("audio") != null) {
            element.setAudio(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("audio").toString(), TreeObjectAudio.class));
        }
    }

}
