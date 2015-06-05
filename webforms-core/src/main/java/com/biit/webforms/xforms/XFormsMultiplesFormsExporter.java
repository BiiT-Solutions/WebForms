package com.biit.webforms.xforms;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.xml.XmlUtils;

/**
 * Export the form in XForms language using each category as an independent file to be use with Orbeon. <br>
 * Can create different strings: <br>
 * - initial instance. Basic structure of the form and the default values of the fields.<br>
 * - Page Flow. Relationship among all files. <br>
 * - StepN-categoryName. A category exported in an independent file. .<br>
 */
public class XFormsMultiplesFormsExporter extends XFormsBasicStructure {
	private final static String DEFAULT_SUBMISSION = "initial-instance.xml";

	public XFormsMultiplesFormsExporter(Form form) throws NotValidTreeObjectException, NotValidChildException {
		super(form);
	}

	/**
	 * Return an string that represents the structure of the form.
	 * 
	 * @return
	 */
	public String getInitialInstancePage() {
		return XmlUtils.format(getFormStructure());
	}

	/**
	 * Return an string that represents the flow among the form's categories.
	 * 
	 * @return
	 */
	public String getCategoriesFlowPage() {
		StringBuilder text = new StringBuilder(
				"<controller xmlns=\"http://www.orbeon.com/oxf/controller\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:oxf=\"http://www.orbeon.com/oxf/processors\">");
		text.append(getSteps());
		text.append(getEpilogue());
		text.append("</controller>");
		return XmlUtils.format(text.toString());
	}

	private String getEpilogue() {
		return "<epilogue url=\"oxf:/config/epilogue.xpl\"/>";
	}

