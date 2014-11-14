package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.floweditor.listeners.InsertTokenListener;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * This class holds all the representation of the form to introduce a token
 * value. Previously this was part of Condition Editor controls.
 */
public class ComponentInsertValue extends CustomComponent {
	private static final long serialVersionUID = -5236675580401960488L;
	private static final String FULL = "100%";
	private static final String EXPAND = null;

	private static final int VALUE_BUTTON_COLS = 3;
	private static final int VALUE_BUTTON_ROWS = 2;
	private static final DatePeriodUnit DATE_FORMAT_DEFAULT_DATE_PERIOD = DatePeriodUnit.YEAR;

	private Question currentQuestion;

	private VerticalLayout insertValueLayout;

	private ComboBox dateFormatType;
	private TextField value;
	private Button insertEqValue, insertNeValue, insertLtValue, insertGtValue, insertLeValue, insertGeValue;

	private List<InsertTokenListener> insertTokenListeners;

	public ComponentInsertValue() {
		super();
		insertTokenListeners = new ArrayList<InsertTokenListener>();
		setCompositionRoot(generate());
	}

	private Component generate() {
		insertValueLayout = new VerticalLayout();
		insertValueLayout.setSpacing(true);
		insertValueLayout.setWidth(FULL);
		insertValueLayout.setHeight(EXPAND);
		insertValueLayout.setSpacing(true);

		dateFormatType = new ComboBox();
		dateFormatType.setWidth(FULL);
		dateFormatType.setTextInputAllowed(false);
		// Test
		dateFormatType.setNullSelectionAllowed(true);
		dateFormatType.addItem("null");
		dateFormatType.setItemCaption("null", LanguageCodes.CAPTION_DATE_PERIOD_NULL.translation());
		dateFormatType.setNullSelectionItemId("null");
		//
		for (DatePeriodUnitUi dateType : DatePeriodUnitUi.values()) {
			dateFormatType.addItem(dateType.getDatePeriodUnit());
			dateFormatType.setItemCaption(dateType.getDatePeriodUnit(), dateType.getTranslation());
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

	/**
	 * Updates insert value hint, prompt and validator.
	 */
	public void setCurrentQuestion(Question question) {
		this.currentQuestion = question;

		value.removeAllValidators();
		value.setValue("");

		insertValueLayout.removeComponent(dateFormatType);
		if (question.getAnswerFormat() == AnswerFormat.DATE) {
			insertValueLayout.addComponentAsFirst(dateFormatType);
			if (question.getAnswerSubformat() == AnswerSubformat.DATE_PERIOD) {
				dateFormatType.setNullSelectionAllowed(false);
				dateFormatType.setValue(DATE_FORMAT_DEFAULT_DATE_PERIOD);
			} else {
				dateFormatType.setNullSelectionAllowed(true);
				dateFormatType.setValue(null);
			}
			updateDateValidatorAndInputPrompt();
		} else {
			dateFormatType.setNullSelectionAllowed(true);
			dateFormatType.setValue(null);
			value.setInputPrompt(question.getAnswerSubformat().getHint());
			value.addValidator(new ValidatorPattern(question.getAnswerSubformat().getRegex()));
		}

		insertEqValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.EQ));
		insertNeValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.NE));
		insertLeValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.LE));
		insertGeValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.GE));
		insertLtValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.LT));
		insertGtValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.GT));
	}

	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	private AnswerSubformat getCurrentValueAnswerSubformat() {
		if (currentQuestion.getAnswerFormat() == AnswerFormat.DATE && getDatePeriodUnit() != null) {
			return AnswerSubformat.DATE_PERIOD;
		} else {
			return currentQuestion.getAnswerSubformat();
		}
	}

	private DatePeriodUnit getDatePeriodUnit() {
		if (dateFormatType.getValue() == null) {
			return null;
		} else {
			return (DatePeriodUnit) dateFormatType.getValue();
		}
	}

	private String getValue() {
		if (!value.isValid()) {
			return null;
		} else {
			return value.getValue();
		}
	}

	private void updateDateValidatorAndInputPrompt() {
		value.removeAllValidators();

		if (currentQuestion != null && currentQuestion.getAnswerSubformat() != null) {
			if (dateFormatType.getValue() == null) {
				value.setInputPrompt(currentQuestion.getAnswerSubformat().getHint());
				value.addValidator(new ValidatorPattern(currentQuestion.getAnswerSubformat().getRegex()));
			} else {
				value.setInputPrompt(AnswerSubformat.DATE_PERIOD.getHint());
				value.addValidator(new ValidatorPattern(AnswerSubformat.DATE_PERIOD.getRegex()));
			}
		}
	}

	private Button createTokenComparationValueButton(String caption, final TokenTypes type) {
		Button button = new Button(caption);
		button.setWidth(FULL);
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 150904421483217498L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (value.isValid()) {
					Token token = UserSessionHandler.getController().createTokenComparationValue(type,
							getCurrentQuestion(), getCurrentValueAnswerSubformat(), getDatePeriodUnit(), getValue());
					fireInsertTokenListeners(token);
				} else {
					MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_VALUE_HAS_WRONG_FORMAT);
				}
			}

		});
		return button;
	}

	public boolean isValid() {
		return value.isValid();
	}

	public void addInsertTokenListener(InsertTokenListener listener) {
		insertTokenListeners.add(listener);
	}

	protected void fireInsertTokenListeners(Token token) {
		for (InsertTokenListener listener : insertTokenListeners) {
			listener.insert(token);
		}
	}

	public TextField getValueField() {
		return value;
	}
}
