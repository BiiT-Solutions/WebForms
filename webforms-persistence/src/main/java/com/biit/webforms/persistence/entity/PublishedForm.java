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


import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.persistence.entity.BaseStorableObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.webforms.serialization.PublishedFormDeserializer;
import com.biit.webforms.serialization.PublishedFormSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.AttributeOverride;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@JsonDeserialize(using = PublishedFormDeserializer.class)
@JsonSerialize(using = PublishedFormSerializer.class)
@Table(name = "tree_forms_published", uniqueConstraints = {@UniqueConstraint(columnNames = {"label", "version", "organization_id"})})
@AttributeOverride(name = "label", column = @Column(length = StorableObject.MAX_UNIQUE_COLUMN_LENGTH, columnDefinition = "varchar("
        + StorableObject.MAX_UNIQUE_COLUMN_LENGTH + ")"))
@Cacheable()
public class PublishedForm extends BaseStorableObject {

    @Column(name = "label", length = 1000, nullable = false)
    private String label = "";

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Column(name = "organization_id", nullable = false, columnDefinition = "DOUBLE")
    private Long organizationId;

    @Lob
    @Column(name = "json", nullable = false, length = Form.MAX_JSON_LENGTH)
    private String jsonCode;

    public PublishedForm() {
        super();
    }

    public PublishedForm(Form form) {
        this();
        setLabel(form.getLabel());
        setVersion(form.getVersion());
        setOrganizationId(form.getOrganizationId());
        setJsonCode(form.toJson());
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getJsonCode() {
        return jsonCode;
    }

    public void setJsonCode(String jsonCode) {
        this.jsonCode = jsonCode;
    }

    public String toJson() {
        try {
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static PublishedForm fromJson(String jsonString) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().readValue(jsonString, PublishedForm.class);
    }

    @Override
    public String toString() {
        return "PublishedForm{"
                + "label='" + label + '\''
                + ", version=" + version
                + ", organizationId=" + organizationId
                + '}';
    }
}
