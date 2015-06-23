package com.biit.webforms.xforms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.biit.webforms.webservices.Webservice;
import com.biit.webforms.webservices.WebserviceValidatedPort;
import com.biit.webforms.xforms.exceptions.DateRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.xml.XmlUtils;
import com.biit.webforms.xml.XpathToXml;

/**
 * Export the form in a XForms language file to be use with Orbeon. <br>
 * The obtained string will have different sections: <br>
 * - Metadata. Different information about the form and xforms language.<br>
 * - Model. The basic structure of the form. <br>
 * - Binding. Used to link the model with some behavior of the component, such
 * as relevance.<br>
 * - Resources. Defines the visualization for each component: label, hint, help. <br>
 * - Templates. This is for loops only <br>
 * - Body. Relation all previous data. <br>
 */
public class XFormsSimpleFormExporter extends XFormsBasicStructure {

	private Set<Webservice> webservices;

	public XFormsSimpleFormExporter(Form form, Set<Webservice> webservices) throws NotValidTreeObjectException, NotValidChildException {
		super(form);
		this.webservices = new HashSet<>();
		for (Webservice webservice : webservices) {
			this.webservices.add(webservice);
		}
	}

	/**
	 * Creates all XForms tags needed to deploy a form in a Orbeon server.
	 * 
	 * @param form
	 * @return
	 * @throws NotExistingDynamicFieldException
	 * @throws InvalidFlowInForm
	 * @throws StringRuleSyntaxError
	 * @throws PostCodeRuleSyntaxError
	 * @throws DateRuleSyntaxError
	 */
	public InputStream generateXFormsLanguage() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError, UnsupportedEncodingException {
		String xforms = generateXFormsForm();
		try {
			xforms = XmlUtils.format(xforms);
			byte[] data = xforms.getBytes(CHARSET);
			// convert array of bytes into file
			InputStream inputStream = new ByteArrayInputStream(data);
			return inputStream;
		} catch (UnsupportedEncodingException e) {
			WebformsLogger.errorMessage(XFormsSimpleFormExporter.class.getName(), e);
			saveToFile(xforms);
			throw e;
		}
	}

