package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.form.validators.ValidateBaseForm;
import com.biit.form.validators.reports.DuplicatedNestedName;
import com.biit.form.validators.reports.DuplicatedNestedNameWithChild;
import com.biit.form.validators.reports.InvalidTreeObjectName;
import com.biit.form.validators.reports.NullValueReport;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.utils.validation.Report;
import com.biit.utils.validation.ValidateReport;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.exception.BadAbcdLink;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.validation.ValidationUpperMenu;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.validators.CompareFormAbcdStructure;
import com.biit.webforms.validators.ValidateFormAbcdCompatibility;
import com.biit.webforms.validators.ValidateFormComplete;
import com.biit.webforms.validators.ValidateFormFlows;
import com.biit.webforms.validators.ValidateFormStructure;
import com.biit.webforms.validators.ValidateLogic;
import com.biit.webforms.validators.reports.BackwardFlow;
import com.biit.webforms.validators.reports.DifferentDateUnitForQuestionsReport;
import com.biit.webforms.validators.reports.FlowOriginIsNotMandatory;
import com.biit.webforms.validators.reports.FormElementWithouthFlowIn;
import com.biit.webforms.validators.reports.IncompleteLogicReport;
import com.biit.webforms.validators.reports.InvalidFlowCondition;
import com.biit.webforms.validators.reports.InvalidFlowSubformat;
import com.biit.webforms.validators.reports.LinkedFormAbcdAnswerNotFound;
import com.biit.webforms.validators.reports.LinkedFormAbcdElementIsBaseGroupNotBaseQuestion;
import com.biit.webforms.validators.reports.LinkedFormAbcdElementIsBaseQuestionNotBaseGroup;
import com.biit.webforms.validators.reports.LinkedFormAbcdElementNotFound;
import com.biit.webforms.validators.reports.LinkedFormAbcdGroupRepeatableStatusIsDifferent;
import com.biit.webforms.validators.reports.LinkedFormStructureNotCompatible;
import com.biit.webforms.validators.reports.MultipleEndFormsFromSameElement;
import com.biit.webforms.validators.reports.MultipleEndLoopsFromSameElement;
import com.biit.webforms.validators.reports.MultipleFlowsWithSameOriginAndDestiny;
import com.biit.webforms.validators.reports.NoSubanswersAllowed;
import com.biit.webforms.validators.reports.NotValidCondition;
import com.biit.webforms.validators.reports.OthersOrphanAt;
import com.biit.webforms.validators.reports.OthersUnicityBrokenAt;
import com.biit.webforms.validators.reports.QuestionNotFound;
import com.biit.webforms.validators.reports.RedundantLogicReport;
import com.biit.webforms.validators.reports.TokenUsesNonFinalAnswer;
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
		upperMenu.addAbcdCompareListener(new ClickListener() {
			private static final long serialVersionUID = -5210017907012197461L;

			@Override
			public void buttonClick(ClickEvent event) {
				compareAbcdForm();
			}
		});
		return upperMenu;
	}

	private void compareAbcdForm() {
		List<Form> linkedForms;
		try {
			linkedForms = UserSessionHandler.getController().getLinkedAbcdForm(
					UserSessionHandler.getController().getCompleteFormView());
			if (linkedForms.isEmpty()) {
				setNoLinkedFormsMessage();
			} else {
				// Select one linked form.

				// Create the report.
				CompareFormAbcdStructure validator = new CompareFormAbcdStructure(UserSessionHandler
						.getController().getCompleteFormView());
				ValidateReport report = new ValidateReport();
				validator.validate(linkedForms, report);
				if (report.isValid()) {
					setLinkedFormsCorrectMessage();
				} else {
					setValidationReport(report);
				}
			}
		} catch (UnexpectedDatabaseException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		} catch (BadAbcdLink e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ABCD_FORM_LINKED_NOT_FOUND);
		}
	}

	private void validateAbcdLink() {
		ValidateBaseForm structureValidator = new ValidateBaseForm();
		if (structureValidator.validate(UserSessionHandler.getController().getCompleteFormView())) {
			List<Form> linkedForms;
			try {
				linkedForms = UserSessionHandler.getController().getLinkedAbcdForm(
						UserSessionHandler.getController().getCompleteFormView());

				if (linkedForms.isEmpty()) {
					setNoLinkedFormsMessage();
				} else {
					ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(UserSessionHandler
							.getController().getCompleteFormView());
					ValidateReport report = new ValidateReport();
					validator.validate(linkedForms, report);
					if (report.isValid()) {
						setLinkedFormsCorrectMessage();
					} else {
						setValidationReport(report);
					}
				}
			} catch (UnexpectedDatabaseException e) {
				WebformsLogger.errorMessage(this.getClass().getName(), e);
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
						LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			} catch (BadAbcdLink e) {
				WebformsLogger.errorMessage(this.getClass().getName(), e);
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
						LanguageCodes.ERROR_ABCD_FORM_LINKED_NOT_FOUND);
			}
		} else {
			MessageManager.showError(LanguageCodes.ERROR_FORM_STRUCTURE_COULD_NOT_BE_VALIDATED);
		}
	}

	private void validateFlow() {
		ValidateFormStructure structureValidator = new ValidateFormStructure();
		if (structureValidator.validate(UserSessionHandler.getController().getCompleteFormView())) {
			ValidateFormFlows validator = new ValidateFormFlows();
			ValidateReport report = new ValidateReport();
			validator.validate(UserSessionHandler.getController().getCompleteFormView(), report);
			if (report.isValid()) {
				ValidateLogic logicValidator = new ValidateLogic();
				logicValidator.validate(UserSessionHandler.getController().getCompleteFormView(), report);
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

	private void validateStructure() {
		ValidateFormStructure validator = new ValidateFormStructure();
		ValidateReport report = new ValidateReport();
		validator.validate(UserSessionHandler.getController().getCompleteFormView(), report);
		if (report.isValid()) {
			setValidationPassedMessage();
		} else {
			setValidationReport(report);
		}
	}

	private void completeValidation() {
		ValidateFormComplete validator = new ValidateFormComplete();
		ValidateReport report = new ValidateReport();
		validator.validate(UserSessionHandler.getController().getCompleteFormView(), report);

		List<Form> linkedForms;
		try {
			linkedForms = UserSessionHandler.getController().getLinkedAbcdForm(
					UserSessionHandler.getController().getCompleteFormView());
			ValidateFormAbcdCompatibility validatorLink = new ValidateFormAbcdCompatibility(UserSessionHandler
					.getController().getCompleteFormView());
			validatorLink.validate(linkedForms, report);

			if (report.isValid()) {
				setValidationPassedMessage();
			} else {
				setValidationReport(report);
			}
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		} catch (BadAbcdLink e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ABCD_FORM_LINKED_NOT_FOUND);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private void setValidationReport(ValidateReport report) {
		changeReport(report.getReports());
	}

	private void setValidationPassedMessage() {
		changeReport(LanguageCodes.MESSAGE_VALIDATION_FINISHED_CORRECTLY.translation());
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
						((DuplicatedNestedName) report).getChild().getName(),
						((DuplicatedNestedName) report).getElement().getPathName() }));
			} else if (report instanceof DuplicatedNestedNameWithChild) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_DUPLICATED_NAMES_WITH_CHILDS,
						new Object[] { ((DuplicatedNestedNameWithChild) report).getChild().getName(),
								((DuplicatedNestedNameWithChild) report).getElement().getPathName() }));
			} else if (report instanceof InvalidTreeObjectName) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_INVALID_ELEMENT_NAME,
						new Object[] { ((InvalidTreeObjectName) report).getInvalidElement().getPathName() }));
			} else if (report instanceof BackwardFlow) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_BACKWARD_FLOW, new Object[] {
						((BackwardFlow) report).getFlow(), ((BackwardFlow) report).getFlow().getOrigin().getPathName(),
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
			} else if (report instanceof InvalidFlowSubformat) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_INVALID_FLOW_SUBFORMAT,
						new Object[] { ((InvalidFlowSubformat) report).getInvalidToken().toString(),
								((InvalidFlowSubformat) report).getFlow(),
								((InvalidFlowSubformat) report).getInvalidToken().getSubformat(),
								((InvalidFlowSubformat) report).getInvalidToken().getQuestion().getAnswerFormat() }));
			} else if (report instanceof LinkedFormAbcdAnswerNotFound) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_ANSWER_NOT_FOUND,
						new Object[] { ((LinkedFormAbcdAnswerNotFound) report).getAbcdform().getLabel(),
								((LinkedFormAbcdAnswerNotFound) report).getAbcdform().getVersion(),
								((LinkedFormAbcdAnswerNotFound) report).getAbcdChild().getPathName() }));
			} else if (report instanceof LinkedFormAbcdElementIsBaseGroupNotBaseQuestion) {
				text.append(ServerTranslate
						.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_ELEMENT_IS_GROUP_NOT_QUESTION,
								new Object[] {
										((LinkedFormAbcdElementIsBaseGroupNotBaseQuestion) report).getAbcdform()
												.getLabel(),
										((LinkedFormAbcdElementIsBaseGroupNotBaseQuestion) report).getAbcdform()
												.getVersion(),
										((LinkedFormAbcdElementIsBaseGroupNotBaseQuestion) report).getAbcdChild()
												.getPathName() }));
			} else if (report instanceof LinkedFormAbcdElementIsBaseQuestionNotBaseGroup) {
				text.append(ServerTranslate
						.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_ELEMENT_IS_QUESTION_NOT_GROUP,
								new Object[] {
										((LinkedFormAbcdElementIsBaseQuestionNotBaseGroup) report).getAbcdform()
												.getLabel(),
										((LinkedFormAbcdElementIsBaseQuestionNotBaseGroup) report).getAbcdform()
												.getVersion(),
										((LinkedFormAbcdElementIsBaseQuestionNotBaseGroup) report).getAbcdChild()
												.getPathName() }));
			} else if (report instanceof LinkedFormAbcdElementNotFound) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_ELEMENT_NOT_FOUND,
						new Object[] { ((LinkedFormAbcdElementNotFound) report).getAbcdform().getLabel(),
								((LinkedFormAbcdElementNotFound) report).getAbcdform().getVersion(),
								((LinkedFormAbcdElementNotFound) report).getAbcdChild().getPathName(),
								((LinkedFormAbcdElementNotFound) report).getWebform().getLabel() }));
			} else if (report instanceof LinkedFormAbcdGroupRepeatableStatusIsDifferent) {
				text.append(ServerTranslate
						.translate(LanguageCodes.VALIDATION_LINKED_FORM_ABCD_GROUP_REPEATABLE_STATUS_IS_DIFFERENT,
								new Object[] {
										((LinkedFormAbcdGroupRepeatableStatusIsDifferent) report).getAbcdForm()
												.getLabel(),
										((LinkedFormAbcdGroupRepeatableStatusIsDifferent) report).getAbcdChild()
												.getPathName() }));
			} else if (report instanceof LinkedFormStructureNotCompatible) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_LINKED_FORM_STRUCTURE_NOT_COMPATIBLE,
						new Object[] { ((LinkedFormStructureNotCompatible) report).getWebform().getLabel(),
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
				text.append(ServerTranslate.translate(
						LanguageCodes.VALIDATION_MULTIPLE_FLOWS_WITH_SAME_ORIGIN_AND_DESTINY, new Object[] {
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
						((QuestionNotFound) report).getWebform().getLabel(),
						((QuestionNotFound) report).getQuestion().getPathName() }));
			} else if (report instanceof RedundantLogicReport) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_REDUNDANT_LOGIC,
						new Object[] { ((RedundantLogicReport) report).getElement().getPathName() }));
			} else if (report instanceof TokenUsesNonFinalAnswer) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_TOKEN_USES_NON_FINAL_ANSWER,
						new Object[] { ((TokenUsesNonFinalAnswer) report).getFlow(),
								((TokenUsesNonFinalAnswer) report).getToken().getAnswer().getPathAnswerValue() }));
			} else if (report instanceof OthersOrphanAt) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_OTHERS_ORPHAN,
						new Object[] { ((OthersOrphanAt) report).getOrigin().getPathName() }));
			} else if (report instanceof FormElementWithouthFlowIn) {
				text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_ELEMENT_NO_FLOW_IN,
						new Object[] { ((FormElementWithouthFlowIn) report).getOrigin().getPathName() }));
			} else if (report instanceof NullValueReport) {
				// Only advise once.
				if (!nullReportAdded) {
					text.append(ServerTranslate.translate(LanguageCodes.VALIDATION_NULL_VALUE));
					nullReportAdded = true;
				}
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
