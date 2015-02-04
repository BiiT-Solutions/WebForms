package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidParentException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;

/**
 * This class is a wrapper of a Form class that translates any block reference to a list of its elements.
 */
public class CompleteFormView extends Form implements IWebformsFormView {
	private static final long serialVersionUID = -426480388117580446L;
	private Form form;

	// Original block -> copied block
	private Map<Block, Block> copiedBlocks;

	public CompleteFormView() {
		copiedBlocks = new HashMap<>();
	}

	public CompleteFormView(Form form) {
		setForm(form);
	}

	@Override
	public List<TreeObject> getChildren() {
		List<TreeObject> children = new ArrayList<>();

		for (TreeObject child : form.getChildren()) {
			if (child instanceof BlockReference) {
				Block copiedBlock = getCopyOfBlock(((BlockReference) child).getReference());
				for (TreeObject linkedChild : copiedBlock.getChildren()) {
					children.add(linkedChild);
				}
			} else {
				children.add(child);
			}
		}
		return children;
	}

	private void createCopyOfBlocks() {
		copiedBlocks = new HashMap<>();
		for (TreeObject child : form.getChildren()) {
			if (child instanceof BlockReference) {
				getCopyOfBlock(((BlockReference) child).getReference());
			}
		}
	}

	/**
	 * Ensures to have only one copy of a block.
	 * 
	 * @param block
	 * @return
	 * @throws NotValidStorableObjectException
	 * @throws CharacterNotAllowedException
	 */
	public Block getCopyOfBlock(Block block) {
		if (copiedBlocks.get(block) == null) {
			try {
				Block copiedBlock = (Block) block.generateCopy(true, true);
				// Linked Building block is not editable by the user directly.
				copiedBlock.setReadOnly(true);
				copiedBlocks.put(block, copiedBlock);

				for (TreeObject linkedChild : copiedBlock.getChildren()) {
					try {
						// To the categories set as parent the form.
						linkedChild.setParent(this);
					} catch (NotValidParentException e) {
						WebformsLogger.errorMessage(this.getClass().getName(), e);
					}
				}

				// Flows are not editable.
				for (Flow flow : copiedBlock.getFlows()) {
					flow.setReadOnly(true);
				}
				updateRuleReferences();

			} catch (NotValidStorableObjectException | CharacterNotAllowedException e) {
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		}
		return copiedBlocks.get(block);
	}

	/**
	 * Change rule elements for current form view elements.
	 */
	private void updateRuleReferences() {
		LinkedHashSet<TreeObject> currentElements = getAllChildrenInHierarchy(TreeObject.class);
		HashMap<String, TreeObject> mappedElements = new HashMap<>();
		for (TreeObject currentElement : currentElements) {
			mappedElements.put(currentElement.getComparationId(), currentElement);
		}
		for (Flow rule : getFlows()) {
			rule.updateReferences(mappedElements);
		}
	}

	@Override
	public void setStatus(FormWorkStatus status) {
		if (form != null) {
			form.setStatus(status);
		}
	}

	@Override
	public FormWorkStatus getStatus() {
		if (form == null) {
			return null;
		}
		return form.getStatus();
	}

	@Override
	public Set<Integer> getLinkedFormVersions() {
		if (form == null) {
			return null;
		}
		return form.getLinkedFormVersions();
	}

	@Override
	public Long getLinkedFormOrganizationId() {
		if (form == null) {
			return null;
		}
		return form.getLinkedFormOrganizationId();
	}

	@Override
	public String getLinkedFormLabel() {
		if (form == null) {
			return null;
		}
		return form.getLinkedFormLabel();
	}

	@Override
	public boolean isLastVersion() {
		if (form == null) {
			return true;
		}
		return form.isLastVersion();
	}

	@Override
	public Set<Flow> getFlows() {
		Set<Flow> flows = new HashSet<>();
		if (form == null) {
			return flows;
		}

		for (TreeObject child : form.getChildren()) {
			// Add linked block children
			if (child instanceof BlockReference) {
				Block block = getCopyOfBlock(((BlockReference) child).getReference());
				for (Flow flow : block.getFlows()) {
					flows.add(flow);
				}
			}
		}

		flows.addAll(form.getFlows());
		return flows;
	}

	/**
	 * Not allowed rules are the rules that comes from linked block and end in another linked block.
	 * 
	 * @param rule
	 * @throws FlowNotAllowedException 
	 */
	@Override
	public void addFlow(Flow rule) throws FlowNotAllowedException {
		if (form != null) {
			BlockReference blockReferenceOfSource = getBlockReference(rule.getOrigin());
			BlockReference blockReferenceOfDestination = getBlockReference(rule.getDestiny());

			// Are in the same linked block.
			if (blockReferenceOfSource != null && blockReferenceOfDestination != null
					&& blockReferenceOfSource.equals(blockReferenceOfDestination)) {
				throw new FlowNotAllowedException("");
			}

			form.addFlow(rule);
		}
	}

	@Override
	public void addChild(TreeObject child) throws NotValidChildException, ElementIsReadOnly {
		if (form != null) {
			form.addChild(child);
		}
	}

	@Override
	public String getComparationId() {
		if (form != null) {
			return form.getComparationId();
		}
		return null;
	}

	@Override
	public Long getOrganizationId() {
		if (form != null) {
			return form.getOrganizationId();
		}
		return null;
	}

	/**
	 * Returns the BlockReference parent object for an element if exists.
	 * 
	 * @param element
	 * @return
	 */
	public BlockReference getBlockReference(TreeObject element) {
		if (element == null) {
			return null;
		}
		for (TreeObject child : form.getChildren()) {
			if (child instanceof BlockReference) {
				if (child.isDescendant(element)) {
					return (BlockReference) child;
				}
			}
		}
		return null;
	}

	public Form getForm() {
		return form;
	}

	@Override
	public String getLabel() {
		if (form != null) {
			return form.getLabel();
		}
		return getDefaultLabel();
	}

	@Override
	public Integer getVersion() {
		if (form != null) {
			return form.getVersion();
		}
		return null;
	}

	@Override
	public Long getId() {
		if (form != null) {
			return form.getId();
		}
		return null;
	}

	@Override
	public String getDescription() {
		if (form != null) {
			return form.getDescription();
		}
		return "";
	}

	@Override
	public boolean removeRule(Flow flow) {
		if (form != null && !flow.isReadOnly()) {
			return form.removeRule(flow);
		}
		return false;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		super.copyData(object);
		if (object instanceof CompleteFormView) {
			try {
				setForm((Form) ((CompleteFormView) object).getForm().generateCopy(true, true));
			} catch (CharacterNotAllowedException e) {
				// Impossible but log it.
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		} else {
			throw new NotValidTreeObjectException("Copy data for CompleteFormView only supports the same type copy");
		}
	}

	public void setForm(Form form) {
		this.form = form;
		createCopyOfBlocks();
	}

	public void removeTreeObject(TreeObject element) throws DependencyExistException, ChildrenNotFoundException,
			ElementIsReadOnly {
		// Check if it is inside a linked block.
		BlockReference blockReference = getBlockReference(element);
		if (blockReference == null) {
			// Standard remove for a normal element.
			element.remove();
		} else {
			blockReference.checkTreeDependencies();
			// if no exception, remove reference from form.
			// blockReference.getChildren() causes to remove also the elements of the block, this is undesired, then we
			// need to remove first the reference of the block before removing it.
			blockReference.setReference(null);
			form.getChildren().remove(blockReference);
			form.getElementsToDelete().add(blockReference);
		}
	}

}
