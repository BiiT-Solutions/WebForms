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

import com.biit.webforms.serialization.WebserviceValidatedPortDeserializer;
import com.biit.webforms.serialization.WebserviceValidatedPortSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonDeserialize(using = WebserviceValidatedPortDeserializer.class)
@JsonSerialize(using = WebserviceValidatedPortSerializer.class)
public class WebserviceValidatedPort extends WebservicePort {

    private String validationXpath;
    private List<String> errorCodes;

    public WebserviceValidatedPort() {
        super();
    }

    public WebserviceValidatedPort(String name, String xpath, String validationXpath, String... values) {
        super(name, xpath);
        setValidationXpath(validationXpath);
        setErrorCodes(new ArrayList<>());
        getErrorCodes().addAll(Arrays.asList(values));
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public String getValidationXpath() {
        return validationXpath;
    }

    public void setValidationXpath(String validationXpath) {
        this.validationXpath = validationXpath;
    }

}
