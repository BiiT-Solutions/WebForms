package com.biit.webforms.persistence.entity.webservices;

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
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.serialization.WebserviceCallDeserializer;
import com.biit.webforms.serialization.WebserviceCallSerializer;
import com.biit.webforms.webservices.Webservice;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonDeserialize(using = WebserviceCallDeserializer.class)
@JsonSerialize(using = WebserviceCallSerializer.class)
@Table(name = "webservice_call")
public class WebserviceCall extends StorableObject {
    private static final long serialVersionUID = -8130775804790464077L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "form")
    private Form form;

    @Column(nullable = false)
    private String name;

    @Column(name = "webservice_name")
    private String webserviceName;

    @ManyToOne()
    @JoinColumn(name = "form_element_trigger")
    private BaseQuestion formElementTrigger;


    //Only for json serialization.
    @Transient
    private transient List<String> formElementTriggerPath;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "webserviceCall")
    private final Set<WebserviceCallInputLink> inputLinks;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "webserviceCall")
    private final Set<WebserviceCallOutputLink> outputLinks;

    private transient boolean readOnly;

    public WebserviceCall() {
        super();
        inputLinks = new HashSet<>();
        outputLinks = new HashSet<>();
    }

    public WebserviceCall(String name, Webservice webservice) {
        super();
        setName(name);
        setWebserviceName(webservice.getName());
        inputLinks = new HashSet<>();
        outputLinks = new HashSet<>();
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        return new HashSet<>();
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof WebserviceCall) {
            copyBasicInfo(object);
            WebserviceCall call = (WebserviceCall) object;
            setName(call.getName());
            setWebserviceName(call.getWebserviceName());
            setFormElementTrigger(call.getFormElementTrigger());
            copyLinkData(inputLinks, call.getInputLinks());
            copyLinkData(outputLinks, call.getOutputLinks());
        } else {
            throw new NotValidStorableObjectException("Element of class '" + object.getClass().getName()
                    + "' is not compatible with '" + WebserviceCall.class.getName() + "'");
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends WebserviceCallLink> void copyLinkData(Set<T> destiny, Set<T> source)
            throws NotValidStorableObjectException {
        destiny.clear();
        for (WebserviceCallLink link : source) {
            T temp = (T) link.generateCopy();
            destiny.add(temp);
            temp.setWebserviceCall(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebserviceName() {
        return webserviceName;
    }

    public void setWebserviceName(String name) {
        this.webserviceName = name;
    }

    public Set<WebserviceCallInputLink> getInputLinks() {
        return inputLinks;
    }

    public void setInputLinks(Set<WebserviceCallInputLink> inputLinks) {
        this.inputLinks.clear();
        for (WebserviceCallInputLink link : inputLinks) {
            addInputLink(link);
        }
    }

    public Set<WebserviceCallOutputLink> getOutputLinks() {
        return outputLinks;
    }

    public void setOutputLinks(Set<WebserviceCallOutputLink> outputLinks) {
        this.outputLinks.clear();
        for (WebserviceCallOutputLink link : outputLinks) {
            addOutputLink(link);
        }
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    /**
     * Question that run the web service.
     */
    public BaseQuestion getFormElementTrigger() {
        return formElementTrigger;
    }

    public void setFormElementTrigger(BaseQuestion formElementTrigger) {
        this.formElementTrigger = formElementTrigger;
    }

    public void addOutputLink(WebserviceCallOutputLink link) {
        outputLinks.add(link);
        link.setWebserviceCall(this);
    }

    public void addInputLink(WebserviceCallInputLink link) {
        inputLinks.add(link);
        link.setWebserviceCall(this);
    }

    public void updateReferences(HashMap<String, BaseQuestion> references) {
        if (getFormElementTrigger() != null) {
            setFormElementTrigger(references.get(getFormElementTrigger().getOriginalReference()));
        }
        for (WebserviceCallLink link : getInputLinks()) {
            link.updateReferences(references);
        }
        for (WebserviceCallLink link : getOutputLinks()) {
            link.updateReferences(references);
        }
    }

    /**
     * Reset 'id' and 'comparationId'
     */
    public void resetIds() {
        super.resetIds();
        for (WebserviceCallLink link : getInputLinks()) {
            link.resetIds();
        }
        for (WebserviceCallLink link : getOutputLinks()) {
            link.resetIds();
        }
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean value) {
        readOnly = value;
    }

    public boolean isUsing(WebformsBaseQuestion question) {
        if (Objects.equals(getFormElementTrigger(), question)) {
            return true;
        }

        for (WebserviceCallLink link : getInputLinks()) {
            if (Objects.equals(link.getFormElement(), question)) {
                return true;
            }
        }

        for (WebserviceCallLink link : getOutputLinks()) {
            if (Objects.equals(link.getFormElement(), question)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getFormElementTriggerPath() {
        return formElementTriggerPath;
    }

    public void setFormElementTriggerPath(List<String> formElementTriggerPath) {
        this.formElementTriggerPath = formElementTriggerPath;
    }
}
