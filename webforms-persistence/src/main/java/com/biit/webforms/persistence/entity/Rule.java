package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.enumerations.RuleType;
import com.biit.webforms.persistence.entity.exceptions.BadRuleContentException;
import com.biit.webforms.persistence.entity.exceptions.RuleDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.RuleSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.RuleWithoutSource;

@Entity
@Table(name = "rules")
public class Rule extends StorableObject {

	// uniqueConstraints = { @UniqueConstraint(columnNames = { "origin_id",
	// "destiny_id" }) }

	/*
	 * Hibernate changes name of column when you use a many-to-one relationship.
	 * If you want to add a constraint attached to that column, you have to
	 * state the name.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "origin_id")
	private TreeObject origin;

	@Enumerated(EnumType.STRING)
	private RuleType ruleType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destiny_id")
	private TreeObject destiny;

	private String conditionString;

	@Transient
	private Object interpretedRule;

	public Rule() {
		super();
		ruleType = RuleType.NORMAL;
		conditionString = new String();
	}

	public TreeObject getOrigin() {
		return origin;
	}

	protected void setOrigin(TreeObject origin) {
		this.origin = origin;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	protected void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}

	public TreeObject getDestiny() {
		return destiny;
	}

	protected void setDestiny(TreeObject destiny) {
		this.destiny = destiny;
	}

	public void setRuleContent(TreeObject origin, RuleType ruleType, TreeObject destiny, String conditionString)
			throws BadRuleContentException, RuleWithoutSource, RuleSameOriginAndDestinyException,
			RuleDestinyIsBeforeOrigin {
		checkRuleRestrictions(origin, ruleType, destiny, conditionString);

		this.origin = origin;
		this.ruleType = ruleType;
		this.destiny = destiny;
		this.conditionString = conditionString;
	}

	public static void checkRuleRestrictions(TreeObject origin, RuleType ruleType, TreeObject destiny,
			String conditionString) throws RuleWithoutSource, BadRuleContentException,
			RuleSameOriginAndDestinyException, RuleDestinyIsBeforeOrigin {
		// No rule without source
		if (origin == null) {
			throw new RuleWithoutSource();
		}
		// If rule type doesn't need destiny, destiny must be null and otherwise
		// If rule type doesn't need conditions, conditionString must be null or
		// empty
		if ((ruleType.isDestinyNull() && destiny != null) || (!ruleType.isDestinyNull() && destiny == null)
				|| (ruleType.isOthers() && conditionString != null && !conditionString.isEmpty())
				|| (!ruleType.isOthers() && conditionString == null)) {
			throw new BadRuleContentException();
		}
		// Rule origin can't be destiny
		if (origin.equals(destiny)) {
			throw new RuleSameOriginAndDestinyException();
		}
		// Rule destiny cannot be prior to origin.
		if (!ruleType.isDestinyNull()) {
			if (!(origin.compareTo(destiny) == -1)) {
				throw new RuleDestinyIsBeforeOrigin();
			}
		}
	}

	/**
	 * Returns a string view of the rule. If the rule hasn't been interpreted,
	 * we do the parse of the current rule form the database string. If has been
	 * initialized, then we return the string view from the current
	 * Interpretation status.
	 * 
	 * @return
	 */
	public String getConditionString() {
		if (getInterpretedRule() == null) {
			// TODO get the new ruleString interpretation
		}
		// If not gets the original setted ruleString
		return conditionString;
	}

	protected void setConditionString(String conditionString) {
		this.conditionString = conditionString;
	}

	/**
	 * Retrieves the rule interpretation
	 * 
	 * @return
	 */
	public Object getInterpretedRule() {
		return interpretedRule;
	}

	public Rule generateCopy() {
		Rule newInstance = null;

		// Store object copy
		try {
			newInstance = this.getClass().newInstance();
			newInstance.setId(getId());
			newInstance.setComparationId(getComparationId());
			newInstance.setCreatedBy(getCreatedBy());
			newInstance.setUpdatedBy(getUpdatedBy());

			// Rule elements copy
			newInstance.setRuleContent(origin, ruleType, destiny, conditionString);
		} catch (InstantiationException | IllegalAccessException | BadRuleContentException | RuleWithoutSource
				| RuleSameOriginAndDestinyException | RuleDestinyIsBeforeOrigin e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		return newInstance;
	}
}
