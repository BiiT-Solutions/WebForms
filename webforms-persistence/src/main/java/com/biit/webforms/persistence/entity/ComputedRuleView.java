package com.biit.webforms.persistence.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.enumerations.RuleType;
import com.biit.webforms.persistence.entity.exceptions.BadRuleContentException;
import com.biit.webforms.persistence.entity.exceptions.RuleDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.RuleSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.RuleWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.RuleWithoutSource;

public class ComputedRuleView {

	private static final String NEXT_ELEMENT_CONDITION = "";
	private TreeObject firstElement;
	private Set<Rule> rules;
	private HashMap<TreeObject, Set<Rule>> rulesByOrigin;
	private HashMap<TreeObject, Set<Rule>> rulesByDestiny;

	public ComputedRuleView() {
		rules = new HashSet<Rule>();
		rulesByOrigin = new HashMap<>();
		rulesByDestiny = new HashMap<>();
	}

	public void addRule(Rule newRule) {
		rules.add(newRule);
		updateMapsAddRule(newRule);
	}

	public void addRules(Set<Rule> rules) {
		this.rules.addAll(rules);
		for (Rule rule : rules) {
			updateMapsAddRule(rule);
		}
	}

	private void updateMapsAddRule(Rule newRule) {
		if (!rulesByOrigin.containsKey(newRule.getOrigin())) {
			rulesByOrigin.put(newRule.getOrigin(), new HashSet<Rule>());
		}
		rulesByOrigin.get(newRule.getOrigin()).add(newRule);
		if (newRule.getDestiny() != null) {
			if (!rulesByDestiny.containsKey(newRule.getDestiny())) {
				rulesByDestiny.put(newRule.getDestiny(), new HashSet<Rule>());
			}
			rulesByDestiny.get(newRule.getDestiny()).add(newRule);
		}
	}

	public Set<Rule> getRules() {
		return rules;
	}

	public Set<Rule> getRulesByOrigin(TreeObject origin) {
		return rulesByOrigin.get(origin);
	}

	public Set<Rule> getRulesByDestiny(TreeObject destiny) {
		return rulesByDestiny.get(destiny);
	}

	public HashMap<TreeObject, Set<Rule>> getRulesByOrigin() {
		return rulesByOrigin;
	}

	public HashMap<TreeObject, Set<Rule>> getRulesByDestiny() {
		return rulesByDestiny;
	}

	/**
	 * Creates a new computed go to next element rule. Type normal, condition =
	 * '' -> true
	 * 
	 * @param origin
	 * @param destiny
	 */
	public void addNewNextElementRule(TreeObject origin, TreeObject destiny) {
		Rule newRule = new Rule();
		try {
			newRule.setRuleContent(origin, RuleType.NORMAL, destiny, false, NEXT_ELEMENT_CONDITION);
			addRule(newRule);
		} catch (BadRuleContentException | RuleWithoutSource | RuleSameOriginAndDestinyException
				| RuleDestinyIsBeforeOrigin | RuleWithoutDestiny e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public void addNewEndFormRule(TreeObject origin) {
		Rule newRule = new Rule();
		try {
			newRule.setRuleContent(origin, RuleType.END_FORM, null, false, NEXT_ELEMENT_CONDITION);
			addRule(newRule);
		} catch (BadRuleContentException | RuleWithoutSource | RuleSameOriginAndDestinyException
				| RuleDestinyIsBeforeOrigin | RuleWithoutDestiny e) {
			// Imposible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public TreeObject getFirstElement() {
		return firstElement;
	}

	public void setFirstElement(TreeObject firstElement) {
		this.firstElement = firstElement;
	}
}
