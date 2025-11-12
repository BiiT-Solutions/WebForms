package com.biit.webforms.persistence.dao.webservices;

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

import com.biit.utils.file.FileReader;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IWebserviceDao;
import com.biit.webforms.persistence.dao.exceptions.WebserviceNotFoundException;
import com.biit.webforms.webservices.Webservice;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

@Repository
public class WebserviceDao implements IWebserviceDao {

    // For resource path we need to use / directly. Is OS independent
    private static final String WEBSERVICE_RESOURCE_PATH = "webservices/";
    private static final String WEBFORMS_WEBSERVICE_CONFIG_PATH = "WEBFORMS_CONFIG_WEBSERVICES";

    @Override
    public Set<Webservice> getAll() {
        Set<Webservice> webservices = new HashSet<Webservice>();
        URL url = FileReader.class.getClassLoader().getResource(WEBSERVICE_RESOURCE_PATH);
        if (url != null) {
            try {
                File dir = new File(url.toURI());
                if (dir.listFiles() != null) {
                    for (File file : dir.listFiles()) {
                        String fileContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                        if (!fileContent.isEmpty()) {
                            try {
                                webservices.add(Webservice.fromJson(fileContent));
                            } catch (JsonProcessingException e) {
                                WebformsLogger.errorMessage(this.getClass().getName(), "Error parsing json file '" + file.getAbsolutePath()
                                        + "' cause '" + e.getMessage() + "'");
                            }
                        }
                    }
                }
            } catch (URISyntaxException | IOException e) {
                WebformsLogger.errorMessage(this.getClass().getName(), e);
            } catch (IllegalArgumentException e) {
                // Directory doesn't exist.
                WebformsLogger.errorMessage(this.getClass().getName(), e);
            }
        }

        String optionalPath = System.getenv(WEBFORMS_WEBSERVICE_CONFIG_PATH);
        if (optionalPath != null) {
            try {
                File dir = new File(optionalPath);
                if (dir.listFiles() != null) {
                    for (File file : dir.listFiles()) {
                        String fileContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                        if (!fileContent.isEmpty()) {
                            try {
                                webservices.add(Webservice.fromJson(fileContent));
                            } catch (JsonProcessingException e) {
                                WebformsLogger.errorMessage(this.getClass().getName(), "Error parsing json file '" + file.getAbsolutePath()
                                        + "' cause '" + e.getMessage() + "'");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                WebformsLogger.errorMessage(this.getClass().getName(), e);
            }
        }

        return webservices;
    }

    @Override
    public Webservice findWebservice(String name) throws WebserviceNotFoundException {
        Set<Webservice> webservices = getAll();
        for (Webservice webservice : webservices) {
            if (webservice.getName().equals(name)) {
                return webservice;
            }
        }
        throw new WebserviceNotFoundException(name);
    }

}
