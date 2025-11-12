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

import com.biit.form.jackson.serialization.CustomSerializer;
import com.biit.webforms.webservices.WebservicePort;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WebservicePortSerializer extends CustomSerializer<WebservicePort> {

    @Override
    public void serialize(WebservicePort src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("name", src.getName());
        jgen.writeStringField("xpath", src.getXpath());
        if (src.getType() != null) {
            jgen.writeStringField("type", src.getType().name());
        }
        if (src.getFormat() != null) {
            jgen.writeStringField("format", src.getFormat().name());
        }
        if (src.getSubformat() != null) {
            jgen.writeStringField("subformat", src.getSubformat().name());
        }
    }

}
