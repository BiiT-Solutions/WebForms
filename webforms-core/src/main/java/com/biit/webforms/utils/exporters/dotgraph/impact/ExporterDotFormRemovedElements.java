package com.biit.webforms.utils.exporters.dotgraph.impact;

import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.exporters.dotgraph.ExporterDotForm;

public class ExporterDotFormRemovedElements extends ExporterDotForm {

	private Form currentVersion;

	public ExporterDotFormRemovedElements(Form currentVersion) {
		this.currentVersion = currentVersion;
	}

	@Override
	public String generateDotNodeChilds(Form form) {
		String dotNodes = new String();
		for (TreeObject child : form.getChildren()) {
			dotNodes += (new ExporterDotCategoryRemovedElements(currentVersion.getChild(child.getPath())))
					.generateDotNodeList((Category) child);
		}
		return dotNodes;
	}

	@Override
	public String generateDotNodeFlow(Form form) {
		String dotFlow = new String();
		ComputedFlowView computedRuleView = form.getComputedFlowsView();
		if (computedRuleView.getFirstElement() != null) {
			dotFlow += "\tstart -> " + getDotId(computedRuleView.getFirstElement()) + "[color=" + getLinkColor(computedRuleView.getFirstElement().isReadOnly())
					+ "];\n";
		}
		for (Flow rule : computedRuleView.getFlows()) {
			if (rule.isGenerated()) {
				setLinkColor(DEFAULT_LINK_COLOR);
			} else {
				if (checkIfOriginOrDesinyAreDeleted(rule)) {
					setLinkColor(DELETED_LINK_COLOR);
				} else {
					if(!checkIfEqualFlowExistsInNew(rule)){
						setLinkColor(DELETED_LINK_COLOR);
					}else{
						setLinkColor(DEFAULT_LINK_COLOR);
					}
				}
			}
			dotFlow += generateDotRule(rule);
		}

		return dotFlow;
	}
	
	private boolean checkIfEqualFlowExistsInNew(Flow rule) {
		//Get origin and destiny in old version
		TreeObject oldOrigin = currentVersion.getChild(rule.getOrigin().getPath());
		TreeObject oldDestiny = null;
		if(rule.getDestiny()!=null){
			oldDestiny = currentVersion.getChild(rule.getDestiny().getPath());
		}	
		Set<Flow> flows = currentVersion.getFlows(oldOrigin, oldDestiny);
		
		if (!flows.isEmpty()) {
			// Flows found with same origin/destiny.
			for (Flow flow : flows) {
				if(flow.isOthers() && rule.isOthers()){
					return true;
				}
				if (flow.getConditionString().equals(rule.getConditionString())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkIfOriginOrDesinyAreDeleted(Flow rule) {
		TreeObject ruleOriginInSecondVersion = currentVersion.getChild(rule.getOrigin().getPath());
		if (ruleOriginInSecondVersion == null) {
			return true;
		} else {
			if (rule.getDestiny() != null) {
				TreeObject ruleDestinyInSecondVersion = currentVersion.getChild(rule.getDestiny().getPath());
				if (ruleDestinyInSecondVersion == null) {
					return true;
				}
			}
			return false;
		}
	}
}
