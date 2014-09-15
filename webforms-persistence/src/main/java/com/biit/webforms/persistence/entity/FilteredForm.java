package com.biit.webforms.persistence.entity;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.enumerations.RuleType;

public class FilteredForm {

	private Form originalForm;
	private TreeObject filter;
	private LinkedHashSet<TreeObject> filteredElements;
	private Set<Rule> filteredRules;
	private ComputedRuleView rules;
	private Set<TreeObject> dependantElements;
	private Form filteredForm;

	public FilteredForm(Form originalForm, TreeObject filter) {
		this.originalForm = originalForm;
		this.filter = filter;

		filteredElements = filter.getAllChildrenInHierarchy(Question.class);

		rules = originalForm.getComputedRuleView();
		
		// Filter rules and get all rules that have origin or destiny
		filteredRules = new HashSet<>();
		for (TreeObject child : filteredElements) {
			if(rules.getRulesByOrigin(child)!=null){
				filteredRules.addAll(rules.getRulesByOrigin(child));
			}
			if(rules.getRulesByDestiny(child)!=null){
				filteredRules.addAll(rules.getRulesByDestiny(child));
			}
		}

		// Get all elements with flow in or out of the selected elements
		dependantElements = new HashSet<TreeObject>();
		for (Rule filteredRule : filteredRules) {
			if (!filteredElements.contains(filteredRule.getOrigin())) {
				dependantElements.add(filteredRule.getOrigin());
			}
			if (!filteredElements.contains(filteredRule.getDestiny())) {
				dependantElements.add(filteredRule.getDestiny());
			}
		}

		Set<TreeObject> selectedChilds = new HashSet<TreeObject>();
		selectedChilds.addAll(filteredElements);
		selectedChilds.addAll(dependantElements);
		try {
			filteredForm = (Form) originalForm.generateCopyWithChilds(selectedChilds);
		} catch (NotValidTreeObjectException e) {
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

	public Set<Rule> getFilteredRules() {
		return filteredRules;
	}

	public ComputedRuleView getRules() {
		return rules;
	}

	public void setRules(ComputedRuleView rules) {
		this.rules = rules;
	}

	public LinkedHashSet<TreeObject> getFilteredElements() {
		return filteredElements;
	}

	public Set<TreeObject> getDependantElements() {
		return dependantElements;
	}

	public boolean hasStartAsDependency() {
		return filteredElements != null && filteredElements.contains(rules.getFirstElement());
	}

	public boolean hasEndAsDependency() {
		for (Rule rule : filteredRules) {
			if (rule.getRuleType() == RuleType.END_FORM) {
				return true;
			}
		}
		return false;
	}
}
