package com.biit.webforms.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.TypeConstraintException;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.biit.form.persistence.dao.hibernate.TreeObjectDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Form;

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

	/**
	 * Filtered version of get All. Takes a Class argument and returns a list
	 * with all the elements that match the class argument.
	 * 
	 * @param cls
	 * @return
	 */
	@Override
	public List<Form> getAll(Class<?> cls) {
		if (!Form.class.isAssignableFrom(cls)) {
			throw new TypeConstraintException("FormDao can only filter subclasses of " + Form.class.getName());
		}
		List<Form> forms = super.getAll();
		List<Form> filteredForms = new ArrayList<Form>();
		for (Form form : forms) {
			if (form.getClass().isAssignableFrom(cls)) {
				filteredForms.add(form);
			}
		}
		return filteredForms;
	}
}
