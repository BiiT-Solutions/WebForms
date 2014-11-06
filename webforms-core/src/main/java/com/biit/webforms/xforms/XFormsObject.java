package com.biit.webforms.xforms;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

public abstract class XFormsObject {
	private TreeObject source;

	private List<XFormsObject> children;

	private XFormsObject parent;

	public XFormsObject(TreeObject treeObject) throws NotValidTreeObjectException, NotValidChildException {
		setSource(treeObject);
		children = new ArrayList<>();
		for (TreeObject child : treeObject.getChildren()) {
			addChild(child);
		}
	}

	protected void setSource(TreeObject treeObject) throws NotValidTreeObjectException {
		source = treeObject;
	}

	protected void addChild(TreeObject child) throws NotValidChildException {
		try {
			XFormsObject newChild;
			if (child instanceof Category) {
				newChild = new XFormsCategory((Category) child);
			} else if (child instanceof Group) {
				if (!child.isInsideALoop()) {
					if (((Group) child).isRepeatable()) {
						newChild = new XFormsRepeatableGroup((Group) child);
					} else {
						newChild = new XFormsGroup((Group) child);
					}
				} else {
					if (((Group) child).isRepeatable()) {
						newChild = new XFormsRepeatableGroupInRepeatableGroup((Group) child);
					} else {
						newChild = new XFormsGroupInRepeatableGroup((Group) child);
					}
				}
			} else if (child instanceof Question) {
				newChild = new XFormsQuestion((Question) child);
			} else if (child instanceof Answer) {
				newChild = new XFormsAnswer((Answer) child);
			} else {
				// Forms cannot be a valid child.
				throw new NotValidChildException("Inserted child '" + child + "' is not valid. ");
			}
			newChild.setParent(this);
			children.add(newChild);
		} catch (NotValidTreeObjectException e) {
			throw new NotValidChildException("Inserted child '" + child + "' is not valid. ");
		}
	}

	protected TreeObject getSource() {
		return source;
	}

	protected String getAlert() {
		return "<alert>" + "</alert>";
	}

	protected String getBindingName() {
		return getControlName() + "-bind";
	}

	protected String getBodyAlert() {
		return getBodyStructure("alert", false);
	}

	protected String getBodyHelp() {
		return "";
	}

	protected String getBodyHint() {
		return getBodyStructure("hint", false);
	}

	protected String getBodyLabel() {
		return getBodyStructure("label", true);
	}

	/**
	 * Defines the structure of the element in the body part of the XForms.
	 * 
	 * @param treeObject
	 * @return
	 * @throws InvalidFlowInForm
	 */
	protected String getBodyStructure(String structure, boolean html) {
		String text = "<xf:" + structure + " ref=\"$form-resources/" + getPath() + "/" + structure + "\"";
		if (html) {
			text += " mediatype=\"text/html\" ";
		}
		text += " />";
		return text;
	}

	/**
	 * Return the complete path of the element.
	 * 
	 * @return
	 */
	protected String getPath() {
		if (getParent() != null) {
			return getParent().getPath() + "/" + getControlName();
		}
		return getControlName();
	}

	protected List<XFormsObject> getChildren() {
		return children;
	}

	protected String getControlName() {
		return getSource().getName();
	}

	protected String getHelp() {
		// Avoid empty help windows.
		return "";
	}

	protected String getLabel() {
		return "<label><![CDATA[" + getSource().getLabel() + "]]></label>";
	}

	protected String getHint() {
		return "<hint/>";
	}

	protected String getRelevantStructure() throws InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		return "";
	}

	protected String getResources() throws NotExistingDynamicFieldException {
		String resource = "<" + getControlName() + ">";
		resource += getLabel();
		resource += getHint();
		resource += getAlert();
		resource += getHelp();

		for (XFormsObject child : getChildren()) {
			resource += child.getResources();
		}

		resource += "</" + getControlName() + ">";
		return resource;
	}

	protected String getSectionControlName() {
		return getControlName() + "-control";
	}

	protected abstract String getBinding() throws NotExistingDynamicFieldException, InvalidDateException,
			StringRuleSyntaxError, PostCodeRuleSyntaxError;

	protected abstract String getSectionBody();

	protected String getDefinition() {
		String section = "<" + getControlName() + ">";
		for (XFormsObject child : getChildren()) {
			section += child.getDefinition();
		}
		section += "</" + getControlName() + ">";
		return section;
	}

	protected XFormsObject getParent() {
		return parent;
	}

	protected void setParent(XFormsObject parent) {
		this.parent = parent;
	}

	/**
	 * Only loops uses templates. Search in childs.
	 * 
	 * @return
	 */
	protected String getTemplates() {
		String templates = "";
		for (XFormsObject child : getChildren()) {
			templates += child.getTemplates();
		}
		return templates;
	}

}
