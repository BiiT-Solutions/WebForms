package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.TableTreeObject;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WindowTokenIn extends WindowAcceptCancel {
	private static final long serialVersionUID = 8283244569022099823L;

	private static final String WIDTH = "550px";
	private static final String HEIGHT = "450px";
	private static final String TABLE_HEIGHT = "224px";
	private static final String TABLE_WIDTH = "350px";
	private static final String LABEL_HEIGTH = "30px";

	private Label treeElementLabel;
	private TableTreeObject answer;
	private TokenIn token;

	public WindowTokenIn() {
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

		answer = new TableTreeObject();
		answer.setNullSelectionAllowed(false);
		answer.setImmediate(true);
		answer.setHeight(TABLE_HEIGHT);
		answer.setWidth(TABLE_WIDTH);
		answer.setSelectable(true);
		answer.setMultiSelect(true);
		answer.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -3716751101306526511L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateAcceptButton();
			}
		});

		rootLayout.addComponent(treeElementLabel);
		rootLayout.addComponent(answer);

		rootLayout.setComponentAlignment(treeElementLabel, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(answer, Alignment.MIDDLE_CENTER);

		rootLayout.setExpandRatio(answer, 1.00f);

		return rootLayout;
	}

	protected void updateAcceptButton() {
		getAcceptButton().setEnabled(selectedMoreThanOneValidElement());
	}

	public void setToken(TokenIn token) {
		this.token = token;
		update();
	}

	private void update() {
		treeElementLabel.setValue(token.getQuestion().getName());
		
		for (TreeObject child : token.getQuestion().getChildren()) {
			addAnswerElement((Answer) child);
		}

		Set<Object> selectedValues = new HashSet<>();
		if (token.getAnswerValues() == null || token.getAnswerValues().isEmpty()) {
			answer.setValue(null);
		} else {
			for (Answer answer : token.getAnswerValues()) {
				selectedValues.add(answer);
			}
			answer.setValue(selectedValues);
		}
	}
	
	private void addAnswerElement(Answer answerElement) {
		answer.loadTreeObject(answerElement, null);
	}

	private boolean selectedMoreThanOneValidElement() {
		if (answer.getValue() == null) {
			return false;
		} else {
			@SuppressWarnings("unchecked")
			Set<Object> selectedValues = (Set<Object>) answer.getValue();
			if (selectedValues.size() > 1) {
				for (Object selectedValue : selectedValues) {
					Answer answer = (Answer) selectedValue;
					if (!answer.isFinalAnswer()) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
	}
	public Answer[] getAnswers() {
		List<Answer> answers = new ArrayList<>();
	
		@SuppressWarnings("unchecked")
		Set<Object> selectedValues = (Set<Object>) answer.getValue();
		Iterator<Object> itr = selectedValues.iterator();
		while(itr.hasNext()){
			answers.add((Answer)itr.next());
		}
		return answers.toArray(new Answer[]{});
	}
}
