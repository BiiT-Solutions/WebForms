package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WindowTokenBetween extends WindowAcceptCancel {
	private static final long serialVersionUID = 8169533634156722082L;
	private static final DatePeriodUnit DATE_FORMAT_DEFAULT_DATE_PERIOD = DatePeriodUnit.YEAR;

	private static final String FULL = "100%";
	private static final String EXPAND = null;

	private static final String WIDTH = "350px";
	private static final String HEIGHT = "350px";

	private VerticalLayout rootLayout;

	private Question question;
	private Label questionLabel;
	private ComboBox datePeriod;
	private TextField valueStart;
	private TextField valueEnd;

	public WindowTokenBetween() {
		super();
		configure();
		setContent(generate());
	}

	private void configure() {
		setModal(true);
		setDraggable(true);
		setResizable(false);
		setClosable(true);
		setWidth(WIDTH);
		setHeight(HEIGHT);
	}

	private Component generate() {
		rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setWidth(FULL);
		rootLayout.setHeight(EXPAND);
		rootLayout.setSpacing(true);

		questionLabel = new Label();
		questionLabel.setWidth(EXPAND);
		questionLabel.setImmediate(true);

		datePeriod = new ComboBox();
		datePeriod.setWidth(FULL);
		datePeriod.setTextInputAllowed(false);
		datePeriod.setNullSelectionAllowed(true);
		datePeriod.addItem("null");
		datePeriod.setItemCaption("null", LanguageCodes.CAPTION_DATE_PERIOD_NULL.translation());
		datePeriod.setNullSelectionItemId("null");

		for (DatePeriodUnitUi dateType : DatePeriodUnitUi.values()) {
			datePeriod.addItem(dateType.getDatePeriodUnit());
			datePeriod.setItemCaption(dateType.getDatePeriodUnit(), dateType.getTranslation());
		}
		datePeriod.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -333682134124174959L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateDateValidatorAndInputPrompt();
			}
		});

		valueStart = generateValueTextField();
		valueEnd = generateValueTextField();

		rootLayout.addComponent(questionLabel);
		rootLayout.addComponent(datePeriod);
		rootLayout.addComponent(valueStart);
		rootLayout.addComponent(valueEnd);

		rootLayout.setComponentAlignment(questionLabel, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(datePeriod, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(valueStart, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(valueEnd, Alignment.MIDDLE_CENTER);

		return rootLayout;
	}

	public TextField generateValueTextField() {
		final TextField valueField = new TextField();
		valueField.setWidth(FULL);
		valueField.setRequired(true);
		valueField.setImmediate(true);
		valueField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -5155837781711968901L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				valueField.isValid();
			}
		});
		return valueField;
	}

	public void setQuestion(Question question) {
		this.question = question;
		update();
	}

	public void setQuestion(Question question, DatePeriodUnit datePeriod, String value) {
		setQuestion(question);
		this.datePeriod.setValue(datePeriod);
		this.valueStart.setValue(value);
	}

	private void update() {
		if (question != null) {
			questionLabel.setValue(question.getName());
		}

		valueStart.setValue("");
		valueEnd.setValue("");

		rootLayout.removeComponent(datePeriod);
		if (question.getAnswerFormat() == AnswerFormat.DATE) {
			rootLayout.addComponent(datePeriod, 1);
			if (question.getAnswerSubformat() == AnswerSubformat.DATE_PERIOD) {
				datePeriod.setNullSelectionAllowed(false);
				datePeriod.setValue(DATE_FORMAT_DEFAULT_DATE_PERIOD);
			} else {
				datePeriod.setNullSelectionAllowed(true);
				datePeriod.setValue(null);
			}
			updateDateValidatorAndInputPrompt();
		} else {
			datePeriod.setNullSelectionAllowed(true);
			datePeriod.setValue(null);
			updateValidatorAndInputPrompt();
		}
	}

	private void updateDateValidatorAndInputPrompt() {
		updateDateValidatorAndInputPrompt(valueStart);
		updateDateValidatorAndInputPrompt(valueEnd);
	}

	private void updateValidatorAndInputPrompt() {
		updateValidatorAndInputPrompt(valueStart);
		updateValidatorAndInputPrompt(valueEnd);
	}

	private void updateValidatorAndInputPrompt(TextField value) {
		value.setInputPrompt(question.getAnswerSubformat().getHint());
		value.addValidator(new ValidatorPattern(question.getAnswerSubformat().getRegex()));
	}

	private void updateDateValidatorAndInputPrompt(TextField value) {
		value.removeAllValidators();

		if (question != null && question.getAnswerSubformat() != null) {
			if (datePeriod.getValue() == null) {
				value.setInputPrompt(question.getAnswerSubformat().getHint());
				value.addValidator(new ValidatorPattern(question.getAnswerSubformat().getRegex()));
			} else {
				value.setInputPrompt(AnswerSubformat.DATE_PERIOD.getHint());
				value.addValidator(new ValidatorPattern(AnswerSubformat.DATE_PERIOD.getRegex()));
			}
		}
	}

	public boolean isDataValid() {
		return valueStart.isValid() && valueEnd.isValid();
	}

	public Question getQuestion() {
		return question;
	}

	public String getValueStart() {
		return valueStart.getValue();
	}

	public String getValueEnd() {
		return valueEnd.getValue();
	}

	public DatePeriodUnit getDatePeriodUnit() {
		return (DatePeriodUnit) datePeriod.getValue();
	}

	public void setToken(TokenBetween token) {
		setQuestion(token.getQuestion());
		datePeriod.setValue(token.getDatePeriodUnit());
		valueStart.setValue(token.getValueStart());
		valueEnd.setValue(token.getValueEnd());
	}
	
	
}
