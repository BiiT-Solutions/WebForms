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

import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.serialization.TreeObjectVideoDeserializer;
import com.biit.webforms.serialization.TreeObjectVideoSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonDeserialize(using = TreeObjectVideoDeserializer.class)
@JsonSerialize(using = TreeObjectVideoSerializer.class)
@Table(name = "videos")
@Cacheable
public class TreeObjectVideo extends StorableObject {

    private static final long serialVersionUID = -2626435917683856455L;

    @OneToOne(optional = false)
    private TreeObject element;

    @Column(columnDefinition = "TEXT")
    private String url;

    private int width;

    private int height;


    public TreeObjectVideo() {

    }

    protected void resetDatabaseIds() {
        setId(null);
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        return new HashSet<>();
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object != null && object instanceof TreeObjectVideo) {
            copyBasicInfo(object);
            if (((TreeObjectVideo) object).getUrl() != null) {
                this.setUrl(((TreeObjectVideo) object).getUrl());
            }
        } else {
            throw new NotValidStorableObjectException("Copy data for Images only supports the same type copy");
        }
    }

    @Override
    public String toString() {
        return getUrl();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeObjectVideo)) return false;
        if (!super.equals(o)) return false;
        TreeObjectVideo that = (TreeObjectVideo) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), url);
    }

    public TreeObject getElement() {
        return element;
    }

    public void setElement(TreeObject element) {
        this.element = element;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
