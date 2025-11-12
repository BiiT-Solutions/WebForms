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

public enum AnswerType {
    SINGLE_SELECTION_RADIO(null, true, true, false, true, false, false, false, false),

    SINGLE_SELECTION_LIST(null, true, false, null, true, false, false, false, false),

    SINGLE_SELECTION_SLIDER(null, true, false, true, true, false, false, true, true),

    MULTIPLE_SELECTION(null, true, true, null, true, false, true, false, false),

    // Uses answer format.
    INPUT(AnswerFormat.TEXT, false, false, null, true, true, false, false, false),

    TEXT_AREA(null, false, false, null, false, true, false, false, false);

    private final AnswerFormat defaultAnswerFormat;
    private final boolean childrenAllowed;
    private final boolean subChildrenAllowed;
    private final Boolean defaultHorizontal;
    private final Boolean defaultMandatory;
    private final boolean defaultValueEnabled;
    private final boolean maxAnswersSelectedEnabled;
    private final boolean inverseAnswerOrder;
    private final boolean answersVisibleOnTooltip;

    private AnswerType(AnswerFormat defaultAnswerType, boolean childrenAllowed, boolean subchildrenAllowed, Boolean defaultHorizontal, Boolean defaultMandatory,
                       boolean defaultValueEnabled, boolean maxAnswersSelectedEnabled, boolean inverseAnswerOrder, boolean answersVisibleOnTooltip) {
        this.defaultAnswerFormat = defaultAnswerType;
        this.childrenAllowed = childrenAllowed;
        this.defaultHorizontal = defaultHorizontal;
        this.defaultMandatory = defaultMandatory;
        this.subChildrenAllowed = subchildrenAllowed;
        this.defaultValueEnabled = defaultValueEnabled;
        this.maxAnswersSelectedEnabled = maxAnswersSelectedEnabled;
        this.inverseAnswerOrder = inverseAnswerOrder;
        this.answersVisibleOnTooltip = answersVisibleOnTooltip;
    }

    public boolean isAnswerFormatEnabled() {
        return defaultAnswerFormat != null;
    }

    public boolean isHorizontalEnabled() {
        return defaultHorizontal != null;
    }

    public Boolean getDefaultHorizontal() {
        if (defaultHorizontal != null) {
            return defaultHorizontal;
        }
        return false;
    }

    public AnswerFormat getDefaultAnswerFormat() {
        return defaultAnswerFormat;
    }

    public boolean isChildrenAllowed() {
        return childrenAllowed;
    }

    public boolean isMandatoryEnabled() {
        return defaultMandatory != null;
    }

    public boolean getDefaultMandatory() {
        if (defaultMandatory != null) {
            return defaultMandatory;
        }
        return false;
    }

    public boolean isSubChildrenAllowed() {
        return subChildrenAllowed;
    }

    public boolean isDefaultValueEnabled() {
        return defaultValueEnabled;
    }

    public static AnswerType from(String name) {
        for (AnswerType answerType : AnswerType.values()) {
            if (answerType.name().equalsIgnoreCase(name)) {
                return answerType;
            }
        }
        return null;
    }

    public boolean isMaxAnswersSelectedEnabled() {
        return maxAnswersSelectedEnabled;
    }

    public boolean isAnswersValuesOnTooltipEnabled() {
        return answersVisibleOnTooltip;
    }

    public boolean isInverseAnswerOrder() {
        return inverseAnswerOrder;
    }
}
