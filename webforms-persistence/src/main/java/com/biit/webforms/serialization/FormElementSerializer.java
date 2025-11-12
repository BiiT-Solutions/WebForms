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


import com.biit.form.jackson.serialization.BaseFormSerializer;
import com.biit.persistence.entity.BaseStorableObject;
import com.biit.webforms.persistence.entity.Form;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.stream.Collectors;

public class FormElementSerializer<T extends Form> extends BaseFormSerializer<T> {

    @Override
    public void serialize(T src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);

        jgen.writeStringField("description", src.getDescription());
        if (src.getFlows() != null) {
            jgen.writeObjectField("flows", src.getFlows());
        }
        jgen.writeObjectField("webserviceCalls", src.getWebserviceCalls());
        if (src.getStatus() != null) {
            jgen.writeStringField("status", src.getStatus().name());
        }
        jgen.writeStringField("linkedFormLabel", src.getLinkedFormLabel());
        jgen.writeObjectField("linkedFormVersions", src.getLinkedFormVersions());
        if (src.getLinkedFormOrganizationId() != null) {
            jgen.writeNumberField("linkedFormOrganizationId", src.getLinkedFormOrganizationId());
        }
        if (src.getFormReferenceId() != null) {
            jgen.writeNumberField("formReferenceId", src.getFormReferenceId());
        }
        jgen.writeObjectField("elementsToHide", src.getElementsToHide().stream()
                .map(BaseStorableObject::getId).collect(Collectors.toSet()));
        if (src.getImage() != null) {
            jgen.writeObjectField("image", src.getImage());
        }
        if (src.getVideo() != null) {
            jgen.writeObjectField("video", src.getVideo());
        }
        if (src.getAudio() != null) {
            jgen.writeObjectField("audio", src.getAudio());
        }
    }

}
