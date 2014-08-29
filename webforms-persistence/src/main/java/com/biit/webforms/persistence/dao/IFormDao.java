package com.biit.webforms.persistence.dao;

import java.util.List;

import com.biit.form.persistence.dao.IBaseFormDao;
import com.biit.webforms.persistence.entity.Form;

public interface IFormDao extends IBaseFormDao<Form> {

	
	/**
	 * Filtered version of get All. Takes a Class argument and returns a list
	 * with all the elements that match the class argument.
	 * 
	 * @param cls
	 * @return
	 */
	public List<Form> getAll(Class<?> cls);
}