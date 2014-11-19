package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.common.components.FilterTreeObjectTableContainsName;
import com.biit.webforms.gui.common.components.TableTreeObject;
import com.biit.webforms.gui.common.components.TableWithSearch;
import com.biit.webforms.gui.webpages.floweditor.listeners.InsertTokenListener;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class ConditionEditorControls extends TabSheet {
	private static final long serialVersionUID = 105765485106857208L;
	private static final String FULL = "100%";
	private static final String EXPAND = null;
	private static final int NUM_BUTTON_COLUMNS = 3;
	private static final int NUM_BUTTON_ROWS = 2;
	protected static final TokenTypes DEFAULT_ANSWER_REFERENCE_TOKEN = TokenTypes.EQ;

	private static final float EXPAND_RATIO_REFERENCE = 60.0f;
	private static final String REDUCED_LAYOUT_MARGIN = "v-reduced-layout-margin";

	private TableTreeObject treeObjectTable;
	private TableTreeObject answerTable;
	private Button insertEqAnswer;
	private Button insertNeAnswer;
	private VerticalLayout insertLayout;
	private VerticalLayout insertAnswerLayout;
	private ComponentInsertValue insertValue;

	// Logic
	private Button and, or, not;
	// Order
	private Button leftPar, rightPar;
	// Other
	private Button pilcrow;

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
						showInsertValue();
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

	/**
	 * Shows the insert value component
	 */
	protected void showInsertValue() {
		insertLayout.removeAllComponents();
		insertValue.setCurrentQuestion(getCurrentQuestion());
		insertLayout.addComponent(insertValue);
		insertLayout.setComponentAlignment(insertValue, Alignment.BOTTOM_CENTER);
	}

	/**
	 * Shows the insert answer component
	 */
	protected void showInsertAnswerLayout() {
		insertLayout.removeAllComponents();
		insertLayout.addComponent(insertAnswerLayout);
	}

	private Question getCurrentQuestion() {
		return (Question) treeObjectTable.getValue();
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
			answerTable.setValue(null);
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
		answerTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -6634125293095182977L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() != null && event.getProperty().getValue() instanceof Answer) {
					boolean isFinalAnswer = ((Answer) event.getProperty().getValue()).getChildren().isEmpty();
					insertEqAnswer.setEnabled(isFinalAnswer);
					insertNeAnswer.setEnabled(isFinalAnswer);
				}
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

	private Component generateReferenceTab() {
		VerticalSplitPanel root = new VerticalSplitPanel();
		root.setWidth(FULL);
		root.setHeight(FULL);

		Component insertReference = generateTreeTableSearch();
		// Generate comparation answer and value layout.
		generateInsertComparationAnswerLayout();
		insertValue = new ComponentInsertValue();

		// Layout where we will change elements. Is needed to avoid resizing
		// problems in component
		insertLayout = new VerticalLayout();
		insertLayout.setSizeFull();
		insertLayout.setMargin(true);
		insertLayout.addStyleName(REDUCED_LAYOUT_MARGIN);

		VerticalLayout insertReferenceLayout = new VerticalLayout();
		insertReferenceLayout.setSizeFull();
		insertReferenceLayout.setMargin(true);
		insertReferenceLayout.addComponent(insertReference);
		insertReferenceLayout.addStyleName(REDUCED_LAYOUT_MARGIN);

		root.setFirstComponent(insertReferenceLayout);
		root.setSecondComponent(insertLayout);
		root.setSplitPosition(EXPAND_RATIO_REFERENCE);

		return root;
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

	private Component generateOperationButtons() {
		and = createTokenButton("AND", TokenTypes.AND);
		or = createTokenButton("OR", TokenTypes.OR);
		not = createTokenButton("NOT", TokenTypes.NOT);
		// Order
		leftPar = createTokenButton("(", TokenTypes.LEFT_PAR);
		rightPar = createTokenButton(")", TokenTypes.RIGHT_PAR);
		// Oher
		pilcrow = createTokenButton("\u00B6", TokenTypes.RETURN);

		GridLayout buttonHolder = new GridLayout(NUM_BUTTON_COLUMNS, NUM_BUTTON_ROWS, and, or, not, leftPar, rightPar,
				pilcrow);
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
		insertValue.addInsertTokenListener(listener);
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

	public void addFilter(Filter filter) {
		((Filterable) treeObjectTable.getContainerDataSource()).addContainerFilter(filter);
	}

	public void removeFilter(Filter filter) {
		((Filterable) treeObjectTable.getContainerDataSource()).removeContainerFilter(filter);
	}

	public TextField getValueField() {
		return insertValue.getValueField();
	}
}
