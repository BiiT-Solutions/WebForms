package com.biit.webforms.gui.webpages.designer;

import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesForm extends StorableObjectProperties<Form> {
	private static final long serialVersionUID = -7053263006728113569L;
	private static final String WIDTH = "200px";

	private TextField label, version;
	private TextArea description;

	private IFormDao formDao;

	public PropertiesForm() {
		super(Form.class);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	@Override
	protected void firePropertyUpdateOnExitListener() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initElement() {

		label = new TextField(LanguageCodes.CAPTION_NAME.translation());
		label.setWidth(WIDTH);
		label.setEnabled(true);

		version = new TextField(LanguageCodes.CAPTION_VERSION.translation());
		version.setWidth(WIDTH);
		version.setEnabled(false);

		description = new TextArea(LanguageCodes.CAPTION_DESCRIPTION.translation());
		description.setWidth(WIDTH);
		description.setMaxLength(Form.MAX_DESCRIPTION_LENGTH);
		description.setImmediate(true);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(label);
		commonProperties.addComponent(version);
		commonProperties.addComponent(description);

		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());
		commonProperties.setEnabled(canEdit);

		addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_FORM.translation(), true);

		super.initElement();
	}

	@Override
	protected void initValues() {
		super.initValues();
		label.setValue(instance.getLabel());
		version.setValue("" + instance.getVersion());
		description.setValue(instance.getDescription());
	}

	@Override
	public void updateElement() {
		if (label.getValue() != null && label.getValue().length() > 0) {
			try {
				// Checks if already exists a form with this label and its version.
				if (!formDao.exists(label.getValue(), instance.getVersion(), instance.getOrganizationId(),
						instance.getId())) {
					UserSessionHandler.getController().updateForm(instance, label.getValue(), description.getValue());
				} else {
					label.setValue(instance.getLabel());
					MessageManager.showWarning(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE,
							LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE_DESCRIPTION);
					UserSessionHandler.getController()
							.updateForm(instance, instance.getLabel(), description.getValue());
				}
			} catch (ReadOnlyException | UnexpectedDatabaseException e) {
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE);
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
			super.updateElement();
		}
	}
}
