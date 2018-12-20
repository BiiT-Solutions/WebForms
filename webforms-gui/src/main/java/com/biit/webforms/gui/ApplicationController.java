package com.biit.webforms.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.IBaseFormView;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.validators.ValidateBaseForm;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.utils.validation.ValidateReport;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.common.components.TreeTableProvider;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.exceptions.BadAbcdLink;
import com.biit.webforms.gui.exceptions.CategoryWithSameNameAlreadyExistsInForm;
import com.biit.webforms.gui.exceptions.DestinyIsContainedAtOrigin;
import com.biit.webforms.gui.exceptions.EmptyBlockCannotBeInserted;
import com.biit.webforms.gui.exceptions.FormWithSameNameException;
import com.biit.webforms.gui.exceptions.LinkCanOnlyBePerformedOnWholeBlock;
import com.biit.webforms.gui.exceptions.NewVersionWithoutFinalDesignException;
import com.biit.webforms.gui.exceptions.NotEnoughRightsToChangeStatusException;
import com.biit.webforms.gui.exceptions.NotValidAbcdForm;
import com.biit.webforms.gui.exceptions.SameOriginAndDestinationException;
import com.biit.webforms.gui.webpages.floweditor.WindowFlow;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.dao.ISimpleFormViewDao;
import com.biit.webforms.persistence.dao.IWebserviceDao;
import com.biit.webforms.persistence.dao.exceptions.WebserviceNotFoundException;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.AttachedFiles;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.IWebformsBlockView;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenWithQuestion;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.FormIsUsedAsReferenceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.biit.webforms.security.IWebformsSecurityService;
import com.biit.webforms.security.WebformsActivity;
import com.biit.webforms.utils.conversor.abcd.importer.ConversorAbcdFormToForm;
import com.biit.webforms.validators.ValidateFormAbcdCompatibility;
import com.biit.webforms.webservice.rest.client.AbcdRestClient;
import com.biit.webforms.webservices.Webservice;
import com.biit.webforms.webservices.WebserviceValidatedPort;
import com.google.gson.JsonParseException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * User Session data handler.
 *
 */
public class ApplicationController {
	private IFormDao formDao;
	private IBlockDao blockDao;
	private ISimpleFormViewDao simpleFormDaoWebforms;
	private IWebserviceDao webserviceDao;

	private Form lastEditedForm;
	private Form formInUse;
	private CompleteFormView completeFormView;

	private boolean unsavedFormChanges = false;
	private Set<Object> collapsedStatus;

	private IWebformsSecurityService webformsSecurityService;

