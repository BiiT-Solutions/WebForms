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
		dotNodes += generateDotNodeChilds(structure);
		return dotNodes;
	}

	@Override
	public String generateDotNodeFlow(FilteredForm structure) {
		String dotFlow = new String();
		ComputedFlowView computedRuleView = structure.getFlows();
		if (structure.hasStartAsDependency() && computedRuleView.getFirstElement() != null) {
			dotFlow += "\tstart -> " + getDotId(computedRuleView.getFirstElement()) + "[color=" + getLinkColor(false) + "];\n";
		}
		for (Flow rule : structure.getFilteredFlows()) {
			dotFlow += generateDotRule(rule);
		}

		return dotFlow;
	}

	@Override
	public String generateDotNodeChilds(FilteredForm structure) {
		String dotNodes = new String();
		for (TreeObject child : structure.getFilteredForm().getChildren()) {
			dotNodes += (new ExporterDotCategory()).generateDotNodeList((Category) child);
		}
		return dotNodes;
	}

}
