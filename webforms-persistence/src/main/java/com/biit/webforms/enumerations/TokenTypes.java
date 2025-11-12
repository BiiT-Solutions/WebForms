package com.biit.webforms.enumerations;

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

import com.biit.webforms.utils.parser.ITokenType;

import java.util.regex.Pattern;

public enum TokenTypes implements ITokenType {

    WHITESPACE("\\s+|\\n+", 0, " ", "", "", " "),
    // Only for UI consistency.
    RETURN("\\n", 0, "", "", "", "\u00B6"),

    LEFT_PAR("\\(", 1, "(", "(", "(", "("),

    RIGHT_PAR("\\)", 1, ")", ")", ")", ")"),

    AND("AND|and|&&", 1, "AND", "and", "&", "AND"),

    OR("OR|or|\\|\\|", 1, "OR", "or", "|", "OR"),

    NOT("NOT|not|!", 1, "NOT", "not", "!", "NOT"),

    GT(">", 1, ">", "&gt;", "gt", ">"),

    LT("<", 1, "<", "&lt;", "lt", "<"),

    GE(">=", 1, ">=", "&gt;=", "gt=", ">="),

    LE("<=", 1, "<=", "&lt;=", "lt=", "<="),

    EQ("==", 1, "==", "=", "=", "=="),

    NE("!=", 1, "<>", "!=", "!=", "<>"),

    BETWEEN("BETWEEN|between", 1, "BETWEEN", "between", "between", "BETWEEN"),

    IN("IN|in", 1, "IN", "in", "in", "IN"),

    EMPTY("EMPTY|empty", 1, "EMPTY", "empty", "empty", "EMPTY");

    private Pattern regexFilter;
    private int preference;
    private String stringForm;
    private String orbeonRepresentation;
    private String expressionSimplifierRepresentation;
    private String expressionEditorRepressentation;

    TokenTypes(String regexFilter, int precedence, String stringForm, String orbeonRepresentation,
               String expressionSimplifierRepresentation, String expressionEditorRepressentation) {
        this.regexFilter = Pattern.compile(regexFilter);
        this.preference = precedence;
        this.stringForm = stringForm;
        this.orbeonRepresentation = orbeonRepresentation;
        this.expressionSimplifierRepresentation = expressionSimplifierRepresentation;
        this.expressionEditorRepressentation = expressionEditorRepressentation;
    }

    @Override
    public String getRegexFilter() {
        return regexFilter.pattern();
    }

    @Override
    public Pattern getRegexFilterPattern() {
        return regexFilter;
    }

    @Override
    public int getLexerPrecedence() {
        return preference;
    }

    @Deprecated
    public String getStringForm() {
        return stringForm;
    }

    @Override
    public String toString() {
        return stringForm;
    }

    public static TokenTypes fromString(String string) {
        for (TokenTypes value : values()) {
            if (value.stringForm.equals(string)) {
                return value;
            }
        }
        return null;
    }

    public static TokenTypes from(String name) {
        for (TokenTypes value : values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public String getOrbeonRepresentation() {
        return orbeonRepresentation;
    }

    public String getExpressionSimplifierRepresentation() {
        return expressionSimplifierRepresentation;
    }

    public static TokenTypes getFromExpressionSimplifierRepresentation(String representation) {
        for (TokenTypes tokenTypes : TokenTypes.values()) {
            if (tokenTypes.getExpressionSimplifierRepresentation().equals(representation)) {
                return tokenTypes;
            }
        }
        return null;
    }

    public String getExpressionEditorRepresentation() {
        return expressionEditorRepressentation;
    }

}
