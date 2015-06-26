package com.biit.webforms.utils.exporters.dotgraph.impact;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.utils.exporters.dotgraph.ExporterDotCategory;

public class ExporterDotCategoryRemovedElements extends ExporterDotCategory {

	private Category newVersionCategory = null;
	private Form newVersionForm = null;

	public ExporterDotCategoryRemovedElements(TreeObject newVersionCategory, Form newVersionForm) {
		if (newVersionCategory != null && newVersionCategory instanceof Category) {
			this.newVersionCategory = (Category) newVersionCategory;
		}
		this.newVersionForm = newVersionForm;
	}

	@Override
	public String generateDotNodeList(Category category) {
		if (newVersionCategory != null && !newVersionCategory.isHiddenElement()) {
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
			// Search for the children. Can be moved.
			newVersionChild = newVersionForm.getChildByOriginalReference(child.getOriginalReference());

			if (child instanceof Group) {
				clusterChilds += (new ExporterDotGroupRemovedElements(newVersionChild, newVersionForm))
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
