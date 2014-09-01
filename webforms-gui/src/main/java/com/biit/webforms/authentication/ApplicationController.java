package com.biit.webforms.authentication;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.Subcategory;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
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
	 * User action to create a form. Needs a unique name where name.length() <
	 * 190 characters.
	 * 
	 * @param formName
	 * @return
	 * @throws FieldTooLongException
	 * @throws FormWithSameNameException
	 */
	public Form createForm(String formName) throws FieldTooLongException, FormWithSameNameException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createForm " + formName + " START");

		// Create new form
		Form newform = null;
		try {
			newform = new Form(formName, getUser());
		} catch (FieldTooLongException ex) {
			WebformsLogger.severe(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " createForm " + ex.getMessage());
			throw ex;
		}

		// Check if database contains a form with the same name.
		if (formDao.getForm(formName) != null) {
			FormWithSameNameException ex = new FormWithSameNameException("Form with name: " + formName
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
				+ " createForm " + formName + " END");
		return newform;
	}

	public Block createBlock(String blockName) throws FieldTooLongException, FormWithSameNameException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createBlock " + blockName + " START");

		// Create new block
		Block newBlock = null;
		try {
			newBlock = new Block(blockName, getUser());
		} catch (FieldTooLongException ex) {
			WebformsLogger.warning(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " createBlock " + ex.getMessage());
			throw ex;
		}

		// Check if database contains a form with the same name.
		if (blockDao.getBlock(blockName) != null) {
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

	public Form createNewFormVersion(Form form) throws NotValidTreeObjectException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createNewFormVersion " + form + " START");
		Form newFormVersion;
		try {
			newFormVersion = form.createNewVersion(getUser());
		} catch (NotValidTreeObjectException ex) {
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

	public void clearFormInUse() {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " clearFormInUse");
		formInUse = null;
	}

	/**
	 * Adds new category to current form
	 * 
	 * @return
	 * @throws NotValidChildException
	 */
	public Category addNewCategory() {
		try {
			return (Category) insertTreeObject(Category.class, getFormInUse(), "new-category");
		} catch (NotValidChildException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	/**
	 * Adds new subcategory to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public Subcategory addNewSubcategory(TreeObject parent) throws NotValidChildException {
		return (Subcategory) insertTreeObject(Subcategory.class, parent, "new-subcategory");
	}

	/**
	 * Adds new group to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public Group addNewGroup(TreeObject parent) throws NotValidChildException {
		return (Group) insertTreeObject(Group.class, parent, "new-group");
	}

	/**
	 * Adds new question to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public Question addNewQuestion(TreeObject parent) throws NotValidChildException {
		return (Question) insertTreeObject(Question.class, parent, "new-question");
	}
	
	/**
	 * Adds new System field
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public SystemField addNewSystemField(TreeObject parent) throws NotValidChildException {
		return (SystemField) insertTreeObject(SystemField.class, parent, "new-system-field");
	}

	/**
	 * Adds new Text to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public Text addNewText(TreeObject parent) throws NotValidChildException {
		return (Text) insertTreeObject(Text.class, parent, "new-text");
	}

	/**
	 * Adds new answer to parent
	 * 
	 * @param parent
	 * @return
	 * @throws NotValidChildException
	 */
	public Answer addNewAnswer(TreeObject parent) throws NotValidChildException {
		return (Answer) insertTreeObject(Answer.class, parent, "new-answer");
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
		} catch (FieldTooLongException | InstantiationException | IllegalAccessException e) {
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
			return newString + "-" + (maxNewString + 1);
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

	public void saveAsBlock(TreeObject element, String blockName) throws FieldTooLongException,
			FormWithSameNameException {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveAsBlock " + formInUse + " " + element + " " + blockName + " START");

		// First we create the new block
		Block block = createBlock(blockName);

		try {
			TreeObject copy = element.generateCopy(true, true);
			Form formOfCopy = (Form) copy.getAncestor(Form.class);
			formOfCopy.resetIds();
			block.setChildren(formOfCopy.getChildren());

			block.setUpdatedBy(getUser());
			block.setUpdateTime();
			blockDao.makePersistent(block);
		} catch (NotValidTreeObjectException | NotValidChildException e) {
			// Impossible, still, if fails remove block.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			blockDao.makeTransient(block);
		}

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " saveAsBlock " + formInUse + " " + element + " " + blockName + " END");
	}

	public void notifyTreeObjectUpdated(Object element) {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " updated element " + element);
	}

	/**
	 * Inserts element belonging group to current form. This generates a clone
	 * of the block using the element as hierarchy seed and introduces to
	 * current form as a new category.
	 * 
	 * @param selectedRow
	 */
	public void insertBlock(TreeObject element) {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " insertBlock " + formInUse + " " + element + " START");

		try {
			TreeObject copy = element.generateCopy(true, true);
			Block blockOfCopy = (Block) copy.getAncestor(Block.class);
			blockOfCopy.resetIds();
			
			formInUse.addChildren(blockOfCopy.getChildren());
		} catch (NotValidTreeObjectException | NotValidChildException e) {
			// Impossible.
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}	
		
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " insertBlock " + formInUse + " " + element + " END");
	}

}
