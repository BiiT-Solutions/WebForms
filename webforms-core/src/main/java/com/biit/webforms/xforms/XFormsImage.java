package com.biit.webforms.xforms;

import com.biit.usermanager.entity.IGroup;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.xforms.XFormsPersistence;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

/**
 * This element is not inside the XForms hierarchy, only stores some method to
 * create an image in Orbeon.
 *
 */
public class XFormsImage {
	private final static String BASIC_NAME = "image";

	public static String getName(TreeObjectImage image) {
		return image.getElement().getName() + "-" + BASIC_NAME;
	}

	public static String getDefinition(TreeObjectImage image, Form form, IGroup<Long> organization, boolean preview) {
		if (image != null) {
			return "<" + getName(image) + ">/fr/service/persistence/crud/" + XFormsPersistence.APP_NAME + "/"
					+ XFormsPersistence.formatFormName(form, organization, preview) + "/data/"
					+ XFormsPersistence.imageDocumentId(form, organization, image, preview) + "/" + image.getComparationId() + "</" + getName(image) + ">";
		}
		return "";
	}

	private static String getSectionControlName(TreeObjectImage image) {
		return getName(image) + "-control";
	}

	private static String getBindingId(TreeObjectImage image) {
		return getName(image) + "-bind";
	}

	public static void getBody(XFormsObject<?> xFormsObject, TreeObjectImage image, StringBuilder body) {
		body.append("<xf:output id=\"" + getSectionControlName(image) + "\" bind=\"" + getBindingId(image) + "\" mediatype=\"image/*\" class=\"image "
				+ image.getElement().getClass().getSimpleName() + "-image\" >");
		body.append(getBodyLabel(xFormsObject, image));
		body.append(getBodyHint(xFormsObject, image));
		body.append("</xf:output>");
	}

	private static String getBodyHint(XFormsObject<?> xFormsObject, TreeObjectImage image) {
		return getBodyStructure(xFormsObject, image, "hint");
	}

	private static String getBodyLabel(XFormsObject<?> xFormsObject, TreeObjectImage image) {
		return getBodyStructure(xFormsObject, image, "label");
	}

	/**
	 * Defines the structure of the element in the body part of the XForms.
	 * 
	 * @param treeObject
	 * @return
	 * @throws InvalidFlowInForm
	 */
	private static String getBodyStructure(XFormsObject<?> xFormsObject, TreeObjectImage image, String structure) {
		String text = "<xf:output ref=\"instance('fr-form-resources')/resource/" + getPath(xFormsObject, image) + "/" + structure + "\"";
		text += " />";
		return text;
	}

	private static String getPath(XFormsObject<?> xFormsObject, TreeObjectImage image) {
		if (image.getElement().getParent() != null) {
			return xFormsObject.getParent().getPath() + "/" + getName(image);
		}
		return getName(image);
	}

	public static String getResources(TreeObjectImage image, OrbeonLanguage language) throws NotExistingDynamicFieldException {
		String resource = "<" + getName(image) + ">";
		resource += getLabel(language);
		resource += getHint(language);

		resource += "</" + getName(image) + ">";
		return resource;
	}

	private static String getLabel(OrbeonLanguage language) {
		return "<label/>";
	}

	private static String getHint(OrbeonLanguage language) {
		return "<hint/>";
	}

	private static void getXFormsType(StringBuilder binding) {
		binding.append("type=\"xf:anyURI\"");
	}

	// <xf:bind id="control-3-bind" ref="control-3" name="control-3"
	// type="xf:anyURI"/>
	public static void getBinding(TreeObjectImage image, StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError {
		binding.append("<xf:bind id=\"").append(getBindingId(image)).append("\"  name=\"").append(getName(image)).append("\" ");
		binding.append("ref=\"").append(getName(image)).append("\" ");
		getXFormsType(binding);
		binding.append("/>");
	}

}
