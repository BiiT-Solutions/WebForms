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

import com.biit.form.jackson.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.Category;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class CategorySerializer extends TreeObjectSerializer<Category> {

    private static final long serialVersionUID = 1555005063841473064L;

    @Override
    public void serialize(Category src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src.getImage() != null) {
            jgen.writeObjectField("image", src.getImage());
        }
        if (src.getVideo() != null) {
            jgen.writeObjectField("video", src.getVideo());
        }
        if (src.getAudio() != null) {
            jgen.writeObjectField("audio", src.getAudio());
        }

        serializeMap("descriptionTranslations", src.getDescriptionTranslations(), jgen);
    }
}
