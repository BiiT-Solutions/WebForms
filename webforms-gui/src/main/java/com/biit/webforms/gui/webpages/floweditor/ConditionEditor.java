package com.biit.webforms.gui.webpages.floweditor;

import javax.transaction.NotSupportedException;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.StatusLabel;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.FlowConditionScript;
import com.biit.webforms.utils.lexer.TokenTypes;
import com.biit.webforms.utils.lexer.exceptions.TokenizationError;
import com.biit.webforms.utils.parser.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.WebformsParser;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.Expression;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class ConditionEditor extends CustomComponent {
	private static final long serialVersionUID = -4957758105459476797L;

	private static final String CONDITION_VALIDATOR = "condition-validator";

	private static final String VALIDATE_BUTTON_HEIGHT = "26px";

	private TextArea textArea;
	private ConditionEditorControls controls;
	private Button validate;
	private StatusLabel status;

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

		checkAndText.addComponent(generateValidator());

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

		horizontalLayout.setExpandRatio(checkAndText, 0.70f);
		horizontalLayout.setExpandRatio(controls, 0.30f);
		return horizontalLayout;
	}

	private Component generateValidator() {
		CssLayout validator = new CssLayout();
		validator.setStyleName(CONDITION_VALIDATOR);

		validate = new Button(LanguageCodes.CAPTION_VALIDATE_CONDITION.translation());
		validate.setHeight(VALIDATE_BUTTON_HEIGHT);
		validate.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 4472374360839523290L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateCondition();
			}
		});
		status = new StatusLabel("");

		validator.addComponent(validate);
		validator.addComponent(status);
		return validator;
	}

	public boolean validateCondition() {
		String currentCondition = textArea.getValue();
		System.out.println("Validate: " + currentCondition);

		try {
			WebformsParser parser = new WebformsParser(currentCondition);
			Expression expression = parser.parseExpression();
			if (expression == null) {
				// No expression
				System.out.println("Expression: empty expression");
				status.setOkText(LanguageCodes.CAPTION_OK_EMPTY_EXPRESSION.translation());
			} else {
				System.out.println("Expression: " + expression.getString());
			}
			return true;
		} catch (TokenizationError | ParseException | ExpectedTokenNotFound e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			
		}
		return false;
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
