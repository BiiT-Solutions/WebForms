package com.biit.webforms.utils.exporters.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.condition.parser.expressions.WebformsExpression;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.utils.exporters.xml.exceptions.ElementWithoutNextElement;
import com.biit.webforms.utils.exporters.xml.exceptions.TooMuchIterationsWhileGeneratingPath;
import com.biit.webforms.utils.math.domain.IDomain;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;

public class XmlExporter {

	private final Form form;
	private final ComputedFlowView computedFlows;
	private final HashMap<Flow, Integer> counters;
	private final HashMap<Flow, IDomain> compiledDomains;

	public XmlExporter(Form form) throws BadFormedExpressions {
		this.form = form;
		this.computedFlows = form.getComputedFlowsView();
		this.counters = new HashMap<>();
		this.compiledDomains = new HashMap<>();
		initializeCounters();
	}

	private void initializeCounters() throws BadFormedExpressions {
		for (Flow flow: computedFlows.getFlows()) {
			counters.put( flow, new Integer(0));
			compiledDomains.put(flow, getDomain(flow));
		}
	}

	public void generate(int number) throws ElementWithoutNextElement, TooMuchIterationsWhileGeneratingPath {
		// This is not a complete search of the form.
		// Complete search with the complexity of forms makes a large impact on
		// computer resources. We are talking about more than 2^64 paths on
		// several forms. Even little forms with 8 question can have more than
		// 64 possible paths.

		// This solution generates One random path in the flow but with a catch.
		// Each time a node is visited a counter for that node is increased.
		// Then at the next iteration to generate a new random path the next
		// node to visit is always the node with the least visits. This solution
		// prioritizes the appearance of each question.

		LinkedHashSet<TreeObject> questions = form.getAllChildrenInHierarchy(BaseQuestion.class);
		if (questions.isEmpty()) {
			return;
		}
		
		BaseQuestion startNode =(BaseQuestion) questions.iterator().next();

		for (int i = 0; i < number; i++) {
			List<Flow> path = generatePath(startNode, questions.size());
			System.out.println(path);
			
			createValueResult(path);
			
			markFlows(path);
		}
		
	}

	private void createValueResult(List<Flow> path) {
		// TODO Auto-generated method stub
		for(Flow flow: path){
			if(compiledDomains.get(flow)==null){
				//Skip empty domains
				continue;
			}
			System.out.println(flow);
			System.out.println(compiledDomains.get(flow));
			compiledDomains.get(flow).generateRandomValue();
		}
	}

	private IDomain getDomain(Flow flow) throws BadFormedExpressions {
		WebformsParser parser = new WebformsParser(flow.getConditionSimpleTokens().iterator());
		try {
			WebformsExpression expression = ((WebformsExpression) parser.parseExpression());
			
			if (expression != null) {
				return expression.getDomain();
			}
			return null;
		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
				| MissingParenthesisException | EmptyParenthesisException e) {
			throw new BadFormedExpressions(flow);
		}
	}

	private void markFlows(List<Flow> path) {
		for(Flow flow: path){
			markFlow(flow, 1);
		}
	}

	private List<Flow> generatePath(BaseQuestion startNode, int numberOfQuestion)
			throws ElementWithoutNextElement, TooMuchIterationsWhileGeneratingPath {

		List<Flow> path = new ArrayList<>();

		BaseQuestion currentNode = startNode;
		int numberOfIterations = 0;
		
		while (currentNode != null) {
			if (numberOfIterations > numberOfQuestion) {
				throw new TooMuchIterationsWhileGeneratingPath();
			}
			numberOfIterations++;
			Set<Flow> nextFlows = getFlows(currentNode);
			Flow nextFlow = getLeastUsedNextFlowNode(nextFlows);
			path.add(nextFlow);
			
			if(nextFlow.getFlowType()==FlowType.NORMAL){
				currentNode = (BaseQuestion) nextFlow.getDestiny();
			}else{
				currentNode = null;
			}
		}

		return path;
	}

	private Flow getLeastUsedNextFlowNode(Set<Flow> nextFlows) {
		Flow leastUsedFlow = null;
		Integer numberOfUsesOfLeastUsedFlow = null;

		for (Flow flow : nextFlows) {
			if(flow.getFlowType()==FlowType.END_LOOP){
				continue;
			}
			
			int numberOfUses = counters.get(flow);
			if ( numberOfUsesOfLeastUsedFlow==null || numberOfUses < numberOfUsesOfLeastUsedFlow) {
				numberOfUsesOfLeastUsedFlow = numberOfUses;
				leastUsedFlow = flow;
			}
		}
		return leastUsedFlow;
	}

	private Set<Flow> getFlows(BaseQuestion currentNode) throws ElementWithoutNextElement {
		Set<Flow> flows = computedFlows.getFlowsByOrigin(currentNode);
		return flows;
	}

	public void markFlow(Flow flow,int quantity) {
		counters.put(flow, counters.get(flow) + quantity);
	}
}
