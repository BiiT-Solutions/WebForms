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
import com.biit.form.jackson.serialization.StorableObjectDeserializer;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowDeserializer extends StorableObjectDeserializer<Flow> {

    @Override
    public void deserialize(Flow element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        if (jsonObject.get("originId") != null) {
            element.setOriginReferencePath(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("originId").toString(), String[].class)));
        }
        if (jsonObject.get("destinyId") != null) {
            element.setDestinyReferencePath(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("destinyId").toString(), String[].class)));
        }
        element.setFlowType(FlowType.from(parseString("flowType", jsonObject)));
        element.setOthers(parseBoolean("others", jsonObject));

        if (!element.isOthers()) {
            final JsonNode tokenObjects = jsonObject.get("condition");
            if (tokenObjects != null) {
                //Handle children one by one.
                if (tokenObjects.isArray()) {
                    List<Token> condition = new ArrayList<>();
                    for (JsonNode childNode : tokenObjects) {
                        try {
                            final Class<? extends Token> classType = (Class<? extends Token>) Class.forName(childNode.get("class").asText());
                            condition.add(ObjectMapperFactory.getObjectMapper().readValue(childNode.toPrettyString(), classType));
                        } catch (ClassNotFoundException | NullPointerException e) {
                            WebformsLogger.severe(this.getClass().getName(), "Invalid condition on flow:\n" + jsonObject.toPrettyString());
                            WebformsLogger.errorMessage(this.getClass().getName(), e);
                            throw new RuntimeException(e);
                        }
                    }
                    element.setCondition(condition);
                }
            }
        }
    }
}