	public ApplicationController() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("webformsFormDao");
		blockDao = (IBlockDao) helper.getBean("blockDao");
		simpleFormDaoWebforms = ((ISimpleFormViewDao) helper.getBean("simpleFormDaoWebforms"));
		webserviceDao = (IWebserviceDao) helper.getBean("webserviceDao");
		webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
	}

	/**
	 * User action to create a form on memory no persistence is done. Needs a
	 * unique name where name.length() < 190 characters.
	 * 
	 * @param formLabel
	 * @return
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 * @throws UnexpectedDatabaseException
	 */
	public Form createForm(String formLabel, Long organizationId) throws FieldTooLongException, FormWithSameNameException, CharacterNotAllowedException,
			UnexpectedDatabaseException {
		logInfoStart("createForm", formLabel, organizationId);

		// Create new form
		Form newform = null;
		try {
			newform = new Form(formLabel, UserSession.getUser(), organizationId);
		} catch (FieldTooLongException | CharacterNotAllowedException ex) {
			WebformsUiLogger.severe(ApplicationController.class.getName(), "createForm '" + ex.getMessage() + "'.");
			throw ex;
		}

		// Check if database contains a form with the same name.
		if (formDao.exists(formLabel, organizationId)) {
			FormWithSameNameException ex = new FormWithSameNameException("Form with name '" + formLabel + "' already exists.");
			WebformsUiLogger.severe(ApplicationController.class.getName(), "createForm " + ex.getMessage());
			throw ex;
		}

		return newform;
	}

	/**
	 * Imports a form serialized in json with formLabel as name in
	 * organizacionId organization. If database contains a form with the same
	 * label a {@link ElementCannotBePersistedException} exception is thrown.
	 * 
	 * @param json
	 * @param formLabel
	 * @param organizationId
	 * @return
	 * @throws FormWithSameNameException
	 * @throws UnexpectedDatabaseException
	 * @throws FieldTooLongException
	 * @throws ElementCannotBePersistedException
	 */
	public Form importFormFromJson(String json, String formLabel, Long organizationId) throws JsonParseException, FormWithSameNameException,
			UnexpectedDatabaseException, FieldTooLongException, ElementCannotBePersistedException {
		// Check if database contains a form with the same name.
		if (formDao.exists(formLabel, organizationId)) {
			FormWithSameNameException ex = new FormWithSameNameException("Form with name: " + formLabel + " already exists");
			WebformsUiLogger.severe(ApplicationController.class.getName(), "createForm " + ex.getMessage());
			throw ex;
		}

		Form newForm = Form.fromJson(json);
		newForm.setOrganizationId(organizationId);
		newForm.setLabel(formLabel);
		newForm.resetUserTimestampInfo(UserSession.getUser().getUniqueId());

		Set<WebserviceCall> webservicesToRemove = new HashSet<>();
		for (WebserviceCall call : newForm.getWebserviceCalls()) {
			if (!checkWebserviceCallConsistency(call)) {
				webservicesToRemove.add(call);
			}
		}
		for (WebserviceCall call : webservicesToRemove) {
			newForm.removeWebserviceCall(call);
		}

		// Reset ids before persisting buf after removing incorrect
		// webservices.
		newForm.resetIds();
		newForm = formDao.makePersistent(newForm);
		if (!webservicesToRemove.isEmpty()) {
			MessageManager.showWarning(LanguageCodes.WARNING_WRONG_WEBSERVICE_CONFIGURATION);
		}
		return newForm;
	}

	private boolean checkWebserviceCallConsistency(WebserviceCall call) {
		Webservice webservice;
		try {
			webservice = webserviceDao.findWebservice(call.getWebserviceName());
		} catch (WebserviceNotFoundException e) {
			return false;
		}

		for (WebserviceCallInputLink input : call.getInputLinks()) {
			WebserviceValidatedPort port = webservice.getInputPort(input.getWebservicePort());
			if (port == null) {
				return false;
			} else {
				for (WebserviceCallInputLinkErrors error : input.getErrors()) {
					if (!port.getErrorCodes().contains(error.getErrorCode())) {
						return false;
					}
				}
			}
		}

		for (WebserviceCallOutputLink output : call.getOutputLinks()) {
			if (webservice.getOutputPort(output.getWebservicePort()) == null) {
				return false;
			}
		}

		return true;
	}

	/**
	 * User action to create a form. Needs a unique name where name.length() <
	 * 190 characters.
	 * 
	 * @param formLabel
	 * @return
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBePersistedException
	 */
	public Form createFormAndPersist(String formLabel, Long organizationId) throws FieldTooLongException, FormWithSameNameException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementCannotBePersistedException {
		logInfoStart("createFormAndPersist", formLabel, organizationId);

		// Create new form
		Form newform = createForm(formLabel, organizationId);
		newform.setCreatedBy(UserSession.getUser().getUniqueId());

		// Persist form.
		try {
			formDao.makePersistent(newform);
		} catch (ConstraintViolationException cve) {
			WebformsUiLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}

		return newform;
	}

	/**
	 * Crates a new form block element with blockName in organizationId. If
	 * another block exists with the same name in the same organization a
	 * {@link ElementCannotBePersistedException} is thrown.
	 * 
	 * @param blockName
	 * @param organizationId
	 * @return
	 * @throws CharacterNotAllowedException
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBePersistedException
	 */
	public Block createBlock(String blockName, Long organizationId) throws CharacterNotAllowedException, FieldTooLongException, FormWithSameNameException,
			UnexpectedDatabaseException, ElementCannotBePersistedException {
		WebformsUiLogger.info(ApplicationController.class.getName(), "createBlock " + blockName);
		logInfoStart("createBlock", blockName, organizationId);

		// Create new block
		Block newBlock = null;
		try {
			newBlock = new Block(blockName, UserSession.getUser(), organizationId);
		} catch (FieldTooLongException | CharacterNotAllowedException ex) {
			WebformsUiLogger.warning(ApplicationController.class.getName(), "createBlock " + ex.getMessage());
			throw ex;
		}

		// Check if database contains a form with the same name.
		if (blockDao.getBlock(blockName, organizationId) != null) {
			FormWithSameNameException ex = new FormWithSameNameException("Block with name '" + blockName + "' already exists");
			WebformsUiLogger.warning(ApplicationController.class.getName(), "createBlock " + ex.getMessage());
			throw ex;
		}

		// Persist block.
		try {
			blockDao.makePersistent(newBlock);
		} catch (ConstraintViolationException cve) {
			WebformsUiLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}
		return newBlock;
	}

	/**
	 * Function to import abcd forms
	 * 
	 * @param abcdForm
	 * @param importLabel
	 * @param organization
	 * @return
	 * @throws NotValidAbcdForm
	 * @throws CharacterNotAllowedException
	 * @throws FormWithSameNameException
	 * @throws FieldTooLongException
	 * @throws UnexpectedDatabaseException
	 * @throws ElementIsReadOnly
	 * @throws ElementCannotBePersistedException
	 */
	public Form importAbcdForm(IBaseFormView simpleFormView, String importLabel, Long organizationId) throws NotValidAbcdForm, FieldTooLongException,
			FormWithSameNameException, CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly, ElementCannotBePersistedException {
		logInfoStart("importAbcdForm", simpleFormView, importLabel, organizationId);

		// Try to create a new form with the name and the organization
		Form webformsForm = createForm(importLabel, organizationId);

		// Read the original form and validate.
		com.biit.abcd.persistence.entity.Form abcdForm;
		abcdForm = getFormFromAbcdById(simpleFormView.getId());

		ValidateBaseForm validator = new ValidateBaseForm();
		ValidateReport report = new ValidateReport();
		if (!validator.validate(abcdForm, report)) {
			WebformsUiLogger.warning(this.getClass().getName(), "Import from Abcd failed. - Form not validated");
			WebformsUiLogger.warning(this.getClass().getName(), report.getReport());
			throw new NotValidAbcdForm();
		}

		ConversorAbcdFormToForm conversor = new ConversorAbcdFormToForm();
		Form webformsConvertedForm = conversor.convert(abcdForm);
		try {
			webformsForm.addChildren(webformsConvertedForm.getChildren());
		} catch (NotValidChildException e) {
			// Should not happen.
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}

		Set<IBaseFormView> linkedForms = new HashSet<IBaseFormView>();
		linkedForms.add(simpleFormView);
		webformsForm.setLinkedForms(linkedForms);

		// Store on the database
		formDao.makePersistent(webformsForm);

		return webformsForm;
	}

	/**
	 * Returns the List of Abcd Forms linked to a form or empty list if there
	 * are no links.
	 * 
	 * @param form
	 * @return
	 * @throws UnexpectedDatabaseException
	 * @throws BadAbcdLink
	 */
	public List<com.biit.abcd.persistence.entity.Form> getLinkedAbcdForm(Form form) throws UnexpectedDatabaseException, BadAbcdLink {
		List<com.biit.abcd.persistence.entity.Form> linkedForms = new ArrayList<>();
		if (form != null && form.getLinkedFormLabel() != null && form.getLinkedFormOrganizationId() != null) {
			for (Integer version : form.getLinkedFormVersions()) {
				com.biit.abcd.persistence.entity.Form abcdForm = getFormFromAbcdByLabelOrganizationAndVersion(form.getLinkedFormLabel(),
						form.getLinkedFormOrganizationId(), version);
				if (abcdForm == null) {
					throw new BadAbcdLink();
				}
				linkedForms.add(abcdForm);
			}
		}
		return linkedForms;
	}

	/**
	 * Returns abcd simpleViewForm linked to form using it's name, version and
	 * organizationId.
	 * 
	 * @param form
	 * @return
	 */
	public List<com.biit.abcd.persistence.entity.SimpleFormView> getLinkedSimpleAbcdForms(Form form) {
		List<com.biit.abcd.persistence.entity.SimpleFormView> linkedSimpleAbcdForms = new ArrayList<>();
		if (form.getLabel() != null && form.getOrganizationId() != null) {
			List<com.biit.abcd.persistence.entity.SimpleFormView> views = getAllSimpleFormViewsFromAbcdByLabelAndOrganization(form.getLinkedFormLabel(),
					form.getLinkedFormOrganizationId());
			for (com.biit.abcd.persistence.entity.SimpleFormView view : views) {
				if (form.getLinkedFormVersions().contains(view.getVersion())) {
					linkedSimpleAbcdForms.add(view);
				}
			}
		}
		return linkedSimpleAbcdForms;
	}

	/**
	 * Creates a new form that has a reference another form.
	 * 
	 * @param form
	 * @param formLabel
	 * @param organizationId
	 * @return
	 * @throws NotValidStorableObjectException
	 * @throws CharacterNotAllowedException
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 * @throws UnexpectedDatabaseException
	 */
	public Form createNewLinkedForm(Form form, String formLabel, Long organizationId) throws NotValidStorableObjectException, CharacterNotAllowedException,
			FieldTooLongException, FormWithSameNameException, UnexpectedDatabaseException {
		WebformsUiLogger.info(ApplicationController.class.getName(), "createNewLinkedForm " + form);

		Form newForm = createForm(formLabel, organizationId);
		newForm.setFormReference(form);
		newForm.setLinkedFormLabel(form.getLinkedFormLabel());
		newForm.setLinkedFormOrganizationId(form.getLinkedFormOrganizationId());
		newForm.setLinkedFormVersions(form.getLinkedFormVersions());
		try {
			formDao.makePersistent(newForm);
		} catch (ConstraintViolationException cve) {
			WebformsUiLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}
		return newForm;
	}

	/**
	 * Generates a copy of the current form data and stores with the version
	 * number increased.
	 * 
	 * @param form
	 * @return
	 * @throws NewVersionWithoutFinalDesignException
	 * @throws NotValidStorableObjectException
	 * @throws CharacterNotAllowedException
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBePersistedException
	 */
	public Form createNewFormVersion(Form form) throws NewVersionWithoutFinalDesignException, NotValidStorableObjectException, CharacterNotAllowedException,
			UnexpectedDatabaseException, ElementCannotBePersistedException {
		WebformsUiLogger.info(ApplicationController.class.getName(), "createNewFormVersion " + form);

		if (form.getStatus() == FormWorkStatus.DESIGN) {
			WebformsUiLogger.info(ApplicationController.class.getName(), "createNewFormVersion " + form
					+ " tried to create a new form version that is still in design");
			throw new NewVersionWithoutFinalDesignException();
		}

		Form newFormVersion;
		try {
			newFormVersion = form.createNewVersion(UserSession.getUser());
		} catch (CharacterNotAllowedException | NotValidStorableObjectException ex) {
			WebformsUiLogger.severe(ApplicationController.class.getName(), "createForm " + ex.getMessage());
			throw ex;
		}

		try {
			formDao.makePersistent(newFormVersion);
		} catch (ConstraintViolationException cve) {
			WebformsUiLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}
		return newFormVersion;
	}

	/**
	 * Modifies current form description with text.
	 * 
	 * @param form
	 * @param text
	 * @throws FieldTooLongException
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBePersistedException
	 */
	public void changeFormDescription(Form form, String text) throws FieldTooLongException, UnexpectedDatabaseException, ElementCannotBePersistedException {
		WebformsUiLogger.info(ApplicationController.class.getName(), "changeFormDescription " + form + " " + text);
		try {
			form.setDescription(text);
			formDao.makePersistent(form);
		} catch (FieldTooLongException e) {
			WebformsUiLogger.info(ApplicationController.class.getName(), "changeFormDescription " + e.getMessage());
			throw e;
		}
	}

	public void setFormInUse(Form form) {
		if (formInUse != null) {
			// Release current form if any.
			collapsedStatus = null;
			UiAccesser.releaseForm(formInUse, UserSession.getUser(), UI.getCurrent());
		}
		if (form == null) {
			formInUse = null;
			collapsedStatus = null;
		} else {
			formInUse = form;
			collapsedStatus = null;
			// Lock new form
			UiAccesser.lockForm(formInUse, UserSession.getUser(), UI.getCurrent());
			WebformsUiLogger.info(ApplicationController.class.getName(), "setFormInUse Form '" + formInUse + "'.");
			setUnsavedFormChanges(false);
			setLastEditedForm(formInUse);
		}
	}

	public Form getFormInUse() {
		return formInUse;
	}

	public CompleteFormView getCompleteFormView() {
		if (formInUse == null) {
			return null;
		}
		if (completeFormView == null || !getFormInUse().getComparationId().equals(completeFormView.getComparationId())) {
			completeFormView = new CompleteFormView(getFormInUse());
		}
		return completeFormView;
	}

	public Form getLastEditedForm() {
		return lastEditedForm;
	}

	private void setLastEditedForm(Form form) {
		this.lastEditedForm = form;
	}

	/**
	 * Clear memory of current form.
	 */
	public void clearFormInUse() {
		if (formInUse != null) {
			WebformsUiLogger.info(ApplicationController.class.getName(), "clearFormInUse");
			UiAccesser.releaseForm(formInUse, UserSession.getUser(), UI.getCurrent());
			clearParameters();
			setUnsavedFormChanges(false);
		}
	}

	/**
	 * Adds new category to current form
	 * 
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public Category addNewCategory() throws NotValidChildException, ElementIsReadOnly {
		setUnsavedFormChanges(true);
		return (Category) insertTreeObject(Category.class, getFormInUse(), "category");
	}

	/**
	 * Adds new group to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public Group addNewGroup(TreeObject parent) throws NotValidChildException, ElementIsReadOnly {
		setUnsavedFormChanges(true);
		return (Group) insertTreeObject(Group.class, parent, "group");
	}

	/**
	 * Adds new question to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public Question addNewQuestion(TreeObject parent) throws NotValidChildException, ElementIsReadOnly {
		setUnsavedFormChanges(true);
		return (Question) insertTreeObject(Question.class, parent, "question");
	}

	/**
	 * Adds a dynamic question to parent element.
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public DynamicAnswer addNewDynamicQuestion(TreeObject parent) throws NotValidChildException, ElementIsReadOnly {
		setUnsavedFormChanges(true);
		DynamicAnswer answer = (DynamicAnswer) insertTreeObject(DynamicAnswer.class, parent, "dynamic");
		try {
			answer.setLabel(LanguageCodes.DYNAMIC_ANSWER_LABEL.translation());
		} catch (FieldTooLongException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
		return answer;
	}

	/**
	 * Adds new System field
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public SystemField addNewSystemField(TreeObject parent) throws NotValidChildException, ElementIsReadOnly {
		setUnsavedFormChanges(true);
		return (SystemField) insertTreeObject(SystemField.class, parent, "system_field");
	}

	/**
	 * Adds new Text to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public Text addNewText(TreeObject parent) throws NotValidChildException, ElementIsReadOnly {
		setUnsavedFormChanges(true);
		return (Text) insertTreeObject(Text.class, parent, "info_text");
	}

	/**
	 * Adds new Attached Files to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public AttachedFiles addNewAttachedFiles(TreeObject parent) throws NotValidChildException, ElementIsReadOnly {
		setUnsavedFormChanges(true);
		return (AttachedFiles) insertTreeObject(AttachedFiles.class, parent, "attached_files");
	}

	/**
	 * Adds new answer to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public Answer addNewAnswer(TreeObject parent) throws NotValidChildException, ElementIsReadOnly {
		setUnsavedFormChanges(true);
		return (Answer) insertTreeObject(Answer.class, parent, "answer");
	}

	/**
	 * Creates any kind of TreeObject descendant with @name and inserts into
	 * parent if possible.
	 * 
	 * @param classType
	 * @param parent
	 * @param name
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public TreeObject insertTreeObject(Class<? extends TreeObject> classType, TreeObject parent, String name) throws NotValidChildException, ElementIsReadOnly {

		// Block references cannot have new childs.
		if (parent instanceof BlockReference || parent.isReadOnly()) {
			throw new NotValidChildException("Block References are read only and cannot be modified. ");
		}

		WebformsUiLogger.info(ApplicationController.class.getName(), "insertTreeObject of type '" + classType.getName() + "' to '" + parent + "' with name '"
				+ name + "' START");

		TreeObject treeObject = null;
		try {
			treeObject = classType.newInstance();
			String nameNumber = getNewStringNumber(parent, name);
			treeObject.setName(nameNumber);
			treeObject.setLabel(nameNumber);
			treeObject.setCreatedBy(UserSession.getUser());
			treeObject.setUpdatedBy(UserSession.getUser());
			parent.addChild(treeObject);
			setUnsavedFormChanges(true);
			WebformsUiLogger
					.info(ApplicationController.class.getName(), "inserted '" + treeObject + "' into '" + parent + "' ('" + parent.getPathName() + "')");
		} catch (FieldTooLongException | InstantiationException | IllegalAccessException | CharacterNotAllowedException e) {
			// Impossible
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		} catch (NotValidChildException e) {
			WebformsUiLogger.severe(this.getClass().getName(),
					"Element of type '" + classType.getName() + "' could not be inserted in '" + parent.getPathName() + "'");
			throw e;
		}

		return treeObject;
	}

	public String getNewStringNumber(TreeObject parent, String newString) {
		int maxNewString = 0;

		List<TreeObject> children = parent.getChildren();
		for (TreeObject child : children) {
			if (child.getName().startsWith(newString)) {
				if (child.getName().length() == newString.length()) {
					// First one (newString)
					maxNewString = Math.max(maxNewString, 1);
				} else {
					// Other, extract number (newStringint)
					String value = child.getName().substring(newString.length());
					try {
						maxNewString = Math.max(maxNewString, Integer.parseInt(value));
					} catch (NumberFormatException nfe) {
						// This is not an error, it won't be logged because it's
						// just for controlling when new-element tag is used
						// with something else than a number.
						// In this case, we simply ignore and continue the
						// function.
					}
				}
			}
		}

		if (maxNewString == 0) {
			return newString;
		} else {
			return newString + (maxNewString + 1);
		}
	}

	/**
	 * Removes a element of a tree.
	 * 
	 * @param row
	 * @throws DependencyExistException
	 * @throws ChildrenNotFoundException
	 * @throws ElementIsReadOnly
	 */
	public void removeTreeObject(TreeObject row) throws DependencyExistException, ChildrenNotFoundException, ElementIsReadOnly {
		WebformsUiLogger.info(ApplicationController.class.getName(), "removeTreeObject " + row + " of " + row.getAncestor(Form.class));

		getCompleteFormView().removeTreeObject(row);
		setUnsavedFormChanges(true);
	}

	/**
	 * Moves a element of a tree object up.
	 * 
	 * @param row
	 * @throws ElementIsReadOnly
	 */
	public void moveUp(TreeObject row) throws ElementIsReadOnly {
		WebformsUiLogger.info(ApplicationController.class.getName(), "move Up " + row + " START");
		// Move the BlockReference and not the element if exists.
		BlockReference blockReference = getCompleteFormView().getBlockReference(row);
		if (blockReference != null) {
			row = blockReference;
		}
		if (row.getParent() != null) {
			TreeObject parent = row.getParent();
			if (parent.moveChildUp(row)) {
				setUnsavedFormChanges(true);
			}
		}
	}

	/**
	 * Moves a element of a tree object down.
	 * 
	 * @param row
	 * @throws ElementIsReadOnly
	 */
	public void moveDown(TreeObject row) throws ElementIsReadOnly {
		WebformsUiLogger.info(ApplicationController.class.getName(), "move Down " + row);
		// Move the BlockReference and not the element if exists.
		BlockReference blockReference = getCompleteFormView().getBlockReference(row);
		if (blockReference != null) {
			row = blockReference;
		}
		if (row.getParent() != null) {
			TreeObject parent = row.getParent();
			if (parent.moveChildDown(row)) {
				setUnsavedFormChanges(true);
			}
		}
	}

	public void saveForm() throws UnexpectedDatabaseException, ElementCannotBePersistedException {
		formInUse.setUpdatedBy(UserSession.getUser());
		formInUse.setUpdateTime();
		Form savedForm = saveForm(formInUse);
		formInUse = savedForm;
		setLastEditedForm(formInUse);
		completeFormView = new CompleteFormView(getFormInUse());

		cleanFormReference(formInUse.getId());
	}

	/**
	 * Performs a transactional save operation of the form.
	 * 
	 * @param form
	 * @return
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBePersistedException
	 */
	@Transactional
	public Form saveForm(Form form) throws UnexpectedDatabaseException, ElementCannotBePersistedException {
		Form mergedForm = form;

		if (form instanceof Block) {
			if (!blockDao.getEntityManager().contains(form)) {
				mergedForm = blockDao.merge((Block) form);
			} else {
				blockDao.makePersistent((Block) mergedForm);
			}
			// Empty form cache to update changes in linked building blocks.
			formDao.evictAllCache();
		} else {
			if (!formDao.getEntityManager().contains(form)) {
				mergedForm = formDao.merge(form);
			} else {
				formDao.makePersistent(mergedForm);
			}
		}
		setUnsavedFormChanges(false);
		cleanFormReference(mergedForm.getId());
		return mergedForm;
	}

	/**
	 * Evicts from cache any form that uses the form as a reference to force the
	 * update.
	 * 
	 * @param form
	 */
	private void cleanFormReference(Long formId) {
		if (formId != null) {
			Set<SimpleFormView> forms = getFormsUsesAsReference(formId);
			for (SimpleFormView form : forms) {
				formDao.evictCache(form.getId());
			}
		}
	}

	/**
	 * Mark form as finished
	 * 
	 * @param form
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBePersistedException
	 */
	public void finishForm(Form form) throws UnexpectedDatabaseException, ElementCannotBePersistedException {
		logInfoStart("finishForm", form);
		form.setStatus(FormWorkStatus.FINAL_DESIGN);
		lockLinkedBlocks(form);
		setUnsavedFormChanges(false);
		saveForm(form);
	}

	/**
	 * Generate a building block from element and save it with blockLabel name
	 * in organization organizationId.
	 * 
	 * @param element
	 * @param blockLabel
	 * @param organizationId
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBeRemovedException
	 * @throws ElementCannotBePersistedException
	 */
	public void saveAsBlock(TreeObject element, String blockLabel, Long organizationId) throws FieldTooLongException, FormWithSameNameException,
			UnexpectedDatabaseException, ElementCannotBeRemovedException, ElementCannotBePersistedException {
		logInfoStart("saveAsBlock ", element, blockLabel, organizationId);

		Block block = null;
		try {
			// First we generate a copy. This can abort the whole process if the
			// resulted simplification is not complete.
			Form copiedForm = formInUse.generateFormCopiedSimplification(element);
			copiedForm.resetIds();

			// Now we create the new block
			block = createBlock(blockLabel, organizationId);
			// And set the copied view content as the children
			block.setChildren(copiedForm.getChildren());
			block.addFlows(copiedForm.getFlows());

			block.setUpdatedBy(UserSession.getUser());
			block.setUpdateTime();
			blockDao.merge(block);
		} catch (NotValidChildException | CharacterNotAllowedException | NotValidStorableObjectException e) {
			// Impossible, still, if fails remove block.
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			if (block != null) {
				blockDao.makeTransient(block);
			}
		}
	}

	/**
	 * Update form metadata.
	 * 
	 * @param form
	 * @param label
	 * @param description
	 */
	public void updateForm(Form form, String label, String description, TreeObjectImage image, boolean editionDisabled) {
		try {
			if (!Objects.equals(form.getLabel(), label) || !Objects.equals(form.getDescription(), description) || !Objects.equals(form.getImage(), image)
					|| (form.isEditionDisabled() != editionDisabled)) {
				setUnsavedFormChanges(true);
				form.setDescription(description);
				form.setLabel(label);
				form.setUpdatedBy(UserSession.getUser());
				form.setUpdateTime();
				form.setImage(image);
				form.setEditionDisabled(editionDisabled);
				logInfoStart("updateForm", form, label, description, image != null ? image.getFileName() : null, editionDisabled);
			}
		} catch (FieldTooLongException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	/**
	 * Update Answer data
	 * 
	 * @param answer
	 * @param value
	 * @param label
	 * @param description
	 */
	public void updateAnswer(Answer answer, String value, String label, String description, TreeObjectImage image) {
		try {
			if (!Objects.equals(answer.getLabel(), label) || !Objects.equals(answer.getDescription(), description) || !Objects.equals(answer.getValue(), value)
					|| !Objects.equals(answer.getImage(), image)) {
				setUnsavedFormChanges(true);
				answer.setValue(value);
				answer.setLabel(label);
				answer.setDescription(description);
				answer.setUpdatedBy(UserSession.getUser());
				answer.setUpdateTime();
				answer.setImage(image);
				logInfoStart("updateAnswer", answer, value, label, description, image != null ? image.getFileName() : null);
			}
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	/**
	 * Update category data.
	 * 
	 * @param category
	 * @param name
	 * @param label
	 */
	public void updateCategory(Category category, String name, String label, TreeObjectImage image) {
		try {
			if (!Objects.equals(category.getName(), name) || !Objects.equals(category.getLabel(), label) || !Objects.equals(category.getImage(), image)) {
				setUnsavedFormChanges(true);
				category.setName(name);
				category.setLabel(label);
				category.setUpdatedBy(UserSession.getUser());
				category.setUpdateTime();
				category.setImage(image);
				logInfoStart("updateCategory", category, name, label, image != null ? image.getFileName() : null);
			}
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	/**
	 * Update group data.
	 * 
	 * @param group
	 * @param name
	 * @param label
	 * @param repeatable
	 */
	public void updateGroup(Group group, String name, String label, boolean repeatable, boolean isTable) {
		try {
			if (!Objects.equals(group.getName(), name) || !Objects.equals(group.getLabel(), label) || group.isRepeatable() != repeatable
					|| group.isShownAsTable() != isTable) {
				setUnsavedFormChanges(true);
				group.setName(name);
				group.setLabel(label);
				group.setRepeatable(repeatable);
				group.setShownAsTable(isTable);
				group.setUpdatedBy(UserSession.getUser());
				group.setUpdateTime();
				logInfoStart("updateGroup", group, name, label, repeatable, isTable);
			}
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	/**
	 * Update question data.
	 * 
	 * @param question
	 * @param name
	 * @param label
	 * @param description
	 * @param mandatory
	 * @param answerType
	 * @param answerFormat
	 * @param answerSubformat
	 * @param horizontal
	 * @param defaultValue
	 */
	public void updateQuestion(Question question, String name, String label, String description, boolean mandatory, AnswerType answerType,
			AnswerFormat answerFormat, AnswerSubformat answerSubformat, boolean horizontal, Object defaultValue, boolean editionDisabled, TreeObjectImage image) {
		try {
			if (!Objects.equals(question.getLabel(), label) || !Objects.equals(question.getDescription(), description)
					|| !Objects.equals(question.getName(), name) || question.isMandatory() != mandatory
					|| (question.getAnswerType() != null && !question.getAnswerType().equals(answerType))
					|| (question.getAnswerFormat() != null && !Objects.equals(question.getAnswerFormat(), answerFormat))
					|| (question.getAnswerSubformat() != null && !Objects.equals(question.getAnswerSubformat(), answerSubformat))
					|| question.isHorizontal() != horizontal || (defaultValue == null && question.getDefaultValue() != "")
					|| (defaultValue != null && !Objects.equals(question.getDefaultValue(), defaultValue.toString()))
					|| (question.isEditionDisabled() != editionDisabled) || !Objects.equals(question.getImage(), image)) {
				setUnsavedFormChanges(true);
				question.setName(name);
				question.setLabel(label);
				question.setDescription(description);
				question.setAnswerType(answerType);
				question.setAnswerFormat(answerFormat);
				question.setAnswerSubformat(answerSubformat);
				question.setMandatory(mandatory);
				question.setHorizontal(horizontal);
				question.setUpdatedBy(UserSession.getUser());
				question.setUpdateTime();
				question.setDefaultValue(defaultValue);
				question.setEditionDisabled(editionDisabled);
				question.setImage(image);
				logInfoStart("updateQuestion", question, name, label, description, mandatory, answerType, answerFormat, answerSubformat, horizontal,
						image != null ? image.getFileName() : null);
			}
		} catch (FieldTooLongException | InvalidAnswerFormatException | CharacterNotAllowedException | InvalidAnswerSubformatException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public void updateAttachedFiles(AttachedFiles attachedFiles, String name, String label, String description, boolean mandatory, boolean editionDisabled) {
		try {
			if (!Objects.equals(attachedFiles.getLabel(), label) || !Objects.equals(attachedFiles.getName(), name)
					|| !Objects.equals(attachedFiles.getDescription(), description) || attachedFiles.isMandatory() != mandatory
					|| (attachedFiles.isEditionDisabled() != editionDisabled)) {
				setUnsavedFormChanges(true);
				attachedFiles.setName(name);
				attachedFiles.setLabel(label);
				attachedFiles.setDescription(description);
				attachedFiles.setMandatory(mandatory);
				attachedFiles.setUpdatedBy(UserSession.getUser());
				attachedFiles.setUpdateTime();
				attachedFiles.setEditionDisabled(editionDisabled);
				logInfoStart("updateAttachedFile", attachedFiles, name, label, mandatory);
			}
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	/**
	 * Inserts element belonging group to current form. This generates a clone
	 * of the block using the element as hierarchy seed and introduces to
	 * current form as a new category.
	 * 
	 * @param selectedRow
	 * @return Inserted category
	 * @throws CategoryWithSameNameAlreadyExistsInForm
	 * @throws EmptyBlockCannotBeInserted
	 * @throws ElementIsReadOnly
	 */
	public TreeObject insertBlock(TreeObject block) throws CategoryWithSameNameAlreadyExistsInForm, EmptyBlockCannotBeInserted, ElementIsReadOnly {
		WebformsUiLogger.info(ApplicationController.class.getName(), "insert Block " + formInUse + " " + block);

		if (block instanceof Block) {
			if (block.getChildren().isEmpty()) {
				throw new EmptyBlockCannotBeInserted();
			}
			try {
				if (formInUse.findChild(block.getChild(0).getName()) != null) {
					// Element found, throw exception.
					throw new CategoryWithSameNameAlreadyExistsInForm();
				}
			} catch (ChildrenNotFoundException e) {
				// Not possible.
				WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			}
		} else {
			// Check name uniqueness first
			Category category = (Category) block.getAncestor(Category.class);
			if (formInUse.findChild(category.getName()) != null) {
				// Element found, throw exception.
				throw new CategoryWithSameNameAlreadyExistsInForm();
			}
		}

		try {
			Block blockToInsert = (Block) block.getAncestor(Block.class);
			Block copiedBlock = (Block) blockToInsert.generateFormCopiedSimplification(block);
			copiedBlock.resetIds();
			// We need different original Ids if we insert the same building
			// block several times in the same form.
			copiedBlock.resetOriginalIds();

			TreeObject insertedCategory = copiedBlock.getChildren().get(0);

			formInUse.addChildren(copiedBlock.getChildren());
			formInUse.addFlows(copiedBlock.getFlows());
			formInUse.addWebserviceCalls(copiedBlock.getWebserviceCalls());

			setUnsavedFormChanges(true);

			return insertedCategory;
		} catch (NotValidStorableObjectException | NotValidChildException | CharacterNotAllowedException e) {
			// Impossible.
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	/**
	 * Linked building blocks are translated to standard blocks to avoid changes
	 * after the form is finished.
	 */
	private void lockLinkedBlocks(Form form) {
		Form completeForm = form;

		// Convert to CompleteForm to use all methods of filtering hide
		// elements.
		if (!(completeForm instanceof CompleteFormView)) {
			completeForm = new CompleteFormView(form);
		}

		// Get needed elements.
		List<TreeObject> children = ((CompleteFormView) completeForm).getAllNotHiddenChildren();
		Set<Flow> flows = ((CompleteFormView) completeForm).getFlows();

		// Reset the parent to the form. Force updating database to correct
		// elements.
		((CompleteFormView) completeForm).getForm().getChildren().clear();
		((CompleteFormView) completeForm).getForm().getChildren().addAll(children);

		// Update flow with the flow of the linked block.
		((CompleteFormView) completeForm).getForm().getFlows().clear();
		((CompleteFormView) completeForm).getForm().getFlows().addAll(flows);
		((CompleteFormView) completeForm).getForm().updateRuleReferences();
	}

	/**
	 * Inserts element belonging group to current form. This generates a clone
	 * of the block using the element as hierarchy seed and introduces to
	 * current form as a new category.
	 * 
	 * @param selectedRow
	 * @throws CategoryWithSameNameAlreadyExistsInForm
	 * @throws EmptyBlockCannotBeInserted
	 * @throws ElementIsReadOnly
	 */
	public TreeObject linkBlock(TreeObject block) throws CategoryWithSameNameAlreadyExistsInForm, EmptyBlockCannotBeInserted, ElementIsReadOnly,
			LinkCanOnlyBePerformedOnWholeBlock {
		WebformsUiLogger.info(ApplicationController.class.getName(), "link Block '" + block + "' in '" + formInUse + "' ");

		if (block instanceof Block) {
			// Check valid block.
			if (block.getChildren().isEmpty()) {
				throw new EmptyBlockCannotBeInserted();
			}
			try {
				if (formInUse.findChild(block.getChild(0).getName()) != null) {
					// Element found, throw exception.
					throw new CategoryWithSameNameAlreadyExistsInForm();
				}
			} catch (ChildrenNotFoundException e) {
				// Not possible.
				WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			}

			// Create a block link and insert it.
			try {
				BlockReference blockReference = new BlockReference((Block) block);
				formInUse.addChild(blockReference);
				setUnsavedFormChanges(true);
				return block.getChild(0);
			} catch (NotValidChildException | ChildrenNotFoundException e) {
				// Impossible.
				WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				return null;
			}
		} else {
			throw new LinkCanOnlyBePerformedOnWholeBlock();
		}
	}

	/**
	 * Moves a TreeObject origin to a new child position on destiny.
	 * 
	 * @param origin
	 * @param destiny
	 * @return
	 * @throws NotValidChildException
	 * @throws SameOriginAndDestinationException
	 * @throws DestinyIsContainedAtOrigin
	 * @throws ElementIsReadOnly
	 */
	public TreeObject moveTo(TreeObject origin, TreeObject destiny) throws NotValidChildException, SameOriginAndDestinationException,
			DestinyIsContainedAtOrigin, ChildrenNotFoundException, ElementIsReadOnly {
		WebformsUiLogger.info(ApplicationController.class.getName(), "move '" + origin + "' to '" + destiny + "'.");
		if (origin.equals(destiny)) {
			throw new SameOriginAndDestinationException("Origin and destination are the same element.");
		}
		if (origin.getParent().equals(destiny)) {
			throw new DestinyIsContainedAtOrigin("Element is already inside the selected element.");
		}
		if (origin.contains(destiny)) {
			throw new DestinyIsContainedAtOrigin("Origin is contained inside destination element.");
		}
		if (destiny.getChild(origin.getName()) != null) {
			MessageManager.showWarning(LanguageCodes.WARNING_ELEMENT_WTIH_SAME_NAME_EXIST);
		}
		if (!destiny.isAllowedChildren(origin)) {
			throw new NotValidChildException("Class '" + this.getClass().getName() + "' does not allows instances of '" + origin.getClass().getName()
					+ "' as child.");
		}
		try {
			TreeObject newInstanceOfOrigin = Form.move(origin, destiny);
			setUnsavedFormChanges(true);
			return newInstanceOfOrigin;
		} catch (NotValidChildException | ChildrenNotFoundException e) {
			WebformsUiLogger.warning(ApplicationController.class.getName(), "move '" + origin + "' to '" + destiny + "' could not be done.");
			throw e;
		}
	}

	/**
	 * This function is called when the UI has expired. The implementation needs
	 * to free any "locked" resources
	 */
	public void freeLockedResources() {
		clearFormInUse();
	}

	/**
	 * Adds flow to a explicit form.
	 * 
	 * @param flow
	 * @param form
	 * @throws FlowNotAllowedException
	 */
	public void addFlowToForm(Flow flow, Form form) throws FlowNotAllowedException {
		logInfoStart("addFlowToForm", flow, form);
		form.addFlow(flow);
		setUnsavedFormChanges(true);
	}

	/**
	 * Update flow content. This function currently is a direct call to the
	 * structure function. If the flow is not on the form, it gets added.
	 * 
	 * @param flow
	 * @param origin
	 * @param flowType
	 * @param destiny
	 * @param conditionString
	 * @throws BadFlowContentException
	 * @throws FlowWithoutSourceException
	 * @throws FlowSameOriginAndDestinyException
	 * @throws FlowDestinyIsBeforeOriginException
	 * @throws FlowWithoutDestinyException
	 * @throws FlowNotAllowedException
	 */
	public void updateFlowContent(Flow flow, BaseQuestion origin, FlowType flowType, BaseQuestion destiny, boolean others, List<Token> condition)
			throws BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException,
			FlowWithoutDestinyException, FlowNotAllowedException {
		logInfoStart("updateFlowContent", flow, origin, flowType, destiny, others, condition);
		flow.setContent(origin, flowType, destiny, others, condition);

		IUser<Long> user = UserSession.getUser();
		if (flow.getCreatedBy() == null) {
			flow.setCreationTime();
			flow.setCreatedBy(user);
		}
		flow.setUpdateTime();
		flow.setUpdatedBy(user);
		setUnsavedFormChanges(true);

		if (!getFormInUse().containsFlow(flow)) {
			addFlowToForm(flow, getCompleteFormView());
		}
	}

	/**
	 * Updates flow update time and updated by in flow. The content of the flow
	 * was already modified by {@link WindowFlow}
	 * 
	 * @param flow
	 */
	public void updateFlow(Flow flow) {
		WebformsUiLogger.info(ApplicationController.class.getName(), "updateFlow '" + flow + "' in '" + getFormInUse() + "' START");
		flow.setUpdateTime();
		flow.setUpdatedBy(UserSession.getUser());
		setUnsavedFormChanges(true);
	}

	/**
	 * Clones flows.
	 * 
	 * @param selectedFlowss
	 */
	public Set<Flow> cloneFlows(Set<Flow> selectedFlows) {
		logInfoStart("cloneFlows", selectedFlows);
		Set<Flow> clonedFlows = new HashSet<Flow>();
		for (Flow selectedFlow : selectedFlows) {
			clonedFlows.add(selectedFlow.generateCopy());
			setUnsavedFormChanges(true);
		}
		return clonedFlows;
	}

	/**
	 * Clones a set of flows, reset Ids and inserts into form
	 * 
	 * @param selectedFlows
	 * @return
	 * @throws FlowNotAllowedException
	 */
	public Set<Flow> cloneFlowsAndInsertIntoForm(Set<Flow> selectedFlows) throws FlowNotAllowedException {
		logInfoStart("cloneFlowsAndInsertIntoForm", selectedFlows);
		Set<Flow> clones = cloneFlows(selectedFlows);
		for (Flow clone : clones) {
			clone.resetIds();
			addFlowToForm(clone, getCompleteFormView());
			setUnsavedFormChanges(true);
		}
		return clones;
	}

	/**
	 * Creates a Token where Q = Value
	 * 
	 * @param type
	 * @param question
	 * @param valueAnswerSubformat
	 * @param datePeriodUnit
	 * @param value
	 * @return
	 */
	public Token createTokenComparationValue(TokenTypes type, WebformsBaseQuestion question, AnswerSubformat valueAnswerSubformat,
			DatePeriodUnit datePeriodUnit, String value) {
		Token token = TokenComparationValue.getToken(type, question, valueAnswerSubformat, datePeriodUnit, value);
		token.setCreatedBy(UserSession.getUser());
		token.setUpdatedBy(UserSession.getUser());
		token.setUpdateTime();
		return token;
	}

	/**
	 * Removes a set of flows from current form.
	 * 
	 * @param selectedFlows
	 */
	public void removeFlows(Set<Flow> selectedFlows) {
		removeFlows(getCompleteFormView(), selectedFlows);
	}

	private void removeFlows(Form form, Set<Flow> flows) {
		logInfoStart("removeFlows", form, flows);
		for (Flow flow : flows) {
			form.removeRule(flow);
		}
		setUnsavedFormChanges(true);
	}

	private void logInfoStart(String functionName, Object... parameters) {
		WebformsUiLogger.info(ApplicationController.class.getName(), getFunctionInfo(functionName, parameters));
	}

	protected String getFunctionInfo(String functionName, Object... parameters) {
		String functionInfo = new String(functionName + "(");
		int i = 0;
		for (Object parameter : parameters) {
			String parameterString = new String();
			if (i > 0) {
				parameterString += ", ";
			}
			parameterString += "arg" + i + ": '" + parameter + "'";
			functionInfo += parameterString;
			i++;
		}
		functionInfo += ")";
		return functionInfo;
	}

	public void logOut() {
		WebformsUiLogger.info(this.getClass().getName(), "User '" + UserSession.getUser().getEmailAddress() + "' has logged out.");
		clearFormInUse();
		UserSession.logout();
	}

	public IFormDao getWebformsFormDao() {
		return formDao;
	}

	public IBlockDao getWebformsBlockDao() {
		return blockDao;
	}

	/**
	 * Get table data provider to fill manager table data (Abcd form).
	 * 
	 * @return
	 */
	public TreeTableProvider<com.biit.abcd.persistence.entity.Form> getTreeTableAbcdFormsProvider() {
		TreeTableProvider<com.biit.abcd.persistence.entity.Form> provider = new TreeTableProvider<com.biit.abcd.persistence.entity.Form>() {

			@Override
			public Collection<com.biit.abcd.persistence.entity.Form> getAll() throws UnexpectedDatabaseException {
				List<com.biit.abcd.persistence.entity.Form> forms = new ArrayList<>();
				Set<IGroup<Long>> userOrganizations = webformsSecurityService.getUserOrganizationsWhereIsAuthorized(UserSession.getUser(),
						WebformsActivity.READ);
				for (IGroup<Long> organization : userOrganizations) {
					forms.addAll(getFormsFromAbcdByOrganization(organization.getUniqueId()));
				}
				return forms;
			}
		};

		return provider;
	}

	/**
	 * Get table data provider to fill manager table data (Webforms form)
	 * 
	 * @return
	 */
	public TreeTableProvider<com.biit.webforms.persistence.entity.SimpleFormView> getTreeTableFormsProvider() {
		TreeTableProvider<com.biit.webforms.persistence.entity.SimpleFormView> provider = new TreeTableProvider<com.biit.webforms.persistence.entity.SimpleFormView>() {

			@Override
			public Collection<com.biit.webforms.persistence.entity.SimpleFormView> getAll() throws UnexpectedDatabaseException {
				List<com.biit.webforms.persistence.entity.SimpleFormView> userForms = new ArrayList<>();

				List<com.biit.webforms.persistence.entity.SimpleFormView> simpleForms = simpleFormDaoWebforms.getAll();

				Set<IGroup<Long>> userOrganizations = webformsSecurityService.getUserOrganizationsWhereIsAuthorized(UserSession.getUser(),
						WebformsActivity.READ);

				for (com.biit.webforms.persistence.entity.SimpleFormView form : simpleForms) {
					for (IGroup<Long> organization : userOrganizations) {
						if (form.getOrganizationId().equals(organization.getUniqueId())) {
							userForms.add(form);
						}
					}
				}

				return userForms;
			}
		};
		return provider;
	}

	/**
	 * Get all forms where the user has READ permission in ABCD and EDIT
	 * permissions in Webforms.
	 * 
	 * @return
	 */
	public TreeTableProvider<com.biit.abcd.persistence.entity.SimpleFormView> getTreeTableSimpleAbcdFormsProvider() {
		TreeTableProvider<com.biit.abcd.persistence.entity.SimpleFormView> provider = new TreeTableProvider<com.biit.abcd.persistence.entity.SimpleFormView>() {
			@Override
			public List<com.biit.abcd.persistence.entity.SimpleFormView> getAll() {
				return getAllSimpleFormViewsFromAbcdForCurrentUser();
			}
		};
		return provider;
	}

	/**
	 * Validate compatibility between a webforms form and a abcd form.
	 * 
	 * @param currentForm
	 * @param abcdSimpleForm
	 * @throws BadAbcdLink
	 */
	public void validateCompatibility(Form currentForm, IBaseFormView abcdSimpleForm) throws BadAbcdLink {
		com.biit.abcd.persistence.entity.Form abcdForm = getFormFromAbcdById(abcdSimpleForm.getId());
		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(currentForm);
		ValidateReport report = new ValidateReport();
		validator.validate(abcdForm, report);
		if (!report.isValid()) {
			throw new BadAbcdLink("Abcd form '" + abcdSimpleForm.getLabel() + "' is not a valid link for form '" + currentForm.getLabel() + "'.");
		}
	}

	/**
	 * Return a simplified version with the abcd form metadata for a determinate
	 * label and organizationId.
	 * 
	 * @param label
	 * @param organizationId
	 * @return
	 */
	public List<com.biit.abcd.persistence.entity.SimpleFormView> getAllSimpleFormViewsFromAbcdByLabelAndOrganization(String label, Long organizationId) {
		return AbcdRestClient.getSimpleFormViewsFromAbcdByLabelAndOrganization(WebformsConfigurationReader.getInstance().getAbcdRestServiceUrl(),
				WebformsConfigurationReader.getInstance().getAbcdRestServiceSimpleFormViewByLabelAndOrganizationPath(),
				UserSession.getUser().getEmailAddress(), label, organizationId);
	}

	/**
	 * Return a simplified version with the abcd form metadata for current user
	 * 
	 * @return
	 */
	public List<com.biit.abcd.persistence.entity.SimpleFormView> getAllSimpleFormViewsFromAbcdForCurrentUser() {
		return AbcdRestClient.getSimpleFormViewsFromAbcdByUserEmail(WebformsConfigurationReader.getInstance().getAbcdRestServiceUrl(),
				WebformsConfigurationReader.getInstance().getAbcdRestServiceAllSimpleFormViewsPath(), UserSession.getUser().getEmailAddress());
	}

	/**
	 * Returns the abcd form data with formId as id.
	 * 
	 * @param formId
	 * @return
	 */
	public com.biit.abcd.persistence.entity.Form getFormFromAbcdById(Long formId) {
		return AbcdRestClient.getFormFromAbcdById(WebformsConfigurationReader.getInstance().getAbcdRestServiceUrl(), WebformsConfigurationReader.getInstance()
				.getAbcdRestServiceCompleteFormByIdPath(), UserSession.getUser().getEmailAddress(), formId);
	}

	/**
	 * Returns all the abcd forms by organization.
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<com.biit.abcd.persistence.entity.Form> getFormsFromAbcdByOrganization(Long organizationId) {
		return AbcdRestClient.getFormsFromAbcdByOrganization(WebformsConfigurationReader.getInstance().getAbcdRestServiceUrl(), WebformsConfigurationReader
				.getInstance().getAbcdRestServiceCompleteFormsByOrganizationPath(), UserSession.getUser().getEmailAddress(), organizationId);
	}

	/**
	 * Returns a specific abcd form by label, organization and version.
	 * 
	 * @param formLabel
	 * @param formOrganization
	 * @param formVersion
	 * @return
	 */
	public com.biit.abcd.persistence.entity.Form getFormFromAbcdByLabelOrganizationAndVersion(String formLabel, Long formOrganization, Integer formVersion) {
		return AbcdRestClient.getFormFromAbcdByLabelOrganizationAndVersion(WebformsConfigurationReader.getInstance().getAbcdRestServiceUrl(),
				WebformsConfigurationReader.getInstance().getAbcdRestServiceCompleteFormByLabelFormAndOrganizationPath(), UserSession.getUser()
						.getEmailAddress(), formLabel, formOrganization, formVersion);
	}

	/**
	 * Returns all organizations where user has permission to do all the
	 * activities in activitiesFilter.
	 * 
	 * @param activitiesFilter
	 * @return
	 */
	public Set<IGroup<Long>> getOrganizatiosWhereUser(IActivity... activitiesFilter) {
		try {
			Set<IGroup<Long>> organizations = webformsSecurityService.getUserOrganizations(UserSession.getUser());
			Iterator<IGroup<Long>> itr = organizations.iterator();
			while (itr.hasNext()) {
				IGroup<Long> organization = itr.next();
				for (IActivity activity : activitiesFilter) {
					// If the user doesn't comply to all activities in the
					// filter in the group, then exit
					if (!webformsSecurityService.isAuthorizedActivity(UserSession.getUser(), organization, activity)) {
						itr.remove();
						break;
					}
				}
			}
			return organizations;
		} catch (UserManagementException e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
		return new HashSet<>();
	}

	public boolean existsUnsavedFormChanges() {
		return unsavedFormChanges;
	}

	public void setUnsavedFormChanges(boolean value) {
		unsavedFormChanges = value;
	}

	public ISimpleFormViewDao getSimpleFormDaoWebforms() {
		return simpleFormDaoWebforms;
	}

	public Form loadForm(IWebformsFormView formView) {
		return formDao.get(formView.getId());
	}

	public Block loadBlock(IWebformsBlockView blockView) {
		return blockDao.get(blockView.getId());
	}

	/**
	 * Returns all simplified versions of a webforms form by label and
	 * organization.
	 * 
	 * @param label
	 * @param organizationId
	 * @return
	 */
	public List<com.biit.webforms.persistence.entity.SimpleFormView> getSimpleFormVersionsWebforms(String label, Long organizationId) {
		List<com.biit.webforms.persistence.entity.SimpleFormView> filteredForms = new ArrayList<>();
		List<com.biit.webforms.persistence.entity.SimpleFormView> forms = simpleFormDaoWebforms.getAll();

		for (com.biit.webforms.persistence.entity.SimpleFormView form : forms) {
			if (form.getLabel().equals(label) && form.getOrganizationId().equals(organizationId)) {
				filteredForms.add(form);
			}
		}
		return filteredForms;
	}

	/**
	 * Modifies status of a form Normal users can change to production status
	 * but to downgrade the status the user needs the downgrade permission.
	 * 
	 * @param formView
	 * @param value
	 * @throws NotEnoughRightsToChangeStatusException
	 * @throws ElementCannotBePersistedException
	 */
	public void changeFormStatus(IWebformsFormView formView, FormWorkStatus value) throws NotEnoughRightsToChangeStatusException,
			ElementCannotBePersistedException {
		// Can downgrade
		boolean userCanDowngradeStatus = webformsSecurityService.isAuthorizedActivity(UserSession.getUser(), formView, WebformsActivity.FORM_STATUS_DOWNGRADE);

		if (!formView.getStatus().isMovingForward(value)) {
			if (!(userCanDowngradeStatus)) {
				throw new NotEnoughRightsToChangeStatusException();
			}
		}

		Form form = loadForm(formView);
		form.setStatus(value);
		try {
			saveForm(form);
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}
	}

	public void evictAllCache() {
		formDao.evictAllCache();
		blockDao.evictAllCache();
		clearParameters();
	}

	public void clearParameters() {
		formInUse = null;
		completeFormView = null;
	}

	/**
	 * Defines if any form is using an element in its flow (as origin or
	 * destination).
	 * 
	 * @param element
	 * @return
	 * @throws UnexpectedDatabaseException
	 */
	public boolean existFormThatUseElementInFlow(TreeObject element) throws UnexpectedDatabaseException {
		// If element has no id, the element is still not persisted in database.
		// Therefore no form can use it.
		if (element.getId() == null) {
			return false;
		}
		List<TreeObject> elements = new ArrayList<>();
		if (element instanceof BaseQuestion) {
			elements.add(element);
		} else {
			elements.addAll(new ArrayList<>(element.getAllChildrenInHierarchy(BaseQuestion.class)));
		}
		return blockDao.getFormFlowsCountUsingElement(elements) > 0;
	}

	public void setCollapsedStatus(Set<Object> collapsedStatus) {
		this.collapsedStatus = collapsedStatus;
	}

	public Set<Object> getCollapsedStatus() {
		return collapsedStatus;
	}

	/**
	 * True if exist a flow that it does not pertain to the referenced block but
	 * points from/to an element of the referenced block.
	 * 
	 * @param elementOfReferencedBlock
	 * @return
	 */
	public boolean existExternalFlowToReferencedElementOrItsChildren(TreeObject elementOfReferencedBlock, BlockReference blockReference) {
		Set<Flow> flows = getCompleteFormView().getFlows();
		for (Flow flow : flows) {
			// Flow uses the element.
			if (flow.getOrigin().equals(elementOfReferencedBlock)) {
				// Flow cames from the element and goes outside of the block.
				if (flow.getDestiny() != null && blockReference.getReference() != null) {
					boolean outsideOfBlock = true;
					for (StorableObject object : blockReference.getReference().getAllInnerStorableObjects()) {
						if (object instanceof TreeObject && ((TreeObject) object).getOriginalReference().equals(flow.getDestiny().getOriginalReference())) {
							outsideOfBlock = false;
						}
					}
					if (outsideOfBlock) {
						return true;
					}
				}
			} else if (flow.getDestiny().equals(elementOfReferencedBlock)) {
				// Flow cames from the element and comes from outside of the
				// block.
				if (flow.getOrigin() != null && blockReference.getReference() != null) {
					boolean outsideOfBlock = true;
					for (StorableObject object : blockReference.getReference().getAllInnerStorableObjects()) {
						if (object instanceof TreeObject && ((TreeObject) object).getOriginalReference().equals(flow.getOrigin().getOriginalReference())) {
							outsideOfBlock = false;
						}
					}
					if (outsideOfBlock) {
						return true;
					}
				}
			}
		}
		for (TreeObject child : elementOfReferencedBlock.getChildren()) {
			if (existExternalFlowToReferencedElementOrItsChildren(child, blockReference)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * True if the user has explicitly defined a flow that uses this element.
	 * 
	 * @param element
	 * @return
	 */
	public boolean existDefinedFlowToReferencedElementOrItsChildren(TreeObject element) {
		Set<Flow> flows = getCompleteFormView().getForm().getFlows();
		for (Flow flow : flows) {
			// Flow uses the element.
			if (flow.getOrigin().equals(element)) {
				return true;
			} else if (flow.getDestiny().equals(element)) {
				return true;
			}
			for (Token token : flow.getComputedCondition()) {
				if (token instanceof TokenWithQuestion) {
					if (((TokenWithQuestion) token).getQuestion().equals(element)) {
						return true;
					}
				}
			}
		}
		for (TreeObject child : element.getChildren()) {
			if (existDefinedFlowToReferencedElementOrItsChildren(child)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns abcd simpleViewForm linked to form using it's name, version and
	 * organizationId.
	 * 
	 * @param form
	 * @return
	 */
	public List<com.biit.abcd.persistence.entity.SimpleFormView> getLinkedSimpleFormViewsFromAbcd(Form form) {
		List<com.biit.abcd.persistence.entity.SimpleFormView> linkedSimpleAbcdForms = new ArrayList<>();
		if (form.getLabel() != null && form.getOrganizationId() != null) {
			List<com.biit.abcd.persistence.entity.SimpleFormView> views = getAllSimpleFormViewsFromAbcdByLabelAndOrganization(form.getLinkedFormLabel(),
					form.getLinkedFormOrganizationId());
			if (views != null) {
				for (com.biit.abcd.persistence.entity.SimpleFormView view : views) {
					if (form.getLinkedFormVersions().contains(view.getVersion())) {
						linkedSimpleAbcdForms.add(view);
					}
				}
			}
		}
		return linkedSimpleAbcdForms;
	}

	/**
	 * Removes form from db with formId as id.
	 * 
	 * @param formId
	 * @throws ElementCannotBeRemovedException
	 * @throws FormIsUsedAsReferenceException
	 */
	public void removeForm(Long formId) throws ElementCannotBeRemovedException, FormIsUsedAsReferenceException {
		Form selectedForm;
		if (formId != null) {
			// Checks is other form is using the selected one as a reference.
			if (getFormsUsesAsReference(formId).isEmpty()) {
				selectedForm = formDao.get(formId);
				if (selectedForm != null) {
					// Remove the form.
					formDao.makeTransient(selectedForm);
					WebformsUiLogger.info(this.getClass().getName(),
							"User '" + UserSession.getUser().getEmailAddress() + "' has removed form '" + selectedForm.getLabel() + "' (version "
									+ selectedForm.getVersion() + ").");

				}
			} else {
				throw new FormIsUsedAsReferenceException(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_LINKED_FORM_DESCRIPTION.translation());
			}
		}
	}

	private Set<SimpleFormView> getFormsUsesAsReference(Long formId) {
		Set<SimpleFormView> formsThatReference = new HashSet<>();
		for (SimpleFormView simpleFormView : simpleFormDaoWebforms.getAll()) {
			if (simpleFormView.getFormReferenceId() != null && simpleFormView.getFormReferenceId().equals(formId)) {
				formsThatReference.add(simpleFormView);
			}
		}
		return formsThatReference;
	}

	public Set<Webservice> getAllWebservices() {
		Set<Webservice> webservices = new HashSet<Webservice>();
		webservices.addAll(webserviceDao.getAll());
		return webservices;
	}

	public WebserviceCall generateNewWebserviceCall(String name, Webservice webservice) {
		WebserviceCall call = new WebserviceCall(name, webservice);
		getCompleteFormView().addWebserviceCall(call);
		return call;
	}

	public void removeWebserviceCall(WebserviceCall call) {
		getCompleteFormView().removeWebserviceCall(call);
	}

	public Webservice findWebservice(String webserviceName) throws WebserviceNotFoundException {
		return webserviceDao.findWebservice(webserviceName);
	}
}
