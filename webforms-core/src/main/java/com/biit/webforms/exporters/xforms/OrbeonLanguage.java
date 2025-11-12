package com.biit.webforms.exporters.xforms;

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

public enum OrbeonLanguage {
	DUTCH("nl"), ENGLISH("en"), SPANISH("es");

	private String abbreviature;

	public String getAbbreviature() {
		return abbreviature;
	}

	private OrbeonLanguage(String abbreviature) {
		this.abbreviature = abbreviature;
	}

	public static OrbeonLanguage get(String abbreviature) {
		for (OrbeonLanguage language : OrbeonLanguage.values()) {
			if (language.getAbbreviature().equals(abbreviature)) {
				return language;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return getAbbreviature();
	}
}
