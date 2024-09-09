package com.biit.webforms.rest;

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
