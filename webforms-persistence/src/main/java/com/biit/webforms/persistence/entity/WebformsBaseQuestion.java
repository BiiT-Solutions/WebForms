package com.biit.webforms.persistence.entity;

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

import com.biit.form.entity.BaseQuestion;
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

        for (DynamicAnswer child : form.getAllChildrenInHierarchy(DynamicAnswer.class)) {
            if (Objects.equals(child.getReference(), this)) {
                throw new DependencyDynamicAnswerExistException("Question '" + child.getReference() + "' depends of element + '" + this + "'");
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
