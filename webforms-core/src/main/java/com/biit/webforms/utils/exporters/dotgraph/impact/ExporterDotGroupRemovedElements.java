package com.biit.webforms.utils.exporters.dotgraph.impact;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.utils.exporters.dotgraph.ExporterDotGroup;

public class ExporterDotGroupRemovedElements extends ExporterDotGroup {

	private Group newVersion = null;

	public ExporterDotGroupRemovedElements(TreeObject newVersion) {
		if (newVersion != null && newVersion instanceof Group) {
			this.newVersion = (Group) newVersion;
		}
	}

	@Override
	public String generateDotNodeList(Group group) {
		if (newVersion != null) {
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
			// Retrive child in other form if exists.
			TreeObject newVersionChild = null;
			if (newVersion != null) {
				newVersionChild = newVersion.getChild(child.getName());
			}

			if (child instanceof Group) {
				clusterChilds += (new ExporterDotGroupRemovedElements(newVersionChild))
						.generateDotNodeList((Group) child);
				continue;
			}
			if (child instanceof BaseQuestion) {
				clusterChilds += (new ExporterDotBaseQuestionRemovedElements(newVersionChild))
						.generateDotNodeList((BaseQuestion) child);
				continue;
			}
			WebformsLogger.severe(this.getClass().getName(), "Has ignored an element of type: "
					+ child.getClass().getName() + " '" + child + "'");
		}
		return clusterChilds;
	}

}
