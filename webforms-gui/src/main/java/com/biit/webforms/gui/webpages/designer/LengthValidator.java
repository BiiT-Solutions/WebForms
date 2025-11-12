package com.biit.webforms.gui.webpages.designer;

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

import com.vaadin.data.Validator;

public class LengthValidator implements Validator {
	private static final long serialVersionUID = 7781498467462964780L;
	private int length;
	
	public LengthValidator(int length) {
		this.length = length;
	}
	
	@Override
	public void validate(Object value) throws InvalidValueException {
		if(value!=null && ((String)value).length()>length){
			throw new InvalidValueException("Max number of characters "+length);
		}
	}

}