	private String generateXFormsForm() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		StringBuilder xforms = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		xforms.append("<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" " + "xmlns:xf=\"http://www.w3.org/2002/xforms\" ");
		xforms.append("xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" ");
		xforms.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
		xforms.append("xmlns:ev=\"http://www.w3.org/2001/xml-events\" " + "xmlns:xi=\"http://www.w3.org/2001/XInclude\" ");
		xforms.append("xmlns:xxi=\"http://orbeon.org/oxf/xml/xinclude\" ");
		xforms.append("xmlns:xxf=\"http://orbeon.org/oxf/xml/xforms\" xmlns:exf=\"http://www.exforms.org/exf/1-0\" ");
		xforms.append("xmlns:fr=\"http://orbeon.org/oxf/xml/form-runner\" xmlns:saxon=\"http://saxon.sf.net/\" ");
		xforms.append("xmlns:sql=\"http://orbeon.org/oxf/xml/sql\"  ");
		xforms.append("xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" ");
		xforms.append("xmlns:fb=\"http://orbeon.org/oxf/xml/form-builder\">");
		xforms.append(getHeader(null));
		xforms.append(getBody(null));
		xforms.append("</xh:html>");
		return xforms.toString();
	}

	private void saveToFile(String content) {
		FileOutputStream fop = null;
		File file;

		try {

			file = new File(System.getProperty("java.io.tmpdir") + File.separator + "xforms.txt");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates the body section of the XForm.
	 * 
	 * @param xFormsObject
	 * 
	 * @return
	 * @throws InvalidFlowInForm
	 */
	@Override
	protected String getBody(XFormsObject<?> xFormsObject) {
		StringBuilder body = new StringBuilder("<xh:body>");
		body.append("<fr:view>");
		body.append("<fr:body xmlns:xbl=\"http://www.w3.org/ns/xbl\" ");
		body.append("xmlns:dataModel=\"java:org.orbeon.oxf.fb.DataModel\" ");
		body.append("xmlns:oxf=\"http://www.orbeon.com/oxf/processors\" ");
		body.append("xmlns:p=\"http://www.orbeon.com/oxf/pipeline\" >");

		body.append(getBodySection(xFormsObject));

		body.append("</fr:body>");
		body.append("</fr:view>");
		body.append("</xh:body>");
		return body.toString();
	}

	@Override
	protected String getInput() {
		// Not needed, only for multiple files.
		return "";
	}

	/**
	 * Get all elements resources structure.
	 * 
	 * @param xformsObject
	 *            is ignored.
	 */
	@Override
	protected String getElementResources(XFormsObject<?> xformsObject) throws NotExistingDynamicFieldException {
		StringBuilder resource = new StringBuilder();
		// Add hidden email field.
		resource.append(XFormsHiddenEmailField.getResources());

		for (XFormsCategory category : getXFormsCategories()) {
			resource.append(category.getResources());
		}
		return resource.toString();
	}

	/**
	 * @param xformsObject
	 *            ignored in this case.
	 */
	@Override
	protected String getBodySection(XFormsObject<?> xformsObject) {
		StringBuilder body = new StringBuilder();

		// Add hidden email field.
		body.append(XFormsHiddenEmailField.getBody());

		for (XFormsCategory category : getXFormsCategories()) {
			category.getSectionBody(body);
		}
		return body.toString();
	}

	@Override
	protected String getEventsDefinitions(XFormsObject<?> xFormsObject) {
		StringBuilder events = new StringBuilder();
		addVisibilityEvents(events);
		addWebserviceCallEvents(events);
		return events.toString();
	}

	private Webservice getWebservice(WebserviceCall call) {
		for (Webservice webservice : webservices) {
			if (Objects.equals(webservice.getName(), call.getWebserviceName())) {
				return webservice;
			}
		}
		return null;
	}

	/**
	 * Add webservice call action and webservice submission definition.
	 * 
	 * @param events
	 */
	private void addWebserviceCallEvents(StringBuilder events) {
		for (WebserviceCall call : getForm().getWebserviceCalls()) {
			events.append("<!-- Webservice call " + getXFormsHelper().getUniqueName(call) + " -->");
			Webservice webservice = getWebservice(call);
			addWebserviceSubmission(call, webservice, events);
			addWebserviceInstance(call, webservice, events);
			addWebserviceCallEvent(call, webservice, events);
		}
	}

	private void addWebserviceCallEvent(WebserviceCall call, Webservice webservice, StringBuilder events) {
		events.append("<xf:action id=\"webservice-call-" + getXFormsHelper().getUniqueName(call) + "-binding\">");
		events.append("<xf:action ev:event=\"xforms-value-changed xforms-enabled\" ev:observer=\""
				+ getXFormsHelper().getXFormsObject(call.getFormElementTrigger()).getSectionControlName() + "\" if=\"true()\">");
		events.append("<xf:send submission=\"" + getWebserviceName(call, webservice) + "\"/>");
		events.append("</xf:action>");
		events.append("<xf:action ev:event=\"xforms-submit\" ev:observer=\"" + getWebserviceName(call, webservice) + "\">");
		events.append("<xf:var name=\"request-instance-name\" value=\"'" + getWebserviceInstance(call, webservice)
				+ "'\" as=\"xs:string\"/>");
		events.append("<xf:insert ref=\"instance('fr-service-request-instance')\" origin=\"saxon:parse(instance($request-instance-name))\"/>");
		events.append("<xf:action context=\"instance('fr-service-request-instance')\">");

		// For each input we need to copy values
		for (WebserviceCallInputLink inputLink : call.getInputLinks()) {
			events.append("<xf:action class=\"fr-set-service-value-action\">");
			events.append("<xf:var name=\"control-name\" value=\"'" + getXFormsHelper().getUniqueName(inputLink.getFormElement()) + "'\"/>");
			events.append("<xf:var name=\"path\" value=\"" + webservice.findInputPort(inputLink).getXpath() + "\"/>");
			events.append("</xf:action>");
		}

		events.append("</xf:action>");
		events.append("</xf:action>");

		// Response actions
		events.append("<xf:action ev:event=\"xforms-submit-done\" ev:observer=\"" + getWebserviceName(call, webservice)
				+ "\" context=\"instance('fr-service-response-instance')\">");
		for (WebserviceCallOutputLink outputLink : call.getOutputLinks()) {
			events.append("<xf:action class=\"fr-set-control-value-action\">");
			events.append("<xf:var name=\"control-name\" value=\"'" + getXFormsHelper().getUniqueName(outputLink.getFormElement())
					+ "'\"/>");
			events.append("<xf:var name=\"control-value\" value=\"" + webservice.findOutputPort(outputLink).getXpath() + "\"/>");
			events.append("</xf:action>");
		}
		for (WebserviceCallInputLink inputLink : call.getInputLinks()) {
			if (inputLink.getValidationXpath() != null) {
				events.append("<xf:insert ref=\"instance('" + getXFormsHelper().getUniqueName(call) + "-" + inputLink.getWebservicePort()
						+ "-instance')\" origin=\"instance('fr-service-response-instance')\"/>");
			}
		}
		events.append("</xf:action>");
		events.append("</xf:action>");
	}

	private String getWebserviceInstance(WebserviceCall call, Webservice webservice) {
		return getXFormsHelper().getUniqueName(call) + "-" + webservice.getName() + "-instance";
	}

	private String getWebserviceName(WebserviceCall call, Webservice webservice) {
		return getXFormsHelper().getUniqueName(call) + "-" + webservice.getName() + "-submission";
	}

	/**
	 * Adds webservice submission which holds the webservice configuration
	 * values.
	 * 
	 * @param call
	 * 
	 * @param call
	 * @param events
	 */
	private void addWebserviceSubmission(WebserviceCall call, Webservice webservice, StringBuilder events) {
		events.append("<xf:submission id=\""
				+ getWebserviceName(call, webservice)
				+ "\" class=\"fr-service\" ref=\"instance('fr-service-request-instance')\" resource=\""
				+ webservice.getUrl()
				+ "\" method=\"post\" serialization=\"application/xml\" mediatype=\"application/xml\" replace=\"instance\" instance=\"fr-service-response-instance\"/>");
	}

	/**
	 * ADds webservice instance which contains the empty xml structure that will
	 * be sent to the webservice.
	 * 
	 * @param call
	 * 
	 * @param webservice
	 * @param events
	 */
	private void addWebserviceInstance(WebserviceCall call, Webservice webservice, StringBuilder events) {
		events.append("<xf:instance id=\"" + getWebserviceInstance(call, webservice)
				+ "\" class=\"fr-service\" xxf:exclude-result-prefixes=\"#all\">");
		events.append("<body xmlns:secure=\"java:org.orbeon.oxf.util.SecureUtils\" xmlns:frf=\"java:org.orbeon.oxf.fr.FormRunner\" xmlns:p=\"http://www.orbeon.com/oxf/pipeline\" xmlns:fbf=\"java:org.orbeon.oxf.fb.FormBuilder\">");
		events.append(generateCodifiedXml(webservice));
		events.append("</body>");
		events.append("</xf:instance>");
	}

	private String generateCodifiedXml(Webservice webservice) {
		XpathToXml conversor = new XpathToXml();

		for (WebserviceValidatedPort inputPort : webservice.getInputPorts()) {
			conversor.addXpath(inputPort.getXpath());
		}
		return conversor.generateCodifiedXml();
	}

	/**
	 * Create the events needed to hide an element that depends on a previous
	 * element visibility.
	 * 
	 * @param events
	 */
	private void addVisibilityEvents(StringBuilder events) {
		addVisibilityStructure(events);
		addVisibilityObservers(events);
	}

	private void addVisibilityStructure(StringBuilder events) {

		events.append("<!-- Keep track of visible/hidden status -->");
		events.append("<xf:instance id=\"visible\">");
		events.append("<var>");

		for (XFormsCategory xFormCategory : getXFormsCategories()) {
			events.append(xFormCategory.getVisibilityStructure());
		}

		events.append("</var>");
		events.append("</xf:instance>");
	}

	private void addVisibilityObservers(StringBuilder events) {
		for (TreeObject question : getForm().getAllNotHiddenChildrenInHierarchy(BaseQuestion.class)) {
			// Element visibility.
			events.append("<!-- Change the visibility status for '" + question + "'. -->");
			String elementName = getXFormsHelper().getUniqueName(question);
			String controlName = getXFormsHelper().getXFormsObject(question).getSectionControlName();
			events.append("<xf:setvalue event=\"xforms-enabled\" observer=\"" + controlName + "\" ref=\"instance('visible')/" + elementName
					+ "\" value=\"'true'\"/>");
			events.append("<xf:setvalue event=\"xforms-disabled\" observer=\"" + controlName + "\" ref=\"instance('visible')/"
					+ elementName + "\" value=\"'false'\"/>");

			// Category visibility.
			events.append("<!-- Update category showed elements count for '" + question.getName() + "'. -->");
			String catId = getXFormsHelper().getUniqueName(question.getAncestor(Category.class));
			events.append("<xf:setvalue event=\"xforms-enabled\" observer=\"" + controlName + "\" ref=\"instance('visible')/" + catId
					+ "\" value=\"instance('visible')/" + catId + " + 1\"/>");
			events.append("<xf:setvalue event=\"xforms-disabled\" observer=\"" + controlName + "\" ref=\"instance('visible')/" + catId
					+ "\" value=\"instance('visible')/" + catId + " - 1\"/>");
		}
	}

	/**
	 * @param xformsObject
	 *            ignored in this case.
	 */
	@Override
	protected String getElementBinding(XFormsObject<?> xformsObject) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		StringBuilder binding = new StringBuilder();
		// Add hidden email field.
		binding.append(XFormsHiddenEmailField.getBinding());

		for (XFormsCategory category : getXFormsCategories()) {
			category.getBinding(binding);
		}

		return binding.toString();
	}

	/**
	 * Creates the model section of XForms. But also adds the webservice
	 * validation members
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	@Override
	protected String getModelInstance() {
		StringBuilder text = new StringBuilder(super.getModelInstance());

		for (WebserviceCall call : getForm().getWebserviceCalls()) {
			for (WebserviceCallInputLink inputLink : call.getInputLinks()) {
				text.append("<xf:instance id=\"" + getXFormsHelper().getUniqueName(call) + "-" + inputLink.getWebservicePort()
						+ "-instance\">");
				text.append("<valid/>");
				text.append("</xf:instance>");
			}
		}

		return text.toString();
	}
}
