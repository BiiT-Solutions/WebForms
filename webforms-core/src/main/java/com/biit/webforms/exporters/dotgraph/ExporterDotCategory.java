package com.biit.webforms.exporters.dotgraph;

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

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Group;

/**
 * Dot graph exporter for categories. A category encloses with a rectangle the
 * elements inside it.
 *
 */
public class ExporterDotCategory extends ExporterDot<Category> {
	private static final String BOX_STYLE = "bold";

	@Override
	public String export(Category structure) {
		return null;
	}

	@Override
	public String generateDotNodeList(Category category) {
		String cluster = new String();
		if (!category.isHiddenElement()) {
			// Cluster tag it's what makes it a separate group
			cluster += "\tsubgraph cluster_" + filterDotLanguageId(category.getComparationId()) + " {\n";
			cluster += "\t\tnode [style=filled, fillcolor=" + getFillColor(category.isReadOnly()) + "];\n";
			cluster += "\t\tlabel = \"" + filterDotLanguage(category.getName()) + "\";\n";

			cluster += generateDotNodeChilds(category);

			cluster += "\t\tcolor=" + getShapeColor(category.isReadOnly()) + ";\n";
			cluster += "\t\tfontcolor=" + getFontColor(category.isReadOnly()) + ";\n";
			cluster += "\t\tpenwidth=" + getPenWidth() + ";\n";
			cluster += "\t\tstyle=" + BOX_STYLE + ";\n";
			cluster += "\t\t}\n";
		}
		return cluster;
	}

	@Override
	public String generateDotNodeChilds(Category category) {
		String clusterChilds = new String();

		for (TreeObject child : category.getChildren()) {
			if (child instanceof Group) {
				clusterChilds += (new ExporterDotGroup()).generateDotNodeList((Group) child);
				continue;
			}
			if (child instanceof BaseQuestion) {
				clusterChilds += (new ExporterDotBaseQuestion()).generateDotNodeList((BaseQuestion) child);
				continue;
			}
			WebformsLogger.severe(this.getClass().getName(), "Has ignored an element of type: " + child.getClass().getName() + " '" + child
					+ "'");
		}
		return clusterChilds;
	}

	@Override
	public String generateDotNodeFlow(Category structure) {
		return null;
	}
}
