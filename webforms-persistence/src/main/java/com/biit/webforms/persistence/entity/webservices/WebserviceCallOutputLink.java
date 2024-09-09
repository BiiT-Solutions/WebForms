package com.biit.webforms.persistence.entity.webservices;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.serialization.WebserviceCallOutputLinkDeserializer;
import com.biit.webforms.serialization.WebserviceCallOutputLinkSerializer;
import com.biit.webforms.webservices.WebservicePort;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@JsonDeserialize(using = WebserviceCallOutputLinkDeserializer.class)
@JsonSerialize(using = WebserviceCallOutputLinkSerializer.class)
@Table(name = "webservice_call_output_link")
public class WebserviceCallOutputLink extends WebserviceCallLink {
    private static final long serialVersionUID = -107489603701913849L;

    private static final boolean EDITABLE_DEFAULT_VALUE = false;

    @Column(name = "editable")
    private boolean isEditable;

    public WebserviceCallOutputLink() {
        super();
        setEditable(EDITABLE_DEFAULT_VALUE);
    }

    public WebserviceCallOutputLink(WebservicePort port) {
        super(port);
        setEditable(EDITABLE_DEFAULT_VALUE);
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        super.copyData(object);
        if (object instanceof WebserviceCallOutputLink) {
            WebserviceCallOutputLink link = (WebserviceCallOutputLink) object;
            setEditable(link.isEditable());
        } else {
            throw new NotValidStorableObjectException("Element of class '" + object.getClass().getName() + "' is not compatible with '"
                    + this.getClass().getName() + "'");
        }
    }

    @Override
    public void clear() {
        super.clear();
        setEditable(EDITABLE_DEFAULT_VALUE);
    }

    @Override
    public WebserviceCallLink generateCopy() throws NotValidStorableObjectException {
        WebserviceCallOutputLink link = new WebserviceCallOutputLink();
        link.copyData(this);
        return link;
    }



}
