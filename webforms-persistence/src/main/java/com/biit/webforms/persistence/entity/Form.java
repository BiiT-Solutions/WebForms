package com.biit.webforms.persistence.entity;

import com.biit.form.entity.*;
import com.biit.form.exceptions.*;
import com.biit.form.json.serialization.hibernate.HibernateProxyTypeAdapter;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.usermanager.entity.IUser;
import com.biit.webforms.computed.ComputedFlowView;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.condition.*;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.ReferenceNotPertainsToFormException;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.biit.webforms.serialization.*;
import com.biit.webforms.webservices.Webservice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "tree_forms", uniqueConstraints = {@UniqueConstraint(columnNames = {"label", "version", "organization_id"})})
@AttributeOverride(name = "label", column = @Column(length = StorableObject.MAX_UNIQUE_COLUMN_LENGTH, columnDefinition = "varchar("
        + StorableObject.MAX_UNIQUE_COLUMN_LENGTH + ")"))
@Cacheable()
public class Form extends BaseForm implements IWebformsFormView, ElementWithImage {
    private static final long serialVersionUID = 5220239269341014315L;
    private static final int MAX_JSON_LENGTH = 1000000;

    private static final List<Class<? extends TreeObject>> ALLOWED_CHILDREN = new ArrayList<>(Arrays.asList(BaseCategory.class,
            BlockReference.class));

    public static final int MAX_DESCRIPTION_LENGTH = 30000;

    @Enumerated(EnumType.STRING)
    private FormWorkStatus status;

