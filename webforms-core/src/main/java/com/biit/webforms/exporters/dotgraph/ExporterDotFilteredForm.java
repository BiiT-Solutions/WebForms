package com.biit.webforms.exporters.dotgraph;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
