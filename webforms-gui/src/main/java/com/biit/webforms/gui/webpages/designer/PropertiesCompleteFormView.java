package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.BaseForm;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.providers.FormProvider;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.server.VaadinServlet;

public class PropertiesCompleteFormView extends PropertiesBaseForm<CompleteFormView> {
    private static final long serialVersionUID = -478559896636685508L;

    private FormProvider formProvider;

    public PropertiesCompleteFormView() {
        super(CompleteFormView.class);
        SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
        formProvider = (FormProvider) helper.getBean("formProvider");
    }

    @Override
    public void updateElement() {
        if (getLabelTextField().isValid() && getLabelTextField().getValue() != null && !getLabelTextField().getValue().isEmpty()) {
            try {
                // Checks if already exists a form with this label and its
                // version.
                if (!formProvider.exists(getLabelTextField().getValue(), getInstance().getVersion(),
                        ((BaseForm) getInstance()).getOrganizationId(), getInstance().getId())) {
                    ApplicationUi.getController().updateForm(getInstance().getForm(), getLabelTextField().getValue(),
                            getDescriptionTextArea().getValue(), getImage(), getVideo(), getAudio(), getDisableEdition().getValue());
                } else {
                    getLabelTextField().setValue(((BaseForm) getInstance()).getLabel());
                    MessageManager.showWarning(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE,
                            LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE_DESCRIPTION);
                    ApplicationUi.getController().updateForm(getInstance().getForm(), getInstance().getLabel(),
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
