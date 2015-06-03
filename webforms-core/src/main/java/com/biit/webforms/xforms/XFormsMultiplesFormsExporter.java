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
		text.append("<xh:html xmlns:xh=\"http://www.w3.org/1999/xhtml\" xmlns:xf=\"http://www.w3.org/2002/xforms\" xmlns:xxf=\"http://orbeon.org/oxf/xml/xforms\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xi=\"http://www.w3.org/2001/XInclude\">");
		text.append(getHeader());
		text.append(getBody());
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
				text.append(xFormsCategory.getSource().getName());
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
	protected String getBody() {
		StringBuilder body = new StringBuilder("<xh:body>");
		body.append("<fr:view>");
		body.append("<fr:body xmlns:xbl=\"http://www.w3.org/ns/xbl\" ");
		body.append("xmlns:dataModel=\"java:org.orbeon.oxf.fb.DataModel\" ");
		body.append("xmlns:oxf=\"http://www.orbeon.com/oxf/processors\" ");
		body.append("xmlns:p=\"http://www.orbeon.com/oxf/pipeline\" >");

		body.append(getBodySection());

		body.append("</fr:body>");
		body.append("</fr:view>");
		body.append("</xh:body>");
		return body.toString();
	}

	public String getOrbeonAppFolder() {
		return getForm().getSimpleAsciiName();
	}

}
