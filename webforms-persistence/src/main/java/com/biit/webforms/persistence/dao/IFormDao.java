package com.biit.webforms.persistence.dao;

import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.webforms.persistence.entity.Form;

public interface IFormDao extends IJpaGenericDao<Form, Long> {

    boolean exists(String label, long organizationId);

    boolean exists(String label, int version, long organizationId, long skipForm);

    Form get(String label, int version, long organizationId);

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