package com.biit.webforms.authentication;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.form.TreeObject;
import com.biit.form.exceptions.FieldTooLongException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
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

	public void setUser(User user){
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
	
	public Form getFormInUse(){
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

	public Category addNewCategory() {
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " addNewCategory to "+ getFormInUse()+" START");
		
		Category category = null;
		try {
			category = new Category(getNewStringNumber(getFormInUse(),"new-category"));
			getFormInUse().addChild(category);
			WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
					+ " new category '"+category+"'");
		} catch (FieldTooLongException | NotValidChildException e) {
			//Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}		
		
		WebformsLogger.info(ApplicationController.class.getName(), "User: " + getUser().getEmailAddress()
				+ " addNewCategory to "+ getFormInUse()+" END");
		return category;
	}
	
	public String getNewStringNumber(TreeObject parent, String newString){
		int maxNewString = 0;
		
		List<TreeObject> children = parent.getChildren();
		for(TreeObject child: children){
			if(child.getName().startsWith(newString)){
				if(child.getName().length()==newString.length()){
					//First one (newString)
					maxNewString = Math.max(maxNewString, 1);
				}else{
					//Other, extract number (newString-int)
					String value = child.getName().substring(newString.length()+1);
					maxNewString = Math.max(maxNewString, Integer.parseInt(value));
				}
			}
		}
		
		if(maxNewString==0){
			return newString;
		}else{
			return newString+"-"+(maxNewString+1);
		}
	}
}
