package com.biit.webforms.persistence.dao.hibernate;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
	
	@Override
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public Block get(Long id) {
		Block block = super.get(id);
		block.initializeSets();
		return block;
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
			Block returnBlock = getEntityManager().createQuery(cq).getSingleResult(); 
			returnBlock.initializeSets();
			return returnBlock;
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
		List<Block> blocks = getEntityManager().createQuery(cq).getResultList();
		for(Block block:blocks){
			block.initializeSets();
		}
		return blocks;
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
	@Caching(evict = { @CacheEvict(value = "buildingBlocks", key = "#block.getId()", condition = "#block.getId() != null") })
	public Block merge(Block block) {
		block.updateChildrenSortSeqs();
		if (block.getCreationTime() == null) {
			block.setCreationTime();
		}
		block.setUpdateTime();

		Block mergedBlock = super.merge(block);
		mergedBlock.initializeSets();
		return mergedBlock;
	}

	@Override
	@CachePut(value = "buildingBlocks", key = "#block.getId()", condition = "#block.getId() != null")
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public Block makePersistent(Block block) {
		block.updateChildrenSortSeqs();
		if (block.getCreationTime() == null) {
			block.setCreationTime();
		}
		block.setUpdateTime();

		return super.makePersistent(block);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "buildingBlocks", key = "#block.getId()", condition = "#block.getId() != null") })
	@Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
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
}