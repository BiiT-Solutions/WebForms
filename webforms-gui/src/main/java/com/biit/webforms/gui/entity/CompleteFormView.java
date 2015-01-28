package com.biit.webforms.gui.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.IWebformsFormView;

public class CompleteFormView extends Form implements IWebformsFormView {
	private static final long serialVersionUID = -426480388117580446L;
	private Form form;

	public CompleteFormView(Form form) {
		this.form = form;
	}

	@Override
	public List<TreeObject> getChildren() {
		List<TreeObject> children = new ArrayList<>();

		for (TreeObject child : form.getChildren()) {
			if (child instanceof BlockReference) {
				try {
					Block copiedBlock = new Block();
					System.out.println("1.- "+ child.getChildren());
					copiedBlock.copyData(((BlockReference) child).getReference());
					copiedBlock.resetIds();
					// Building block is not editable by the user directly.
					copiedBlock.setReadOnly(true);
					System.out.println("2.- "+ copiedBlock.getChildren());
					children.addAll(copiedBlock.getChildren());
				} catch (NotValidStorableObjectException e) {
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
			} else {
				children.add(child);
			}
		}
		System.out.println("---------------------------------");
		System.out.println(children);
		return children;
	}

	@Override
	public void setStatus(FormWorkStatus status) {
		if (form != null) {
			form.setStatus(status);
		}
	}

	@Override
	public FormWorkStatus getStatus() {
		if (form == null) {
			return null;
		}
		return form.getStatus();
	}

	@Override
	public Set<Integer> getLinkedFormVersions() {
		if (form == null) {
			return null;
		}
		return form.getLinkedFormVersions();
	}

	@Override
	public Long getLinkedFormOrganizationId() {
		if (form == null) {
			return null;
		}
		return form.getLinkedFormOrganizationId();
	}

	@Override
	public String getLinkedFormLabel() {
		if (form == null) {
			return null;
		}
		return form.getLinkedFormLabel();
	}

	@Override
	public boolean isLastVersion() {
		if (form == null) {
			return true;
		}
		return form.isLastVersion();
	}

}
