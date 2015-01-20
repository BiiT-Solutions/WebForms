package com.biit.webforms.gui.common.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.CollapseListener;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.Tree.ExpandListener;

/**
 * TreeObjectTable component
 * 
 * This is a customized component to represent a TreeObject in a tree table.
 * 
 */
public class TableTreeObject extends TreeTable {
	private static final long serialVersionUID = -6949123334668973540L;
	private IconProvider<TreeObject> iconProvider = new IconProviderTreeObjectDefault();

	protected enum TreeObjectTableProperties {
		ELEMENT_NAME
	};

	public TableTreeObject() {
		initContainerProperties();
		setImmediate(true);
		
		// Quick and dirty fix to jumping around table bug
		addExpandListener(new ExpandListener() {
			private static final long serialVersionUID = -6212747624453604935L;

			@Override
			public void nodeExpand(ExpandEvent event) {
				setValue(event.getItemId());
			}
		});
		addCollapseListener(new CollapseListener() {
			private static final long serialVersionUID = 8688330729095127848L;

			@Override
			public void nodeCollapse(CollapseEvent event) {
				setValue(event.getItemId());
			}
		});
		// End of dirty little nasty fix.
	}

	protected void initContainerProperties() {
		addContainerProperty(TreeObjectTableProperties.ELEMENT_NAME, Component.class, null,
				CommonComponentsLanguageCodes.FORM_TREE_PROPERTY_NAME.translation(), null, Align.LEFT);
		setCellStyleGenerator(new TreeObjectTableCellStyleGenerator());
	}

	/**
	 * Loads a tree object structure recursively. At the end of the process
	 * selects the root element inserted. element. It can also be specified an
	 * array of filterClasses. If this is not specified, then every kind of
	 * element is allowed. Else only the elements in the hierarchy whose path
	 * is made of valid elements.
	 * 
	 * @param element
	 * @param parent
	 */
	public void loadTreeObject(TreeObject element, TreeObject parent, Class<?> ... filterClases) {
		loadTreeObject(element, parent, true, filterClases);
	}

	public void loadTreeObject(TreeObject element, TreeObject parent, boolean select, Class<?> ... filterClases) {
		if (element != null) {
			if (isAdmitedInFilter(element, filterClases)) {
				addRow(element, parent, select);

				List<TreeObject> children = element.getChildren();
				for (TreeObject child : children) {
					loadTreeObject(child, element, false, filterClases);
				}
			}
		}
	}

