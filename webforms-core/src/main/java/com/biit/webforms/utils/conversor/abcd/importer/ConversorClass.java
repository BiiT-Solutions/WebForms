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

/**
 * Conversor abstract class Which is derived by all conversors..
 *
 * @param <O>
 * @param <D>
 */
public abstract class ConversorClass<O,D> implements Conversor<O, D> {

	public D convert(O origin){
		D destiny = createDestinyInstance();
		copyData(origin, destiny);
		return destiny;
	}
	
	public abstract D createDestinyInstance();
	
	public abstract void copyData(O origin, D destiny);
	
}
