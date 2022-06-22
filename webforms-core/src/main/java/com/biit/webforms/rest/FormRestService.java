package com.biit.webforms.rest;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Form;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
        WebformsLogger.info(FormRestService.class.getName(), "Requesting Form using endpoint '/forms/json' with payload '{}'.", petition);
        try {
            parsedPetition = parsePetition(petition);
            Form form = formDao.get(parsedPetition.formName, parsedPetition.getVersion(), parsedPetition.getOrganizationId());
            if (form == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Unknown form with name '" + parsedPetition.getFormName() +
                        "' and version '" + parsedPetition.getVersion() + "' in organization '" + parsedPetition.getOrganizationId() + "'.\"}").build();
            }
            String json = form.toJson();
            WebformsLogger.debug(FormRestService.class.getName(), "Form retrieved successfully:\n{} ", json);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonSyntaxException ex) {
            WebformsLogger.errorMessage(this.getClass().getName(), ex);
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Json syntax error\"}").build();
        }


    }

    private FormDescription parsePetition(String petition) throws JsonSyntaxException {
        if (petition == null || petition.length() == 0) {
            throw new JsonSyntaxException("Empty parameter not allowed.");
        }
        return new Gson().fromJson(petition, FormDescription.class);
    }

    static class FormDescription {
        private String formName;
        private int version;
        private int organizationId;

        public String getFormName() {
            return formName;
        }

        public void setFormName(String formName) {
            this.formName = formName;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(int organizationId) {
            this.organizationId = organizationId;
        }
    }
}
