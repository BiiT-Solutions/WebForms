package com.biit.webforms.utils.exporters.dotgraph;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Group;

public class ExporterDotGroup extends ExporterDot<Group> {
	private final static String BOX_STYLE = "dashed";

	@Override
	public String export(Group structure) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateDotNodeList(Group group) {
		String cluster = new String();
		// Cluster tag it's what makes it a separate group
		cluster += "\tsubgraph cluster_" + filterDotLanguageId(group.getComparationId()) + " {\n";
		cluster += "\t\tnode [style=filled, fillcolor=" + getFillColor() + "];\n";
		cluster += "\t\tlabel = \"" + filterDotLanguage(group.getName()) + "\";\n";

		for (TreeObject child : group.getChildren()) {
			if (child instanceof Group) {
				cluster += (new ExporterDotGroup()).generateDotNodeList((Group) child);
				continue;
			}
			if (child instanceof BaseQuestion) {
				cluster += (new ExporterDotBaseQuestion()).generateDotNodeList((BaseQuestion) child);
				continue;
			}
			WebformsLogger.severe(this.getClass().getName(), "Has ignored an element of type: "
					+ child.getClass().getName() + " '" + child + "'");
		}
		cluster += "\t\tcolor=" + getShapeColor() + ";\n";
		cluster += "\t\tfontcolor=" + getFontColor() + ";\n";
		cluster += "\t\tpenwidth=" + getPenWidth() + ";\n";
		cluster += "\t\tstyle=" + BOX_STYLE + ";\n";
		cluster += "\t\t}\n";
		
		return cluster;
	}

	@Override
	public String generateDotNodeFlow(Group structure) {
		// TODO Auto-generated method stub
		return null;
	}

}
