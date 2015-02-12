package com.biit.webforms.persistence.dao.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.TypeConstraintException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import com.biit.form.TreeObject;
import com.biit.form.persistence.dao.hibernate.BaseFormDao;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.liferay.portal.model.Organization;

@Repository
public class FormDao extends BaseFormDao<Form> implements IFormDao {

	public FormDao() {
		super(Form.class);
	}

	@Override
	protected void initializeSets(List<Form> forms) {
		super.initializeSets(forms);
		// for (Form form : forms) {
		// // Initializes the sets for lazy-loading (within the same session)
		// //Hibernate.initialize(form.getFlows());
		// }
	}

	@Override
	@Cacheable(value = "forms", key = "#id")
	public Form read(Long id) throws UnexpectedDatabaseException {
		// WebformsLogger.info(FormDao.class.getName(), getSessionFactory().getStatistics().toString());
		Form form = super.read(id);
		// WebformsLogger.info(FormDao.class.getName(), getSessionFactory().getStatistics().toString());
		return form;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "forms", key = "#form.getId()", condition = "#form.getId() != null") })
	public void makeTransient(Form form) throws UnexpectedDatabaseException, ElementCannotBeRemovedException {
		// Unlink all building block refereces to avoid to remove its elements.
		for (TreeObject child : form.getChildren()) {
			if (child instanceof BlockReference) {
				((BlockReference) child).setReference(null);
			}
		}
		super.makeTransient(form);
	}

	/**
	 * Filtered version of get All. Takes a Class argument and returns a list with all the elements that match the class
	 * argument.
	 * 
	 * @param cls
	 * @return
	 * @throws UnexpectedDatabaseException
	 */
	@Override
	public List<Form> getAll(Class<?> cls, Organization organization) throws UnexpectedDatabaseException {
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
			return result;
		} catch (RuntimeException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@Override
	@CachePut(value = "forms", key = "#form.getId()", condition = "#form.getId() != null")
	public Form makePersistent(Form form) throws UnexpectedDatabaseException, ElementCannotBePersistedException {
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.

		// Sort the rules
		Set<Flow> rules = form.getFlows();
		Iterator<Flow> ruleItr = rules.iterator();
		while (ruleItr.hasNext()) {
			Flow rule = ruleItr.next();
			rule.updateConditionSortSeq();
		}

		return super.makePersistent(form);
	}

	@Override
	@CacheEvict(value = "forms", allEntries = true)
	public void evictAllCache() {
		super.evictAllCache();
	}
}
