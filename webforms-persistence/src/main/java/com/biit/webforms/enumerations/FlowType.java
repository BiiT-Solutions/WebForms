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
