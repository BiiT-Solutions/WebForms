package com.biit.webforms.persistence.entity;

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
import java.util.Set;

@Entity
@JsonDeserialize(using = TreeObjectVideoDeserializer.class)
@JsonSerialize(using = TreeObjectVideoSerializer.class)
@Table(name = "videos")
@Cacheable(true)
public class TreeObjectVideo extends StorableObject {

    private static final long serialVersionUID = -2626435917683856455L;

    @OneToOne(optional = false)
    private TreeObject element;

    @Column(columnDefinition = "TEXT")
    private String url;

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
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TreeObjectVideo other = (TreeObjectVideo) obj;
        if (url == null) {
            return other.url == null;
        }
        return true;
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
