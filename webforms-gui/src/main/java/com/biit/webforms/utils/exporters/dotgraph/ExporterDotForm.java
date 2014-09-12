package com.biit.webforms.utils.exporters.dotgraph;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.ComputedRuleView;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Rule;

public class ExporterDotForm extends ExporterDot<Form> {

	@Override
	public String export(Form form) {
		String dotCode = new String();
		dotCode += "digraph G {\n";
		dotCode += "size=\"" + getSizeLimit() + "\";\n";
		dotCode += "\tgraph [ resolution=60, fontsize=" + getSmallFontSize() + " ];\n";
		dotCode += "\tnode [ fontsize=" + getSmallFontSize() + "];\n";
		dotCode += "\tedge [ fontsize=" + getSmallFontSize() + "];\n";
		dotCode += "\tpagedir=\"TL\";\n";
		dotCode += createLegend(form);
		dotCode += generateDotNodeList(form);
		dotCode += generateDotNodeFlow(form);
		dotCode += "\tstart [shape=Mdiamond];\n";
		dotCode += "\tend [shape=Msquare];\n";
		dotCode += "}\n";

		return dotCode;
	}

	@Override
	public String generateDotNodeList(Form form) {
		String dotNodes = new String();
		for (TreeObject child : form.getChildren()) {
			dotNodes += (new ExporterDotCategory()).generateDotNodeList((Category) child);
		}
		return dotNodes;
	}

	@Override
	public String generateDotNodeFlow(Form form) {
		String dotFlow = new String();
		ComputedRuleView computedRuleView = form.getComputedRuleView();
		if (computedRuleView.getFirstElement() != null) {
			dotFlow += "\tstart -> " + getDotId(computedRuleView.getFirstElement()) + "[color=" + getLinkColor()
					+ "];\n";
		}
		for (Rule rule : computedRuleView.getRules()) {
			dotFlow += generateDotRule(rule);
		}

		return dotFlow;
	}

	private String generateDotRule(Rule rule) {
		String dotRule = new String();
		String origin = getDotId(rule.getOrigin());
		String destiny = null;
		String label = null;
		switch (rule.getRuleType()) {
		case NORMAL:
			destiny = getDotId(rule.getDestiny());
			label = filterDotLanguage(rule.getConditionString());
			break;
		case OTHERS:
			destiny = getDotId(rule.getDestiny());
			label = "OTHERS";
			break;
		case END_FORM:
			destiny = "end";
			label = filterDotLanguage(rule.getConditionString());
			break;
		case END_LOOP:
			destiny = getDotId(rule.getDestiny());
			label = filterDotLanguage(rule.getConditionString());
			break;
		case LOOP:
			destiny = "LOOP";
			label = filterDotLanguage(rule.getConditionString());
			break;
		}

		dotRule += "\t" + origin + " -> " + destiny + " [label = \"" + label + "\" color=" + getLinkColor()
				+ ", penwidth=" + getPenWidth() + "];\n";

		return dotRule;
	}

	private String createLegend(Form form) {
		return "\tsubgraph cluster_0{rank = sink; Legend [shape=none, margin=0, label=<<table border=\"0\"><tr><td>"
				+ form.getName() + "</td></tr><tr><td>version " + form.getVersion() + " ("
				+ getTimestampFormattedString(form.getUpdateTime()) + ")</td></tr></table>> ]}\n";
	}

	private String getTimestampFormattedString(Timestamp timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat();
		return formatter.format(timestamp);
	}

	private String getDotId(TreeObject node) {
		return "id_" + filterDotLanguageId(node.getComparationId());
	}
}
