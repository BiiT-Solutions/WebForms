package com.biit.webforms.gui.webpages.formmanager;

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

import com.biit.form.entity.IBaseFormView;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.TreeTableBaseForm;
import com.biit.webforms.gui.common.components.TreeTableProvider;
import com.biit.webforms.gui.common.components.WindowProceedAction;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.utils.RootForm;
import com.biit.webforms.gui.exceptions.NotEnoughRightsToChangeStatusException;
import com.biit.webforms.language.FormWorkStatusUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.security.WebformsActivity;
import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

public class TreeTableFormVersion extends TreeTableBaseForm<SimpleFormView> {
    private static final long serialVersionUID = -7776688515497328826L;

    enum TreeTableFormVersionProperties {
        ACCESS, USED_BY, STATUS, PUBLISHED, LINKED_FORM, LINKED_ORGANIZATION, LINKED_VERSIONS;
    }

    public TreeTableFormVersion(TreeTableProvider<SimpleFormView> formProvider, IconProviderFormLinked iconProviderFormLinked) {
        super(formProvider, iconProviderFormLinked);
        configureContainerProperties();
        setImmediate(true);
    }

    @Override
    protected void configureContainerProperties() {
        super.configureContainerProperties();

        addContainerProperty(TreeTableFormVersionProperties.ACCESS, String.class, "",
                ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_ACCESS), null, Align.CENTER);

