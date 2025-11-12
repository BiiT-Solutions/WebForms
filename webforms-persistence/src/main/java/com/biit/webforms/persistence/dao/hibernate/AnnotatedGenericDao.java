package com.biit.webforms.persistence.dao.hibernate;

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

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.persistence.dao.jpa.GenericDao;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;

public abstract class AnnotatedGenericDao<EntityClass, PrimaryKeyClass extends Serializable> extends
		GenericDao<EntityClass, PrimaryKeyClass> implements IJpaGenericDao<EntityClass, PrimaryKeyClass> {

	@PersistenceContext(unitName = "webformsPersistenceUnit")
	@Qualifier(value = "webformsManagerFactory")
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public AnnotatedGenericDao(Class<EntityClass> entityClass) {
		super(entityClass);
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public EntityClass makePersistent(EntityClass entity) {
		return super.makePersistent(entity);
	}
	
	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public EntityClass merge(EntityClass entity) {
		return super.merge(entity);
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public void makeTransient(EntityClass entity) throws ElementCannotBeRemovedException {
		super.makeTransient(entity);
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public EntityClass get(PrimaryKeyClass id) {
		return super.get(id);
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public int getRowCount() {
		return super.getRowCount();
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public List<EntityClass> getAll() {
		return super.getAll();
	}

}
