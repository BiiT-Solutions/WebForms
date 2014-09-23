package com.biit.webforms.gui.webpages.floweditor;

import javax.transaction.NotSupportedException;

import com.biit.form.TreeObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.FlowConditionScript;
import com.biit.webforms.utils.lexer.TokenTypes;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class ConditionEditor extends CustomComponent {
	private static final long serialVersionUID = -4957758105459476797L;

	private TextArea textArea;
	private ConditionEditorControls controls;

	public ConditionEditor() {
		super();
		setSizeFull();
		setCompositionRoot(generateComposition());
		initializeComposition();
	}

	/**
	 * Initialize composition default values
	 */
	private void initializeComposition() {
		clean();
	}

	/**
	 * Generate Vaadin composition
	 * 
	 * @return
	 */
	private Component generateComposition() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setSpacing(true);
		
		VerticalLayout checkAndText = new VerticalLayout();
		checkAndText.setSizeFull();
		
		checkAndText.addComponent(new Button("kiwi"));

		textArea = new TextArea();
		textArea.setSizeFull();
		// textArea.addTextChangeListener(new TextChangeListener() {
		// private static final long serialVersionUID = 5907726658563018736L;
		//
		// @Override
		// public void textChange(TextChangeEvent event) {
		//
		// }
		// });
		textArea.setImmediate(true);
		checkAndText.addComponent(textArea);
		checkAndText.setExpandRatio(textArea, 1.0f);

		controls = new ConditionEditorControls();
		controls.setSizeFull();
		controls.addInsertTokenListener(new InsertTokenListener() {

			@Override
			public void insert(TreeObject currentTreeObjectReference) {
				append(currentTreeObjectReference);
			}

			@Override
			public void insert(TokenTypes type) {
				append(type);
			}

			@Override
			public void insert(Answer answerValue) {
				append(answerValue);
			}
		});

		horizontalLayout.addComponent(checkAndText);
		horizontalLayout.addComponent(controls);

		horizontalLayout.setExpandRatio(textArea, 0.70f);
		horizontalLayout.setExpandRatio(checkAndText, 0.30f);
		return horizontalLayout;
	}

	private void append(TokenTypes token) {
		append(token.getStringForm());
	}

	private void append(String value) {
		if (!textArea.getValue().isEmpty()) {
			textArea.setValue(textArea.getValue() + " ");
		}
		textArea.setValue(textArea.getValue() + value);
	}

	private void append(TreeObject treeObject) {
		if (treeObject instanceof FlowConditionScript) {
			append(((FlowConditionScript) treeObject).getScriptRepresentation());
		} else {
			WebformsLogger.errorMessage(this.getClass().getName(), new NotSupportedException(
					"TreeObject that doesn't support FlowConditionScript"));
		}
	}

	private void append(Answer reference) {
		// If reference is a Answer, we insert a equals statement
		append(reference.getScriptRepresentation());
	}

	public void clean() {
		textArea.setValue(new String());
	}

	/**
	 * Sets treeObject as the selected value in the reference table on control
	 * panel.
	 * 
	 * @param treeObject
	 */
	public void selectReferenceElement(TreeObject treeObject) {
		controls.selectTreeObject(treeObject);
	}

	public String getCondition() {
		return textArea.getValue();
	}

	public void setCondition(String conditionString) {
		textArea.setValue(conditionString);
	}

}
