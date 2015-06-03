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
import com.biit.webforms.xforms.exceptions.DateRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public abstract class XFormsBasicStructure {
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
			XFormsCategory xFormsCategory = createXFormsCategory((Category) child);
			xFormsCategories.add(xFormsCategory);
		}
	}

	protected XFormsCategory createXFormsCategory(Category category) throws NotValidTreeObjectException,
			NotValidChildException {
		return new XFormsCategory(getXFormsHelper(), category);
	}

	protected Form getForm() {
		return form;
	}

	protected String getFormStructure() {
		StringBuilder text = new StringBuilder("<form>");

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

	protected XFormsHelper getXFormsHelper() {
		return xFormsHelper;
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
	protected String getHeader() throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		StringBuilder header = new StringBuilder("<xh:head>");
		header.append("<xh:meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		header.append("<xh:title>" + getForm().getLabel() + "</xh:title>");
		header.append("<xf:model id=\"fr-form-model\" xxf:expose-xpath-types=\"true\">");
		header.append("<xf:instance xxf:readonly=\"true\" id=\"fr-form-metadata\" xxf:exclude-result-prefixes=\"#all\">");
		header.append(getMetaData(getForm()));
		header.append("</xf:instance>");
		header.append(getModelInstance());
		header.append(getBinding());
		header.append(getAttachments());
		header.append(getResources());
		header.append(getInstances());
		header.append(getTemplatesOfLoops());
		header.append("</xf:model>");
		header.append("</xh:head>");
		return header.toString();
	}

	/**
	 * Creates the model section of XForms.
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	protected String getModelInstance() {
		StringBuilder text = new StringBuilder("<xf:instance id=\"fr-form-instance\">");
		text.append(getFormStructure());
		text.append("</xf:instance>");
		return text.toString();
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

		for (XFormsCategory category : getXFormsCategories()) {
			category.getBinding(binding);
		}
		binding.append(" </xf:bind>");
		return binding.toString();
	}

	private String getTemplatesOfLoops() {
		String templates = "";
		for (XFormsCategory xFormsCategory : getXFormsCategories()) {
			templates += xFormsCategory.getTemplates();
		}
		return templates;
	}

	/**
	 * Creates all resources of the form (labels initial values, ...).
	 * 
	 * @return
	 * @throws NotExistingDynamicFieldException
	 * @throws InvalidFlowInForm
	 */
	private String getResources() throws NotExistingDynamicFieldException {
		StringBuilder resource = new StringBuilder("<xf:instance id=\"fr-form-resources\" xxf:readonly=\"false\">");
		resource.append("<resources>");
		resource.append("<resource xml:lang=\"en\">");

		// Add hidden email field.
		resource.append(XFormsHiddenEmailField.getResources());

		for (XFormsCategory category : getXFormsCategories()) {
			resource.append(category.getResources());
		}

		resource.append("</resource>");
		resource.append("</resources>");
		resource.append("</xf:instance>");

		return resource.toString();
	}

	/**
	 * Creates the body section of the XForm.
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	protected abstract String getBody();

	/**
	 * Shows the sections defined in the header.
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	protected String getBodySection() {
		StringBuilder body = new StringBuilder();

		// Add hidden email field.
		body.append(XFormsHiddenEmailField.getBody());

		for (XFormsCategory category : getXFormsCategories()) {
			category.getSectionBody(body);
		}
		return body.toString();
	}
}
