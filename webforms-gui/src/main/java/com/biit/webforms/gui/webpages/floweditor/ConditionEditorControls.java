package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.TreeObjectTable;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.enumerations.AnswerSubformat;
import com.biit.webforms.theme.ThemeIcons;
import com.biit.webforms.utils.lexer.TokenTypes;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Right side controls of
 * 
 */
public class ConditionEditorControls extends TabSheet {

	private static final long serialVersionUID = 105765485106857208L;
	private static final String FULL = "100%";
	private static final String EXPAND = null;
	private static final int NUM_BUTTON_COLUMNS = 3;
	private static final int NUM_BUTTON_ROWS = 4;
	protected static final TokenTypes DEFAULT_ANSWER_REFERENCE_TOKEN = TokenTypes.EQ;

	private TreeObjectTable treeObjectTable;
	private Button insertReference;
	private TreeObjectTable answerTable;
	private Button insertAnswer;
	private VerticalLayout insertAnswerLayout;

	// Logic
	private Button and, or, not;
	// Comparation
	private Button lessThan, lessEqual, greaterThan, greaterEqual, equals, notEquals;
	// Order
	private Button leftPar, rightPar;
	// Oher
	private Button carry;

	private ComboBox valueFormat;
	private TextField value;
	private Button insertValue;

	private List<InsertTokenListener> insertTokenListeners;

	public ConditionEditorControls() {
		super();
		insertTokenListeners = new ArrayList<>();
		generateComposition();
		initializeComposition();
	}

	private void generateComposition() {
		addTab(generateReferenceTab(), "", ThemeIcons.CONDITION_HELPER_FORM_REFERENCE.getThemeResource());
		addTab(generateControlsTab(), "", ThemeIcons.CONDITION_HELPER_CONTROLS.getThemeResource());
	}

	/**
	 * Initialize Ui default values.
	 */
	private void initializeComposition() {
		treeObjectTable.loadTreeObject(UserSessionHandler.getController().getFormInUse(), null, Form.class,
				Category.class, Group.class, Question.class);
		treeObjectTable.setValue(null);
		updateInsertAnswerLayout();

		// Subanswer format.
		for (AnswerSubformatUi subformat : AnswerSubformatUi.values()) {
			valueFormat.addItem(subformat.getSubformat());
			valueFormat.setItemCaption(subformat.getSubformat(), subformat.getTranslationCode().translation());
		}
		valueFormat.setValue(AnswerSubformatUi.values()[0].getSubformat());
	}

