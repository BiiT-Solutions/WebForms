package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.form.validators.ValidateBaseForm;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.utils.validation.ValidateReport;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.exception.BadAbcdLink;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.validation.ValidationUpperMenu;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.validators.ValidateFormAbcdCompatibility;
import com.biit.webforms.validators.ValidateFormComplete;
import com.biit.webforms.validators.ValidateFormFlows;
import com.biit.webforms.validators.ValidateFormStructure;
import com.biit.webforms.validators.ValidateLogic;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class Validation extends SecuredWebPage {
	private static final long serialVersionUID = 3991819645036921442L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

	private ValidationUpperMenu upperMenu;
	private TextArea textArea;

	@Override
	protected void initContent() {

		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		setBottomMenu(new FormEditBottomMenu());

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);

		textArea = new TextArea();
		textArea.setSizeFull();
		textArea.setReadOnly(true);

		rootLayout.addComponent(textArea);
		getWorkingArea().addComponent(rootLayout);
	}

	private ValidationUpperMenu createUpperMenu() {
		ValidationUpperMenu upperMenu = new ValidationUpperMenu();

		upperMenu.addCompleteValidationListener(new ClickListener() {
			private static final long serialVersionUID = -3292908759875065150L;

			@Override
			public void buttonClick(ClickEvent event) {
				completeValidation();
			}
		});
		upperMenu.addValidateStructureValidationListener(new ClickListener() {
			private static final long serialVersionUID = 1689353001966500222L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateStructure();
			}
		});
		upperMenu.addValidateFlowListener(new ClickListener() {
			private static final long serialVersionUID = -3892060791990799778L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateFlow();
			}
		});
		upperMenu.addValidateAbcdLinkListener(new ClickListener() {
			private static final long serialVersionUID = 6979359279501389211L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateAbcdLink();
			}
		});

		return upperMenu;
	}

	protected void validateAbcdLink() {

		ValidateBaseForm structureValidator = new ValidateBaseForm();
		if (structureValidator.validate(UserSessionHandler.getController().getFormInUse())) {
			List<Form> linkedForms;
			try {
				linkedForms = UserSessionHandler.getController().getLinkedAbcdForm(
						UserSessionHandler.getController().getFormInUse());

				if (linkedForms.isEmpty()) {
					setNoLinkedFormsMessage();
				} else {
					ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(UserSessionHandler
							.getController().getFormInUse());
					ValidateReport report = new ValidateReport();
					validator.validate(linkedForms, report);
					if (report.isValid()) {
						setLinkedFormsCorrectMessage();
					} else {
						setValidationReport(report);
					}
				}
			} catch (UnexpectedDatabaseException e) {
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
						LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			} catch (BadAbcdLink e) {
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
						LanguageCodes.ERROR_ABCD_FORM_LINKED_NOT_FOUND);
			}
		} else {
			MessageManager.showError(LanguageCodes.ERROR_FORM_STRUCTURE_COULD_NOT_BE_VALIDATED);
		}
	}

	protected void validateFlow() {
		ValidateFormStructure structureValidator = new ValidateFormStructure();
		if (structureValidator.validate(UserSessionHandler.getController().getFormInUse())) {
			ValidateFormFlows validator = new ValidateFormFlows();
			ValidateReport report = new ValidateReport();
			validator.validate(UserSessionHandler.getController().getFormInUse(), report);
			if (report.isValid()) {
				ValidateLogic logicValidator = new ValidateLogic();
				logicValidator.validate(UserSessionHandler.getController().getFormInUse(), report);
				if (report.isValid()) {
					setValidationPassedMessage();
				} else {
					setValidationReport(report);
				}
			} else {
				setValidationReport(report);
			}
		} else {
			MessageManager.showError(LanguageCodes.ERROR_FORM_STRUCTURE_COULD_NOT_BE_VALIDATED);
		}
	}

	protected void validateStructure() {
		ValidateFormStructure validator = new ValidateFormStructure();
		ValidateReport report = new ValidateReport();
		validator.validate(UserSessionHandler.getController().getFormInUse(), report);
		if (report.isValid()) {
			setValidationPassedMessage();
		} else {
			setValidationReport(report);
		}
	}

	protected void completeValidation() {
		ValidateFormComplete validator = new ValidateFormComplete();
		ValidateReport report = new ValidateReport();
		validator.validate(UserSessionHandler.getController().getFormInUse(), report);

		List<Form> linkedForms;
		try {
			linkedForms = UserSessionHandler.getController().getLinkedAbcdForm(
					UserSessionHandler.getController().getFormInUse());
			ValidateFormAbcdCompatibility validatorLink = new ValidateFormAbcdCompatibility(UserSessionHandler
					.getController().getFormInUse());
			validatorLink.validate(linkedForms, report);

			if (report.isValid()) {
				setValidationPassedMessage();
			} else {
				setValidationReport(report);
			}
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		} catch (BadAbcdLink e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ABCD_FORM_LINKED_NOT_FOUND);
		}
	}

	private void setValidationReport(ValidateReport report) {
		textArea.setReadOnly(false);
		changeReport(report.getReport());
		textArea.setReadOnly(true);
	}

	private void setValidationPassedMessage() {
		textArea.setReadOnly(false);
		changeReport(LanguageCodes.MESSAGE_VALIDATION_FINISHED_CORRECTLY.translation());
		textArea.setReadOnly(true);
	}

	private void setNoLinkedFormsMessage() {
		changeReport(LanguageCodes.MESSAGE_VALIDATION_NO_ABCD_FORMS_LINKED.translation());
	}

	private void setLinkedFormsCorrectMessage() {
		changeReport(LanguageCodes.MESSAGE_VALIDATION_All_LINKED_FORMS_CORRECT.translation());
	}

	private void changeReport(String report) {
		textArea.setReadOnly(false);
		textArea.setValue(report);
		textArea.setReadOnly(true);
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

}