	private boolean isAdmitedInFilter(TreeObject element, Class<?> ... filterClases) {
		if (filterClases == null || filterClases.length == 0) {
			// No filter, then everything is admited.
			return true;
		}
		for (Class<?> filterClass : filterClases) {
			if (filterClass.isInstance(element)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected void setValuesToItem(Item item, TreeObject element) {
		Object treeObjectIcon = createElementWithIcon(element);
		item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(treeObjectIcon);
	}

	/**
	 * Adds a new row to the table. Default configuration makes that adding a
	 * new row also selects.
	 * 
	 * @param element
	 * @param parent
	 * @return
	 */
	public Item addRow(TreeObject element, TreeObject parent) {
		return addRow(element, parent, true);
	}

	public Item addRow(TreeObject element, TreeObject parent, boolean selectRow) {
		if (element != null) {
			Item item = addItem(element);
			if (parent != null) {
				// This status must be true before setting the relationship.
				setChildrenAllowed(parent, true);
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			setValuesToItem(item, element);
			if (selectRow) {
				setValue(element);
			}
			setChildrenAllowed(element, false);
			return item;
		}
		return null;
	}

	public void updateRow(TreeObject element) {
		Item item = getItem(element);
		if (item != null) {
			// Remove children of table if they are no longer related.
			Collection<?> immutableList = getChildren(element);
			if (immutableList != null && (!immutableList.isEmpty())) {
				List<Object> children = new ArrayList<Object>();
				children.addAll(immutableList);
				for (Object child : children) {
					if (!element.getChildren().contains(child)) {
						removeRow((TreeObject) child);
					}
				}
			}

			if (element.getChildren().isEmpty()) {
				setChildrenAllowed(element, false);
			}

			// Update
			setValuesToItem(item, element);
		}
	}

	public void removeRow(TreeObject element) {
		if(getChildren(getParent(element)).size()==1){
			setChildrenAllowed(getParent(element), false);
		}
		
		for (TreeObject child : element.getChildren()) {
			removeRow(child);
		}
		removeItem(element);		
	}

	/**
	 * Adds item to table. This function is a specialization of
	 * {@link TreeTable#addItemAfter(Object, Object)} for form members.
	 * 
	 * @param element
	 */
	public void addRowAfter(Object previousItemId, TreeObject element, TreeObject parent) {
		if (element != null) {
			Item item = addItemAfter(previousItemId, (Object) element);
			if (parent != null) {
				setChildrenAllowed(parent, true);
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			setValuesToItem(item, element);
			setValue(element);
			setChildrenAllowed(element, false);
		}
	}

	/**
	 * Gets Name property to show form a TreeObject element. If the name can't
	 * be defined, then raises a {@link UnsupportedOperationException}
	 * 
	 * @param element
	 * @return
	 */
	public static String getItemName(TreeObject element) {
		String name = element.getName();
		if (name == null) {
			throw new UnsupportedOperationException(TreeObject.class.getName() + " subtype unknown.");
		}
		return name;
	}

	public void setRootElement(TreeObject root) {
		this.removeAllItems();
		select(null);
		if (root != null) {
			loadTreeObject(root, null);
			try {
				setCollapsed(root, false);
			} catch (Exception e) {
				// Root is not inserted. Ignore error.
			}
		}
	}

	public TreeObject getTreeObjectSelected() {
		Object value = super.getValue();
		if (value instanceof TreeObject) {
			return (TreeObject) value;
		}
		return null;
	}

	public void setTreeObjectsSelected(Set<TreeObject> treeObjects) {
		super.setValue(treeObjects);
	}

	public void setTreeObjectSelected(TreeObject treeObject) {
		super.setValue(treeObject);
	}

	@SuppressWarnings("unchecked")
	public Set<TreeObject> getTreeObjectsSelected() {
		Object value = super.getValue();
		if (value instanceof Set<?>) {
			Set<Object> setObject = (Set<Object>) value;
			Set<TreeObject> setTreeObject = new HashSet<TreeObject>();
			for (Object obj : setObject) {
				setTreeObject.add((TreeObject) obj);
			}
			return setTreeObject;
		}
		return null;
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return Collections.EMPTY_LIST;
	}

	public boolean isElementFiltered(Object itemId) {
		return false;
	}

	protected Object createElementWithIcon(final TreeObject element) {
		ComponentCellTreeObject cell = new ComponentCellTreeObject(getIconProvider());
		cell.update(element);
		cell.registerTouchCallBack(this, element);

		return cell;
	}

	/**
	 * Collapse the tree in a specific hierarchy level to inner levels. The
	 * level is specified by a class.
	 * 
	 * @param collapseFrom
	 */
	public void collapseFrom(Class<?> collapseFrom) {
		for (Object item : getItemIds()) {
			if (item.getClass() == collapseFrom) {
				this.setCollapsed(item, true);
			} else {
				if (this.getParent(item) != null && this.isCollapsed(this.getParent(item))) {
					this.setCollapsed(item, true);
				}
			}
		}
	}

	public TreeObject getSelectedRow() {
		return (TreeObject) getValue();
	}

	public void selectPreviousRow() {
		TreeObject currentRow = getSelectedRow();
		if (currentRow instanceof BaseForm) {
			// Do nothing, this is the form and the first row.
			return;
		}
		int index = currentRow.getParent().getChildren().indexOf(currentRow);
		if (index == 0) {
			setValue(currentRow.getParent());
		} else {
			try {
				setValue(currentRow.getParent().getChild(index - 1));
			} catch (ChildrenNotFoundException e) {
				// Impossible
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	public void redrawRow(TreeObject row) {
		if (row != null) {
			updateRow(row);
			for (TreeObject childRow : row.getChildren()) {
				removeRow(childRow);
				loadTreeObject(childRow, row);
			}
		}
	}

	public IconProvider<TreeObject> getIconProvider() {
		return iconProvider;
	}

	public void setIconProvider(IconProvider<TreeObject> iconProvider) {
		this.iconProvider = iconProvider;
	}

}
