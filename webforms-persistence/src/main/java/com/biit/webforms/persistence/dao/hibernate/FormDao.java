package com.biit.webforms.persistence.dao.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Form;

@Repository
public class FormDao extends AnnotatedGenericDao<Form, Long> implements IFormDao {

	public FormDao() {
		super(Form.class);
	}

	@Override
	@Cacheable(value = "webformsforms", key = "#id")
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public Form get(Long id) {
		Form form = super.get(id);
		if (form != null) {
			form.initializeSets();
		}
		return form;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "webformsforms", key = "#form.getId()", condition = "#form.getId() != null") })
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public void makeTransient(Form form) throws ElementCannotBeRemovedException {
		form.getFlows().clear();
		Form mergedForm = getEntityManager().contains(form) ? form : getEntityManager().merge(form);
		makePersistent(mergedForm);

		super.makeTransient(mergedForm);
	}

	@Override
	@CachePut(value = "webformsforms", key = "#form.getId()", condition = "#form.getId() != null")
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public Form merge(Form form) {
		form.updateChildrenSortSeqs();
		if (form.getCreationTime() == null) {
			form.setCreationTime();
		}

		Form mergedForm = super.merge(form);
		if (mergedForm != null) {
			mergedForm.initializeSets();
		}
		return mergedForm;
	}

	@Override
	@CachePut(value = "webformsforms", key = "#form.getId()", condition = "#form.getId() != null")
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public Form makePersistent(Form form) {
		form.updateChildrenSortSeqs();
		if (form.getCreationTime() == null) {
			form.setCreationTime();
		}
		return super.makePersistent(form);
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public boolean exists(String label, int version, long organizationId, long id) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		// Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = m.entity(Form.class);
		Root<Form> form = cq.from(Form.class);

		cq.select(cb.count(form));
		cq.where(cb.and(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)), label),
				cb.equal(form.get(formMetamodel.getSingularAttribute("version", Integer.class)), version),
				cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId),
				cb.notEqual(form.get(formMetamodel.getSingularAttribute("id", Long.class)), id)));

		return getEntityManager().createQuery(cq).getSingleResult() > 0;
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public boolean exists(String label, long organizationId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		// Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = m.entity(Form.class);
		Root<Form> form = cq.from(Form.class);

		cq.select(cb.count(form));
		cq.where(cb.and(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)), label),
				cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId)));
		return getEntityManager().createQuery(cq).getSingleResult() > 0;
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public List<Form> getAll() {
		List<Form> forms = super.getAll();
		for (Form form : forms) {
			form.initializeSets();
		}
		return forms;
	}

	@Override
	@CacheEvict(value = "webformsforms", allEntries = true)
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public void evictAllCache() {
		super.evictAllCache();
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "webformsforms", key = "#id", condition = "#id != null") })
	public void evictCache(Long id) {
		// Do nothing.
	}

}