        addContainerProperty(TreeTableFormVersionProperties.STATUS, ComboBox.class, "",
                ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_STATUS), null, Align.CENTER);

        addContainerProperty(TreeTableFormVersionProperties.LINKED_FORM, String.class, "",
                ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_LINKED_FORM), null, Align.CENTER);

        addContainerProperty(TreeTableFormVersionProperties.USED_BY, String.class, "",
                ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_USEDBY), null, Align.CENTER);

        addContainerProperty(TreeTableFormVersionProperties.PUBLISHED, String.class, false,
                ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_PUBLISHED), null, Align.CENTER);

        addContainerProperty(TreeTableFormVersionProperties.LINKED_ORGANIZATION, String.class, "",
                ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_LINKED_ORGANIZATION), null, Align.CENTER);

        addContainerProperty(TreeTableFormVersionProperties.LINKED_VERSIONS, String.class, "",
                ServerTranslate.translate(LanguageCodes.FORM_TABLE_COLUMN_LINKED_VERSIONS), null, Align.CENTER);

        setColumnCollapsible(TreeTableFormVersionProperties.ACCESS, true);
        setColumnCollapsible(TreeTableFormVersionProperties.USED_BY, true);
        setColumnCollapsible(TreeTableFormVersionProperties.PUBLISHED, true);
        setColumnCollapsible(TreeTableFormVersionProperties.STATUS, true);
        setColumnCollapsible(TreeTableFormVersionProperties.LINKED_FORM, true);
        setColumnCollapsible(TreeTableFormVersionProperties.LINKED_ORGANIZATION, true);
        setColumnCollapsible(TreeTableFormVersionProperties.LINKED_VERSIONS, true);

        setColumnExpandRatio(TreeTableFormVersionProperties.ACCESS, 1);
        setColumnExpandRatio(TreeTableFormVersionProperties.USED_BY, 1);
        setColumnExpandRatio(TreeTableFormVersionProperties.PUBLISHED, 0.8f);
        setColumnExpandRatio(TreeTableFormVersionProperties.STATUS, 1.2f);
        setColumnExpandRatio(TreeTableFormVersionProperties.LINKED_FORM, 1.2f);
        setColumnExpandRatio(TreeTableFormVersionProperties.LINKED_VERSIONS, 1.0f);

        setColumnCollapsed(TreeTableFormVersionProperties.LINKED_ORGANIZATION, true);

        // Set new visibility order
        setVisibleColumns(TreeTableBaseFormProperties.FORM_LABEL, TreeTableBaseFormProperties.VERSION,
                TreeTableFormVersionProperties.ACCESS, TreeTableFormVersionProperties.USED_BY,
                TreeTableFormVersionProperties.PUBLISHED, TreeTableFormVersionProperties.STATUS,
                TreeTableBaseFormProperties.ORGANIZATION, TreeTableFormVersionProperties.LINKED_FORM,
                TreeTableFormVersionProperties.LINKED_ORGANIZATION, TreeTableFormVersionProperties.LINKED_VERSIONS,
                TreeTableBaseFormProperties.CREATED_BY, TreeTableBaseFormProperties.CREATION_DATE,
                TreeTableBaseFormProperties.MODIFIED_BY, TreeTableBaseFormProperties.MODIFICATION_DATE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Item updateRow(IBaseFormView form) {
        Item item = super.updateRow(form);
        item.getItemProperty(TreeTableFormVersionProperties.ACCESS).setValue(getFormPermissionsTag((SimpleFormView) form));

        IUser<Long> userOfForm = UiAccesser.getUserUsingForm(form);
        if (userOfForm != null) {
            item.getItemProperty(TreeTableFormVersionProperties.USED_BY).setValue(userOfForm.getEmailAddress());
        } else {
            item.getItemProperty(TreeTableFormVersionProperties.USED_BY).setValue("");
        }

        item.getItemProperty(TreeTableFormVersionProperties.PUBLISHED).setValue(((SimpleFormView) form).isPublished() ? "✓" : "");

        // Status
        item.getItemProperty(TreeTableFormVersionProperties.STATUS).setValue(generateStatusComboBox((IWebformsFormView) form));

        // Linked parameters
        item.getItemProperty(TreeTableFormVersionProperties.LINKED_FORM).setValue(((IWebformsFormView) form).getLinkedFormLabel());

        if (((IWebformsFormView) form).getLinkedFormOrganizationId() != null) {
            IGroup<Long> linkedOrganization = getWebformsSecurityService().getOrganization(UserSession.getUser(),
                    ((IWebformsFormView) form).getLinkedFormOrganizationId());
            if (linkedOrganization != null) {
                item.getItemProperty(TreeTableFormVersionProperties.LINKED_ORGANIZATION).setValue(linkedOrganization.getUniqueName());
            }
        }

        item.getItemProperty(TreeTableFormVersionProperties.LINKED_VERSIONS).setValue(
                ((IWebformsFormView) form).getLinkedFormVersions().toString().replace("[", "").replace("]", ""));

        return item;
    }

    private Component generateStatusComboBox(final IWebformsFormView form) {
        final ComboBox statusComboBox = new ComboBox();
        statusComboBox.setNullSelectionAllowed(false);
        for (FormWorkStatusUi formStatus : FormWorkStatusUi.values()) {
            statusComboBox.addItem(formStatus.getFormWorkStatus());
            statusComboBox.setItemCaption(formStatus.getFormWorkStatus(), formStatus.getLanguageCode().translation());
        }
        statusComboBox.setValue(form.getStatus());
        statusComboBox.setWidth("100%");
        statusComboBox.setImmediate(true);

        boolean userCanUpgradeStatus = getWebformsSecurityService().isAuthorizedActivity(UserSession.getUser(), form,
                WebformsActivity.FORM_STATUS_DOWNGRADE);

        statusComboBox.setEnabled(userCanUpgradeStatus);
        statusComboBox.addValueChangeListener((ValueChangeListener) event -> {
            if (form.getStatus() != null && form.getStatus().equals(statusComboBox.getValue())) {
                // It's the same status. Don't do anything.
                return;
            }

            new WindowProceedAction(LanguageCodes.CAPTION_PROCEED_MODIFY_STATUS, window -> {
                changeStatus(form, statusComboBox, (FormWorkStatus) statusComboBox.getValue());
                form.setStatus((FormWorkStatus) statusComboBox.getValue());
                updateRow(form);
            }, window -> statusComboBox.setValue(form.getStatus()));

        });

        return statusComboBox;
    }

    private void changeStatus(IWebformsFormView form, ComboBox statusComboBox, FormWorkStatus value) {
        updateRow(form);
        try {
            ApplicationUi.getController().changeFormStatus(form, value);
        } catch (NotEnoughRightsToChangeStatusException e) {
            statusComboBox.setValue(form.getStatus());
            MessageManager.showWarning(LanguageCodes.ERROR_CAPTION_NOT_ALLOWED, LanguageCodes.ERROR_DESCRIPTION_NOT_ENOUGH_RIGHTS);
        } catch (ElementCannotBePersistedException e) {
            MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
            WebformsUiLogger.errorMessage(this.getClass().getName(), e);
        }
    }

    /**
     * This function selects the last form used by the user or the first.
     */
    public void selectLastUsedForm() {
        try {
            if (ApplicationUi.getController().getLastEditedForm() != null) {
                // Update form with new object if the form has change.
                selectForm(ApplicationUi.getController().getLastEditedForm());
            } else {
                // Select default one.
                selectFirstRow();
            }
        } catch (Exception e) {
            // Select default one.
            selectFirstRow();
        }
    }

    /**
     * This function returns an string with read only if the form can't be
     * edited by the user
     *
     * @param form
     * @return
     */
    private String getFormPermissionsTag(SimpleFormView form) {
        if (UiAccesser.getUserUsingForm(form) != null && !UiAccesser.getUserUsingForm(form).equals(UserSession.getUser())) {
            return LanguageCodes.CAPTION_IN_USE.translation();
        }

        if (!getWebformsSecurityService().isAuthorizedToForm(form, UserSession.getUser()) && (form.getStatus() == null
                || form.getStatus().equals(FormWorkStatus.DESIGN))) {
            return LanguageCodes.CAPTION_READ_ONLY.translation();
        }
        return "";
    }

    public void selectForm(IBaseFormView form) {
        if (form == null || form.getName() == null) {
            setValue(null);
        } else {
            SimpleFormView simpleFormView = SimpleFormView.getSimpleFormView((IWebformsFormView) form);
            setValue(simpleFormView);
            select(simpleFormView);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Item updateRow(RootForm form) {
        final Item item = super.updateRow(form);
        if (item != null) {
            item.getItemProperty(TreeTableFormVersionProperties.PUBLISHED).setValue("");
        }
        return item;
    }

}
