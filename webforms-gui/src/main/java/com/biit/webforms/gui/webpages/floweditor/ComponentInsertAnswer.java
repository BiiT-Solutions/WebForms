package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.common.components.TableTreeObject;
import com.biit.webforms.gui.webpages.floweditor.listeners.InsertTokenListener;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class ComponentInsertAnswer extends CustomComponent {
	private static final long serialVersionUID = 8934445212073001188L;
	private static final String FULL = "100%";
	private static final String EXPAND = null;
	private static final String COMPONENT_ID = "com.biit.webforms.gui.webpages.floweditor.ComponentInsertAnswer";
	private static final String ANSWER_TABLE_ID = "com.biit.webforms.gui.webpages.floweditor.ComponentInsertAnswer.AnswerTable";

	private List<InsertTokenListener> insertTokenListeners;

	private Question currentQuestion;
	private VerticalLayout rootLayout;
	private TableTreeObject answerTable;
	private Button insertEqAnswer;
	private Button insertNeAnswer;
	private Button insertInAnswer;

	public ComponentInsertAnswer() {
		super();
		setId(COMPONENT_ID);
		insertTokenListeners = new ArrayList<InsertTokenListener>();
		setCompositionRoot(generate());
		setSizeFull();
	}

	private Component generate() {
		rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		answerTable = new TableTreeObject();
		answerTable.setId(ANSWER_TABLE_ID);
		answerTable.setSizeFull();
		answerTable.setNullSelectionAllowed(false);
		answerTable.setImmediate(true);
		answerTable.setSelectable(true);
		answerTable.setMultiSelect(true);
		answerTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -6634125293095182977L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateInsertAnswerButtonState();
			}
		});

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(FULL);
		buttonLayout.setHeight(EXPAND);

		insertEqAnswer = new Button(TokenTypes.EQ.toString());
		insertEqAnswer.setWidth(FULL);
		insertEqAnswer.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2959069539884251545L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireInsertTokenListeners(TokenComparationAnswer.getTokenEqual(currentQuestion,
						getSingleAnswer()));
			}
		});
		insertNeAnswer = new Button(TokenTypes.NE.toString());
		insertNeAnswer.setWidth(FULL);
		insertNeAnswer.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2959069539884251545L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireInsertTokenListeners(TokenComparationAnswer.getTokenNotEqual(currentQuestion,
						getSingleAnswer()));
			}
		});

		insertInAnswer = new Button(TokenTypes.IN.toString());
		insertInAnswer.setWidth(FULL);
		insertInAnswer.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2959069539884251545L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireInsertTokenListeners(TokenIn.getTokenIn(currentQuestion,getAnswers()));
			}
		});

		buttonLayout.addComponent(insertEqAnswer);
		buttonLayout.addComponent(insertNeAnswer);
		buttonLayout.addComponent(insertInAnswer);

		rootLayout.addComponent(answerTable);
		rootLayout.addComponent(buttonLayout);
		rootLayout.setExpandRatio(answerTable, 1.0f);

		return rootLayout;
	}

	protected Answer[] getAnswers() {
		List<Answer> answers = new ArrayList<>();
	
		@SuppressWarnings("unchecked")
		Set<Object> selectedValues = (Set<Object>) answerTable.getValue();
		Iterator<Object> itr = selectedValues.iterator();
		while(itr.hasNext()){
			answers.add((Answer)itr.next());
		}
		return answers.toArray(new Answer[]{});
	}

	/**
	 * Removes all current items. Gets current selected element in tree.
	 * Question answers to the list and selects the first or just disables the
	 * whole component.
	 */
	protected void updateInsertAnswerLayout() {
		answerTable.removeAllItems();
		answerTable.setValue(null);
		
		if (currentQuestion == null || currentQuestion.getChildren().isEmpty()) {
			rootLayout.setEnabled(false);
		} else {
			rootLayout.setEnabled(true);
			for (TreeObject child : currentQuestion.getChildren(Answer.class)) {
				answerTable.loadTreeObject(child, null);
			}
			answerTable.setValue(null);
		}
		
		updateInsertAnswerButtonState();
	}

	private void updateInsertAnswerButtonState() {
		insertEqAnswer.setEnabled(selectedOneValidElement());
		insertNeAnswer.setEnabled(selectedOneValidElement());
		insertInAnswer.setEnabled(selectedMoreThanOneValidElement());
	}

	private boolean selectedOneValidElement() {
		if (answerTable.getValue() == null) {
			return false;
		} else {
			@SuppressWarnings("unchecked")
			Set<Object> selectedValues = (Set<Object>) answerTable.getValue();
			if (selectedValues.size() == 1) {
				for (Object selectedValue : selectedValues) {
					Answer answer = (Answer) selectedValue;
					if (answer.isFinalAnswer()) {
						return true;
					}
				}
			}
			return false;
		}
	}

	private boolean selectedMoreThanOneValidElement() {
		if (answerTable.getValue() == null) {
			return false;
		} else {
			@SuppressWarnings("unchecked")
			Set<Object> selectedValues = (Set<Object>) answerTable.getValue();
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
	
	private Answer getSingleAnswer(){
		@SuppressWarnings("unchecked")
		Set<Object> selectedValues = (Set<Object>) answerTable.getValue();
		return (Answer)selectedValues.iterator().next();
	}

	public void addInsertTokenListener(InsertTokenListener listener) {
		insertTokenListeners.add(listener);
	}

	protected void fireInsertTokenListeners(Token token) {
		for (InsertTokenListener listener : insertTokenListeners) {
			listener.insert(token);
		}
	}

	public void setCurrentQuestion(Question currentQuestion) {
		this.currentQuestion = currentQuestion;
		updateInsertAnswerLayout();
	}
}
