package com.biit.webforms.persistence.dao.hibernate;

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
	public Form get(Long id) {
		return super.get(id);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "webformsforms", key = "#form.getId()", condition = "#form.getId() != null") })
	public void makeTransient(Form form) throws ElementCannotBeRemovedException {
		super.makeTransient(form);
	}

	@Override
	@CachePut(value = "webformsforms", key = "#form.getId()", condition = "#form.getId() != null")
	public void makePersistent(Form form) {
		super.makePersistent(form);
	}

	// /**
	// * Filtered version of get All. Takes a Class argument and returns a list with all the elements that match the
	// class
	// * argument.
	// *
	// * @param cls
	// * @return
	// * @throws UnexpectedDatabaseException
	// */
	// @Override
	// public List<Form> getAll(Class<?> cls, Organization organization) throws UnexpectedDatabaseException {
	// if (!Form.class.isAssignableFrom(cls)) {
	// throw new TypeConstraintException("FormDao can only filter subclasses of " + Form.class.getName());
	// }
	//
	// Session session = getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// try {
	// // session.createCriteria(getType()).list() is not working returns
	// // repeated elements due to
	// //
	// http://stackoverflow.com/questions/8758363/why-session-createcriteriaclasstype-list-return-more-object-than-in-list
	// // if we have a list with eager fetch.
	// Criteria criteria = session.createCriteria(cls);
	// // This is executed in java side.
	// criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
	// criteria.add(Restrictions.eq("organizationId", organization.getOrganizationId()));
	// @SuppressWarnings("unchecked")
	// List<Form> result = criteria.list();
	//
	// // Filter by class
	// Iterator<Form> itr = result.iterator();
	// while (itr.hasNext()) {
	// Form form = itr.next();
	// if (!form.getClass().isAssignableFrom(cls)) {
	// itr.remove();
	// }
	// }
	// initializeSets(result);
	// session.getTransaction().commit();
	// return result;
	// } catch (RuntimeException e) {
	// WebformsLogger.errorMessage(this.getClass().getName(), e);
	// session.getTransaction().rollback();
	// throw new UnexpectedDatabaseException(e.getMessage(), e);
	// }
	// }

	@Override
	@CacheEvict(value = "webformsforms", allEntries = true)
	public void evictAllCache() {
		super.evictAllCache();
	}

	@Override
	public Form getForm(String label, Integer version, Long organizationId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Form> cq = cb.createQuery(Form.class);
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formType = m.entity(Form.class);
		Root<Form> formRoot = cq.from(Form.class);
		cq.where(cb.and(cb.equal(formRoot.get(formType.getSingularAttribute("label", String.class)), label),
				cb.equal(formRoot.get(formType.getSingularAttribute("version", Integer.class)), version),
				cb.equal(formRoot.get(formType.getSingularAttribute("organizationId", Long.class)), organizationId)));

		return getEntityManager().createQuery(cq).getSingleResult();
	}
}
