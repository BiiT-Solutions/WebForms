package com.biit.webforms.utils.exporters.dotgraph;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Rule;

public abstract class ExporterDotFormBasic<T> extends ExporterDot<T> {

	protected String generateDotRule(Rule rule) {
		String dotRule = new String();
		String origin = getDotId(rule.getOrigin());
		String destiny = null;
		String label = null;
		
		if (rule.isOthers()) {
			label = "OTHERS";
		} else {	
			label = filterDotLanguage(rule.getConditionString());
		}
		
		switch (rule.getRuleType()) {
		case NORMAL:
		case END_LOOP:
			destiny = getDotId(rule.getDestiny());			
			break;
		case END_FORM:
			destiny = "end";
			break;
		}

		dotRule += "\t" + origin + " -> " + destiny + " [label = \"" + label + "\" color=" + getLinkColor()
				+ ", penwidth=" + getPenWidth() + "];\n";

		return dotRule;
	}

	protected String createLegend(Form form) {
		return "\tsubgraph cluster_0{rank = sink; Legend [shape=none, margin=0, label=<<table border=\"0\"><tr><td>"
				+ form.getName() + "</td></tr><tr><td>version " + form.getVersion() + " ("
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
