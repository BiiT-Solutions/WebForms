package com.biit.webforms.persistence.dao;

import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.webforms.persistence.entity.Form;

public interface IFormDao extends IJpaGenericDao<Form, Long> {

	boolean exists(String label, long organizationId);

	boolean exists(String label, int version, long organizationId, long skipForm);

}