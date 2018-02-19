package com.biit.webforms.exporters.xforms;

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
import com.biit.usermanager.entity.IGroup;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.exporters.xforms.exceptions.DateRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.biit.webforms.webservices.Webservice;
import com.biit.webforms.webservices.WebserviceValidatedPort;
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

	public XFormsSimpleFormExporter(Form form, IGroup<Long> organization, Set<Webservice> webservices, boolean includeImages, boolean previewMode)
			throws NotValidTreeObjectException, NotValidChildException {
		super(form, organization);
		setImagesEnabled(includeImages);
		setPreviewMode(previewMode);
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
	public InputStream generateXFormsLanguage() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError,
			UnsupportedEncodingException {
		String xforms = generateXFormsForm();
		try {
			xforms = XmlUtils.format(xforms);
			byte[] data = xforms.getBytes(CHARSET);
			// convert array of bytes into file
			InputStream inputStream = new ByteArrayInputStream(data);
			return inputStream;
		} catch (Exception e) {
			WebformsLogger.errorMessage(XFormsSimpleFormExporter.class.getName(), e);
			saveToFile(xforms);
			throw e;
		}
	}

	private String generateXFormsForm() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
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

		// Add form image information
		if (getXFormsHelper().isImagesEnabled() && getForm().getImage() != null) {
			XFormsImage.getBody(null, getForm().getImage(), body, getXFormsHelper());
		}

		if (WebformsConfigurationReader.getInstance().isXFormsCustomWizardEnabled()) {
			setCategoriesMenu(body);
			body.append("<xh:div class=\"webforms-category-components\" id=\"webforms-category-components\">");
		}

		body.append(getBodySection(xFormsObject));

		if (WebformsConfigurationReader.getInstance().isXFormsCustomWizardEnabled()) {
			body.append("</xh:div>");
			// Script that manages the previous/next buttons of the runner
			body.append("<script type=\"text/javascript\" src=\"/orbeon/forms/assets/categories-menu.js\"/>");

		}
		// Script that manages the error position in the form runner
		body.append("<script type=\"text/javascript\" src=\"/orbeon/forms/assets/error-position-manager.js\"/>");
		body.append("</fr:body>");
		if (WebformsConfigurationReader.getInstance().isXFormsCustomWizardEnabled()) {
			createFormRunnerButtons(body);
		}
		body.append("</fr:view>");
		body.append("</xh:body>");
		return body.toString();
	}

	private void createFormRunnerButtons(StringBuilder body) {
		body.append("<fr:buttons xmlns:oxf=\"http://www.orbeon.com/oxf/processors\"");
		body.append(" xmlns:p=\"http://www.orbeon.com/oxf/pipeline\"");
		body.append(" xmlns:xbl=\"http://www.w3.org/ns/xbl\">");
		body.append("<xf:trigger id=\"previous-button\">");
		body.append("<xf:label>Previous</xf:label>");
		body.append("<xf:action ev:event=\"DOMActivate\">");
		body.append("<xf:load resource=\"javascript:clickPreviousCategory()\"/>");
		body.append("</xf:action>");
		body.append("</xf:trigger>");
		body.append("<xf:trigger id=\"next-button\">");
		body.append("<xf:label>Next</xf:label>");
		body.append("<xf:action ev:event=\"DOMActivate\">");
		body.append("<xf:load resource=\"javascript:clickNextCategory()\"/>");
		body.append("</xf:action>");
		body.append("</xf:trigger>");
		body.append("<fr:save-draft-button/>");
		body.append("<fr:submit-button/>");
		body.append("</fr:buttons>");
	}

	/**
	 * Defines the categories menu browser on top of the form
	 * 
	 * @param body
	 */
	private void setCategoriesMenu(StringBuilder body) {
		body.append("<xh:div id=\"categories_menu\" class=\"categories-menu\">");
		body.append("<xh:ul class=\"webforms-list-no-bullets\">");

		for (XFormsCategory category : getXFormsCategories()) {
			body.append("<xh:li class=\"webforms-list-no-bullets-element {if(instance('show-category')/" + category.getUniqueName()
					+ "='true') then 'webforms-list-no-bullets-element-selected' else ''}\">");
			body.append("<xf:trigger appearance=\"minimal\" class=\"{if(instance('category-menu-button-active')/" + category.getUniqueName()
					+ "='false') then 'category-button-disabled' else ''}\">");
			body.append("<xf:label>" + category.getSource().getLabel() + "</xf:label>");
			body.append("<xf:action ev:event=\"DOMActivate\">");

			for (XFormsCategory categoryAux : getXFormsCategories()) {
				if (categoryAux.equals(category)) {
					body.append("<xf:setvalue ref=\"instance('show-category')/" + categoryAux.getUniqueName() + "\">true</xf:setvalue>");
				} else {
					body.append("<xf:setvalue ref=\"instance('show-category')/" + categoryAux.getUniqueName() + "\">false</xf:setvalue>");
				}
			}
			body.append("<xf:load resource=\"javascript:enableDisablePreviousNextButtons()\"/>");
			body.append("</xf:action>");
			body.append("</xf:trigger>");
			body.append("</xh:li>");
		}

		body.append("</xh:ul>");
		body.append("</xh:div>");
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
	protected String getElementResources(XFormsObject<?> xformsObject, OrbeonLanguage language) throws NotExistingDynamicFieldException {
		StringBuilder resource = new StringBuilder();
		// Add hidden email field.
		resource.append(XFormsHiddenEmailField.getResources());

		for (XFormsCategory category : getXFormsCategories()) {
			resource.append(category.getResources(language));
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
		if (WebformsConfigurationReader.getInstance().isXFormsCustomWizardEnabled()) {
			addCategoryMenuEvents(events);
		}
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
		events.append("<xf:var name=\"request-instance-name\" value=\"'" + getWebserviceInstance(call, webservice) + "'\" as=\"xs:string\"/>");
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
			events.append("<xf:var name=\"control-name\" value=\"'" + getXFormsHelper().getUniqueName(outputLink.getFormElement()) + "'\"/>");
			events.append("<xf:var name=\"control-value\" value=\"" + webservice.findOutputPort(outputLink).getXpath() + "\"/>");
			events.append("</xf:action>");
		}

		for (WebserviceCallInputLink inputLink : call.getInputLinks()) {
			events.append("<xf:action class=\"fr-set-control-value-action\">");
			events.append("<xf:var name=\"control-name\" value=\"'"
					+ getXFormsHelper().getWebserviceValidationField(inputLink.getFormElement()).getUniqueName() + "'\"/>");
			events.append("<xf:var name=\"control-value\" value=\"" + inputLink.getValidationXpath() + "\"/>");
			events.append("</xf:action>");
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
	 * Adds webservice instance which contains the empty xml structure that will
	 * be sent to the webservice.
	 * 
	 * @param call
	 * 
	 * @param webservice
	 * @param events
	 */
	private void addWebserviceInstance(WebserviceCall call, Webservice webservice, StringBuilder events) {
		events.append("<xf:instance id=\"" + getWebserviceInstance(call, webservice) + "\" class=\"fr-service\" xxf:exclude-result-prefixes=\"#all\">");
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
			// System Fields are always hidden!!
			if (!(question instanceof SystemField)) {
				// Element visibility.
				events.append("<!-- Change the visibility status for '" + question + "'. -->");
				String elementName = getXFormsHelper().getUniqueName(question);
				String controlName = getXFormsHelper().getXFormsObject(question).getSectionControlName();
				events.append("<xf:setvalue event=\"xforms-enabled\" observer=\"" + controlName + "\" ref=\"instance('visible')/" + elementName
						+ "\" value=\"'true'\"/>");
				events.append("<xf:setvalue event=\"xforms-disabled\" observer=\"" + controlName + "\" ref=\"instance('visible')/" + elementName
						+ "\" value=\"'false'\"/>");

				// Category visibility.
				events.append("<!-- Update category showed elements count for '" + question.getName() + "'. -->");
				String catId = getXFormsHelper().getUniqueName(question.getAncestor(Category.class));
				events.append("<xf:setvalue event=\"xforms-enabled\" observer=\"" + controlName + "\" ref=\"instance('visible')/" + catId
						+ "\" value=\"instance('visible')/" + catId + " + 1\"/>");
				events.append("<xf:setvalue event=\"xforms-disabled\" observer=\"" + controlName + "\" ref=\"instance('visible')/" + catId
						+ "\" value=\"instance('visible')/" + catId + " - 1\"/>");

				// Reset value if is hidden.
				events.append("<xf:setvalue event=\"xforms-disabled\" observer=\"" + controlName + "\" ref=\"instance('fr-form-instance')/"
						+ question.getPathName() + "\" value=\"''\"/>");
			}
		}
	}

	private void addCategoryMenuEvents(StringBuilder events) {
		addCategoryMenuVisibilityStructure(events);
		addCategoryMenuVisibilityEvents(events);
	}

	private void addCategoryMenuVisibilityStructure(StringBuilder events) {
		events.append("<!-- Keeps track of category menu buttons status -->");
		events.append("<xf:instance id=\"category-menu-button-active\">");
		events.append("<category-menu-button-active>");

		for (int index = 0; index < getXFormsCategories().size(); index++) {
			XFormsCategory xFormCategory = getXFormsCategories().get(index);
			events.append("<" + xFormCategory.getUniqueName() + ">");
			if (index == 0) {
				events.append("true");
			} else {
				events.append("false");
			}
			events.append("</" + xFormCategory.getUniqueName() + ">");
		}
		events.append("</category-menu-button-active>");
		events.append("</xf:instance>");

		events.append("<!-- Keeps track of the category that should be shown based on the categroy menu buttons -->");
		events.append("<xf:instance id=\"show-category\">");
		events.append("<show-category>");

		for (int index = 0; index < getXFormsCategories().size(); index++) {
			XFormsCategory xFormCategory = getXFormsCategories().get(index);
			events.append("<" + xFormCategory.getUniqueName() + ">");
			if (index == 0) {
				events.append("true");
			} else {
				events.append("false");
			}
			events.append("</" + xFormCategory.getUniqueName() + ">");
		}
		events.append("</show-category>");
		events.append("</xf:instance>");
	}

	private void addCategoryMenuVisibilityEvents(StringBuilder events) {
		events.append("<!-- Through these events the category menu buttons are enabled/disabled -->");

		events.append("<xf:action ev:event=\"xforms-enabled xforms-disabled\"");
		events.append(" ev:observer=\"");
		for (XFormsCategory category : getXFormsCategories()) {
			events.append(category.getUniqueName() + "-control ");
		}
		events.deleteCharAt(events.length() - 1).append("\"> <xf:load resource=\"javascript:enableDisablePreviousNextButtons()\"/>");
		events.append("</xf:action>");

		for (XFormsCategory category : getXFormsCategories()) {
			// Enable category button event
			events.append("<xf:action ev:event=\"xforms-enabled\" ev:observer=\"" + category.getUniqueName() + "-control\">");
			events.append("<xf:setvalue ref=\"instance('category-menu-button-active')/" + category.getUniqueName() + "\">true</xf:setvalue>");
			events.append("</xf:action>");
			// Disable category button event
			events.append("<xf:action ev:event=\"xforms-disabled\" ev:observer=\"" + category.getUniqueName() + "-control\" if=\"instance('visible')/"
					+ category.getUniqueName() + "=0\">");
			events.append("<xf:setvalue ref=\"instance('category-menu-button-active')/" + category.getUniqueName() + "\">false</xf:setvalue>");
			events.append("</xf:action>");
		}
	}

	/**
	 * @param xformsObject
	 *            ignored in this case.
	 */
	@Override
	protected String getElementBinding(XFormsObject<?> xformsObject) throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
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
		return text.toString();
	}
}
