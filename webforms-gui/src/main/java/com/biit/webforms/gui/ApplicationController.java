package com.biit.webforms.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.security.AbcdAuthorizationService;
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
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.utils.validation.ValidateReport;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.authentication.exception.BadAbcdLink;
import com.biit.webforms.authentication.exception.CategoryWithSameNameAlreadyExistsInForm;
import com.biit.webforms.authentication.exception.DestinyIsContainedAtOrigin;
import com.biit.webforms.authentication.exception.EmptyBlockCannotBeInserted;
import com.biit.webforms.authentication.exception.NewVersionWithoutFinalDesignException;
import com.biit.webforms.authentication.exception.NotEnoughRightsToChangeStatusException;
import com.biit.webforms.authentication.exception.NotValidAbcdForm;
import com.biit.webforms.authentication.exception.SameOriginAndDestinationException;
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
import com.biit.webforms.gui.webpages.floweditor.WindowFlow;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.IWebformsBlockView;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.biit.webforms.utils.conversor.ConversorAbcdFormToForm;
import com.biit.webforms.validators.ValidateFormAbcdCompatibility;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

public class ApplicationController {
	private User user;

	private IFormDao formDao;
	private IBlockDao blockDao;
	private com.biit.abcd.persistence.dao.IFormDao formDaoAbcd;
	private ISimpleFormViewDao simpleFormDaoAbcd;
	private com.biit.webforms.persistence.dao.ISimpleFormViewDao simpleFormDaoWebforms;

	private Form lastEditedForm;
	private Form formInUse;
	private CompleteFormView completeFormView;

	private boolean unsavedFormChanges = false;
	private Set<Object> collapsedStatus;

