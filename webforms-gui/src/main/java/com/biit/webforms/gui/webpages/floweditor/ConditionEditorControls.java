package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.List;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.TreeObjectTable;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.theme.ThemeIcons;
import com.biit.webforms.utils.lexer.TokenTypes;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;
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

	private TreeObjectTable treeObjectTable;
	private IconButton insertReference;

	// Logic
	private Button and, or, not;
	// Comparation
	private Button lessThan, lessEqual, greaterThan, greaterEqual, equals, notEquals;
	// Order
	private Button leftPar, rightPar;
	// Oher
	private Button carry;

	private List<InsertTokenListener> insertTokenListeners;

	public ConditionEditorControls() {
		super();
		insertTokenListeners = new ArrayList<>();
		generateComposition();
		initializeComposition();
	}

	private void generateComposition() {
		addTab(generateReferenceTab(), "", ThemeIcons.CONDITION_HELPER_REFERENCE.getThemeResource());
		addTab(generateControlsTab(), "", ThemeIcons.CONDITION_HELPER_CONTROLS.getThemeResource());
	}

	private void initializeComposition() {
		treeObjectTable.loadTreeObject(UserSessionHandler.getController().getFormInUse(), null);
	}

	private Component generateReferenceTab() {
		VerticalLayout root = new VerticalLayout();
		root.setWidth(FULL);
		root.setHeight(FULL);
		root.setSpacing(true);

		treeObjectTable = new TreeObjectTable();
		treeObjectTable.setSizeFull();
		treeObjectTable.setSelectable(true);
		// Insert reference button is only enabled if the selection in tree
		// object table is not null and not a form
		treeObjectTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1366837844085658172L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (treeObjectTable.getValue() == null || treeObjectTable.getValue() instanceof Form) {
					insertReference.setEnabled(false);
				} else {
					insertReference.setEnabled(true);
				}
			}
		});

		insertReference = new IconButton(LanguageCodes.CAPTION_INSERT_TREE_OBJECT_REFENCE, ThemeIcons.INSERT_REFERENCE,
				LanguageCodes.TOOLTIP_INSERT_TREE_OBJECT_REFENCE);
		insertReference.setWidth(FULL);

		root.addComponent(treeObjectTable);
		root.addComponent(insertReference);

		root.setExpandRatio(treeObjectTable, 1.0f);

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

		buttonHolder.setWidth(FULL);
		buttonHolder.setHeight(EXPAND);

		root.addComponent(buttonHolder);
		root.setComponentAlignment(buttonHolder, Alignment.MIDDLE_CENTER);

		return root;
	}

	public void addInsertTokenListener(InsertTokenListener listener) {
		insertTokenListeners.add(listener);
	}

	protected void fireInsertTokenListeners(TokenTypes type) {
		for (InsertTokenListener listener : insertTokenListeners) {
			listener.insert(type);
		}
	}

	protected void fireInsertReferenceListeners() {
		for (InsertTokenListener listener : insertTokenListeners) {
			listener.insert(getCurrentTreeObjectReference());
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
}
