package com.biit.webforms.gui.webpages.floweditor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.floweditor.listeners.InsertTokenListener;
import com.biit.webforms.language.AnswerSubformatUi;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * This class holds all the representation of the form to introduce a token value. Previously this was part of Condition
 * Editor controls.
 */
public class ComponentInsertValue extends CustomComponent {
	private static final long serialVersionUID = -5236675580401960488L;
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String FULL = "100%";
	private static final String EXPAND = null;

	private static final int VALUE_BUTTON_COLS = 4;
	private static final int VALUE_BUTTON_ROWS = 2;
	private static final DatePeriodUnit DATE_FORMAT_DEFAULT_DATE_PERIOD = DatePeriodUnit.YEAR;

	private Question currentQuestion;

	private VerticalLayout insertValueLayout;

	private ComboBox datePeriodUnit;
	private AbstractField<?> value;
	private Button insertEqValue, insertNeValue, insertLtValue, insertGtValue, insertLeValue, insertGeValue,
			insertBetweenButton;

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

		datePeriodUnit = new ComboBox();
		datePeriodUnit.setWidth(FULL);
		datePeriodUnit.setTextInputAllowed(false);
		// Test
		datePeriodUnit.setNullSelectionAllowed(true);
		datePeriodUnit.addItem("null");
		datePeriodUnit.setItemCaption("null", LanguageCodes.CAPTION_DATE_PERIOD_NULL.translation());
		datePeriodUnit.setNullSelectionItemId("null");
		//
		for (DatePeriodUnitUi dateType : DatePeriodUnitUi.values()) {
			datePeriodUnit.addItem(dateType.getDatePeriodUnit());
			datePeriodUnit.setItemCaption(dateType.getDatePeriodUnit(), dateType.getTranslation());
		}
		datePeriodUnit.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -333682134124174959L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				insertValueLayout.removeComponent(datePeriodUnit);
				updateValueComponent();
				insertValueLayout.addComponentAsFirst(datePeriodUnit);
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
		insertBetweenButton = createBetweenButton();

		GridLayout buttonLayout = new GridLayout(VALUE_BUTTON_COLS, VALUE_BUTTON_ROWS, insertEqValue, insertLeValue,
				insertLtValue, insertBetweenButton, insertNeValue, insertGeValue, insertGtValue);
		buttonLayout.setWidth(FULL);

		insertValueLayout.addComponent(datePeriodUnit);
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

		insertValueLayout.removeComponent(datePeriodUnit);
		updateValueComponent();
		if (question.getAnswerFormat() == AnswerFormat.DATE) {
			insertValueLayout.addComponentAsFirst(datePeriodUnit);
			if (question.getAnswerSubformat() == AnswerSubformat.DATE_PERIOD) {
				datePeriodUnit.setNullSelectionAllowed(false);
				datePeriodUnit.setValue(DATE_FORMAT_DEFAULT_DATE_PERIOD);
			} else {
				datePeriodUnit.setNullSelectionAllowed(true);
				datePeriodUnit.setValue(null);
			}
			updateDateValidatorAndInputPrompt();
		} else {
			datePeriodUnit.setNullSelectionAllowed(true);
			datePeriodUnit.setValue(null);
			((TextField) value).setInputPrompt(AnswerSubformatUi.get(question.getAnswerSubformat()).getInputPrompt());
			value.addValidator(new ValidatorPattern(question.getAnswerSubformat().getRegex()));
		}

		insertEqValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.EQ));
		insertNeValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.NE));
		insertLeValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.LE));
		insertGeValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.GE));
		insertLtValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.LT));
		insertGtValue.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.GT));
		insertBetweenButton.setEnabled(question.getAnswerFormat().isValidTokenType(TokenTypes.BETWEEN));
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
		if (datePeriodUnit.getValue() == null) {
			return null;
		} else {
			return (DatePeriodUnit) datePeriodUnit.getValue();
		}
	}

	private String getValue() {
		if (!value.isValid() || value.getValue() == null) {
			return null;
		} else {
			if (value instanceof DateField) {
				return new SimpleDateFormat(DATE_FORMAT).format(((Date) value.getValue()));
			} else {
				return value.getValue().toString();
			}
		}
	}

	private void updateDateValidatorAndInputPrompt() {
		value.removeAllValidators();
		if (currentQuestion != null && currentQuestion.getAnswerSubformat() != null) {
			if (datePeriodUnit.getValue() != null) {
				((TextField) value).setInputPrompt(AnswerSubformat.DATE_PERIOD.getHint());
				value.addValidator(new ValidatorPattern(AnswerSubformat.DATE_PERIOD.getRegex()));
			} else {
				value.addValidator(new NullValidator(LanguageCodes.VALIDATION_NULL_VALUE.translation(), false));
			}
		}
	}

	private void updateValueComponent() {
		if (value != null) {
			insertValueLayout.removeComponent(value);
		}

		if (getCurrentQuestion().getAnswerFormat().equals(AnswerFormat.DATE)
				&& (getCurrentValueAnswerSubformat().equals(AnswerSubformat.DATE)
						|| getCurrentValueAnswerSubformat().equals(AnswerSubformat.DATE_PAST)
						|| getCurrentValueAnswerSubformat().equals(AnswerSubformat.DATE_FUTURE) || getCurrentValueAnswerSubformat()
						.equals(AnswerSubformat.DATE_BIRTHDAY))) {
			value = new DateField();
		} else {
			value = new TextField();
		}
		value.setImmediate(true);
		value.setWidth(FULL);
		if (value instanceof TextField) {
			((TextField) value).setNullRepresentation("");
		}

		insertValueLayout.addComponentAsFirst(value);
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
					MessageManager.showError(LanguageCodes.ERROR_MESSAGE_VALUE_HAS_WRONG_FORMAT);
				}
			}

		});
		return button;
	}

	private Button createBetweenButton() {
		Button button = new Button(TokenTypes.BETWEEN.toString());
		button.setWidth(FULL);
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 8499457213878436486L;

			@Override
			public void buttonClick(ClickEvent event) {
				openWindowTokenBetween();
			}
		});
		return button;
	}

	protected void openWindowTokenBetween() {
		WindowTokenBetween window = new WindowTokenBetween();
		window.setQuestion(currentQuestion, (DatePeriodUnit) datePeriodUnit.getValue(), value.getValue().toString());
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowTokenBetween between = (WindowTokenBetween) window;
				if (((WindowTokenBetween) window).isDataValid()) {
					fireInsertTokenListeners(TokenBetween.getBetween(between.getQuestion(),
							between.getDatePeriodUnit(), between.getValueStart(), between.getValueEnd()));
					window.close();
				} else {
					MessageManager.showError(LanguageCodes.ERROR_MESSAGE_FIELDS_ARE_NOT_FILLED_CORRECTLY);
				}
			}
		});
		window.showCentered();
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

	public AbstractField<?> getValueField() {
		return value;
	}
}
