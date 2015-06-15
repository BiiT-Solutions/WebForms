package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
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
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenComplex;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;

@Entity
@Table(name = "flow")
@Cacheable(true)
public class Flow extends StorableObject {
	private static final long serialVersionUID = 2814442921737652036L;

	private static final String TOKEN_SEPARATOR = " ";

	// Hibernate changes name of column when you use a many-to-one relationship. If you want to add a constraint
	// attached to that column, you have to state the name.
	@ManyToOne
	@JoinColumn(name = "origin_id")
	private BaseQuestion origin;

	@Enumerated(EnumType.STRING)
	private FlowType flowType;

	@ManyToOne
	@JoinColumn(name = "destiny_id")
	private BaseQuestion destiny;

	private boolean others;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "flow")
	@BatchSize(size = 100)
	@OrderBy(value = "sortSeq ASC")
	private List<Token> condition;

	@ManyToOne(optional = false)
	private Form form;

	@Transient
	private boolean generated;

	private transient boolean readOnly;

	public Flow() {
		super();
		flowType = FlowType.NORMAL;
		condition = new ArrayList<Token>();
		generated = false;
		readOnly = false;
	}

	public BaseQuestion getOrigin() {
		return origin;
	}

	public void setOrigin(BaseQuestion origin) {
		this.origin = origin;
	}

	public FlowType getFlowType() {
		return flowType;
	}

	public void setFlowType(FlowType flowType) {
		this.flowType = flowType;
	}

	public BaseQuestion getDestiny() {
		return destiny;
	}

	public void setDestiny(BaseQuestion destiny) {
		this.destiny = destiny;
	}

	public boolean isOthers() {
		return others;
	}

	public void setOthers(boolean others) {
		this.others = others;
	}

	public void setContent(BaseQuestion origin, FlowType flowType, BaseQuestion destiny, boolean others,
			List<Token> condition) throws BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException {
		checkFlowRestrictions(origin, flowType, destiny, others, condition);

		this.origin = origin;
		this.flowType = flowType;
		this.destiny = destiny;
		this.others = others;
		setCondition(condition);
	}

	public static void checkFlowRestrictions(TreeObject origin, FlowType flowType, TreeObject destiny, boolean others,
			List<Token> condition) throws FlowWithoutSourceException, BadFlowContentException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException {
		// No flow without source
		if (origin == null) {
			throw new FlowWithoutSourceException();
		}
		// If flow type doesn't need destiny, destiny must be null and otherwise
		if ((flowType.isDestinyNull() && destiny != null) || (!flowType.isDestinyNull() && destiny == null)) {
			throw new FlowWithoutDestinyException();
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
				throw new FlowDestinyIsBeforeOriginException();
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
	
	public String getConditionStringWithFormat(){
		StringBuilder sb = new StringBuilder();

		Iterator<Token> itr = getCondition().iterator();

		while (itr.hasNext()) {
			Token next = itr.next();
			if(next.getType()==TokenTypes.RETURN){
				sb.append("\n");
			}else{
				sb.append(next);
			}
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
				innerStorableObjects.addAll(token.getAllInnerStorableObjects());
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
				otherCondition.add(Token.getNotToken());
				otherCondition.add(Token.getLeftParenthesisToken());
				otherCondition.addAll(flow.getConditionSimpleTokens());
				otherCondition.add(Token.getRigthParenthesisToken());
				if (itr.hasNext()) {
					otherCondition.add(Token.getAndToken());
				}
			}
			return otherCondition;
		} else {
			return condition;
		}
	}

	/**
	 * Method to obtain simple version of the tokens. This has to be used by orbeon.
	 * 
	 * @return
	 */
	public List<Token> getConditionSimpleTokens() {
		List<Token> allTokens = getCondition();
		List<Token> simplifiedTokens = new ArrayList<Token>();

		for (Token token : allTokens) {
			if (token instanceof TokenComplex) {
				simplifiedTokens.addAll(((TokenComplex) token).getSimpleTokens());
			} else {
				simplifiedTokens.add(token);
			}
		}

		return simplifiedTokens;
	}

	public void setCondition(List<Token> condition) {
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
	 * This functions updates references to question and answers If a reference is missing it will throw a
	 * {@code UpdateNullReferenceException}
	 * 
	 * @param mappedCopiedQuestions
	 * @param mappedCopiedAnswers
	 * @throws UpdateNullReferenceException
	 */
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		if (getOrigin() != null) {
			setOrigin((BaseQuestion) mappedElements.get(getOrigin().getOriginalReference()));
		}
		if (getDestiny() != null) {
			setDestiny((BaseQuestion) mappedElements.get(getDestiny().getOriginalReference()));
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
		sb.append(" to ");
		if (flowType == FlowType.END_FORM) {
			sb.append("END_FORM");
		} else {
			sb.append(destiny);
		}

		String condition = getConditionString();
		if (condition.length() > 0) {
			sb.append(" ['");
			sb.append(condition);
			sb.append("']");
		}
		return sb.toString();
	}

	public boolean isDependent(TreeObject treeObject) {
		return false;
	}

	public boolean isDependent(Answer answer) {
		for (Token token : condition) {
			if (token instanceof TokenComparationAnswer) {
				if (((TokenComparationAnswer) token).getAnswer().equals(answer)) {
					return true;
				}
				continue;
			}
			if (token instanceof TokenIn) {
				for (TokenInValue inValue : ((TokenIn) token).getValues()) {
					if (inValue.getAnswerValue().equals(answer)) {
						return true;
					}
				}
				continue;
			}
		}
		return false;
	}

	public boolean isDependent(WebformsBaseQuestion question) {
		if (origin.equals(question) || (destiny != null && destiny.equals(question))) {
			return true;
		}

		for (Token token : condition) {
			if (token instanceof TokenComparationAnswer) {
				if (((TokenComparationAnswer) token).getQuestion().equals(question)) {
					return true;
				}
				continue;
			}
			if (token instanceof TokenComparationValue) {
				if (((TokenComparationValue) token).getQuestion().equals(question)) {
					return true;
				}
				continue;
			}
			if (token instanceof TokenBetween) {
				if (((TokenBetween) token).getQuestion().equals(question)) {
					return true;
				}
				continue;
			}
			if (token instanceof TokenIn) {
				if (((TokenIn) token).getQuestion().equals(question)) {
					return true;
				}
				continue;
			}
		}
		return false;
	}

	public boolean isGenerated() {
		return generated;
	}

	public void setGenerated(boolean value) {
		this.generated = value;
	}

	public boolean isContentEqual(Flow formRule) {

		if (!origin.getPathName().equals(formRule.origin.getPathName())) {
			return false;
		}

		if (flowType != formRule.flowType) {
			return false;
		}

		if ((destiny != null && formRule.destiny == null) || (destiny == null) && formRule.destiny != null) {
			return false;
		}

		if (destiny != null && formRule.destiny != null
				&& !destiny.getPathName().equals(formRule.destiny.getPathName())) {
			return false;
		}

		if (others != formRule.others) {
			return false;
		}

		if ((condition != null && formRule.condition == null) || (condition == null && formRule.condition != null)) {
			return false;
		}

		if (condition != null && formRule.condition != null) {
			if (condition.size() != formRule.condition.size()) {
				return false;
			}
			for (int i = 0; i < condition.size(); i++) {
				if (!condition.get(i).isContentEqual(formRule.condition.get(i))) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public void resetUserTimestampInfo(Long userId) {
		super.resetUserTimestampInfo(userId);
		for (Token token : getCondition()) {
			token.resetUserTimestampInfo(userId);
		}
	}

	/**
	 * A flow is hidden if any of its element (origin, destination, condition) is hidden.
	 * 
	 * @param block
	 * @param flow
	 * @return
	 */
	public boolean isHidden() {
		// Check source and destiny.
		if (getOrigin().isHiddenElement() || getDestiny().isHiddenElement()) {
			return true;
		}
		// Check condition.
		List<Token> tokens = getConditionSimpleTokens();
		for (Token token : tokens) {
			if (token instanceof TokenComparationAnswer) {
				if (((TokenComparationAnswer) token).getQuestion().isHiddenElement()
						|| ((TokenComparationAnswer) token).getAnswer().isHiddenElement()) {
					return true;
				}
			} else if (token instanceof TokenComparationValue) {
				if (((TokenComparationValue) token).getQuestion().isHiddenElement()) {
					return true;
				}
			}
		}

		return false;
	}
}
