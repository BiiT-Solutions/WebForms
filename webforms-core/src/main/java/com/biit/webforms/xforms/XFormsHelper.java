package com.biit.webforms.xforms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.Token;

/**
 * Stores some basic data and perform basic and generic methods.
 */
class XFormsHelper {
	private List<BaseQuestion> questions;
	// Stores already calculated visibility to avoid recalculation.
	private HashMap<TreeObject, String> visibilityOfQuestions;
	// Stores already calculated visibility to avoid recalculation.
	private HashMap<TreeObject, List<Token>> visibilityOfQuestionsAsTokens;

	// Links a treeObject with its XForms object.
	private HashMap<TreeObject, XFormsObject<? extends TreeObject>> xformsFromTreeObject;
	private List<XFormsQuestion> xFormsQuestions;
	private Set<Flow> flows;
	private HashMap<TreeObject, Set<Flow>> flowsByOrigin;
	private HashMap<TreeObject, Set<Flow>> flowsByDestiny;
	// Control names must be unique.
	private HashMap<TreeObject, String> controlNames;
	private Set<String> usedControlNames;

	protected XFormsHelper(Form form) {
		questions = new ArrayList<>();
		xFormsQuestions = new ArrayList<>();
		xformsFromTreeObject = new HashMap<>();
		visibilityOfQuestions = new HashMap<>();
		visibilityOfQuestionsAsTokens = new HashMap<>();
		usedControlNames = new HashSet<String>();
		controlNames = new HashMap<>();
		flows = new HashSet<Flow>();
		flowsByOrigin = new HashMap<>();
		flowsByDestiny = new HashMap<>();

		// Set questions in order.
		LinkedHashSet<TreeObject> allBaseQuestions = form.getAllChildrenInHierarchy(BaseQuestion.class);
		for (TreeObject element : allBaseQuestions) {
			questions.add((BaseQuestion) element);
		}

		// Set flows.
		for (Flow flow : form.getFlows()) {
			addFlow(flow);
		}

		// Set default flows.
		for (int i = 0; i < questions.size() - 1; i++) {
			// No flow defined, use default flow.
			if (flowsByOrigin.get(questions.get(i)) == null) {
				Flow flow = new Flow();
				flow.setOrigin(questions.get(i));
				flow.setDestiny(questions.get(i + 1));
				addFlow(flow);
			}
		}
	}

	private void addFlow(Flow flow) {
		flows.add(flow);
		if (!flowsByOrigin.containsKey(flow.getOrigin())) {
			flowsByOrigin.put(flow.getOrigin(), new HashSet<Flow>());
		}
		flowsByOrigin.get(flow.getOrigin()).add(flow);
		if (flow.getDestiny() != null) {
			if (!flowsByDestiny.containsKey(flow.getDestiny())) {
				flowsByDestiny.put(flow.getDestiny(), new HashSet<Flow>());
			}
			flowsByDestiny.get(flow.getDestiny()).add(flow);
		}
	}

	public Set<Flow> getFlowsWithOrigin(TreeObject origin) {
		Set<Flow> flows = flowsByOrigin.get(origin);
		if (flows != null) {
			return flows;
		}
		return new HashSet<Flow>();
	}

	public Set<Flow> getFlowsWithDestiny(TreeObject source) {
		Set<Flow> flows = flowsByDestiny.get(source);
		if (flows != null) {
			return flows;
		}
		return new HashSet<Flow>();
	}

	public boolean isFirstQuestion(TreeObject treeObject) {
		return questions.indexOf(treeObject) == 0;
	}

	public BaseQuestion getPreviousBaseQuestion(BaseQuestion treeObject) {
		if (treeObject != null) {
			int index = questions.indexOf(treeObject);
			if (index > 0) {
				return questions.get(index - 1);
			}
		}
		return null;
	}

	/**
	 * Return the last fork that is critical for the flow to achieve this element. If this element is a fork, returns
	 * it.
	 * 
	 * @param treeObject
	 * @return
	 */
	public Set<BaseQuestion> getPreviousForkTokens(BaseQuestion treeObject) {
		Set<BaseQuestion> tokens = new HashSet<>();
		if (treeObject != null) {
			Set<Flow> flowsFromElement = flowsByOrigin.get(treeObject);
			// This element is a fork
			if (flowsFromElement != null && flowsFromElement.size() > 1) {
				tokens.add(treeObject);
				return tokens;
			}

			Set<Flow> flowsToElement = flowsByDestiny.get(treeObject);
			if (flowsToElement != null) {
				for (Flow flow : flowsToElement) {
					if (flow.getCondition().isEmpty()) {
						tokens.addAll(getPreviousForkTokens((BaseQuestion) flowsToElement.iterator().next().getOrigin()));
					} else {
						tokens.add((BaseQuestion) flowsToElement.iterator().next().getOrigin());
						return tokens;
					}
				}
			}
		}
		return tokens;
	}

	public TreeObject getNextQuestion(TreeObject treeObject) {
		int index = questions.indexOf(treeObject);
		if (index < questions.size() - 1) {
			return questions.get(index + 1);
		}
		return null;
	}

	public List<XFormsQuestion> getXFormsQuestions() {
		return xFormsQuestions;
	}

	public void addXFormsQuestion(XFormsQuestion xFormsQuestion) {
		xFormsQuestions.add(xFormsQuestion);
		xformsFromTreeObject.put(xFormsQuestion.getSource(), xFormsQuestion);
	}

	public XFormsQuestion getPreviousElement(XFormsQuestion xFormsQuestion) {
		if (xFormsQuestion != null) {
			int index = xFormsQuestions.indexOf(xFormsQuestion);
			if (index > 0) {
				return xFormsQuestions.get(index - 1);
			}
		}
		return null;
	}

	public String getVisibilityOfElement(TreeObject treeObject) {
		return visibilityOfQuestions.get(treeObject);
	}

	public void addVisibilityOfElement(TreeObject treeObject, String visibility) {
		visibilityOfQuestions.put(treeObject, visibility);
	}

	public List<Token> getVisibilityOfQuestionAsToken(TreeObject treeObject) {
		return visibilityOfQuestionsAsTokens.get(treeObject);
	}

	public void addVisibilityOfQuestionAsToken(TreeObject treeObject, List<Token> visibility) {
		visibilityOfQuestionsAsTokens.put(treeObject, visibility);
	}

	public XFormsObject<? extends TreeObject> getXFormsObject(TreeObject treeObject) {
		return xformsFromTreeObject.get(treeObject);
	}

	/**
	 * Checks that the controlName is unique.
	 * 
	 * @param treeObject
	 * @return
	 */
	public String getUniqueName(TreeObject treeObject) {
		if (controlNames.get(treeObject) == null) {
			if (!usedControlNames.contains(treeObject.getName())) {
				controlNames.put(treeObject, treeObject.getName());
				usedControlNames.add(treeObject.getName());
			} else {
				int i = 2;
				while (usedControlNames.contains(treeObject.getName() + "-" + i)) {
					i++;
				}
				controlNames.put(treeObject, treeObject.getName() + "-" + i);
				usedControlNames.add(treeObject.getName() + "-" + i);
			}
		}
		return controlNames.get(treeObject);
	}

}
