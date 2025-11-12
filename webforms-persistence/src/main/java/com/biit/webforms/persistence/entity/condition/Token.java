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
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.serialization.TokenDeserializer;
import com.biit.webforms.serialization.TokenSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for any kind of token.
 */
@Entity
@JsonDeserialize(using = TokenDeserializer.class)
@JsonSerialize(using = TokenSerializer.class)
@Table(name = "token")
public class Token extends StorableObject {
    private static final long serialVersionUID = 113069074950725476L;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenTypes type;

    @Column(name = "sort_sequence", nullable = false)
    private long sortSeq = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flow")
    private Flow flow;

    public Token() {
        super();
    }

    public Token(TokenTypes tokenType) throws NotValidTokenType {
        setType(tokenType);
    }

    public TokenTypes[] getValidTokenTypes() {
        return null;
    }

    private boolean isValidTokenType(TokenTypes tokenType) {
        TokenTypes[] tokenTypes = getValidTokenTypes();
        if (tokenTypes == null) {
            return true;
        }
        for (int i = 0; i < tokenTypes.length; i++) {
            if (tokenTypes[i].equals(tokenType)) {
                return true;
            }
        }
        return false;
    }

    public TokenTypes getType() {
        return type;
    }

    public void setType(TokenTypes tokenType) throws NotValidTokenType {
        if (isValidTokenType(tokenType)) {
            this.type = tokenType;
        } else {
            throw new NotValidTokenType(
                    "Token type " + tokenType + " is not valid for token class " + this.getClass().getName());
        }
    }

    @Override
    public String toString() {
        return type.toString();
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        return new HashSet<>();
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof Token) {
            copyBasicInfo(object);
            Token token = (Token) object;
            this.type = token.type;
            this.sortSeq = token.sortSeq;
        } else {
            throw new NotValidStorableObjectException(
                    object.getClass().getName() + " is not compatible with " + Token.class.getName());
        }
    }

    public Token generateCopy() {
        try {
            Token newInstance = this.getClass().getDeclaredConstructor().newInstance();
            newInstance.copyData(this);
            return newInstance;
        } catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException
                 | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            // Impossible
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            return null;
        }
    }

    public long getSortSeq() {
        return sortSeq;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public void setSortSeq(long sortSeq) {
        this.sortSeq = sortSeq;
    }

    public static Token getToken(TokenTypes tokenType) {
        try {
            return new Token(tokenType);
        } catch (NotValidTokenType e) {
            // Impossible
            WebformsLogger.errorMessage(Token.class.getName(), e);
            return null;
        }
    }

    public void updateReferences(HashMap<String, TreeObject> mappedElements) {
        // There are no references to update
    }

    public static Token getNotToken() {
        return getToken(TokenTypes.NOT);
    }

    public static Token getLeftParenthesisToken() {
        return getToken(TokenTypes.LEFT_PAR);
    }

    public static Token getRigthParenthesisToken() {
        return getToken(TokenTypes.RIGHT_PAR);
    }

    public static Token getAndToken() {
        return getToken(TokenTypes.AND);
    }

    public static Token getOrToken() {
        return getToken(TokenTypes.OR);
    }

    /**
     * Returns a copy with the inverse of the current token comparation.
     *
     * @return
     */
    public Token inverse() {
        try {
            Token copiedToken = generateCopy();
            switch (getType()) {
                case LT:
                    copiedToken.setType(TokenTypes.GE);
                    break;
                case LE:
                    copiedToken.setType(TokenTypes.GT);
                    break;
                case GT:
                    copiedToken.setType(TokenTypes.LE);
                    break;
                case GE:
                    copiedToken.setType(TokenTypes.LT);
                    break;
                case EQ:
                    copiedToken.setType(TokenTypes.NE);
                    break;
                case NE:
                    copiedToken.setType(TokenTypes.EQ);
                    break;
                case AND:
                    copiedToken.setType(TokenTypes.AND);
                    break;
                case BETWEEN:
                    copiedToken.setType(TokenTypes.BETWEEN);
                    break;
                case EMPTY:
                    copiedToken.setType(TokenTypes.EMPTY);
                    break;
                case IN:
                    copiedToken.setType(TokenTypes.IN);
                    break;
                case LEFT_PAR:
                    copiedToken.setType(TokenTypes.LEFT_PAR);
                    break;
                case NOT:
                    copiedToken.setType(TokenTypes.NOT);
                    break;
                case OR:
                    copiedToken.setType(TokenTypes.OR);
                    break;
                case RETURN:
                    copiedToken.setType(TokenTypes.RETURN);
                    break;
                case RIGHT_PAR:
                    copiedToken.setType(TokenTypes.RIGHT_PAR);
                    break;
                case WHITESPACE:
                    copiedToken.setType(TokenTypes.WHITESPACE);
                    break;
                default:
                    break;
            }

            return copiedToken;
        } catch (NotValidTokenType e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }
        return null;
    }

    public String getExpressionSimplifierRepresentation() {
        return type.getExpressionSimplifierRepresentation();
    }

    public static Token getFromSimplifierRepresentation(String representation) {
        TokenTypes tokenTypes = TokenTypes.getFromExpressionSimplifierRepresentation(representation);
        if (tokenTypes == null) {
            return null;
        }
        return getToken(tokenTypes);
    }

    public String getExpressionEditorRepresentation() {
        return type.getExpressionEditorRepresentation();
    }

    public boolean isContentEqual(Token token) {
        if (type != token.type) {
            return false;
        }
        return true;
    }
}
