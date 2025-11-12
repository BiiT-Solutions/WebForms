package com.biit.webforms.rest;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IPublishedFormDao;
import com.biit.webforms.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.webforms.persistence.entity.PublishedForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path(value = "/published-forms")
public class PublishedFormRestService {

    @Autowired
    private IPublishedFormDao publishedFormDao;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{formLabel}/versions/{formVersion}/organizations/{organizationId}")
    public Response getForm(@PathParam("formLabel") String formLabel, @PathParam("formVersion") int formVersion, @PathParam("organizationId") Long organizationId) {
        WebformsLogger.info(PublishedFormRestService.class.getName(), "Requesting Published Form using endpoint 'GET /forms' with formLabel '{}', formVersion '{}' and organization '{}'.",
                formLabel, formVersion, organizationId);
        try {
            PublishedForm publishedForm = publishedFormDao.get(formLabel, formVersion, organizationId);
            WebformsLogger.debug(PublishedFormRestService.class.getName(), "Form obtained '{}'.", publishedForm);
            if (publishedForm == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown Published Form with label '" + formLabel +
                        "' and version '" + formVersion + "' in organization '" + organizationId + "'.\"}").build();
            }
            final String json = publishedForm.getJsonCode();
            WebformsLogger.debug(PublishedFormRestService.class.getName(), "Form retrieved successfully:\n{} ", json);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (MultiplesFormsFoundException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Multiples forms match the search criteria. " +
                    "Please define some extra parameters\"}").build();
        }
    }
}
