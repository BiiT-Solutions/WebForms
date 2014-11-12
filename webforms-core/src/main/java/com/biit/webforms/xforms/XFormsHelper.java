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

/**
 * Stores some basic data and perform basic and generic methods.
 */
class XFormsHelper {
	private List<TreeObject> questions;
	// Stores already calculated visibility to avoid recalculation.
	private HashMap<TreeObject, String> visibilityOfQuestions;
	// Links a treeObject with its XForms object.
	private HashMap<TreeObject, XFormsObject<? extends TreeObject>> xformsFromTreeObject;
	private List<XFormsQuestion> xFormsQuestions;
	private Set<Flow> flows;
	private HashMap<TreeObject, Set<Flow>> flowsByOrigin;
	private HashMap<TreeObject, Set<Flow>> flowsByDestiny;

	protected XFormsHelper(Form form) {
		questions = new ArrayList<>();
		xFormsQuestions = new ArrayList<>();
		xformsFromTreeObject = new HashMap<>();
		visibilityOfQuestions = new HashMap<>();
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

	public TreeObject getPreviousQuestion(TreeObject treeObject) {
		int index = questions.indexOf(treeObject);
		if (index > 0) {
			return questions.get(index - 1);
		}
		return null;
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
		int index = xFormsQuestions.indexOf(xFormsQuestion);
		if (index > 0) {
			return xFormsQuestions.get(index - 1);
		}
		return null;
	}

	public String getVisibilityOfQuestion(TreeObject treeObject) {
		return visibilityOfQuestions.get(treeObject);
	}

	public void addVisibilityOfQuestion(TreeObject treeObject, String visibility) {
		visibilityOfQuestions.put(treeObject, visibility);
	}

	public XFormsObject<? extends TreeObject> getXFormsObject(TreeObject treeObject) {
		return xformsFromTreeObject.get(treeObject);
	}

}
