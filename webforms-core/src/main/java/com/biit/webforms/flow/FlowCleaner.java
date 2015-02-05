package com.biit.webforms.flow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;

public class FlowCleaner {
	
	private Form form;
	private Set<Flow> otherFlowsRemoved;
	private Set<Flow> uselessFlowRemoved;

	public FlowCleaner(Form form) {
		this.form = form;
		otherFlowsRemoved = new HashSet<>();
		uselessFlowRemoved = new HashSet<>();
	}

	public void cleanFlow() {
		LinkedHashSet<TreeObject> questions = form.getAllChildrenInHierarchy(BaseQuestion.class);
		Iterator<TreeObject> questionIterator = questions.iterator();
		TreeObject question = null;
		TreeObject nextQuestion = null;
		while (questionIterator.hasNext()) {
			nextQuestion = questionIterator.next();
			if (question != null) {
				Set<Flow> flows = form.getFlowsFrom((BaseQuestion) question);
				clearInvalidOthersFlow((BaseQuestion) question, flows);
				removeUselessFlows((BaseQuestion) question, (BaseQuestion) nextQuestion, flows);
			}
			question = nextQuestion;
		}
	}

	private void clearInvalidOthersFlow(TreeObject question, Set<Flow> flows) {
		// Others is not allowed if is the only question.
		if (flows.size() == 1) {
			Flow flow = flows.iterator().next();
			if (!flow.isReadOnly() && flow.isOthers()) {
				flow.setOthers(false);
				// Add a copy because maybe changed by other rules.
				otherFlowsRemoved.add(flow.generateCopy());
			}
		}
	}

	private void removeUselessFlows(BaseQuestion question, BaseQuestion nextQuestion, Set<Flow> flows) {
		// One flow without condition that goes to next element, is the default
		// one, we can remove it.
		if (flows.size() == 1) {
			Flow flow = flows.iterator().next();
			// Flow point to nextQuestion.
			if ((flow != null) && (flow.getDestiny() != null) && (nextQuestion != null)) {
			if (!flow.isReadOnly() && flow.getDestiny().equals(nextQuestion)) {
				// Has no condition.
				if (flow.getCondition() == null || flow.getCondition().isEmpty()) {
					form.removeRule(flow);
					uselessFlowRemoved.add(flow);
					}
				}
			}
		}
	}

	public Set<Flow> getOtherFlowsRemoved() {
		return otherFlowsRemoved;
	}

	public Set<Flow> getUselessFlowRemoved() {
		return uselessFlowRemoved;
	}

}
