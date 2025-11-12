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
import com.biit.webforms.persistence.entity.Group;

/**
 * Exporter to dot graph code for form groups.
 *
 */
public class ExporterDotGroup extends ExporterDot<Group> {
	private static final String BOX_STYLE = "dashed";
	private static final String LOOP_BOX_STYLE = "dotted";

	@Override
	public String export(Group structure) {
		return null;
	}

	@Override
	public String generateDotNodeList(Group group) {
		String cluster = new String();
		if (!group.isHiddenElement()) {
			// Cluster tag it's what makes it a separate group
			cluster += "\tsubgraph cluster_" + filterDotLanguageId(group.getComparationId()) + " {\n";
			cluster += "\t\tnode [style=filled, fillcolor=" + getFillColor(group.isReadOnly()) + "];\n";
			if (group.isRepeatable()) {
				cluster += "\t\tlabel = \"" + filterDotLanguage(group.getName() + " (Loop)") + "\";\n";
			} else {
				cluster += "\t\tlabel = \"" + filterDotLanguage(group.getName()) + "\";\n";
			}

			cluster += generateDotNodeChilds(group);

			cluster += "\t\tcolor=" + getShapeColor(group.isReadOnly()) + ";\n";
			cluster += "\t\tfontcolor=" + getFontColor(group.isReadOnly()) + ";\n";
			cluster += "\t\tpenwidth=" + getPenWidth() + ";\n";
			cluster += "\t\tstyle=" + getGroupStyle(group) + ";\n";

			cluster += "\t\t}\n";
		}

		return cluster;
	}

	@Override
	public String generateDotNodeChilds(Group group) {
		String clusterChilds = new String();

		for (TreeObject child : group.getChildren()) {
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

	public String getGroupStyle(Group group) {
		if (group.isRepeatable()) {
			return LOOP_BOX_STYLE;
		} else {
			return BOX_STYLE;
		}
	}

	@Override
	public String generateDotNodeFlow(Group structure) {
		return null;
	}

}
