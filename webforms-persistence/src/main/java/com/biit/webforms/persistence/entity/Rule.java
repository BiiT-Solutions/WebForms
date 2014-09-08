package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.webforms.persistence.entity.enumerations.RuleType;
import com.biit.webforms.persistence.entity.exceptions.RuleBadDestinyParameterException;

@Entity
@Table(name = "rules", uniqueConstraints = { @UniqueConstraint(columnNames = { "origin_id", "destiny_id" }) })
public class Rule extends StorableObject {

	/*
	 * Hibernate changes name of column when you use a many-to-one relationship.
	 * If you want to add a constraint attachet to that column, you have to
	 * state the name.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "origin_id")
	private TreeObject origin;

	@Enumerated(EnumType.STRING)
	private RuleType sourceType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destiny_id")
	private TreeObject destiny;

	private String ruleString;

	@Transient
	private Object interpretedRule;

	public Rule() {
	}

	public TreeObject getOrigin() {
		return origin;
	}

	public void setOrigin(TreeObject origin) {
		this.origin = origin;
	}

	public RuleType getSourceType() {
		return sourceType;
	}

	protected void setSourceType(RuleType sourceType) {
		this.sourceType = sourceType;
	}

	public TreeObject getDestiny() {
		return destiny;
	}

	protected void setDestiny(TreeObject destiny) {
		this.destiny = destiny;
	}

	public void setDestiny(RuleType sourceType, TreeObject destiny) throws RuleBadDestinyParameterException {
		if ((sourceType.isDestinyNull() && destiny != null) || (!sourceType.isDestinyNull() && destiny == null)) {
			throw new RuleBadDestinyParameterException();
		} else {
			this.sourceType = sourceType;
			this.destiny = destiny;
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
	public String getRuleString() {
		if (getInterpretedRule() == null) {
			// TODO get the new ruleString interpretation
		}
		// If not gets the original setted ruleString
		return ruleString;
	}

	protected void setRuleString(String ruleString) {
		this.ruleString = ruleString;
	}

	/**
	 * Retrueves the rule interpretation
	 * 
	 * @return
	 */
	public Object getInterpretedRule() {
		return interpretedRule;
	}
}
