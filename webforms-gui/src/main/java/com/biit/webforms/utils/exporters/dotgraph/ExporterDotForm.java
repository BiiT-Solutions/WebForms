package com.biit.webforms.utils.exporters.dotgraph;

import com.biit.form.TreeObject;
import com.biit.webforms.computed.ComputedRuleView;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Rule;

public class ExporterDotForm extends ExporterDotFormBasic<Form> {

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
}
