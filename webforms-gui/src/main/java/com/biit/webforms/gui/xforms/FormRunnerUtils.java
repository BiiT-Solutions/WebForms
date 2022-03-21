package com.biit.webforms.gui.xforms;

import com.biit.usermanager.entity.IGroup;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.persistence.entity.Form;
import org.apache.http.client.ClientProtocolException;
import org.glassfish.jersey.SslConfigurator;

import javax.net.ssl.SSLContext;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class FormRunnerUtils {

	/**
	 * Makes a post to the formrunner server to store the form.
	 * 
	 * @param form
	 */
	public static boolean saveFormInFormRunner(Form form, IGroup<Long> organization, boolean preview,
			boolean includeImages) {
		// Save it.
		try {
			if (preview) {
				savePreviewForm(form);
			} else {
				saveForm(form);
			}
		} catch (ClientProtocolException e) {
			// MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			WebformsUiLogger.errorMessage(FormRunnerUtils.class.getName(), e);
			return false;
		} catch (IOException e) {
			// MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			WebformsUiLogger.errorMessage(FormRunnerUtils.class.getName(), e);
			return false;
		}
		return true;
	}

	private static void savePreviewForm(Form form) throws ClientProtocolException, IOException {
		String jsonForm = form.toJson();
		String url = WebformsConfigurationReader.getInstance().getFormrunnerRestUrl();

		String service = "/forms/preview";
		String message = "{\"json\":" + jsonForm + "}";
		WebformsUiLogger.info(FormRunnerUtils.class.getName(), "URL to save in preview: " + url + service);
		WebformsUiLogger.debug(FormRunnerUtils.class.getName(), "PUBLISHING PREVIEW:\n" + jsonForm);
		String response = post(false, url, service, message, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
				false, null);
		WebformsUiLogger.info(FormRunnerUtils.class.getName(), "Response from the server: " + response);
	}

	private static void saveForm(Form form) throws ClientProtocolException, IOException {
		String jsonForm = form.toJson();
		String url = WebformsConfigurationReader.getInstance().getFormrunnerRestUrl();

		String service = "/forms";
		String message = "{\"json\":" + jsonForm + "}";
		WebformsUiLogger.info(FormRunnerUtils.class.getName(), "URL to save in published: " + url + service);
		WebformsUiLogger.debug(FormRunnerUtils.class.getName(), "PUBLISHING FORM:\n" + jsonForm);
		String response = post(false, url, service, message, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
				false, null);
		WebformsUiLogger.info(FormRunnerUtils.class.getName(), "Response from the server: " + response);
	}

	public static String post(boolean ssl, String target, String path, String message, String requestType,
			String messageType, boolean authentication, Map<String, Object> parameters) // throws
																						// UnprocessableEntityException,
																						// EmptyResultException
	{

		// HttpAuthenticationFeature authenticationFeature = null;
		if (authentication) {
			// authenticationFeature =
			// HttpAuthenticationFeature.basic(UsmoConfigurationReader.getInstance().getRestServiceUser(),
			// UsmoConfigurationReader.getInstance()
			// .getRestServicePassword());
		}

		String response = null;
		// RestClientLogger.debug(RestGenericClient.class.getName(), "Calling rest
		// service (post) '" + target + "/" + path + "' with message:\n '" + message +
		// "'.");
		try {
			ClientBuilder builder = ClientBuilder.newBuilder();

			// Https
			if (ssl) {
				SSLContext sslContext = SslConfigurator.newInstance(true).createSSLContext();
				builder = builder.sslContext(sslContext);
			}

			// Enable authentication
			/*
			 * if (authentication && authenticationFeature != null) { builder =
			 * builder.register(authenticationFeature); }
			 */

			// Add Parameters
			WebTarget webTarget = builder.build().target(UriBuilder.fromUri(target).build()).path(path);
			WebformsUiLogger.info("FormRunnerUtils", webTarget.toString());
			if (parameters != null && !parameters.isEmpty()) {
				for (Entry<String, Object> record : parameters.entrySet()) {
					webTarget = webTarget.queryParam(record.getKey(), record.getValue());
				}
			}

			// Call the webservice
			response = webTarget.request(requestType).post(Entity.entity(message, messageType), String.class);

			// RestClientLogger.debug(RestGenericClient.class.getName(), "Service returns '"
			// + response + "'.");
			return response;
		} catch (Exception e) {
			if (e instanceof ClientErrorException) {
				if (e.getMessage().contains("HTTP 422")) {
					// UnprocessableEntityException uee = new
					// UnprocessableEntityException(e.getMessage());
					// uee.setStackTrace(e.getStackTrace());
					// throw uee;
				} else if (e.getMessage().contains("HTTP 406")) {
					// EmptyResultException uee = new EmptyResultException(e.getMessage());
					// uee.setStackTrace(e.getStackTrace());
					// throw uee;
				}
			}
			// RestClientLogger.severe(RestGenericClient.class.getName(), "Calling rest
			// service '" + target + "/" + path + "' with message:\n '" + message + "'
			// error!");
			// RestClientLogger.errorMessage(RestGenericClient.class.getName(), e);
		}
		return "";
	}

}
