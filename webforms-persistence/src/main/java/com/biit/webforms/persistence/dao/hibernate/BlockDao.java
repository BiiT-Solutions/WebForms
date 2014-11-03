package com.biit.webforms.persistence.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.biit.form.persistence.dao.hibernate.TreeObjectDao;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Flow;
import com.liferay.portal.model.Organization;

@Repository
public class BlockDao extends TreeObjectDao<Block> implements IBlockDao {

	public BlockDao() {
		super(Block.class);
	}

	public BlockDao(HibernateTransactionManager transactionManager) {
		super(Block.class);
	}

	@Override
	protected void initializeSets(List<Block> blocks) {
		super.initializeSets(blocks);
		for (Block block : blocks) {
			// Initializes the sets for lazy-loading (within the same session)+
			Hibernate.initialize(block.getFlows());
		}
	}

	@Override
	public int getLastVersion(String label, Long organizationId) {
		throw new UnsupportedOperationException("Block dao doesn't allow a get last version");
	}

	@Override
	public int getLastVersion(Block form) {
		throw new UnsupportedOperationException("Block dao doesn't allow a get last version");
	}

	@Override
	public Block getBlock(String blockLabel, Organization organization) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Block.class);
			criteria.add(Restrictions.eq("label", blockLabel));
			criteria.add(Restrictions.eq("organizationId", organization.getOrganizationId()));
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

	@Override
	public Block getForm(String label, Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Block.class);
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("organizationId", organizationId));
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

	@Override
	public List<Block> getAll(Organization organization) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			// session.createCriteria(getType()).list() is not working returns
			// repeated elements due to
			// http://stackoverflow.com/questions/8758363/why-session-createcriteriaclasstype-list-return-more-object-than-in-list
			// if we have a list with eager fetch.
			Criteria criteria = session.createCriteria(getType());
			// This is executed in java side.
			criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
			criteria.add(Restrictions.eq("organizationId", organization.getOrganizationId()));
			@SuppressWarnings("unchecked")
			List<Block> result = criteria.list();
			initializeSets(result);
			session.getTransaction().commit();
			return result;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	@Transactional
	public Block makePersistent(Block entity) {
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
		return getForm(label, organizationId) != null;
	}

	@Override
	public Block getForm(String label, Integer version, Long organizationId) {
		throw new UnsupportedOperationException("Block dao doesn't allow a get by name, version and organization");
	}

	@Override
	public Collection<Block> getAll(Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(getType());
			criteria.add(Restrictions.eq("organizationId", organizationId));
			@SuppressWarnings("unchecked")
			List<Block> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
			return results;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
}