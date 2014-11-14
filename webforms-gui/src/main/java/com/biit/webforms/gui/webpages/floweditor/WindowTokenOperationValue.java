package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
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
	private TextField value;
	private TokenComparationValue token;

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

	private Component generate() {
		HorizontalLayout rootLayout = new HorizontalLayout();
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

		operator = new ComboBox();
		operator.setNullSelectionAllowed(false);
		operator.setTextInputAllowed(false);
		operator.setWidth(OPERATOR_WIDTH);

		value = new TextField();
		value.removeAllValidators();
		value.setValue(null);
		value.setImmediate(true);
		value.setRequired(true);
		value.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1715304718376682642L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				value.isValid();
			}
		});

		rootLayout.addComponent(treeElementLabel);
		rootLayout.addComponent(datePeriodUnit);
		rootLayout.addComponent(operator);
		rootLayout.addComponent(value);
		rootLayout.setComponentAlignment(treeElementLabel, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(operator, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(value, Alignment.MIDDLE_CENTER);

		return rootLayout;
	}

	public TokenTypes getOperator() {
		return (TokenTypes) operator.getValue();
	}

	public AnswerSubformat getAnswerSubformat() {
		if (token.getQuestion().getAnswerFormat() == AnswerFormat.DATE
				&& token.getQuestion().getAnswerSubformat() != AnswerSubformat.DATE_PERIOD) {
			// Date time or period format
			if (AnswerSubformat.DATE_PERIOD.getRegex().matcher(value.getValue()).matches()) {
				return AnswerSubformat.DATE_PERIOD;
			}
		}
		return token.getQuestion().getAnswerSubformat();
	}

	public String getValue() {
		return value.getValue();
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
		if (token.getQuestion().getAnswerFormat() == AnswerFormat.DATE) {
			if (token.getQuestion().getAnswerSubformat() == AnswerSubformat.DATE_PERIOD) {
				datePeriodUnit.setNullSelectionAllowed(false);
			} else {
				datePeriodUnit.setNullSelectionAllowed(true);
			}
			datePeriodUnit.setValue(token.getDatePeriodUnit());
			if(token.getSubformat()==AnswerSubformat.DATE_PERIOD && token.getDatePeriodUnit()==null){
				//If there is an error on DB.
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

		for (TokenTypes type : token.getQuestion().getAnswerFormat().getValidTokenTypes()) {
			addTokenType(type);
		}
		operator.setValue(token.getType());

		updateHintAndValidators();
		value.setValue(token.getValue());
	}

	protected void updateHintAndValidators() {
		value.removeAllValidators();
		if (datePeriodUnit.getValue() == null) {
			value.setInputPrompt(token.getQuestion().getAnswerSubformat().getHint());
			value.addValidator(new ValidatorPattern(token.getQuestion().getAnswerSubformat().getRegex()));
		} else {
			value.setInputPrompt(AnswerSubformat.DATE_PERIOD.getHint());
			value.addValidator(new ValidatorPattern(AnswerSubformat.DATE_PERIOD.getRegex()));
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