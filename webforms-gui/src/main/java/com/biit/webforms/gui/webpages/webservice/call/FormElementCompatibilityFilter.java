package com.biit.webforms.gui.webpages.webservice.call;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
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

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

import java.util.Objects;

public class FormElementCompatibilityFilter implements Filter {
	private static final long serialVersionUID = 6762055574561628797L;

	private AnswerType type;
	private AnswerFormat format;
	private AnswerSubformat subformat;

	public FormElementCompatibilityFilter(AnswerType type, AnswerFormat format, AnswerSubformat subformat) {
		this.type = type;
		this.format = format;
		this.subformat = subformat;
	}

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		//Passes filter if its a system field
		if (itemId instanceof SystemField) {
			return true;
		}
		if (itemId instanceof Question) {
			Question question = (Question) itemId;
			//Passes filter if its a question and the type, format and subformat are equal.
			return Objects.equals(question.getAnswerType(), type) && Objects.equals(question.getAnswerFormat(), format)
					&& Objects.equals(question.getAnswerSubformat(), subformat);
		}
		return false;
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		return true;
	}

}
