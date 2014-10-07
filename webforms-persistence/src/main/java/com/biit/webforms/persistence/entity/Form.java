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

import com.biit.form.BaseAnswer;
import com.biit.form.BaseForm;
import com.biit.form.BaseQuestion;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.computed.ComputedRuleView;
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

	private Long organizationId;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "form")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Rule> rules;

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
		for (Rule rule : getRules()) {
			rule.resetIds();
		}
	}

	@Override
	protected void copyData(TreeObject object) throws NotValidTreeObjectException {
		super.copyData(object);
		if (object instanceof Form) {
			description = new String(((Form) object).getDescription());
			organizationId = new Long(((Form) object).getOrganizationId());
			status = ((Form) object).getStatus();
		} else {
			throw new NotValidTreeObjectException("Copy data for Form only supports the same type copy");
		}
	}

	public Form createNewVersion(User user) throws NotValidTreeObjectException, CharacterNotAllowedException {
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
		return description;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Organization organization) {
		this.organizationId = organization.getOrganizationId();
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public FormWorkStatus getStatus() {
		return status;
	}

	public void setStatus(FormWorkStatus status) {
		this.status = status;
	}

	public void addRule(Rule rule) {
		this.rules.add(rule);
		rule.setForm(this);
	}

	public boolean containsRule(Rule rule) {
		return rules.contains(rule);
	}

	public Set<Rule> getRules() {
		return rules;
	}

	public void setRules(Set<Rule> rules) {
		this.rules.clear();
		addRules(rules);
	}

	public void addRules(Set<Rule> rules) {
		this.rules.addAll(rules);
		for (Rule rule : rules) {
			rule.setForm(this);
		}
	}

	/**
	 * This method creates a ComputeRuleView with all the current rules and the
	 * implicit rules (question without rule goes to the next element)
	 * 
	 * @return
	 */
	public ComputedRuleView getComputedRuleView() {
		LinkedHashSet<TreeObject> allBaseQuestions = getAllChildrenInHierarchy(BaseQuestion.class);
		ComputedRuleView computedView = new ComputedRuleView();

		if (!allBaseQuestions.isEmpty()) {
			Object[] baseQuestions = allBaseQuestions.toArray();
			computedView.setFirstElement((TreeObject) baseQuestions[0]);
			computedView.addRules(rules);

			int numQuestions = baseQuestions.length - 1;

			for (int i = 0; i < numQuestions; i++) {
				if (computedView.getRulesByOrigin((TreeObject) baseQuestions[i]) == null) {
					computedView
							.addNewNextElementRule((TreeObject) baseQuestions[i], (TreeObject) baseQuestions[i + 1]);
				}
			}
			if (computedView.getRulesByOrigin((TreeObject) baseQuestions[numQuestions]) == null) {
				computedView.addNewEndFormRule((TreeObject) baseQuestions[numQuestions]);
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
		for (Rule rule : rules) {
			innerStorableObjects.addAll(rule.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	/**
	 * Overriden version of generate Copy to generate a copy of the flow rules.
	 */
	@Override
	public TreeObject generateCopy(boolean copyParentHierarchy, boolean copyChilds) throws NotValidTreeObjectException,
			CharacterNotAllowedException {
		Form copy = (Form) super.generateCopy(copyParentHierarchy, copyChilds);

		if (copyChilds) {
			// Now we get all the questions
			LinkedHashSet<TreeObject> copiedQuestions = copy.getAllChildrenInHierarchy(BaseQuestion.class);
			HashMap<TreeObject, TreeObject> mappedCopiedQuestions = new HashMap<>();
			for (TreeObject question : copiedQuestions) {
				mappedCopiedQuestions.put(question, question);
			}

			for (Rule rule : getRules()) {
				Rule copiedRule = rule.generateCopy();
				if (copiedRule.getOrigin() != null) {
					copiedRule.setOrigin(mappedCopiedQuestions.get(copiedRule.getOrigin()));
				}
				if (copiedRule.getDestiny() != null) {
					copiedRule.setDestiny(mappedCopiedQuestions.get(copiedRule.getDestiny()));
				}
				copy.addRule(copiedRule);
			}
		}

		return copy;
	}

	public Form generateFormCopiedSimplification(TreeObject seed) throws NotValidTreeObjectException,
			CharacterNotAllowedException {
		TreeObject copiedSeed = seed.generateCopy(true, true);
		Form formSeed = (Form) copiedSeed.getAncestor(Form.class);

		LinkedHashSet<TreeObject> copiedQuestions = formSeed.getAllChildrenInHierarchy(BaseQuestion.class);
		HashMap<Question, Question> mappedCopiedQuestions = new HashMap<>();
		for (TreeObject question : copiedQuestions) {
			mappedCopiedQuestions.put((Question) question, (Question) question);
		}
		LinkedHashSet<TreeObject> copiedAnswers = formSeed.getAllChildrenInHierarchy(BaseAnswer.class);
		HashMap<Answer, Answer> mappedCopiedAnswers = new HashMap<>();
		for (TreeObject answer : copiedAnswers) {
			mappedCopiedAnswers.put((Answer) answer, (Answer) answer);
		}

		for (Rule rule : getRules()) {
			// Discard all the rules that do not apply to the simplified view.
			if (mappedCopiedQuestions.containsKey(rule.getOrigin())
					&& (rule.getDestiny() == null || mappedCopiedQuestions.containsKey(rule.getDestiny()))) {

				Rule copiedRule = rule.generateCopy();
				copiedRule.updateReferences(mappedCopiedQuestions, mappedCopiedAnswers);
				formSeed.addRule(copiedRule);
			} else {
				continue;
			}
		}
		return formSeed;
	}

	public void removeRule(Rule dbRule) {
		rules.remove(dbRule);
	}
}
