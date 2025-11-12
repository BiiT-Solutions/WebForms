package com.biit.webforms.utils.conversor.abcd.exporter;

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

import com.biit.abcd.persistence.entity.Group;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.utils.conversor.abcd.importer.ConversorTreeObject;

/**
 * Conversor from abcd groups to webforms groups.
 *
 * A group only has base question elements.
 *
 */
public class ConversorGroupToAbcdGroup extends ConversorTreeObject<com.biit.webforms.persistence.entity.Group, Group> {

	private ConversorQuestionToAbcdQuestion conversorQuestion = new ConversorQuestionToAbcdQuestion();

	@Override
	public Group createDestinyInstance() {
		return new Group();
	}

	@Override
	public void copyData(com.biit.webforms.persistence.entity.Group origin, Group destiny) {
		// Copy base data
		super.copyData(origin, destiny);

		// Copy repeatable info
		destiny.setRepeatable(origin.isRepeatable());

		for (TreeObject child : origin.getChildren()) {
			if (!(child instanceof com.biit.webforms.persistence.entity.Group || child instanceof com.biit.webforms.persistence.entity.Question)) {
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
				continue;
			}
			TreeObject convertedChild = null;
			if (child instanceof com.biit.webforms.persistence.entity.Group) {
				convertedChild = convert((com.biit.webforms.persistence.entity.Group) child);
			}
			if (child instanceof com.biit.webforms.persistence.entity.Question) {
				convertedChild = conversorQuestion.convert((com.biit.webforms.persistence.entity.Question) child);
			}
			try {
				destiny.addChild(convertedChild);
			} catch (NotValidChildException | ElementIsReadOnly e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

}
