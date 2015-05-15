package com.biit.webforms.utils.exporters.dotgraph;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Group;

public class ExporterDotCategory extends ExporterDot<Category> {
	private final static String BOX_STYLE = "bold";

	@Override
	public String export(Category structure) {
		// TODO Auto-generated method stub
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
			WebformsLogger.severe(this.getClass().getName(), "Has ignored an element of type: "
					+ child.getClass().getName() + " '" + child + "'");
		}
		return clusterChilds;
	}

	@Override
	public String generateDotNodeFlow(Category structure) {
		// TODO Auto-generated method stub
		return null;
	}
}
