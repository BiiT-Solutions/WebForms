package com.biit.webforms.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import com.biit.form.BaseForm;
import com.biit.form.BaseGroup;
import com.biit.form.BaseQuestion;
import com.biit.form.IBaseFormView;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.biit.webforms.persistence.entity.exceptions.ReferenceNotPertainsToForm;
import com.biit.webforms.serialization.AnswerDeserializer;
import com.biit.webforms.serialization.AnswerSerializer;
import com.biit.webforms.serialization.BaseRepeatableGroupDeserializer;
import com.biit.webforms.serialization.BaseRepeatableGroupSerializer;
import com.biit.webforms.serialization.FormDeserializer;
import com.biit.webforms.serialization.FormSerializer;
import com.biit.webforms.serialization.QuestionDeserializer;
import com.biit.webforms.serialization.QuestionSerializer;
import com.biit.webforms.serialization.StorableObjectDeserializer;
import com.biit.webforms.serialization.SystemFieldDeserializer;
import com.biit.webforms.serialization.SystemFieldSerializer;
import com.biit.webforms.serialization.TextDeserializer;
import com.biit.webforms.serialization.TextSerializer;
import com.biit.webforms.serialization.TokenBetweenSerializer;
import com.biit.webforms.serialization.TokenComparationAnswerSerializer;
import com.biit.webforms.serialization.TokenComparationValueSerializer;
import com.biit.webforms.serialization.TokenInSerializer;
import com.biit.webforms.serialization.TokenInValueSerializer;
import com.biit.webforms.serialization.TokenSerializer;
import com.biit.webforms.serialization.TreeObjectDeserializer;
import com.biit.webforms.serialization.TreeObjectSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.liferay.portal.model.User;

@Entity
@Table(name = "tree_forms", uniqueConstraints = { @UniqueConstraint(columnNames = { "label", "version",
		"organizationId" }) })
@AttributeOverride(name = "label", column = @Column(length = StorableObject.MAX_UNIQUE_COLUMN_LENGTH, columnDefinition = "varchar("
		+ StorableObject.MAX_UNIQUE_COLUMN_LENGTH + ")"))
@Polymorphism(type = PolymorphismType.EXPLICIT)
public class Form extends BaseForm implements IWebformsFormView {
	private static final long serialVersionUID = 5220239269341014315L;

	public static final int MAX_DESCRIPTION_LENGTH = 30000;

	@Enumerated(EnumType.STRING)
	private FormWorkStatus status;

