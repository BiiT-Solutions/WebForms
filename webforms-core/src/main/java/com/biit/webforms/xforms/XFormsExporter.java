package com.biit.webforms.xforms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.utils.date.DateManager;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.xforms.exceptions.DateRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.xml.XmlUtils;

/**
 * Export the form in XForms language to be use with Orbeon. <br>
 * The obtained string will have different sections: <br>
 * - Metadata. Different information about the form and xforms language.<br>
 * - Model. The basic structure of the form. <br>
 * - Binding. Used to link the model with some behavior of the component, such as relevance.<br>
 * - Resources. Defines the visualization for each component: label, hint, help. <br>
 * - Templates. This is for loops only <br>
 * - Body. Relation all previous data. <br>
 */
public class XFormsExporter {
	public final static String APP_NAME = "WebForms";
	private Form form;
	private XFormsHelper xFormsHelper;
	private List<XFormsCategory> xFormsCategories;

	public XFormsExporter(Form form) throws NotValidTreeObjectException, NotValidChildException {
		this.form = form;
		xFormsHelper = new XFormsHelper(form);
		createXFormObjectsStructure();
	}

	private void createXFormObjectsStructure() throws NotValidTreeObjectException, NotValidChildException {
		xFormsCategories = new ArrayList<>();
		for (TreeObject child : form.getChildren()) {
			XFormsCategory xFormsCategory = new XFormsCategory(xFormsHelper, (Category) child);
			xFormsCategories.add(xFormsCategory);
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
	public InputStream generateXFormsLanguage() throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		String xforms = generateXFormsForm();
		try {
			xforms = XmlUtils.format(xforms);
			byte[] data = xforms.getBytes();
			// convert array of bytes into file
			InputStream inputStream = new ByteArrayInputStream(data);
			return inputStream;
		} catch (Exception e) {
			WebformsLogger.errorMessage(XFormsExporter.class.getName(), e);
			saveToFile(xforms);
			throw e;
		}
	}

	private String generateXFormsForm() throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		String xforms = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" " + "xmlns:xf=\"http://www.w3.org/2002/xforms\" "
				+ "xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" "
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns:ev=\"http://www.w3.org/2001/xml-events\" " + "xmlns:xi=\"http://www.w3.org/2001/XInclude\" "
				+ "xmlns:xxi=\"http://orbeon.org/oxf/xml/xinclude\" "
				+ "xmlns:xxf=\"http://orbeon.org/oxf/xml/xforms\" " + "xmlns:exf=\"http://www.exforms.org/exf/1-0\" "
				+ "xmlns:fr=\"http://orbeon.org/oxf/xml/form-runner\" " + "xmlns:saxon=\"http://saxon.sf.net/\" "
				+ "xmlns:sql=\"http://orbeon.org/oxf/xml/sql\"  "
				+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
				+ "xmlns:fb=\"http://orbeon.org/oxf/xml/form-builder\">";
		xforms += getHeader();
		xforms += getBody();
		xforms += "</xh:html>";
		return xforms;
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
	 * Needed tags for attach files to the form (pdf, images, ...).
	 * 
	 * @param form
	 * @return
	 */
	private String getAttachments() {
		String attachment = "<xf:instance id=\"fr-form-attachments\">";
		attachment += "<attachments>";
		attachment += "<css mediatype=\"text/css\" filename=\"\" size=\"\"/>";
		attachment += "<pdf mediatype=\"application/pdf\" filename=\"\" size=\"\"/>";
		attachment += "</attachments>";
		attachment += "</xf:instance>";
		return attachment;
	}

	/**
	 * Bind the model with the presentation of the form.
	 * 
	 * @return
	 * @throws NotExistingDynamicFieldException
	 * @throws InvalidFlowInForm
	 * @throws StringRuleSyntaxError
	 * @throws PostCodeRuleSyntaxError
	 * @throws DateRuleSyntaxError
	 */
	private String getBinding() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		StringBuilder binding = new StringBuilder();
		binding.append("<xf:bind id=\"fr-form-binds\" ref=\"instance('fr-form-instance')\">");

		// Add hidden email field.
		binding.append(XFormsHiddenEmailField.getBinding());

		for (XFormsCategory category : xFormsCategories) {
			category.getBinding(binding);
		}
		binding.append(" </xf:bind>");
		return binding.toString();
	}

