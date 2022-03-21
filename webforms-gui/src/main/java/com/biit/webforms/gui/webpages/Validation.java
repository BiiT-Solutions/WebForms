package com.biit.webforms.gui.webpages;

import com.biit.form.entity.IBaseFormView;
import com.biit.form.validators.ValidateBaseForm;
import com.biit.form.validators.reports.DuplicatedNestedName;
import com.biit.form.validators.reports.DuplicatedNestedNameWithChild;
import com.biit.form.validators.reports.InvalidTreeObjectName;
import com.biit.form.validators.reports.NullValueReport;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.usermanager.security.IActivity;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ValidateReport;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.exceptions.BadAbcdLink;
import com.biit.webforms.gui.webpages.validation.ValidationUpperMenu;
import com.biit.webforms.gui.webpages.validation.WindowCompareAbcdForm;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.security.WebformsActivity;
import com.biit.webforms.validators.*;
import com.biit.webforms.validators.reports.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Validation extends SecuredWebPage {
	private static final long serialVersionUID = 3991819645036921442L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(Arrays.asList(WebformsActivity.READ));

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
		upperMenu.addAbcdCompareListener(new ClickListener() {
			private static final long serialVersionUID = -5210017907012197461L;

			@Override
			public void buttonClick(ClickEvent event) {
				compareAbcdForm();
			}
		});
		return upperMenu;
	}

	/**
	 * Opens window to link form with abcd form and version.
	 */
	private void compareAbcdForm() {
		final Form form = ApplicationUi.getController().getFormInUse();

		List<com.biit.abcd.persistence.entity.SimpleFormView> availableForms;
		if (form.getLinkedFormLabel() != null) {
			// Already linked form, show only the versions of this form.
			availableForms = ApplicationUi.getController().getAllSimpleFormViewsFromAbcdByLabelAndOrganization(form.getLinkedFormLabel(),
					form.getLinkedFormOrganizationId());

			// Let user choose the version.
			WindowCompareAbcdForm linkAbcdForm = new WindowCompareAbcdForm(form);
			for (com.biit.abcd.persistence.entity.SimpleFormView simpleFormView : availableForms) {

				if (getWebformsSecurityService().isAuthorizedActivity(UserSession.getUser(), simpleFormView.getOrganizationId(),
						WebformsActivity.FORM_EDITING)) {
					linkAbcdForm.add(simpleFormView);
				}
			}

			linkAbcdForm.setValue(ApplicationUi.getController().getLinkedSimpleFormViewsFromAbcd(form));
			linkAbcdForm.addAcceptActionListener(new AcceptActionListener() {

				@Override
				public void acceptAction(WindowAcceptCancel window) {
					WindowCompareAbcdForm linkWindow = (WindowCompareAbcdForm) window;

					ValidateReport report = new ValidateReport();
					for (IBaseFormView abcdForm : linkWindow.getValue()) {
						// Create the report.
						CompareFormAbcdStructure validator = new CompareFormAbcdStructure(ApplicationUi.getController()
								.getCompleteFormView());
						validator.validate(ApplicationUi.getController().getFormFromAbcdById(abcdForm.getId()), report);
						if (report.isValid()) {
							if (report.hasWarnings()) {
								setValidationWarningMessage(report);
							} else {
								setLinkedFormsCorrectMessage();
							}
						} else {
							setValidationReport(report);
						}
						linkWindow.close();
					}
				}
			});
			linkAbcdForm.showCentered();
		} else {
			setNoLinkedFormsMessage();
		}
	}

	private void validateAbcdLink() {
		ValidateBaseForm structureValidator = new ValidateBaseForm();
		if (structureValidator.validate(ApplicationUi.getController().getCompleteFormView())) {
			List<com.biit.abcd.persistence.entity.Form> linkedForms;
			try {
				linkedForms = ApplicationUi.getController()
						.getLinkedAbcdForm(ApplicationUi.getController().getCompleteFormView());

				if (linkedForms.isEmpty()) {
					setNoLinkedFormsMessage();
				} else {
					ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(ApplicationUi.getController()
							.getCompleteFormView());
					ValidateReport report = new ValidateReport();
					validator.validate(linkedForms, report);
					if (report.isValid()) {
						if (report.hasWarnings()) {
							setValidationWarningMessage(report);
						} else {
							setLinkedFormsCorrectMessage();
						}
					} else {
						setValidationReport(report);
					}
				}
			} catch (UnexpectedDatabaseException e) {
				WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			} catch (BadAbcdLink e) {
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ABCD_FORM_LINKED_NOT_FOUND);
			}
		} else {
			MessageManager.showError(LanguageCodes.ERROR_FORM_STRUCTURE_COULD_NOT_BE_VALIDATED);
		}
	}

	private void validateFlow() {
		ValidateFormStructure structureValidator = new ValidateFormStructure();
		if (structureValidator.validate(ApplicationUi.getController().getCompleteFormView())) {
			ValidateFormFlows validator = new ValidateFormFlows();
			ValidateReport report = new ValidateReport();
			validator.validate(ApplicationUi.getController().getCompleteFormView(), report);
			if (report.isValid()) {
				ValidateLogic logicValidator = new ValidateLogic();
				logicValidator.validate(ApplicationUi.getController().getCompleteFormView(), report);
				if (report.isValid()) {
					if (report.hasWarnings()) {
						setValidationWarningMessage(report);
					} else {
						setValidationPassedMessage();
					}
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

	private void validateStructure() {
		ValidateFormStructure validator = new ValidateFormStructure();
		ValidateReport report = new ValidateReport();
		validator.validate(ApplicationUi.getController().getCompleteFormView(), report);
		if (report.isValid()) {
			if (report.hasWarnings()) {
				setValidationWarningMessage(report);
			} else {
				setValidationPassedMessage();
			}
		} else {
			setValidationReport(report);
		}
	}

	private void completeValidation() {
		ValidateFormComplete validator = new ValidateFormComplete(ApplicationUi.getController().getAllWebservices());
		ValidateReport report = new ValidateReport();
		validator.validate(ApplicationUi.getController().getCompleteFormView(), report);

		List<com.biit.abcd.persistence.entity.Form> linkedForms;
		try {
			linkedForms = ApplicationUi.getController().getLinkedAbcdForm(ApplicationUi.getController().getCompleteFormView());
			ValidateFormAbcdCompatibility validatorLink = new ValidateFormAbcdCompatibility(ApplicationUi.getController()
					.getCompleteFormView());
			validatorLink.validate(linkedForms, report);

			if (report.isValid()) {
				if (report.hasWarnings()) {
					setValidationWarningMessage(report);
				} else {
					setValidationPassedMessage();
				}
			} else {
				setValidationReport(report);
			}
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		} catch (BadAbcdLink e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ABCD_FORM_LINKED_NOT_FOUND);
		}
	}

	private void setValidationReport(ValidateReport report) {
		changeReport(report.getReports());
	}

	private void setValidationPassedMessage() {
		changeReport(LanguageCodes.MESSAGE_VALIDATION_FINISHED_CORRECTLY.translation());
	}

	private void setValidationWarningMessage(ValidateReport report) {
		MessageManager.showWarning(LanguageCodes.WARNING_FORM_VALIDATION);
		changeReport(report.getReports());
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

	private void changeReport(List<Report> reports) {
		StringBuilder text = new StringBuilder();
		textArea.setReadOnly(false);

		// Translate the reports.
		for (Report report : reports) {
			boolean nullReportAdded = false;
			if (report instanceof DuplicatedNestedName) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_DUPLICATED_NAMES, new Object[] {
						((DuplicatedNestedName) report).getChild().getName(), ((DuplicatedNestedName) report).getElement().getPathName() }));
			} else if (report instanceof DuplicatedNestedNameWithChild) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_DUPLICATED_NAMES_WITH_CHILDS, new Object[] {
						((DuplicatedNestedNameWithChild) report).getChild().getName(),
						((DuplicatedNestedNameWithChild) report).getElement().getPathName() }));
			} else if (report instanceof InvalidTreeObjectName) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_INVALID_ELEMENT_NAME,
						new Object[] { ((InvalidTreeObjectName) report).getInvalidElement().getPathName() }));
			} else if (report instanceof BackwardFlow) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_BACKWARD_FLOW,
						new Object[] { ((BackwardFlow) report).getFlow(), ((BackwardFlow) report).getFlow().getOrigin().getPathName(),
								((BackwardFlow) report).getFlow().getDestiny().getPathName() }));
			} else if (report instanceof DifferentDateUnitForQuestionsReport) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_INVALID_DATE_UNIT, new Object[] {
						((DifferentDateUnitForQuestionsReport) report).getElement().getPathName(),
						((DifferentDateUnitForQuestionsReport) report).getQuestions() }));
			} else if (report instanceof FlowOriginIsNotMandatory) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_FLOW_ORIGIN_NOT_MANDATORY, new Object[] {
						((FlowOriginIsNotMandatory) report).getFlow(),
						((FlowOriginIsNotMandatory) report).getFlow().getOrigin().getPathName() }));
			} else if (report instanceof IncompleteLogicReport) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_INCOMPLETE_LOGIN_REPORT,
						new Object[] { ((IncompleteLogicReport) report).getElement().getPathName() }));
			} else if (report instanceof InvalidFlowCondition) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_INVALID_FLOW_CONDITION,
						new Object[] { ((InvalidFlowCondition) report).getFlow().toString() }));
			} else if (report instanceof ConditionWithNotMandatoryQuestion) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_CONDITION_WITH_NOT_MANDATORY_QUESTION, new Object[] {
						((ConditionWithNotMandatoryQuestion) report).getQuestion().getPathName(),
						((ConditionWithNotMandatoryQuestion) report).getFlow().toString() }));
			} else if (report instanceof InvalidFlowSubformat) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_INVALID_FLOW_SUBFORMAT, new Object[] {
						((InvalidFlowSubformat) report).getInvalidToken().toString(), ((InvalidFlowSubformat) report).getFlow(),
						((InvalidFlowSubformat) report).getInvalidToken().getSubformat(),
						((InvalidFlowSubformat) report).getInvalidToken().getQuestion().getAnswerFormat() }));
			} else if (report instanceof FormAnswerNotFound) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_ANSWER_NOT_FOUND, new Object[] {
						((FormAnswerNotFound) report).getFormWithElement().getLabel(),
						((FormAnswerNotFound) report).getFormWithElement().getVersion(),
						((FormAnswerNotFound) report).getElementMissed().getPathName(),
						((FormAnswerNotFound) report).getFormWithoutElement().getLabel(),
						((FormAnswerNotFound) report).getFormWithoutElement().getVersion() }));
			} else if (report instanceof FormElementIsBaseGroupNotBaseQuestion) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_ELEMENT_IS_GROUP_NOT_QUESTION,
						new Object[] { ((FormElementIsBaseGroupNotBaseQuestion) report).getFormWithGroup().getLabel(),
								((FormElementIsBaseGroupNotBaseQuestion) report).getFormWithGroup().getVersion(),
								((FormElementIsBaseGroupNotBaseQuestion) report).getElementAsGroup().getPathName(),
								((FormElementIsBaseGroupNotBaseQuestion) report).getFormWithQuestion().getLabel(),
								((FormElementIsBaseGroupNotBaseQuestion) report).getFormWithQuestion().getVersion() }));
			} else if (report instanceof FormElementIsBaseQuestionNotBaseGroup) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_ELEMENT_IS_QUESTION_NOT_GROUP,
						new Object[] { ((FormElementIsBaseQuestionNotBaseGroup) report).getFormWithQuestion().getLabel(),
								((FormElementIsBaseQuestionNotBaseGroup) report).getFormWithQuestion().getVersion(),
								((FormElementIsBaseQuestionNotBaseGroup) report).getElementAsQuestion().getPathName(),
								((FormElementIsBaseQuestionNotBaseGroup) report).getFormWithGroup().getLabel(),
								((FormElementIsBaseQuestionNotBaseGroup) report).getFormWithGroup().getVersion() }));
			} else if (report instanceof FormElementNotFound) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_ELEMENT_NOT_FOUND, new Object[] {
						((FormElementNotFound) report).getFormWithElement().getLabel(),
						((FormElementNotFound) report).getFormWithElement().getVersion(),
						((FormElementNotFound) report).getElementMissed().getPathName(),
						((FormElementNotFound) report).getFormWithoutElement().getLabel(),
						((FormElementNotFound) report).getFormWithoutElement().getVersion() }));
			} else if (report instanceof FormGroupRepeatableStatusIsDifferent) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_GROUP_REPEATABLE_STATUS_IS_DIFFERENT,
						new Object[] { ((FormGroupRepeatableStatusIsDifferent) report).getAbcdForm().getLabel(),
								((FormGroupRepeatableStatusIsDifferent) report).getAbcdChild().getPathName() }));
			} else if (report instanceof LinkedFormStructureNotCompatible) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_LINKED_FORM_STRUCTURE_NOT_COMPATIBLE, new Object[] {
						((LinkedFormStructureNotCompatible) report).getWebform().getLabel(),
						((LinkedFormStructureNotCompatible) report).getWebform().getVersion(),
						((LinkedFormStructureNotCompatible) report).getAbcdForm().getLabel(),
						((LinkedFormStructureNotCompatible) report).getAbcdForm().getVersion() }));
			} else if (report instanceof MultipleEndFormsFromSameElement) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_MULTIPLE_END_FORMS_FROM_SAME_ELEMENT,
						new Object[] { ((MultipleEndFormsFromSameElement) report).getOrigin().getPathName() }));
			} else if (report instanceof MultipleEndLoopsFromSameElement) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_MULTIPLE_END_LOOPS_FROM_SAME_ELEMENT,
						new Object[] { ((MultipleEndLoopsFromSameElement) report).getOrigin().getPathName() }));
			} else if (report instanceof MultipleFlowsWithSameOriginAndDestiny) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_MULTIPLE_FLOWS_WITH_SAME_ORIGIN_AND_DESTINY, new Object[] {
						((MultipleFlowsWithSameOriginAndDestiny) report).getOrigin().getPathName(),
						((MultipleFlowsWithSameOriginAndDestiny) report).getDestination().getPathName() }));
			} else if (report instanceof NoSubanswersAllowed) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_NO_SUBANSWERS_ALLOWED,
						new Object[] { ((NoSubanswersAllowed) report).getQuestion().getPathName() }));
			} else if (report instanceof NotValidCondition) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_NOT_VALID_CONDITION, new Object[] {
						((NotValidCondition) report).getBadFormedExpression(),
						((NotValidCondition) report).getBadFormedExpression().get(0).getOrigin().getPathName() }));
			} else if (report instanceof OthersUnicityBrokenAt) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_OTHERS_UNICITY_BROKEN,
						new Object[] { ((OthersUnicityBrokenAt) report).getOrigin().getPathName() }));
			} else if (report instanceof QuestionNotFound) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_QUESTION_NOT_FOUND, new Object[] {
						((QuestionNotFound) report).getWebform().getLabel(), ((QuestionNotFound) report).getQuestion().getPathName() }));
			} else if (report instanceof RedundantLogicReport) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_REDUNDANT_LOGIC,
						new Object[] { ((RedundantLogicReport) report).getElement().getPathName() }));
			} else if (report instanceof FlowBlockedInQuestion) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_FLOW_BLOCKED_IN_QUESTION,
						new Object[] { ((FlowBlockedInQuestion) report).getElement().getPathName() }));
			} else if (report instanceof TokenUsesNonFinalAnswer) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_TOKEN_USES_NON_FINAL_ANSWER, new Object[] {
						((TokenUsesNonFinalAnswer) report).getFlow(),
						((TokenUsesNonFinalAnswer) report).getToken().getAnswer() != null ? ((TokenUsesNonFinalAnswer) report).getToken()
								.getAnswer().getPathAnswerValue() : "null" }));
			} else if (report instanceof OthersOrphanAt) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_OTHERS_ORPHAN, new Object[] { ((OthersOrphanAt) report)
						.getOrigin().getPathName() }));
			} else if (report instanceof FormElementWithoutFlowIn) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_ELEMENT_NO_FLOW_IN,
						new Object[] { ((FormElementWithoutFlowIn) report).getOrigin().getPathName() }));
			} else if (report instanceof MultipleDynamicAnswersReferenceTheSameQuestion) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_MULTIPLE_DYNAMIC_ANSWERS_REFERENCE_SAME_QUESTION,
						new Object[] { ((MultipleDynamicAnswersReferenceTheSameQuestion) report).getQuestion().getPathName(),
								((MultipleDynamicAnswersReferenceTheSameQuestion) report).getAnswer().getReference().getPathName() }));
			} else if (report instanceof DynamicAnswerNullReference) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_DYNAMIC_ANSWER_NULL_VALUE,
						new Object[] { ((DynamicAnswerNullReference) report).getQuestion().getPathName() }));
			} else if (report instanceof DynamicAnswerReferenceInvalid) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_DYNAMIC_ANSWER_REFERENCE_INVALID, new Object[] {
						((DynamicAnswerReferenceInvalid) report).getQuestion().getPathName(),
						((DynamicAnswerReferenceInvalid) report).getReference().getPathName() }));
			} else if (report instanceof NullValueReport) {
				// Only advise once.
				if (!nullReportAdded) {
					text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_NULL_VALUE));
					nullReportAdded = true;
				}
			} else if (report instanceof EmptyFlowIsNotAlone) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_EMPTY_FLOW_IS_NOT_ALONE,
						new Object[] { ((EmptyFlowIsNotAlone) report).getFlow().getOrigin().getPathName() }));
			} else if (report instanceof FlowConditionIsUseless) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_CONDITION_IS_USELESS, new Object[] {
						((FlowConditionIsUseless) report).getFlow(), ((FlowConditionIsUseless) report).getCondition() }));
			} else if (report instanceof WebserviceCallCorruption) {
				text.append(ServerTranslate.translate(LanguageCodes.WEBSERVICE_CALL_CORRUPTION,
						new Object[] { ((WebserviceCallCorruption) report).getCall().getName() }));
			} else if (report instanceof WebserviceCallIncompatibleField) {
				text.append(ServerTranslate.translate(LanguageCodes.WEBSERVICE_CALL_INCOMPATIBLE_FIELD, new Object[] {
						((WebserviceCallIncompatibleField) report).getLink().getWebservicePort(),
						((WebserviceCallIncompatibleField) report).getCall().getName() }));
			} else if (report instanceof WebserviceCallInputAfterTrigger) {
				text.append(ServerTranslate.translate(LanguageCodes.WEBSERVICE_CALL_INPUT_AFTER_TRIGGER, new Object[] {
						((WebserviceCallInputAfterTrigger) report).getLink().getWebservicePort(),
						((WebserviceCallInputAfterTrigger) report).getCall().getName() }));
			} else if (report instanceof WebserviceCallInputNull) {
				text.append(ServerTranslate.translate(LanguageCodes.WEBSERVICE_CALL_INPUT_NULL, new Object[] {
						((WebserviceCallInputNull) report).getLink().getWebservicePort(),
						((WebserviceCallInputNull) report).getCall().getName() }));
			} else if (report instanceof WebserviceCallOutputAfterTrigger) {
				text.append(ServerTranslate.translate(LanguageCodes.WEBSERVICE_CALL_OUTPUT_AFTER_TRIGGER, new Object[] {
						((WebserviceCallOutputAfterTrigger) report).getLink().getWebservicePort(),
						((WebserviceCallOutputAfterTrigger) report).getCall().getName() }));
			} else if (report instanceof WebserviceCallReferencesUnexistingWebservice) {
				text.append(ServerTranslate.translate(LanguageCodes.WEBSERVICE_CALL_REFERENCES_UNEXISTING_WEBSERVICE, new Object[] {
						((WebserviceCallReferencesUnexistingWebservice) report).getCall().getName(),
						((WebserviceCallReferencesUnexistingWebservice) report).getCall().getWebserviceName() }));
			} else if (report instanceof WebserviceCallTriggerNull) {
				text.append(ServerTranslate.translate(LanguageCodes.WEBSERVICE_CALL_TRIGGER_NULL,
						new Object[] { ((WebserviceCallTriggerNull) report).getCall().getName() }));
			} else if (report instanceof InvalidDefaultValue) {
				text.append(ServerTranslate.translate(
						LanguageCodes.VALIDATION_INVALID_DEFAULT_VALUE,
						new Object[] {
								((InvalidDefaultValue) report).getQuestion().getPathName(),
								((InvalidDefaultValue) report).getQuestion().getDefaultValueString(),
								AnswerSubformatUi.get(((InvalidDefaultValue) report).getQuestion().getAnswerSubformat()).getLanguageCode()
										.translation() }));
			} else {
				text.append(report.getReport());
			}
			text.append(System.lineSeparator());
		}

		textArea.setValue(text.toString());
		textArea.setReadOnly(true);
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

}
