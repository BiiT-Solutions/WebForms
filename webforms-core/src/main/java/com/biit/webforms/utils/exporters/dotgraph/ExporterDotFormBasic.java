package com.biit.webforms.utils.exporters.dotgraph;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.Flow;

public abstract class ExporterDotFormBasic<T> extends ExporterDot<T> {

	protected String generateDotRule(Flow rule) {
		String dotRule = new String();
		String origin = getDotId(rule.getOrigin());
		String destiny = null;
		String label = null;
		
		if (rule.isOthers()) {
			label = "OTHERS";
		} else {	
			label = filterDotLanguage(rule.getConditionString());
		}
		
		switch (rule.getFlowType()) {
		case NORMAL:
			destiny = getDotId(rule.getDestiny());
			break;
		case END_LOOP:
			BaseRepeatableGroup group = ((Question)rule.getOrigin()).getRepeatableGroup();
			if(group == null || group.getChildren().isEmpty()){
				return "";
			}
			destiny = getDotId(group.getChildren().get(0));			
			break;
		case END_FORM:
			destiny = "end";
			break;
		}

		dotRule += "\t" + origin + " -> " + destiny + " [label = \"" + label + "\", fontcolor=" + getFontColor(rule.isReadOnly()) +", color=" + getLinkColor(rule.isReadOnly())
				+ ", penwidth=" + getPenWidth() + "];\n";

		return dotRule;
	}

	protected String createLegend(Form form) {
		return "\tsubgraph cluster_0{rank = sink; Legend [shape=none, margin=0, label=<<table border=\"0\"><tr><td>"
				+ form.getLabel() + "</td></tr><tr><td>version " + form.getVersion() + " ("
				+ getTimestampFormattedString(form.getUpdateTime()) + ")</td></tr></table>> ]}\n";
	}

	protected String getTimestampFormattedString(Timestamp timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat();
		return formatter.format(timestamp);
	}

	protected String getDotId(TreeObject node) {
		return "id_" + filterDotLanguageId(node.getComparationId());
	}

}
