package com.biit.webforms.utils.exporters.dotgraph;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Flow;

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
		return generateDotNodeChilds(form);
	}

	@Override
	public String generateDotNodeFlow(Form form) {
		String dotFlow = new String();
		ComputedFlowView computedRuleView = form.getComputedFlowsView();
		if (computedRuleView.getFirstElement() != null) {
			dotFlow += "\tstart -> " + getDotId(computedRuleView.getFirstElement()) + "[color=" + getLinkColor(false)
					+ "];\n";
		}
		for (Flow rule : computedRuleView.getFlows()) {
			dotFlow += generateDotRule(rule);
		}

		return dotFlow;
	}

	@Override
	public String generateDotNodeChilds(Form form) {
		String dotNodes = new String();
		for (TreeObject child : form.getChildren()) {
			dotNodes += (new ExporterDotCategory()).generateDotNodeList((Category) child);
		}
		return dotNodes;
	}
}
