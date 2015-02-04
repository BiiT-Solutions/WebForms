package com.biit.webforms.persistence.dao.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import com.biit.form.persistence.dao.hibernate.TreeObjectDao;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Flow;

@Repository
public class BlockDao extends TreeObjectDao<Block> implements IBlockDao {

	public BlockDao() {
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
	public Block getBlock(String blockLabel, Long organizationId) throws UnexpectedDatabaseException {
		return getForm(blockLabel, organizationId);
	}

	@Override
	public Block getForm(String label, Long organizationId) throws UnexpectedDatabaseException {
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
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
		return null;
	}

	@Override
	// @Cacheable(value = "buildingBlocks")
	public List<Block> getAll(Long organizationId) throws UnexpectedDatabaseException {
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
			criteria.add(Restrictions.eq("organizationId", organizationId));
			@SuppressWarnings("unchecked")
			List<Block> result = criteria.list();
			initializeSets(result);
			session.getTransaction().commit();
			return result;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@Override
	@CachePut(value = "buildingBlocks", key = "#entity.getId()", condition = "#entity.getId() != null")
	// Clear all forms (maybe some of them have a BlockReference to this block)
	@CacheEvict(value = "forms", allEntries = true)
	public Block makePersistent(Block entity) throws UnexpectedDatabaseException {
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
	public boolean exists(String label, Long organizationId) throws UnexpectedDatabaseException {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(getType());
			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			int rows = ((Long) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return rows > 0;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@Override
	public boolean exists(String label, Integer version, Long organizationId) throws UnexpectedDatabaseException {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(getType());
			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			criteria.add(Restrictions.eq("version", version));
			int rows = ((Long) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return rows > 0;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@Override
	public boolean exists(String label, Integer version, Long organizationId, Long differentFormId)
			throws UnexpectedDatabaseException {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(getType());
			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			criteria.add(Restrictions.eq("version", version));
			criteria.add(Restrictions.ne("id", differentFormId));
			int rows = ((Long) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return rows > 0;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@Override
	public Block getForm(String label, Integer version, Long organizationId) {
		throw new UnsupportedOperationException("Block dao doesn't allow a get by name, version and organization");
	}

	@Override
	public Long getId(String blockLabel, Long organizationId, int version) throws UnexpectedDatabaseException {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(getType());
			criteria.setProjection(Projections.property("id"));
			criteria.add(Restrictions.eq("label", blockLabel));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			criteria.add(Restrictions.eq("version", version));
			List<?> result = criteria.list();
			Long formId = null;
			for (Iterator<?> it = result.iterator(); it.hasNext();) {
				formId = (Long) it.next();
				break;
			}
			session.getTransaction().commit();
			return formId;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@Override
	@CacheEvict(value = "buildingBlocks", allEntries = true)
	public void evictAllCache() {
		super.evictAllCache();
	}

	@Override
	@Cacheable(value = "buildingBlocks", key = "#id")
	public Block read(Long id) throws UnexpectedDatabaseException {
		return super.read(id);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "buildingBlocks", key = "#block.getId()", condition = "#block.getId() != null") })
	public void makeTransient(Block block) throws UnexpectedDatabaseException, ElementCannotBeRemovedException {
		// Check the block is not linked.
		if (getLinkedBlocksTo(block) > 0) {
			throw new ElementCannotBeRemovedException("Building block is linked in one or more form.");
		}
		super.makeTransient(block);
	}

	private int getLinkedBlocksTo(Block block) throws UnexpectedDatabaseException {
		if (block == null || block.getId() == null) {
			return 0;
		}
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Query query = session.createQuery("SELECT count(*) FROM BlockReference WHERE reference_ID=:blockId");
			query.setLong("blockId", block.getId());
			Long count = (Long) query.uniqueResult();
			session.getTransaction().commit();
			return count.intValue();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}
}