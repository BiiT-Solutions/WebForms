package com.biit.webforms.persistence.dao.hibernate;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenIn;

@Repository
public class BlockDao extends AnnotatedGenericDao<Block, Long> implements IBlockDao {

	public BlockDao() {
		super(Block.class);
	}

	/**
	 * Application expects null block if no block was found.
	 */
	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public Block getBlock(String blockLabel, Long organizationId) throws UnexpectedDatabaseException {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Block> cq = cb.createQuery(Block.class);
		// Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Block> formMetamodel = m.entity(Block.class);
		Root<Block> form = cq.from(Block.class);

		cq.where(cb.and(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)), blockLabel),
				cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId)));
		try {
			return getEntityManager().createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public List<Block> getAll(Long organizationId) throws UnexpectedDatabaseException {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Block> cq = cb.createQuery(Block.class);
		// Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Block> formMetamodel = m.entity(Block.class);
		Root<Block> form = cq.from(Block.class);

		cq.where(cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public int getFormFlowsCountUsingElement(List<Long> ids) throws UnexpectedDatabaseException {
		if (ids == null || ids.isEmpty()) {
			return 0;
		}

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		// Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Flow> flowType = m.entity(Flow.class);
		Root<Flow> flow = cq.from(Flow.class);

		cq.select(cb.count(flow));
		cq.where(cb.or(flow.get(flowType.getSingularAttribute("originId", Long.class)).in(ids),
				flow.get(flowType.getSingularAttribute("destinyId", Long.class)).in(ids)));
		return getEntityManager().createQuery(cq).getSingleResult().intValue();
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public Block merge(Block block) {
		block.updateChildrenSortSeqs();
		if (block.getCreationTime() == null) {
			block.setCreationTime();
		}
		block.setUpdateTime();

		return super.merge(block);
	}

	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public Block makePersistent(Block block) {
		block.updateChildrenSortSeqs();
		if (block.getCreationTime() == null) {
			block.setCreationTime();
		}
		block.setUpdateTime();

		return super.makePersistent(block);
	}

	// @Override
	// public int getLastVersion(String label, Long organizationId) {
	// throw new UnsupportedOperationException("Block dao doesn't allow a get last version");
	// }
	//
	// @Override
	// public int getLastVersion(Block form) {
	// throw new UnsupportedOperationException("Block dao doesn't allow a get last version");
	// }
	//
	// @Override
	// public Block getBlock(String blockLabel, Long organizationId) throws UnexpectedDatabaseException {
	// return getForm(blockLabel, organizationId);
	// }
	//
	// @Override
	// public Block getForm(String label, Long organizationId) throws UnexpectedDatabaseException {
	// Session session = getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// try {
	// Criteria criteria = session.createCriteria(Block.class);
	// criteria.add(Restrictions.eq("label", label));
	// criteria.add(Restrictions.eq("organizationId", organizationId));
	// @SuppressWarnings("unchecked")
	// List<Block> results = criteria.list();
	// initializeSets(results);
	// session.getTransaction().commit();
	// if (!results.isEmpty()) {
	// return (Block) results.get(0);
	// }
	// } catch (RuntimeException e) {
	// session.getTransaction().rollback();
	// throw new UnexpectedDatabaseException(e.getMessage(), e);
	// }
	// return null;
	// }
	//
	// @Override
	// // @Cacheable(value = "buildingBlocks")
	// public List<Block> getAll(Long organizationId) throws UnexpectedDatabaseException {
	// Session session = getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// try {
	// // session.createCriteria(getType()).list() is not working returns
	// // repeated elements due to
	// //
	// http://stackoverflow.com/questions/8758363/why-session-createcriteriaclasstype-list-return-more-object-than-in-list
	// // if we have a list with eager fetch.
	// Criteria criteria = session.createCriteria(getType());
	// // This is executed in java side.
	// criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
	// criteria.add(Restrictions.eq("organizationId", organizationId));
	// @SuppressWarnings("unchecked")
	// List<Block> result = criteria.list();
	// initializeSets(result);
	// session.getTransaction().commit();
	// return result;
	// } catch (RuntimeException e) {
	// session.getTransaction().rollback();
	// throw new UnexpectedDatabaseException(e.getMessage(), e);
	// }
	// }

	// // For solving Hibernate bug
	// // https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
	// // list of children
	// // with @Orderby or @OrderColumn we use our own order manager.
	//
	// // Sort the rules
	// Set<Flow> rules = entity.getFlows();
	// Iterator<Flow> ruleItr = rules.iterator();
	// while (ruleItr.hasNext()) {
	// Flow rule = ruleItr.next();
	// rule.updateConditionSortSeq();
	// }
	//
	// return super.makePersistent(entity);
	// }
	//
	// @Override
	// public boolean exists(String label, Long organizationId) throws UnexpectedDatabaseException {
	// Session session = getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// try {
	// Criteria criteria = session.createCriteria(getType());
	// criteria.setProjection(Projections.rowCount());
	// criteria.add(Restrictions.eq("label", label));
	// criteria.add(Restrictions.eq("organizationId", organizationId));
	// int rows = ((Long) criteria.uniqueResult()).intValue();
	// session.getTransaction().commit();
	// return rows > 0;
	// } catch (RuntimeException e) {
	// session.getTransaction().rollback();
	// throw new UnexpectedDatabaseException(e.getMessage(), e);
	// }
	// }
	//
	// @Override
	// public boolean exists(String label, Integer version, Long organizationId) throws UnexpectedDatabaseException {
	// Session session = getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// try {
	// Criteria criteria = session.createCriteria(getType());
	// criteria.setProjection(Projections.rowCount());
	// criteria.add(Restrictions.eq("label", label));
	// criteria.add(Restrictions.eq("organizationId", organizationId));
	// criteria.add(Restrictions.eq("version", version));
	// int rows = ((Long) criteria.uniqueResult()).intValue();
	// session.getTransaction().commit();
	// return rows > 0;
	// } catch (RuntimeException e) {
	// session.getTransaction().rollback();
	// throw new UnexpectedDatabaseException(e.getMessage(), e);
	// }
	// }
	//
	// @Override
	// public boolean exists(String label, Integer version, Long organizationId, Long differentFormId)
	// throws UnexpectedDatabaseException {
	// Session session = getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// try {
	// Criteria criteria = session.createCriteria(getType());
	// criteria.setProjection(Projections.rowCount());
	// criteria.add(Restrictions.eq("label", label));
	// criteria.add(Restrictions.eq("organizationId", organizationId));
	// criteria.add(Restrictions.eq("version", version));
	// criteria.add(Restrictions.ne("id", differentFormId));
	// int rows = ((Long) criteria.uniqueResult()).intValue();
	// session.getTransaction().commit();
	// return rows > 0;
	// } catch (RuntimeException e) {
	// session.getTransaction().rollback();
	// throw new UnexpectedDatabaseException(e.getMessage(), e);
	// }
	// }
	//
	// @Override
	// public Block getForm(String label, Integer version, Long organizationId) {
	// throw new UnsupportedOperationException("Block dao doesn't allow a get by name, version and organization");
	// }
	//
	// @Override
	// public Long getId(String blockLabel, Long organizationId, int version) throws UnexpectedDatabaseException {
	// Session session = getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// try {
	// Criteria criteria = session.createCriteria(getType());
	// criteria.setProjection(Projections.property("id"));
	// criteria.add(Restrictions.eq("label", blockLabel));
	// criteria.add(Restrictions.eq("organizationId", organizationId));
	// criteria.add(Restrictions.eq("version", version));
	// List<?> result = criteria.list();
	// Long formId = null;
	// for (Iterator<?> it = result.iterator(); it.hasNext();) {
	// formId = (Long) it.next();
	// break;
	// }
	// session.getTransaction().commit();
	// return formId;
	// } catch (RuntimeException e) {
	// session.getTransaction().rollback();
	// throw new UnexpectedDatabaseException(e.getMessage(), e);
	// }
	// }
	//
	// @Override
	// @CacheEvict(value = "buildingBlocks", allEntries = true)
	// public void evictAllCache() {
	// super.evictAllCache();
	// }
	//
	// @Override
	// @Cacheable(value = "buildingBlocks", key = "#id")
	// public Block read(Long id) throws UnexpectedDatabaseException {
	// return super.read(id);
	// }
	//

	@Override
	@Caching(evict = { @CacheEvict(value = "buildingBlocks", key = "#block.getId()", condition = "#block.getId() != null") })
	public void makeTransient(Block block) throws ElementCannotBeRemovedException {
		// Check the block is not linked.
		if (countLinkedBlocksTo(block) > 0) {
			throw new ElementCannotBeRemovedException("Building block is linked in one or more form.");
		}
		super.makeTransient(block);
	}

	private int countLinkedBlocksTo(Block block) {
		if (block == null || block.getId() == null) {
			return 0;
		}

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<BlockReference> blockType = m.entity(BlockReference.class);
		Root<BlockReference> root = query.from(BlockReference.class);

		query.select(cb.count(root));
		query.where(cb.equal(root.get(blockType.getSingularAttribute("reference", Block.class)), block.getId()));

		return getEntityManager().createQuery(query).getSingleResult().intValue();
	}

	@Override
	public boolean checkIfBlockElementCanBeRemoved(TreeObject treeObject) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Flow> flowType = m.entity(Flow.class);
		Root<Flow> root = query.from(Flow.class);

		query.select(cb.count(root));
		query.where(cb.or(
				cb.equal(root.get(flowType.getSingularAttribute("origin", BaseQuestion.class)), treeObject.getId()),
				cb.equal(root.get(flowType.getSingularAttribute("destiny", BaseQuestion.class)), treeObject.getId())));
		if (getEntityManager().createQuery(query).getSingleResult().intValue() > 0) {
			return false;
		}

		// Query for relationships with token types
		if (countReferencesInTokenClass(TokenBetween.class, treeObject.getId()) > 0) {
			return false;
		}
		if (countReferencesInTokenClass(TokenIn.class, treeObject.getId()) > 0) {
			return false;
		}
		if (countReferencesInTokenClass(TokenComparationAnswer.class, treeObject.getId()) > 0) {
			return false;
		}
		if (countReferencesInTokenClass(TokenComparationValue.class, treeObject.getId()) > 0) {
			return false;
		}

		return true;
	}

	private <T> int countReferencesInTokenClass(Class<T> clazz, Long id) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<T> type = m.entity(clazz);
		Root<T> root = query.from(clazz);

		query.select(cb.count(root));
		query.where(cb.equal(root.get(type.getSingularAttribute("question", clazz)), id));
		return getEntityManager().createQuery(query).getSingleResult().intValue();
	}

	//
	// /**
	// * Return the number of flows that have as origin or destination using any element defined by a list of ids.
	// *
	// * @param ids
	// * @return
	// * @throws UnexpectedDatabaseException
	// */
	// @Override
	// public int getFormFlowsCountUsingElement(List<Long> ids) throws UnexpectedDatabaseException {
	// if (ids == null || ids.isEmpty()) {
	// return 0;
	// }
	// Session session = getSessionFactory().getCurrentSession();
	// session.beginTransaction();
	// try {
	// Query query = session
	// .createQuery("SELECT count(*) FROM Flow fl INNER JOIN fl.form fr WHERE origin_id IN (:originId) OR destiny_id IN (:destinyId)");
	// query.setParameterList("destinyId", ids);
	// query.setParameterList("originId", ids);
	// Long count = (Long) query.uniqueResult();
	// session.getTransaction().commit();
	// return count.intValue();
	// } catch (RuntimeException e) {
	// session.getTransaction().rollback();
	// throw new UnexpectedDatabaseException(e.getMessage(), e);
	// }
	// }
}