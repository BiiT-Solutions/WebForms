package com.biit.webforms.utils.conversor.abcd.importer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.logger.WebformsLogger;

/**
 * Abstract conversor of tree objects. All elements of forms in abcd and
 * webforms implement a treeObject.
 *
 * @param <O>
 * @param <D>
 */
public abstract class ConversorTreeObject<O extends TreeObject, D extends TreeObject> extends ConversorClass<O, D> {

	@Override
	public void copyData(O origin, D destiny) {
		try {
			destiny.setName(origin.getName());
			if (origin.getLabel() != null && !origin.getLabel().isEmpty()) {
				destiny.setLabel(origin.getLabel());
			} else {
				destiny.setLabel(origin.getName());
			}
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}
}
