package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenComplex;
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
	@JoinColumn(name = "origin_id", nullable = false)
	private TreeObject origin;

	@Enumerated(EnumType.STRING)
	private FlowType flowType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destiny_id")
	private TreeObject destiny;

	private boolean others;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "flow")
	@BatchSize(size = 100)
	@OrderBy(value = "sortSeq ASC")
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

	public void setOrigin(TreeObject origin) {
		this.origin = origin;
	}

	public FlowType getFlowType() {
		return flowType;
	}

	public void setFlowType(FlowType flowType) {
		this.flowType = flowType;
	}

	public TreeObject getDestiny() {
		return destiny;
	}

	public void setDestiny(TreeObject destiny) {
		this.destiny = destiny;
	}

	public boolean isOthers() {
		return others;
	}

	public void setOthers(boolean others) {
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

	public String getConditionString() {
		StringBuilder sb = new StringBuilder();

		Iterator<Token> itr = getCondition().iterator();

		while (itr.hasNext()) {
			sb.append(itr.next());
			if (itr.hasNext()) {
				sb.append(TOKEN_SEPARATOR);
			}
		}

		return sb.toString();
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Flow) {
			copyBasicInfo(object);
			// Flow elements copy
			Flow flow = (Flow) object;
			// Do not use setContent to avoid checks.
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
		HashSet<StorableObject> innerStorableObjects = new HashSet<StorableObject>();

		if (!isOthers()) {
			for (Token token : getCondition()) {
				innerStorableObjects.add(token);
			}
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
		if (isOthers() && getForm() != null) {
			List<Token> otherCondition = new ArrayList<>();

			Set<Flow> otherFlows = getForm().getFlowsFrom((BaseQuestion) origin);
			otherFlows.remove(this);

			Iterator<Flow> itr = otherFlows.iterator();

			// Generate inverse of other flow conditions.
			while (itr.hasNext()) {
				Flow flow = itr.next();
				if (flow.equals(this)) {
					// This element, pass
					continue;
				}
				if (flow.isOthers()) {
					// Two others in the same unit of flow is forbidden. This is
					// to avoid exceptions.
					return condition;
				}
				otherCondition.add(Token.not());
				otherCondition.add(Token.leftPar());
				otherCondition.addAll(flow.getCondition());
				otherCondition.add(Token.rigthPar());
				if (itr.hasNext()) {
					otherCondition.add(Token.and());
				}
			}
			return otherCondition;
		} else {
			return condition;
		}
	}
	
	/**
	 * Method to obtain simple version of the tokens. This has to be used by orbeon.
	 * @return
	 */
	public List<Token> getConditionSimpleTokens(){
		List<Token> allTokens = getCondition();
		List<Token> simplifiedTokens = new ArrayList<Token>();
		
		for(Token token: allTokens){
			if(token instanceof TokenComplex){
				simplifiedTokens.addAll(((TokenComplex) token).getSimpleTokens());
			}else{
				simplifiedTokens.add(token);
			}
		}
		
		return simplifiedTokens;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(origin);
		sb.append("->");
		if (flowType == FlowType.END_FORM) {
			sb.append("END_FORM");
		} else {
			sb.append(destiny);
		}
		sb.append(" '");
		sb.append(getConditionString());
		sb.append("'");
		return sb.toString();
	}

	public boolean isDependent(TreeObject treeObject) {
		if(origin.equals(treeObject) || (destiny!=null && destiny.equals(treeObject))){
			return true;
		}
		if(treeObject instanceof Question){
			Question question = (Question) treeObject;
			for(Token token: condition){
				if(token instanceof TokenComparationAnswer){
					if(((TokenComparationAnswer) token).getQuestion().equals(question)){
						return true;
					}
					continue;
				}
				if(token instanceof TokenComparationValue){
					if(((TokenComparationValue) token).getQuestion().equals(question)){
						return true;
					}
					continue;
				}				
			}
		}
		return false;
	}
}
