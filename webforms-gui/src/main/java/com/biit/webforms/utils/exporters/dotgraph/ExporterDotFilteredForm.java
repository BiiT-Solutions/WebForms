package com.biit.webforms.utils.exporters.dotgraph;

import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.ComputedRuleView;
import com.biit.webforms.persistence.entity.FilteredForm;
import com.biit.webforms.persistence.entity.Rule;

public class ExporterDotFilteredForm extends ExporterDotFormBasic<FilteredForm> {

	@Override
	public String export(FilteredForm filteredForm) {
		String dotCode = new String();
		dotCode += "digraph G {\n";
		dotCode += "size=\"" + getSizeLimit() + "\";\n";
		dotCode += "\tgraph [ resolution=60, fontsize=" + getSmallFontSize() + " ];\n";
		dotCode += "\tnode [ fontsize=" + getSmallFontSize() + "];\n";
		dotCode += "\tedge [ fontsize=" + getSmallFontSize() + "];\n";
		dotCode += "\tpagedir=\"TL\";\n";
		dotCode += createLegend(filteredForm.getOriginalForm());
		dotCode += generateDotNodeList(filteredForm);
		dotCode += generateDotNodeFlow(filteredForm);
		if (filteredForm.hasStartAsDependency()) {
			dotCode += "\tstart [shape=Mdiamond];\n";
		}
		if (filteredForm.hasEndAsDependency()) {
			dotCode += "\tend [shape=Msquare];\n";
		}
		dotCode += "}\n";

		return dotCode;
	}

	@Override
	public String generateDotNodeList(FilteredForm structure) {
		String dotNodes = new String();
		for (TreeObject child : structure.getFilteredForm().getChildren()) {
			dotNodes += (new ExporterDotCategory()).generateDotNodeList((Category) child);
		}
		return dotNodes;
	}

	@Override
	public String generateDotNodeFlow(FilteredForm structure) {
		String dotFlow = new String();
		ComputedRuleView computedRuleView = structure.getRules();
		if (structure.hasStartAsDependency() && computedRuleView.getFirstElement() != null) {
			dotFlow += "\tstart -> " + getDotId(computedRuleView.getFirstElement()) + "[color=" + getLinkColor()
					+ "];\n";
		}
		for (Rule rule : structure.getFilteredRules()) {
			dotFlow += generateDotRule(rule);
		}

		return dotFlow;
	}

}
