package com.biit.webforms.exporters.dotgraph.impact;

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
