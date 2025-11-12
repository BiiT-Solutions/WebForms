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
