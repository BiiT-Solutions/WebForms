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

import com.biit.form.entity.BaseAnswer;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.computed.FlowConditionScript;
import com.biit.webforms.serialization.DynamicAnswerDeserializer;
import com.biit.webforms.serialization.DynamicAnswerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@JsonDeserialize(using = DynamicAnswerDeserializer.class)
@JsonSerialize(using = DynamicAnswerSerializer.class)
@Table(name = "tree_dynamic_answer")
@Cacheable
public class DynamicAnswer extends BaseAnswer implements FlowConditionScript {
    private static final long serialVersionUID = -1233082747412521896L;
    public static final String ANSWER_TAG_ALLOWED_CHARS = ".*";
    private static final Pattern NAME_ALLOWED = Pattern.compile(ANSWER_TAG_ALLOWED_CHARS);

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "reference")
    private Question reference;

    //Only for json serialization.
    @Transient
    private transient List<String> referencePath;

    public Question getReference() {
        return reference;
    }

    public void setReference(Question reference) {
        this.reference = reference;
    }

    public void setName(String name) throws FieldTooLongException, CharacterNotAllowedException {
        super.setName(getComparationId());
    }

    public String getReferenceName() {
        if (reference != null) {
            return "{" + reference.getPathName() + "}";
        } else {
            return "{}";
        }
    }

    @Override
    public String getScriptRepresentation() {
        return null;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof DynamicAnswer) {
            copyBasicInfo(object);

            DynamicAnswer dynamicQuestion = (DynamicAnswer) object;
            setReference(dynamicQuestion.getReference());
        } else {
            throw new NotValidTreeObjectException("Copy data for Question only supports the same type copy");
        }
    }

    @Override
    public Pattern getNameAllowedPattern() {
        return NAME_ALLOWED;
    }

    public List<String> getReferencePath() {
        return referencePath;
    }

    public void setReferencePath(List<String> referencePath) {
        this.referencePath = referencePath;
    }
}
