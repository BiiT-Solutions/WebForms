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


import com.biit.form.jackson.serialization.BaseRepeatableGroupSerializer;
import com.biit.webforms.persistence.entity.Group;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class GroupSerializer extends BaseRepeatableGroupSerializer<Group> {

    private static final long serialVersionUID = -6382690305218725067L;

    @Override
    public void serialize(Group src, JsonGenerator jgen) throws IOException {
        super.serialize(src, jgen);
        jgen.writeBooleanField("isTable", src.isShownAsTable());
        jgen.writeNumberField("numberOfColumn", src.getNumberOfColumns());
        if (src.getTotalAnswersValue() != null) {
            jgen.writeNumberField("totalAnswersValue", src.getTotalAnswersValue());
        }

        serializeMap("descriptionTranslations", src.getDescriptionTranslations(), jgen);
    }
}
