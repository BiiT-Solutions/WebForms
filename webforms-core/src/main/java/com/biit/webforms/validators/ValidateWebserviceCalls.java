package com.biit.webforms.validators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Webservice;
import com.biit.webforms.persistence.entity.WebserviceCall;
import com.biit.webforms.persistence.entity.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.WebserviceCallLink;
import com.biit.webforms.persistence.entity.WebserviceCallOutputLink;
import com.biit.webforms.persistence.entity.WebserviceValidatedPort;
import com.biit.webforms.validators.reports.WebserviceCallCorruption;
import com.biit.webforms.validators.reports.WebserviceCallInputAfterTrigger;
import com.biit.webforms.validators.reports.WebserviceCallInputNull;
import com.biit.webforms.validators.reports.WebserviceCallOutputAfterTrigger;
import com.biit.webforms.validators.reports.WebserviceCallReferencesUnexistingWebservice;
import com.biit.webforms.validators.reports.WebserviceCallTriggerNull;

/**
 * Validate webservice calls. This validator checks that all webservice calls
 * comply these restrictions.
 * 
 * Webservice call points to an existing webservice (Terminates otherwise)
 * Checks that all input/output ports exist on webservice (Terminates otherwise)
 * 
 * Checks that call trigger is not null Checks input fields are not null Checks
 * that input fields are elements that appear before the trigger or they are the
 * trigger.
 * Checks that output fields are elements after trigger.
 *
 */
public class ValidateWebserviceCalls extends SimpleValidator<Form> {

	private HashMap<String, Webservice> webservices;

	public ValidateWebserviceCalls() {
		super(Form.class);
		setStopOnFail(false);
		webservices = new HashMap<String, Webservice>();
	}

	@Override
	protected void validateImplementation(Form element) {
		if (getExtraData() != null && (getExtraData() instanceof Set<?>)) {
			for (Object object : (Set<?>) getExtraData()) {
				if (object instanceof Webservice) {
					Webservice webservice = (Webservice) object;
					webservices.put(webservice.getName(), webservice);
				}
			}
		}

		Set<WebserviceCall> calls = element.getWebserviceCalls();
		System.out.println("Kiwiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
		for (WebserviceCall call : calls) {
			System.out.println("Call "+call.getName());
			boolean criticFail = false;
			Webservice webservice = webservices.get(call.getWebserviceName());
			if (!checkWebserviceConsistency(call, webservice)) {
				// Skip the rest of the checkings
				continue;
			}

			// Check call trigger and input values are not null
			System.out.println("Trigger "+call.getFormElementTrigger());
			if (call.getFormElementTrigger() == null) {
				assertTrue(false, new WebserviceCallTriggerNull(call));
				criticFail = true;
			}
			for (WebserviceCallLink input : getAllInputLinks(webservice, call)) {
				assertTrue(input.getFormElement() != null, new WebserviceCallInputNull(call, (WebserviceCallInputLink) input));
				if (input.getFormElement() == null) {
					criticFail = true;
				}
			}

			if (criticFail) {
				// Skip the rest of the checkings
				continue;
			}

			// Check order of elements.
			checkOrderOfElements(call);
		}
	}

	private boolean checkWebserviceConsistency(WebserviceCall call, Webservice webservice) {
		// Check webservice still exists
		if (webservice == null) {
			assertTrue(false, new WebserviceCallReferencesUnexistingWebservice(call));
			return false;
		}

		// Check that all the input ports exist on the webservice
		for (WebserviceCallInputLink input : call.getInputLinks()) {
			if (webservice.getInputPort(input.getWebservicePort()) == null) {
				assertTrue(false, new WebserviceCallCorruption(call));
				return false;
			}
		}

		// Check that all the output ports exist on the webservice
		for (WebserviceCallOutputLink output : call.getOutputLinks()) {
			if (webservice.getOutputPort(output.getWebservicePort()) == null) {
				assertTrue(false, new WebserviceCallCorruption(call));
				return false;
			}
		}
		return true;
	}

	private void checkOrderOfElements(WebserviceCall call) {
		boolean failed = false;
		for (WebserviceCallInputLink input : call.getInputLinks()) {
			// All input elements must be before the element trigger.
			if (input.getFormElement().compareTo(call.getFormElementTrigger()) > 0) {
				assertTrue(false, new WebserviceCallInputAfterTrigger(call, input));
				failed = true;
			}
		}
		if (!failed) {
			for (WebserviceCallOutputLink output : call.getOutputLinks()) {
				// All input elements must be before the element trigger.
				if (output.getFormElement().compareTo(call.getFormElementTrigger()) <= 0) {
					assertTrue(false, new WebserviceCallOutputAfterTrigger(call, output));
					failed = true;
				}
			}
		}
	}

	private Set<WebserviceCallLink> getAllInputLinks(Webservice webservice, WebserviceCall webserviceCall) {
		Set<WebserviceValidatedPort> outputPorts = webservice.getInputPorts();
		Set<WebserviceCallInputLink> existentLinks = webserviceCall.getInputLinks();
		Set<WebserviceCallLink> allLinks = new HashSet<>();
		for (WebserviceCallInputLink link : existentLinks) {
			allLinks.add(link);
		}

		for (WebserviceValidatedPort port : outputPorts) {
			boolean exists = false;
			for (WebserviceCallInputLink link : existentLinks) {
				if (link.getWebservicePort().equals(port.getName())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				allLinks.add(new WebserviceCallInputLink(port));
			}
		}
		return allLinks;
	}
}
