package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.common.components.FilterTreeObjectTableContainsName;
import com.biit.webforms.gui.common.components.TableTreeObject;
import com.biit.webforms.gui.common.components.TableWithSearch;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.floweditor.listeners.InsertTokenListener;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ConditionEditorControls extends TabSheet {
	private static final long serialVersionUID = 105765485106857208L;
	private static final String FULL = "100%";
	private static final String EXPAND = null;
	private static final int NUM_BUTTON_COLUMNS = 3;
	private static final int NUM_BUTTON_ROWS = 2;
	protected static final TokenTypes DEFAULT_ANSWER_REFERENCE_TOKEN = TokenTypes.EQ;
	private static final int VALUE_BUTTON_COLS = 3;
	private static final int VALUE_BUTTON_ROWS = 2;
	private static final float EXPAND_RATIO_REFERENCE = 0.7f;
	private static final float EXPAND_RATIO_INSERT_VALUE_OR_ANSWER = 0.3f;

	private TableTreeObject treeObjectTable;
	private TableTreeObject answerTable;
	private Button insertEqAnswer;
	private Button insertNeAnswer;
	private VerticalLayout insertLayout;
	private VerticalLayout insertAnswerLayout;
	private VerticalLayout insertValueLayout;
	private ComboBox dateFormatType;

	// Logic
	private Button and, or, not;
	// Order
	private Button leftPar, rightPar;
	// Oher
	private Button carry;

	private TextField value;
	private Button insertEqValue, insertNeValue, insertLtValue, insertGtValue, insertLeValue, insertGeValue;

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

	}

	/**
	 * Layout with treeobjectTable and button to insert reference.
	 * 
	 * @return
	 */
	private Component generateTreeTableSearch() {
		treeObjectTable = new TableTreeObject();
		treeObjectTable.setSizeFull();
		treeObjectTable.setSelectable(true);
		// Insert reference button is only enabled if the selection in tree
		// object table is not null and not a form
		treeObjectTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1366837844085658172L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (treeObjectTable.getValue() == null || !(treeObjectTable.getValue() instanceof Question)) {
					insertLayout.removeAllComponents();
				} else {
					AnswerType answerType = ((Question) treeObjectTable.getValue()).getAnswerType();
					if (answerType == AnswerType.INPUT) {
						showInsertValueLayout();
					} else {
						showInsertAnswerLayout();
					}
				}
				updateInsertAnswerLayout();
			}
		});

		TableWithSearch tableWithSearch = new TableWithSearch(treeObjectTable, new FilterTreeObjectTableContainsName());
		tableWithSearch.setSearchCaptionAtLeft(true);
		tableWithSearch.setSizeFull();

		return tableWithSearch;
	}

	protected void showInsertValueLayout() {
		insertLayout.removeAllComponents();
		updateInsertValueLayout();
		insertLayout.addComponent(insertValueLayout);
		insertLayout.setComponentAlignment(insertValueLayout, Alignment.BOTTOM_CENTER);
	}

	/**
	 * Updates insert value hint, prompt and validator.
	 */
	private void updateInsertValueLayout() {
		value.removeAllValidators();
		value.setValue("");
		if (getCurrentQuestion().getAnswerFormat() == AnswerFormat.DATE
				&& getCurrentQuestion().getAnswerSubformat() != AnswerSubformat.DATE_PERIOD) {
			dateFormatType.setValue(null);
			dateFormatType.setEnabled(true);
			updateDateValidatorAndInputPrompt();
		} else {
			value.setInputPrompt(getCurrentQuestion().getAnswerSubformat().getHint());
			value.addValidator(new ValidatorPattern(getCurrentQuestion().getAnswerSubformat().getRegex()));
			dateFormatType.setValue(null);
			dateFormatType.setEnabled(false);
		}

		insertEqValue.setEnabled(getCurrentQuestion().getAnswerFormat().isValidTokenType(TokenTypes.EQ));
		insertNeValue.setEnabled(getCurrentQuestion().getAnswerFormat().isValidTokenType(TokenTypes.NE));
		insertLeValue.setEnabled(getCurrentQuestion().getAnswerFormat().isValidTokenType(TokenTypes.LE));
		insertGeValue.setEnabled(getCurrentQuestion().getAnswerFormat().isValidTokenType(TokenTypes.GE));
		insertLtValue.setEnabled(getCurrentQuestion().getAnswerFormat().isValidTokenType(TokenTypes.LT));
		insertGtValue.setEnabled(getCurrentQuestion().getAnswerFormat().isValidTokenType(TokenTypes.GT));
	}

	private void updateDateValidatorAndInputPrompt() {
		value.removeAllValidators();
		if (getCurrentQuestion() != null && getCurrentQuestion().getAnswerSubformat() != null) {
			if (dateFormatType.getValue() == null) {
				value.setInputPrompt(getCurrentQuestion().getAnswerSubformat().getHint());
				value.addValidator(new ValidatorPattern(getCurrentQuestion().getAnswerSubformat().getRegex()));
			} else {
				value.setInputPrompt(AnswerSubformat.NUMBER.getHint());
				value.addValidator(new ValidatorPattern(AnswerSubformat.NUMBER.getRegex()));
			}
		}
	}

	private Question getCurrentQuestion() {
		return (Question) treeObjectTable.getValue();
	}

	protected void showInsertAnswerLayout() {
		insertLayout.removeAllComponents();
		insertLayout.addComponent(insertAnswerLayout);
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
		insertEqAnswer.setEnabled(answerTable.getValue() != null);
		insertNeAnswer.setEnabled(answerTable.getValue() != null);
	}

	/**
	 * Layout with ListSelection and button to insert Q=A
	 * 
	 * @return
	 */
	private Component generateInsertComparationAnswerLayout() {
		insertAnswerLayout = new VerticalLayout();
		insertAnswerLayout.setSpacing(true);
		insertAnswerLayout.setSizeFull();

		answerTable = new TableTreeObject();
		answerTable.setSizeFull();
		answerTable.setNullSelectionAllowed(false);
		answerTable.setImmediate(true);
		answerTable.setSelectable(true);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(FULL);
		buttonLayout.setHeight(EXPAND);

		insertEqAnswer = new Button(TokenTypes.EQ.toString());
		insertEqAnswer.setWidth(FULL);
		insertEqAnswer.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2959069539884251545L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireInsertTokenListeners(TokenComparationAnswer.getTokenEqual((Question) treeObjectTable.getValue(),
						(Answer) answerTable.getValue()));
			}
		});
		insertNeAnswer = new Button(TokenTypes.NE.toString());
		insertNeAnswer.setWidth(FULL);
		insertNeAnswer.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2959069539884251545L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireInsertTokenListeners(TokenComparationAnswer.getTokenNotEqual((Question) treeObjectTable.getValue(),
						(Answer) answerTable.getValue()));
			}
		});

		buttonLayout.addComponent(insertEqAnswer);
		buttonLayout.addComponent(insertNeAnswer);

		insertAnswerLayout.addComponent(answerTable);
		insertAnswerLayout.addComponent(buttonLayout);
		insertAnswerLayout.setExpandRatio(answerTable, 1.0f);

		return insertAnswerLayout;
	}

	/**
	 * Layout with textField and button to insert Q=Value
	 * 
	 * @return
	 */
	private Component generateInsertComparationValueLayout() {
		insertValueLayout = new VerticalLayout();
		insertValueLayout.setSpacing(true);
		insertValueLayout.setWidth(FULL);
		insertValueLayout.setHeight(EXPAND);
		insertValueLayout.setSpacing(true);

		dateFormatType = new ComboBox();
		dateFormatType.setWidth(FULL);
		for (DateTypeUi dateType : DateTypeUi.values()) {
			dateFormatType.addItem(dateType);
			dateFormatType.setItemCaption(dateType, dateType.getTranslation());
		}
		dateFormatType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -333682134124174959L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateDateValidatorAndInputPrompt();
			}
		});

		value = new TextField();
		value.setWidth(FULL);
		value.setRequired(true);
		value.setImmediate(true);
		value.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -5155837781711968901L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				value.isValid();
			}
		});

		insertEqValue = createTokenComparationValueButton(TokenTypes.EQ.toString(), TokenTypes.EQ);
		insertNeValue = createTokenComparationValueButton(TokenTypes.NE.toString(), TokenTypes.NE);
		insertLtValue = createTokenComparationValueButton(TokenTypes.LE.toString(), TokenTypes.LE);
		insertGtValue = createTokenComparationValueButton(TokenTypes.GE.toString(), TokenTypes.GE);
		insertLeValue = createTokenComparationValueButton(TokenTypes.LT.toString(), TokenTypes.LT);
		insertGeValue = createTokenComparationValueButton(TokenTypes.GT.toString(), TokenTypes.GT);

		GridLayout buttonLayout = new GridLayout(VALUE_BUTTON_COLS, VALUE_BUTTON_ROWS, insertEqValue, insertLeValue,
				insertLtValue, insertNeValue, insertGeValue, insertGtValue);
		buttonLayout.setWidth(FULL);

		insertValueLayout.addComponent(dateFormatType);
		insertValueLayout.addComponent(value);
		insertValueLayout.addComponent(buttonLayout);

		insertValueLayout.setComponentAlignment(value, Alignment.BOTTOM_CENTER);
		insertValueLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

		return insertValueLayout;
	}

	private Component generateReferenceTab() {
		VerticalLayout root = new VerticalLayout();
		root.setWidth(FULL);
		root.setHeight(FULL);
		root.setSpacing(true);
		root.setMargin(true);

		Component insertReferenceLayout = generateTreeTableSearch();
		// Generate comparation answer and value layout.
		generateInsertComparationAnswerLayout();
		generateInsertComparationValueLayout();

		// Layout where we will change elements. Is needed to avoid resizing
		// problems in component
		insertLayout = new VerticalLayout();
		insertLayout.setSizeFull();

		root.addComponent(insertReferenceLayout);
		root.addComponent(insertLayout);
		root.setExpandRatio(insertReferenceLayout, EXPAND_RATIO_REFERENCE);
		root.setExpandRatio(insertLayout, EXPAND_RATIO_INSERT_VALUE_OR_ANSWER);

		return root;
	}

	private Button createTokenComparationValueButton(String caption, final TokenTypes type) {
		Button button = new Button(caption);
		button.setWidth(FULL);
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 150904421483217498L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (value.isValid()) {
					fireInsertTokenListeners(TokenComparationValue.getToken(type, getCurrentQuestion(),
							getCurrentValueAnswerSubformat(), getCurrentValue()));
				} else {
					MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_VALUE_HAS_WRONG_FORMAT);
				}
			}
		});
		return button;
	}

	protected String getCurrentValue() {
		if (dateFormatType.getValue() == null) {
			return value.getValue();
		} else {
			return value.getValue() + ((DateTypeUi) (dateFormatType.getValue())).getRepresentation();
		}
	}

	private Button createTokenButton(String string, final TokenTypes tokenType) {
		Button button = new Button(string);
		button.setWidth(FULL);
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 150904421483217498L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireInsertTokenListeners(Token.getToken(tokenType));
			}
		});
		return button;
	}

	protected AnswerSubformat getCurrentValueAnswerSubformat() {
		if (getCurrentQuestion().getAnswerFormat() == AnswerFormat.DATE
				&& getCurrentQuestion().getAnswerSubformat() != AnswerSubformat.DATE_PERIOD) {
			if (dateFormatType.getValue() == null) {
				// There is no dateFormat defined (normal date)
				return getCurrentQuestion().getAnswerSubformat();
			} else {
				return AnswerSubformat.DATE_PERIOD;
			}
		}
		return getCurrentQuestion().getAnswerSubformat();
	}

	private Component generateOperationButtons() {
		and = createTokenButton("AND", TokenTypes.AND);
		or = createTokenButton("OR", TokenTypes.OR);
		not = createTokenButton("NOT", TokenTypes.NOT);
		// Order
		leftPar = createTokenButton("(", TokenTypes.LEFT_PAR);
		rightPar = createTokenButton(")", TokenTypes.RIGHT_PAR);
		// Oher
		carry = createTokenButton("\u23CE", TokenTypes.RETURN);

		GridLayout buttonHolder = new GridLayout(NUM_BUTTON_COLUMNS, NUM_BUTTON_ROWS, and, or, not, leftPar, rightPar,
				carry);
		buttonHolder.setCaption(LanguageCodes.CAPTION_OPERATORS.translation());

		buttonHolder.setWidth(FULL);
		buttonHolder.setHeight(EXPAND);

		return buttonHolder;
	}

	private Component generateControlsTab() {
		VerticalLayout root = new VerticalLayout();
		root.setWidth(FULL);
		root.setHeight(FULL);
		root.setMargin(true);

		Component buttonHolder = generateOperationButtons();
		root.addComponent(buttonHolder);
		root.setComponentAlignment(buttonHolder, Alignment.TOP_CENTER);

		return root;
	}

	public void addInsertTokenListener(InsertTokenListener listener) {
		insertTokenListeners.add(listener);
	}

	protected void fireInsertTokenListeners(Token token) {
		for (InsertTokenListener listener : insertTokenListeners) {
			listener.insert(token);
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

	public TextField getValueField() {
		return value;
	}

	public void addFilter(Filter filter) {
		((Filterable) treeObjectTable.getContainerDataSource()).addContainerFilter(filter);
	}

	public void removeFilter(Filter filter) {
		((Filterable) treeObjectTable.getContainerDataSource()).removeContainerFilter(filter);
	}
}
