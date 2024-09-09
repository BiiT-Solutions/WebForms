package com.biit.webforms.persistence.dao.hibernate;

import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.webforms.persistence.dao.IPublishedFormDao;
import com.biit.webforms.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.webforms.persistence.entity.PublishedForm;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PublishedFormDao extends AnnotatedGenericDao<PublishedForm, Long> implements IPublishedFormDao {

    public PublishedFormDao() {
        super(PublishedForm.class);
    }


    @Override
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
    public void makeTransient(PublishedForm form) throws ElementCannotBeRemovedException {
        PublishedForm mergedForm = getEntityManager().contains(form) ? form : getEntityManager().merge(form);
        makePersistent(mergedForm);
        super.makeTransient(mergedForm);
    }


    @Override
    @CachePut(value = "webformsforms", key = "#form.getId()", condition = "#form.getId() != null")
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public PublishedForm merge(PublishedForm form) {
        if (form.getCreationTime() == null) {
            form.setCreationTime();
        }
        if (form.getUpdateTime() == null) {
            form.setUpdateTime();
        }

        return super.merge(form);
    }


    @Override
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public PublishedForm makePersistent(PublishedForm form) {
        if (form.getCreationTime() == null) {
            form.setCreationTime();
        }
        return super.makePersistent(form);
    }


    @Override
    @Transactional(value = "webformsTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
    public PublishedForm get(String label, Integer version, Long organizationId) throws MultiplesFormsFoundException {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PublishedForm> criteriaQuery = criteriaBuilder.createQuery(PublishedForm.class);
        // Metamodel of the entity table
        Metamodel metamodel = getEntityManager().getMetamodel();
        EntityType<PublishedForm> formMetamodel = metamodel.entity(PublishedForm.class);
        Root<PublishedForm> typesRoot = criteriaQuery.from(PublishedForm.class);

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
            List<PublishedForm> resultForms = getEntityManager().createQuery(criteriaQuery).getResultList();

            if (resultForms.size() > 1) {
                throw new MultiplesFormsFoundException();
            } else if (resultForms.isEmpty()) {
                return null;
            }
            return resultForms.get(0);
        } catch (NoResultException e) {
            return null;
        }
    }

}
