package com.biit.webforms.persistence.dao.hibernate;

import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.persistence.dao.ISimpleFormViewDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.persistence.entity.SimpleFormViewWithContent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class SimpleFormViewDao implements ISimpleFormViewDao {

    private Class<SimpleFormView> type;

    @PersistenceContext(unitName = "webformsPersistenceUnit")
    @Qualifier(value = "webformsManagerFactory")
    private EntityManager entityManager;

    public SimpleFormViewDao() {
        this.type = SimpleFormView.class;
    }

    public Class<SimpleFormView> getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getRowCount() {
        Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM tree_forms");

        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            return (int) row[0];
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SimpleFormView> getAll() {
        Query query = entityManager
                .createNativeQuery("SELECT tf.id, tf.name, tf.label, tf.version, tf.creation_time, tf.created_by, tf.update_time, tf.updated_by, tf.comparation_id, tf.organization_id, tf.linked_form_label, tf.linked_form_organization_id, tf.status, tf.form_reference, max.maxversion, json is not null "
                        + "FROM tree_forms tf INNER JOIN "
                        + "(SELECT MAX(version) AS maxversion, label, organization_id FROM tree_forms "
                        + "GROUP BY label, organization_id) AS max  ON max.label = tf.label and max.organization_id = tf.organization_id "
                        + "ORDER BY label, tf.version DESC;");

        List<Object[]> rows = query.getResultList();

        List<SimpleFormView> formViews = new ArrayList<>();
        for (Object[] row : rows) {
            SimpleFormView formView = new SimpleFormView();
            formView.setId(((Number) row[0]).longValue());
            formView.setName((String) row[1]);
            formView.setLabel((String) row[2]);
            formView.setVersion((Integer) row[3]);
            formView.setCreationTime((Timestamp) row[4]);
            if (row[5] != null) {
                formView.setCreatedBy(((Double) row[5]).longValue());
            }
            formView.setUpdateTime((Timestamp) row[6]);
            if (row[7] != null) {
                formView.setUpdatedBy(((Double) row[7]).longValue());
            }
            formView.setComparationId((String) row[8]);
            formView.setOrganizationId(((Double) row[9]).longValue());
            if (row[10] != null) {
                formView.setLinkedFormLabel((String) row[10]);
            }
            if (row[11] != null) {
                formView.setLinkedFormOrganizationId(((Number) row[11]).longValue());
            }

            if (row[12] != null) {
                formView.setStatus(FormWorkStatus.from((String) row[12]));
            }

            if (row[13] != null) {
                formView.setFormReferenceId(((BigInteger) row[13]).longValue());
            }

            formView.setLastVersion(row[14].equals(row[3]));

            formView.setHasJson(row[15] != null && ((Number) row[15]).intValue() > 0);

            formView.setLinkedFormVersions(getLinkedFormVersions(formView.getId()));

            formViews.add(formView);
        }

        return formViews;
    }

    @Override
    public SimpleFormViewWithContent get(Long id) {
        Query query = entityManager
                .createNativeQuery("SELECT tf.id, tf.name, tf.label, tf.version, tf.creation_time, tf.created_by, tf.update_time, tf.updated_by, tf.comparation_id, tf.organization_id, tf.linked_form_label, tf.linked_form_organization_id, tf.status, tf.form_reference, tf.json "
                        + "FROM tree_forms tf WHERE tf.id='" + id + "'");

        Object[] row = (Object[]) query.getSingleResult();

        SimpleFormViewWithContent formView = new SimpleFormViewWithContent();
        formView.setId(id);
        formView.setName((String) row[1]);
        formView.setLabel((String) row[2]);
        formView.setVersion((Integer) row[3]);
        formView.setCreationTime((Timestamp) row[4]);
        if (row[5] != null) {
            formView.setCreatedBy(((Number) row[5]).longValue());
        }
        formView.setUpdateTime((Timestamp) row[6]);
        if (row[7] != null) {
            formView.setUpdatedBy(((Number) row[7]).longValue());
        }
        formView.setComparationId((String) row[8]);
        formView.setOrganizationId(((Number) row[9]).longValue());
        if (row[10] != null) {
            formView.setLinkedFormLabel((String) row[10]);
        }
        if (row[11] != null) {
            formView.setLinkedFormOrganizationId(((Number) row[11]).longValue());
        }

        if (row[12] != null) {
            formView.setStatus(FormWorkStatus.from((String) row[12]));
        }

        if (row[13] != null) {
            formView.setFormReferenceId(((Number) row[13]).longValue());
        }

        formView.setHasJson(row[14] != null);
        formView.setJson((String)  row[14]);

        formView.setLinkedFormVersions(getLinkedFormVersions(formView.getId()));

        return formView;
    }

    @SuppressWarnings("unchecked")
    private Set<Integer> getLinkedFormVersions(long formId) {
        Set<Integer> linkedVersions = new HashSet<>();

        Query query = entityManager.createNativeQuery("SELECT linked_form_versions FROM linked_form_versions WHERE form_id=" + formId);
        List<Object> rows = query.getResultList();

        for (Object row : rows) {
            linkedVersions.add((Integer) row);
        }
        return linkedVersions;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SimpleFormView> getFormsThatUse(Block block) {
        Query query = entityManager
                .createNativeQuery("SELECT tf.id, tf.name, tf.label, tf.version, tf.creation_time, tf.created_by, tf.update_time, tf.updated_by, tf.comparation_id, tf.organization_id, tf.linked_form_label, tf.linked_form_organization_id, tf.status, max.maxversion "
                        + " FROM tree_forms tf "
                        + " INNER JOIN (SELECT MAX(version) AS maxversion, label, organization_id FROM tree_forms "
                        + " GROUP BY label, organization_id) AS max  ON max.label = tf.label and max.organization_id = tf.organization_id "
                        + " WHERE EXISTS "
                        + " (SELECT * FROM tree_blocks_references tr WHERE tr.parent=tf.id AND EXISTS "
                        + " (SELECT * FROM tree_blocks tb WHERE tr.reference=" + block.getId() + ")) " + " ORDER BY label, tf.version DESC;");

        List<Object[]> rows = query.getResultList();

        List<SimpleFormView> formViews = new ArrayList<>();
        for (Object[] row : rows) {
            SimpleFormView formView = new SimpleFormView();
            formView.setId(((Number) row[0]).longValue());
            formView.setName((String) row[1]);
            formView.setLabel((String) row[2]);
            formView.setVersion((Integer) row[3]);
            formView.setCreationTime((Timestamp) row[4]);
            if (row[4] != null) {
                formView.setCreatedBy(((Number) row[5]).longValue());
            }
            formView.setUpdateTime((Timestamp) row[6]);
            if (row[6] != null) {
                formView.setUpdatedBy(((Double) row[7]).longValue());
            }
            formView.setComparationId((String) row[8]);
            formView.setOrganizationId(((Number) row[9]).longValue());
            if (row[10] != null) {
                formView.setLinkedFormLabel((String) row[10]);
            }
            if (row[11] != null) {
                formView.setLinkedFormOrganizationId(((Number) row[11]).longValue());
            }

            if (row[12] != null) {
                formView.setStatus(FormWorkStatus.from((String) row[12]));
            }

            formView.setLastVersion(row[13].equals(row[3]));

            formView.setLinkedFormVersions(getLinkedFormVersions(formView.getId()));

            formViews.add(formView);
        }

        return formViews;
    }
}
