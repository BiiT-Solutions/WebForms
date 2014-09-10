package com.biit.webforms.persistence.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.TypeConstraintException;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.stereotype.Repository;

import com.biit.form.persistence.dao.hibernate.TreeObjectDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Form;
import com.liferay.portal.model.Organization;

@Repository
public class FormDao extends TreeObjectDao<Form> implements IFormDao {

	public FormDao() {
		super(Form.class);
	}

	@Override
	protected void initializeSets(List<Form> forms) {
		for (Form form : forms) {
			// Initializes the sets for lazy-loading (within the same session)+
			Hibernate.initialize(form.getChildren());
			Hibernate.initialize(form.getRules());
		}
	}

	@Override
	public int getLastVersion(Form form) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.setProjection(Projections.max("version"));
			criteria.add(Restrictions.eq("name", form.getName()));
			Integer maxVersion = (Integer) criteria.uniqueResult();
			session.getTransaction().commit();
			return maxVersion;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public Form getForm(String name) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.add(Restrictions.eq("name", name));
			@SuppressWarnings("unchecked")
			List<Form> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
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
	public Form getForm(String formName, Organization organization) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.add(Restrictions.eq("name", formName));
			criteria.add(Restrictions.eq("organizationId", organization.getOrganizationId()));
			@SuppressWarnings("unchecked")
			List<Form> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
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
			
			//Filter by class
			Iterator<Form> itr = result.iterator();
			while(itr.hasNext()){
				Form form = itr.next();
				if(!form.getClass().isAssignableFrom(cls)){
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
			session.getTransaction().rollback();
			throw e;
		}
	}
}
