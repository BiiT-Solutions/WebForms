package com.biit.webforms.persistence.entity.condition;

import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.serialization.TokenInDeserializer;
import com.biit.webforms.serialization.TokenInSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonDeserialize(using = TokenInDeserializer.class)
@JsonSerialize(using = TokenInSerializer.class)
@Table(name = "token_in")
public class TokenIn extends TokenComplex implements ITokenQuestion {
    private static final long serialVersionUID = -1264101992865476909L;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "tokenIn")
    @BatchSize(size = 10)
    @OrderBy(value = "sort_seq ASC")
    private List<TokenInValue> values;

    public TokenIn() {
        values = new ArrayList<TokenInValue>();
    }

    @Override
    public List<Token> getSimpleTokens() {
        List<Token> simpleTokens = new ArrayList<Token>();
        simpleTokens.add(Token.getLeftParenthesisToken());
        if (!values.isEmpty()) {
            simpleTokens.add(TokenComparationAnswer.getTokenEqual(getQuestion(), values.get(0).getAnswerValue()));
            for (int i = 1; i < values.size(); i++) {
                simpleTokens.add(TokenComparationValue.getOrToken());
                simpleTokens.add(TokenComparationAnswer.getTokenEqual(getQuestion(), values.get(i).getAnswerValue()));
            }

        }
        simpleTokens.add(Token.getRigthParenthesisToken());
        return simpleTokens;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof TokenIn) {
            super.copyData(object);
            TokenIn token = (TokenIn) object;
            this.values = new ArrayList<>();
            for (TokenInValue value : token.values) {
                TokenInValue valueCopy = value.generateCopy();
                valueCopy.setTokenIn(this);
                this.values.add(valueCopy);
            }
        } else {
            throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
                    + this.getClass().getName());
        }
    }

    @Override
    public void updateReferences(HashMap<String, TreeObject> mappedElements) {
        setQuestion((WebformsBaseQuestion) mappedElements.get(getQuestion().getOriginalReference()));
        for (TokenInValue value : values) {
            if (value.getAnswerValue() != null) {
                value.setAnswerValue((Answer) mappedElements.get(value.getAnswerValue().getOriginalReference()));
            }
        }
    }

    public static Token getTokenIn(WebformsBaseQuestion question, Answer... answers) {
        try {
            TokenIn token = new TokenIn();
            token.setType(TokenTypes.IN);
            token.setQuestion(question);
            for (Answer answer : answers) {
                TokenInValue value = new TokenInValue();
                value.setAnswerValue(answer);
                value.setTokenIn(token);
                token.values.add(value);
            }
            return token;
        } catch (NotValidTokenType e) {
            WebformsLogger.errorMessage(TokenComparationValue.class.getName(), e);
            return null;
        }
    }

    @Override
    public String toString() {
        String referenceString = null;
        if (getQuestion() != null) {
            referenceString = getQuestion().getName();
        }
        String answerString = null;
        if (values != null) {
            answerString = values.toString();
        }
        return referenceString + " " + getType() + " " + answerString;
    }

    public List<Answer> getAnswerValues() {
        List<Answer> answers = new ArrayList<Answer>();
        for (TokenInValue value : values) {
            answers.add(value.getAnswerValue());
        }
        return answers;
    }

    public void setAnswers(Answer[] answers) {
        values.clear();
        for (Answer answer : answers) {
            TokenInValue value = new TokenInValue();
            value.setAnswerValue(answer);
            values.add(value);
            value.setTokenIn(this);
        }
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        HashSet<StorableObject> innerStorableObjects = new HashSet<StorableObject>();

        for (TokenInValue value : values) {
            innerStorableObjects.add(value);
            innerStorableObjects.addAll(value.getAllInnerStorableObjects());
        }

        return innerStorableObjects;
    }

    @Override
    public void resetIds() {
        super.resetIds();
        for (TokenInValue value : values) {
            value.resetIds();
        }
    }

    public List<TokenInValue> getValues() {
        return values;
    }

    public void setValues(List<TokenInValue> values) {
        this.values.clear();
        for (TokenInValue value : values) {
            this.values.add(value);
            value.setTokenIn(this);
        }
    }

    @Override
    public String getExpressionEditorRepresentation() {
        return toString();
    }

    /**
     * Compares two token ComparationValue. it must be of token in type.
     */
    @Override
    public boolean isContentEqual(Token token) {
        if (token instanceof TokenIn) {
            TokenIn tokenIn = (TokenIn) token;
            if (super.isContentEqual(token)) {
                if (values.size() != tokenIn.values.size()) {
                    return false;
                }

                for (int i = 0; i < values.size(); i++) {
                    if (!values.get(i).isContentEqual(tokenIn.values.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void resetUserTimestampInfo(Long userId) {
        super.resetUserTimestampInfo(userId);
        for (TokenInValue value : getValues()) {
            value.resetUserTimestampInfo(userId);
        }
    }

    @Override
    public Boolean evaluate() {
        throw new UnsupportedOperationException();
    }
}
