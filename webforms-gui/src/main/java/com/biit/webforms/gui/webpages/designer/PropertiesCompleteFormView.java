package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.BaseForm;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.server.VaadinServlet;

public class PropertiesCompleteFormView extends PropertiesBaseForm<CompleteFormView> {
	private static final long serialVersionUID = -478559896636685508L;

	private IFormDao formDao;

	public PropertiesCompleteFormView() {
		super(CompleteFormView.class);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("webformsFormDao");
	}

	@Override
	public void updateElement() {
		if (getLabelTextField().isValid() && getLabelTextField().getValue() != null && getLabelTextField().getValue().length() > 0) {
			try {
				// Checks if already exists a form with this label and its
				// version.
				if (!formDao.exists(getLabelTextField().getValue(), ((BaseForm) getInstance()).getVersion(),
						((BaseForm) getInstance()).getOrganizationId(), getInstance().getId())) {
					ApplicationUi.getController().updateForm((Form) getInstance().getForm(), getLabelTextField().getValue(),
							getDescriptionTextArea().getValue(), getImage(), getDisableEdition().getValue());
				} else {
					getLabelTextField().setValue(((BaseForm) getInstance()).getLabel());
					MessageManager.showWarning(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE,
							LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE_DESCRIPTION);
					ApplicationUi.getController().updateForm((Form) getInstance().getForm(), ((BaseForm) getInstance()).getLabel(),
							getDescriptionTextArea().getValue(), getImage(), getDisableEdition().getValue());
				}
			} catch (ReadOnlyException e) {
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE);
				WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			}
			super.updateElement();
		}
	}
}
