package com.biit.webforms.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.validators.ValidateBaseForm;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.utils.validation.ValidateReport;
import com.biit.webforms.authentication.exception.CategoryWithSameNameAlreadyExistsInForm;
import com.biit.webforms.authentication.exception.DestinyIsContainedAtOrigin;
import com.biit.webforms.authentication.exception.NewVersionWithoutFinalDesignException;
import com.biit.webforms.authentication.exception.NotValidAbcdForm;
import com.biit.webforms.authentication.exception.SameOriginAndDestinationException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.gui.common.components.TreeTableProvider;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.gui.webpages.floweditor.WindowFlow;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.biit.webforms.utils.conversor.ConversorAbcdFormToForm;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.vaadin.server.VaadinServlet;

public class ApplicationController {

	private User user;

	private IFormDao formDao;
	private IBlockDao blockDao;
	private com.biit.abcd.persistence.dao.IFormDao formDaoAbcd;
	private ISimpleFormViewDao simpleFormDaoAbcd;

	private Form lastEditedForm;
	private Form formInUse;

	public ApplicationController() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
		blockDao = (IBlockDao) helper.getBean("blockDao");
		formDaoAbcd = (com.biit.abcd.persistence.dao.IFormDao) helper.getBean("formDaoAbcd");
		simpleFormDaoAbcd = ((ISimpleFormViewDao) helper.getBean("simpleFormDaoAbcd"));
	}

	/**
	 * User action to create a form on memory no persistance is done. Needs a
	 * unique name where name.length() < 190 characters.
	 * 
	 * @param formLabel
	 * @return
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 */
	public Form createForm(String formLabel, Organization organization) throws FieldTooLongException,
			FormWithSameNameException, CharacterNotAllowedException {
		logInfoStart("createForm", formLabel, organization);

		// Create new form
		Form newform = null;
		try {
			newform = new Form(formLabel, getUser(), organization);
		} catch (FieldTooLongException | CharacterNotAllowedException ex) {
			WebformsLogger.severe(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " createForm " + ex.getMessage());
			throw ex;
		}

		// Check if database contains a form with the same name.
		if (formDao.getForm(formLabel, organization.getOrganizationId()) != null) {
			FormWithSameNameException ex = new FormWithSameNameException("Form with name: " + formLabel
					+ " already exists");
			WebformsLogger.severe(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " createForm " + ex.getMessage());
			throw ex;
		}

		logInfoEnd("createForm", formLabel, organization);
		return newform;
	}

	/**
	 * User action to create a form. Needs a unique name where name.length() <
	 * 190 characters.
	 * 
	 * @param formLabel
	 * @return
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 */
	public Form createFormAndPersist(String formLabel, Organization organization) throws FieldTooLongException,
			FormWithSameNameException, CharacterNotAllowedException {
		logInfoStart("createFormAndPersist", formLabel, organization);

		// Create new form
		Form newform = createForm(formLabel, organization);

		// Persist form.
		try {
			formDao.makePersistent(newform);
		} catch (ConstraintViolationException cve) {
			WebformsLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}

		logInfoEnd("createFormAndPersist", formLabel, organization);
		return newform;
	}

	public Block createBlock(String blockName, Organization organization) throws FieldTooLongException,
			CharacterNotAllowedException, FormWithSameNameException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createBlock " + blockName + " START");

		// Create new block
		Block newBlock = null;
		try {
			newBlock = new Block(blockName, getUser(), organization);
		} catch (FieldTooLongException | CharacterNotAllowedException ex) {
			WebformsLogger.warning(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " createBlock " + ex.getMessage());
			throw ex;
		}

		// Check if database contains a form with the same name.
		if (blockDao.getBlock(blockName, organization) != null) {
			FormWithSameNameException ex = new FormWithSameNameException("Block with name: " + blockName
					+ " already exists");
			WebformsLogger.warning(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " createBlock " + ex.getMessage());
			throw ex;
		}

		// Persist form.
		try {
			blockDao.makePersistent(newBlock);
		} catch (ConstraintViolationException cve) {
			WebformsLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createBlock " + blockName + " END");
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
	 */
	public Form importAbcdForm(SimpleFormView simpleFormView, String importLabel, Organization organization)
			throws NotValidAbcdForm, FieldTooLongException, FormWithSameNameException, CharacterNotAllowedException {
		logInfoStart("importAbcdForm", simpleFormView, importLabel, organization);

		// Try to create a new form with the name and the organization
		Form webformsForm = createForm(importLabel, organization);

		// Read the original form and validate.
		com.biit.abcd.persistence.entity.Form abcdForm = formDaoAbcd.read(simpleFormView.getId());
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
		webformsForm.setLinkedFormLabel(simpleFormView.getLabel());
		webformsForm.addLinkedFormVersions(simpleFormView.getVersion());
		webformsForm.setLinkedFormOrganizationId(simpleFormView.getOrganizationId());

		// Store on the database
		formDao.makePersistent(webformsForm);

		logInfoEnd("importAbcdForm", simpleFormView, importLabel, organization);

		return webformsForm;
	}

	/**
	 * Returns the Abcd Form linked to a form or null value if it is not linked
	 * or the link is wrong.
	 * 
	 * @param form
	 * @return
	 */
	public com.biit.abcd.persistence.entity.Form getLinkedAbcdForm(Form form) {
		if (form.getLabel() != null && form.getOrganizationId() != null) {
			return formDaoAbcd.getForm(form.getLabel(), form.getVersion(), form.getOrganizationId());
		}
		return null;
	}

	/**
	 * Returns abcd simpleViewForm linked to form using it's name, version and
	 * organizationId.
	 * 
	 * @param form
	 * @return
	 */
	public SimpleFormView getLinkedSimpleAbcdForm(Form form) {
		if (form.getLabel() != null && form.getOrganizationId() != null) {
			List<SimpleFormView> views = simpleFormDaoAbcd.getSimpleFormViewByName(form.getLinkedFormLabel());
			for (SimpleFormView view : views) {
				if (form.getLinkedFormVersions().contains(view.getVersion())
						&& view.getOrganizationId().equals(form.getLinkedFormOrganizationId())) {
					return view;
				}
			}
		}
		System.out.println("Return: null");
		return null;
	}

	public Form createNewFormVersion(Form form) throws NewVersionWithoutFinalDesignException,
			NotValidStorableObjectException, CharacterNotAllowedException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createNewFormVersion " + form + " START");

		if (form.getStatus() == FormWorkStatus.DESIGN) {
			WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " createNewFormVersion " + form + " tried to create a new form version that is still in design");
			throw new NewVersionWithoutFinalDesignException();
		}

		Form newFormVersion;
		try {
			newFormVersion = form.createNewVersion(getUser());
		} catch (CharacterNotAllowedException | NotValidStorableObjectException ex) {
			WebformsLogger.severe(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " createForm " + ex.getMessage());
			throw ex;
		}

		try {
			formDao.makePersistent(newFormVersion);
		} catch (ConstraintViolationException cve) {
			WebformsLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createNewFormVersion " + form + " END");
		return newFormVersion;
	}

	public void changeFormDescription(Form form, String text) throws FieldTooLongException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " changeFormDescription " + form + " " + text + " START");
		try {
			form.setDescription(text);
			formDao.makePersistent(form);
		} catch (FieldTooLongException e) {
			WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " changeFormDescription " + e.getMessage());
			throw e;
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " changeFormDescription " + form + " " + text + " END");
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
			UiAccesser.releaseForm(formInUse, user);
		}
		// Lock new form
		UiAccesser.lockForm(form, user);

		if (form instanceof Block) {
			WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " setFormInUse Block:" + form);
		} else {
			WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " setFormInUse Form: " + form);
		}

		this.formInUse = form;
		setLastEditedForm(form);
	}

	public Form getFormInUse() {
		return formInUse;
	}

	public Form getLastEditedForm() {
		return lastEditedForm;
	}

	private void setLastEditedForm(Form form) {
		this.lastEditedForm = form;
	}

	private String getUserEmailAddress() {
		return getUser() != null ? getUser().getEmailAddress() : "NO_USER";
	}

	public void clearFormInUse() {
		if (formInUse != null) {
			WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUserEmailAddress()
					+ " clearFormInUse");
			UiAccesser.releaseForm(formInUse, user);
			formInUse = null;
		}
	}

	/**
	 * Adds new category to current form
	 * 
	 * @return
	 * @throws NotValidChildException
	 */
	public Category addNewCategory() {
		try {
			return (Category) insertTreeObject(Category.class, getFormInUse(), "new_category");
		} catch (NotValidChildException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	/**
	 * Adds new group to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public Group addNewGroup(TreeObject parent) throws NotValidChildException {
		return (Group) insertTreeObject(Group.class, parent, "new_group");
	}

	/**
	 * Adds new question to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public Question addNewQuestion(TreeObject parent) throws NotValidChildException {
		return (Question) insertTreeObject(Question.class, parent, "new_question");
	}

	/**
	 * Adds new System field
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public SystemField addNewSystemField(TreeObject parent) throws NotValidChildException {
		return (SystemField) insertTreeObject(SystemField.class, parent, "new_system_field");
	}

	/**
	 * Adds new Text to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public Text addNewText(TreeObject parent) throws NotValidChildException {
		return (Text) insertTreeObject(Text.class, parent, "new_text");
	}

	/**
	 * Adds new answer to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public Answer addNewAnswer(TreeObject parent) throws NotValidChildException {
		return (Answer) insertTreeObject(Answer.class, parent, "new_answer");
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
	 */
	public TreeObject insertTreeObject(Class<? extends TreeObject> classType, TreeObject parent, String name)
			throws NotValidChildException {

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " insertTreeObject of type '" + classType.getName() + "' to '" + parent + "' with name '" + name
				+ "' START");

		TreeObject treeObject = null;
		try {
			treeObject = classType.newInstance();
			String nameNumber = getNewStringNumber(parent, name);
			treeObject.setName(nameNumber);
			treeObject.setLabel(nameNumber + "_label");
			treeObject.setCreatedBy(UserSessionHandler.getUser());
			treeObject.setUpdatedBy(UserSessionHandler.getUser());
			parent.addChild(treeObject);
			WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " inserted '" + treeObject + "' into '" + parent + "'");
		} catch (FieldTooLongException | InstantiationException | IllegalAccessException | CharacterNotAllowedException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		} catch (NotValidChildException e) {
			WebformsLogger.severe(this.getClass().getName(), "Element of type '" + classType.getName()
					+ "' could not be inserted in '" + parent + "'");
			throw e;
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " insertTreeObject of type '" + classType.getName() + "' to '" + parent + "' with name '" + name
				+ "' END");

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
					// Other, extract number (newString-int)
					String value = child.getName().substring(newString.length() + 1);
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
			return newString + "_" + (maxNewString + 1);
		}
	}

	/**
	 * Removes a element of a tree.
	 * 
	 * @param row
	 * @throws DependencyExistException
	 */
	public void removeTreeObject(TreeObject row) throws DependencyExistException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " removeTreeObject " + row + " of " + row.getAncestor(Form.class) + " START");

		row.remove();

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " removeTreeObject " + row + " of " + row.getAncestor(Form.class) + " END");
	}

	/**
	 * Moves a element of a tree object up.
	 * 
	 * @param row
	 */
	public void moveUp(TreeObject row) {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress() + " moveUp "
				+ row + " START");
		if (row.getParent() != null) {
			TreeObject parent = row.getParent();
			int index = parent.getChildren().indexOf(row);
			if (index > 0) {
				parent.getChildren().remove(index);
				parent.getChildren().add(index - 1, row);
			}
		}
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress() + " moveUp "
				+ row + " END");
	}

	/**
	 * Moves a element of a tree object down.
	 * 
	 * @param row
	 */
	public void moveDown(TreeObject row) {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " moveDown " + row + " START");
		if (row.getParent() != null) {
			TreeObject parent = row.getParent();
			int index = parent.getChildren().indexOf(row);
			if (index < parent.getChildren().size() - 1) {
				parent.getChildren().remove(index);
				parent.getChildren().add(index + 1, row);
			}
		}
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress() + " moveUp "
				+ row + " END");
	}

	public void saveForm() {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveForm " + formInUse + " START");

		formInUse.setUpdatedBy(getUser());
		formInUse.setUpdateTime();
		formDao.makePersistent(formInUse);

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveForm " + formInUse + " END");
	}

	public void saveForm(Form form) {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveForm " + form + " START");

		form.setUpdatedBy(getUser());
		form.setUpdateTime();
		formDao.makePersistent(form);

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveForm " + form + " END");
	}

	public void finishForm(Form form) {
		logInfoStart("finishForm",form);
		form.setStatus(FormWorkStatus.FINAL_DESIGN);
		saveForm(form);
		logInfoEnd("finishForm",form);
	}

	public void saveAsBlock(TreeObject element, String blockLabel, Organization organization)
			throws FieldTooLongException, FormWithSameNameException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveAsBlock " + formInUse + " " + element + " " + blockLabel + " START");

		Block block = null;
		try {
			// First we generate a copy. This can abort the whole process if the
			// resulted simplification is not complete.
			Form copiedForm = formInUse.generateFormCopiedSimplification(element);
			copiedForm.resetIds();

			// Now we create the new block
			block = createBlock(blockLabel, organization);
			// And set the copied view content as the children
			block.setChildren(copiedForm.getChildren());
			block.addFlows(copiedForm.getFlows());

			block.setUpdatedBy(getUser());
			block.setUpdateTime();
			blockDao.makePersistent(block);
		} catch (NotValidChildException | CharacterNotAllowedException | NotValidStorableObjectException e) {
			// Impossible, still, if fails remove block.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			if (block != null) {
				blockDao.makeTransient(block);
			}
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveAsBlock " + formInUse + " " + element + " " + blockLabel + " END");
	}

	public void updateForm(Form form, String description) {
		logInfoStart("updateForm", form, description);
		try {
			form.setDescription(description);
			form.setUpdatedBy(UserSessionHandler.getUser());
			form.setUpdateTime();
		} catch (FieldTooLongException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		logInfoEnd("updateForm", form, description);
	}

	public void updateAnswer(Answer answer, String value, String label, String description) {
		logInfoStart("updateAnswer", answer, value, label, description);
		try {
			answer.setValue(value);
			answer.setLabel(label);
			answer.setDescription(description);
			answer.setUpdatedBy(UserSessionHandler.getUser());
			answer.setUpdateTime();

		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		logInfoEnd("updateAnswer", answer, value, label);
	}

	public void updateCategory(Category category, String name, String label) {
		logInfoStart("updateCategory", category, name, label);
		try {
			category.setName(name);
			category.setLabel(label);
			category.setUpdatedBy(UserSessionHandler.getUser());
			category.setUpdateTime();
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		logInfoEnd("updateCategory", category, name, label);
	}

	public void updateGroup(Group group, String name, String label, boolean repeatable) {
		logInfoStart("updateGroup", group, name, label, repeatable);
		try {
			group.setName(name);
			group.setLabel(label);
			group.setRepeatable(repeatable);
			group.setUpdatedBy(UserSessionHandler.getUser());
			group.setUpdateTime();

		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		logInfoEnd("updateGroup", group, name, label, repeatable);
	}

	public void updateQuestion(Question question, String name, String label, String description, boolean mandatory,
			AnswerType answerType, AnswerFormat answerFormat, AnswerSubformat answerSubformat, boolean horizontal) {
		logInfoStart("updateQuestion", question, name, label, description, mandatory, answerType, answerFormat,
				answerSubformat, horizontal);
		try {
			question.setName(name);
			question.setLabel(label);
			question.setDescription(description);
			question.setMandatory(mandatory);
			question.setAnswerType(answerType);
			question.setAnswerFormat(answerFormat);
			question.setAnswerSubformat(answerSubformat);
			question.setHorizontal(horizontal);
			question.setUpdatedBy(UserSessionHandler.getUser());
			question.setUpdateTime();
		} catch (FieldTooLongException | InvalidAnswerFormatException | CharacterNotAllowedException
				| InvalidAnswerSubformatException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		logInfoStart("updateQuestion", question, name, label, description, mandatory, answerType, answerFormat,
				answerSubformat, horizontal);
	}

	/**
	 * Inserts element belonging group to current form. This generates a clone
	 * of the block using the element as hierarchy seed and introduces to
	 * current form as a new category.
	 * 
	 * @param selectedRow
	 * @throws CategoryWithSameNameAlreadyExistsInForm
	 */
	public void insertBlock(TreeObject element) throws CategoryWithSameNameAlreadyExistsInForm {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " insertBlock " + formInUse + " " + element + " START");

		// Check name uniqueness first
		Category category = (Category) element.getAncestor(Category.class);
		if (formInUse.findChild(category.getName()) != null) {
			// Element found, throw exception.
			throw new CategoryWithSameNameAlreadyExistsInForm();
		}

		try {
			Block blockToInsert = (Block) element.getAncestor(Block.class);
			Block copiedBlock = (Block) blockToInsert.generateFormCopiedSimplification(element);
			copiedBlock.resetIds();

			formInUse.addChildren(copiedBlock.getChildren());
			formInUse.addFlows(copiedBlock.getFlows());

		} catch (NotValidStorableObjectException | NotValidChildException | CharacterNotAllowedException e) {
			// Impossible.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " insertBlock " + formInUse + " " + element + " END");
	}

	/**
	 * Moves a TreeObject origin to a new child position on destiny.
	 * 
	 * @param origin
	 * @param destiny
	 * @throws NotValidChildException
	 * @throws SameOriginAndDestinationException
	 * @throws DestinyIsContainedAtOrigin
	 */
	public void moveTo(TreeObject origin, TreeObject destiny) throws NotValidChildException,
			SameOriginAndDestinationException, DestinyIsContainedAtOrigin, ChildrenNotFoundException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress() + " move "
				+ origin + " to " + destiny + " START");
		if (origin.equals(destiny)) {
			throw new SameOriginAndDestinationException("Origin and destination are the same element");
		}
		if (origin.contains(destiny)) {
			throw new DestinyIsContainedAtOrigin("Origin is contained inside destination element.");
		}
		try {
			destiny.addChild(origin);
			TreeObject.move(origin, destiny);
		} catch (NotValidChildException | ChildrenNotFoundException e) {
			WebformsLogger.warning(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " move " + origin + " to " + destiny + " could not be done.");
			throw e;
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress() + " move "
				+ origin + " to " + destiny + " END");
	}

	/**
	 * This function is called when the ui has expired. The implementation needs
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
	 */
	public void addFlowToForm(Flow flow, Form form) {
		logInfoStart("addFlowToForm", flow, form);
		form.addFlow(flow);
		logInfoEnd("addFlowToForm", flow, form);
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
	 * @throws FlowWithoutSource
	 * @throws FlowSameOriginAndDestinyException
	 * @throws FlowDestinyIsBeforeOrigin
	 * @throws FlowWithoutDestiny
	 */
	public void updateFlowContent(Flow flow, TreeObject origin, FlowType flowType, TreeObject destiny, boolean others,
			List<Token> condition) throws BadFlowContentException, FlowWithoutSource,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {
		logInfoStart("updateFlowContent", flow, origin, flowType, destiny, others, condition);

		flow.setContent(origin, flowType, destiny, others, condition);

		if (flow.getCreatedBy() == null) {
			flow.setCreationTime();
			flow.setCreatedBy(getUser());
		}
		flow.setUpdateTime();
		flow.setUpdatedBy(getUser());

		if (!getFormInUse().containsFlow(flow)) {
			addFlowToForm(flow, getFormInUse());
		}
		logInfoEnd("updateFlowContent", flow, origin, flowType, destiny, condition);
	}

	/**
	 * Updates flow update time and updated by in flow. The content of the flow
	 * was already modified by {@link WindowFlow}
	 * 
	 * @param flow
	 */
	public void updateFlow(Flow flow) {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " updateFlow " + flow + " in " + getFormInUse() + "START");
		flow.setUpdateTime();
		flow.setUpdatedBy(getUser());
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " updateFlow " + flow + " in " + getFormInUse() + "END");
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
		}
		logInfoEnd("cloneFlows", selectedFlows);
		return clonedFlows;
	}

	/**
	 * Clones a set of flows, reset Ids and inserts into form
	 * 
	 * @param selectedFlows
	 * @return
	 */
	public Set<Flow> cloneFlowsAndInsertIntoForm(Set<Flow> selectedFlows) {
		logInfoStart("cloneFlowsAndInsertIntoForm", selectedFlows);
		Set<Flow> clones = cloneFlows(selectedFlows);
		for (Flow clone : clones) {
			clone.resetIds();
			addFlowToForm(clone, UserSessionHandler.getController().getFormInUse());
		}
		logInfoEnd("cloneFlowsAndInsertIntoForm", selectedFlows);
		return clones;
	}

	/**
	 * Removes a set of flows from current form.
	 * 
	 * @param selectedFlows
	 */
	public void removeFlows(Set<Flow> selectedFlows) {
		removeFlows(getFormInUse(), selectedFlows);
	}

	private void removeFlows(Form form, Set<Flow> flows) {
		logInfoStart("removeFlows", form, flows);
		for (Flow flow : flows) {
			form.getFlows().remove(flow);
		}
		logInfoEnd("removeFlows", form, flows);
	}

	protected void logInfoStart(String functionName, Object... parameters) {
		WebformsLogger.info(ApplicationController.class.getName(),
				getUserInfo() + " " + getFunctionInfo(functionName, parameters) + " START");
	}

	protected void logInfoEnd(String functionName, Object... parameters) {
		WebformsLogger.info(ApplicationController.class.getName(),
				getUserInfo() + " " + getFunctionInfo(functionName, parameters) + " END");
	}

	protected void logInfo(String functionName, Object... parameters) {
		WebformsLogger.info(ApplicationController.class.getName(),
				getUserInfo() + " " + getFunctionInfo(functionName, parameters));
	}

	protected String getUserInfo() {
		String userInfo = new String("User: ");
		if (getUser() == null) {
			return userInfo + "NO USER";
		} else {
			return userInfo + getUser().getEmailAddress();
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

	public Set<Flow> getFormInUseFlows() {
		logInfo("getFormInUseFlows");
		return getFormInUse().getFlows();
	}

	public void logOut() {
		logInfo("logOut");
		clearFormInUse();
		ApplicationUi.navigateTo(WebMap.LOGIN_PAGE);
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
			public Collection<com.biit.abcd.persistence.entity.Form> getAll() {
				List<com.biit.abcd.persistence.entity.Form> forms = new ArrayList<>();

				List<Organization> userOrganizations = WebformsAuthorizationService.getInstance()
						.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), WebformsActivity.READ);
				for (Organization organization : userOrganizations) {
					forms.addAll(getFormDaoAbcd().getAll(organization.getOrganizationId()));
				}
				return forms;
			}
		};

		return provider;
	}

	public TreeTableProvider<Form> getTreeTableFormsProvider() {
		TreeTableProvider<Form> provider = new TreeTableProvider<Form>() {

			@Override
			public Collection<Form> getAll() {
				List<Form> forms = new ArrayList<>();

				List<Organization> userOrganizations = WebformsAuthorizationService.getInstance()
						.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), WebformsActivity.READ);
				for (Organization organization : userOrganizations) {
					forms.addAll(getWebformsFormDao().getAll(organization.getOrganizationId()));
				}
				return forms;
			}
		};
		return provider;
	}

	public TreeTableProvider<SimpleFormView> getTreeTableSimpleAbcdFormsProvider() {
		TreeTableProvider<SimpleFormView> provider = new TreeTableProvider<SimpleFormView>() {

			@Override
			public Collection<SimpleFormView> getAll() {
				List<SimpleFormView> forms = new ArrayList<>();

				// Get all organizations where user has read permissions and
				// store ids in a hash map.
				List<Organization> userOrganizations = WebformsAuthorizationService.getInstance()
						.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), WebformsActivity.READ);
				HashSet<Long> userOrganizationIds = new HashSet<Long>();
				for (Organization organization : userOrganizations) {
					userOrganizationIds.add(organization.getOrganizationId());
				}

				// Get all simple forms and add to the form list if their
				// organization id is on the organization id map.
				List<SimpleFormView> simpleForms = getSimpleFormDaoAbcd().getAll();
				for (SimpleFormView simpleForm : simpleForms) {
					if (userOrganizationIds.contains(simpleForm.getOrganizationId())) {
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

	public void validateCompatibility(Form currentForm, SimpleFormView abcdForm) {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns all organizations where user has permission to do all the
	 * activities in activitiesFilter.
	 * 
	 * @param activitiesFilter
	 * @return
	 */
	public List<Organization> getOrganizatiosWhereUser(IActivity... activitiesFilter) {
		try {
			List<Organization> organizations = WebformsAuthorizationService.getInstance().getUserOrganizations(
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
		return new ArrayList<>();
	}
}
