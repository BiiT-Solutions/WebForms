package com.biit.webforms.webservice.rest.client;

import javax.net.ssl.SSLContext;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Client to make a call to a OrbeonDrools project webservice.
 *
 */
public class AbcdRestClient {

	public static String getSimpleFormViewsFromAbcd(String url, String path, String message) {
		boolean ssl = url.startsWith("https");

		try {
			return post(ssl, url, path, message, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		} catch (InternalServerErrorException e) {
			WebformsLogger.errorMessage(AbcdRestClient.class.getName(), e);
			return null;
		}
	}
	
	public static String getFormsFromAbcd(String url, String path, String message) {
		boolean ssl = url.startsWith("https");

		try {
			return post(ssl, url, path, message, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		} catch (InternalServerErrorException e) {
			WebformsLogger.errorMessage(AbcdRestClient.class.getName(), e);
			return null;
		}
	}

	public static String post(boolean ssl, String target, String path, String message, String requestType,
			String messageType) {
		String response = null;
		WebformsLogger.debug(AbcdRestClient.class.getName(), "Calling rest service '" + target + "/" + path
				+ "' with message " + message);

		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(WebformsConfigurationReader.getInstance()
				.getAbcdRestServiceUser(), WebformsConfigurationReader.getInstance().getAbcdRestServicePassword());

		if (ssl) {
			SSLContext sslContext = SslConfigurator.newInstance(true).createSSLContext();
			response = ClientBuilder.newBuilder().sslContext(sslContext).build()
					.target(UriBuilder.fromUri(target).build()).path(path).register(feature).request(requestType)
					.post(Entity.entity(message, messageType), String.class);
		} else {
			response = ClientBuilder.newBuilder().build().target(UriBuilder.fromUri(target).build()).path(path)
					.register(feature).request(requestType).post(Entity.entity(message, messageType), String.class);
		}
		WebformsLogger.debug(AbcdRestClient.class.getName(), "Service returns " + response);
		return response;
	}

}
