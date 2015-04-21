package com.biit.webforms.persistence.entity;

import java.sql.Timestamp;
import java.util.Set;

import com.biit.form.entity.IBaseFormView;
import com.biit.webforms.enumerations.FormWorkStatus;

public class SimpleBlockView extends SimpleFormView implements IWebformsBlockView {
	private String name;
	private String label;
	private Integer version;
	private Long id;
	private Timestamp creationTime;
	private Long createdBy;
	private Timestamp updateTime;
	private Long updatedBy;
	private String comparationId;
	private Long organizationId;
	private boolean isLastVersion;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((comparationId == null) ? 0 : comparationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SimpleBlockView other = (SimpleBlockView) obj;
		if (comparationId == null) {
			if (other.getComparationId() != null) {
				return false;
			}
		} else if (!comparationId.equals(other.comparationId)) {
			return false;
		}
		return true;
	}

	public static SimpleBlockView getSimpleBlockView(IWebformsBlockView block) {
		SimpleBlockView view = new SimpleBlockView();
		view.name = block.getName();
		view.label = block.getLabel();
		view.version = block.getVersion();
		view.id = block.getId();
		view.creationTime = block.getCreationTime();
		view.createdBy = block.getCreatedBy();
		view.updateTime = block.getUpdateTime();
		view.updatedBy = block.getUpdatedBy();
		view.comparationId = block.getComparationId();
		view.organizationId = block.getOrganizationId();

		return view;
	}

	@Override
	public String toString() {
		return name + " " + label + " " + version;
	}

	@Override
	public boolean isLastVersion() {
		return isLastVersion;
	}

	public void setLastVersion(boolean isLastVersion) {
		this.isLastVersion = isLastVersion;
	}

	public IBaseFormView generateCopy(boolean copyParentHierarchy, boolean copyChilds) {
		return SimpleBlockView.getSimpleBlockView(this);
	}

	@Override
	public void setStatus(FormWorkStatus status) {
	}

	@Override
	public FormWorkStatus getStatus() {
		return null;
	}

	@Override
	public Set<Integer> getLinkedFormVersions() {
		return null;
	}

	@Override
	public Long getLinkedFormOrganizationId() {
		return null;
	}

	@Override
	public String getLinkedFormLabel() {
		return null;
	}
}
