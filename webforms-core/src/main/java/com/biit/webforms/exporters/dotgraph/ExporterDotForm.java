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
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Flow;

/**
 * Dot graph code exporter for forms.
 */
public class ExporterDotForm extends ExporterDotFormBasic<Form> {

    @Override
    public String export(Form form) {
        String dotCode = "";
        dotCode += "digraph G {\n";
        dotCode += "\tgraph [ fontsize=" + getSmallFontSize() + " ];\n";
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
        StringBuilder dotFlow = new StringBuilder("");
        ComputedFlowView computedRuleView = form.getComputedFlowsView();
        if (computedRuleView.getFirstElement() != null) {
            dotFlow.append("\tstart -> ").append(getDotId(computedRuleView.getFirstElement())).append("[color=").append(getLinkColor(false)).append("];\n");
        }
        for (Flow rule : computedRuleView.getFlows()) {
            dotFlow.append(generateDotRule(rule));
        }

        return dotFlow.toString();
    }

    @Override
    public String generateDotNodeChilds(Form form) {
        StringBuilder dotNodes = new StringBuilder("");
        for (TreeObject child : form.getChildren()) {
            dotNodes.append((new ExporterDotCategory()).generateDotNodeList((Category) child));
        }
        return dotNodes.toString();
    }
}
