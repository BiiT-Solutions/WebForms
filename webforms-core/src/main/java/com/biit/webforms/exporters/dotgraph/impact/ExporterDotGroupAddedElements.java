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
 * Exporter to dot graph for group elements in the added elements of the impact
 * analysis.
 *
 */
public class ExporterDotGroupAddedElements extends ExporterDotGroup {

	private Group oldVersion = null;

	public ExporterDotGroupAddedElements(TreeObject oldVersion) {
		if (oldVersion != null && oldVersion instanceof Group) {
			this.oldVersion = (Group) oldVersion;
		}
	}

	@Override
	public String generateDotNodeList(Group group) {
		if (oldVersion == null || (oldVersion.isHiddenElement() && !group.isHiddenElement())) {
			setFillColor(NEW_FILL_COLOR);
			setShapeColor(NEW_SHAPE_COLOR);
			setFontColor(NEW_FONT_COLOR);
		} else {
			if (oldVersion.isContentEqual(group) && oldVersion.hasSameChildren(group)) {
				setFillColor(DEFAULT_FILL_COLOR);
				setShapeColor(DEFAULT_SHAPE_COLOR);
				setFontColor(DEFAULT_FONT_COLOR);
			} else {
				setFillColor(MODIFIED_FILL_COLOR);
				setShapeColor(MODIFIED_SHAPE_COLOR);
				setFontColor(MODIFIED_FONT_COLOR);
			}
		}
		return super.generateDotNodeList(group);
	}

	@Override
	public String generateDotNodeChilds(Group group) {
		String clusterChilds = new String();

		for (TreeObject child : group.getChildren()) {
			// Retrive child in other form if exists.
			TreeObject oldVersionChild = null;
			if (oldVersion != null) {
				// Search for the children. Can be moved.
				oldVersionChild = oldVersion.getAncestor(Form.class).getChildByOriginalReference(child.getOriginalReference());
			}

			if (child instanceof Group) {
				clusterChilds += (new ExporterDotGroupAddedElements(oldVersionChild)).generateDotNodeList((Group) child);
				continue;
			}
			if (child instanceof BaseQuestion) {
				clusterChilds += (new ExporterDotBaseQuestionAddedElements(oldVersionChild)).generateDotNodeList((BaseQuestion) child);
				continue;
			}
			WebformsLogger.severe(this.getClass().getName(), "Has ignored an element of type: " + child.getClass().getName() + " '" + child
					+ "'");
		}
		return clusterChilds;
	}
}
