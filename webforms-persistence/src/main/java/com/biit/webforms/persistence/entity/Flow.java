package com.biit.webforms.persistence.entity;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
import com.biit.webforms.persistence.entity.condition.TokenEmpty;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.serialization.FlowDeserializer;
import com.biit.webforms.serialization.FlowSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonDeserialize(using = FlowDeserializer.class)
@JsonSerialize(using = FlowSerializer.class)
@Table(name = "flow")
@Cacheable
public class Flow extends StorableObject {
    private static final long serialVersionUID = 2814442921737652036L;

    private static final String TOKEN_SEPARATOR = " ";

    // Hibernate changes name of column when you use a many-to-one relationship.
    // If you want to add a constraint
    // attached to that column, you have to state the name.
    @ManyToOne
    @JoinColumn(name = "origin_id")
    private BaseQuestion origin;

    @Enumerated(EnumType.STRING)
    @Column(name = "flow_type")
    private FlowType flowType;

    @ManyToOne
    @JoinColumn(name = "destiny_id")
    private BaseQuestion destiny;

    private boolean others;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "flow")
    @BatchSize(size = 100)
    @OrderBy(value = "sortSeq ASC")
    private List<Token> condition;

    @ManyToOne(optional = false)
    private Form form;

    @Transient
    private boolean generated;

    private transient boolean readOnly;

    //Only for json serialization.
    @Transient
    private transient List<String> originReferencePath;

    @Transient
    private transient List<String> destinyReferencePath;

    public Flow() {
        super();
        flowType = FlowType.NORMAL;
        condition = new ArrayList<>();
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

        Iterator<Token> itr = getComputedCondition().iterator();

        while (itr.hasNext()) {
            sb.append(itr.next());
            if (itr.hasNext()) {
                sb.append(TOKEN_SEPARATOR);
            }
        }

        return sb.toString();
    }

    public String getConditionStringWithFormat() {
        StringBuilder sb = new StringBuilder();

        Iterator<Token> itr = getComputedCondition().iterator();

        while (itr.hasNext()) {
            Token next = itr.next();
            if (next.getType() == TokenTypes.RETURN) {
                sb.append("\\n");
            } else {
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
            this.setForm(flow.getForm());
            this.setGenerated(flow.isGenerated());
            this.setReadOnly(flow.isReadOnly());
        } else {
            throw new NotValidStorableObjectException(
                    object.getClass().getName() + " is not compatible with " + Flow.class.getName());
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
            newInstance = this.getClass().getDeclaredConstructor().newInstance();
            newInstance.copyData(this);
        } catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException
                 | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            // Impossible
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }

        return newInstance;
    }

    private List<Token> generateCopyCondition() {
        List<Token> conditionCopy = new ArrayList<Token>();
        // We only copy the real conditions tokens, we cannot use
        // getCondition();
        for (Token token : condition) {
            if (token != null) {
                conditionCopy.add(token.generateCopy());
            }
        }
        return conditionCopy;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        HashSet<StorableObject> innerStorableObjects = new HashSet<StorableObject>();

        if (!isOthers()) {
            for (Token token : getComputedCondition()) {
                if (token != null) {
                    innerStorableObjects.add(token);
                    innerStorableObjects.addAll(token.getAllInnerStorableObjects());
                }
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

    /**
     * Return condition that can computed. For example others is translated to
     * Tokens.
     *
     * @return
     */
    public List<Token> getComputedCondition() {
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
        List<Token> allTokens = getComputedCondition();
        List<Token> simplifiedTokens = new ArrayList<Token>();

        for (Token token : allTokens) {
            if (token != null) {
                if (token instanceof TokenComplex) {
                    simplifiedTokens.addAll(((TokenComplex) token).getSimpleTokens());
                } else {
                    simplifiedTokens.add(token);
                }
            }
        }

        return simplifiedTokens;
    }

    public void setCondition(List<Token> condition) {
        this.condition.clear();
        this.condition.addAll(condition);
        // We only copy the real conditions tokens, we cannot use
        // getCondition();
        for (Token token : condition) {
            if (token != null) {
                token.setFlow(this);
            }
        }
    }

    public void updateConditionSortSeq() {
        for (int i = 0; i < condition.size(); i++) {
            if (condition.get(i) != null) {
                condition.get(i).setSortSeq(i);
            }
        }
    }

    /**
     * This functions updates references to question and answers If a reference is
     * missing it will throw a {@code UpdateNullReferenceException}
     */
    public void updateReferences(HashMap<String, TreeObject> mappedElements) {
        if (getOrigin() != null) {
            setOrigin((BaseQuestion) mappedElements.get(getOrigin().getOriginalReference()));
        }
        if (getDestiny() != null) {
            setDestiny((BaseQuestion) mappedElements.get(getDestiny().getOriginalReference()));
        }
        for (Token token : getComputedCondition()) {
            if (token != null) {
                token.updateReferences(mappedElements);
            }
        }
    }

    @Override
    public void resetIds() {
        super.resetIds();
        for (Token token : getComputedCondition()) {
            if (token != null) {
                token.resetIds();
            }
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
            if (token != null) {
                if (token instanceof TokenComparationAnswer) {
                    if (((TokenComparationAnswer) token).getAnswer() != null
                            && ((TokenComparationAnswer) token).getAnswer().equals(answer)) {
                        return true;
                    }
                    continue;
                }
                if (token instanceof TokenIn) {
                    for (TokenInValue inValue : ((TokenIn) token).getValues()) {
                        if (inValue.getAnswerValue() != null && inValue.getAnswerValue().equals(answer)) {
                            return true;
                        }
                    }
                    continue;
                }
            }
        }
        return false;
    }

    public boolean isDependent(WebformsBaseQuestion question) {
        if (origin.equals(question) || (destiny != null && destiny.equals(question))) {
            return true;
        }

        for (Token token : condition) {
            if (token != null) {
                if (token instanceof TokenComparationAnswer) {
                    if (((TokenComparationAnswer) token).getQuestion() != null
                            && Objects.equals(((TokenComparationAnswer) token).getQuestion(), question)) {
                        return true;
                    }
                    continue;
                }
                if (token instanceof TokenComparationValue) {
                    if (((TokenComparationValue) token).getQuestion() != null
                            && Objects.equals(((TokenComparationValue) token).getQuestion(), question)) {
                        return true;
                    }
                    continue;
                }
                if (token instanceof TokenBetween) {
                    if (((TokenBetween) token).getQuestion() != null
                            && Objects.equals(((TokenBetween) token).getQuestion(), question)) {
                        return true;
                    }
                    continue;
                }
                if (token instanceof TokenEmpty) {
                    if (((TokenEmpty) token).getQuestion() != null
                            && Objects.equals(((TokenEmpty) token).getQuestion(), question)) {
                        return true;
                    }
                    continue;
                }
                if (token instanceof TokenIn) {
                    if (((TokenIn) token).getQuestion() != null
                            && Objects.equals(((TokenIn) token).getQuestion(), question)) {
                        return true;
                    }
                    continue;
                }
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

    public boolean isContentEqual(Flow flow) {

        if (!getOrigin().getPathName().equals(flow.getOrigin().getPathName())) {
            return false;
        }

        if (getFlowType() != flow.getFlowType()) {
            return false;
        }

        if ((destiny != null && flow.destiny == null) || (destiny == null) && flow.destiny != null) {
            return false;
        }

        if (destiny != null && flow.destiny != null && !destiny.getPathName().equals(flow.destiny.getPathName())) {
            return false;
        }

        if (others != flow.others) {
            return false;
        }

        if ((getComputedCondition() != null && flow.getComputedCondition() == null)
                || (getComputedCondition() == null && flow.getComputedCondition() != null)) {
            return false;
        }

        if (getComputedCondition() != null && flow.getComputedCondition() != null) {
            if (getComputedCondition().size() != flow.getComputedCondition().size()) {
                return false;
            }
            for (int i = 0; i < getComputedCondition().size(); i++) {
                if (!getComputedCondition().get(i).isContentEqual(flow.getComputedCondition().get(i))) {
                    return false;
                }
            }
        }

        if (generated != flow.generated) {
            return false;
        }

        if (readOnly != flow.readOnly) {
            return false;
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
        for (Token token : getComputedCondition()) {
            if (token != null) {
                token.resetUserTimestampInfo(userId);
            }
        }
    }

    /**
     * A flow is hidden if any of its element (origin, destination, condition) is
     * hidden.
     *
     * @return
     */
    public boolean isHidden() {
        // Check source and destiny.
        if ((getOrigin() != null && getOrigin().isHiddenElement())
                || (getDestiny() != null && getDestiny().isHiddenElement())) {
            return true;
        }
        // Check condition.
        List<Token> tokens = getConditionSimpleTokens();
        for (Token token : tokens) {
            if (token != null) {
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
        }

        return false;
    }

    public List<String> getOriginReferencePath() {
        return originReferencePath;
    }

    public void setOriginReferencePath(List<String> originReferencePath) {
        this.originReferencePath = originReferencePath;
    }

    public List<String> getDestinyReferencePath() {
        return destinyReferencePath;
    }

    public void setDestinyReferencePath(List<String> destinyReferencePath) {
        this.destinyReferencePath = destinyReferencePath;
    }

    public List<Token> getCondition() {
        return condition;
    }
}
