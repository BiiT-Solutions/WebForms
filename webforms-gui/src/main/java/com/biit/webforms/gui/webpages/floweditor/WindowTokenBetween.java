package com.biit.webforms.gui.webpages.floweditor;

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

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WindowTokenBetween extends WindowAcceptCancel {
	private static final long serialVersionUID = 8169533634156722082L;
	private static final DatePeriodUnit DATE_FORMAT_DEFAULT_DATE_PERIOD = DatePeriodUnit.YEAR;
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	private static final String FULL = "100%";
	private static final String EXPAND = null;

	private static final String WIDTH = "350px";
	private static final String HEIGHT = "350px";

	private VerticalLayout rootLayout;

	private WebformsBaseQuestion question;
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

	public void setQuestion(WebformsBaseQuestion question) {
		this.question = question;
		updateQuestion();
	}

	public void setQuestion(WebformsBaseQuestion question, DatePeriodUnit datePeriod, Object value) {
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
			if (!question.getAnswerFormat().equals(AnswerFormat.DATE)) {
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

	public WebformsBaseQuestion getQuestion() {
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
			((TextField) valueEnd).setValue(token.getValueEnd());
		} else {
			DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			try {
				((DateField) valueEnd).setValue(format.parse(token.getValueEnd()));
			} catch (ReadOnlyException | ConversionException | ParseException e) {
				valueEnd.setValue(null);
			}
		}
	}

}
