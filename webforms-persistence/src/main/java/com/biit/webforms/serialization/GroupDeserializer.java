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

import com.biit.form.jackson.serialization.BaseRepeatableGroupDeserializer;
import com.biit.webforms.persistence.entity.Group;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class GroupDeserializer extends BaseRepeatableGroupDeserializer<Group> {

    private static final long serialVersionUID = -72886343605932274L;

    @Override
    public void deserialize(Group element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setShownAsTable(parseBoolean("isTable", jsonObject));
        element.setTotalAnswersValue(parseInteger("totalAnswersValue", jsonObject));
        if (parseInteger("numberOfColumn", jsonObject) != null) {
            element.setNumberOfColumns(parseInteger("numberOfColumn", jsonObject));
        } else {
            element.setNumberOfColumns(1);
        }

        element.setDescriptionTranslations(parseMap("descriptionTranslations", jsonObject));
    }

}
