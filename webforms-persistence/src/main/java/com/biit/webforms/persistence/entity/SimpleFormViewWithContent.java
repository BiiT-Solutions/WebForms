package com.biit.webforms.persistence.entity;

import javax.persistence.Cacheable;

/**
 * As Lazy is not correctly configured, we use this class to show basic form information in the Form Manager.
 */
@Cacheable(true)
public class SimpleFormViewWithContent extends SimpleFormView {

    private String json;

    public SimpleFormViewWithContent() {

    }

    public SimpleFormViewWithContent(Form form) {
        super(form);
        setJson(form.toJson());
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
        SimpleFormViewWithContent other = (SimpleFormViewWithContent) obj;
        if (getComparationId() == null) {
            if (other.getComparationId() != null) {
                return false;
            }
        } else if (!getComparationId().equals(other.getComparationId())) {
            return false;
        }
        return true;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public static SimpleFormViewWithContent get(Form form) {
        final SimpleFormViewWithContent simpleFormView = new SimpleFormViewWithContent();
        simpleFormView.setName(form.getName());
        simpleFormView.setLabel(form.getLabel());
        simpleFormView.setVersion(form.getVersion());
        simpleFormView.setId(form.getId());
        simpleFormView.setCreationTime(form.getCreationTime());
        simpleFormView.setCreatedBy(form.getCreatedBy());
        simpleFormView.setUpdateTime(form.getUpdateTime());
        simpleFormView.setUpdatedBy(form.getUpdatedBy());
        simpleFormView.setComparationId(form.getComparationId());
        simpleFormView.setOrganizationId(form.getOrganizationId());
        simpleFormView.setLinkedFormLabel(form.getLinkedFormLabel());
        simpleFormView.setLinkedFormVersions(form.getLinkedFormVersions());
        simpleFormView.setLinkedFormOrganizationId(form.getLinkedFormOrganizationId());
        simpleFormView.setStatus(form.getStatus());
        simpleFormView.setLastVersion(form.isLastVersion());
        simpleFormView.setFormReferenceId(form.getFormReferenceId());
        return simpleFormView;
    }
}
