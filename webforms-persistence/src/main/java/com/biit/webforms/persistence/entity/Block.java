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

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.usermanager.entity.IUser;
import com.biit.webforms.persistence.entity.exceptions.OnlyOneChildIsAllowedException;
import com.biit.webforms.serialization.BlockDeserializer;
import com.biit.webforms.serialization.BlockSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@JsonDeserialize(using = BlockDeserializer.class)
@JsonSerialize(using = BlockSerializer.class)
@Table(name = "tree_blocks")
@Cacheable()
public class Block extends Form implements IWebformsBlockView {
    private static final long serialVersionUID = -5029214862461479704L;
    private static final String DEFAULT_LABEL = "Block";
    public static final String DEFAULT_TECHNICAL_NAME = "block";

    public Block() {
        super();
    }

    @Override
    protected String getDefaultLabel() {
        return DEFAULT_LABEL;
    }

    public Block(String name, IUser<Long> user, Long organizationId) throws FieldTooLongException, CharacterNotAllowedException {
        super(name, user, organizationId);
    }

    @Override
    protected String getDefaultTechnicalName() {
        return DEFAULT_TECHNICAL_NAME;
    }

    @Override
    public void addChild(TreeObject child) throws NotValidChildException, ElementIsReadOnly {
        if (!getChildren().isEmpty()) {
            throw new OnlyOneChildIsAllowedException("Building blocks only can have one category. ");
        }
        super.addChild(child);
    }

}