	/**
	 * Layout with treeobjectTable and button to insert reference.
	 * 
	 * @return
	 */
	private Component generateInsertReferenceLayout() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		treeObjectTable = new TreeObjectTable();
		treeObjectTable.setSizeFull();
		treeObjectTable.setSelectable(true);
		// Insert reference button is only enabled if the selection in tree
		// object table is not null and not a form
		treeObjectTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1366837844085658172L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (treeObjectTable.getValue() == null
						|| !(treeObjectTable.getValue() instanceof Question || treeObjectTable.getValue() instanceof Answer)) {
					insertReference.setEnabled(false);
				} else {
					insertReference.setEnabled(true);
				}
				updateInsertAnswerLayout();
			}
		});

		insertReference = new Button(ServerTranslate.translate(LanguageCodes.CAPTION_INSERT_QUESTION_REFENCE));
		insertReference.setWidth(FULL);
		insertReference.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2959069539884251545L;

			@Override
			public void buttonClick(ClickEvent event) {
				// Inserts only the reference to question or answer value
				fireInsertReferenceListeners(getCurrentTreeObjectReference());
			}
		});

		rootLayout.addComponent(treeObjectTable);
		rootLayout.addComponent(insertReference);
		rootLayout.setExpandRatio(treeObjectTable, 1.0f);

		return rootLayout;
	}

	/**
	 * Removes all current items. Gets current selected element in tree.
	 * Question answers to the list and selects the first or just disables the
	 * whole component.
	 */
	protected void updateInsertAnswerLayout() {
		answerTable.removeAllItems();
		answerTable.setValue(null);
		if (treeObjectTable.getValue() == null || !(treeObjectTable.getValue() instanceof Question)
				|| ((Question) treeObjectTable.getValue()).getChildren().isEmpty()) {
			insertAnswerLayout.setEnabled(false);
		} else {
			insertAnswerLayout.setEnabled(true);
			Question question = (Question) treeObjectTable.getValue();
			for (TreeObject child : question.getChildren()) {
				answerTable.loadTreeObject(child, null);
			}
			answerTable.setValue(question.getChildren().get(0));
		}
	}

	/**
	 * Layout with ListSelection and button to insert Q=A
	 * 
	 * @return
	 */
	private Component generateInsertAnswerLayout() {
		insertAnswerLayout = new VerticalLayout();
		insertAnswerLayout.setSpacing(true);
		insertAnswerLayout.setSizeFull();

		answerTable = new TreeObjectTable();
		answerTable.setSizeFull();
		answerTable.setNullSelectionAllowed(false);
		answerTable.setImmediate(true);
		answerTable.setSelectable(true);

		insertAnswer = new Button(ServerTranslate.translate(LanguageCodes.CAPTION_INSERT_ANSWER_REFENCE));
		insertAnswer.setWidth(FULL);
		insertAnswer.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2959069539884251545L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireInsertReferenceListeners(getCurrentTreeObjectReference());
				fireInsertTokenListeners(DEFAULT_ANSWER_REFERENCE_TOKEN);
				fireInsertAnswerValueListeners((Answer) answerTable.getValue());
				// TODO second level answer
			}
		});

		insertAnswerLayout.addComponent(answerTable);
		insertAnswerLayout.addComponent(insertAnswer);
		insertAnswerLayout.setExpandRatio(answerTable, 1.0f);

		return insertAnswerLayout;
	}

	private Component generateReferenceTab() {
		VerticalLayout root = new VerticalLayout();
		root.setWidth(FULL);
		root.setHeight(FULL);
		root.setSpacing(true);

		Component insertReferenceLayout = generateInsertReferenceLayout();
		Component insertAnswerLayout = generateInsertAnswerLayout();

		root.addComponent(insertReferenceLayout);
		root.addComponent(insertAnswerLayout);

		root.setExpandRatio(insertReferenceLayout, 1.0f);
		root.setExpandRatio(insertAnswerLayout, 1.0f);

		return root;
	}

	private Button createButton(String caption, final TokenTypes type) {
		Button button = new Button(caption);
		button.setWidth(FULL);
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 150904421483217498L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireInsertTokenListeners(type);
			}
		});
		return button;
	}

	private Component generateControlsTab() {
		VerticalLayout root = new VerticalLayout();
		root.setWidth(FULL);
		root.setHeight(FULL);

		and = createButton("AND", TokenTypes.AND);
		or = createButton("OR", TokenTypes.OR);
		not = createButton("NOT", TokenTypes.NOT);
		// Comparation
		lessThan = createButton("<", TokenTypes.LT);
		lessEqual = createButton("<=", TokenTypes.LE);
		greaterThan = createButton(">", TokenTypes.GT);
		greaterEqual = createButton(">=", TokenTypes.GE);
		equals = createButton("==", TokenTypes.EQ);
		notEquals = createButton("!=", TokenTypes.NE);
		// Order
		leftPar = createButton("(", TokenTypes.LEFT_PAR);
		rightPar = createButton(")", TokenTypes.RIGHT_PAR);
		// Oher
		carry = createButton("\u23CE", TokenTypes.RETURN);

		GridLayout buttonHolder = new GridLayout(NUM_BUTTON_COLUMNS, NUM_BUTTON_ROWS, and, or, not, lessThan,
				lessEqual, greaterThan, greaterEqual, equals, notEquals, leftPar, rightPar, carry);
		buttonHolder.setCaption(LanguageCodes.CAPTION_OPERATORS.translation());

		buttonHolder.setWidth(FULL);
		buttonHolder.setHeight(EXPAND);

		root.addComponent(buttonHolder);
		root.setComponentAlignment(buttonHolder, Alignment.MIDDLE_CENTER);

		VerticalLayout verticalValueLayout = new VerticalLayout();
		verticalValueLayout.setCaption(LanguageCodes.CAPTION_VALUE.translation());
		verticalValueLayout.setWidth(FULL);
		verticalValueLayout.setHeight(EXPAND);
		verticalValueLayout.setSpacing(true);

		valueFormat = new ComboBox();
		valueFormat.setNullSelectionAllowed(false);
		valueFormat.setImmediate(true);
		valueFormat.setWidth(FULL);
		valueFormat.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 7254145595251837513L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				value.removeAllValidators();
				
				value.setValue("");
				value.setInputPrompt(((AnswerSubformat) valueFormat.getValue()).getHint());
				
				value.addValidator(new ValidatorPattern(((AnswerSubformat) valueFormat.getValue()).getRegex()));
			}
		});
		value = new TextField();
		value.setWidth(FULL);
		
		value.setImmediate(true);
		insertValue = new Button(LanguageCodes.CAPTION_VALUE.translation());
		insertValue.setWidth(FULL);
		insertValue.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 5350736534893525919L;

			@Override
			public void buttonClick(ClickEvent event) {
				insertValue();
			}
		});		

		verticalValueLayout.addComponent(valueFormat);
		verticalValueLayout.addComponent(value);
		verticalValueLayout.addComponent(insertValue);

		root.addComponent(verticalValueLayout);
		root.setComponentAlignment(verticalValueLayout, Alignment.MIDDLE_CENTER);

		return root;
	}

	protected void insertValue() {
		// TODO Auto-generated method stub

	}

	public void addInsertTokenListener(InsertTokenListener listener) {
		insertTokenListeners.add(listener);
	}

	protected void fireInsertTokenListeners(TokenTypes type) {
		for (InsertTokenListener listener : insertTokenListeners) {
			listener.insert(type);
		}
	}

	protected void fireInsertReferenceListeners(TreeObject treeObject) {
		for (InsertTokenListener listener : insertTokenListeners) {
			listener.insert(treeObject);
		}
	}

	protected void fireInsertAnswerValueListeners(Answer answer) {
		for (InsertTokenListener listener : insertTokenListeners) {
			listener.insert(answer);
		}
	}

	public TreeObject getCurrentTreeObjectReference() {
		if (treeObjectTable.getValue() != null && !(treeObjectTable.getValue() instanceof Form)) {
			TreeObject reference = (TreeObject) treeObjectTable.getValue();
			return reference;
		} else {
			return null;
		}
	}

	/**
	 * Selects a treeObject element from the form tree object table.
	 * 
	 * @param treeObject
	 */
	public void selectTreeObject(TreeObject treeObject) {
		treeObjectTable.setValue(treeObject);
	}
}
