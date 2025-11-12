package com.biit.webforms.exporters.xforms;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.form.entity.BaseCategory;
import com.biit.form.entity.BaseGroup;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.persistence.entity.ElementWithMedia;
import com.biit.webforms.persistence.entity.Form;

/**
 * Categories are Sections in Orbeon. The same as groups.
 *
 */
public class XFormsCategory extends XFormsGroup {

	private static final String CSS_CLASS_CATEGORY = "webforms-category";

	public XFormsCategory(XFormsHelper xFormsHelper, BaseCategory category) throws NotValidTreeObjectException, NotValidChildException {
		super(xFormsHelper, category);
	}

	/**
	 * Categories has the image inside the object, not before as others
	 * XFormsObjects
	 */
	@Override
	protected String getDefinition() {
		StringBuilder section = new StringBuilder();

		section.append("<" + getName() + ">");
		// Add element's image
		if (getXFormsHelper().isImagesEnabled() && getSource() instanceof ElementWithMedia && ((ElementWithMedia) getSource()).getImage() != null) {
			section.append(XFormsImage.getDefinition(((ElementWithMedia) getSource()).getImage(), getXFormsHelper(),
					(Form) getSource().getAncestor(Form.class), getXFormsHelper().getOrganization(), getXFormsHelper().isPreviewMode()));
		}
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			section.append(child.getDefinition());
		}
		section.append("</" + getName() + ">");
		return section.toString();
	}

	@Override
	protected void setSource(BaseGroup treeObject) throws NotValidTreeObjectException {
		if (treeObject instanceof BaseCategory) {
			super.setSource(treeObject);
		} else {
			throw new NotValidTreeObjectException("Invalid source!");
		}
	}

	@Override
	protected String getCassGroupClass() {
		return CSS_CLASS_CATEGORY;
	}

	@Override
	protected void getBinding(StringBuilder binding) throws NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		binding.append("<xf:bind id=\"").append(getBindingId()).append("\" name=\"").append(getBindingName()).append("\"");
		getRelevantStructure(binding);

		binding.append(" ref=\"").append(getXPath()).append("\" >");

		// Add form image binding
		if (getXFormsHelper().isImagesEnabled() && ((ElementWithMedia) getSource()).getImage() != null) {
			XFormsImage.getBinding(this, ((ElementWithMedia) getSource()).getImage(), binding, getRelevantStructure(), getXFormsHelper(), getSource());
		}

		// Add also children.
		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			child.getBinding(binding);
		}
		binding.append("</xf:bind>");
	}

	/**
	 * Categories has the image inside the object, not before as others
	 * XFormsObjects
	 */
	@Override
	protected String getResources(OrbeonLanguage language) throws NotExistingDynamicFieldException {
		StringBuilder resource = new StringBuilder();

		resource.append("<" + getName() + ">");
		resource.append(getLabel(language));
		resource.append(getHint(language));
		resource.append(getAlert(language));
		resource.append(getHelp(language));

		// Add element's image.
		if (getXFormsHelper().isImagesEnabled() && getSource() instanceof ElementWithMedia && ((ElementWithMedia) getSource()).getImage() != null) {
			resource.append(XFormsImage.getResources(((ElementWithMedia) getSource()).getImage(), language, getXFormsHelper()));
		}

		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			resource.append(child.getResources(language));
		}

		resource.append("</" + getName() + ">");
		return resource.toString();
	}

	/**
	 * Groups are represented as sections.
	 */
	@Override
	protected void getSectionBody(StringBuilder body) {
		body.append("<fr:section id=\"").append(getSectionControlName());
		body.append("\" class=\"").append(getCssClass());
		if (WebformsConfigurationReader.getInstance().isXFormsCustomWizardEnabled()) {
			body.append(getCSSDisplayConditionRule());
		}
		body.append("\" bind=\"").append(getBindingId()).append("\">");
		body.append(getBodyLabel());
		body.append(getBodyHint());
		body.append(getBodyAlert());
		body.append(getBodyHelp());

		// Add element's image.
		if (getXFormsHelper().isImagesEnabled() && getSource() instanceof ElementWithMedia && ((ElementWithMedia) getSource()).getImage() != null) {
			XFormsImage.getBody(this, ((ElementWithMedia) getSource()).getImage(), body, getXFormsHelper());
		}

		for (XFormsObject<? extends TreeObject> child : getChildren()) {
			child.getSectionBody(body);
		}
		body.append("</fr:section>");
	}

	private String getCSSDisplayConditionRule() {
		return " {if((instance('category-menu-button-active')/" + getUniqueName() + "='true') and (instance('show-category')/" + getUniqueName()
				+ "='true')) then '' else 'category-show-disabled'}";
	}
}
