package com.biit.webforms.persistence.dao.hibernate;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.TypeConstraintException;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.biit.form.persistence.dao.hibernate.TreeObjectDao;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Flow;
import com.liferay.portal.model.Organization;

@Repository
public class FormDao extends TreeObjectDao<Form> implements IFormDao {

	public FormDao() {
		super(Form.class);
	}

	@Override
	protected void initializeSets(List<Form> forms) {
		super.initializeSets(forms);
		for (Form form : forms) {
			// Initializes the sets for lazy-loading (within the same session)+
			Hibernate.initialize(form.getFlows());
		}
	}
	
	@Override
	public int getLastVersion(Form form, Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.setProjection(Projections.max("version"));
			criteria.add(Restrictions.eq("label", form.getLabel()));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			Integer maxVersion = (Integer) criteria.uniqueResult();
			session.getTransaction().commit();
			return maxVersion;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public int getLastVersion(String label, Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.setProjection(Projections.max("version"));
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			Integer maxVersion = (Integer) criteria.uniqueResult();
			session.getTransaction().commit();
			return maxVersion;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	@Cacheable(value = "forms", key = "#id")
	public Form read(Long id) {
		return super.read(id);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "forms", key = "#form.label"),
			@CacheEvict(value = "forms", key = "#form.id"),
			@CacheEvict(value = "forms", key = "#form.label, #form.organizationId") })
	public void makeTransient(Form form) {
		super.makeTransient(form);
	}

	@Override
	@Cacheable(value = "forms", key = "#label")
	public Form getForm(String label,Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			@SuppressWarnings("unchecked")
			List<Form> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
			// For solving Hibernate bug
			// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
			// list of children
			// with @Orderby or @OrderColumn we use our own order manager.
			sortChildren(results);
			if (!results.isEmpty()) {
				return (Form) results.get(0);
			}
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return null;
	}

	@Override
	@Cacheable(value = "forms", key = "#label, #organization.id")
	public Form getForm(String formLabel, Organization organization) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.add(Restrictions.eq("label", formLabel));
			criteria.add(Restrictions.eq("organizationId", organization.getOrganizationId()));
			@SuppressWarnings("unchecked")
			List<Form> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
			// For solving Hibernate bug
			// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
			// list of children
			// with @Orderby or @OrderColumn we use our own order manager.
			sortChildren(results);
			if (!results.isEmpty()) {
				return (Form) results.get(0);
			}
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return null;
	}

	/**
	 * Filtered version of get All. Takes a Class argument and returns a list
	 * with all the elements that match the class argument.
	 * 
	 * @param cls
	 * @return
	 */
	@Cacheable(value = "forms")
	public List<Form> getAll(Class<?> cls, Organization organization) {
		if (!Form.class.isAssignableFrom(cls)) {
			throw new TypeConstraintException("FormDao can only filter subclasses of " + Form.class.getName());
		}

		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			// session.createCriteria(getType()).list() is not working returns
			// repeated elements due to
			// http://stackoverflow.com/questions/8758363/why-session-createcriteriaclasstype-list-return-more-object-than-in-list
			// if we have a list with eager fetch.
			Criteria criteria = session.createCriteria(cls);
			// This is executed in java side.
			criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
			criteria.add(Restrictions.eq("organizationId", organization.getOrganizationId()));
			@SuppressWarnings("unchecked")
			List<Form> result = criteria.list();

			// Filter by class
			Iterator<Form> itr = result.iterator();
			while (itr.hasNext()) {
				Form form = itr.next();
				if (!form.getClass().isAssignableFrom(cls)) {
					itr.remove();
				}
			}
			initializeSets(result);
			session.getTransaction().commit();
			// For solving Hibernate bug
			// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
			// list of children
			// with @Orderby or @OrderColumn we use our own order manager.
			sortChildren(result);
			return result;
		} catch (RuntimeException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	protected void sortChildren(List<Form> forms) {
		for (Form form : forms) {
			sortChildren(form);
		}
	}

	protected void sortChildren(Form form) {
		super.sortChildren(form);

		for (Flow rule : form.getFlows()) {
			sortChildren(rule);
		}
	}

	private void sortChildren(Flow rule) {
		if (rule != null) {
			Collections.sort(rule.getCondition(), new TokenSort());
		}
	}

	@Override
	@Transactional
	public Form makePersistent(Form entity) {
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.

		// Sort the rules
		Set<Flow> rules = entity.getFlows();
		Iterator<Flow> ruleItr = rules.iterator();
		while (ruleItr.hasNext()) {
			Flow rule = ruleItr.next();
			rule.updateConditionSortSeq();
		}

		return super.makePersistent(entity);
	}

	@Override
	public boolean exists(String label, Long organizationId) {
		return getForm(label,organizationId)!=null;
	}
	
	public Form getForm(String label, Integer version, Organization organization){
		 return getForm(label,version,organization.getOrganizationId());
	 }

	@Override
	public Form getForm(String label, Integer version, Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("version", version));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			
			@SuppressWarnings("unchecked")
			List<Form> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
			// For solving Hibernate bug
			// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
			// list of children
			// with @Orderby or @OrderColumn we use our own order manager.
			sortChildren(results);
			if (!results.isEmpty()) {
				return (Form) results.get(0);
			}
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return null;
	}
}
