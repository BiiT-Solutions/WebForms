package com.biit.webforms.authentication;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.FieldTooLongException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.Subcategory;
import com.biit.webforms.persistence.entity.Text;
import com.liferay.portal.model.User;
import com.vaadin.server.VaadinServlet;

public class ApplicationController {

	private User user;

	private IFormDao formDao;

	private Form lastEditedForm;
	private Form formInUse;

	public ApplicationController() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
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

	public Form createNewFormVersion(Form form) {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " createNewFormVersion " + form + " START");
		Form newFormVersion = form.createNewVersion(getUser());

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
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " setFormInUse " + form);
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
			treeObject.setName(getNewStringNumber(parent, name));
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
						// In this case, we simply ignore and continue the function.
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
				+ " removeTreeObject " + row + " of " + row.getForm() + " START");

		row.remove();

		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " removeTreeObject " + row + " of " + row.getForm() + " END");
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

	public void notifyTreeObjectUpdated(Object element) {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " updated element " + element);
	}

}
