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
import com.biit.webforms.serialization.TreeObjectAudioDeserializer;
import com.biit.webforms.serialization.TreeObjectAudioSerializer;
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
@JsonDeserialize(using = TreeObjectAudioDeserializer.class)
@JsonSerialize(using = TreeObjectAudioSerializer.class)
@Table(name = "audios")
@Cacheable
public class TreeObjectAudio extends StorableObject {

    private static final long serialVersionUID = 3999491361852197724L;

    @OneToOne(optional = false)
    private TreeObject element;

    @Column(columnDefinition = "TEXT")
    private String url;

    public TreeObjectAudio() {

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
        if (object != null && object instanceof TreeObjectAudio) {
            copyBasicInfo(object);
            if (((TreeObjectAudio) object).getUrl() != null) {
                this.setUrl(((TreeObjectAudio) object).getUrl());
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
        if (!(o instanceof TreeObjectAudio)) return false;
        if (!super.equals(o)) return false;
        TreeObjectAudio that = (TreeObjectAudio) o;
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

}
