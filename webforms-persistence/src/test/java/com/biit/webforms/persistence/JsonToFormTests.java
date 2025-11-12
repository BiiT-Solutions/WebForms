package com.biit.webforms.persistence;

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

import com.biit.webforms.persistence.entity.Form;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test(groups = {"jsonTests"})
public class JsonToFormTests {

    private String readJsonFile(String fileName) throws URISyntaxException, IOException {
        return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                .getResource("json" + File.separator + fileName).toURI()))).trim();
    }

    @Test
    public void readOrganizationForm() throws URISyntaxException, IOException {
        final String json = readJsonFile("Organization.json");

        Form organizationForm = Form.fromJson(json);
        Assert.assertNotNull(organizationForm);
    }
}
