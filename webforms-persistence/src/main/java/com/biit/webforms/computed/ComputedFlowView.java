package com.biit.webforms.computed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;

public class ComputedFlowView {

	private TreeObject firstElement;
	private Set<Flow> flows;
	private HashMap<TreeObject, Set<Flow>> flowsByOrigin;
	private HashMap<TreeObject, Set<Flow>> flowsByDestiny;

	public ComputedFlowView() {
		flows = new HashSet<Flow>();
		flowsByOrigin = new HashMap<>();
		flowsByDestiny = new HashMap<>();
	}

	public void addFlow(Flow flow) {
		flows.add(flow);
		updateMapsAddFlow(flow);
	}

	public void addFlows(Set<Flow> flows) {
		this.flows.addAll(flows);
		for (Flow flow : flows) {
			updateMapsAddFlow(flow);
		}
	}

	private void updateMapsAddFlow(Flow flow) {
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

	public Set<Flow> getFlows() {
		return flows;
	}

	public Set<Flow> getFlowsByOrigin(TreeObject origin) {
		return flowsByOrigin.get(origin);
	}

	public Set<Flow> getFlowsByDestiny(TreeObject destiny) {
		return flowsByDestiny.get(destiny);
	}

	public HashMap<TreeObject, Set<Flow>> getFlowsByOrigin() {
		return flowsByOrigin;
	}

	public HashMap<TreeObject, Set<Flow>> getFlowsByDestiny() {
		return flowsByDestiny;
	}

	/**
	 * Creates a new computed go to next element Flow. Type normal, condition =
	 * '' -> true
	 * 
	 * @param origin
	 * @param destiny
	 */
	public void addNewNextElementFlow(TreeObject origin, TreeObject destiny) {
		Flow flow = new Flow();
		try {
			flow.setContent(origin, FlowType.NORMAL, destiny, false, new ArrayList<Token>());
			flow.setGenerated(true);
			addFlow(flow);
		} catch (BadFlowContentException | FlowWithoutSource | FlowSameOriginAndDestinyException
				| FlowDestinyIsBeforeOrigin | FlowWithoutDestiny e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public void addNewEndFormFlow(TreeObject origin) {
		Flow flow = new Flow();
		try {
			flow.setContent(origin, FlowType.END_FORM, null, false, new ArrayList<Token>());
			flow.setGenerated(true);
			addFlow(flow);
		} catch (BadFlowContentException | FlowWithoutSource | FlowSameOriginAndDestinyException
				| FlowDestinyIsBeforeOrigin | FlowWithoutDestiny e) {
			// Imposible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public TreeObject getFirstElement() {
		return firstElement;
	}

	public void setFirstElement(TreeObject firstElement) {
		this.firstElement = firstElement;
	}
}
