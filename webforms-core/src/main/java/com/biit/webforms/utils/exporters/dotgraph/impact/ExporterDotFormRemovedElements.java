package com.biit.webforms.utils.exporters.dotgraph.impact;

import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.exporters.dotgraph.ExporterDotForm;

public class ExporterDotFormRemovedElements extends ExporterDotForm {

	private Form newVersion;

	public ExporterDotFormRemovedElements(Form newVersion) {
		this.newVersion = newVersion;
	}

	@Override
	public String generateDotNodeChilds(Form oldVersion) {
		String dotNodes = new String();
		for (TreeObject child : oldVersion.getChildren()) {
			dotNodes += (new ExporterDotCategoryRemovedElements(newVersion.getChildByOriginalReference(child
					.getOriginalReference()), newVersion)).generateDotNodeList((Category) child);
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
				setLinkColor(DEFAULT_LINK_COLOR);
			} else {
				if (checkIfOriginOrDestinyAreDeleted(rule)) {
					setLinkColor(DELETED_LINK_COLOR);
				} else {
					if (!checkIfEqualFlowExistsInNew(rule)) {
						setLinkColor(DELETED_LINK_COLOR);
					} else {
						setLinkColor(DEFAULT_LINK_COLOR);
					}
				}
			}
			dotFlow += generateDotRule(rule);
		}

		return dotFlow;
	}

	private boolean checkIfEqualFlowExistsInNew(Flow rule) {
		// Get origin and destiny in old version
		TreeObject oldOrigin = newVersion.getChildByOriginalReference(rule.getOrigin().getOriginalReference());
		TreeObject oldDestiny = null;
		if (rule.getDestiny() != null) {
			oldDestiny = newVersion.getChildByOriginalReference(rule.getDestiny().getOriginalReference());
		}
		Set<Flow> flows = newVersion.getFlows(oldOrigin, oldDestiny);
		
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

	private boolean checkIfOriginOrDestinyAreDeleted(Flow rule) {
		TreeObject ruleOriginInSecondVersion = newVersion.getChildByOriginalReference(rule.getOrigin()
				.getOriginalReference());
		if (ruleOriginInSecondVersion == null) {
			return true;
		} else {
			if (rule.getDestiny() != null) {
				TreeObject ruleDestinyInSecondVersion = newVersion.getChildByOriginalReference(rule.getDestiny()
						.getOriginalReference());
				if (ruleDestinyInSecondVersion == null) {
					return true;
				}
			}
			return false;
		}
	}
}
