package com.biit.webforms.persistence.entity;

import java.sql.Timestamp;
import java.util.Set;

import com.biit.webforms.enumerations.FormWorkStatus;

public class SimpleFormView implements IWebformsFormView {
	private String name;
	private String label;
	private Integer version;
	private Long id;
	private Timestamp creationTime;
	private Long createdBy;
	private Timestamp updateTime;
	private Long updatedBy;
	private String comparationId;
	private long organizationId;
	private String linkedFormLabel;
	private Set<Integer> linkedFormVersions;
	private long linkedFormOrganizationId;
	private FormWorkStatus status;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Integer getVersion() {
		return version;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public Timestamp getCreationTime() {
		return creationTime;
	}

	@Override
	public Long getCreatedBy() {
		return createdBy;
	}

	@Override
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	@Override
	public Long getUpdatedBy() {
		return updatedBy;
	}

	@Override
	public String getComparationId() {
		return comparationId;
	}

	@Override
	public Long getOrganizationId() {
		return organizationId;
	}

	@Override
	public String getLinkedFormLabel() {
		return linkedFormLabel;
	}

	@Override
	public Set<Integer> getLinkedFormVersions() {
		return linkedFormVersions;
	}

	@Override
	public Long getLinkedFormOrganizationId() {
		return linkedFormOrganizationId;
	}
	
	@Override
	public FormWorkStatus getStatus() {
		return status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setComparationId(String comparationId) {
		this.comparationId = comparationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public void setLinkedFormLabel(String linkedFormLabel) {
		this.linkedFormLabel = linkedFormLabel;
	}

	public void setLinkedFormVersions(Set<Integer> linkedFormVersions) {
		this.linkedFormVersions = linkedFormVersions;
	}

	public void setLinkedFormOrganizationId(long linkedFormOrganizationId) {
		this.linkedFormOrganizationId = linkedFormOrganizationId;
	}

	public void setStatus(FormWorkStatus status) {
		this.status = status;
	}
}
