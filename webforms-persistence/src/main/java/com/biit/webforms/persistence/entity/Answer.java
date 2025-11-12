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
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.computed.FlowConditionScript;
import com.biit.webforms.persistence.entity.exceptions.ElementIsUsedAsDefaultValueException;
import com.biit.webforms.serialization.AnswerDeserializer;
import com.biit.webforms.serialization.AnswerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Answer is a class that contains the information of a defined and possible
 * answer to a multiple choice question.
 * <p>
 * -Has the next properties: name (value for client purposes, the method get/set
 * name and value affect the same parameter)
 * <p>
 * -label
 * <p>
 * -description
 * <p>
 */
@Entity
@JsonDeserialize(using = AnswerDeserializer.class)
@JsonSerialize(using = AnswerSerializer.class)
@Table(name = "tree_answers")
@Cacheable()
public class Answer extends BaseAnswer implements FlowConditionScript, ElementWithMedia, ElementWithDescription, ElementWithTranslation {
    private static final long serialVersionUID = 7614678800982506178L;
    private static final List<Class<? extends TreeObject>> ALLOWED_CHILDREN = new ArrayList<>(Collections.singletonList(Answer.class));
    public static final int MAX_DESCRIPTION_LENGTH = 10000;

    @Column(length = MAX_DESCRIPTION_LENGTH, columnDefinition = "varchar(" + MAX_DESCRIPTION_LENGTH + ")")
    private String description;

    @OneToOne(mappedBy = "element", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private TreeObjectImage image;

    @OneToOne(mappedBy = "element", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private TreeObjectVideo video;

    @OneToOne(mappedBy = "element", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private TreeObjectAudio audio;

    @SuppressWarnings("JpaAttributeTypeInspection")
    @ElementCollection
    @CollectionTable(name = "description_translations")
    @MapKeyColumn(name = "language")
    @Column(name = "translation")
    private Map<String, String> descriptionTranslations;

    public Answer() {
        super();
        description = "";
        descriptionTranslations = new HashMap<>();
    }

    public Answer(String name) throws FieldTooLongException, CharacterNotAllowedException {
        super(name);
        setValue(name);
        description = "";
        descriptionTranslations = new HashMap<>();
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (image != null) {
            image.resetIds();
        }
        if (video != null) {
            video.resetIds();
        }
        if (audio != null) {
            audio.resetIds();
        }
    }

    @Override
    protected void resetDatabaseIds() {
        super.resetDatabaseIds();
        if (image != null) {
            image.resetDatabaseIds();
        }
        if (video != null) {
            video.resetDatabaseIds();
        }
        if (audio != null) {
            audio.resetDatabaseIds();
        }
    }

    @Override
    protected List<Class<? extends TreeObject>> getAllowedChildren() {
        return ALLOWED_CHILDREN;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof Answer) {
            copyBasicInfo(object);
            if (((Answer) object).getDescription() != null) {
                description = ((Answer) object).getDescription();
            }
            if (((Answer) object).getDescriptionTranslations() != null) {
                descriptionTranslations = new HashMap<>(((Answer) object).getDescriptionTranslations());
            }
        } else {
            throw new NotValidTreeObjectException("Copy data for Answer only supports the same type copy");
        }
    }

    /**
     * Set value is an alias for {@link Answer#setLabel(String)}
     */
    public void setValue(String value) throws FieldTooLongException, CharacterNotAllowedException {
        setName(value);
    }

    /**
     * Get Value is an alias for {@link Answer#getName()}
     */
    public String getValue() {
        return getName();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getScriptRepresentation() {
        return getScriptValueRepresentation(getName());
    }

    public static String getScriptValueRepresentation(String value) {
        return "'" + value + "'";
    }

    /**
     * Checks if this answer is a subanswer by looking if it has a parent and if
     * it has it, if is an answer.
     */
    public boolean isSubanswer() {
        return getParent() != null && getParent() instanceof Answer;
    }

    public String getPathAnswerValue() {
        if (getParent() == null || !(getParent() instanceof Answer)) {
            return getValue();
        } else {
            return getParent().getPathName() + TreeObject.DEFAULT_PATH_SEPARATOR + getValue();
        }
    }

    @Override
    public String toString() {
        return getValue();
    }

    /**
     * A final answer is an answer that doesn't contain children
     */
    public boolean isFinalAnswer() {
        return getChildren().isEmpty();
    }

    public int exportToJavaCode(StringBuilder sb, int counter) {
        String idName = "el_" + counter;

        sb.append("Answer ").append(idName).append("  = new Answer();").append(System.lineSeparator());
        sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
        sb.append(idName).append(".setLabel(\"").append(this.getLabel()).append("\");").append(System.lineSeparator());

        return counter;
    }

    @Override
    public void checkDependencies() throws DependencyExistException {
        Form form = (Form) this.getAncestor(Form.class);
        if (form == null) {
            return;
        }

        for (Flow flow : form.getFlows()) {
            if (flow.isDependent(this)) {
                throw new DependencyExistException("Flow '" + flow + "' depends of element '" + this + "'");
            }
        }

        //Check default answers.
        if (getParent() != null && getParent() instanceof Question) {
            final Question question = (Question) getParent();
            if (question.getDefaultValueAnswer() != null && Objects.equals(question.getDefaultValueAnswer(), this)) {
                throw new ElementIsUsedAsDefaultValueException("Question '" + question + "' has '" + this + "' as default value.");
            }
        }
    }

    @Override
    public void setImage(TreeObjectImage image) {
        this.image = image;
        if (image != null) {
            image.setElement(this);
        }
    }

    @Override
    public TreeObjectImage getImage() {
        return image;
    }

    @Override
    public void setVideo(TreeObjectVideo video) {
        this.video = video;
        if (video != null) {
            video.setElement(this);
        }
    }

    @Override
    public TreeObjectVideo getVideo() {
        return video;
    }

    @Override
    public void setAudio(TreeObjectAudio audio) {
        this.audio = audio;
        if (audio != null) {
            audio.setElement(this);
        }
    }

    @Override
    public TreeObjectAudio getAudio() {
        return audio;
    }

    @Override
    public Map<String, String> getDescriptionTranslations() {
        return descriptionTranslations;
    }

    @Override
    public void setDescriptionTranslations(Map<String, String> descriptionTranslations) {
        this.descriptionTranslations = descriptionTranslations;
    }
}
