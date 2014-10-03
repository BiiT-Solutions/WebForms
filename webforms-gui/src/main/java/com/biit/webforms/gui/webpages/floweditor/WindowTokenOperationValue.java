package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenTypes;
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

	private Label treeElementLabel;
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

		operator = new ComboBox();
		operator.setNullSelectionAllowed(false);

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
		for (TokenTypes type : token.getQuestion().getAnswerFormat().getValidTokenTypes()) {
			addTokenType(type);
		}
		operator.setValue(token.getType());

		String inputPrompt = token.getQuestion().getAnswerSubformat().getHint();
		if (token.getQuestion().getAnswerFormat() == AnswerFormat.DATE
				&& token.getQuestion().getAnswerSubformat() != AnswerSubformat.DATE_PERIOD) {
			value.addValidator(new ValidatorPattern(token.getQuestion().getAnswerSubformat().getRegex(),
					AnswerSubformat.DATE_PERIOD.getRegex()));
		} else {
			value.addValidator(new ValidatorPattern(token.getQuestion().getAnswerSubformat().getRegex()));
		}
		value.setInputPrompt(inputPrompt);
		value.setValue(token.getValue());

	}

	private void addTokenType(TokenTypes type) {
		operator.addItem(type);
		operator.setItemCaption(type, type.toString());
	}
}