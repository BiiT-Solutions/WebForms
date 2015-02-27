package com.biit.webforms.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;

public class FormWalker {

	/**
	 * If origin or destiny is null returns an empty list.
	 * 
	 * @param form
	 * @param origin
	 * @param destiny
	 * @return
	 */
	public static Set<List<BaseQuestion>> getAllPathsFromOriginToDestiny(
			Form form, BaseQuestion origin, BaseQuestion destiny) {
		if (origin == null || destiny == null) {
			return new HashSet<List<BaseQuestion>>();
		}

		ComputedFlowView computedFlowView = form.getComputedFlowsView();

		HashMap<BaseQuestion, Set<List<BaseQuestion>>> exploredQuestions = new HashMap<>();

		// Get reverse list of elements from origin to destiny.
		List<TreeObject> selectedObjects = form.getAll(BaseQuestion.class);
		List<BaseQuestion> selectedElements = new ArrayList<BaseQuestion>();
		for (int i = selectedObjects.indexOf(destiny); i >= selectedObjects
				.indexOf(origin); i--) {
			selectedElements.add((BaseQuestion) selectedObjects.get(i));
		}

		for (BaseQuestion element : selectedElements) {
			// Initialize all the elements to explore.
			exploredQuestions.put(element, new HashSet<List<BaseQuestion>>());
		}

		for (int i=0;i<selectedElements.size()-1;i++) {
			
			BaseQuestion element = selectedElements.get(i);
			// Get all flow to current element.
			Set<Flow> flows = computedFlowView.getFlowsByDestiny(element);
			for (Flow flow : flows) {
				if (exploredQuestions.containsKey(flow.getOrigin())) {
					// Origin is in the questions to explore. Then create all
					// the paths
					if (exploredQuestions.get(element).isEmpty()) {
						// If current element doesn't have paths then create a
						// new path with it and insert on origin of flow.
						List<BaseQuestion> newPath = new ArrayList<BaseQuestion>();
						newPath.add(element);
						exploredQuestions.get(flow.getOrigin()).add(newPath);
					} else {
						// Insert the multiple paths in the origin of flow with
						// current element added.
						for (List<BaseQuestion> path : exploredQuestions
								.get(element)) {
							List<BaseQuestion> newPath = new ArrayList<BaseQuestion>(
									path);
							newPath.add(element);
							exploredQuestions.get(flow.getOrigin())
									.add(newPath);
						}
					}
				}
			}
		}

		return exploredQuestions.get(selectedElements.get(selectedElements
				.size() - 1));
	}

	public static boolean allPathsFromOriginToDestinyPassThrough(Form form,
			BaseQuestion origin, BaseQuestion destiny,
			BaseQuestion requiredQuestion) {

		// Check that a path exists origin -> required -> destiny
		if (!findPath(form, origin, requiredQuestion)
				|| !findPath(form, requiredQuestion, destiny)) {
			return false;
		}

		Set<List<BaseQuestion>> paths = getAllPathsFromOriginToDestiny(form,
				origin, destiny);

		for (List<BaseQuestion> path : paths) {
			if (!path.contains(requiredQuestion)) {
				return false;
			}
		}

		return true;
	}

	public static boolean findPath(Form form, BaseQuestion origin,
			BaseQuestion destiny) {

		ComputedFlowView computedFlowView = form.getComputedFlowsView();

		Set<BaseQuestion> exploredQuestions = new HashSet<>();
		Stack<BaseQuestion> questionsToExplore = new Stack<>();
		questionsToExplore.add(origin);

		while (!questionsToExplore.isEmpty()) {
			BaseQuestion questionToExplore = questionsToExplore.pop();
			if (exploredQuestions.contains(questionToExplore)) {
				// Question already explored.
				continue;
			}

			if (questionsToExplore != null && questionToExplore.equals(destiny)) {
				// Path found. Return true.
				return true;
			}
			if (questionsToExplore != null
					&& questionToExplore.compareTo(destiny) > 0) {
				// This question is after destiny question. No Path can exist.
				// Mark as explored and ignore
				exploredQuestions.add(questionToExplore);
				continue;
			}

			Set<Flow> flows = computedFlowView
					.getFlowsByOrigin(questionToExplore);
			for (Flow flow : flows) {
				if (flow.getFlowType() == FlowType.END_FORM && destiny == null) {
					return true;
				}
				if (flow.getFlowType() == FlowType.END_FORM
						|| flow.getFlowType() == FlowType.END_LOOP) {
					continue;
				} else {
					if (!exploredQuestions.contains(flow.getDestiny())) {
						questionsToExplore.push((BaseQuestion) flow
								.getDestiny());
					}
				}
			}
		}

		return false;
	}

}
