package com.biit.webforms.computed;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ComputedFlowView {

	private TreeObject firstElement;
	private final Set<Flow> flows;
	private final HashMap<TreeObject, Set<Flow>> flowsByOrigin;
	private final HashMap<TreeObject, Set<Flow>> flowsByDestiny;

	public ComputedFlowView() {
		flows = new HashSet<>();
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
			flowsByOrigin.put(flow.getOrigin(), new HashSet<>());
		}
		flowsByOrigin.get(flow.getOrigin()).add(flow);
		if (flow.getDestiny() != null) {
			if (!flowsByDestiny.containsKey(flow.getDestiny())) {
				flowsByDestiny.put(flow.getDestiny(), new HashSet<>());
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
	 * Creates a new computed go to next element Flow. Type normal, condition = '' then true
	 *
	 */
	public void addNewNextElementFlow(BaseQuestion origin, BaseQuestion destiny) {
		Flow flow = new Flow();
		try {
			flow.setContent(origin, FlowType.NORMAL, destiny, false, new ArrayList<>());
			flow.setGenerated(true);
			flow.setReadOnly(origin.isReadOnly() && destiny.isReadOnly());
			addFlow(flow);
		} catch (BadFlowContentException | FlowWithoutSourceException | FlowSameOriginAndDestinyException
				| FlowDestinyIsBeforeOriginException | FlowWithoutDestinyException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public void addNewEndFormFlow(BaseQuestion origin) {
		Flow flow = new Flow();
		try {
			flow.setContent(origin, FlowType.END_FORM, null, false, new ArrayList<>());
			flow.setGenerated(true);
			addFlow(flow);
		} catch (BadFlowContentException | FlowWithoutSourceException | FlowSameOriginAndDestinyException
				| FlowDestinyIsBeforeOriginException | FlowWithoutDestinyException e) {
			// Impossible
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
