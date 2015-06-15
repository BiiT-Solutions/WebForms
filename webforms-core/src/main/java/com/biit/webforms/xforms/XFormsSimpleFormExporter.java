package com.biit.webforms.xforms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.xforms.exceptions.DateRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.xml.XmlUtils;

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

	public XFormsSimpleFormExporter(Form form) throws NotValidTreeObjectException, NotValidChildException {
		super(form);
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
			StringRuleSyntaxError, PostCodeRuleSyntaxError, UnsupportedEncodingException {
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

	private String generateXFormsForm() throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		StringBuilder xforms = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		xforms.append("<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" "
				+ "xmlns:xf=\"http://www.w3.org/2002/xforms\" ");
		xforms.append("xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" ");
		xforms.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
		xforms.append("xmlns:ev=\"http://www.w3.org/2001/xml-events\" "
				+ "xmlns:xi=\"http://www.w3.org/2001/XInclude\" ");
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

		setCategoriesMenu(body);
		body.append(getBodySection(xFormsObject));

		body.append("</fr:body>");
		body.append("</fr:view>");
		body.append("</xh:body>");
		return body.toString();
	}

	/**
	 * Defines the categories menu browser on top of the form
	 * 
	 * @param body
	 */
	private void setCategoriesMenu(StringBuilder body) {
		body.append("<xh:div id='categories_menu'>");
		body.append("<xh:table>");
		body.append("<xh:tr>");

		for (XFormsCategory category : getXFormsCategories()) {
			body.append("<xh:td>");
			body.append("<xf:trigger appearance=\"minimal\">");
			body.append("<xf:label>" + category.getUniqueName() + "</xf:label>");
			body.append("</xf:trigger>");
			body.append("</xh:td>");
		}

		body.append("</xh:tr>");
		body.append("</xh:table>");
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
		addCategoryMenuEvents(events);
		return events.toString();
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
		for (Flow flow : getXFormsHelper().getDefaultFlows()) {
			// Element visibility.
			events.append("<!-- Change the visibility status for '" + flow.getOrigin().getName() + "'. -->");
			String elementName = getXFormsHelper().getUniqueName(flow.getOrigin());
			String controlName = getXFormsHelper().getXFormsObject(flow.getOrigin()).getSectionControlName();
			events.append("<xf:setvalue event=\"xforms-enabled\" observer=\"" + controlName
					+ "\" ref=\"instance('visible')/" + elementName + "\" value=\"'true'\"/>");
			events.append("<xf:setvalue event=\"xforms-disabled\" observer=\"" + controlName
					+ "\" ref=\"instance('visible')/" + elementName + "\" value=\"'false'\"/>");

			// Category visibility.
			events.append("<!-- Update category showed elements count for '" + flow.getOrigin().getName() + "'. -->");
			String catId = getXFormsHelper().getUniqueName(flow.getOrigin().getAncestor(Category.class));
			events.append("<xf:setvalue event=\"xforms-enabled\" observer=\"" + controlName
					+ "\" ref=\"instance('visible')/" + catId + "\" value=\"instance('visible')/" + catId + " + 1\"/>");
			events.append("<xf:setvalue event=\"xforms-disabled\" observer=\"" + controlName
					+ "\" ref=\"instance('visible')/" + catId + "\" value=\"instance('visible')/" + catId + " - 1\"/>");
		}
	}

	private void addCategoryMenuEvents(StringBuilder events) {
		addCategoryMenuVisibilityStructure(events);
		//addHideCategoryObservers(events);
	}

	private void addCategoryMenuVisibilityStructure(StringBuilder events) {
		events.append("<!-- Keep track of category menu visible/hidden status -->");
		events.append("<xf:instance id=\"category-menu-visibility\">");
		events.append("<var>");

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
		events.append("</var>");
		events.append("</xf:instance>");
	}

	/**
	 * @param xformsObject
	 *            ignored in this case.
	 */
	@Override
	protected String getElementBinding(XFormsObject<?> xformsObject) throws NotExistingDynamicFieldException,
			InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		StringBuilder binding = new StringBuilder();
		// Add hidden email field.
		binding.append(XFormsHiddenEmailField.getBinding());

		for (XFormsCategory category : getXFormsCategories()) {
			category.getBinding(binding);
		}

		return binding.toString();
	}

}
