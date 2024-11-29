package com.biit.webforms.persistence.entity;

import com.biit.form.entity.IBaseFormView;
import com.biit.webforms.enumerations.FormWorkStatus;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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
    private Long organizationId;
    private String linkedFormLabel;
    private Set<Integer> linkedFormVersions;
    private Long linkedFormOrganizationId;
    private FormWorkStatus status;
    private boolean isLastVersion;
    private Long formReferenceId;

    private boolean hasJson;

    public SimpleFormView() {

    }

    public SimpleFormView(Form form) {
        setName(form.getName());
        setLabel(form.getLabel());
        setVersion(form.getVersion());
        setId(form.getId());
        setCreationTime(form.getCreationTime());
        setCreatedBy(form.getCreatedBy());
        setUpdateTime(form.getUpdateTime());
        setUpdatedBy(form.getUpdatedBy());
        setComparationId(form.getComparationId());
        setOrganizationId(form.getOrganizationId());
        setLinkedFormLabel(form.getLinkedFormLabel());
        setLinkedFormVersions(form.getLinkedFormVersions());
        setLinkedFormOrganizationId(form.getLinkedFormOrganizationId());
        setStatus(form.getStatus());
        setLastVersion(form.isLastVersion());
        setFormReferenceId(form.getFormReferenceId());
    }

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

    @Override
    public void setStatus(FormWorkStatus status) {
        this.status = status;
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
        SimpleFormView other = (SimpleFormView) obj;
        if (comparationId == null) {
            return other.comparationId == null;
        } else return comparationId.equals(other.comparationId);
    }

    public static SimpleFormView getSimpleFormView(IWebformsFormView form) {
        final SimpleFormView view = new SimpleFormView();
        view.name = form.getName();
        view.label = form.getLabel();
        view.version = form.getVersion();
        view.id = form.getId();
        view.creationTime = form.getCreationTime();
        view.createdBy = form.getCreatedBy();
        view.updateTime = form.getUpdateTime();
        view.updatedBy = form.getUpdatedBy();
        view.comparationId = form.getComparationId();
        view.organizationId = form.getOrganizationId();
        view.linkedFormLabel = form.getLinkedFormLabel();
        view.linkedFormVersions = new HashSet<>(form.getLinkedFormVersions());
        view.linkedFormOrganizationId = form.getLinkedFormOrganizationId();
        view.status = form.getStatus();
        view.setHasJson(form.hasJson());

        return view;
    }

    public static SimpleFormView getSimpleFormView(IWebformsBlockView form) {
        SimpleFormView view = new SimpleFormView();
        view.name = form.getName();
        view.label = form.getLabel();
        view.version = form.getVersion();
        view.id = form.getId();
        view.creationTime = form.getCreationTime();
        view.createdBy = form.getCreatedBy();
        view.updateTime = form.getUpdateTime();
        view.updatedBy = form.getUpdatedBy();
        view.comparationId = form.getComparationId();
        view.organizationId = form.getOrganizationId();
        view.linkedFormLabel = form.getLinkedFormLabel();
        view.linkedFormVersions = new HashSet<>(form.getLinkedFormVersions());
        view.linkedFormOrganizationId = form.getLinkedFormOrganizationId();
        view.status = form.getStatus();

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

    public IBaseFormView generateCopy(boolean copyParentHierarchy, boolean copyChildren) {
        return SimpleFormView.getSimpleFormView(this);
    }

    public Long getFormReferenceId() {
        return formReferenceId;
    }

    public void setFormReferenceId(Long formReferenceId) {
        this.formReferenceId = formReferenceId;
    }

    @Override
    public Set<TreeObjectImage> getAllImages() {
        return new HashSet<>();
    }

    @Override
    public boolean hasJson() {
        return hasJson;
    }

    @Override
    public void setHasJson(boolean hasJson) {
        this.hasJson = hasJson;
    }
}
