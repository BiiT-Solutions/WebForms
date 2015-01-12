package com.biit.webforms.persistence.dao.hibernate;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.biit.webforms.persistence.dao.ISimpleBlockViewDao;
import com.biit.webforms.persistence.entity.SimpleBlockView;

public class SimpleBlockViewDao implements ISimpleBlockViewDao {

	private Class<SimpleBlockView> type;

	private SessionFactory sessionFactory = null;

	public SimpleBlockViewDao() {
		this.type = SimpleBlockView.class;
	}

	public Class<SimpleBlockView> getType() {
		return type;
	}

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getRowCount() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT COUNT(*) FROM tree_blocks");

		List<Object[]> rows = query.list();
		session.getTransaction().commit();

		for (Object[] row : rows) {
			return (int) row[0];
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleBlockView> getAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session
				.createSQLQuery("SELECT tf.ID, tf.name, tf.label, tf.version, tf.creationTime, tf.createdBy, tf.updateTime, tf.updatedBy, tf.comparationId, tf.organizationId, max.maxversion "
						+ "FROM tree_blocks tf INNER JOIN "
						+ "(SELECT MAX(version) AS maxversion, label, organizationId FROM tree_blocks "
						+ "GROUP BY label, organizationId) AS max  ON max.label = tf.label and max.organizationId = tf.organizationId "
						+ "ORDER BY label, tf.version DESC;");

		List<Object[]> rows = query.list();

		session.getTransaction().commit();

		List<SimpleBlockView> blocksViews = new ArrayList<>();
		for (Object[] row : rows) {
			SimpleBlockView blockView = new SimpleBlockView();
			blockView.setId(((BigInteger) row[0]).longValue());
			blockView.setName((String) row[1]);
			blockView.setLabel((String) row[2]);
			blockView.setVersion((Integer) row[3]);
			blockView.setCreationTime((Timestamp) row[4]);
			if (row[4] != null) {
				blockView.setCreatedBy(((Double) row[5]).longValue());
			}
			blockView.setUpdateTime((Timestamp) row[6]);
			if (row[6] != null) {
				blockView.setUpdatedBy(((Double) row[7]).longValue());
			}
			blockView.setComparationId((String) row[8]);
			blockView.setOrganizationId(((Double) row[9]).longValue());

			blockView.setLastVersion((Integer) row[10] == (Integer) row[3]);

			blocksViews.add(blockView);
		}

		return blocksViews;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleBlockView> getAll(Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session
				.createSQLQuery("SELECT tf.ID, tf.name, tf.label, tf.version, tf.creationTime, tf.createdBy, tf.updateTime, tf.updatedBy, tf.comparationId, tf.organizationId, max.maxversion "
						+ "FROM tree_blocks tf INNER JOIN "
						+ "(SELECT MAX(version) AS maxversion, label, organizationId FROM tree_blocks WHERE organizationId="
						+ organizationId
						+ " GROUP BY label, organizationId) AS max  ON max.label = tf.label and max.organizationId = tf.organizationId "
						+ "ORDER BY label, tf.version DESC;");

		List<Object[]> rows = query.list();

		session.getTransaction().commit();

		List<SimpleBlockView> blocksViews = new ArrayList<>();
		for (Object[] row : rows) {
			SimpleBlockView blockView = new SimpleBlockView();
			blockView.setId(((BigInteger) row[0]).longValue());
			blockView.setName((String) row[1]);
			blockView.setLabel((String) row[2]);
			blockView.setVersion((Integer) row[3]);
			blockView.setCreationTime((Timestamp) row[4]);
			if (row[4] != null) {
				blockView.setCreatedBy(((Double) row[5]).longValue());
			}
			blockView.setUpdateTime((Timestamp) row[6]);
			if (row[6] != null) {
				blockView.setUpdatedBy(((Double) row[7]).longValue());
			}
			blockView.setComparationId((String) row[8]);
			blockView.setOrganizationId(((Double) row[9]).longValue());

			blockView.setLastVersion((Integer) row[10] == (Integer) row[3]);

			blocksViews.add(blockView);
		}

		return blocksViews;
	}
}
