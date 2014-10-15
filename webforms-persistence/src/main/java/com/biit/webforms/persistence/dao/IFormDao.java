package com.biit.webforms.persistence.dao;

import java.util.List;

import com.biit.form.persistence.dao.IBaseFormDao;
import com.biit.webforms.persistence.entity.Form;
import com.liferay.portal.model.Organization;

public interface IFormDao extends IBaseFormDao<Form> {

	/**
	 * Filtered version of get All. Takes a Class argument and returns a list
	 * with all the elements that match the class argument. That pertain to the
	 * specified organization.
	 * 
	 * @param cls
	 * @return
	 */
	public List<Form> getAll(Class<?> cls, Organization organization);
	
	/**
	 * Get for by label, version and organization.
	 * @param label
	 * @param version
	 * @param organization
	 * @return
	 */
	public Form getForm(String label, Integer version, Organization organization);
}