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

import org.hibernate.annotations.BatchSize;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;

@Entity
@Table(name = "flow")
public class Flow extends StorableObject {

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
	private FlowType flowType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destiny_id")
	private TreeObject destiny;

	private boolean others;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "flow")
	@BatchSize(size = 100)
	private List<Token> condition;

	@ManyToOne
	private Form form;

	public Flow() {
		super();
		flowType = FlowType.NORMAL;
		condition = new ArrayList<Token>();
	}

	public TreeObject getOrigin() {
		return origin;
	}

	protected void setOrigin(TreeObject origin) {
		this.origin = origin;
	}

	public FlowType getFlowType() {
		return flowType;
	}

	protected void setFlowType(FlowType flowType) {
		this.flowType = flowType;
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

	public void setContent(TreeObject origin, FlowType flowType, TreeObject destiny, boolean others,
			List<Token> condition) throws BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {
		checkFlowRestrictions(origin, flowType, destiny, others, condition);

		this.origin = origin;
		this.flowType = flowType;
		this.destiny = destiny;
		this.others = others;
		setCondition(condition);
	}

	public static void checkFlowRestrictions(TreeObject origin, FlowType flowType, TreeObject destiny, boolean others,
			List<Token> condition) throws FlowWithoutSource, BadFlowContentException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {
		// No flow without source
		if (origin == null) {
			throw new FlowWithoutSource();
		}
		// If flow type doesn't need destiny, destiny must be null and otherwise
		if ((flowType.isDestinyNull() && destiny != null) || (!flowType.isDestinyNull() && destiny == null)) {
			throw new FlowWithoutDestiny();
		}

		if (others && (condition == null || !condition.isEmpty())) {
			throw new BadFlowContentException("Flows with other must have empty condition");
		}
		if (!others && condition == null) {
			throw new BadFlowContentException("Flows that are not other must have a not null condition string.");
		}

		// Flow origin can't be destiny
		if (origin.equals(destiny)) {
			throw new FlowSameOriginAndDestinyException();
		}
		// Flow destiny cannot be prior to origin.
		if (!flowType.isDestinyNull()) {
			if (!(origin.compareTo(destiny) == -1)) {
				throw new FlowDestinyIsBeforeOrigin();
			}
		}
	}

	/**
	 * Returns a string view of the flow. If the flow hasn't been interpreted,
	 * we do the parse of the current flow form the database string. If has been
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
		if (object instanceof Flow) {
			copyBasicInfo(object);
			// Flow elements copy
			Flow flow = (Flow) object;
			//Do not use setContent to avoid checks.
			this.setOrigin(flow.getOrigin());
			this.setFlowType(flow.getFlowType());
			this.setDestiny(flow.getDestiny());
			this.setOthers(flow.isOthers());
			this.setCondition(flow.generateCopyCondition());
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ Flow.class.getName());
		}
	}

	/**
	 * generates a new copy of the flow and its tokens.
	 * 
	 * @return
	 */
	public Flow generateCopy() {
		Flow newInstance = null;

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

		for (Token token : getCondition()) {
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
			token.setFlow(this);
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
