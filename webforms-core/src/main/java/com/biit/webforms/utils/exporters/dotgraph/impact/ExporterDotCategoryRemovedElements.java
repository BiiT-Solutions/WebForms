package com.biit.webforms.utils.exporters.dotgraph.impact;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.utils.exporters.dotgraph.ExporterDotCategory;

public class ExporterDotCategoryRemovedElements extends ExporterDotCategory {

	private Category newVersionCategory = null;

	public ExporterDotCategoryRemovedElements(TreeObject newVersionCategory) {
		if (newVersionCategory != null && newVersionCategory instanceof Category) {
			this.newVersionCategory = (Category) newVersionCategory;
		}
	}

	@Override
	public String generateDotNodeList(Category category) {
		if (newVersionCategory != null) {
			setFillColor(DEFAULT_FILL_COLOR);
			setShapeColor(DEFAULT_SHAPE_COLOR);
			setFontColor(DEFAULT_FONT_COLOR);
		} else {
			setFillColor(DELETED_FILL_COLOR);
			setShapeColor(DELETED_SHAPE_COLOR);
			setFontColor(DELETED_FONT_COLOR);
		}
		return super.generateDotNodeList(category);
	}

	@Override
	public String generateDotNodeChilds(Category category) {
		String clusterChilds = new String();

		for (TreeObject child : category.getChildren()) {
			// Retrive child in other form if exists.
			TreeObject newVersionChild = null;
			if (newVersionCategory != null) {
				newVersionChild = newVersionCategory.getChild(child.getName());
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
