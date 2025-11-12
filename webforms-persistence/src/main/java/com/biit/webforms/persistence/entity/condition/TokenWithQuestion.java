package com.biit.webforms.persistence.entity.condition;

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
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.serialization.TokenWithQuestionDeserializer;
import com.biit.webforms.serialization.TokenWithQuestionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.List;

@Entity
@JsonDeserialize(using = TokenWithQuestionDeserializer.class)
@JsonSerialize(using = TokenWithQuestionSerializer.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TokenWithQuestion extends Token {
    private static final long serialVersionUID = 3644011189971053183L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question")
    private WebformsBaseQuestion question;

    //Only for json serialization.
    @Transient
    private transient List<String> questionReferencePath;

    public TokenWithQuestion() {
        super();
    }

    public TokenWithQuestion(TokenTypes tokenType) throws NotValidTokenType {
        setType(tokenType);
    }

    public WebformsBaseQuestion getQuestion() {
        return question;
    }

    public void setQuestion(WebformsBaseQuestion question) {
        this.question = question;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof TokenWithQuestion) {
            super.copyData(object);
            TokenWithQuestion token = (TokenWithQuestion) object;
            this.question = token.getQuestion();
        } else {
            throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with " + TokenWithQuestion.class.getName());
        }
    }

    @Override
    public void updateReferences(HashMap<String, TreeObject> mappedElements) {
        if (question != null) {
            question = (WebformsBaseQuestion) mappedElements.get(question.getOriginalReference());
        }
    }

    @Override
    public boolean isContentEqual(Token token) {
        if (token instanceof TokenWithQuestion) {
            if (super.isContentEqual(token)) {
                if (!getQuestion().getPathName().equals(((TokenWithQuestion) token).getQuestion().getPathName())) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }


    public List<String> getQuestionReferencePath() {
        return questionReferencePath;
    }

    public void setQuestionReferencePath(List<String> questionReferencePath) {
        this.questionReferencePath = questionReferencePath;
    }
}
