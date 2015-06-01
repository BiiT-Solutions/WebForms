package com.biit.webforms.gui.components;

import java.util.HashSet;
import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.common.components.TableTreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Item;

/**
 * Table tree object with a label property added.
 *
 */
public class TableTreeObjectLabel extends TableTreeObject {
	private static final long serialVersionUID = 2882643672843469056L;
	private static final int LABEL_MAX_LENGTH = 50;

	public enum TreeObjectTableDesignerProperties {
		ELEMENT_LABEL
	};

	@Override
	protected void initContainerProperties() {
		super.initContainerProperties();
		addContainerProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL, String.class, null,
				LanguageCodes.CAPTION_LABEL.translation(), null, Align.LEFT);
	}

	@Override
	protected void setValuesToItem(Item item, TreeObject element) {
		super.setValuesToItem(item, element);
		String label = element.getLabel();
		if (label == null) {
			label = new String();
		}
		setLabelToItem(item, label);
	}

	/**
	 * Sets label to item and cuts label to max length.
	 * 
	 * @param item
	 * @param label
	 */
	@SuppressWarnings("unchecked")
	private void setLabelToItem(Item item, String label) {
		if (label.length() > LABEL_MAX_LENGTH) {
			label = label.substring(0, LABEL_MAX_LENGTH - 1);
			label += "...";
		}
		item.getItemProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL).setValue(label);
	}

	/**
	 * Expands the tree until treeObject
	 * 
	 * @param whereToMove
	 */
	public void expand(TreeObject treeObject) {
		if(treeObject==null){
			throw new NullPointerException();
		}
		// Disable fix to avoid select wrong element caused by triggers of
		// expand/contract listeners.
		disableFixForJumpingTableWhenExpandOrCollapse();
		expandImplementation(treeObject);
		enableFixForJumpingTableWhenExpandOrCollapse();
	}

	private void expandImplementation(TreeObject treeObject) {
		if (treeObject.getParent() != null) {
			expandImplementation(treeObject.getParent());
			setCollapsed(treeObject.getParent(), false);
		}
	}

	public Set<Object> getCollapsedStatus(TreeObject treeObject) {
		Set<Object> collapsedItems = new HashSet<>();
		getCollapsedStatus(treeObject, collapsedItems);
		return collapsedItems;
	}

	private void getCollapsedStatus(TreeObject treeObject, Set<Object> collapsedItems) {
		if (isCollapsed(treeObject)) {
			collapsedItems.add(treeObject);
		}
		for (TreeObject child : treeObject.getChildren()) {
			getCollapsedStatus(child, collapsedItems);
		}
	}

	public void setCollapsedStatus(TreeObject treeObject, Set<Object> collapsedStatus) {
		disableFixForJumpingTableWhenExpandOrCollapse();
		setCollapsedStatusImplementation(treeObject, collapsedStatus);
		enableFixForJumpingTableWhenExpandOrCollapse();
	}
	
	public void setCollapsedStatusImplementation(TreeObject treeObject, Set<Object> collapsedStatus) {
		setCollapsed(treeObject, false);
		for (TreeObject child : treeObject.getChildren()) {
			setCollapsedStatusImplementation(child, collapsedStatus);
		}
		setCollapsed(treeObject, collapsedStatus.contains(treeObject));
	}
}
