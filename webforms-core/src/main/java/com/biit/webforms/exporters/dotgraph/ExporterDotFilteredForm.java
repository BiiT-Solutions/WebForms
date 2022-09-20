package com.biit.webforms.exporters.dotgraph;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.computed.FilteredForm;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;

/**
 * Specialization of the form exporter that removes all the elements that do not
 * pass the filter Otherwise works exactly as the DotGraph Form exporter.
 *
 */
public class ExporterDotFilteredForm extends ExporterDotFormBasic<FilteredForm> {

	@Override
	public String export(FilteredForm filteredForm) {
		String dotCode = "";
		dotCode += "digraph G {\n";
		dotCode += "\tgraph [ fontsize=" + getSmallFontSize() + " ];\n";
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
		dotNodes += generateDotNodeChilds(structure);
		return dotNodes;
	}

	@Override
	public String generateDotNodeFlow(FilteredForm structure) {
		StringBuilder dotFlow = new StringBuilder("");
		ComputedFlowView computedRuleView = structure.getFlows();
		if (structure.hasStartAsDependency() && computedRuleView.getFirstElement() != null) {
			dotFlow.append("\tstart -> ").append(getDotId(computedRuleView.getFirstElement())).append("[color=").append(getLinkColor(false)).append("];\n");
		}
		for (Flow rule : structure.getFilteredFlows()) {
			dotFlow.append(generateDotRule(rule));
		}

		return dotFlow.toString();
	}

	@Override
	public String generateDotNodeChilds(FilteredForm structure) {
		StringBuilder dotNodes = new StringBuilder("");
		for (TreeObject child : structure.getFilteredForm().getChildren()) {
			dotNodes.append((new ExporterDotCategory()).generateDotNodeList((Category) child));
		}
		return dotNodes.toString();
	}

}
