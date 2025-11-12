package com.biit.webforms.persistence.dao;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
