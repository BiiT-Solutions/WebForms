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
 * - Binding. Used to link the model with some behavior of the component, such as relevance.<br>
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
		xforms.append(getHeader());
		xforms.append(getBody());
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
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
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

}
