package com.biit.webforms.persistence.entity;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
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

import javax.persistence.Cacheable;

/**
 * As Lazy is not correctly configured, we use this class to show basic form information in the Form Manager.
 */
@Cacheable
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
            return other.getComparationId() == null;
        } else {
            return getComparationId().equals(other.getComparationId());
        }
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
