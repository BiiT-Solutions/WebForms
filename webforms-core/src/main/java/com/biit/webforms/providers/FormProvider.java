package com.biit.webforms.providers;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.dao.ISimpleFormViewDao;
import com.biit.webforms.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.persistence.entity.SimpleFormViewWithContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormProvider {

    @Autowired
    private IFormDao formDao;

    @Autowired
    private ISimpleFormViewDao simpleFormViewDao;

    public FormProvider() {
        super();
    }


    public Form get(Long formId) {
        return get(simpleFormViewDao.get(formId));
    }

    public Form saveForm(Form form) {
        form.updateChildrenSortSeqs();
        if (form.getCreationTime() == null) {
            form.setCreationTime();
        }
        form.setJsonCode(form.toJson());
        if (form instanceof CompleteFormView) {
            form = ((CompleteFormView) form).getForm();
        }
        //Save from json.
        try {
            Form mutilatedForm = form.copy(form.getCreatedBy(), form.getLabel());
            mutilatedForm.setId(form.getId());
            mutilatedForm.setComparationId(form.getComparationId());
            mutilatedForm.setJsonCode(form.toJson());
            //Delete all children and rules from form to speed up save (as are stored as Json).
            mutilatedForm.getChildren().clear();
            mutilatedForm.getFlows().clear();
            mutilatedForm.getWebserviceCalls().clear();
            mutilatedForm.getLinkedFormVersions().clear();
            mutilatedForm.getElementsToHide().clear();
            mutilatedForm.setImage(null);
            mutilatedForm.setVideo(null);
            mutilatedForm.setAudio(null);
            mutilatedForm.setCreationTime(form.getCreationTime());
            mutilatedForm.setDescriptionTranslations(null);
            if (mutilatedForm.getId() != null) {
                formDao.merge(mutilatedForm);
                return form;
            } else {
                mutilatedForm = formDao.makePersistent(mutilatedForm);
                //Store form id on json. First update form from complete one.
                form = Form.fromJson(mutilatedForm.getJsonCode());
                form.setId(mutilatedForm.getId());
                mutilatedForm.setJsonCode(form.toJson());
                formDao.merge(mutilatedForm);
                return form;
            }
        } catch (CharacterNotAllowedException | NotValidStorableObjectException | FieldTooLongException |
                 JsonProcessingException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }
        //Save old way
        if (form.getId() != null) {
            return formDao.merge(form);
        } else {
            return formDao.makePersistent(form);
        }
    }

    public void deleteForm(Form form) throws ElementCannotBeRemovedException {
        formDao.makeTransient(form);
    }

    public Form get(SimpleFormView simpleFormView) {
        if (simpleFormView == null) {
            return null;
        }
        final SimpleFormViewWithContent simpleFormViewWithContent = simpleFormViewDao.get(simpleFormView.getId());
        if (simpleFormViewWithContent.getJson() != null && !simpleFormViewWithContent.getJson().isEmpty()) {
            try {
                WebformsLogger.debug(this.getClass().getName(), "Obtaining form '" + simpleFormView.getLabel() + "' from json structure.");
                return Form.fromJson(simpleFormViewWithContent.getJson());
            } catch (JsonProcessingException e) {
                WebformsLogger.errorMessage(this.getClass().getName(), e);
                return formDao.get(simpleFormView.getId());
            }
        }
        WebformsLogger.debug(this.getClass().getName(), "Obtaining form '" + simpleFormView.getLabel() + "' from standard database.");
        return formDao.get(simpleFormView.getId());
    }

    public boolean exists(String value, long organizationId) {
        return formDao.exists(value, organizationId);
    }

    public boolean exists(String label, int version, long organizationId, long id) {
        return formDao.exists(label, version, organizationId, id);
    }

    public void evictAllCache() {
        formDao.evictAllCache();
    }

    public String getJson(Long formId) {
        return formDao.getJson(formId);
    }

    public Form get(String label, Integer version, Long organizationId) throws MultiplesFormsFoundException {
        final Form plainForm = formDao.get(label, version, organizationId);
        if (plainForm.getJsonCode() != null && !plainForm.getJsonCode().isEmpty()) {
            try {
                WebformsLogger.debug(this.getClass().getName(), "Obtaining form '" + plainForm.getLabel() + "' from json structure.");
                return Form.fromJson(plainForm.getJsonCode());
            } catch (JsonProcessingException e) {
                WebformsLogger.errorMessage(this.getClass().getName(), e);
            }
        }
        return plainForm;
    }

}
