package com.biit.webforms.utils.exporters.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.condition.parser.expressions.WebformsExpression;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.utils.exporters.xml.exceptions.ElementWithoutNextElement;
import com.biit.webforms.utils.exporters.xml.exceptions.TooMuchIterationsWhileGeneratingPath;
import com.biit.webforms.utils.math.domain.IDomain;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.range.PostalCode;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.xml.XmlUtils;

public class XmlExporter {

	private static Random random = new Random();
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
		for (Flow flow : computedFlows.getFlows()) {
			counters.put(flow, new Integer(0));
			compiledDomains.put(flow, getDomain(flow));
		}
	}

	public List<String> generate(int number) throws ElementWithoutNextElement, TooMuchIterationsWhileGeneratingPath {
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
			return null;
		}

		List<String> xmlFiles = new ArrayList<String>();
		
		BaseQuestion startNode = (BaseQuestion) questions.iterator().next();

		for (int i = 0; i < number; i++) {
			List<Flow> path = generatePath(startNode, questions.size());

			HashMap<Question, String> valuesOfPath = createValueResult(path);
			xmlFiles.add(generateXml(path, valuesOfPath));

			markFlows(path);
		}
		return xmlFiles;
	}

	private String generateXml(List<Flow> path, HashMap<Question, String> valuesOfPath) {
		TreeObject currentElement = null;

		StringBuilder sb = new StringBuilder();
		
		String xmlBaseAddress = WebformsConfigurationReader.getInstance().getXmlBaseAddress();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<"
				+ form.getLabelWithouthSpaces()
				+ " xmlns=\""+xmlBaseAddress+""
				+ form.getLabelWithouthSpaces()
				+ "\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\""+xmlBaseAddress+ " schema.xsd"
				+ form.getLabelWithouthSpaces() + "\">");
		for (Flow flow : path) {
			if (flow.getOrigin() instanceof Text || flow.getOrigin() instanceof SystemField) {
				continue;
			}

			generateGroupChange(sb, currentElement, flow.getOrigin());
			currentElement = flow.getOrigin();
			generateQuestion(sb, (Question) flow.getOrigin(), valuesOfPath);
		}
		closeGroups(sb, currentElement);
		sb.append("</" + form.getLabelWithouthSpaces() + ">");

		return XmlUtils.format(sb.toString());
	}

	private void closeGroups(StringBuilder sb, TreeObject currentElement) {
		List<TreeObject> currentElementParents = currentElement.getAllParentElements();
		for (int i = currentElementParents.size() - 1; i > 0; i--) {
			sb.append("</" + currentElementParents.get(i).getName() + ">");
		}
	}

	private void generateQuestion(StringBuilder sb, Question question, HashMap<Question, String> valuesOfPath) {
		sb.append("<" + question.getName() + ">");
		if (valuesOfPath.containsKey(question)) {
			sb.append(valuesOfPath.get(question));
		} else {
			switch (question.getAnswerType()) {
			case INPUT:
				generateRandomInput(sb, question);
				break;
			case MULTIPLE_SELECTION:
			case SINGLE_SELECTION_LIST:
			case SINGLE_SELECTION_RADIO:
				generateRandomQuestion(sb, question);
				break;
			case TEXT_AREA:
				generateRandomText(sb);
				break;
			}
		}
		sb.append("</" + question.getName() + ">");
	}

	private void generateRandomInput(StringBuilder sb, Question question) {
		switch (question.getAnswerSubformat()) {
		case AMOUNT:
			sb.append(random.nextInt(Integer.MAX_VALUE));
			break;
		case BSN:
			sb.append("123456782");
			break;
		case DATE:
		case DATE_BIRTHDAY:
		case DATE_FUTURE:
		case DATE_PAST:
			sb.append(new Date(random.nextLong()));
			break;
		case EMAIL:
			sb.append("example@mail.com");
			break;
		case FLOAT:
			sb.append(Math.abs(random.nextFloat()));
			break;
		case IBAN:
			sb.append("ES7620770024003102575766");
			break;
		case NUMBER:
			sb.append(random.nextInt(Integer.MAX_VALUE));
			break;
		case PHONE:
			sb.append("999999999");
			break;
		case POSTAL_CODE:
			sb.append(PostalCode.random());
			break;
		case TEXT:
			sb.append("Lorem ipsum dolor sit amet");
			break;
		default:
			break;
		}
	}

	private void generateRandomQuestion(StringBuilder sb, Question question) {
		LinkedHashSet<Answer> finalAnswers = question.getFinalAnswers();
		List<Answer> answers = new ArrayList<>();
		answers.addAll(finalAnswers);

		if (!finalAnswers.isEmpty()) {
			sb.append(answers.get(random.nextInt(answers.size())).getName());
		}
	}

	private void generateRandomText(StringBuilder sb) {
		sb.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris facilisis pretium ex, non tempor ipsum lobortis a. Ut pellentesque orci accumsan velit ultrices convallis. ");
	}

	private void generateGroupChange(StringBuilder sb, TreeObject currentElement, TreeObject newElement) {
		List<TreeObject> newElementParents = newElement.getAllParentElements();
		if (currentElement != null) {
			List<TreeObject> currentElementParents = currentElement.getAllParentElements();

			for (int i = currentElementParents.size() - 1; i > 0; i--) {
				if (!newElementParents.contains(currentElementParents.get(i))) {
					sb.append("</" + currentElementParents.get(i).getName() + ">");
				}
			}
			for (int i = 1; i < newElementParents.size(); i++) {
				if (!currentElementParents.contains(newElementParents.get(i))) {
					sb.append("<" + newElementParents.get(i).getName() + ">");
				}
			}

		} else {
			for (int i = 1; i < newElementParents.size(); i++) {
				sb.append("<" + newElementParents.get(i).getName() + ">");
			}
		}
	}

	private HashMap<Question, String> createValueResult(List<Flow> path) {
		HashMap<Question, String> randomValues = new HashMap<Question, String>();
		for (Flow flow : path) {
			if (compiledDomains.get(flow) == null) {
				// Skip empty domains
				continue;
			}
			if(compiledDomains.get(flow).isEmpty()){
				//Skip
				continue;
			}
			randomValues.putAll(compiledDomains.get(flow).generateRandomValue());
		}
		return randomValues;
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
		for (Flow flow : path) {
			markFlow(flow, 1);
		}
	}

	private List<Flow> generatePath(BaseQuestion startNode, int numberOfQuestion) throws ElementWithoutNextElement,
			TooMuchIterationsWhileGeneratingPath {

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

			if (nextFlow.getFlowType() == FlowType.NORMAL) {
				currentNode = (BaseQuestion) nextFlow.getDestiny();
			} else {
				currentNode = null;
			}
		}

		return path;
	}

	private Flow getLeastUsedNextFlowNode(Set<Flow> nextFlows) {
		Flow leastUsedFlow = null;
		Integer numberOfUsesOfLeastUsedFlow = null;

		for (Flow flow : nextFlows) {
			if (flow.getFlowType() == FlowType.END_LOOP) {
				continue;
			}

			int numberOfUses = counters.get(flow);
			if (numberOfUsesOfLeastUsedFlow == null || numberOfUses < numberOfUsesOfLeastUsedFlow) {
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

	public void markFlow(Flow flow, int quantity) {
		counters.put(flow, counters.get(flow) + quantity);
	}
}
