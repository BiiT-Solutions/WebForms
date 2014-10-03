package com.biit.webforms.authentication;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.exception.CategoryWithSameNameAlreadyExistsInForm;
import com.biit.webforms.authentication.exception.DestinyIsContainedAtOrigin;
import com.biit.webforms.authentication.exception.NewVersionWithoutFinalDesignException;
import com.biit.webforms.authentication.exception.SameOriginAndDestinationException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.enumerations.RuleType;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.gui.webpages.floweditor.WindowRule;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.Rule;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.exceptions.BadRuleContentException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.biit.webforms.persistence.entity.exceptions.RuleDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.RuleSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.RuleWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.RuleWithoutSource;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.vaadin.server.VaadinServlet;

public class ApplicationController {

	private User user;

	private IFormDao formDao;
	private IBlockDao blockDao;

	private Form lastEditedForm;
	private Form formInUse;

	public ApplicationController() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
		blockDao = (IBlockDao) helper.getBean("blockDao");
	}

	/**
	 * User action to create a form. Needs a unique name where name.length() < 190 characters.
	 * 
	 * @param formLabel
	 * @return
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 */
	public Form createForm(String formLabel, Organization organization) throws FieldTooLongException,
			FormWithSameNameException, CharacterNotAllowedException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createForm " + formLabel + " START");

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
		if (formDao.getForm(formLabel, organization) != null) {
			FormWithSameNameException ex = new FormWithSameNameException("Form with name: " + formLabel
					+ " already exists");
			WebformsLogger.severe(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " createForm " + ex.getMessage());
			throw ex;
		}

		// Persist form.
		try {
			formDao.makePersistent(newform);
		} catch (ConstraintViolationException cve) {
			WebformsLogger.errorMessage(ApplicationController.class.getName(), cve);
			throw cve;
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createForm " + formLabel + " END");
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

	public Form createNewFormVersion(Form form) throws NewVersionWithoutFinalDesignException,
			NotValidTreeObjectException, CharacterNotAllowedException {
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
		} catch (NotValidTreeObjectException | CharacterNotAllowedException ex) {
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
	 * Creates any kind of TreeObject descendant with @name and inserts into parent if possible.
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

	public void finishForm() {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " finishForm " + formInUse + " START");
		formInUse.setStatus(FormWorkStatus.FINAL_DESIGN);
		saveForm(formInUse);
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " finishForm " + formInUse + " END");
	}

	public void saveAsBlock(TreeObject element, String blockLabel, Organization organization)
			throws FieldTooLongException, FormWithSameNameException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveAsBlock " + formInUse + " " + element + " " + blockLabel + " START");

		Block block = null;
		try {
			// First we create the new block
			block = createBlock(blockLabel, organization);

			Form copiedForm = formInUse.generateFormCopiedSimplification(element);
			copiedForm.resetIds();
			block.setChildren(copiedForm.getChildren());
			block.addRules(copiedForm.getRules());

			block.setUpdatedBy(getUser());
			block.setUpdateTime();
			blockDao.makePersistent(block);
		} catch (NotValidTreeObjectException | NotValidChildException | CharacterNotAllowedException e) {
			// Impossible, still, if fails remove block.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			if (block != null) {
				blockDao.makeTransient(block);
			}
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveAsBlock " + formInUse + " " + element + " " + blockLabel + " END");
	}
	
	public void updateForm(Form form, String description){
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
	
	public void updateAnswer(Answer answer, String value, String label, String description){
		logInfoStart("updateAnswer", answer, value, label,description);
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
	 * Inserts element belonging group to current form. This generates a clone of the block using the element as
	 * hierarchy seed and introduces to current form as a new category.
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
			formInUse.addRules(copiedBlock.getRules());

		} catch (NotValidTreeObjectException | NotValidChildException | CharacterNotAllowedException e) {
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
	 * This function is called when the ui has expired. The implementation needs to free any "locked" resources
	 */
	public void freeLockedResources() {
		clearFormInUse();
	}

	/**
	 * Adds rule to a explicit form.
	 * 
	 * @param newRule
	 * @param form
	 */
	public void addRuleToForm(Rule newRule, Form form) {
		logInfoStart("addRuleToForm", newRule, form);
		form.addRule(newRule);
		logInfoEnd("addRuleToForm", newRule, form);
	}

	/**
	 * Update rule content. This function currently is a direct call to the structure function. If the rule is not on
	 * the form, it gets added.
	 * 
	 * @param rule
	 * @param origin
	 * @param ruleType
	 * @param destiny
	 * @param conditionString
	 * @throws BadRuleContentException
	 * @throws RuleWithoutSource
	 * @throws RuleSameOriginAndDestinyException
	 * @throws RuleDestinyIsBeforeOrigin
	 * @throws RuleWithoutDestiny
	 */
	public void updateRuleContent(Rule rule, TreeObject origin, RuleType ruleType, TreeObject destiny, boolean others,
			List<Token> condition) throws BadRuleContentException, RuleWithoutSource,
			RuleSameOriginAndDestinyException, RuleDestinyIsBeforeOrigin, RuleWithoutDestiny {
		logInfoStart("updateRuleContent", rule, origin, ruleType, destiny, others, condition);

		rule.setRuleContent(origin, ruleType, destiny, others, condition);

		if (rule.getCreatedBy() == null) {
			rule.setCreationTime();
			rule.setCreatedBy(getUser());
		}
		rule.setUpdateTime();
		rule.setUpdatedBy(getUser());

		if (!getFormInUse().containsRule(rule)) {
			addRuleToForm(rule, getFormInUse());
		}
		logInfoEnd("updateRuleContent", rule, origin, ruleType, destiny, condition);
	}

	/**
	 * Updates rule update time and updated by in rule. The content of the rule was already modified by
	 * {@link WindowRule}
	 * 
	 * @param newRule
	 */
	public void updateRule(Rule newRule) {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " updateRule " + newRule + " in " + getFormInUse() + "START");
		newRule.setUpdateTime();
		newRule.setUpdatedBy(getUser());
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " updateRule " + newRule + " in " + getFormInUse() + "END");
	}

	/**
	 * Clones rules.
	 * 
	 * @param selectedRules
	 */
	public Set<Rule> cloneRules(Set<Rule> selectedRules) {
		logInfoStart("cloneRules", selectedRules);
		Set<Rule> clonedRules = new HashSet<Rule>();
		for (Rule selectedRule : selectedRules) {
			clonedRules.add(selectedRule.generateCopy());
		}
		logInfoEnd("cloneRules", selectedRules);
		return clonedRules;
	}

	/**
	 * Clones a set of rules, reset Ids and inserts into form
	 * 
	 * @param selectedRules
	 * @return
	 */
	public Set<Rule> cloneRulesAndInsertIntoForm(Set<Rule> selectedRules) {
		logInfoStart("cloneRulesAndInsertIntoForm", selectedRules);
		Set<Rule> clones = cloneRules(selectedRules);
		for (Rule clone : clones) {
			clone.resetIds();
			addRuleToForm(clone, UserSessionHandler.getController().getFormInUse());
		}
		logInfoEnd("cloneRulesAndInsertIntoForm", selectedRules);
		return clones;
	}

	/**
	 * Removes a set of rules from current form.
	 * 
	 * @param selectedRules
	 */
	public void removeRules(Set<Rule> selectedRules) {
		removeRules(getFormInUse(), selectedRules);
	}

	private void removeRules(Form form, Set<Rule> rules) {
		logInfoStart("removeRules", form, rules);
		for (Rule rule : rules) {
			form.getRules().remove(rule);
		}
		logInfoEnd("removeRules", form, rules);
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

	public Set<Rule> getFormInUseRules() {
		logInfo("getFormInUseRules");
		return getFormInUse().getRules();
	}

	public void logOut() {
		logInfo("logOut");
		clearFormInUse();
		ApplicationUi.navigateTo(WebMap.LOGIN_PAGE);
	}
}
