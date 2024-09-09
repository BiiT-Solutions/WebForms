package com.biit.webforms.validators;

import com.biit.form.entity.BaseQuestion;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.flow.FormWalker;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenWithQuestion;

/**
 * Redundant conditions are checked with this validator. "A -- A=1 --&gt; B --&gt; C -- A=1 --&gt; D" In this
 * example," C -- A=1 --&gt; D" is useless and can cause problems if it also has an 'OTHER' rule due to the 'OTHER' will be
 * translated as NOT(A=1) and therefore D will be shown if A=2 but C not.
 * <p>
 * This validation has a limitation. The expression will check against minterms. This means that validator will only
 * comply against simple conditions Q=A. Validation against complex entities like (Q1=A and Q2=B) was too complex and the
 * usual case is that a minterm like Q1=A is a used to reach the element.
 */
public class ValidateUselessConditions extends SimpleValidator<Flow> {

    public ValidateUselessConditions() {
        super(Flow.class);
    }

    @Override
    protected void validateImplementation(Flow flow) {
        if (!flow.getComputedCondition().isEmpty() && !flow.isOthers()) {
            for (Token token : flow.getComputedCondition()) {
                if (token instanceof TokenWithQuestion && ((TokenWithQuestion) token).getQuestion() != null) {
                    BaseQuestion question = null;
                    // Condition is not the source of the flow.
                    if (token instanceof TokenComparationValue) {
                        if (!((TokenComparationValue) token).getQuestion().equals(flow.getOrigin())) {
                            question = ((TokenComparationValue) token).getQuestion();
                        }
                    } else if (token instanceof TokenComparationAnswer) {
                        if (!((TokenComparationAnswer) token).getQuestion().equals(flow.getOrigin())) {
                            question = ((TokenComparationAnswer) token).getQuestion();
                        }
                    }
                    if (question != null) {
                        // Now let's check if there is a path from origin to this
                        // element without a flow condition with only this token.
                        if (!FormWalker.existsPathWithoutThisToken(flow.getForm(), flow.getOrigin(), token)) {
                            assertTrue(false, new FlowConditionIsUseless(flow, token));
                        }
                    }
                }
            }
        }
    }
}
