package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.biit.form.BaseForm;
import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.persistence.entity.exceptions.ReferenceNotPertainsToForm;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;

@Entity
@Table(name = "tree_forms", uniqueConstraints = { @UniqueConstraint(columnNames = { "label", "version",
		"organizationId" }) })
@AttributeOverride(name = "label", column = @Column(length = StorableObject.MAX_UNIQUE_COLUMN_LENGTH))
@Cache(region = "forms", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Form extends BaseForm {
	public static final int MAX_DESCRIPTION_LENGTH = 30000;

	@Enumerated(EnumType.STRING)
	private FormWorkStatus status;

	@Column(length = MAX_DESCRIPTION_LENGTH)
	@Lob
	private String description;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "form")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Flow> rules;

	public Form() {
		super();
		status = FormWorkStatus.DESIGN;
		description = new String();
		rules = new HashSet<>();
	}

	public Form(String label, User user, Organization organization) throws FieldTooLongException,
			CharacterNotAllowedException {
		super(label);
		status = FormWorkStatus.DESIGN;
		description = new String();
		rules = new HashSet<>();
		setCreatedBy(user);
		setUpdatedBy(user);
		setOrganizationId(organization);
	}

	@Override
	public void resetIds() {
		// Overriden version to also reset ids of rules.
		super.resetIds();
		for (Flow rule : getFlows()) {
			rule.resetIds();
		}
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		super.copyData(object);
		if (object instanceof Form) {
			description = new String(((Form) object).getDescription());
			status = ((Form) object).getStatus();
		} else {
			throw new NotValidTreeObjectException("Copy data for Form only supports the same type copy");
		}
	}

	public Form createNewVersion(User user) throws NotValidStorableObjectException, CharacterNotAllowedException {
		Form newVersion = (Form) generateCopy(false, true);
		newVersion.setVersion(newVersion.getVersion() + 1);
		newVersion.resetIds();
		newVersion.setCreatedBy(user);
		newVersion.setUpdatedBy(user);
		newVersion.setStatus(FormWorkStatus.DESIGN);
		return newVersion;
	}

	public void setDescription(String description) throws FieldTooLongException {
		if (description == null) {
			description = "";
		}
		if (description.length() > MAX_DESCRIPTION_LENGTH) {
			throw new FieldTooLongException("Description is longer than maximum: " + MAX_DESCRIPTION_LENGTH);
		}
		this.description = new String(description);
	}

	public String getDescription() {
		if (description == null) {
			return new String();
		} else {
			return description;
		}
	}

	public void setOrganizationId(Organization organization) {
		setOrganizationId(organization.getOrganizationId());
	}

	public FormWorkStatus getStatus() {
		return status;
	}

	public void setStatus(FormWorkStatus status) {
		this.status = status;
	}

	public void addFlow(Flow rule) {
		this.rules.add(rule);
		rule.setForm(this);
	}

	public boolean containsFlow(Flow rule) {
		return rules.contains(rule);
	}

	public Set<Flow> getFlows() {
		return rules;
	}

	public void setRules(Set<Flow> rules) {
		this.rules.clear();
		addFlows(rules);
	}

	public void addFlows(Set<Flow> rules) {
		this.rules.addAll(rules);
		for (Flow rule : rules) {
			rule.setForm(this);
		}
	}

	/**
	 * This method creates a ComputeRuleView with all the current rules and the
	 * implicit rules (question without rule goes to the next element)
	 * 
	 * @return
	 */
	public ComputedFlowView getComputedFlowsView() {
		LinkedHashSet<TreeObject> allBaseQuestions = getAllChildrenInHierarchy(BaseQuestion.class);
		ComputedFlowView computedView = new ComputedFlowView();

		if (!allBaseQuestions.isEmpty()) {
			Object[] baseQuestions = allBaseQuestions.toArray();
			computedView.setFirstElement((TreeObject) baseQuestions[0]);
			computedView.addFlows(rules);

			int numQuestions = baseQuestions.length - 1;

			for (int i = 0; i < numQuestions; i++) {
				if (computedView.getFlowsByOrigin((TreeObject) baseQuestions[i]) == null) {
					computedView
							.addNewNextElementFlow((TreeObject) baseQuestions[i], (TreeObject) baseQuestions[i + 1]);
				}
			}
			if (computedView.getFlowsByOrigin((TreeObject) baseQuestions[numQuestions]) == null) {
				computedView.addNewEndFormFlow((TreeObject) baseQuestions[numQuestions]);
			}
		}
		return computedView;
	}

	public String getReference(TreeObject element) throws ReferenceNotPertainsToForm {
		List<TreeObject> parentList = new ArrayList<>();
		TreeObject parent;
		while ((parent = element.getParent()) != null) {
			parentList.add(parent);
		}
		if (!parentList.get(parentList.size() - 1).equals(this)) {
			throw new ReferenceNotPertainsToForm("TreeObject: '" + element + "' doesn't belong to '" + this + "'");
		}

		String reference = "<" + element.getName() + ">";
		for (TreeObject listedParent : parentList) {
			reference = "<" + listedParent.getName() + ">" + reference;
		}
		return "${" + reference + "}";
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.addAll(super.getAllInnerStorableObjects());
		for (Flow rule : rules) {
			innerStorableObjects.add(rule);
			innerStorableObjects.addAll(rule.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	/**
	 * Overriden version of generate Copy to generate a copy of the flow rules.
	 */
	@Override
	public TreeObject generateCopy(boolean copyParentHierarchy, boolean copyChilds)
			throws NotValidStorableObjectException, CharacterNotAllowedException {
		Form copy = (Form) super.generateCopy(copyParentHierarchy, copyChilds);

		if (copyChilds) {
			copy.copyRules(this, false);
			copy.updateRuleReferences();
		}

		return copy;
	}

	/**
	 * Generate copy used to remove all rule elements that are not in the view.
	 * 
	 * @param seed
	 * @return
	 * @throws NotValidTreeObjectException
	 * @throws CharacterNotAllowedException
	 */
	public Form generateFormCopiedSimplification(TreeObject seed) throws NotValidStorableObjectException,
			CharacterNotAllowedException {
		TreeObject copiedSeed = seed.generateCopy(true, true);
		Form formSeed = (Form) copiedSeed.getAncestor(Form.class);

		formSeed.copyRules(this, true);
		formSeed.updateRuleReferences();

		return formSeed;
	}

	/**
	 * Copy to this element the rules in form
	 * 
	 * @param form
	 */
	private void copyRules(Form form, boolean discard) {
		LinkedHashSet<TreeObject> currentElements = getAllChildrenInHierarchy(TreeObject.class);
		HashMap<String, TreeObject> mappedElements = new HashMap<>();
		for (TreeObject currentElement : currentElements) {
			mappedElements.put(currentElement.getComparationId(), currentElement);
		}

		for (Flow rule : form.getFlows()) {
			Flow copiedRule = rule.generateCopy();
			// If rule origin is not in the list of current elements or it has
			// destiny and is not in the list.
			if (discard) {
				if (!mappedElements.containsKey(copiedRule.getOrigin().getComparationId())
						|| (copiedRule.getDestiny() != null && !mappedElements.containsKey(copiedRule.getDestiny()
								.getComparationId()))) {
					continue;
				}
			}
			addFlow(copiedRule);
		}
	}

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

	public void removeRule(Flow dbRule) {
		rules.remove(dbRule);
	}
}
