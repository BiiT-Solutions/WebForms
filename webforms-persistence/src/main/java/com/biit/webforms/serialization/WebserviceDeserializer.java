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

import com.biit.form.jackson.serialization.CustomDeserializer;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.webservices.Webservice;
import com.biit.webforms.webservices.WebservicePort;
import com.biit.webforms.webservices.WebserviceValidatedPort;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class WebserviceDeserializer extends CustomDeserializer<Webservice> {

    @Override
    public void deserialize(Webservice element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setName(parseString("name", jsonObject));
        element.setDescription(parseString("description", jsonObject));
        element.setProtocol(parseString("protocol", jsonObject));
        element.setHost(parseString("host", jsonObject));
        element.setPort(parseString("port", jsonObject));
        element.setPath(parseString("path", jsonObject));


        if (jsonObject.get("inputPorts") != null) {
            element.setInputPorts(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("inputPorts").toString(), WebserviceValidatedPort[].class))));
        }
        if (jsonObject.get("outputPorts") != null) {
            element.setOutputPorts(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("outputPorts").toString(), WebservicePort[].class))));
        }
    }
}
