package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.logger.WebformsLogger;

@Entity
@Table(name = "tree_blocks_references")
@Cacheable(true)
public class BlockReference extends TreeObject implements IWebformsBlockView {
	private static final long serialVersionUID = -4300039254232003868L;

	public static final String DEFAULT_TECHNICAL_NAME = "block_reference";
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDS = new ArrayList<Class<? extends TreeObject>>();

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Block reference;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tree_blocks_references_hidden_elements")
	private Set<TreeObject> elementsToHide;

	public BlockReference() {
		super();
		elementsToHide = new HashSet<>();
	}

	public BlockReference(Block reference) {
		super();
		this.reference = reference;
		elementsToHide = new HashSet<>();
	}

	public Block getReference() {
		return reference;
	}

	public void setReference(Block reference) {
		this.reference = reference;
	}

	@Override
	public void setStatus(FormWorkStatus status) {
		if (reference != null) {
			reference.setStatus(status);
		}
	}

	@Override
	public FormWorkStatus getStatus() {
		if (reference != null) {
			return reference.getStatus();
		}
		return null;
	}

	@Override
	public Set<Integer> getLinkedFormVersions() {
		return null;
	}

	@Override
	public Long getLinkedFormOrganizationId() {
		return null;
	}

	@Override
	public String getLinkedFormLabel() {
		return null;
	}

	@Override
	public Long getOrganizationId() {
		if (reference != null) {
			return reference.getOrganizationId();
		}
		return null;
	}

	@Override
	public Integer getVersion() {
		if (reference != null) {
			return reference.getVersion();
		}
		return null;
	}

	@Override
	public boolean isLastVersion() {
		if (reference != null) {
			return reference.isLastVersion();
		}
		return true;
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		if (reference != null) {
			reference.checkDependencies();
		}
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof BlockReference) {
			// Nothing to copy except basic information data.
			copyBasicInfo(object);
			setReference(((BlockReference) object).getReference());
		} else {
			throw new NotValidTreeObjectException("Copy data for a Block Reference only supports the same type copy");
		}
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_TECHNICAL_NAME;
	}

	@Override
	public String toString() {
		if (reference != null) {
			return "[" + reference.getLabel() + "]";
		}
		return "[empty]";
	}

	@Override
	public String getLabel() {
		if (reference != null) {
			return reference.getLabel();
		}
		return null;
	}

	@Override
	public String getName() {
		// Returns the name of the first category of the block
		try {
			return reference.getChild(0).getName();
		} catch (ChildrenNotFoundException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	@Override
	public List<TreeObject> getChildren() {
		if (reference == null) {
			return new ArrayList<TreeObject>();
		}
		return reference.getChildren();
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		return new HashSet<>();
	}

	public Set<TreeObject> getElementsToHide() {
		return elementsToHide;
	}

	/**
	 * Mark an element as hidden.
	 * 
	 * @param element
	 * @return true if the element has change its state to hidden.
	 * @throws ElementCannotBeRemovedException
	 */
	public boolean hideElement(TreeObject element) throws ElementCannotBeRemovedException {
		if (!reference.getAllInnerStorableObjects().contains(element)) {
			throw new ElementCannotBeRemovedException("Element '" + element
					+ "' does not exists in the Building block.");
		}
		// If parent is hidden, do not hide this element.
		boolean toHide = true;
		for (TreeObject ancestor : element.getAncestors()) {
			if (elementsToHide.contains(ancestor)) {
				toHide = false;
				break;
			}
		}
		if (toHide) {
			elementsToHide.add(element);
		}
		return toHide;
	}

	/**
	 * Shows a hidden element.
	 * 
	 * @param element
	 * @return true if the element has change its state to not hidden.
	 */
	public boolean showElement(TreeObject element) {
		if (elementsToHide.contains(element)) {
			elementsToHide.remove(element);
			return true;
		}
		return false;
	}

	/**
	 * For some cases, i.e. using Springcache we need to initialize all sets (disabling the Lazy loading).
	 * 
	 * @param elements
	 */
	@Override
	public void initializeSets() {
		super.initializeSets();
		reference.initializeSets();
	}
}
