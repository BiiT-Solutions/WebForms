package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.BaseForm;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.server.VaadinServlet;

public class PropertiesForm extends PropertiesBaseForm<Form> {
	private static final long serialVersionUID = -1665998885081665306L;

	private IFormDao formDao;

	public PropertiesForm() {
		super(Form.class);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("webformsFormDao");
	}

	@Override
	public void updateElement() {
		System.out.println("########################################### "+getLabelTextField().isValid());
		if (getLabelTextField().isValid() && getLabelTextField().getValue() != null
				&& getLabelTextField().getValue().length() > 0) {
			try {
				// Checks if already exists a form with this label and its version.
				if (!formDao.exists(getLabelTextField().getValue(), ((BaseForm) getInstance()).getVersion(),
						((BaseForm) getInstance()).getOrganizationId(), getInstance().getId())) {
					UserSessionHandler.getController().updateForm((Form) getInstance(), getLabelTextField().getValue(),
							getDescriptionTextArea().getValue());
				} else {
					getLabelTextField().setValue(((BaseForm) getInstance()).getLabel());
					MessageManager.showWarning(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE,
							LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE_DESCRIPTION);
					UserSessionHandler.getController().updateForm((Form) getInstance(),
							((BaseForm) getInstance()).getLabel(), getDescriptionTextArea().getValue());
				}
			} catch (ReadOnlyException e) {
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE);
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
			super.updateElement();
		}
	}

}
