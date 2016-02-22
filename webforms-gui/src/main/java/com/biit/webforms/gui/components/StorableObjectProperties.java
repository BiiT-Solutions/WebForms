package com.biit.webforms.gui.components;

import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.persistence.entity.StorableObject;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.common.components.PropertiesForClassComponent;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.security.IWebformsSecurityService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public abstract class StorableObjectProperties<T extends StorableObject> extends PropertiesForClassComponent<T> {
	private static final long serialVersionUID = -1986275953105055523L;
	private TextField createdByField, creationTimeField, updatedByField, updateTimeField;

	private IWebformsSecurityService webformsSecurityService;

	private T instance;

	protected StorableObjectProperties(Class<? extends T> type) {
		super(type);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
		initElement();
	}

	/**
	 * This method needs to be overwritten
	 * 
	 * @param element
	 */
	protected void initElement() {
		createCommonProperties();
	}

	private void createCommonProperties() {
		createdByField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_CREATED_BY));
		createdByField.setEnabled(false);
		creationTimeField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_CREATION_TIME));
		creationTimeField.setEnabled(false);
		updatedByField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_UPDATED_BY));
		updatedByField.setEnabled(false);
		updateTimeField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_UPDATE_TIME));
		updateTimeField.setEnabled(false);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(createdByField);
		commonProperties.addComponent(creationTimeField);
		commonProperties.addComponent(updatedByField);
		commonProperties.addComponent(updateTimeField);

		addTab(commonProperties, ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_COMMON_FORM_CAPTION), false);
	}

	/**
	 * This method needs to be overwritten
	 * 
	 * @param element
	 */
	protected void initValues() {
		initCommonValues();
	}

	private void initCommonValues() {
		String valueCreatedBy = "";
		String valueUpdatedBy = "";

		if (getInstance() == null) {
			return;
		}

		try {
			valueCreatedBy = getInstance().getCreatedBy() == null ? "" : webformsSecurityService.getUserById(getInstance().getCreatedBy())
					.getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueCreatedBy = getInstance().getCreatedBy() + "";
		}

		try {
			valueUpdatedBy = getInstance().getUpdatedBy() == null ? "" : webformsSecurityService.getUserById(getInstance().getUpdatedBy())
					.getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueUpdatedBy = getInstance().getUpdatedBy() + "";
		}

		String valueCreationTime = getInstance().getCreationTime() == null ? "" : getInstance().getCreationTime().toString();
		String valueUpdatedTime = getInstance().getUpdateTime() == null ? "" : getInstance().getUpdateTime().toString();

		createdByField.setValue(valueCreatedBy);
		creationTimeField.setValue(valueCreationTime);
		updatedByField.setValue(valueUpdatedBy);
		updateTimeField.setValue(valueUpdatedTime);
	}

	@Override
	protected void setElementAbstract(T element) {
		setInstance(element);
		initValues();
	}

	@Override
	public void updateElement() {
		getInstance().setUpdatedBy(UserSession.getUser());
		getInstance().setUpdateTime();
		// // Update common ui fields.
		// initValues();

		firePropertyUpdateListener(getInstance());
	}

	protected T getInstance() {
		return instance;
	}

	protected void setInstance(T instance) {
		this.instance = instance;
	}

	public IWebformsSecurityService getWebformsSecurityService() {
		return webformsSecurityService;
	}

}