	/**
	 * Starts the creation of the header part in a XForm.
	 * 
	 * @param form
	 * @return
	 * @throws NotExistingDynamicFieldException
	 * @throws InvalidFlowInForm
	 * @throws StringRuleSyntaxError
	 * @throws PostCodeRuleSyntaxError
	 * @throws DateRuleSyntaxError
	 */
	private String getHeader() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		String header = "<xh:head>";
		header += "<xh:meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />";
		header += "<xh:title>" + form.getLabel() + "</xh:title>";
		header += "<xf:model id=\"fr-form-model\" xxf:expose-xpath-types=\"true\">";
		header += "<xf:instance xxf:readonly=\"true\" id=\"fr-form-metadata\" xxf:exclude-result-prefixes=\"#all\">";
		header += getMetaData(form);
		header += "</xf:instance>";
		header += getModel();
		header += getBinding();
		header += getAttachments();
		header += getResources();
		header += getInstances();
		header += getTemplatesOfLoops();
		header += "</xf:model>";
		header += "</xh:head>";
		return header;
	}

	private String getTemplatesOfLoops() {
		String templates = "";
		for (XFormsCategory xFormsCategory : xFormsCategories) {
			templates += xFormsCategory.getTemplates();
		}
		return templates;
	}

	/**
	 * Creates the metadata information of the form.
	 * 
	 * @param form
	 * @return
	 */
	public String getMetaData(Form form) {
		String metadata = "<metadata>";

		metadata += "<application-name>" + APP_NAME + "</application-name>";
		metadata += "<form-name>" + formatFormName(form) + "</form-name>";
		metadata += "<title xml:lang=\"en\">" + form.getLabel() + "</title>";
		metadata += "<description xml:lang=\"en\">" + createFormDescription(form) + "</description>";

		metadata += "</metadata>";

		return metadata;
	}

	/**
	 * Removes undesired characters.
	 * 
	 * @param form
	 * @return
	 */
	public static String formatFormName(Form form) {
		return form.getLabel().replace(" ", "_") + "_v" + form.getVersion();
	}

	/**
	 * Basic form information.
	 * 
	 * @param form
	 * @return
	 */
	private static String createFormDescription(Form form) {
		return "Version: " + form.getVersion() + ". Modification date: "
				+ DateManager.convertDateToStringWithHours(form.getUpdateTime()) + ". Publication date: "
				+ DateManager.convertDateToStringWithHours(new Timestamp(new java.util.Date().getTime())) + ".";
	}

	/**
	 * Creates the model section of XForms.
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	private String getModel() {
		String text = "<xf:instance id=\"fr-form-instance\">";
		text += "<form>";

		// Add hidden email field.
		text += XFormsHiddenEmailField.getModel();

		for (XFormsCategory xFormCategory : xFormsCategories) {
			text += xFormCategory.getDefinition();
		}
		text += "</form>";
		text += "</xf:instance>";
		return text;
	}

	/**
	 * Creates all resources of the form (labels initial values, ...).
	 * 
	 * @return
	 * @throws NotExistingDynamicFieldException
	 * @throws InvalidFlowInForm
	 */
	private String getResources() throws NotExistingDynamicFieldException {
		String resource = "<xf:instance id=\"fr-form-resources\" xxf:readonly=\"false\">";
		resource += "<resources>";
		resource += "<resource xml:lang=\"en\">";

		// Add hidden email field.
		resource += XFormsHiddenEmailField.getResources();

		for (XFormsCategory category : xFormsCategories) {
			resource += category.getResources();
		}

		resource += "</resource>";
		resource += "</resources>";
		resource += "</xf:instance>";

		return resource;
	}

	/**
	 * Contains instance data associated with the model element. It is used to supply initial values for forms controls,
	 * or to provide additional data to be submitted with the form.
	 * 
	 * @return
	 * @throws InvalidFlowInForm
	 */
	private String getInstances() {
		String instances = "<xf:instance id=\"fr-service-request-instance\" xxf:exclude-result-prefixes=\"#all\">";
		instances += "<request/>";
		instances += "</xf:instance>";
		instances = "<xf:instance id=\"fr-service-response-instance\" xxf:exclude-result-prefixes=\"#all\">";
		instances += "<response/>";
		instances += "</xf:instance>";
		return instances;
	}

	/**
	 * Creates the body section of the XForm.
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	private String getBody() {
		String body = "<xh:body>";
		body += "<fr:view>";
		body += "<fr:body xmlns:xbl=\"http://www.w3.org/ns/xbl\" "
				+ "xmlns:dataModel=\"java:org.orbeon.oxf.fb.DataModel\" "
				+ "xmlns:oxf=\"http://www.orbeon.com/oxf/processors\" "
				+ "xmlns:p=\"http://www.orbeon.com/oxf/pipeline\" >";

		body += getBodySection();

		body += "</fr:body>";
		body += "</fr:view>";
		body += "</xh:body>";
		return body;
	}

	/**
	 * Shows the sections defined in the header.
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	private String getBodySection() {
		StringBuilder body = new StringBuilder();

		// Add hidden email field.
		body.append(XFormsHiddenEmailField.getBody());

		for (XFormsCategory category : xFormsCategories) {
			category.getSectionBody(body);
		}
		return body.toString();
	}
}
