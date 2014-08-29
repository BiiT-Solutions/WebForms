package com.biit.webforms.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.biit.form.persistence.dao.hibernate.TreeObjectDao;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Form;

@Repository
public class BlockDao extends TreeObjectDao<Block> implements IBlockDao {

	public BlockDao() {
		super(Block.class);
	}

	@Override
	protected void initializeSets(List<Block> blocks) {
		for (Block block : blocks) {
			// Initializes the sets for lazy-loading (within the same session)+
			Hibernate.initialize(block.getChildren());
		}
	}

	@Override
	public int getLastVersion(Block form) {
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
	public Block getBlock(String name) {
		return getForm(name);
	}

	@Override
	public Block getForm(String name) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Block.class);
			criteria.add(Restrictions.eq("name", name));
			@SuppressWarnings("unchecked")
			List<Block> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
			if (!results.isEmpty()) {
				return (Block) results.get(0);
			}
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return null;
	}
}