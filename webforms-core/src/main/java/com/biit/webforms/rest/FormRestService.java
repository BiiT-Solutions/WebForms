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

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.exceptions.InvalidValue;
import com.biit.webforms.providers.FormProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/forms")
public class FormRestService {

    @Autowired
    private FormProvider formProvider;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/json")
    public Response getForm(String petition) {
        FormDescription parsedPetition;
        WebformsLogger.info(FormRestService.class.getName(), "Requesting Form using endpoint 'POST /forms/json' with payload '{}'.", petition);
        try {
            parsedPetition = parsePetition(petition);
            WebformsLogger.debug(FormRestService.class.getName(), "Payload obtained '{}'.", parsedPetition);
            Form form = formProvider.get(parsedPetition.formName, parsedPetition.getVersion(), parsedPetition.getOrganizationId());
            WebformsLogger.debug(FormRestService.class.getName(), "Form obtained '{}'.", form);
            if (form == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown form with name '" + parsedPetition.getFormName() +
                        "' and version '" + parsedPetition.getVersion() + "' in organization '" + parsedPetition.getOrganizationId() + "'.\"}").build();
            }
            final String json;
            if (form.getJsonCode() != null) {
                json = form.getJsonCode();
            } else {
                json = form.toJson();
            }
            WebformsLogger.debug(FormRestService.class.getName(), "Form retrieved successfully:\n{} ", json);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException | InvalidValue ex) {
            WebformsLogger.errorMessage(this.getClass().getName(), ex);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Json syntax error\"}").build();
        } catch (MultiplesFormsFoundException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Multiples forms match the search criteria. " +
                    "Please define some extra parameters\"}").build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{formName}/versions/{formVersion}/organizations/{organizationId}")
    public Response getForm(@PathParam("formName") String formName, @PathParam("formVersion") int formVersion, @PathParam("organizationId") long organizationId) {
        WebformsLogger.info(FormRestService.class.getName(), "Requesting Form using endpoint 'GET /forms' with formName '{}', formVersion '{}' and organization '{}'.",
                formName, formVersion, organizationId);
        try {
            //String jsonCode = formDao.getJson(formView.getId());
            Form form = formProvider.get(formName, formVersion, organizationId);
            WebformsLogger.debug(FormRestService.class.getName(), "Form obtained '{}'.", form);
            if (form == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown form with name '" + formName +
                        "' and version '" + formVersion + "' in organization '" + organizationId + "'.\"}").build();
            }
            final String json = new CompleteFormView(form).toJson();
            WebformsLogger.debug(FormRestService.class.getName(), "Form retrieved successfully:\n{} ", json);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (MultiplesFormsFoundException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Multiples forms match the search criteria. " +
                    "Please define some extra parameters\"}").build();
        }
    }


    private FormDescription parsePetition(String petition) throws JsonProcessingException, InvalidValue {
        if (petition == null || petition.isEmpty()) {
            throw new InvalidValue("Empty parameter not allowed.");
        }
        return ObjectMapperFactory.getObjectMapper().readValue(petition, FormDescription.class);
    }

    static class FormDescription {
        private String formName;
        private Integer version;
        private Long organizationId;

        public FormDescription() {
            super();
        }

        public FormDescription(String formName, Integer version, Long organizationId) {
            this();
            this.formName = formName;
            this.version = version;
            this.organizationId = organizationId;
        }

        public String getFormName() {
            return formName;
        }

        public void setFormName(String formName) {
            this.formName = formName;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public Long getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(Long organizationId) {
            this.organizationId = organizationId;
        }

        @Override
        public String toString() {
            return "FormDescription{" +
                    "formName='" + formName + '\'' +
                    ", version=" + version +
                    ", organizationId=" + organizationId +
                    '}';
        }
    }
}
