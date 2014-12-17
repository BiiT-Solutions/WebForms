package com.biit.webforms.utils.exporters.xml;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.exporters.xml.export.ElementWithoutNextElement;

public class XmlExporter {

	private final Form form;
	private final ComputedFlowView flows;
	private HashMap<BaseQuestion, Integer> counters;

	public XmlExporter(Form form) {
		this.form = form;
		this.flows = form.getComputedFlowsView();
		this.counters = new HashMap<>();
		initializeCounters();
	}

	private void initializeCounters() {
		LinkedHashSet<TreeObject> questions = form.getAllChildrenInHierarchy(BaseQuestion.class);
		for(TreeObject question: questions){
			counters.put((BaseQuestion) question, new Integer(0));
		}
	}

	public void generate(int number) throws ElementWithoutNextElement {
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
		if(questions.isEmpty()){
			return;
		}
		
		List<BaseQuestion> path = generatePath((BaseQuestion) questions.iterator().next(), questions.size());
		
		
	}

	private List<BaseQuestion> generatePath(BaseQuestion startNode,int numberOfQuestion) throws ElementWithoutNextElement {
		
		BaseQuestion currentNode = startNode;
		int numberOfIterations = 0;
		while(currentNode!=null){
			if(numberOfIterations<numberOfQuestion){
				//Throw exception?
				break;
			}
			numberOfIterations++;
			Set<Flow> flows = getFlows(currentNode);
		}
		
		return null;
	}
	
	private Set<Flow> getFlows(BaseQuestion currentNode) throws ElementWithoutNextElement {
		if(flows.getFlowsByOrigin(currentNode).isEmpty()){
			throw new ElementWithoutNextElement();
		}
		return flows.getFlowsByOrigin(currentNode);
	}

	public void visitQuestion(BaseQuestion question){
		counters.put(question,counters.get(question)+1);
	}

}
