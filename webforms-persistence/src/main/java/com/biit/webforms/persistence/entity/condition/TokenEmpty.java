package com.biit.webforms.persistence.entity.condition;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.serialization.TokenEmptyDeserializer;
import com.biit.webforms.serialization.TokenEmptySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@JsonDeserialize(using = TokenEmptyDeserializer.class)
@JsonSerialize(using = TokenEmptySerializer.class)
@Table(name = "token_empty")
public class TokenEmpty extends TokenComparationValue implements ITokenQuestion {
    private static final long serialVersionUID = -1264101992865476909L;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswerSubformat subformat;

    private String value;

    public TokenEmpty() {
    }

    public static Token getTokenEmpty(WebformsBaseQuestion question, String value) {
        try {
            TokenEmpty token = new TokenEmpty();
            token.setType(TokenTypes.EMPTY);
            token.setQuestion(question);
            token.setContent(value);
            return token;
        } catch (NotValidTokenType e) {
            WebformsLogger.errorMessage(TokenComparationValue.class.getName(), e);
            return null;
        }
    }

    public void setContent(String value) {
        if (getQuestion() != null) {
            subformat = getQuestion().getAnswerSubformat();
        } else {
            subformat = AnswerSubformat.DATE_PERIOD;
        }
        this.value = value;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof TokenEmpty) {
            super.copyData(object);
            TokenEmpty token = (TokenEmpty) object;
            this.subformat = token.subformat;
            this.value = token.value;
        } else {
            throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
                    + this.getClass().getName());
        }
    }

    @Override
    public String toString() {
        String referenceString = null;
        if (getQuestion() != null) {
            referenceString = getQuestion().getName();
        }
        return referenceString + "." + getType();
    }

    @Override
    public String getExpressionEditorRepresentation() {
        return toString();
    }

    @Override
    public Boolean evaluate() {
        return value == null || value.isEmpty();
    }

    public AnswerSubformat getSubformat() {
        return subformat;
    }

    public void setSubformat(AnswerSubformat subformat) {
        this.subformat = subformat;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
