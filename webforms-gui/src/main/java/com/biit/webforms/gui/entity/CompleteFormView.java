package com.biit.webforms.gui.entity;

import java.util.Set;

import com.biit.form.BaseForm;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.IWebformsFormView;

public class CompleteFormView extends BaseForm implements IWebformsFormView {
	private static final long serialVersionUID = -426480388117580446L;
	private Form form;

	public CompleteFormView(Form form) {
		this.form = form;
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
