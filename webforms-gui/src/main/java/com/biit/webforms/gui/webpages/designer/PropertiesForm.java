package com.biit.webforms.gui.webpages.designer;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
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

import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.providers.FormProvider;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.server.VaadinServlet;

public class PropertiesForm extends PropertiesBaseForm<Form> {
    private static final long serialVersionUID = -1665998885081665306L;

    private final FormProvider formProvider;

    public PropertiesForm() {
        super(Form.class);
        SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
        formProvider = (FormProvider) helper.getBean("formProvider");
    }

    @Override
    public void updateElement() {
        if (getLabelTextField().isValid() && getLabelTextField().getValue() != null && !getLabelTextField().getValue().isEmpty()) {
            try {
                // Checks if already exists a form with this label and its
                // version.
                if (!formProvider.exists(getLabelTextField().getValue(), (getInstance()).getVersion(),
                        getInstance().getOrganizationId(), getInstance().getId())) {
                    ApplicationUi.getController().updateForm(getInstance(), getLabelTextField().getValue(),
                            getDescriptionTextArea().getValue(), getImage(), getVideo(), getAudio(), getDisableEdition().getValue());
                } else {
                    getLabelTextField().setValue((getInstance()).getLabel());
                    MessageManager.showWarning(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE,
                            LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE_DESCRIPTION);
                    ApplicationUi.getController().updateForm(getInstance(), (getInstance()).getLabel(),
                            getDescriptionTextArea().getValue(), getImage(), getVideo(), getAudio(), getDisableEdition().getValue());
                }
            } catch (ReadOnlyException e) {
                MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE);
                WebformsUiLogger.errorMessage(this.getClass().getName(), e);
            }
            super.updateElement();
        }
    }

}
