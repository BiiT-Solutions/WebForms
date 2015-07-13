package com.biit.webforms.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.Token;

/**
 * This class is a helper class with static methods that walk through the flow
 * graph and retrieve the desired information for each case.
 *
 */
public class FormWalker {

	/**
	 * Returns a set with all the paths from a origin node to a destiny node.
	 * 
	 * If origin or destiny is null returns an empty list.
	 * 
	 * @param form
	 * @param origin
	 * @param destiny
	 * @return
	 */
	public static Set<List<BaseQuestion>> getAllPathsFromOriginToDestiny(Form form, BaseQuestion origin, BaseQuestion destiny) {
		if (origin == null || destiny == null) {
			return new HashSet<List<BaseQuestion>>();
		}

		ComputedFlowView computedFlowView = form.getComputedFlowsView();

		HashMap<BaseQuestion, Set<List<BaseQuestion>>> exploredQuestions = new HashMap<>();

		// Get reverse list of elements from origin to destiny.
		List<TreeObject> selectedObjects = form.getAll(BaseQuestion.class);
		List<BaseQuestion> selectedElements = new ArrayList<BaseQuestion>();
		for (int i = selectedObjects.indexOf(destiny); i >= selectedObjects.indexOf(origin); i--) {
			selectedElements.add((BaseQuestion) selectedObjects.get(i));
		}

		for (BaseQuestion element : selectedElements) {
			// Initialize all the elements to explore.
			exploredQuestions.put(element, new HashSet<List<BaseQuestion>>());
		}

		for (int i = 0; i < selectedElements.size() - 1; i++) {
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
						for (List<BaseQuestion> path : exploredQuestions.get(element)) {
							List<BaseQuestion> newPath = new ArrayList<BaseQuestion>(path);
							newPath.add(element);
							exploredQuestions.get(flow.getOrigin()).add(newPath);
						}
					}
				}
			}
		}

		return exploredQuestions.get(selectedElements.get(selectedElements.size() - 1));
	}

	/**
	 * This function returns true if all paths form origin from destiny pass
	 * through a determined question by @requiredQuestion
	 * 
	 * This function requires to get all the paths between two nodes which makes
	 * this function expensive.
	 * 
	 * @param form
	 * @param origin
	 * @param destiny
	 * @param requiredQuestion
	 * @return
	 */
	public static boolean allPathsFromOriginToDestinyPassThrough(Form form, BaseQuestion origin, BaseQuestion destiny,
			BaseQuestion requiredQuestion) {

		// Check that a path exists origin -> required -> destiny
		if (!findPath(form, origin, requiredQuestion) || !findPath(form, requiredQuestion, destiny)) {
			return false;
		}

		if (anyPathFromOriginDoesntPassThrough(form, origin, destiny, requiredQuestion)) {
			return false;
		}

		return true;
	}

	/**
	 * This function returns true if any path doesn't pass through a determined
	 * question by @questionToAvoid
	 * 
	 * This function uses a lazy approach. When a path passes through the
	 * question to avoid returns as false directly.
	 * 
	 * @param form
	 * @param origin
	 * @param destiny
	 * @param questionToAvoid
	 * @return
	 */
	public static boolean anyPathFromOriginDoesntPassThrough(Form form, BaseQuestion origin, BaseQuestion destiny,
			BaseQuestion questionToAvoid) {
		ComputedFlowView computedFlowView = form.getComputedFlowsView();
		if (origin == null) {
			origin = (BaseQuestion) computedFlowView.getFirstElement();
		}
		if (questionToAvoid.equals(destiny)) {
			return false;
		}

		Set<BaseQuestion> exploredQuestions = new HashSet<>();
		Stack<BaseQuestion> questionsToExplore = new Stack<>();
		questionsToExplore.add(origin);

		while (!questionsToExplore.isEmpty()) {
			BaseQuestion questionToExplore = questionsToExplore.pop();
			if (exploredQuestions.contains(questionToExplore)) {
				// Question already explored.
				continue;
			} else {
				exploredQuestions.add(questionToExplore);
			}

			if (questionToExplore != null && questionToExplore.equals(destiny)) {
				// Path found. Return true.
				return true;
			}
			if (questionToExplore != null && (questionToExplore.compareTo(destiny) > 0 || questionToExplore.equals(questionToAvoid))) {
				// This question is after destiny question. No Path can exist.
				// Or
				// This question is the question to avoid.
				// Ignore and check other paths.
				continue;
			}

			Set<Flow> flows = computedFlowView.getFlowsByOrigin(questionToExplore);
			for (Flow flow : flows) {
				if (flow.getFlowType() == FlowType.END_FORM && destiny == null) {
					return true;
				}
				if (flow.getFlowType() == FlowType.END_FORM || flow.getFlowType() == FlowType.END_LOOP) {
					continue;
				} else {
					if (!exploredQuestions.contains(flow.getDestiny())) {
						questionsToExplore.push((BaseQuestion) flow.getDestiny());
					}
				}
			}
		}
		return false;
	}

	/**
	 * This function tries to find a path between two elements. This function
	 * uses a lazy approach. When a node reached outside the bounds of the
	 * beginning-end is discarded and at the first occurrence of getting to the end
	 * question the function is terminated with true.
	 * 
	 * @param form
	 * @param origin
	 * @param destiny
	 * @return
	 */
	public static boolean findPath(Form form, BaseQuestion origin, BaseQuestion destiny) {

		ComputedFlowView computedFlowView = form.getComputedFlowsView();
		if (origin == null) {
			origin = (BaseQuestion) computedFlowView.getFirstElement();
		}

		Set<BaseQuestion> exploredQuestions = new HashSet<>();
		Stack<BaseQuestion> questionsToExplore = new Stack<>();
		questionsToExplore.add(origin);

		while (!questionsToExplore.isEmpty()) {
			BaseQuestion questionToExplore = questionsToExplore.pop();
			if (exploredQuestions.contains(questionToExplore)) {
				// Question already explored.
				continue;
			} else {
				exploredQuestions.add(questionToExplore);
			}

			if (questionsToExplore != null && questionToExplore.equals(destiny)) {
				// Path found. Return true.
				return true;
			}
			if (questionsToExplore != null && questionToExplore.compareTo(destiny) > 0) {
				// This question is after destiny question. No Path can exist.
				// Ignore
				continue;
			}

			Set<Flow> flows = computedFlowView.getFlowsByOrigin(questionToExplore);
			for (Flow flow : flows) {
				if (flow.getFlowType() == FlowType.END_FORM && destiny == null) {
					return true;
				}
				if (flow.getFlowType() == FlowType.END_FORM || flow.getFlowType() == FlowType.END_LOOP) {
					continue;
				} else {
					if (!exploredQuestions.contains(flow.getDestiny())) {
						questionsToExplore.push((BaseQuestion) flow.getDestiny());
					}
				}
			}
		}

		return false;
	}

	/**
	 * This function returs true if a path can be found form end to the beginning of the form
	 * @param form
	 * @param end
	 * @param token
	 * @return
	 */
	public static boolean existsPathWithoutThisToken(Form form, BaseQuestion end, Token token) {
		// This algorithm walks the graph from end to beginning. If it reaches a
		// flow with the same token ignores the path. If the algorithm reaches
		// the beginning
		// of the form then a path without this token exists.

		ComputedFlowView computedFlowView = form.getComputedFlowsView();
		BaseQuestion origin = (BaseQuestion) computedFlowView.getFirstElement();

		// Initialice with end question.
		Set<BaseQuestion> exploredQuestions = new HashSet<>();
		Stack<BaseQuestion> questionsToExplore = new Stack<>();
		questionsToExplore.add(end);

		while (!questionsToExplore.isEmpty()) {
			BaseQuestion questionToExplore = questionsToExplore.pop();
			// This question has already been explored.
			if (exploredQuestions.contains(questionToExplore)) {
				continue;
			} else {
				// Mark as explored
				exploredQuestions.add(questionToExplore);
			}

			// Question to explore is the origin of the form. Then a path
			// without token exists.
			if (questionToExplore.equals(origin)) {
				return true;
			}

			for (Flow flow : computedFlowView.getFlowsByDestiny(questionToExplore)) {
				if (flow.isOthers() || flow.getComputedCondition().isEmpty() || flow.getComputedCondition().size() > 1) {
					questionsToExplore.push(flow.getOrigin());
				} else {
					if (!flow.getComputedCondition().get(0).isContentEqual(token)) {
						questionsToExplore.push(flow.getOrigin());
					}
					// Is the same token ignore and don't put in questions to
					// explore stack.
				}
			}
		}
		return false;
	}

}
