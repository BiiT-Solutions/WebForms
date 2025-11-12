package com.biit.webforms.serialization;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.form.jackson.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.TreeObjectAudio;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.entity.TreeObjectVideo;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TextDeserializer extends TreeObjectDeserializer<Text> {

    private static final long serialVersionUID = 9172438219562674834L;

    @Override
    public void deserialize(Text element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setDescription(parseString("description", jsonObject));
        if ((jsonObject.get("image") != null)) {
            element.setImage(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("image").toString(), TreeObjectImage.class));
        }
        if (jsonObject.get("video") != null) {
            element.setVideo(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("video").toString(), TreeObjectVideo.class));
        }
        if (jsonObject.get("audio") != null) {
            element.setAudio(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("audio").toString(), TreeObjectAudio.class));
        }

        element.setDescriptionTranslations(parseMap("descriptionTranslations", jsonObject));
    }
}