	public List<String> getAllcategoryModelPages() throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		List<String> files = new ArrayList<>();
		for (XFormsCategory xFormsCategory : this.getXFormsCategories()) {
			files.add(getCategoryModelPage(xFormsCategory));
		}
		return files;
	}

	/**
	 * Returns the page for one category.
	 * 
	 * @param xFormsCategory
	 * @return
	 * @throws PostCodeRuleSyntaxError
	 * @throws StringRuleSyntaxError
	 * @throws InvalidDateException
	 * @throws NotExistingDynamicFieldException
	 */
	public String getCategoryModelPage(XFormsCategory xFormsCategory) throws NotExistingDynamicFieldException,
			InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		StringBuilder text = new StringBuilder();
		text.append("<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" xmlns:xf=\"http://www.w3.org/2002/xforms\" xmlns:xxf=\"http://orbeon.org/oxf/xml/xforms\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:fr=\"http://orbeon.org/oxf/xml/form-runner\" xmlns:xi=\"http://www.w3.org/2001/XInclude\">");
		text.append(getHeader(xFormsCategory));
		text.append(getBody(xFormsCategory));
		text.append("</xh:html>");
		return XmlUtils.format(text.toString());
	}

	private String getSteps() {
		StringBuilder text = new StringBuilder();

		for (XFormsCategory xFormsCategory : getXFormsCategories()) {
			boolean firstCategory = xFormsCategory.getSource().getParent().getChildren()
					.indexOf(xFormsCategory.getSource()) == 0;
			text.append("<page path=\"/" + getOrbeonAppFolder() + "/");
			if (!firstCategory) {
				text.append(getCategoryPage(xFormsCategory));
			}
			text.append("\"");
			// First page have defined the form structure.
			text.append(" view=\"" + getCategoryFileName(xFormsCategory) + "\"");
			if (firstCategory) {
				text.append(" default-submission=\"" + DEFAULT_SUBMISSION + "\"");
			}
			text.append(" />");
		}

		return text.toString();
	}

	public String getCategoryFileName(XFormsCategory xFormsCategory) {
		int step = xFormsCategory.getSource().getParent().getChildren().indexOf(xFormsCategory.getSource());
		return "step" + step + "-" + xFormsCategory.getSource().getName() + ".xhtml";
	}

	public List<String> getAllCategoriesFileNames() {
		List<String> fileNames = new ArrayList<>();
		for (XFormsCategory formCategory : getXFormsCategories()) {
			fileNames.add(getCategoryFileName(formCategory));
		}
		return fileNames;
	}

	/**
	 * Model is empty. Already created in initial-instance.xml
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	@Override
	protected String getModelInstance() {
		return "";
	}

	@Override
	protected String getBody(XFormsObject<?> xFormsObject) {
		StringBuilder body = new StringBuilder("<xh:body>");
		body.append("<xh:table cellpadding=\"3\">");

		body.append(getBodySection(xFormsObject));

		// Add Next and previous buttons.
		body.append(generateNextAndPreviousButtons(xFormsObject));

		body.append("</xh:table>");
		body.append("</xh:body>");
		return body.toString();
	}

	public String getOrbeonAppFolder() {
		return getForm().getSimpleAsciiName();
	}

	private String getCategoryPage(XFormsCategory xFormsCategory) {
		if (xFormsCategory == null
				|| xFormsCategory.getSource().getParent().getChildren().indexOf(xFormsCategory.getSource()) == 0) {
			return "";
		}
		return xFormsCategory.getSource().getName();
	}

	/**
	 * In this case, the input comes from an instance.
	 */
	@Override
	protected String getInput() {
		return "<xf:instance src=\"input:instance\" id=\"fr-form-instance\"/>";
	}

	/**
	 * Creates the resources for current category.
	 */
	@Override
	protected String getElementResources(XFormsObject<?> xformsObject) throws NotExistingDynamicFieldException {
		StringBuilder resource = new StringBuilder();

		// Add hidden email field in first category.
		if (getXFormsCategories().indexOf(xformsObject) == 0) {
			resource.append(XFormsHiddenEmailField.getResources());
		}

		resource.append(xformsObject.getResources());

		return resource.toString();
	}

	/**
	 * Adds next and previous button at the end of the page.
	 * 
	 * @param xformsObject
	 * @return
	 */
	private String generateNextAndPreviousButtons(XFormsObject<?> xformsObject) {
		StringBuilder resource = new StringBuilder();
		resource.append("<xh:tr>");
		resource.append("<xh:td>");

		if (getXFormsCategories().indexOf(xformsObject) > 0) {
			resource.append("<xf:trigger>");
			resource.append("<xf:label>&lt; Previous</xf:label>");
			resource.append("<xf:send ev:event=\"DOMActivate\" submission=\"previous-submission\"/>");
			resource.append("</xf:trigger>");
		}

		if (getXFormsCategories().indexOf(xformsObject) < getXFormsCategories().size() - 1
				&& getXFormsCategories().indexOf(xformsObject) >= 0) {
			resource.append("<xf:trigger>");
			resource.append("<xf:label>Next &gt;</xf:label>");
			resource.append("<xf:send ev:event=\"DOMActivate\" submission=\"next-submission\"/>");
			resource.append("</xf:trigger>");
		}

		resource.append("</xh:td>");
		resource.append("</xh:tr>");
		return resource.toString();
	}

	@Override
	protected String getBodySection(XFormsObject<?> xformsObject) {
		StringBuilder body = new StringBuilder();

		// Add hidden email field in first category.
		if (getXFormsCategories().indexOf(xformsObject) == 0) {
			body.append(XFormsHiddenEmailField.getBody());
		}

		xformsObject.getSectionBody(body);

		return body.toString();
	}

	@Override
	protected String getEventsDefinitions(XFormsObject<?> xFormsObject) {
		StringBuilder events = new StringBuilder();

		getNextAndPreviousButtonEvents(events, xFormsObject);
		getSkipEmptyCategoryEvents(events, xFormsObject);

		return events.toString();
	}

	/**
	 * Create events for next and previous buttons.
	 * 
	 * @param events
	 * @param xFormsObject
	 */
	private void getNextAndPreviousButtonEvents(StringBuilder events, XFormsObject<?> xFormsObject) {
		if (xFormsObject instanceof XFormsCategory) {
			XFormsCategory xFormsCategory = (XFormsCategory) xFormsObject;
			int xFormsCategoryIndex = xFormsCategory.getSource().getParent().getChildren()
					.indexOf(xFormsCategory.getSource());
			XFormsCategory previousXFormsCategory = null;
			XFormsCategory nextXFormsCategory = null;

			if (xFormsCategoryIndex > 0) {
				previousXFormsCategory = (XFormsCategory) getXFormsHelper().getXFormsObject(
						xFormsCategory.getSource().getParent().getChildren().get(xFormsCategoryIndex - 1));
			}

			if (xFormsCategoryIndex < xFormsCategory.getSource().getParent().getChildren().size() - 1) {
				nextXFormsCategory = (XFormsCategory) getXFormsHelper().getXFormsObject(
						xFormsCategory.getSource().getParent().getChildren().get(xFormsCategoryIndex + 1));
			}

			events.append("<!-- Next / Previous button events -->");
			if (previousXFormsCategory != null) {
				events.append("<xf:submission id=\"previous-submission\" resource=\"/" + getOrbeonAppFolder() + "/"
						+ getCategoryPage(previousXFormsCategory) + "\" method=\"post\" replace=\"all\"/>");
			}
			if (nextXFormsCategory != null) {
				events.append("<xf:submission id=\"next-submission\" resource=\"/" + getOrbeonAppFolder() + "/"
						+ getCategoryPage(nextXFormsCategory) + "\" method=\"post\" replace=\"all\"/>");
			}
		}
	}

	/**
	 * If a page has no visible elements is skipped.
	 * 
	 * @param events
	 * @param xFormsObject
	 */
	private void getSkipEmptyCategoryEvents(StringBuilder events, XFormsObject<?> xFormsObject) {
		events.append("<!-- Keep track of enabled/disabled status -->");
		events.append("<xf:instance id=\"totalVisibleElements-" + xFormsObject.getSource().getSimpleAsciiName() + "\">");
		events.append("<value>0</value>");
		events.append("</xf:instance>");
		events.append("<!-- Update for each element -->");
		for (XFormsObject<?> xFormQuestion : xFormsObject.getAllChildrenInHierarchy(XFormsQuestion.class)) {
			events.append("<xf:setvalue event=\"xforms-enabled\" observer=\"" + xFormQuestion.getSectionControlName()
					+ "\" ref=\"instance('totalVisibleElements-" + xFormsObject.getSource().getSimpleAsciiName()
					+ "')\" value=\"instance('totalVisibleElements') + 1\"/>");
			events.append("<xf:setvalue event=\"xforms-disabled\" observer=\"" + xFormQuestion.getSectionControlName()
					+ "\" ref=\"instance('totalVisibleElements-" + xFormsObject.getSource().getSimpleAsciiName()
					+ "')\" value=\"instance('totalVisibleElements') - 1\"/>");
		}
		events.append("<!-- Redirect to next page -->");
		events.append("<xf:send ev:event=\"xforms-ready\" submission=\"next-submission\" if=\"instance('totalVisibleElements') = 0\"/>");
	}

	@Override
	protected String getElementBinding(XFormsObject<?> xformsObject) throws NotExistingDynamicFieldException,
			InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		StringBuilder binding = new StringBuilder();

		// Add hidden email field.
		if (getXFormsCategories().indexOf(xformsObject) == 0) {
			binding.append(XFormsHiddenEmailField.getBinding());
		}

		xformsObject.getBinding(binding);

		return binding.toString();
	}
}
