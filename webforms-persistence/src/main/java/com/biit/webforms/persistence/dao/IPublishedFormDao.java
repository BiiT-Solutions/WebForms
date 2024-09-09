package com.biit.webforms.persistence.dao;

import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.webforms.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.webforms.persistence.entity.PublishedForm;

public interface IPublishedFormDao extends IJpaGenericDao<PublishedForm, Long> {

    PublishedForm get(String label, Integer version, Long organizationId) throws MultiplesFormsFoundException;
}
