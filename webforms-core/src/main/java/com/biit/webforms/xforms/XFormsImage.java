package com.biit.webforms.xforms;

import com.biit.form.entity.TreeObject;
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

	public static String getName(TreeObjectImage image, XFormsHelper xFormsHelper) {
		return xFormsHelper.getUniqueName(image.getElement()) + "-" + BASIC_NAME;
	}

	public static String getDefinition(TreeObjectImage image, XFormsHelper xFormsHelper, Form form, IGroup<Long> organization, boolean preview) {
		if (image != null) {
			// Remote image
			if (image.getUrl() != null) {
				return "<" + getName(image, xFormsHelper) + ">" + image.getUrl() + "</" + getName(image, xFormsHelper) + ">";
			} else {
				// Embedded image.
				return "<" + getName(image, xFormsHelper) + ">/fr/service/persistence/crud/" + XFormsPersistence.APP_NAME + "/"
						+ XFormsPersistence.formatFormName(form, organization, preview) + "/data/"
						+ XFormsPersistence.imageDocumentId(form, organization, image, preview) + "/" + image.getComparationId() + "</"
						+ getName(image, xFormsHelper) + ">";
			}
		}
		return "";
	}

	private static String getSectionControlName(TreeObjectImage image, XFormsHelper xFormsHelper) {
		return getName(image, xFormsHelper) + "-control";
	}

	private static String getBindingId(TreeObjectImage image, XFormsHelper xFormsHelper) {
		return getName(image, xFormsHelper) + "-bind";
	}

	public static void getBody(XFormsObject<?> xFormsObject, TreeObjectImage image, StringBuilder body, XFormsHelper xFormsHelper) {
		body.append("<xf:output id=\"" + getSectionControlName(image, xFormsHelper) + "\" bind=\"" + getBindingId(image, xFormsHelper)
				+ "\" mediatype=\"image/*\" class=\"image " + image.getElement().getClass().getSimpleName() + "-image\" >");
		body.append(getBodyLabel(xFormsObject, image, xFormsHelper));
		body.append(getBodyHint(xFormsObject, image, xFormsHelper));
		body.append("</xf:output>");
	}

	private static String getBodyHint(XFormsObject<?> xFormsObject, TreeObjectImage image, XFormsHelper xFormsHelper) {
		return getBodyStructure(xFormsObject, image, "hint", xFormsHelper);
	}

	private static String getBodyLabel(XFormsObject<?> xFormsObject, TreeObjectImage image, XFormsHelper xFormsHelper) {
		return getBodyStructure(xFormsObject, image, "label", xFormsHelper);
	}

	/**
	 * Defines the structure of the element in the body part of the XForms.
	 * 
	 * @param treeObject
	 * @return
	 * @throws InvalidFlowInForm
	 */
	private static String getBodyStructure(XFormsObject<?> xFormsObject, TreeObjectImage image, String structure, XFormsHelper xFormsHelper) {
		String text = "<xf:output ref=\"instance('fr-form-resources')/resource/" + getPath(xFormsObject, image, xFormsHelper) + "/" + structure + "\"";
		text += " />";
		return text;
	}

	private static String getPath(XFormsObject<?> xFormsObject, TreeObjectImage image, XFormsHelper xFormsHelper) {
		if (xFormsObject != null) {
			return xFormsObject.getPath() + "/" + getName(image, xFormsHelper);
		}
		return getName(image, xFormsHelper);
	}

	public static String getResources(TreeObjectImage image, OrbeonLanguage language, XFormsHelper xFormsHelper) {
		String resource = "<" + getName(image, xFormsHelper) + ">";
		resource += getLabel(language);
		resource += getHint(language);

		resource += "</" + getName(image, xFormsHelper) + ">";
		return resource;
	}

	private static String getLabel(OrbeonLanguage language) {
		return "<label/>";
	}

	private static String getHint(OrbeonLanguage language) {
		return "<hint/>";
	}

	private static void getXFormsType(StringBuilder binding) {
		binding.append(" type=\"xf:anyURI\"");
	}

	public static void getBinding(XFormsObject<?> xFormsObject, TreeObjectImage image, StringBuilder binding, String relevance, XFormsHelper xFormsHelper,
			TreeObject source) throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		binding.append("<xf:bind id=\"").append(getBindingId(image, xFormsHelper)).append("\"  name=\"").append(getName(image, xFormsHelper)).append("\" ");
		if (xFormsObject != null) {
			binding.append("ref=\"").append(xFormsObject.getXPath() + "/" + getName(image, xFormsHelper)).append("\" ");
		} else {
			binding.append("ref=\"").append(getName(image, xFormsHelper)).append("\" ");
		}
		if (relevance != null) {
			binding.append(relevance);
		}
		getXFormsType(binding);
		binding.append("/>");
	}

}
