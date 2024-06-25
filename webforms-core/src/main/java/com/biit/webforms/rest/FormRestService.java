package com.biit.webforms.rest;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.exceptions.InvalidValue;
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
    private IFormDao formDao;

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
            Form form = formDao.get(parsedPetition.formName, parsedPetition.getVersion(), parsedPetition.getOrganizationId());
            WebformsLogger.debug(FormRestService.class.getName(), "Form obtained '{}'.", form);
            if (form == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown form with name '" + parsedPetition.getFormName() +
                        "' and version '" + parsedPetition.getVersion() + "' in organization '" + parsedPetition.getOrganizationId() + "'.\"}").build();
            }
            String json = form.toJson();
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
        FormDescription parsedPetition;
        WebformsLogger.info(FormRestService.class.getName(), "Requesting Form using endpoint 'GET /forms' with formName '{}', formVersion '{}' and organization '{}'.",
                formName, formVersion, organizationId);
        try {
            Form form = formDao.get(formName, formVersion, organizationId);
            WebformsLogger.debug(FormRestService.class.getName(), "Form obtained '{}'.", form);
            if (form == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown form with name '" + formName +
                        "' and version '" + formVersion + "' in organization '" + organizationId + "'.\"}").build();
            }
            final String json = form.toJson();
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

        public FormDescription(String formName, Integer version, Long organizationId) {
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
