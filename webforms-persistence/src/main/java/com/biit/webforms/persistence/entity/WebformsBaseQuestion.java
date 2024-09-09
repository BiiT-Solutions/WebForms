package com.biit.webforms.persistence.entity;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.exceptions.DependencyDynamicAnswerExistException;
import com.biit.webforms.persistence.entity.exceptions.FlowDependencyExistException;
import com.biit.webforms.persistence.entity.exceptions.WebserviceDependencyExistException;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class WebformsBaseQuestion extends BaseQuestion {
    private static final long serialVersionUID = 5749191470931873398L;

    public WebformsBaseQuestion() {
        super();
    }

    public WebformsBaseQuestion(String name) throws FieldTooLongException, CharacterNotAllowedException {
        super(name);
    }

    @Override
    public void checkDependencies() throws DependencyExistException {
        Form form = (Form) this.getAncestor(Form.class);
        if (form == null) {
            return;
        }

        for (TreeObject child : form.getAllChildrenInHierarchy(DynamicAnswer.class)) {
            DynamicAnswer dynamicAnswer = (DynamicAnswer) child;
            if (Objects.equals(dynamicAnswer.getReference(), this)) {
                throw new DependencyDynamicAnswerExistException("Question '" + dynamicAnswer.getReference() + "' depends of element + '" + this + "'");
            }
        }

        for (Flow flow : form.getFlows()) {
            if (flow.isDependent(this)) {
                throw new FlowDependencyExistException("Flow '" + flow + "' depends of element '" + this + "'");
            }
        }

        for (WebserviceCall call : form.getWebserviceCalls()) {
            if (call.isUsing(this)) {
                throw new WebserviceDependencyExistException("Webservice call '" + call.getName() + "' depends of element '" + this + "'");
            }
        }
    }

    public abstract AnswerFormat getAnswerFormat();

    public abstract AnswerSubformat getAnswerSubformat();

    public abstract AnswerType getAnswerType();

    public abstract boolean isMandatory();

    public abstract int exportToJavaCode(StringBuilder sb, int counter);


}
