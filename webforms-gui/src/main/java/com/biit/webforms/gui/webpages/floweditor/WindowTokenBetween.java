package com.biit.webforms.gui.webpages.floweditor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WindowTokenBetween extends WindowAcceptCancel {
	private static final long serialVersionUID = 8169533634156722082L;
	private static final DatePeriodUnit DATE_FORMAT_DEFAULT_DATE_PERIOD = DatePeriodUnit.YEAR;
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	private static final String FULL = "100%";
	private static final String EXPAND = null;

	private static final String WIDTH = "350px";
	private static final String HEIGHT = "350px";

	private VerticalLayout rootLayout;

	private Question question;
	private Label questionLabel;
	private ComboBox datePeriod;
	private AbstractField<?> valueStart;
	private AbstractField<?> valueEnd;

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
				updateDates();
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

	private AbstractField<?> updateValueField(AbstractField<?> value) {
		if (value != null) {
			rootLayout.removeComponent(value);
		}
		if (question == null || !question.getAnswerFormat().equals(AnswerFormat.DATE) || datePeriod.getValue() != null) {
			value = new TextField();
			value.setWidth(FULL);
			((TextField) value).setValue("");
		} else {
			value = new DateField();
			value.setWidth(FULL);
			value.setValue(null);
		}
		value.removeAllValidators();
		value.setImmediate(true);
		value.setRequired(true);
		rootLayout.addComponent(value);
		rootLayout.setComponentAlignment(value, Alignment.MIDDLE_CENTER);
		return value;
	}

	public void setQuestion(Question question) {
		this.question = question;
		updateQuestion();
	}

	public void setQuestion(Question question, DatePeriodUnit datePeriod, Object value) {
		setQuestion(question);
		this.datePeriod.setValue(datePeriod);
		if (valueStart instanceof TextField) {
			((TextField) valueStart).setValue(value.toString());
		} else {
			try {
				((DateField) valueStart).setValue((Date) value);
			} catch (Exception e) {
				valueStart.setValue(null);
			}
		}
	}

	private void updateQuestion() {
		if (question != null) {
			questionLabel.setValue(question.getName());
		}

		updateDates();

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

	private void updateDates() {
		valueStart = updateValueField(valueStart);
		valueEnd = updateValueField(valueEnd);
		valueStart.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1715304718376682642L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				valueStart.isValid();
			}
		});
		valueEnd.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1715304718376682642L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				valueEnd.isValid();
			}
		});
	}

	private void updateDateValidatorAndInputPrompt() {
		updateDateValidatorAndInputPrompt(valueStart);
		updateDateValidatorAndInputPrompt(valueEnd);
	}

	private void updateValidatorAndInputPrompt() {
		updateValidatorAndInputPrompt(valueStart);
		updateValidatorAndInputPrompt(valueEnd);
	}

	private void updateValidatorAndInputPrompt(AbstractField<?> value) {
		if (value instanceof TextField) {
			((TextField) value).setInputPrompt(AnswerSubformatUi.get(question.getAnswerSubformat()).getInputPrompt());
		}
		value.addValidator(new ValidatorPattern(question.getAnswerSubformat().getRegex()));
	}

	private void updateDateValidatorAndInputPrompt(AbstractField<?> value) {
		value.removeAllValidators();

		if (value instanceof TextField) {
			if (datePeriod.getValue() == null) {
				((TextField) value).setInputPrompt(AnswerSubformatUi.get(question.getAnswerSubformat())
						.getInputPrompt());
				value.addValidator(new ValidatorPattern(question.getAnswerSubformat().getRegex()));
			} else {
				((TextField) value).setInputPrompt(AnswerSubformatUi.get(AnswerSubformat.DATE_PERIOD).getInputPrompt());
				value.addValidator(new ValidatorPattern(AnswerSubformat.DATE_PERIOD.getRegex()));
			}
		} else {
			value.addValidator(new NullValidator(LanguageCodes.VALIDATION_NULL_VALUE.translation(), false));
		}
	}

	public boolean isDataValid() {
		return valueStart.isValid() && valueEnd.isValid();
	}

	public Question getQuestion() {
		return question;
	}

	public String getValueStart() {
		if (valueStart.getValue() == null) {
			return "";
		} else {
			if (valueStart instanceof DateField) {
				return new SimpleDateFormat(DATE_FORMAT).format(((Date) valueStart.getValue()));
			} else {
				return valueStart.getValue().toString();
			}
		}
	}

	public String getValueEnd() {
		if (valueEnd.getValue() == null) {
			return "";
		} else {
			if (valueEnd instanceof DateField) {
				return new SimpleDateFormat(DATE_FORMAT).format(((Date) valueEnd.getValue()));
			} else {
				return valueEnd.getValue().toString();
			}
		}
	}

	public DatePeriodUnit getDatePeriodUnit() {
		return (DatePeriodUnit) datePeriod.getValue();
	}

	public void setToken(TokenBetween token) {
		setQuestion(token.getQuestion());
		datePeriod.setValue(token.getDatePeriodUnit());
		if (valueStart instanceof TextField) {
			((TextField) valueStart).setValue(token.getValueStart());
		} else {
			DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			try {
				((DateField) valueStart).setValue(format.parse(token.getValueStart()));
			} catch (ReadOnlyException | ConversionException | ParseException e) {
				valueStart.setValue(null);
			}
		}

		if (valueEnd instanceof TextField) {
			((TextField) valueEnd).setValue(token.getValueStart());
		} else {
			DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			try {
				((DateField) valueEnd).setValue(format.parse(token.getValueEnd()));
			} catch (ReadOnlyException | ConversionException | ParseException e) {
				valueStart.setValue(null);
			}
		}
	}

}
