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

import com.biit.form.entity.TreeObject;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.common.components.TableTreeObject;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.*;

public class WindowTokenOperationAnswer extends WindowAcceptCancel {
	private static final long serialVersionUID = 697691525922280194L;
	private static final String WIDTH = "550px";
	private static final String HEIGHT = "450px";
	private static final String TABLE_HEIGHT = "224px";
	private static final String TABLE_WIDTH = "350px";
	private static final String LABEL_HEIGTH = "30px";
	private static final String OPERATOR_HEIGTH = "30px";

	private Label treeElementLabel;
	private ComboBox operator;
	private TableTreeObject answer;
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
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		treeElementLabel = new Label("null-content");
		treeElementLabel.setImmediate(true);
		treeElementLabel.setWidth(null);
		treeElementLabel.setHeight(LABEL_HEIGTH);

		operator = new ComboBox();
		operator.setNullSelectionAllowed(false);
		operator.setHeight(OPERATOR_HEIGTH);

		answer = new TableTreeObject();
		answer.setNullSelectionAllowed(false);
		answer.setImmediate(true);
		answer.setHeight(TABLE_HEIGHT);
		answer.setWidth(TABLE_WIDTH);
		answer.setSelectable(true);
		answer.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -3716751101306526511L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() == null || !(event.getProperty().getValue() instanceof Answer)) {
					getAcceptButton().setEnabled(false);
				} else {
					Answer answer = (Answer) event.getProperty().getValue();
					getAcceptButton().setEnabled(answer.getChildren() == null || answer.getChildren().isEmpty());
				}
			}
		});

		rootLayout.addComponent(treeElementLabel);
		rootLayout.addComponent(operator);
		rootLayout.addComponent(answer);

		rootLayout.setComponentAlignment(treeElementLabel, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(operator, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(answer, Alignment.MIDDLE_CENTER);

		rootLayout.setExpandRatio(answer, 1.00f);

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
		if (token.getQuestion() != null) {
			treeElementLabel.setValue(token.getQuestion().getName());
			// Set label
			addTokenType(TokenTypes.EQ);
			addTokenType(TokenTypes.NE);

			operator.setValue(token.getType());

			for (TreeObject child : token.getQuestion().getChildren()) {
				addAnswerElement((Answer) child);
			}
			if (token.getAnswer() != null) {
				answer.setValue(token.getAnswer());
			}
		}
	}

	private void addAnswerElement(Answer answerElement) {
		answer.loadTreeObject(answerElement, null);
	}

	private void addTokenType(TokenTypes type) {
		operator.addItem(type);
		operator.setItemCaption(type, type.toString());
	}

	public Answer getAnswer() {
		return (Answer) answer.getValue();
	}
}
