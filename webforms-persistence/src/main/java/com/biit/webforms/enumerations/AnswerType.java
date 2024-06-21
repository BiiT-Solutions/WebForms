package com.biit.webforms.enumerations;

public enum AnswerType {
    SINGLE_SELECTION_RADIO(null, true, true, false, true, false, false),

    SINGLE_SELECTION_LIST(null, true, false, null, true, false, false),

    SINGLE_SELECTION_SLIDER(null, true, false, false, true, false, false),

    MULTIPLE_SELECTION(null, true, true, null, true, false, false),

    // Uses answer format.
    INPUT(AnswerFormat.TEXT, false, false, null, true, true, false),

    TEXT_AREA(null, false, false, null, false, true, false);

    private final AnswerFormat defaultAnswerFormat;
    private final boolean childrenAllowed;
    private final boolean subChildrenAllowed;
    private final Boolean defaultHorizontal;
    private final Boolean defaultMandatory;
    private final boolean defaultValueEnabled;
    private final boolean maxAnswersSelectedEnabled;

    private AnswerType(AnswerFormat defaultAnswerType, boolean childrenAllowed, boolean subchildrenAllowed, Boolean defaultHorizontal, Boolean defaultMandatory,
                       boolean defaultValueEnabled, boolean maxAnswersSelectedEnabled) {
        this.defaultAnswerFormat = defaultAnswerType;
        this.childrenAllowed = childrenAllowed;
        this.defaultHorizontal = defaultHorizontal;
        this.defaultMandatory = defaultMandatory;
        this.subChildrenAllowed = subchildrenAllowed;
        this.defaultValueEnabled = defaultValueEnabled;
        this.maxAnswersSelectedEnabled = maxAnswersSelectedEnabled;
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
}
