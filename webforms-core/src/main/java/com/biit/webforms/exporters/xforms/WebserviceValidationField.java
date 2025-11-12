package com.biit.webforms.exporters.xforms;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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
import com.biit.webforms.persistence.entity.ElementWithMedia;
import com.biit.webforms.persistence.entity.TreeObjectAudio;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.entity.TreeObjectVideo;

/**
 * Class to hold the information of a validation link.
 */
public class WebserviceValidationField extends BaseQuestion implements ElementWithMedia {

    private static final long serialVersionUID = -1264035729915575376L;

    private final BaseQuestion question;

    public WebserviceValidationField(BaseQuestion question) {
        this.question = question;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        // This method is not implemented this class is not intended to be used
        // as a member of the form.
    }

    public BaseQuestion getQuestion() {
        return question;
    }

    @Override
    public String getName() {
        return question.getName() + "-validation";
    }

    @Override
    public void setImage(TreeObjectImage image) {
        // Do nothing
    }

    @Override
    public TreeObjectImage getImage() {
        return null;
    }

    @Override
    public void setVideo(TreeObjectVideo video) {
        // Do nothing
    }

    @Override
    public TreeObjectVideo getVideo() {
        return null;
    }

    @Override
    public void setAudio(TreeObjectAudio audio) {
        // Do nothing
    }

    @Override
    public TreeObjectAudio getAudio() {
        return null;
    }
}
