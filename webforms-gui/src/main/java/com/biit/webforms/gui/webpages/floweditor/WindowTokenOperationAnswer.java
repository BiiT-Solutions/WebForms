package com.biit.webforms.gui.webpages.floweditor;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenTypes;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class WindowTokenOperationAnswer extends WindowAcceptCancel {
	private static final long serialVersionUID = 697691525922280194L;
	private static final String WIDTH = "650px";
	private static final String HEIGHT = "250px";

	private Label treeElementLabel;
	private ComboBox operator;
	private ComboBox answer;
	private TokenComparationAnswer token;

	public WindowTokenOperationAnswer() {
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
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		treeElementLabel = new Label("null-content");
		treeElementLabel.setImmediate(true);
		treeElementLabel.setWidth(null);

		operator = new ComboBox();
		operator.setNullSelectionAllowed(false);

		answer = new ComboBox();
		answer.setNullSelectionAllowed(false);
		answer.setImmediate(true);

		rootLayout.addComponent(treeElementLabel);
		rootLayout.addComponent(operator);
		rootLayout.addComponent(answer);

		rootLayout.setComponentAlignment(treeElementLabel, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(operator, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(answer, Alignment.MIDDLE_CENTER);

		return rootLayout;
	}

	public TokenTypes getOperator() {
		return (TokenTypes) operator.getValue();
	}

	public void setToken(TokenComparationAnswer token) {
		this.token = token;
		update();
	}

	private void update() {
		treeElementLabel.setValue(token.getQuestion().getName());
		// Set label
		addTokenType(TokenTypes.EQ);
		addTokenType(TokenTypes.NE);

		operator.setValue(token.getType());

		for (TreeObject child : token.getQuestion().getChildren()) {
			addAnswerElement((Answer) child);
		}
		answer.setValue(token.getAnswer());
	}

	private void addAnswerElement(Answer answerElement) {
		answer.addItem(answerElement);
		answer.setItemCaption(answerElement, answerElement.getPathAnswerValue());
		for (TreeObject child : answerElement.getChildren()) {
			addAnswerElement((Answer) child);
		}
	}

	private void addTokenType(TokenTypes type) {
		operator.addItem(type);
		operator.setItemCaption(type, type.toString());
	}

	public Answer getAnswer() {
		return (Answer) answer.getValue();
	}
}