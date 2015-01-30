package com.biit.webforms.gui.components;

import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.persistence.entity.StorableObject;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.PropertiesForClassComponent;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.LiferayServiceAccess;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public abstract class StorableObjectProperties<T extends StorableObject> extends PropertiesForClassComponent<T> {
	private static final long serialVersionUID = -1986275953105055523L;
	protected TextField createdBy, creationTime, updatedBy, updateTime;

	private T instance;

	public StorableObjectProperties(Class<? extends T> type) {
		super(type);
		initElement();
	}

	/**
	 * This method needs to be overwritten
	 * 
	 * @param element
	 */
	protected void initElement() {
		createdBy = new TextField(
				ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_CREATED_BY));
		createdBy.setEnabled(false);
		creationTime = new TextField(
				ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_CREATION_TIME));
		creationTime.setEnabled(false);
		updatedBy = new TextField(
				ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_UPDATED_BY));
		updatedBy.setEnabled(false);
		updateTime = new TextField(
				ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_UPDATE_TIME));
		updateTime.setEnabled(false);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(createdBy);
		commonProperties.addComponent(creationTime);
		commonProperties.addComponent(updatedBy);
		commonProperties.addComponent(updateTime);

		addTab(commonProperties,
				ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_COMMON_FORM_CAPTION),
				false);
	}

	/**
	 * This method needs to be overwritten
	 * 
	 * @param element
	 */
	protected void initValues() {
		String valueCreatedBy = "";
		String valueUpdatedBy = "";
		try {
			valueCreatedBy = getInstance().getCreatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(getInstance().getCreatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueCreatedBy = getInstance().getCreatedBy() + "";
		}

		try {
			valueUpdatedBy = getInstance().getUpdatedBy() == null ? "" : LiferayServiceAccess.getInstance()
					.getUserById(getInstance().getUpdatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueUpdatedBy = getInstance().getUpdatedBy() + "";
		}

		String valueCreationTime = getInstance().getCreationTime() == null ? "" : getInstance().getCreationTime()
				.toString();
		String valueUpdatedTime = getInstance().getUpdateTime() == null ? "" : getInstance().getUpdateTime().toString();

		createdBy.setValue(valueCreatedBy);
		creationTime.setValue(valueCreationTime);
		updatedBy.setValue(valueUpdatedBy);
		updateTime.setValue(valueUpdatedTime);
	}

	@Override
	protected void setElementAbstract(T element) {
		setInstance(element);
		initValues();
	}

	@Override
	public void updateElement() {
		getInstance().setUpdatedBy(UserSessionHandler.getUser());
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

}
