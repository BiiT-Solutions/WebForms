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

import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.serialization.GroupDeserializer;
import com.biit.webforms.serialization.GroupSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Entity
@JsonDeserialize(using = GroupDeserializer.class)
@JsonSerialize(using = GroupSerializer.class)
@Table(name = "tree_groups")
@Cacheable
public class Group extends BaseRepeatableGroup implements ElementWithTranslation {
    private static final long serialVersionUID = 5363295280240190378L;
    private static final boolean DEFAULT_REPEATABLE = false;

    @Column(name = "is_table", nullable = false)
    private boolean showAsTable = false;

    @Column(name = "number_of_column", nullable = false, columnDefinition = "int default 1")
    private int numberOfColumns = 1;

    @Column(name = "total_answers_value")
    private Integer totalAnswersValue;

    public Group() {
        super();
        setRepeatable(DEFAULT_REPEATABLE);
    }

    public Group(String name) throws FieldTooLongException, CharacterNotAllowedException {
        super(name);
        setRepeatable(DEFAULT_REPEATABLE);
    }

    public int exportToJavaCode(StringBuilder sb, int counter) {
        String idName = "el_" + counter;

        sb.append("Group ").append(idName).append("  = new Group();").append(System.lineSeparator());
        sb.append(idName).append(".setName(\"").append(this.getName()).append("\");").append(System.lineSeparator());
        sb.append(idName).append(".setLabel(\"").append(this.getLabel()).append("\");").append(System.lineSeparator());
        if (isRepeatable()) {
            sb.append(idName).append(".setRepeatable(true);").append(System.lineSeparator());
        } else {
            sb.append(idName).append(".setRepeatable(false);").append(System.lineSeparator());
        }

        int currentCounter = counter;
        for (TreeObject child : getChildren()) {
            int tempCounter = currentCounter + 1;
            if (child instanceof Group) {
                currentCounter = ((Group) child).exportToJavaCode(sb, currentCounter + 1);
            }
            if (child instanceof Question) {
                currentCounter = ((Question) child).exportToJavaCode(sb, currentCounter + 1);
            }
            if (child instanceof Text) {
                currentCounter = ((Text) child).exportToJavaCode(sb, currentCounter + 1);
            }
            if (child instanceof SystemField) {
                currentCounter = ((SystemField) child).exportToJavaCode(sb, currentCounter + 1);
            }
            sb.append("//group").append(System.lineSeparator());
            sb.append(idName).append(".addChild(").append("el_" + tempCounter).append(");").append(System.lineSeparator());
        }
        return currentCounter;
    }

    @Override
    public boolean isContentEqual(TreeObject treeObject) {
        if (treeObject instanceof Group) {
            return super.isContentEqual(treeObject);
        }
        return false;
    }

    public boolean isShownAsTable() {
        return showAsTable;
    }

    public void setShownAsTable(boolean isTable) {
        this.showAsTable = isTable;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = Math.max(numberOfColumns, 1);
    }

    public boolean isShowAsTable() {
        return showAsTable;
    }

    public void setShowAsTable(boolean showAsTable) {
        this.showAsTable = showAsTable;
    }

    public Integer getTotalAnswersValue() {
        return totalAnswersValue;
    }

    public void setTotalAnswersValue(Integer totalAnswersValue) {
        this.totalAnswersValue = totalAnswersValue;
    }

    @Override
    public Map<String, String> getDescriptionTranslations() {
        return new HashMap<>();
    }

    @Override
    public void setDescriptionTranslations(Map<String, String> descriptionTranslations) {
        // No description.
    }
}
