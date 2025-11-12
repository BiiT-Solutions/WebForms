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
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class WebserviceCallDeserializer extends StorableObjectDeserializer<WebserviceCall> {

    @Override
    public void deserialize(WebserviceCall element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setName(parseString("name", jsonObject));
        element.setWebserviceName(parseString("webserviceName", jsonObject));


        if (jsonObject.get("formElementTrigger_id") != null) {
            element.setFormElementTriggerPath(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("formElementTrigger_id").toString(), String[].class)));
        }

        element.setInputLinks(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("inputLinks").toString(), WebserviceCallInputLink[].class))));
        element.setOutputLinks(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("outputLinks").toString(), WebserviceCallOutputLink[].class))));
    }
}
