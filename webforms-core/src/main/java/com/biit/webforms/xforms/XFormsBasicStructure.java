package com.biit.webforms.xforms;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.utils.date.DateManager;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;

public class XFormsBasicStructure {
	public final static String APP_NAME = "WebForms";
	protected final static String CHARSET = "UTF-8";
	private List<XFormsCategory> xFormsCategories;
	private Form form;
	private XFormsHelper xFormsHelper;

	public XFormsBasicStructure(Form form) throws NotValidTreeObjectException, NotValidChildException {
		this.form = form;
		xFormsHelper = new XFormsHelper(form);
		createXFormObjectsStructure();
	}

	/**
	 * Basic form information.
	 * 
	 * @param form
	 * @return
	 */
	protected static String createFormDescription(Form form) {
		return "Version: " + form.getVersion() + ". Modification date: "
				+ DateManager.convertDateToStringWithHours(form.getUpdateTime()) + ". Publication date: "
				+ DateManager.convertDateToStringWithHours(new Timestamp(new java.util.Date().getTime())) + ".";
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
	 * Needed tags for attach files to the form (pdf, images, ...).
	 * 
	 * @param form
	 * @return
	 */
	protected static String getAttachments() {
		String attachment = "<xf:instance id=\"fr-form-attachments\">";
		attachment += "<attachments>";
		attachment += "<css mediatype=\"text/css\" filename=\"\" size=\"\"/>";
		attachment += "<pdf mediatype=\"application/pdf\" filename=\"\" size=\"\"/>";
		attachment += "</attachments>";
		attachment += "</xf:instance>";
		return attachment;
	}

	/**
	 * Contains instance data associated with the model element. It is used to supply initial values for forms controls,
	 * or to provide additional data to be submitted with the form.
	 * 
	 * @return
	 * @throws InvalidFlowInForm
	 */
	protected static String getInstances() {
		StringBuilder instances = new StringBuilder(
				"<xf:instance id=\"fr-service-request-instance\" xxf:exclude-result-prefixes=\"#all\">");
		instances.append("<request/>");
		instances.append("</xf:instance>");
		instances.append("<xf:instance id=\"fr-service-response-instance\" xxf:exclude-result-prefixes=\"#all\">");
		instances.append("<response/>");
		instances.append("</xf:instance>");
		return instances.toString();
	}

	/**
	 * Creates the metadata information of the form.
	 * 
	 * @param form
	 * @return
	 */
	protected static String getMetaData(Form form) {
		StringBuilder metadata = new StringBuilder("<metadata>");

		metadata.append("<application-name>" + APP_NAME + "</application-name>");
		metadata.append("<form-name>" + formatFormName(form) + "</form-name>");
		metadata.append("<title xml:lang=\"en\">" + form.getLabel() + "</title>");
		metadata.append("<description xml:lang=\"en\">" + createFormDescription(form) + "</description>");

		metadata.append("</metadata>");

		return metadata.toString();
	}

	private void createXFormObjectsStructure() throws NotValidTreeObjectException, NotValidChildException {
		xFormsCategories = new ArrayList<>();
		for (TreeObject child : form.getChildren()) {
			XFormsCategory xFormsCategory = new XFormsCategory(xFormsHelper, (Category) child);
			xFormsCategories.add(xFormsCategory);
		}
	}

	protected Form getForm() {
		return form;
	}

	protected String getFormStructure() {
		StringBuilder text =  new StringBuilder("<form>");

		// Add hidden email field.
		text.append(XFormsHiddenEmailField.getModel());

		for (XFormsCategory xFormCategory : getXFormsCategories()) {
			text.append(xFormCategory.getDefinition());
		}
		text.append("</form>");
		return text.toString();
	}

	protected List<XFormsCategory> getXFormsCategories() {
		return xFormsCategories;
	}
}
