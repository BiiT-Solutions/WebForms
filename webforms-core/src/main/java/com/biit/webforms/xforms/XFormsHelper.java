package com.biit.webforms.xforms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;

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
	private Set<Flow> defaultFlows;
	private HashMap<TreeObject, Set<Flow>> flowsByOrigin;
	private HashMap<TreeObject, Set<Flow>> flowsByDestiny;
	// Control names must be unique.
	private HashMap<TreeObject, String> controlNames;
	private Set<String> usedControlNames;
	private HashMap<BaseQuestion, WebserviceCallOutputLink> webserviceOuput;
	private HashMap<BaseQuestion, Set<WebserviceCallInputLink>> webserviceInputs;
	private Set<String> usedCallNames;
	
	private HashMap<WebserviceCall, String> callNames;

	protected XFormsHelper(Form form) {
		questions = new ArrayList<>();
		xFormsQuestions = new ArrayList<>();
		xformsFromTreeObject = new HashMap<>();
		visibilityOfQuestions = new HashMap<>();
		visibilityOfQuestionsAsTokens = new HashMap<>();
		usedControlNames = new HashSet<String>();
		controlNames = new HashMap<>();
		flows = new HashSet<Flow>();
		defaultFlows = new HashSet<Flow>();
		flowsByOrigin = new HashMap<>();
		flowsByDestiny = new HashMap<>();
		webserviceOuput = new HashMap<>();
		webserviceInputs = new HashMap<>();
		callNames = new HashMap<>();
		usedCallNames = new HashSet<>();

		// Set questions in order.
		LinkedHashSet<TreeObject> allBaseQuestions = form.getAllNotHiddenChildrenInHierarchy(BaseQuestion.class);
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
				defaultFlows.add(flow);
			}
		}
		
		for(WebserviceCall call: form.getWebserviceCalls()){
			for(WebserviceCallOutputLink link: call.getOutputLinks()){
				webserviceOuput.put(link.getFormElement(), link);
			}
			for(WebserviceCallInputLink link: call.getInputLinks()){
				if(webserviceInputs.get(link.getFormElement())==null){
					webserviceInputs.put(link.getFormElement(), new HashSet<WebserviceCallInputLink>());
				}
				webserviceInputs.get(link.getFormElement()).add(link);
			}
		}
	}
	
	public WebserviceCallOutputLink getWebserviceCallOutputLink(BaseQuestion question){
		return webserviceOuput.get(question);
	}
	
	public Set<WebserviceCallInputLink> getWebserviceCallInputLinks(BaseQuestion question) {
		return webserviceInputs.get(question);
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
	 * Return the tokens needed to represent the previous visibility of a node. The previous visibility is the answers
	 * of a questions needed to make this element visible. It is analyzed by flow and not by question to allow
	 * recursivity.
	 * 
	 * @param flow
	 *            the flow that goes to the element to analyze.
	 * @return
	 */
	public List<Token> getPreviousVisibilityTokens(Flow flow) {
		List<Token> tokens = new ArrayList<>();
		if (flow != null) {
			Set<Flow> flowsFromOrigin = flowsByOrigin.get(flow.getOrigin());
			// This element is a fork, has more than one flow.
			if (flowsFromOrigin != null && flowsFromOrigin.size() > 1) {
				tokens.addAll(flow.getConditionSimpleTokens());
				if (flow.getOrigin() instanceof Question) {
					// Input field must filled up!
					// if (((Question) flow.getOrigin()).getAnswerType().equals(AnswerType.INPUT)) {
					if (!tokens.isEmpty()) {
						tokens.add(Token.getAndToken());
					}
					tokens.add(new TokenAnswerNeeded(flow.getOrigin(),
							((Question) flow.getOrigin()).getAnswerFormat() != null
									&& ((Question) flow.getOrigin()).getAnswerFormat().equals(AnswerFormat.DATE)));
					// }
				}
				return tokens;
			}

			Set<Flow> flowsToElement = flowsByDestiny.get(flow.getOrigin());
			if (flowsToElement != null) {
				for (Flow flowToOrigin : flowsToElement) {
					if (!tokens.isEmpty()) {
						tokens.add(Token.getOrToken());
					}
					tokens.addAll(getPreviousVisibilityTokens(flowToOrigin));
				}
			}
		}
		return tokens;
	}

	/**
	 * Return all the elements which answers has impact to a question visibility.
	 * 
	 * @param flow
	 * @return
	 */
	public Set<TreeObject> getSourceOfRelevance(Flow flow) {
		Set<TreeObject> sources = new HashSet<>();
		// Has a condition, the flow does not inherit relevance rule.
		if (!flow.getCondition().isEmpty()) {
			sources.add(flow.getOrigin());
			return sources;
		}

		Set<Flow> flowsFromOrigin = flowsByOrigin.get(flow.getOrigin());
		// Has more than one outgoing flow, the flow does not inherit relevance rule.
		if (flowsFromOrigin.size() > 1) {
			sources.add(flow.getOrigin());
			return sources;
		}

		Set<Flow> flowsToOrigin = flowsByDestiny.get(flow.getOrigin());
		for (Flow flowTo : flowsToOrigin) {
			sources.addAll(getSourceOfRelevance(flowTo));
		}

		return sources;
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

	public void addXFormsObject(XFormsObject<?> xFormsObject) {
		xformsFromTreeObject.put(xFormsObject.getSource(), xFormsObject);
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
	
	public String getUniqueName(WebserviceCall call){
		if (callNames.get(call) == null) {
			if (!usedCallNames.contains(call.getName())) {
				callNames.put(call, call.getName());
				usedCallNames.add(call.getName());
			} else {
				int i = 2;
				while (usedCallNames.contains(call.getName() + "-" + i)) {
					i++;
				}
				callNames.put(call, call.getName() + "-" + i);
				usedCallNames.add(call.getName() + "-" + i);
			}
		}
		return callNames.get(call);
	}

	public Set<Flow> getDefaultFlows() {
		return defaultFlows;
	}

}
