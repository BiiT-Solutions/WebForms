package com.biit.webforms.persistence.dao;

import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.webforms.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.webforms.persistence.entity.Form;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface IFormDao extends IJpaGenericDao<Form, Long> {

    boolean exists(String label, long organizationId);

    boolean exists(String label, int version, long organizationId, long skipForm);

    Form get(String label, Integer version, Long organizationId) throws MultiplesFormsFoundException;

    String getJson(Long formId);

    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    int setJson(Long formId, String json);

    void evictCache(Long id);

    // TODO
    // /**
    // * Filtered version of get All. Takes a Class argument and returns a list with all the elements that match the
    // class
    // * argument. That pertain to the specified organization.
    // *
    // * @param cls
    // * @return
    // * @throws UnexpectedDatabaseException
    // */
    // public List<Form> getAll(Class<?> cls, Organization organization) throws UnexpectedDatabaseException;
}