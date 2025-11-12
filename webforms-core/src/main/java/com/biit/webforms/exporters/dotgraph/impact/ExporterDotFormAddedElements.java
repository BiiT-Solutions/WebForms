package com.biit.webforms.exporters.dotgraph.impact;

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

import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.exporters.dotgraph.ExporterDotForm;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;

/**
 * Exporter to dot graph code for forms in the added elements graph of the
 * impact analysis.
 *
 */
public class ExporterDotFormAddedElements extends ExporterDotForm {

	private Form oldVersion;

	public ExporterDotFormAddedElements(Form oldVersion) {
		this.oldVersion = oldVersion;
	}

	@Override
	public String generateDotNodeChilds(Form form) {
		String dotNodes = new String();
		for (TreeObject child : form.getChildren()) {
			dotNodes += (new ExporterDotCategoryAddedElements(oldVersion.getChild(child.getPath()))).generateDotNodeList((Category) child);

		}
		return dotNodes;
	}

	@Override
	public String generateDotNodeFlow(Form form) {
		String dotFlow = new String();
		ComputedFlowView computedRuleView = form.getComputedFlowsView();
		if (computedRuleView.getFirstElement() != null) {
			dotFlow += "\tstart -> " + getDotId(computedRuleView.getFirstElement()) + "[color="
					+ getLinkColor(computedRuleView.getFirstElement().isReadOnly()) + "];\n";
		}
		for (Flow rule : computedRuleView.getFlows()) {
			if (rule.isGenerated()) {
				// Default rules. Ignore.
				setLinkColor(DEFAULT_LINK_COLOR);
			} else {
				if (checkIfRuleOriginOrDestinyIsNewElement(rule)) {
					setLinkColor(NEW_LINK_COLOR);
				} else {
					if (!checkIfEqualFlowExisted(rule)) {
						setLinkColor(MODIFIED_LINK_COLOR);
					} else {
						setLinkColor(DEFAULT_LINK_COLOR);
					}
				}
			}
			dotFlow += generateDotRule(rule);
		}

		return dotFlow;
	}

	/**
	 * Checks if a flow with the same content existed in the previous version.
	 *
	 * @param rule
	 * @return
	 */
	private boolean checkIfEqualFlowExisted(Flow rule) {
		// Get origin and destiny in old version
		TreeObject oldOrigin = oldVersion.getChildByOriginalReference(rule.getOrigin().getOriginalReference());
		TreeObject oldDestiny = null;
		if (rule.getDestiny() != null) {
			oldDestiny = oldVersion.getChildByOriginalReference(rule.getDestiny().getOriginalReference());
		}
		Set<Flow> flows = oldVersion.getFlows(oldOrigin, oldDestiny);

		if (!flows.isEmpty()) {
			// Flows found with same origin/destiny.
			for (Flow flow : flows) {
				if (flow.isOthers() && rule.isOthers()) {
					return true;
				}
				if (flow.getConditionString().equals(rule.getConditionString())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if origin or destiny are not found in the new version. If origin or
	 * destiny node are not found, then the rule is considered as new.
	 *
	 * @param rule
	 * @return
	 */
	private boolean checkIfRuleOriginOrDestinyIsNewElement(Flow rule) {
		TreeObject oldVersionOrigin = oldVersion.getChildByOriginalReference(rule.getOrigin().getOriginalReference());
		if (oldVersionOrigin == null) {
			return true;
		}
		if (rule.getDestiny() != null) {
			TreeObject oldVersionDestiny = oldVersion.getChildByOriginalReference(rule.getDestiny().getOriginalReference());
			if (oldVersionDestiny == null) {
				return true;
			}
		}
		return false;
	}
}
