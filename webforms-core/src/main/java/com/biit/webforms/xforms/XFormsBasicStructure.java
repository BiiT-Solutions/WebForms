package com.biit.webforms.xforms;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.usermanager.entity.IGroup;
import com.biit.utils.date.DateManager;
import com.biit.webforms.configuration.WebformsConfigurationReader;
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

	public XFormsBasicStructure(Form form, IGroup<Long> organization) throws NotValidTreeObjectException, NotValidChildException {
		this.form = form;
		xFormsHelper = new XFormsHelper(form);
		setOrganization(organization);
		createXFormObjectsStructure();
	}

	/**
	 * Basic form information.
	 * 
	 * @param form
	 * @return
	 */
	protected static String createFormDescription(Form form) {
		return "Version: " + form.getVersion() + ". Modification date: " + DateManager.convertDateToStringWithHours(form.getUpdateTime())
				+ ". Publication date: " + DateManager.convertDateToStringWithHours(new Timestamp(new java.util.Date().getTime())) + ".";
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
	 * Contains instance data associated with the model element. It is used to
	 * supply initial values for forms controls, or to provide additional data
	 * to be submitted with the form.
	 * 
	 * @return
	 * @throws InvalidFlowInForm
	 */
	protected static String getInstances() {
		StringBuilder instances = new StringBuilder("<xf:instance id=\"fr-service-request-instance\" xxf:exclude-result-prefixes=\"#all\">");
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
		metadata.append("<singleton>false</singleton>");

		metadata.append("</metadata>");

		return metadata.toString();
	}

	private void createXFormObjectsStructure() throws NotValidTreeObjectException, NotValidChildException {
		xFormsCategories = new ArrayList<>();
		for (TreeObject child : form.getAllNotHiddenChildren()) {
			XFormsCategory xFormsCategory = createXFormsCategory((Category) child);
			xFormsCategories.add(xFormsCategory);
			getXFormsHelper().addXFormsObject(xFormsCategory);
		}
	}

	protected XFormsCategory createXFormsCategory(Category category) throws NotValidTreeObjectException, NotValidChildException {
		return new XFormsCategory(getXFormsHelper(), category);
	}

	protected Form getForm() {
		return form;
	}

	protected String getFormStructure() {
		StringBuilder text = new StringBuilder("<form>");

		// Add hidden email field.
		text.append(XFormsHiddenEmailField.getModel());

		// Add form's image
		if (getXFormsHelper().isImagesEnabled() && getForm().getImage() != null) {
			text.append(XFormsImage.getDefinition(getForm().getImage(), getForm(), getXFormsHelper().getOrganization(), getXFormsHelper().isPreviewMode()));
		}

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
	protected String getHeader(XFormsObject<?> xFormsObject) throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		StringBuilder header = new StringBuilder("<xh:head>");
		header.append("<xh:meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		header.append("<xh:title>" + getForm().getLabel() + "</xh:title>");
		header.append("<xf:model id=\"fr-form-model\" xxf:expose-xpath-types=\"true\">");
		header.append(getInput());
		header.append("<xf:instance xxf:readonly=\"true\" id=\"fr-form-metadata\" xxf:exclude-result-prefixes=\"#all\">");
		header.append(getMetaData(getForm()));
		header.append("</xf:instance>");
		header.append(getModelInstance());
		header.append(getBinding(xFormsObject));
		header.append(getAttachments());
		header.append(getResources(xFormsObject));
		header.append(getInstances());
		header.append(getTemplatesOfLoops());
		header.append(getEventsDefinitions(xFormsObject));
		header.append("</xf:model>");
		header.append("</xh:head>");
		return header.toString();
	}

	protected abstract String getEventsDefinitions(XFormsObject<?> xFormsObject);

	/**
	 * Sets the xforms tags for getting data for a different file.
	 * 
	 * @return
	 */
	protected abstract String getInput();

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
	private String getBinding(XFormsObject<?> xformsObject) throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		StringBuilder binding = new StringBuilder();
		binding.append("<xf:bind id=\"fr-form-binds\" ref=\"instance('fr-form-instance')\">");

		// Add form image binding
		if (getXFormsHelper().isImagesEnabled() && getForm().getImage() != null) {
			XFormsImage.getBinding(null, getForm().getImage(), binding, null);
		}

		binding.append(getElementBinding(xformsObject));

		binding.append(" </xf:bind>");
		return binding.toString();
	}

	/**
	 * Get the element biding from an element. If the element is null gets the
	 * binding for all elements of the form.
	 * 
	 * @param xformsObject
	 * @return
	 * @throws NotExistingDynamicFieldException
	 * @throws InvalidDateException
	 * @throws StringRuleSyntaxError
	 * @throws PostCodeRuleSyntaxError
	 */
	protected abstract String getElementBinding(XFormsObject<?> xformsObject) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError;

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
	 * @param xFormsCategory
	 *            if not null, get resources for only this category
	 * @return
	 * @throws NotExistingDynamicFieldException
	 */
	private String getResources(XFormsObject<?> xformsObject) throws NotExistingDynamicFieldException {
		StringBuilder resource = new StringBuilder("<xf:instance id=\"fr-form-resources\" xxf:readonly=\"false\" xxf:exclude-result-prefixes=\"#all\">");
		resource.append("<resources>");
		// For each language
		for (OrbeonLanguage language : getEnabledLanguages()) {
			resource.append("<resource xml:lang=\"" + language.getAbbreviature() + "\">");

			// Add form's image.
			if (getXFormsHelper().isImagesEnabled() && getForm().getImage() != null) {
				resource.append(XFormsImage.getResources(getForm().getImage(), language));
			}

			// Add resources
			resource.append(getElementResources(xformsObject, language));

			resource.append("</resource>");
		}
		resource.append("</resources>");
		resource.append("</xf:instance>");

		return resource.toString();
	}

	private Set<OrbeonLanguage> getEnabledLanguages() {
		Set<OrbeonLanguage> availableLanguages = new HashSet<>();
		for (String language : WebformsConfigurationReader.getInstance().getOrbeonAvailableLanguages()) {
			OrbeonLanguage orbeonLanguage = OrbeonLanguage.get(language);
			if (orbeonLanguage != null) {
				availableLanguages.add(orbeonLanguage);
			}
		}
		// At least, english.
		if (availableLanguages.isEmpty()) {
			availableLanguages.add(OrbeonLanguage.ENGLISH);
		}
		return availableLanguages;
	}

	/**
	 * Creates the hierarchy of the resources.
	 * 
	 * @param xformsObject
	 * @return
	 * @throws NotExistingDynamicFieldException
	 */
	protected abstract String getElementResources(XFormsObject<?> xformsObject, OrbeonLanguage language) throws NotExistingDynamicFieldException;

	/**
	 * Creates the body section of the XForm.
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	protected abstract String getBody(XFormsObject<?> xformsObject);

	/**
	 * Shows the sections defined in the header.
	 * 
	 * @param form
	 * @return
	 * @throws InvalidFlowInForm
	 */
	protected abstract String getBodySection(XFormsObject<?> xformsObject);

	public void setImagesEnabled(boolean imagesEnabled) {
		getXFormsHelper().setImagesEnabled(imagesEnabled);
	}

	public void setPreviewMode(boolean previewMode) {
		getXFormsHelper().setPreviewMode(previewMode);
	}

	public void setOrganization(IGroup<Long> organization) {
		getXFormsHelper().setOrganization(organization);
	}
}
