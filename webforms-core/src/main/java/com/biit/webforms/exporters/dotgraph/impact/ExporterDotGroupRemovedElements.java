package com.biit.webforms.exporters.dotgraph.impact;

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
import com.biit.webforms.exporters.dotgraph.ExporterDotGroup;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;

/**
 * Exporter to dot graph code for groups in the removed elements graph of the
 * impact analysis
 *
 */
public class ExporterDotGroupRemovedElements extends ExporterDotGroup {

	private Group newVersion = null;
	private Form newVersionForm = null;

	public ExporterDotGroupRemovedElements(TreeObject newVersion, Form newVersionForm) {
		if (newVersion != null && newVersion instanceof Group) {
			this.newVersion = (Group) newVersion;
		}
		this.newVersionForm = newVersionForm;
	}

	@Override
	public String generateDotNodeList(Group group) {
		if (newVersion != null && !newVersion.isHiddenElement()) {
			setFillColor(DEFAULT_FILL_COLOR);
			setShapeColor(DEFAULT_SHAPE_COLOR);
			setFontColor(DEFAULT_FONT_COLOR);
		} else {
			setFillColor(DELETED_FILL_COLOR);
			setShapeColor(DELETED_SHAPE_COLOR);
			setFontColor(DELETED_FONT_COLOR);
		}
		return super.generateDotNodeList(group);
	}

	@Override
	public String generateDotNodeChilds(Group group) {
		String clusterChilds = new String();

		for (TreeObject child : group.getChildren()) {
			// Retrieve child in other form if exists.
			TreeObject newVersionChild = null;
			// Search for the children. Can be moved.
			newVersionChild = newVersionForm.getChildByOriginalReference(child.getOriginalReference());

			if (child instanceof Group) {
				clusterChilds += (new ExporterDotGroupRemovedElements(newVersionChild, newVersionForm)).generateDotNodeList((Group) child);
				continue;
			}
			if (child instanceof BaseQuestion) {
				clusterChilds += (new ExporterDotBaseQuestionRemovedElements(newVersionChild)).generateDotNodeList((BaseQuestion) child);
				continue;
			}
			WebformsLogger.severe(this.getClass().getName(), "Has ignored an element of type: " + child.getClass().getName() + " '" + child
					+ "'");
		}
		return clusterChilds;
	}

}
