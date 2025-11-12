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

import com.biit.form.jackson.serialization.StorableObjectSerializer;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class FlowSerializer extends StorableObjectSerializer<Flow> {

    @Override
    public void serialize(Flow src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        if (src != null) {
            if (src.getOrigin() != null) {
                jgen.writeFieldName("originId");
                jgen.writeStartArray("originId");
                for (String reference : src.getOrigin().getPath()) {
                    jgen.writeString(reference);
                }
                jgen.writeEndArray();
            }

            if (src.getDestiny() != null) {
                jgen.writeFieldName("destinyId");
                jgen.writeStartArray("destinyId");
                for (String reference : src.getDestiny().getPath()) {
                    jgen.writeString(reference);
                }
                jgen.writeEndArray();
            }

            if (src.getFlowType() != null) {
                jgen.writeStringField("flowType", src.getFlowType().name());
            } else {
                WebformsLogger.errorMessage(this.getClass().getName(), "Flow without flowtype!");
            }
            jgen.writeBooleanField("others", src.isOthers());
            //If is an others flow, getCondition returns the negation of the sum of all other conditions.
            //While the condition inner value is null.
            if (!src.isOthers()) {
                jgen.writeObjectField("condition", src.getComputedCondition());
            }
        }
    }

}
