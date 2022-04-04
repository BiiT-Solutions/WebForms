package com.biit.webforms.computed;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class FilteredForm {

	private final Form originalForm;
	private final TreeObject filter;
	private final LinkedHashSet<TreeObject> filteredElements;
	private final Set<Flow> filteredFlows;
	private ComputedFlowView flows;
	private final Set<TreeObject> dependantElements;
	private Form filteredForm;

	public FilteredForm(Form originalForm, TreeObject filter) {
		this.originalForm = originalForm;
		this.filter = filter;

		filteredElements = filter.getAllNotHiddenChildrenInHierarchy(BaseQuestion.class);

		flows = originalForm.getComputedFlowsView();

		// Filter flows and get all flows that have origin or destiny
		filteredFlows = new HashSet<>();
		for (TreeObject child : filteredElements) {
			if (flows.getFlowsByOrigin(child) != null) {
				filteredFlows.addAll(flows.getFlowsByOrigin(child));
			}
			if (flows.getFlowsByDestiny(child) != null) {
				filteredFlows.addAll(flows.getFlowsByDestiny(child));
			}
		}

		// Get all elements with flow in or out of the selected elements
		dependantElements = new HashSet<>();
		for (Flow filteredFlow : filteredFlows) {
			if (!filteredElements.contains(filteredFlow.getOrigin())) {
				dependantElements.add(filteredFlow.getOrigin());
			}
			if (!filteredElements.contains(filteredFlow.getDestiny())) {
				dependantElements.add(filteredFlow.getDestiny());
			}
		}

		Set<TreeObject> selectedChildren = new HashSet<>();
		selectedChildren.addAll(filteredElements);
		selectedChildren.addAll(dependantElements);
		try {
			filteredForm = (Form) originalForm.generateCopyWithChildren(selectedChildren);
		} catch (NotValidStorableObjectException | CharacterNotAllowedException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

	}

	public Form getOriginalForm() {
		return originalForm;
	}

	public Form getFilteredForm() {
		return filteredForm;
	}

	public TreeObject getFilter() {
		return filter;
	}

	public Set<Flow> getFilteredFlows() {
		return filteredFlows;
	}

	public ComputedFlowView getFlows() {
		return flows;
	}

	public void setFlows(ComputedFlowView flows) {
		this.flows = flows;
	}

	public LinkedHashSet<TreeObject> getFilteredElements() {
		return filteredElements;
	}

	public Set<TreeObject> getDependantElements() {
		return dependantElements;
	}

	public boolean hasStartAsDependency() {
		return filteredElements != null && filteredElements.contains(flows.getFirstElement());
	}

	public boolean hasEndAsDependency() {
		for (Flow flow : filteredFlows) {
			if (flow.getFlowType() == FlowType.END_FORM) {
				return true;
			}
		}
		return false;
	}
}