	@Lob
	private String description;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "form")
	// @JoinTable(joinColumns = @JoinColumn(name = "form_ID"),
	// inverseJoinColumns = @JoinColumn(name = "rule_ID"), indexes = {
	// @Index(columnList = "form_ID") })
	// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Flow> rules;

	private String linkedFormLabel;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "linked_form_versions", joinColumns = @JoinColumn(name = "formId"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"formId", "linkedFormVersions" }))
	private Set<Integer> linkedFormVersions;

	private Long linkedFormOrganizationId;

	@Transient
	private boolean isLastVersion;

	public Form() {
		super();
		status = FormWorkStatus.DESIGN;
		description = new String();
		rules = new HashSet<>();
		linkedFormVersions = new HashSet<>();
	}

	public Form(String label, User user, Long organizationId) throws FieldTooLongException,
			CharacterNotAllowedException {
		super(label);
		status = FormWorkStatus.DESIGN;
		description = new String();
		rules = new HashSet<>();
		linkedFormVersions = new HashSet<>();
		setCreatedBy(user);
		setUpdatedBy(user);
		setOrganizationId(organizationId);
	}

	@Override
	public void resetIds() {
		// Overridden version to also reset ids of rules.
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

			linkedFormLabel = ((Form) object).getLinkedFormLabel();
			setLinkedFormVersions(((Form) object).getLinkedFormVersions());
			linkedFormOrganizationId = ((Form) object).getLinkedFormOrganizationId();

		} else {
			throw new NotValidTreeObjectException("Copy data for Form only supports the same type copy");
		}
	}

	public Form createNewVersion(User user) throws NotValidStorableObjectException, CharacterNotAllowedException {
		return createNewVersion(user.getUserId());
	}

	public Form createNewVersion(Long userId) throws NotValidStorableObjectException, CharacterNotAllowedException {
		Form newVersion = (Form) generateCopy(false, true);
		newVersion.setVersion(newVersion.getVersion() + 1);
		newVersion.setLastVersion(true);
		this.setLastVersion(false);
		newVersion.resetIds();
		newVersion.setCreatedBy(userId);
		newVersion.setUpdatedBy(userId);
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

	public Set<Flow> getFlowsFrom(BaseQuestion from) {
		Set<Flow> flows = new HashSet<Flow>();
		for (Flow flow : getFlows()) {
			if (from != null && from.equals(flow.getOrigin())) {
				flows.add(flow);
			}
		}
		return flows;
	}

	public Set<Flow> getFlowsTo(BaseGroup to) {
		Set<Flow> flows = new HashSet<Flow>();
		if (to != null) {
			for (TreeObject children : to.getAllChildrenInHierarchy(BaseQuestion.class)) {
				flows.addAll(getFlowsTo((BaseQuestion) children));
			}
		}
		return flows;
	}

	public Set<Flow> getFlowsTo(BaseQuestion to) {
		Set<Flow> flows = new HashSet<Flow>();
		for (Flow flow : getFlows()) {
			if (to != null && flow.getDestiny() != null && to.equals(flow.getDestiny())) {
				flows.add(flow);
			}
		}
		return flows;
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
	 * This method creates a ComputeRuleView with all the current rules and the implicit rules (question without rule
	 * goes to the next element)
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

	public boolean removeRule(Flow dbRule) {
		int currentRules = rules.size();
		rules.remove(dbRule);
		return currentRules == rules.size();
	}

	/**
	 * This is the only function that has to be used to link forms. This controls that the linked element and versions
	 * are present at the same time or not when modifying data. It is assumed that all linked forms have the same name
	 * and organization.
	 * 
	 * @param linkedForms
	 */
	public void setLinkedForms(Set<IBaseFormView> linkedForms) {
		if (linkedForms == null || linkedForms.isEmpty()) {
			setLinkedFormLabel(null);
			setLinkedFormVersions(null);
			setLinkedFormOrganizationId(null);
		} else {
			Set<Integer> versionNumbers = new HashSet<Integer>();
			for (IBaseFormView linkedForm : linkedForms) {
				setLinkedFormLabel(linkedForm.getLabel());
				versionNumbers.add(linkedForm.getVersion());
				setLinkedFormOrganizationId(linkedForm.getOrganizationId());
			}
			setLinkedFormVersions(versionNumbers);
		}
	}

	public String getLinkedFormLabel() {
		return linkedFormLabel;
	}

	public Set<Integer> getLinkedFormVersions() {
		return linkedFormVersions;
	}

	public void addLinkedFormVersion(Integer versionNumber) {
		getLinkedFormVersions().add(versionNumber);
	}

	public Long getLinkedFormOrganizationId() {
		return linkedFormOrganizationId;
	}

	protected void setLinkedFormLabel(String linkedFormLabel) {
		this.linkedFormLabel = linkedFormLabel;
	}

	protected void setLinkedFormVersions(Set<Integer> linkedFormVersions) {
		this.linkedFormVersions.clear();
		if (linkedFormVersions != null) {
			this.linkedFormVersions.addAll(linkedFormVersions);
		}
	}

	protected void setLinkedFormOrganizationId(Long linkedFormOrganizationId) {
		this.linkedFormOrganizationId = linkedFormOrganizationId;
	}

	@Override
	public boolean isLastVersion() {
		return isLastVersion;
	}

	public void setLastVersion(boolean isLastVersion) {
		this.isLastVersion = isLastVersion;
	}

	public String exportToJavaCode(StringBuilder sb) {
		Integer counter = 0;

		sb.append("Form form = new Form();").append(System.lineSeparator());
		sb.append("form.setLabel(\"").append(this.getLabel()).append("\");").append(System.lineSeparator());
		sb.append("form.setDescription(\"").append(this.getDescription()).append("\");").append(System.lineSeparator());

		for (TreeObject child : getChildren()) {
			int tempCounter = counter + 1;
			counter = ((Category) child).exportToJavaCode(sb, counter + 1);
			sb.append("//form").append(System.lineSeparator());
			sb.append("form.addChild(").append("el_" + tempCounter).append(");").append(System.lineSeparator());
		}

		return sb.toString();
	}

	public Set<Flow> getFlows(String originPath, String destinyPath) {
		return getFlows(getChild(originPath), getChild(destinyPath));
	}

	public Set<Flow> getFlows(TreeObject origin, TreeObject destiny) {
		Set<Flow> selectedFlows = new HashSet<Flow>();
		for (Flow flow : rules) {
			if (flow.getOrigin().equals(origin)
					&& ((flow.getDestiny() != null && flow.getDestiny().equals(destiny)) || (flow.getDestiny() == null && destiny == null))) {
				selectedFlows.add(flow);
			}
		}
		return selectedFlows;
	}

	public static Form fromJson(String jsonString) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TreeObject.class, new StorableObjectDeserializer<TreeObject>());
		gsonBuilder.registerTypeAdapter(Form.class, new FormDeserializer());
		gsonBuilder.registerTypeAdapter(Category.class, new TreeObjectDeserializer<Category>(Category.class));
		gsonBuilder.registerTypeAdapter(Group.class, new BaseRepeatableGroupDeserializer<Group>(Group.class));
		gsonBuilder.registerTypeAdapter(Question.class, new QuestionDeserializer());
		gsonBuilder.registerTypeAdapter(Text.class, new TextDeserializer());
		gsonBuilder.registerTypeAdapter(SystemField.class, new SystemFieldDeserializer());
		gsonBuilder.registerTypeAdapter(Answer.class, new AnswerDeserializer());
		Gson gson = gsonBuilder.create();

		return (Form) gson.fromJson(jsonString, Form.class);
	}

	public String toJson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(Form.class, new FormSerializer());
		gsonBuilder.registerTypeAdapter(Category.class, new TreeObjectSerializer<Category>());
		gsonBuilder.registerTypeAdapter(Group.class, new BaseRepeatableGroupSerializer<Group>());
		gsonBuilder.registerTypeAdapter(Question.class, new QuestionSerializer());
		gsonBuilder.registerTypeAdapter(Text.class, new TextSerializer());
		gsonBuilder.registerTypeAdapter(SystemField.class, new SystemFieldSerializer());
		gsonBuilder.registerTypeAdapter(Answer.class, new AnswerSerializer());
		gsonBuilder.registerTypeAdapter(Flow.class, new FlowSerializer());
		gsonBuilder.registerTypeAdapter(Token.class, new TokenSerializer<Token>());
		gsonBuilder.registerTypeAdapter(TokenBetween.class, new TokenBetweenSerializer());
		gsonBuilder.registerTypeAdapter(TokenComparationAnswer.class, new TokenComparationAnswerSerializer());
		gsonBuilder.registerTypeAdapter(TokenComparationValue.class, new TokenComparationValueSerializer());
		gsonBuilder.registerTypeAdapter(TokenIn.class, new TokenInSerializer());
		gsonBuilder.registerTypeAdapter(TokenInValue.class, new TokenInValueSerializer());
		Gson gson = gsonBuilder.create();

		return gson.toJson(this);
	}

	public String getLabelWithouthSpaces() {
		return getLabel().replace(" ", "_");
	}

	/**
	 * Compares the content of treeObject - Needs to be an instance of Form
	 * 
	 * @param treeObject
	 * @return
	 */
	public boolean isContentEqual(TreeObject treeObject) {
		if (treeObject instanceof Form) {
			if (super.isContentEqual(treeObject)) {
				Form form = (Form) treeObject;

				if (status != null && status != form.status) {
					return false;
				}
				if (description != null && !description.equals(form.description)) {
					return false;
				}

				if ((rules == null && form.rules != null) || (rules != null && form.rules == null)) {
					return false;
				}

				// They have the same number of rules
				if (rules != null && form.rules != null && rules.size() != form.rules.size()) {
					return false;
				}
				if (rules != null) {
					for (Flow rule : rules) {
						String originPath = rule.getOrigin().getPathName();
						String destinyPath = null;
						if (rule.getDestiny() != null) {
							destinyPath = rule.getDestiny().getPathName();
						}
						Set<Flow> formRules = form.getFlows(originPath, destinyPath);

						boolean foundEqualFlow = false;
						for (Flow formRule : formRules) {
							if (rule.isContentEqual(formRule)) {
								foundEqualFlow = true;
								break;
							}
						}
						if (!foundEqualFlow) {
							return false;
						}
					}
				}

				if (linkedFormLabel != null && !linkedFormLabel.equals(form.linkedFormLabel)) {
					return false;
				}

				if (linkedFormVersions != null && !linkedFormVersions.equals(form.linkedFormVersions)) {
					return false;
				}

				if (linkedFormOrganizationId != null && !linkedFormVersions.equals(form.linkedFormOrganizationId)) {
					return false;
				}

				return true;
			}
		}
		return false;
	}
}
