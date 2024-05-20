package com.biit.webforms.webservice.rest.client;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.logger.WebformsLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.net.ssl.SSLContext;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbcdRestClient {

    private static final String PARAMETER_NAME = "parameter";

    public static List<SimpleFormView> getSimpleFormViewsFromAbcdByUserEmail(String url, String path, String userEmail) {
        if (userEmail != null) {
            return getSimpleFormViewsFromAbcd(url, path, userEmail);
        } else {
            return null;
        }
    }

    public static List<SimpleFormView> getSimpleFormViewsFromAbcdByLabelAndOrganization(String url, String path,
                                                                                        String userEmail, String formLabel, Long organization) {
        if ((userEmail != null) && (formLabel != null) && (organization != null)) {
            return getSimpleFormViewsFromAbcd(url, path, userEmail, formLabel, organization.toString());
        } else {
            return null;
        }
    }

    public static Form getFormFromAbcdByLabelOrganizationAndVersion(String url, String path, String userEmail,
                                                                    String formLabel, Long formOrganization, Integer formVersion) {
        if ((userEmail != null) && (formLabel != null) && (formOrganization != null) && (formVersion != null)) {
            return getFormFromAbcd(url, path, userEmail, formLabel, formOrganization.toString(), formVersion.toString());
        } else {
            return null;
        }
    }

    public static Form getFormFromAbcdById(String url, String path, String userEmail, Long formId) {
        if ((userEmail != null) && (formId != null)) {
            return getFormFromAbcd(url, path, userEmail, formId.toString());
        } else {
            return null;
        }
    }

    public static List<Form> getFormsFromAbcdByOrganization(String url, String path, String userEmail,
                                                            Long formOrganization) {
        if ((userEmail != null) && (formOrganization != null)) {
            return getFormsFromAbcd(url, path, userEmail, formOrganization.toString());
        } else {
            return null;
        }
    }

    private static List<SimpleFormView> getSimpleFormViewsFromAbcd(String url, String path, String... parameters) {
        boolean ssl = url.startsWith("https");
        try {
            String simpleFormsJson = get(ssl, url, path, MediaType.APPLICATION_JSON, parameters);
            if (simpleFormsJson != null) {
                final SimpleFormView[] simpleFormsDeserialized = ObjectMapperFactory.getObjectMapper().readValue(simpleFormsJson, com.biit.abcd.persistence.entity.SimpleFormView[].class);
                return Arrays.asList(simpleFormsDeserialized);
            } else {
                // Return an empty list to avoid possible failures
                return new ArrayList<>();
            }
        } catch (InternalServerErrorException | JsonProcessingException e) {
            WebformsLogger.errorMessage(AbcdRestClient.class.getName(), e);
            return new ArrayList<>();
        }
    }

    private static Form getFormFromAbcd(String url, String path, String... parameters) {
        boolean ssl = url.startsWith("https");
        try {
            String simpleFormsJson = get(ssl, url, path, MediaType.APPLICATION_JSON, parameters);
            if (simpleFormsJson != null) {
                try {
                    return Form.fromJson(simpleFormsJson);
                } catch (JsonProcessingException e) {
                    WebformsLogger.errorMessage(AbcdRestClient.class.getName(), e);
                    return null;
                }
            }
        } catch (InternalServerErrorException e) {
            WebformsLogger.errorMessage(AbcdRestClient.class.getName(), e);
            return null;
        }
        return null;
    }

    private static List<Form> getFormsFromAbcd(String url, String path, String... parameters) {
        boolean ssl = url.startsWith("https");
        try {
            String simpleFormsJson = get(ssl, url, path, MediaType.APPLICATION_JSON, parameters);
            if (simpleFormsJson != null) {
                try {
                    return Arrays.asList(Form.fromJsonList(simpleFormsJson));
                } catch (JsonProcessingException e) {
                    WebformsLogger.errorMessage(AbcdRestClient.class.getName(), e);
                    return null;
                }
            }
        } catch (InternalServerErrorException e) {
            WebformsLogger.errorMessage(AbcdRestClient.class.getName(), e);
            return null;
        }
        return null;
    }

    private static String get(boolean ssl, String target, String path, String requestType, String... parameters) {
        String responseString = null;
        WebformsLogger.debug(AbcdRestClient.class.getName(), "Calling rest service '" + target + "/" + path
                + "' and parameters " + Arrays.toString(parameters));

        HttpAuthenticationFeature authenticationFeature = HttpAuthenticationFeature.basic(WebformsConfigurationReader
                .getInstance().getAbcdRestServiceUser(), WebformsConfigurationReader.getInstance()
                .getAbcdRestServicePassword());
        Response response = null;
        if (ssl) {
            SSLContext sslContext = SslConfigurator.newInstance(true).createSSLContext();
            response = ClientBuilder.newBuilder().sslContext(sslContext).build()
                    .target(UriBuilder.fromUri(target).build()).path(path)
                    .queryParam(PARAMETER_NAME, (Object[]) parameters).register(authenticationFeature)
                    .request(requestType).get();
        } else {
            response = ClientBuilder.newBuilder().build().target(UriBuilder.fromUri(target).build()).path(path)
                    .queryParam(PARAMETER_NAME, (Object[]) parameters).register(authenticationFeature)
                    .request(requestType).get(Response.class);
        }
        if (response.getStatusInfo().toString().equals(Response.Status.OK.toString())) {
            responseString = response.readEntity(String.class);
        }
        WebformsLogger.debug(AbcdRestClient.class.getName(), "Service returns " + responseString);
        return responseString;
    }
}
