package com.biit.webforms.webservices;

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

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.serialization.WebservicePortDeserializer;
import com.biit.webforms.serialization.WebservicePortSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonDeserialize(using = WebservicePortDeserializer.class)
@JsonSerialize(using = WebservicePortSerializer.class)
public class WebservicePort {

    private String name;

    private String xpath;

    private AnswerType type;
    private AnswerFormat format;
    private AnswerSubformat subformat;

    public WebservicePort() {

    }

    public WebservicePort(String name, String xpath) {
        setName(name);
        setXpath(xpath);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public AnswerType getType() {
        return type;
    }

    public void setType(AnswerType type) {
        this.type = type;
    }

    public AnswerFormat getFormat() {
        return format;
    }

    public void setFormat(AnswerFormat format) {
        this.format = format;
    }

    public AnswerSubformat getSubformat() {
        return subformat;
    }

    public void setSubformat(AnswerSubformat subformat) {
        this.subformat = subformat;
    }
}