	public ApplicationController() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("webformsFormDao");
		blockDao = (IBlockDao) helper.getBean("blockDao");
		formDaoAbcd = (com.biit.abcd.persistence.dao.IFormDao) helper.getBean("formDaoAbcd");
		simpleFormDaoAbcd = ((ISimpleFormViewDao) helper.getBean("simpleFormDaoAbcd"));
		simpleFormDaoWebforms = ((com.biit.webforms.persistence.dao.ISimpleFormViewDao) helper
				.getBean("simpleFormDaoWebforms"));
	}

	/**
	 * User action to create a form on memory no persistence is done. Needs a unique name where name.length() < 190
	 * characters.
	 * 
	 * @param formLabel
	 * @return
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 * @throws UnexpectedDatabaseException
	 */
	public Form createForm(String formLabel, Long organizationId) throws FieldTooLongException,
			FormWithSameNameException, CharacterNotAllowedException, UnexpectedDatabaseException {
		logInfoStart("createForm", formLabel, organizationId);

		// Create new form
		Form newform = null;
		try {
			newform = new Form(formLabel, getUser(), organizationId);
		} catch (FieldTooLongException | CharacterNotAllowedException ex) {
			WebformsLogger.severe(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' createForm '" + ex.getMessage() + "'.");
			throw ex;
		}

		// Check if database contains a form with the same name.
		if (formDao.exists(formLabel, organizationId)) {
			FormWithSameNameException ex = new FormWithSameNameException("Form with name '" + formLabel
					+ "' already exists.");
			WebformsLogger.severe(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' createForm " + ex.getMessage());
			throw ex;
		}

		return newform;
	}

	public Form importFormFromJson(String json, String formLabel, Long organizationId)
			throws FormWithSameNameException, UnexpectedDatabaseException, FieldTooLongException,
			ElementCannotBePersistedException {
		// Check if database contains a form with the same name.
		if (formDao.exists(formLabel, organizationId)) {
			FormWithSameNameException ex = new FormWithSameNameException("Form with name: " + formLabel
					+ " already exists");
			WebformsLogger.severe(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' createForm " + ex.getMessage());
			throw ex;
		}

		Form newForm = Form.fromJson(json);
		newForm.resetIds();
		newForm.setOrganizationId(organizationId);
		newForm.setLabel(formLabel);
		newForm.resetUserTimestampInfo(UserSessionHandler.getUser().getUserId());

		formDao.makePersistent(newForm);
		return formInUse;
	}

	/**
	 * User action to create a form. Needs a unique name where name.length() < 190 characters.
	 * 
	 * @param formLabel
	 * @return
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBePersistedException
	 */
	public Form createFormAndPersist(String formLabel, Long organizationId) throws FieldTooLongException,
			FormWithSameNameException, CharacterNotAllowedException, UnexpectedDatabaseException,
			ElementCannotBePersistedException {
		logInfoStart("createFormAndPersist", formLabel, organizationId);

		// Create new form
		Form newform = createForm(formLabel, organizationId);

		// Persist form.
		try {
			formDao.makePersistent(newform);
		} catch (ConstraintViolationException cve) {
			WebformsLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}

		return newform;
	}

	public Block createBlock(String blockName, Long organizationId) throws CharacterNotAllowedException,
			FieldTooLongException, FormWithSameNameException, UnexpectedDatabaseException,
			ElementCannotBePersistedException {
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress() + "' createBlock "
				+ blockName);
		logInfoStart("createBlock", blockName, organizationId);

		// Create new block
		Block newBlock = null;
		try {
			newBlock = new Block(blockName, getUser(), organizationId);
		} catch (FieldTooLongException | CharacterNotAllowedException ex) {
			WebformsLogger.warning(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' createBlock " + ex.getMessage());
			throw ex;
		}

		// Check if database contains a form with the same name.
		if (blockDao.getBlock(blockName, organizationId) != null) {
			FormWithSameNameException ex = new FormWithSameNameException("Block with name '" + blockName
					+ "' already exists");
			WebformsLogger.warning(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' createBlock " + ex.getMessage());
			throw ex;
		}

		// Persist block.
		try {
			blockDao.makePersistent(newBlock);
		} catch (ConstraintViolationException cve) {
			WebformsLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}
		return newBlock;
	}

	public com.biit.abcd.persistence.entity.Form getAbcdForm(Long id) throws UnexpectedDatabaseException {
		return formDaoAbcd.get(id);
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
	public Form importAbcdForm(IBaseFormView simpleFormView, String importLabel, Long organizationId)
			throws NotValidAbcdForm, FieldTooLongException, FormWithSameNameException, CharacterNotAllowedException,
			UnexpectedDatabaseException, ElementIsReadOnly, ElementCannotBePersistedException {
		logInfoStart("importAbcdForm", simpleFormView, importLabel, organizationId);

		// Try to create a new form with the name and the organization
		Form webformsForm = createForm(importLabel, organizationId);

		// Read the original form and validate.
		com.biit.abcd.persistence.entity.Form abcdForm;
		abcdForm = formDaoAbcd.get(simpleFormView.getId());

		ValidateBaseForm validator = new ValidateBaseForm();
		ValidateReport report = new ValidateReport();
		if (!validator.validate(abcdForm, report)) {
			WebformsLogger.warning(this.getClass().getName(), "Import from Abcd failed. - Form not validated");
			WebformsLogger.warning(this.getClass().getName(), report.getReport());
			throw new NotValidAbcdForm();
		}

		ConversorAbcdFormToForm conversor = new ConversorAbcdFormToForm();
		Form webformsConvertedForm = conversor.convert(abcdForm);
		try {
			webformsForm.addChildren(webformsConvertedForm.getChildren());
		} catch (NotValidChildException e) {
			// Should not happen.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		Set<IBaseFormView> linkedForms = new HashSet<IBaseFormView>();
		linkedForms.add(simpleFormView);
		webformsForm.setLinkedForms(linkedForms);

		// Store on the database
		formDao.makePersistent(webformsForm);

		return webformsForm;
	}

	/**
	 * Returns the List of Abcd Forms linked to a form or empty list if there are no links.
	 * 
	 * @param form
	 * @return
	 * @throws UnexpectedDatabaseException
	 * @throws BadAbcdLink
	 */
	public List<com.biit.abcd.persistence.entity.Form> getLinkedAbcdForm(Form form) throws UnexpectedDatabaseException,
			BadAbcdLink {
		List<com.biit.abcd.persistence.entity.Form> linkedForms = new ArrayList<>();
		if (form != null && form.getLinkedFormLabel() != null && form.getLinkedFormOrganizationId() != null) {
			for (Integer version : form.getLinkedFormVersions()) {
				com.biit.abcd.persistence.entity.Form abcdForm = formDaoAbcd.getForm(form.getLinkedFormLabel(),
						version, form.getLinkedFormOrganizationId());
				if (abcdForm == null) {
					throw new BadAbcdLink();
				}
				linkedForms.add(abcdForm);
			}
		}
		return linkedForms;
	}

	/**
	 * Returns abcd simpleViewForm linked to form using it's name, version and organizationId.
	 * 
	 * @param form
	 * @return
	 */
	public List<com.biit.abcd.persistence.entity.SimpleFormView> getLinkedSimpleAbcdForms(Form form) {
		List<com.biit.abcd.persistence.entity.SimpleFormView> linkedSimpleAbcdForms = new ArrayList<>();
		if (form.getLabel() != null && form.getOrganizationId() != null) {
			List<com.biit.abcd.persistence.entity.SimpleFormView> views = simpleFormDaoAbcd
					.getSimpleFormViewByLabelAndOrganization(form.getLinkedFormLabel(),
							form.getLinkedFormOrganizationId());
			for (com.biit.abcd.persistence.entity.SimpleFormView view : views) {
				if (form.getLinkedFormVersions().contains(view.getVersion())) {
					linkedSimpleAbcdForms.add(view);
				}
			}
		}
		return linkedSimpleAbcdForms;
	}

	public Form createNewFormVersion(Form form) throws NewVersionWithoutFinalDesignException,
			NotValidStorableObjectException, CharacterNotAllowedException, UnexpectedDatabaseException,
			ElementCannotBePersistedException {
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
				+ "' createNewFormVersion " + form);

		if (form.getStatus() == FormWorkStatus.DESIGN) {
			WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' createNewFormVersion " + form + " tried to create a new form version that is still in design");
			throw new NewVersionWithoutFinalDesignException();
		}

		Form newFormVersion;
		try {
			newFormVersion = form.createNewVersion(getUser());
		} catch (CharacterNotAllowedException | NotValidStorableObjectException ex) {
			WebformsLogger.severe(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' createForm " + ex.getMessage());
			throw ex;
		}

		try {
			formDao.makePersistent(newFormVersion);
		} catch (ConstraintViolationException cve) {
			WebformsLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}
		return newFormVersion;
	}

	public void changeFormDescription(Form form, String text) throws FieldTooLongException,
			UnexpectedDatabaseException, ElementCannotBePersistedException {
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
				+ "' changeFormDescription " + form + " " + text);
		try {
			form.setDescription(text);
			formDao.makePersistent(form);
		} catch (FieldTooLongException e) {
			WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' changeFormDescription " + e.getMessage());
			throw e;
		}
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		if (user != null) {
			return user;
		} else {
			WebformsLogger.severe(ApplicationController.class.getSimpleName(),
					"Application controller method accessed without user.");
			return null;
		}
	}

	public void setFormInUse(Form form) {
		if (formInUse != null) {
			// Release current form if any.
			collapsedStatus = null;
			UiAccesser.releaseForm(formInUse, user);
		}
		if (form == null) {
			formInUse = null;
			collapsedStatus = null;
		} else {
			formInUse = form;
			collapsedStatus = null;
			// Lock new form
			UiAccesser.lockForm(formInUse, user);
			WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' setFormInUse Form '" + formInUse + "'.");
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

	private String getUserEmailAddress() {
		return getUser() != null ? getUser().getEmailAddress() : "";
	}

	public void clearFormInUse() {
		if (formInUse != null) {
			WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' clearFormInUse");
			UiAccesser.releaseForm(formInUse, user);
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
		return (Category) insertTreeObject(Category.class, getFormInUse(), "Category");
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
		return (Group) insertTreeObject(Group.class, parent, "Group");
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
		return (Question) insertTreeObject(Question.class, parent, "Question");
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
		return (SystemField) insertTreeObject(SystemField.class, parent, "SystemField");
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
		return (Text) insertTreeObject(Text.class, parent, "Text");
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
		return (Answer) insertTreeObject(Answer.class, parent, "Answer");
	}

	/**
	 * Creates any kind of TreeObject descendant with @name and inserts into parent if possible.
	 * 
	 * @param classType
	 * @param parent
	 * @param name
	 * @return
	 * @throws NotValidChildException
	 * @throws ElementIsReadOnly
	 */
	public TreeObject insertTreeObject(Class<? extends TreeObject> classType, TreeObject parent, String name)
			throws NotValidChildException, ElementIsReadOnly {

		// Block references cannot have new childs.
		if (parent instanceof BlockReference || parent.isReadOnly()) {
			throw new NotValidChildException("Block References are read only and cannot be modified. ");
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
				+ "' insertTreeObject of type '" + classType.getName() + "' to '" + parent + "' with name '" + name
				+ "' START");

		TreeObject treeObject = null;
		try {
			treeObject = classType.newInstance();
			String nameNumber = getNewStringNumber(parent, name);
			treeObject.setName(nameNumber);
			treeObject.setLabel(nameNumber);
			treeObject.setCreatedBy(UserSessionHandler.getUser());
			treeObject.setUpdatedBy(UserSessionHandler.getUser());
			parent.addChild(treeObject);
			setUnsavedFormChanges(true);
			WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
					+ "' inserted '" + treeObject + "' into '" + parent + "' ('" + parent.getPathName() + "')");
		} catch (FieldTooLongException | InstantiationException | IllegalAccessException | CharacterNotAllowedException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		} catch (NotValidChildException e) {
			WebformsLogger.severe(this.getClass().getName(), "Element of type '" + classType.getName()
					+ "' could not be inserted in '" + parent.getPathName() + "'");
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
	public void removeTreeObject(TreeObject row) throws DependencyExistException, ChildrenNotFoundException,
			ElementIsReadOnly {
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress()
				+ "' removeTreeObject " + row + " of " + row.getAncestor(Form.class));

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
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress() + "' move Up "
				+ row + " START");
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
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress() + "' move Down "
				+ row);
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
		Form savedForm = saveForm(formInUse);
		formInUse = savedForm;
		setLastEditedForm(formInUse);
		completeFormView = new CompleteFormView(getFormInUse());
	}

	@Transactional
	public Form saveForm(Form form) throws UnexpectedDatabaseException, ElementCannotBePersistedException {
		form.setUpdatedBy(getUser());
		form.setUpdateTime();

		Form mergedForm = form;

		if (form instanceof Block) {
			if (!blockDao.getEntityManager().contains(form)) {
				mergedForm = blockDao.merge((Block) form);
			} else {
				blockDao.makePersistent((Block) mergedForm);
			}
		} else {
			if (!formDao.getEntityManager().contains(form)) {
				mergedForm = formDao.merge(form);
			} else {
				formDao.makePersistent(mergedForm);
			}
		}
		setUnsavedFormChanges(false);
		return mergedForm;
	}

	public void finishForm(Form form) throws UnexpectedDatabaseException, ElementCannotBePersistedException {
		logInfoStart("finishForm", form);
		form.setStatus(FormWorkStatus.FINAL_DESIGN);
		lockLinkedBlocks(form);
		setUnsavedFormChanges(false);
		saveForm(form);
	}

	public void saveAsBlock(TreeObject element, String blockLabel, Long organizationId) throws FieldTooLongException,
			FormWithSameNameException, UnexpectedDatabaseException, ElementCannotBeRemovedException,
			ElementCannotBePersistedException {
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

			block.setUpdatedBy(getUser());
			block.setUpdateTime();
			blockDao.merge(block);
		} catch (NotValidChildException | CharacterNotAllowedException | NotValidStorableObjectException e) {
			// Impossible, still, if fails remove block.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			if (block != null) {
				blockDao.makeTransient(block);
			}
		}
	}

	public void updateForm(Form form, String label, String description) {
		try {
			if (!form.getLabel().equals(label) || !form.getDescription().equals(description)) {
				setUnsavedFormChanges(true);
				logInfoStart("updateForm", form, label, description);
			}
			form.setDescription(description);
			form.setLabel(label);
			form.setUpdatedBy(UserSessionHandler.getUser());
			form.setUpdateTime();
		} catch (FieldTooLongException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public void updateAnswer(Answer answer, String value, String label, String description) {
		try {
			if (!answer.getLabel().equals(label) || !answer.getDescription().equals(description)
					|| !answer.getValue().equals(value)) {
				setUnsavedFormChanges(true);
				logInfoStart("updateAnswer", answer, value, label, description);
			}

			answer.setValue(value);
			answer.setLabel(label);
			answer.setDescription(description);
			answer.setUpdatedBy(UserSessionHandler.getUser());
			answer.setUpdateTime();
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public void updateCategory(Category category, String name, String label) {
		try {
			if (!category.getName().equals(label) || !category.getLabel().equals(label)) {
				setUnsavedFormChanges(true);
				logInfoStart("updateCategory", category, name, label);
			}

			category.setName(name);
			category.setLabel(label);
			category.setUpdatedBy(UserSessionHandler.getUser());
			category.setUpdateTime();
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public void updateGroup(Group group, String name, String label, boolean repeatable) {
		try {
			if (!group.getName().equals(name) || !group.getLabel().equals(label) || group.isRepeatable() != repeatable) {
				setUnsavedFormChanges(true);
				logInfoStart("updateGroup", group, name, label, repeatable);
			}

			group.setName(name);
			group.setLabel(label);
			group.setRepeatable(repeatable);
			group.setUpdatedBy(UserSessionHandler.getUser());
			group.setUpdateTime();
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public void updateQuestion(Question question, String name, String label, String description, boolean mandatory,
			AnswerType answerType, AnswerFormat answerFormat, AnswerSubformat answerSubformat, boolean horizontal) {
		try {
			if (!question.getLabel().equals(label)
					|| !question.getDescription().equals(description)
					|| !question.getName().equals(name)
					|| question.isMandatory() != mandatory
					|| (question.getAnswerType() != null && !question.getAnswerType().equals(answerType))
					|| (question.getAnswerFormat() != null && !question.getAnswerFormat().equals(answerFormat))
					|| (question.getAnswerSubformat() != null && !question.getAnswerSubformat().equals(answerSubformat))
					|| question.isHorizontal() != horizontal) {
				setUnsavedFormChanges(true);
				logInfoStart("updateQuestion", question, name, label, description, mandatory, answerType, answerFormat,
						answerSubformat, horizontal);
			}

			question.setName(name);
			question.setLabel(label);
			question.setDescription(description);
			question.setAnswerType(answerType);
			question.setAnswerFormat(answerFormat);
			question.setAnswerSubformat(answerSubformat);
			question.setMandatory(mandatory);
			question.setHorizontal(horizontal);
			question.setUpdatedBy(UserSessionHandler.getUser());
			question.setUpdateTime();
		} catch (FieldTooLongException | InvalidAnswerFormatException | CharacterNotAllowedException
				| InvalidAnswerSubformatException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	/**
	 * Inserts element belonging group to current form. This generates a clone of the block using the element as
	 * hierarchy seed and introduces to current form as a new category.
	 * 
	 * @param selectedRow
	 * @throws CategoryWithSameNameAlreadyExistsInForm
	 * @throws EmptyBlockCannotBeInserted
	 * @throws ElementIsReadOnly
	 */
	public void insertBlock(TreeObject block) throws CategoryWithSameNameAlreadyExistsInForm,
			EmptyBlockCannotBeInserted, ElementIsReadOnly {
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress() + "' insert Block "
				+ formInUse + " " + block);

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
				WebformsLogger.errorMessage(this.getClass().getName(), e);
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

			formInUse.addChildren(copiedBlock.getChildren());
			formInUse.addFlows(copiedBlock.getFlows());

			setUnsavedFormChanges(true);
		} catch (NotValidStorableObjectException | NotValidChildException | CharacterNotAllowedException e) {
			// Impossible.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	/**
	 * Linked building blocks are translated to standard blocks to avoid changes after the form is finished.
	 */
	private void lockLinkedBlocks(Form form) {
		Form completeForm = form;

		// Convert to CompleteForm to use all methods of filtering hide elements.
		if (!(completeForm instanceof CompleteFormView)) {
			completeForm = new CompleteFormView(form);
		}

		// Get needed elements.
		List<TreeObject> children = ((CompleteFormView) completeForm).getAllNotHiddenChildren();
		Set<Flow> flows = ((CompleteFormView) completeForm).getFlows();
		
		// Reset the parent to the form. Force updating database to correct elements.
		((CompleteFormView) completeForm).getForm().getChildren().clear();
		((CompleteFormView) completeForm).getForm().getChildren().addAll(children);		

		// Update flow with the flow of the linked block.
		((CompleteFormView) completeForm).getForm().getFlows().clear();
		((CompleteFormView) completeForm).getForm().getFlows().addAll(flows);
		((CompleteFormView) completeForm).getForm().updateRuleReferences();
	}

	/**
	 * Inserts element belonging group to current form. This generates a clone of the block using the element as
	 * hierarchy seed and introduces to current form as a new category.
	 * 
	 * @param selectedRow
	 * @throws CategoryWithSameNameAlreadyExistsInForm
	 * @throws EmptyBlockCannotBeInserted
	 * @throws ElementIsReadOnly
	 */
	public void linkBlock(TreeObject block) throws CategoryWithSameNameAlreadyExistsInForm, EmptyBlockCannotBeInserted,
			ElementIsReadOnly {
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress() + "' link Block '"
				+ block + "' in '" + formInUse + "' ");

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
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}

			// Create a block link and insert it.
			try {
				BlockReference blockReference = new BlockReference((Block) block);
				formInUse.addChild(blockReference);
				setUnsavedFormChanges(true);
			} catch (NotValidChildException e) {
				// Impossible.
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		} else {
			MessageManager.showError(LanguageCodes.ERROR_LINK_BLOCK_NOT_COMPLETE);
		}
	}

	/**
	 * Moves a TreeObject origin to a new child position on destiny.
	 * 
	 * @param origin
	 * @param destiny
	 * @throws NotValidChildException
	 * @throws SameOriginAndDestinationException
	 * @throws DestinyIsContainedAtOrigin
	 * @throws ElementIsReadOnly
	 */
	public void moveTo(TreeObject origin, TreeObject destiny) throws NotValidChildException,
			SameOriginAndDestinationException, DestinyIsContainedAtOrigin, ChildrenNotFoundException, ElementIsReadOnly {
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress() + "' move '"
				+ origin + "' to '" + destiny + "'.");
		if (origin.equals(destiny)) {
			throw new SameOriginAndDestinationException("Origin and destination are the same element");
		}
		if (origin.contains(destiny)) {
			throw new DestinyIsContainedAtOrigin("Origin is contained inside destination element.");
		}
		if (destiny.getChild(origin.getName()) != null) {
			MessageManager.showWarning(LanguageCodes.WARNING_ELEMENT_WTIH_SAME_NAME_EXIST);
		}
		try {
			destiny.addChild(origin);
			TreeObject.move(origin, destiny);
			setUnsavedFormChanges(true);
		} catch (NotValidChildException | ChildrenNotFoundException e) {
			WebformsLogger.warning(ApplicationController.class.getName(), "User '" + getUserEmailAddress() + "' move '"
					+ origin + "' to '" + destiny + "' could not be done.");
			throw e;
		}
	}

	/**
	 * This function is called when the UI has expired. The implementation needs to free any "locked" resources
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
	 * Update flow content. This function currently is a direct call to the structure function. If the flow is not on
	 * the form, it gets added.
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
	public void updateFlowContent(Flow flow, BaseQuestion origin, FlowType flowType, BaseQuestion destiny,
			boolean others, List<Token> condition) throws BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException,
			FlowNotAllowedException {
		logInfoStart("updateFlowContent", flow, origin, flowType, destiny, others, condition);

		flow.setContent(origin, flowType, destiny, others, condition);

		if (flow.getCreatedBy() == null) {
			flow.setCreationTime();
			flow.setCreatedBy(getUser());
		}
		flow.setUpdateTime();
		flow.setUpdatedBy(getUser());
		setUnsavedFormChanges(true);

		if (!getFormInUse().containsFlow(flow)) {
			addFlowToForm(flow, getCompleteFormView());
		}
	}

	/**
	 * Updates flow update time and updated by in flow. The content of the flow was already modified by
	 * {@link WindowFlow}
	 * 
	 * @param flow
	 */
	public void updateFlow(Flow flow) {
		WebformsLogger.info(ApplicationController.class.getName(), "User '" + getUserEmailAddress() + "' updateFlow '"
				+ flow + "' in '" + getFormInUse() + "' START");
		flow.setUpdateTime();
		flow.setUpdatedBy(getUser());
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
	public Token createTokenComparationValue(TokenTypes type, Question question, AnswerSubformat valueAnswerSubformat,
			DatePeriodUnit datePeriodUnit, String value) {
		Token token = TokenComparationValue.getToken(type, question, valueAnswerSubformat, datePeriodUnit, value);
		token.setCreatedBy(UserSessionHandler.getUser());
		token.setUpdatedBy(UserSessionHandler.getUser());
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
		WebformsLogger.info(ApplicationController.class.getName(),
				getUserInfo() + " " + getFunctionInfo(functionName, parameters));
	}

	protected String getUserInfo() {
		String userInfo = new String("User '");
		if (getUser() == null) {
			return userInfo + "' NO USER";
		} else {
			return userInfo + getUser().getEmailAddress() + "'";
		}
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
		WebformsLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
				+ "' has logged out.");
		clearFormInUse();
		UserSessionHandler.logout();
		// ApplicationUi.navigateTo(WebMap.LOGIN_PAGE);
		UI.getCurrent().getPage().setLocation("./VAADIN/logout.html");
		try {
			UI.getCurrent().close();
		} catch (Exception e) {
			WebformsLogger.errorMessage(UserSessionHandler.class.getName(), e);
		}
	}

	public IFormDao getWebformsFormDao() {
		return formDao;
	}

	public IBlockDao getWebformsBlockDao() {
		return blockDao;
	}

	public com.biit.abcd.persistence.dao.IFormDao getFormDaoAbcd() {
		return formDaoAbcd;
	}

	public TreeTableProvider<com.biit.abcd.persistence.entity.Form> getTreeTableAbcdFormsProvider() {
		TreeTableProvider<com.biit.abcd.persistence.entity.Form> provider = new TreeTableProvider<com.biit.abcd.persistence.entity.Form>() {

			@Override
			public Collection<com.biit.abcd.persistence.entity.Form> getAll() throws UnexpectedDatabaseException {
				List<com.biit.abcd.persistence.entity.Form> forms = new ArrayList<>();

				Set<Organization> userOrganizations = WebformsAuthorizationService.getInstance()
						.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), WebformsActivity.READ);
				for (Organization organization : userOrganizations) {
					forms.addAll(getFormDaoAbcd().getAll(organization.getOrganizationId()));
				}
				return forms;
			}
		};

		return provider;
	}

	public TreeTableProvider<com.biit.webforms.persistence.entity.SimpleFormView> getTreeTableFormsProvider() {
		TreeTableProvider<com.biit.webforms.persistence.entity.SimpleFormView> provider = new TreeTableProvider<com.biit.webforms.persistence.entity.SimpleFormView>() {

			@Override
			public Collection<com.biit.webforms.persistence.entity.SimpleFormView> getAll()
					throws UnexpectedDatabaseException {
				List<com.biit.webforms.persistence.entity.SimpleFormView> userForms = new ArrayList<>();

				List<com.biit.webforms.persistence.entity.SimpleFormView> simpleForms = simpleFormDaoWebforms.getAll();

				Set<Organization> userOrganizations = WebformsAuthorizationService.getInstance()
						.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), WebformsActivity.READ);

				for (com.biit.webforms.persistence.entity.SimpleFormView form : simpleForms) {
					for (Organization organization : userOrganizations) {
						if (form.getOrganizationId().equals(organization.getOrganizationId())) {
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
	 * Get all forms where the user has READ permission in ABCD and EDIT permissions in Webforms.
	 * 
	 * @return
	 */
	public TreeTableProvider<SimpleFormView> getTreeTableSimpleAbcdFormsProvider() {
		TreeTableProvider<SimpleFormView> provider = new TreeTableProvider<SimpleFormView>() {

			@Override
			public Collection<SimpleFormView> getAll() {
				List<SimpleFormView> forms = new ArrayList<>();

				// Get all organizations where user has read permissions and
				// store ids in a hash map. User must be in the same
				// organizations in both ABCD and WebForms.
				Set<Organization> userOrganizationsFromAbcd = AbcdAuthorizationService.getInstance()
						.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), AbcdActivity.READ);
				HashSet<Long> userAbcdOrganizationIds = new HashSet<Long>();
				for (Organization organization : userOrganizationsFromAbcd) {
					userAbcdOrganizationIds.add(organization.getOrganizationId());
				}

				Set<Organization> userOrganizationsFromWebforms = WebformsAuthorizationService.getInstance()
						.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(),
								WebformsActivity.FORM_EDITING);
				HashSet<Long> userWebformsOrganizationIds = new HashSet<Long>();
				for (Organization organization : userOrganizationsFromWebforms) {
					userWebformsOrganizationIds.add(organization.getOrganizationId());
				}

				// Get all simple forms and add to the form list if their
				// organization id is on the organization id map.
				List<SimpleFormView> simpleForms = getSimpleFormDaoAbcd().getAll();
				for (SimpleFormView simpleForm : simpleForms) {
					if (userAbcdOrganizationIds.contains(simpleForm.getOrganizationId())
							&& userWebformsOrganizationIds.contains(simpleForm.getOrganizationId())) {
						forms.add(simpleForm);
					}
				}

				return forms;
			}
		};

		return provider;
	}

	public ISimpleFormViewDao getSimpleFormDaoAbcd() {
		return simpleFormDaoAbcd;
	}

	public void validateCompatibility(Form currentForm, IBaseFormView abcdSimpleForm) throws BadAbcdLink {
		com.biit.abcd.persistence.entity.Form abcdForm = formDaoAbcd.get(abcdSimpleForm.getId());
		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(currentForm);
		ValidateReport report = new ValidateReport();
		validator.validate(abcdForm, report);
		if (!report.isValid()) {
			throw new BadAbcdLink("Abcd form '" + abcdSimpleForm.getLabel() + "' is not a valid link for form '"
					+ currentForm.getLabel() + "'.");
		}
	}

	/**
	 * Returns all organizations where user has permission to do all the activities in activitiesFilter.
	 * 
	 * @param activitiesFilter
	 * @return
	 */
	public Set<Organization> getOrganizatiosWhereUser(IActivity... activitiesFilter) {
		try {
			Set<Organization> organizations = WebformsAuthorizationService.getInstance().getUserOrganizations(
					UserSessionHandler.getUser());
			Iterator<Organization> itr = organizations.iterator();
			while (itr.hasNext()) {
				Organization organization = itr.next();
				for (IActivity activity : activitiesFilter) {
					// If the user doesn't comply to all activities in the
					// filter in the group, then exit
					if (!WebformsAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
							organization, activity)) {
						itr.remove();
						break;
					}
				}
			}
			return organizations;
		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		return new HashSet<>();
	}

	public boolean existsUnsavedFormChanges() {
		return unsavedFormChanges;
	}

	public void setUnsavedFormChanges(boolean value) {
		unsavedFormChanges = value;
	}

	public com.biit.webforms.persistence.dao.ISimpleFormViewDao getSimpleFormDaoWebforms() {
		return simpleFormDaoWebforms;
	}

	public Form loadForm(IWebformsFormView formView) {
		return formDao.get(formView.getId());
	}

	public Block loadBlock(IWebformsBlockView blockView) {
		return blockDao.get(blockView.getId());
	}

	public List<com.biit.webforms.persistence.entity.SimpleFormView> getSimpleFormVersionsWebforms(String label,
			Long organizationId) {
		List<com.biit.webforms.persistence.entity.SimpleFormView> filteredForms = new ArrayList<>();
		List<com.biit.webforms.persistence.entity.SimpleFormView> forms = simpleFormDaoWebforms.getAll();

		for (com.biit.webforms.persistence.entity.SimpleFormView form : forms) {
			if (form.getLabel().equals(label) && form.getOrganizationId().equals(organizationId)) {
				filteredForms.add(form);
			}
		}
		return filteredForms;
	}

	public void changeFormStatus(IWebformsFormView formView, FormWorkStatus value)
			throws NotEnoughRightsToChangeStatusException, ElementCannotBePersistedException {
		// Can downgrade
		boolean userCanDowngradeStatus = WebformsAuthorizationService.getInstance().isAuthorizedActivity(
				UserSessionHandler.getUser(), formView, WebformsActivity.FORM_STATUS_DOWNGRADE);

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
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}
	}

	public void evictAllCache() {
		formDao.evictAllCache();
		blockDao.evictAllCache();
		formDaoAbcd.evictAllCache();
		clearParameters();
	}

	public void clearParameters() {
		formInUse = null;
		completeFormView = null;
	}

	/**
	 * Defines if any form is using an element in its flow (as origin or destination).
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
		elements.add(element);
		return blockDao.getFormFlowsCountUsingElement(elements) > 0;
	}

	public void setCollapsedStatus(Set<Object> collapsedStatus) {
		this.collapsedStatus = collapsedStatus;
	}
	
	public Set<Object> getCollapsedStatus(){
		return collapsedStatus;
	}
}
