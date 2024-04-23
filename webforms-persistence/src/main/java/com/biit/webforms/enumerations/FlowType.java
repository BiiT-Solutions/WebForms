package com.biit.webforms.enumerations;

public enum FlowType {

    NORMAL(false, false),

    END_LOOP(true, true),

    END_FORM(true, false),

    ;

    private final boolean destinyNull;
    private final boolean onlyInRepeatableGroups;

    FlowType(boolean destinyNull, boolean onlyInRepeatableGroups) {
        this.destinyNull = destinyNull;
        this.onlyInRepeatableGroups = onlyInRepeatableGroups;
    }

    public boolean isDestinyNull() {
        return destinyNull;
    }

    public boolean isOnlyInRepeatableGroups() {
        return onlyInRepeatableGroups;
    }

    public static FlowType getDefaultFlowType() {
        return NORMAL;
    }

    public static FlowType from(String name) {
        for (FlowType flowType : FlowType.values()) {
            if (flowType.name().equalsIgnoreCase(name)) {
                return flowType;
            }
        }
        return null;
    }
}
