package com.biit.webforms.persistence.entity;

import java.util.Set;

import com.biit.form.entity.IBaseFormView;
import com.biit.webforms.enumerations.FormWorkStatus;

public interface IWebformsFormView extends IBaseFormView {

	public void setStatus(FormWorkStatus status);

	public FormWorkStatus getStatus();

	public Set<Integer> getLinkedFormVersions();

	public Long getLinkedFormOrganizationId();

	public String getLinkedFormLabel();

	public boolean isLastVersion();
}
