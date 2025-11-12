package com.biit.webforms.persistence.dao.hibernate;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.biit.webforms.persistence.dao.ISimpleBlockViewDao;
import com.biit.webforms.persistence.entity.SimpleBlockView;

@Repository
public class SimpleBlockViewDao implements ISimpleBlockViewDao {

	@PersistenceContext(unitName = "webformsPersistenceUnit")
	@Qualifier(value = "webformsManagerFactory")
	private EntityManager entityManager;
	
	private Class<SimpleBlockView> type;

	public SimpleBlockViewDao() {
		this.type = SimpleBlockView.class;
	}

	public Class<SimpleBlockView> getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getRowCount() {
		Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM tree_blocks");

		List<Object[]> rows = query.getResultList();
		for (Object[] row : rows) {
			return (int) row[0];
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleBlockView> getAll() {
		Query query = entityManager.createNativeQuery("SELECT tf.id, tf.name, tf.label, tf.version, tf.creation_time, tf.created_by, tf.update_time, tf.updated_by, tf.comparation_id, tf.organization_id, max.maxversion "
						+ "FROM tree_blocks tf INNER JOIN "
						+ "(SELECT MAX(version) AS maxversion, label, organization_id FROM tree_blocks "
						+ "GROUP BY label, organization_id) AS max  ON max.label = tf.label and max.organization_id = tf.organization_id "
						+ "ORDER BY label, tf.version DESC;");

		List<Object[]> rows = query.getResultList();

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

			blockView.setLastVersion(((Integer) row[10]).equals((Integer) row[3]));

			blocksViews.add(blockView);
		}

		return blocksViews;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleBlockView> getAll(Long organizationId) {
		Query query = entityManager.createNativeQuery("SELECT tf.id, tf.name, tf.label, tf.version, tf.creation_time, tf.created_by, tf.update_time, tf.updated_by, tf.comparation_id, tf.organization_id, max.maxversion "
						+ "FROM tree_blocks tf INNER JOIN "
						+ "(SELECT MAX(version) AS maxversion, label, organization_id FROM tree_blocks WHERE organization_id="
						+ organizationId
						+ " GROUP BY label, organization_id) AS max  ON max.label = tf.label and max.organization_id = tf.organization_id "
						+ "ORDER BY label, tf.version DESC;");

		List<Object[]> rows = query.getResultList();
		
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

			blockView.setLastVersion(((Integer) row[10]).equals((Integer) row[3]));

			blocksViews.add(blockView);
		}

		return blocksViews;
	}
}
