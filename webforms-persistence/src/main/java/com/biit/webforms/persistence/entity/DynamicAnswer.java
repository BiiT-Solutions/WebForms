package com.biit.webforms.persistence.entity;

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
@Cacheable(true)
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