    @Lob
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "form")
    private final Set<Flow> rules;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "form")
    private final Set<WebserviceCall> webserviceCalls;

    @Column(name = "linked_form_label")
    private String linkedFormLabel;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "linked_form_versions", joinColumns = @JoinColumn(name = "form_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "form_id", "linked_form_versions"}))
    @Column(name = "linked_form_versions")
    private final Set<Integer> linkedFormVersions;

    @Column(name = "linked_form_organization_id")
    private Long linkedFormOrganizationId;

    @Transient
    private boolean isLastVersion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "form_reference")
    private Form formReference;

    //For serialization of formReference only;
    private transient Long formReferenceId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tree_forms_references_hidden_elements", joinColumns = @JoinColumn(name = "form", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "element_to_hide", referencedColumnName = "id"))
    private Set<TreeObject> elementsToHide;
    private transient Set<Long> elementsToHideId;

    @OneToOne(mappedBy = "element", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private TreeObjectImage image;

    // Disables in orbeon the edition of this field. Means that when creating a
    // new form in orbeon is enabled, but when editing is disabled.
    @Column(name = "edition_disabled", nullable = false, columnDefinition = "bit default 0")
    private boolean editionDisabled = false;

    @ExcludeFromJson
    @Lob
    @Column(name = "json", length = MAX_JSON_LENGTH)
    private String jsonCode;

    public Form() {
        super();
        status = FormWorkStatus.DESIGN;
        description = "";
        rules = new HashSet<>();
        linkedFormVersions = new HashSet<>();
        webserviceCalls = new HashSet<>();
        formReference = null;
        elementsToHide = new HashSet<>();
    }

    public Form(String label, IUser<Long> user, Long organizationId) throws FieldTooLongException, CharacterNotAllowedException {
        super(label);
        status = FormWorkStatus.DESIGN;
        description = "";
        rules = new HashSet<>();
        webserviceCalls = new HashSet<>();
        linkedFormVersions = new HashSet<>();
        setCreatedBy(user);
        setUpdatedBy(user);
        setOrganizationId(organizationId);
        formReference = null;
        elementsToHide = new HashSet<>();
    }

    public void addFlow(Flow rule) throws FlowNotAllowedException {
        rules.add(rule);
        rule.setForm(this);
    }

    public void addFlows(Set<Flow> rules) {
        this.rules.addAll(rules);
        for (Flow rule : rules) {
            rule.setForm(this);
        }
    }

    public String getJsonCode() {
        return jsonCode;
    }

    public void setJsonCode(String jsonCode) {
        this.jsonCode = jsonCode;
    }

    public void addLinkedFormVersion(Integer versionNumber) {
        getLinkedFormVersions().add(versionNumber);
    }

    public boolean containsFlow(Flow rule) {
        return getFlows().contains(rule);
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        super.copyData(object);
        if (object instanceof Form) {
            description = ((Form) object).getDescription();
            status = ((Form) object).getStatus();
            setEditionDisabled(((Form) object).isEditionDisabled());

            linkedFormLabel = ((Form) object).getLinkedFormLabel();
            setLinkedFormVersions(((Form) object).getLinkedFormVersions());
            linkedFormOrganizationId = ((Form) object).getLinkedFormOrganizationId();
        } else {
            throw new NotValidTreeObjectException("Copy data for Form only supports the same type copy");
        }
    }

    /**
     * Returns all elements that the user has selected to hide.
     */
    public Set<TreeObject> getElementsToHide() {
        return elementsToHide;
    }

    /**
     * Returns all elements that the user has selected to hide and the elements
     * that are children of these elements.
     */
    public Set<TreeObject> getAllElementsToHide() {
        Set<TreeObject> elementsToHide = new HashSet<>();
        for (TreeObject elementToHide : getElementsToHide()) {
            elementsToHide.add(elementToHide);
            elementsToHide.addAll(elementToHide.getAll(TreeObject.class));
        }
        return elementsToHide;
    }

    /**
     * Mark an element as hidden.
     *
     * @return true if the element has change its state to hidden.
     */
    public boolean hideElement(TreeObject element) throws ElementCannotBeRemovedException {
        // If parent is hidden, do not hide this element.
        if (element == null) {
            return false;
        }
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
     * @return true if the element has change its state to not hidden.
     */
    public boolean showElement(TreeObject element) {
        boolean showed = false;
        if (elementsToHide.contains(element)) {
            elementsToHide.remove(element);
            showed = true;
        }
        // Show also any child.
        for (TreeObject child : element.getChildren()) {
            showElement(child);
        }
        return showed;
    }

    /**
     * Copy to this element the rules in form
     */
    private void copyRules(Form form, boolean discard) {
        LinkedHashSet<TreeObject> currentElements = getAllChildrenInHierarchy(TreeObject.class);
        HashMap<String, TreeObject> mappedElements = new HashMap<>();
        for (TreeObject currentElement : currentElements) {
            mappedElements.put(currentElement.getComparationId(), currentElement);
        }

        for (Flow rule : form.getFlows()) {
            Flow copiedRule = rule.generateCopy();
            // If rule origin is not in the list of current elements, or it has
            // destiny and is not in the list.
            if (discard) {
                if (!mappedElements.containsKey(copiedRule.getOrigin().getComparationId())
                        || (copiedRule.getDestiny() != null && !mappedElements.containsKey(copiedRule.getDestiny().getComparationId()))) {
                    continue;
                }
            }
            try {
                addFlow(copiedRule);
            } catch (FlowNotAllowedException e) {
                // Impossible
                WebformsLogger.errorMessage(this.getClass().getName(), e);
            }
        }
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
        newVersion.setCreationTime();
        newVersion.setUpdateTime();
        return newVersion;
    }

    public Form createNewVersion(IUser<Long> user) throws NotValidStorableObjectException, CharacterNotAllowedException {
        return createNewVersion(user.getUniqueId());
    }

    /**
     * Equals by comparationId and class. Comparation by class has been removed
     * to allow the comparation with CompleteFormView.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof StorableObject)) {
            return false;
        }

        StorableObject other = (StorableObject) obj;
        if (getComparationId() == null) {
            return other.getComparationId() == null;
        } else return getComparationId().equals(other.getComparationId());
    }

    public String exportToJavaCode(StringBuilder sb) {
        int counter = 0;

        sb.append("Form form = new Form();").append(System.lineSeparator());
        sb.append("form.setLabel(\"").append(getLabel()).append("\");").append(System.lineSeparator());
        sb.append("form.setDescription(\"").append(getDescription()).append("\");").append(System.lineSeparator());

        for (TreeObject child : getChildren()) {
            int tempCounter = counter + 1;
            counter = ((Category) child).exportToJavaCode(sb, counter + 1);
            sb.append("//form").append(System.lineSeparator());
            sb.append("form.addChild(").append("el_").append(tempCounter).append(");").append(System.lineSeparator());
        }

        return sb.toString();
    }

    /**
     * Overridden version of generate Copy to generate a copy of the flow rules.
     */
    @Override
    public TreeObject generateCopy(boolean copyParentHierarchy, boolean copyChildren) throws NotValidStorableObjectException, CharacterNotAllowedException {
        Form copy = (Form) super.generateCopy(copyParentHierarchy, copyChildren);

        if (copyChildren) {
            copy.setFormReference(getFormReference());
            copy.updateElementsToHide(getElementsToHide());
            copy.copyRules(this, false);
            copy.updateRuleReferences();
            copy.webserviceCalls(this);
            copy.updateWebserviceCallReferences();
        }

        copy.updateDynamicAnswers();

        return copy;
    }

    private void updateWebserviceCallReferences() {
        HashMap<String, BaseQuestion> references = new HashMap<>();
        for (BaseQuestion object : this.getAll(BaseQuestion.class)) {
            references.put(object.getOriginalReference(), object);
        }
        for (WebserviceCall call : getWebserviceCalls()) {
            call.updateReferences(references);
        }
    }

    private void webserviceCalls(Form form) throws NotValidStorableObjectException {
        for (WebserviceCall call : form.getWebserviceCalls()) {
            WebserviceCall copy = new WebserviceCall();
            copy.copyData(call);
            addWebserviceCall(copy);
        }
    }

    private void updateDynamicAnswers() {
        updateDynamicAnswers(getAllChildrenInHierarchy(Question.class));
    }

    private void updateDynamicAnswers(Set<Question> allQuestionsInHierarchy) {
        HashMap<String, Question> questions = new HashMap<>();
        for (Question child : allQuestionsInHierarchy) {
            questions.put(child.getOriginalReference(), child);
        }

        for (DynamicAnswer child : getAllChildrenInHierarchy(DynamicAnswer.class)) {
            child.setReference(questions.get(child.getReference().getOriginalReference()));
        }
    }

    /**
     * Update references for a new copy, version... of the form.
     */
    private void updateElementsToHide(Set<TreeObject> elementsToHide) {
        HashMap<String, TreeObject> elements = new HashMap<>();
        for (TreeObject element : getAllChildrenInHierarchy(TreeObject.class)) {
            elements.put(element.getOriginalReference(), element);
        }
        if (getFormReference() != null) {
            for (TreeObject element : getFormReference().getAllChildrenInHierarchy(TreeObject.class)) {
                elements.put(element.getOriginalReference(), element);
            }
        }
        Set<TreeObject> newElementsToHide = new HashSet<>();
        for (TreeObject elementToHide : elementsToHide) {
            if (elements.get(elementToHide.getOriginalReference()) != null) {
                newElementsToHide.add(elements.get(elementToHide.getOriginalReference()));
            }
        }
        setElementsToHide(newElementsToHide);
    }

    /**
     * Generate copy used to remove all rule elements that are not in the view.
     */
    public Form generateFormCopiedSimplification(TreeObject seed) throws NotValidStorableObjectException, CharacterNotAllowedException {
        TreeObject copiedSeed = seed.generateCopy(true, true);
        Form formSeed = (Form) copiedSeed.getAncestor(Form.class);

        formSeed.copyRules(this, true);
        formSeed.updateRuleReferences();

        return formSeed;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>(super.getAllInnerStorableObjects());
        for (Flow rule : getFlows()) {
            innerStorableObjects.add(rule);
            innerStorableObjects.addAll(rule.getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    protected List<Class<? extends TreeObject>> getAllowedChildren() {
        return ALLOWED_CHILDREN;
    }

    /**
     * This method creates a ComputeRuleView with all the current rules and the
     * implicit rules (question without rule goes to the next element)
     */
    public ComputedFlowView getComputedFlowsView() {
        LinkedHashSet<TreeObject> allBaseQuestions = getAllNotHiddenChildrenInHierarchy(BaseQuestion.class);
        ComputedFlowView computedView = new ComputedFlowView();

        if (!allBaseQuestions.isEmpty()) {
            Object[] baseQuestions = allBaseQuestions.toArray();
            computedView.setFirstElement((TreeObject) baseQuestions[0]);
            computedView.addFlows(getFlows());

            int numQuestions = baseQuestions.length - 1;

            for (int i = 0; i < numQuestions; i++) {
                if (computedView.getFlowsByOrigin((TreeObject) baseQuestions[i]) == null) {
                    computedView.addNewNextElementFlow((BaseQuestion) baseQuestions[i], (BaseQuestion) baseQuestions[i + 1]);
                }
            }
            if (computedView.getFlowsByOrigin((BaseQuestion) baseQuestions[numQuestions]) == null) {
                computedView.addNewEndFormFlow((BaseQuestion) baseQuestions[numQuestions]);
            }
        }
        return computedView;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        } else {
            return description;
        }
    }

    public Set<Flow> getFlows() {
        return rules;
    }

    public Set<Flow> getFlows(String originPath, String destinyPath) {
        return getFlows(getChild(originPath), getChild(destinyPath));
    }

    /**
     * Gets all flows that has as source and destiny the input parameters.
     */
    public Set<Flow> getFlows(TreeObject origin, TreeObject destiny) {
        Set<Flow> selectedFlows = new HashSet<>();
        for (Flow flow : getFlows()) {
            if (flow.getOrigin().getOriginalReference().equals(origin.getOriginalReference())
                    && ((flow.getDestiny() != null && destiny != null && flow.getDestiny().getOriginalReference().equals(destiny.getOriginalReference())) || (flow
                    .getDestiny() == null && destiny == null))) {
                selectedFlows.add(flow);
            }
        }
        return selectedFlows;
    }

    public Set<Flow> getFlowsFrom(BaseQuestion from) {
        Set<Flow> flows = new HashSet<>();
        for (Flow flow : getFlows()) {
            if (from != null && from.equals(flow.getOrigin())) {
                flows.add(flow);
            }
        }
        return flows;
    }

    public Set<Flow> getFlowsTo(BaseGroup to) {
        Set<Flow> flows = new HashSet<>();
        if (to != null) {
            for (BaseQuestion children : to.getAllChildrenInHierarchy(BaseQuestion.class)) {
                flows.addAll(getFlowsTo(children));
            }
        }
        return flows;
    }

    public Set<Flow> getFlowsTo(BaseQuestion to) {
        Set<Flow> flows = new HashSet<>();
        for (Flow flow : getFlows()) {
            if (to != null && flow.getDestiny() != null && to.equals(flow.getDestiny())) {
                flows.add(flow);
            }
        }
        return flows;
    }

    public BaseFormMetadata getFormMetadata() {
        BaseFormMetadata metadata = new BaseFormMetadata();
        metadata.setFormLabel(getLabel());
        metadata.setFormOrganizationId(getOrganizationId());
        metadata.setFormVersion(getVersion());
        metadata.setLinkedLabel(getLinkedFormLabel());
        metadata.setLinkedOrganizationId(getLinkedFormOrganizationId());
        metadata.setLinkedVersions(getLinkedFormVersions());
        return metadata;
    }

    public String getLabelWithoutSpaces() {
        return getLabel().replace(" ", "_");
    }

    @Override
    public String getLinkedFormLabel() {
        return linkedFormLabel;
    }

    @Override
    public Long getLinkedFormOrganizationId() {
        return linkedFormOrganizationId;
    }

    @Override
    public Set<Integer> getLinkedFormVersions() {
        return linkedFormVersions;
    }

    public String getReference(TreeObject element) throws ReferenceNotPertainsToFormException {
        List<TreeObject> parentList = new ArrayList<>();
        TreeObject parent;
        while ((parent = element.getParent()) != null) {
            parentList.add(parent);
        }
        if (!parentList.get(parentList.size() - 1).equals(this)) {
            throw new ReferenceNotPertainsToFormException("TreeObject: '" + element + "' doesn't belong to '" + this + "'");
        }

        StringBuilder reference = new StringBuilder("<" + element.getName() + ">");
        for (TreeObject listedParent : parentList) {
            reference.insert(0, "<" + listedParent.getName() + ">");
        }
        return "${" + reference + "}";
    }

    public FormWorkStatus getStatus() {
        return status;
    }

    /**
     * Compares the content of treeObject - Needs to be an instance of Form
     */
    @Override
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
                if (rules != null && rules.size() != form.rules.size()) {
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

                if (this.isEditionDisabled() != form.isEditionDisabled()) {
                    return false;
                }

                if (linkedFormLabel != null && !linkedFormLabel.equals(form.linkedFormLabel)) {
                    return false;
                }
                if (linkedFormVersions != null && !linkedFormVersions.equals(form.linkedFormVersions)) {
                    return false;
                }
                if (linkedFormOrganizationId != null && !linkedFormOrganizationId.equals(form.linkedFormOrganizationId)) {
                    return false;
                }

                if (formReference != null && !formReference.equals(form.getFormReference())) {
                    return false;
                }

                for (TreeObject elementToHide : elementsToHide) {
                    boolean contains = false;
                    for (TreeObject elementToHideInOtherForm : form.getElementsToHide()) {
                        if (elementToHide.getComparationId().equals(elementToHideInOtherForm.getComparationId())) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        return false;
                    }
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLastVersion() {
        return isLastVersion;
    }

    public boolean removeRule(Flow flow) {
        int currentRules = rules.size();
        rules.remove(flow);
        return currentRules == rules.size();
    }

    @Override
    public void resetIds() {
        // Overridden version to also reset ids of rules.
        super.resetIds();
        for (Flow rule : getFlows()) {
            rule.resetIds();
        }
        for (WebserviceCall call : getWebserviceCalls()) {
            call.resetIds();
        }
        if (image != null) {
            image.resetIds();
        }
    }

    @Override
    protected void resetDatabaseIds() {
        super.resetDatabaseIds();
        if (image != null) {
            image.resetDatabaseIds();
        }
    }

    public void setDescription(String description) throws FieldTooLongException {
        if (description == null) {
            description = "";
        }
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new FieldTooLongException("Description is longer than maximum: " + MAX_DESCRIPTION_LENGTH);
        }
        this.description = description;
    }

    public void setLastVersion(boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }

    public void setLinkedFormLabel(String linkedFormLabel) {
        this.linkedFormLabel = linkedFormLabel;
    }

    public void setLinkedFormOrganizationId(Long linkedFormOrganizationId) {
        this.linkedFormOrganizationId = linkedFormOrganizationId;
    }

    /**
     * This is the only function that has to be used to link forms. This
     * controls that the linked element and versions are present at the same
     * time or not when modifying data. It is assumed that all linked forms have
     * the same name and organization.
     */
    public void setLinkedForms(Set<IBaseFormView> linkedForms) {
        if (linkedForms == null || linkedForms.isEmpty()) {
            setLinkedFormLabel(null);
            setLinkedFormVersions(null);
            setLinkedFormOrganizationId(null);
        } else {
            Set<Integer> versionNumbers = new HashSet<>();
            for (IBaseFormView linkedForm : linkedForms) {
                setLinkedFormLabel(linkedForm.getLabel());
                versionNumbers.add(linkedForm.getVersion());
                setLinkedFormOrganizationId(linkedForm.getOrganizationId());
            }
            setLinkedFormVersions(versionNumbers);
        }
    }

    public void setLinkedFormVersions(Set<Integer> linkedFormVersions) {
        this.linkedFormVersions.clear();
        if (linkedFormVersions != null) {
            this.linkedFormVersions.addAll(linkedFormVersions);
        }
    }

    public void setRules(Set<Flow> rules) {
        this.rules.clear();
        addFlows(rules);
    }

    public void setStatus(FormWorkStatus status) {
        this.status = status;
    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.addSerializationExclusionStrategy(GsonUtils.getStrategyToAvoidAnnotation());
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        gsonBuilder.registerTypeAdapter(Form.class, new FormSerializer());
        gsonBuilder.registerTypeAdapter(Block.class, new BlockSerializer());
        gsonBuilder.registerTypeAdapter(BlockReference.class, new BlockReferenceSerializer());
        gsonBuilder.registerTypeAdapter(Category.class, new CategorySerializer());
        gsonBuilder.registerTypeAdapter(Group.class, new GroupSerializer());
        gsonBuilder.registerTypeAdapter(Question.class, new QuestionSerializer());
        gsonBuilder.registerTypeAdapter(Text.class, new TextSerializer());
        gsonBuilder.registerTypeAdapter(AttachedFiles.class, new AttachedFilesSerializer());
        gsonBuilder.registerTypeAdapter(SystemField.class, new SystemFieldSerializer());
        gsonBuilder.registerTypeAdapter(Answer.class, new AnswerSerializer());
        gsonBuilder.registerTypeAdapter(DynamicAnswer.class, new DynamicAnswerSerializer());
        gsonBuilder.registerTypeAdapter(Flow.class, new FlowSerializer());
        gsonBuilder.registerTypeAdapter(Token.class, new TokenSerializer<>());
        gsonBuilder.registerTypeAdapter(TokenBetween.class, new TokenBetweenSerializer());
        gsonBuilder.registerTypeAdapter(TokenEmpty.class, new TokenEmptySerializer());
        gsonBuilder.registerTypeAdapter(TokenComparationAnswer.class, new TokenComparationAnswerSerializer());
        gsonBuilder.registerTypeAdapter(TokenComparationValue.class, new TokenComparationValueSerializer());
        gsonBuilder.registerTypeAdapter(TokenIn.class, new TokenInSerializer());
        gsonBuilder.registerTypeAdapter(TokenInValue.class, new TokenInValueSerializer());
        gsonBuilder.registerTypeAdapter(WebserviceCall.class, new WebserviceCallSerializer());
        gsonBuilder.registerTypeAdapter(WebserviceCallInputLink.class, new WebserviceCallInputLinkSerializer());
        gsonBuilder.registerTypeAdapter(WebserviceCallInputLinkErrors.class, new WebserviceCallInputLinkErrorsSerializer());
        gsonBuilder.registerTypeAdapter(WebserviceCallOutputLink.class, new WebserviceCallOutputLinkSerializer());
        gsonBuilder.registerTypeAdapter(TreeObjectImage.class, new TreeObjectImageSerializer());
        Gson gson = gsonBuilder.create();

        return gson.toJson(this);
    }

    public void updateRuleReferences() {
        updateRuleReferences(getFlows());
    }

    public Set<List<Flow>> getAllFlowsFromOriginToDestiny(BaseQuestion origin, BaseQuestion destiny, HashMap<TreeObject, Integer> questionIndex,
                                                          ComputedFlowView computedFlowsView) {
        Set<List<Flow>> availablePaths = new HashSet<>();
        Set<Flow> flowsTo = computedFlowsView.getFlowsByDestiny(destiny);
        for (Flow flow : flowsTo) {
            // Flows that comes from question hat are previous to the origin,
            // can be discarded.
            if (questionIndex.get(flow.getOrigin()) < questionIndex.get(origin)) {
                continue;
            }
            // Add recursively all the paths.
            List<Flow> path = new ArrayList<>();
            Set<List<Flow>> parentAvailablePaths = getAllFlowsFromOriginToDestiny(origin, flow.getOrigin(), questionIndex, computedFlowsView);
            for (List<Flow> incomingPaths : parentAvailablePaths) {
                path.addAll(incomingPaths);
                path.add(flow);
            }
            availablePaths.add(path);
        }
        return availablePaths;
    }

    /**
     * Gets the relationship between questions and the global index in the form.
     */
    public HashMap<TreeObject, Integer> getQuestionsInOrder() {
        HashMap<TreeObject, Integer> questionIndex = new HashMap<>();
        LinkedHashSet<TreeObject> orderedQuestions = new LinkedHashSet<>();
        if (formReference != null) {
            orderedQuestions.addAll(getAllChildrenInHierarchy(BaseQuestion.class));
        }
        orderedQuestions.addAll(getAllChildrenInHierarchy(BaseQuestion.class));
        int index = 0;
        for (TreeObject orderedQuestion : orderedQuestions) {
            questionIndex.put(orderedQuestion, index);
            index++;
        }
        return questionIndex;
    }

    public void updateRuleReferences(Set<Flow> flows) {
        LinkedHashSet<TreeObject> currentElements = getAllChildrenInHierarchy(TreeObject.class);
        if (formReference != null) {
            currentElements.addAll(formReference.getAllChildrenInHierarchy(TreeObject.class));
        }
        HashMap<String, TreeObject> mappedElements = new HashMap<>();
        for (TreeObject currentElement : currentElements) {
            mappedElements.put(currentElement.getOriginalReference(), currentElement);
        }
        for (Flow rule : flows) {
            rule.updateReferences(mappedElements);
        }
    }

    public static Form fromJson(String jsonString) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Form.class, new FormDeserializer());
        gsonBuilder.registerTypeAdapter(TreeObjectImage.class, new TreeObjectImageDeserializer());
        Gson gson = gsonBuilder.create();

        return gson.fromJson(jsonString, Form.class);
    }

    /**
     * For some cases, i.e. using Springcache we need to initialize all sets
     * (disabling the Lazy loading).
     */
    @Override
    public void initializeSets() {
        super.initializeSets();
        getFlows().size();
        getWebserviceCalls().size();
        if (formReference != null) {
            formReference.initializeSets();
        }
    }

    @Override
    public void resetUserTimestampInfo(Long userId) {
        super.resetUserTimestampInfo(userId);
        for (Flow flow : getFlows()) {
            flow.resetUserTimestampInfo(userId);
        }
    }

    /**
     * Get index of child. If the element is a category of a LinkedBuildingBlock
     * try to find it in the Block References.
     */
    @Override
    public Integer getIndex(TreeObject child) {
        // Standard form element.
        int index = getChildren().indexOf(child);
        if (index >= 0) {
            return index;
        }
        // Child not found. Maybe is a category of a block reference.
        for (TreeObject blockReference : getChildren()) {
            if (blockReference instanceof BlockReference) {
                index = blockReference.getIndex(child);
                if (index >= 0) {
                    // Return the index of the block reference.
                    return getIndex(blockReference);
                }
            }
        }
        return -1;
    }

    /**
     * Real index taking in account the form reference elements.
     */
    @Override
    public Integer getRelativeIndex(TreeObject child) {
        // Normal form without linked form.
        if (getFormReference() == null) {
            return getIndex(child);
        }
        int index;
        if (((index = getFormReference().getIndex(child)) >= 0)) {
            return index;
        }
        // Linked Form is before the elements of the form.
        if (getChildren().contains(child)) {
            return getIndex(child) + getFormReference().getChildren().size();
        }
        return -1;
    }

    /**
     * Moves an object of the tree to last position in @toParent returns @objectToMove
     */
    public static synchronized TreeObject move(TreeObject objectToMove, TreeObject toParent) throws ChildrenNotFoundException, NotValidChildException,
            ElementIsReadOnly {
        if (!Objects.equals(objectToMove.getAncestor(Form.class), toParent.getAncestor(Form.class))) {
            throw new NotValidChildException("Root form for each element is different");
        }
        TreeObject newInstanceOfObjectToMove = TreeObject.move(objectToMove, toParent);

        Form form = (Form) objectToMove.getAncestor(Form.class);
        form.updateRuleReferences();
        form.updateDynamicAnswers();
        form.updateWebserviceCallReferences();
        return newInstanceOfObjectToMove;
    }

    @Override
    public void updateChildrenSortSeqs() {
        super.updateChildrenSortSeqs();
        for (Flow flow : getFlows()) {
            flow.updateConditionSortSeq();
        }
    }

    public Form getFormReference() {
        return formReference;
    }

    public Long getFormReferenceId() {
        return formReferenceId;
    }

    public void setFormReferenceId(Long formReferenceId) {
        this.formReferenceId = formReferenceId;
    }

    public void setFormReference(Form formReference) {
        this.formReference = formReference;
        this.formReferenceId = formReference != null ? formReference.getId() : null;
    }

    public void setElementsToHide(Set<TreeObject> elementsToHide) {
        this.elementsToHide = elementsToHide;
    }

    @Override
    public int compareTo(TreeObject arg0) {
        if (getFormReference() != null && getFormReference().equals(arg0)) {
            // Form reference always in first place.
            return 1;
        }
        if (arg0 instanceof Form && ((Form) arg0).getFormReference() != null && ((Form) arg0).getFormReference().equals(this)) {
            return -1;

        }
        return super.compareTo(arg0);
    }

    public Set<WebserviceCall> getWebserviceCalls() {
        return webserviceCalls;
    }

    public void addWebserviceCall(WebserviceCall webserviceCall) {
        webserviceCalls.add(webserviceCall);
        webserviceCall.setForm(this);
    }

    public void addWebserviceCalls(Set<WebserviceCall> webserviceCalls) {
        for (WebserviceCall call : webserviceCalls) {
            addWebserviceCall(call);
        }
    }

    public boolean usesWebservice(Webservice webservice) {
        for (WebserviceCall call : getWebserviceCalls()) {
            if (Objects.equals(call.getWebserviceName(), webservice.getName())) {
                return true;
            }
        }
        return false;
    }

    public void removeWebserviceCall(WebserviceCall call) {
        webserviceCalls.remove(call);
        call.setForm(null);
    }

    @Override
    public void setImage(TreeObjectImage image) {
        this.image = image;
        if (image != null) {
            image.setElement(this);
        }
    }

    @Override
    public TreeObjectImage getImage() {
        return image;
    }

    @Override
    public Set<TreeObjectImage> getAllImages() {
        Set<TreeObjectImage> images = new HashSet<>();
        if (this.getImage() != null) {
            images.add(this.getImage());
        }
        for (StorableObject children : getAllInnerStorableObjects()) {
            if (children instanceof ElementWithImage) {
                if (((ElementWithImage) children).getImage() != null) {
                    images.add(((ElementWithImage) children).getImage());
                }
            }
        }
        return images;
    }

    @Override
    public boolean hasJson() {
        return getJsonCode() != null;
    }

    @Override
    public void setHasJson(boolean hasJson) {
        throw new UnsupportedOperationException();
    }

    public boolean isEditionDisabled() {
        return editionDisabled;
    }

    public void setEditionDisabled(boolean editionDisabled) {
        this.editionDisabled = editionDisabled;
    }

    public Set<Long> getElementsToHideId() {
        return elementsToHideId;
    }

    public void setElementsToHideId(Set<Long> elementsToHideId) {
        this.elementsToHideId = elementsToHideId;
    }

    @Override
    public void addChild(TreeObject child) throws NotValidChildException, ElementIsReadOnly {
        if (child instanceof BlockReference) {
            // Include elements hidden in block.
            elementsToHide.addAll(child.getAllHiddenChildrenInHierarchy(null));
        }
        super.addChild(child);
    }
}
