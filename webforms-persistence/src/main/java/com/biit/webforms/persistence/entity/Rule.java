package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.RuleType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.exceptions.BadRuleContentException;
import com.biit.webforms.persistence.entity.exceptions.RuleDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.RuleSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.RuleWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.RuleWithoutSource;

@Entity
@Table(name = "rules")
public class Rule extends StorableObject {

	// uniqueConstraints = { @UniqueConstraint(columnNames = { "origin_id",
	// "destiny_id" }) }

	private static final String TOKEN_SEPARATOR = " ";

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

	private boolean others;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "rule")
	private List<Token> condition;

	@ManyToOne
	private Form form;

	public Rule() {
		super();
		ruleType = RuleType.NORMAL;
		condition = new ArrayList<Token>();
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

	public boolean isOthers() {
		return others;
	}

	protected void setOthers(boolean others) {
		this.others = others;
	}

	public void setRuleContent(TreeObject origin, RuleType ruleType, TreeObject destiny, boolean others,
			List<Token> condition) throws BadRuleContentException, RuleWithoutSource,
			RuleSameOriginAndDestinyException, RuleDestinyIsBeforeOrigin, RuleWithoutDestiny {
		checkRuleRestrictions(origin, ruleType, destiny, others, condition);

		this.origin = origin;
		this.ruleType = ruleType;
		this.destiny = destiny;
		this.others = others;
		setCondition(condition);
	}

	public static void checkRuleRestrictions(TreeObject origin, RuleType ruleType, TreeObject destiny, boolean others,
			List<Token> condition) throws RuleWithoutSource, BadRuleContentException,
			RuleSameOriginAndDestinyException, RuleDestinyIsBeforeOrigin, RuleWithoutDestiny {
		// No rule without source
		if (origin == null) {
			throw new RuleWithoutSource();
		}
		// If rule type doesn't need destiny, destiny must be null and otherwise
		if ((ruleType.isDestinyNull() && destiny != null) || (!ruleType.isDestinyNull() && destiny == null)) {
			throw new RuleWithoutDestiny();
		}

		if (others && (condition == null || !condition.isEmpty())) {
			throw new BadRuleContentException("Rules with other must have empty condition");
		}
		if (!others && condition == null) {
			throw new BadRuleContentException("Rules that are not other must have a not null condition string.");
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
		String conditionString = new String();

		for (Token token : condition) {
			conditionString += token + TOKEN_SEPARATOR;
		}

		return conditionString;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Rule) {
			copyBasicInfo(object);
			// Rule elements copy
			Rule rule = (Rule) object;
			try {
				setRuleContent(rule.getOrigin(), rule.getRuleType(), rule.getDestiny(), rule.isOthers(),
						rule.generateCopyCondition());
			} catch (BadRuleContentException | RuleWithoutSource | RuleSameOriginAndDestinyException
					| RuleDestinyIsBeforeOrigin | RuleWithoutDestiny e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ Rule.class.getName());
		}
	}

	/**
	 * generates a new copy of the rule and its tokens.
	 * 
	 * @return
	 */
	public Rule generateCopy() {
		Rule newInstance = null;

		// Store object copy
		try {
			newInstance = this.getClass().newInstance();
			newInstance.copyData(this);
		} catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		return newInstance;
	}

	private List<Token> generateCopyCondition() {
		List<Token> conditionCopy = new ArrayList<Token>();
		for (Token token : condition) {
			conditionCopy.add(token.generateCopy());
		}
		return conditionCopy;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		// Return nothing
		HashSet<StorableObject> innerStorableObjects = new HashSet<StorableObject>();
		
		for(Token token: getCondition()){
			innerStorableObjects.add(token);
		}
		return innerStorableObjects;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Form getForm() {
		return form;
	}

	public List<Token> getCondition() {
		return condition;
	}

	protected void setCondition(List<Token> condition) {
		this.condition.clear();
		this.condition.addAll(condition);
		for (Token token : this.condition) {
			token.setRule(this);
		}
	}

	public void updateConditionSortSeq() {
		for (int i = 0; i < condition.size(); i++) {
			condition.get(i).setSortSeq(i);
		}
	}

	/**
	 * This functions updates references to question and answers If a reference
	 * is missing it will throw a {@code UpdateNullReferenceException}
	 * 
	 * @param mappedCopiedQuestions
	 * @param mappedCopiedAnswers
	 * @throws UpdateNullReferenceException
	 */
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		if (getOrigin() != null) {
			setOrigin(mappedElements.get(getOrigin().getComparationId()));
		}
		if (getDestiny() != null) {
			setDestiny(mappedElements.get(getDestiny().getComparationId()));
		}
		for (Token token : getCondition()) {
			token.updateReferences(mappedElements);
		}
	}

	@Override
	public void resetIds() {
		super.resetIds();
		for (Token token : getCondition()) {
			token.resetIds();
		}
	}
}
