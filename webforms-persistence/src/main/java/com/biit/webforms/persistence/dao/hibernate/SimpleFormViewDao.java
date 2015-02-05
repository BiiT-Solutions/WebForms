package com.biit.webforms.persistence.dao.hibernate;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.persistence.dao.ISimpleFormViewDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.SimpleFormView;

@Repository
public class SimpleFormViewDao implements ISimpleFormViewDao {

	private Class<SimpleFormView> type;

	private SessionFactory sessionFactory = null;

	public SimpleFormViewDao() {
		this.type = SimpleFormView.class;
	}

	public Class<SimpleFormView> getType() {
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
		SQLQuery query = session.createSQLQuery("SELECT COUNT(*) FROM tree_forms");

		List<Object[]> rows = query.list();
		session.getTransaction().commit();

		for (Object[] row : rows) {
			return (int) row[0];
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleFormView> getAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session
				.createSQLQuery("SELECT tf.ID, tf.name, tf.label, tf.version, tf.creationTime, tf.createdBy, tf.updateTime, tf.updatedBy, tf.comparationId, tf.organizationId, tf.linkedFormLabel, tf.linkedFormOrganizationId, tf.status, max.maxversion "
						+ "FROM tree_forms tf INNER JOIN "
						+ "(SELECT MAX(version) AS maxversion, label, organizationId FROM tree_forms "
						+ "GROUP BY label, organizationId) AS max  ON max.label = tf.label and max.organizationId = tf.organizationId "
						+ "ORDER BY label, tf.version DESC;");

		List<Object[]> rows = query.list();

		session.getTransaction().commit();

		List<SimpleFormView> formViews = new ArrayList<>();
		for (Object[] row : rows) {
			SimpleFormView formView = new SimpleFormView();
			formView.setId(((BigInteger) row[0]).longValue());
			formView.setName((String) row[1]);
			formView.setLabel((String) row[2]);
			formView.setVersion((Integer) row[3]);
			formView.setCreationTime((Timestamp) row[4]);
			if (row[4] != null) {
				formView.setCreatedBy(((Double) row[5]).longValue());
			}
			formView.setUpdateTime((Timestamp) row[6]);
			if (row[6] != null) {
				formView.setUpdatedBy(((Double) row[7]).longValue());
			}
			formView.setComparationId((String) row[8]);
			formView.setOrganizationId(((Double) row[9]).longValue());
			if (row[10] != null) {
				formView.setLinkedFormLabel((String) row[10]);
			}
			if (row[11] != null) {
				formView.setLinkedFormOrganizationId(((BigInteger) row[11]).longValue());
			}

			if (row[12] != null) {
				formView.setStatus(FormWorkStatus.getFromString((String) row[12]));
			}

			formView.setLastVersion((Integer) row[13] == (Integer) row[3]);

			formView.setLinkedFormVersions(getLinkedFormVersions(formView.getId()));

			formViews.add(formView);
		}

		return formViews;
	}

	@SuppressWarnings("unchecked")
	private Set<Integer> getLinkedFormVersions(long formId) {
		Set<Integer> linkedVersions = new HashSet<>();

		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session.createSQLQuery("SELECT linkedFormVersions FROM linked_form_versions WHERE formId="
				+ formId);
		List<Integer> rows = query.list();

		session.getTransaction().commit();

		for (Integer row : rows) {
			linkedVersions.add(row);
		}

		return linkedVersions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleFormView> getFormsThatUse(Block block) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session
				.createSQLQuery("SELECT tf.ID, tf.name, tf.label, tf.version, tf.creationTime, tf.createdBy, tf.updateTime, tf.updatedBy, tf.comparationId, tf.organizationId, tf.linkedFormLabel, tf.linkedFormOrganizationId, tf.status, max.maxversion "
						+ " FROM tree_forms tf "
						+ " INNER JOIN (SELECT MAX(version) AS maxversion, label, organizationId FROM tree_forms "
						+ " GROUP BY label, organizationId) AS max  ON max.label = tf.label and max.organizationId = tf.organizationId "
						+ " WHERE EXISTS "
						+ " (SELECT * FROM tree_blocks_references tr WHERE tr.parent_ID=tf.ID AND EXISTS "
						+ " (SELECT * FROM tree_blocks tb WHERE tr.reference_ID="
						+ block.getId()
						+ ")) "
						+ " ORDER BY label, tf.version DESC;");

		List<Object[]> rows = query.list();

		session.getTransaction().commit();

		List<SimpleFormView> formViews = new ArrayList<>();
		for (Object[] row : rows) {
			SimpleFormView formView = new SimpleFormView();
			formView.setId(((BigInteger) row[0]).longValue());
			formView.setName((String) row[1]);
			formView.setLabel((String) row[2]);
			formView.setVersion((Integer) row[3]);
			formView.setCreationTime((Timestamp) row[4]);
			if (row[4] != null) {
				formView.setCreatedBy(((Double) row[5]).longValue());
			}
			formView.setUpdateTime((Timestamp) row[6]);
			if (row[6] != null) {
				formView.setUpdatedBy(((Double) row[7]).longValue());
			}
			formView.setComparationId((String) row[8]);
			formView.setOrganizationId(((Double) row[9]).longValue());
			if (row[10] != null) {
				formView.setLinkedFormLabel((String) row[10]);
			}
			if (row[11] != null) {
				formView.setLinkedFormOrganizationId(((BigInteger) row[11]).longValue());
			}

			if (row[12] != null) {
				formView.setStatus(FormWorkStatus.getFromString((String) row[12]));
			}

			formView.setLastVersion((Integer) row[13] == (Integer) row[3]);

			formView.setLinkedFormVersions(getLinkedFormVersions(formView.getId()));

			formViews.add(formView);
		}

		return formViews;
	}
}
