package com.biit.webforms.persistence.dao.hibernate;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.webforms.persistence.entity.BlockReference;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FormDao extends AnnotatedGenericDao<Form, Long> implements IFormDao {

    public FormDao() {
        super(Form.class);
    }

    public Form prepareForm(Form form) {
        if (form != null) {
            form.initializeSets();
            initializeAndUnproxy(form);
        }
        return form;
    }

    @Override
    @Cacheable(value = "webformsforms", key = "#id")
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public Form get(Long id) {
        Form form = super.get(id);
        prepareForm(form);
        return form;
    }

    /**
     * Hibernate 5 stores the proxy class longer. Must be removed or causes
     * problems later.
     *
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends TreeObject> T initializeAndUnproxy(T entity) {
        if (entity == null) {
            throw new NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) Hibernate.unproxy(entity);
        }

        if (entity instanceof BlockReference) {
            ((BlockReference) entity).setReference(initializeAndUnproxy(((BlockReference) entity).getReference()));
        } else if (!entity.getChildren().isEmpty()) {
            List<TreeObject> children = new ArrayList<>();
            for (TreeObject child : entity.getChildren()) {
                children.add(initializeAndUnproxy(child));
            }
            try {
                entity.setChildren(children);
            } catch (NotValidChildException e) {
                WebformsLogger.errorMessage(FormDao.class.getName(), e);
            }
        }
        return entity;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "webformsforms", key = "#form.getId()", condition = "#form.getId() != null")})
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
    public void makeTransient(Form form) throws ElementCannotBeRemovedException {
        form.getFlows().clear();
        Form mergedForm = getEntityManager().contains(form) ? form : getEntityManager().merge(form);
        makePersistent(mergedForm);
        super.makeTransient(mergedForm);
    }

    @Override
    @CachePut(value = "webformsforms", key = "#form.getId()", condition = "#form.getId() != null")
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Form merge(Form form) {
        form.updateChildrenSortSeqs();
        if (form.getCreationTime() == null) {
            form.setCreationTime();
        }

        Form mergedForm = super.merge(form);
        if (mergedForm != null) {
            //Store the IDs on the JSON.
            mergedForm.setJsonCode(mergedForm.toJson());
            setJson(mergedForm.getId(), mergedForm.getJsonCode());

            mergedForm.initializeSets();
        }
        return mergedForm;
    }

    @Override
    @CachePut(value = "webformsforms", key = "#form.getId()", condition = "#form.getId() != null")
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Form makePersistent(Form form) {
        form.updateChildrenSortSeqs();
        if (form.getCreationTime() == null) {
            form.setCreationTime();
        }
        Form formDB;
        if (form instanceof CompleteFormView) {
            formDB = super.makePersistent(((CompleteFormView) form).getForm());
        } else {
            formDB = super.makePersistent(form);
        }
        return formDB;
    }

    @Override
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public boolean exists(String label, int version, long organizationId, long id) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        // Metamodel of the entity table
        Metamodel m = getEntityManager().getMetamodel();
        EntityType<Form> formMetamodel = m.entity(Form.class);
        Root<Form> form = cq.from(Form.class);

        cq.select(cb.count(form));
        cq.where(cb.and(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)), label),
                cb.equal(form.get(formMetamodel.getSingularAttribute("version", Integer.class)), version),
                cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId),
                cb.notEqual(form.get(formMetamodel.getSingularAttribute("id", Long.class)), id)));

        return getEntityManager().createQuery(cq).getSingleResult() > 0;
    }

    @Override
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public Form get(String label, Integer version, Long organizationId) throws MultiplesFormsFoundException {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Form> criteriaQuery = criteriaBuilder.createQuery(Form.class);
        // Metamodel of the entity table
        Metamodel metamodel = getEntityManager().getMetamodel();
        EntityType<Form> formMetamodel = metamodel.entity(Form.class);
        Root<Form> typesRoot = criteriaQuery.from(Form.class);

        final List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(typesRoot.get(formMetamodel.getSingularAttribute("label", String.class)), label));

        if (version != null) {
            predicates.add(criteriaBuilder.equal(typesRoot.get(formMetamodel.getSingularAttribute("version", Integer.class)), version));
        }

        if (organizationId != null) {
            predicates.add(criteriaBuilder.equal(typesRoot.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId));
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

        try {
            List<Form> resultForms = getEntityManager().createQuery(criteriaQuery).getResultList();

            if (resultForms.size() > 1) {
                throw new MultiplesFormsFoundException();
            } else if (resultForms.isEmpty()) {
                return null;
            }
            return prepareForm(resultForms.get(0));
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public boolean exists(String label, long organizationId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        // Metamodel of the entity table
        Metamodel m = getEntityManager().getMetamodel();
        EntityType<Form> formMetamodel = m.entity(Form.class);
        Root<Form> form = cq.from(Form.class);

        cq.select(cb.count(form));
        cq.where(cb.and(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)), label),
                cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId)));
        return getEntityManager().createQuery(cq).getSingleResult() > 0;
    }

    @Override
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public String getJson(Long formId) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        // Metamodel of the entity table
        Metamodel metamodel = getEntityManager().getMetamodel();
        EntityType<Form> formMetamodel = metamodel.entity(Form.class);
        Root<Form> typesRoot = criteriaQuery.from(Form.class);

        final List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(typesRoot.get(formMetamodel.getSingularAttribute("id", Long.class)), formId));

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

        criteriaQuery.select(typesRoot.get("jsonCode"));
        try {
            return getEntityManager().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public int setJson(Long formId, String json) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<Form> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Form.class);
        Root<Form> typesRoot = criteriaUpdate.from(Form.class);

        final List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(typesRoot.get("id"), formId));

        criteriaUpdate.set(typesRoot.get("jsonCode"), json);
        criteriaUpdate.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));
        return getEntityManager().createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public List<Form> getAll() {
        List<Form> forms = super.getAll();
        for (Form form : forms) {
            form.initializeSets();
        }
        return forms;
    }

    @Override
    @CacheEvict(value = "webformsforms", allEntries = true)
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public void evictAllCache() {
        super.evictAllCache();
    }

    @Override
    @Caching(evict = {@CacheEvict(value = "webformsforms", key = "#id", condition = "#id != null")})
    public void evictCache(Long id) {
        // Do nothing.
    }

}
