package com.biit.webforms.gui.webpages.floweditor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class WindowTokenOperationValue extends WindowAcceptCancel {
	private static final long serialVersionUID = 697691525922280194L;
	private static final String WIDTH = "650px";
	private static final String HEIGHT = "250px";
	private static final Object DEFAULT_DATE_PERIOD_UNIT = DatePeriodUnit.YEAR;
	private static final String DATE_PERIOD_WIDTH = "100px";
	private static final String OPERATOR_WIDTH = "100px";

	private Label treeElementLabel;
	private ComboBox datePeriodUnit;
	private ComboBox operator;
	private AbstractField<?> value;
	private TokenComparationValue token;
	private HorizontalLayout rootLayout;

	public WindowTokenOperationValue() {
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

	private HorizontalLayout generate() {
		rootLayout = new HorizontalLayout();
		rootLayout.setWidth(null);
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		treeElementLabel = new Label("null-content");
		treeElementLabel.setWidth(null);

		datePeriodUnit = new ComboBox();
		datePeriodUnit.setTextInputAllowed(false);
		datePeriodUnit.setWidth(DATE_PERIOD_WIDTH);
		datePeriodUnit.addItem("null");
		datePeriodUnit.setItemCaption("null", LanguageCodes.CAPTION_DATE_PERIOD_NULL.translation());
		datePeriodUnit.setNullSelectionItemId("null");
		for (DatePeriodUnitUi datePeriod : DatePeriodUnitUi.values()) {
			datePeriodUnit.addItem(datePeriod.getDatePeriodUnit());
			datePeriodUnit.setItemCaption(datePeriod.getDatePeriodUnit(), datePeriod.getRepresentation());
		}
		datePeriodUnit.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -333682134124174959L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateValueField();
			}
		});

		operator = new ComboBox();
		operator.setNullSelectionAllowed(false);
		operator.setTextInputAllowed(false);
		operator.setWidth(OPERATOR_WIDTH);

		updateValueField();

		rootLayout.addComponent(treeElementLabel);
		rootLayout.addComponent(datePeriodUnit);
		rootLayout.addComponent(operator);
		rootLayout.addComponent(value);
		rootLayout.setComponentAlignment(treeElementLabel, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(operator, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(value, Alignment.MIDDLE_CENTER);

		return rootLayout;
	}

	private void updateValueField() {
		if (value != null) {
			rootLayout.removeComponent(value);
		}
		if (token == null || !(token.getQuestion() instanceof Question)
				|| !((Question) token.getQuestion()).getAnswerFormat().equals(AnswerFormat.DATE)
				|| datePeriodUnit.getValue() != null) {
			value = new TextField();
			((TextField) value).setValue("");
		} else {
			value = new DateField();
			value.setValue(null);
		}
		value.removeAllValidators();
		value.setImmediate(true);
		value.setRequired(true);
		value.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1715304718376682642L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				value.isValid();
			}
		});

		rootLayout.addComponent(value);
	}

	public TokenTypes getOperator() {
		return (TokenTypes) operator.getValue();
	}

	public AnswerSubformat getAnswerSubformat() {
		if (!(token.getQuestion() instanceof Question)) {
			return null;
		}

		if (((Question) token.getQuestion()).getAnswerFormat() == AnswerFormat.DATE
				&& ((Question) token.getQuestion()).getAnswerSubformat() != AnswerSubformat.DATE_PERIOD) {
			// Date time or period format
			if (AnswerSubformat.DATE_PERIOD.getRegex().matcher(value.getValue().toString()).matches()) {
				return AnswerSubformat.DATE_PERIOD;
			}
		}
		return ((Question) token.getQuestion()).getAnswerSubformat();
	}

	public String getValue() {
		if (!value.isValid() || value.getValue() == null) {
			return null;
		} else {
			if (value instanceof DateField) {
				return new SimpleDateFormat(TokenComparationValue.DATE_FORMAT).format(((Date) value.getValue()));
			} else {
				return value.getValue().toString();
			}
		}
	}

	/**
	 * This function checks that the current value is correct.
	 */
	protected boolean acceptAction() {
		if (value.isValid()) {
			return true;
		}
		return false;
	}

	public void setToken(TokenComparationValue token) {
		this.token = token;
		update();
	}

	private void update() {
		// Set label
		treeElementLabel.setValue(token.getQuestion().getName());

		// Set Date period if needed
		if ((token.getQuestion() instanceof Question)
				&& ((Question) token.getQuestion()).getAnswerFormat() == AnswerFormat.DATE) {
			if (((Question) token.getQuestion()).getAnswerSubformat() == AnswerSubformat.DATE_PERIOD) {
				datePeriodUnit.setNullSelectionAllowed(false);
			} else {
				datePeriodUnit.setNullSelectionAllowed(true);
			}
			datePeriodUnit.setValue(token.getDatePeriodUnit());
			if (token.getSubformat() == AnswerSubformat.DATE_PERIOD && token.getDatePeriodUnit() == null) {
				// If there is an error on DB.
				datePeriodUnit.setValue(DEFAULT_DATE_PERIOD_UNIT);
			}

			datePeriodUnit.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 2809186951838524143L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					updateHintAndValidators();
				}
			});
		} else {
			datePeriodUnit.setVisible(false);
		}

		// Standard questions have IN
		for (TokenTypes type : ((WebformsBaseQuestion) token.getQuestion()).getAnswerFormat().getValidTokenTypes()) {
			if (!type.equals(TokenTypes.BETWEEN) && !type.equals(TokenTypes.IN)) {
				addTokenType(type);
			}
		}

		operator.setValue(token.getType());

		updateHintAndValidators();
		if (value instanceof TextField) {
			((TextField) value).setValue(token.getValue());
		} else {
			DateFormat format = new SimpleDateFormat(TokenComparationValue.DATE_FORMAT);
			try {
				((DateField) value).setValue(format.parse(token.getValue()));
			} catch (ReadOnlyException | ConversionException | ParseException e) {
				value.setValue(null);
			}
		}
	}

	private void updateHintAndValidators() {
		// Set field.
		updateValueField();
		value.removeAllValidators();
		if (value instanceof TextField) {
			// System field
			if ((token.getQuestion() instanceof SystemField)) {
				((TextField) value).setInputPrompt(AnswerSubformatUi.get(AnswerSubformat.TEXT).getInputPrompt());
			} else if (datePeriodUnit.getValue() == null) {
				((TextField) value).setInputPrompt(AnswerSubformatUi.get(
						((Question) token.getQuestion()).getAnswerSubformat()).getInputPrompt());
				value.addValidator(new ValidatorPattern(((Question) token.getQuestion()).getAnswerSubformat()
						.getRegex()));
			} else {
				((TextField) value).setInputPrompt(AnswerSubformatUi.get(AnswerSubformat.DATE_PERIOD).getInputPrompt());
				value.addValidator(new ValidatorPattern(AnswerSubformat.DATE_PERIOD.getRegex()));
			}
		} else {
			value.addValidator(new NullValidator(LanguageCodes.VALIDATION_NULL_VALUE.translation(), false));
		}
	}

	private void addTokenType(TokenTypes type) {
		operator.addItem(type);
		operator.setItemCaption(type, type.toString());
	}

	public DatePeriodUnit getDatePeriod() {
		if (datePeriodUnit.getValue() == null) {
			return null;
		}
		return (DatePeriodUnit) datePeriodUnit.getValue();
	}
}