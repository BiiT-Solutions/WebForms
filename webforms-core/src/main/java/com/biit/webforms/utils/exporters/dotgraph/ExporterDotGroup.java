package com.biit.webforms.utils.exporters.dotgraph;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Group;

public class ExporterDotGroup extends ExporterDot<Group> {
	private final static String BOX_STYLE = "dashed";
	private final static String LOOP_BOX_STYLE = "dotted";

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
			WebformsLogger.severe(this.getClass().getName(), "Has ignored an element of type: "
					+ child.getClass().getName() + " '" + child + "'");
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
		// TODO Auto-generated method stub
		return null;
	}

}
