package com.biit.webforms.gui.webpages.floweditor;

import java.util.List;

import com.biit.form.TreeObject;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.gui.common.components.StatusLabel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.webpages.floweditor.listeners.InsertTokenListener;
import com.biit.webforms.gui.webpages.floweditor.listeners.TokenDoubleClickListener;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.ExpressionNotWellFormedException;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.vaadin.data.Container.Filter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class ConditionEditor extends CustomComponent {
	private static final long serialVersionUID = 8836808232493250676L;

	private static final String CONDITION_VALIDATOR = "condition-validator";

	private static final String VALIDATE_BUTTON_HEIGHT = "26px";

	private ConditionEditorControls controls;
	private Button validate;
	private StatusLabel status;
	private TokenDisplay tokenDisplay;

	public ConditionEditor() {
		super();
		setSizeFull();
		setCompositionRoot(generate());
		initialize();
	}

	private Component generate() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setSpacing(true);

		VerticalLayout checkAndTokens = generateCheckAndTokens();
		controls = generateControls();
		// Text field needs to remove the shortcuts of the tokenDisplay.
		controls.getValueField().addFocusListener(new FocusListener() {
			private static final long serialVersionUID = -5045926489824179125L;

			@Override
			public void focus(FocusEvent event) {
				tokenDisplay.disableShortcuts();
			}
		});
		controls.getValueField().addBlurListener(new BlurListener() {
			private static final long serialVersionUID = 619744746840638011L;

			@Override
			public void blur(BlurEvent event) {
				tokenDisplay.enableShortcuts();
			}
		});

		horizontalLayout.addComponent(checkAndTokens);
		horizontalLayout.addComponent(controls);

		horizontalLayout.setExpandRatio(checkAndTokens, 0.70f);
		horizontalLayout.setExpandRatio(controls, 0.30f);

		return horizontalLayout;
	}

	private ConditionEditorControls generateControls() {
		ConditionEditorControls controls = new ConditionEditorControls();
		controls.setSizeFull();
		controls.addInsertTokenListener(new InsertTokenListener() {

			@Override
			public void insert(Token token) {
				tokenDisplay.addToken(token);
			}
		});
		return controls;
	}

	private VerticalLayout generateCheckAndTokens() {
		VerticalLayout checkAndTokens = new VerticalLayout();
		checkAndTokens.setSizeFull();
		checkAndTokens.addComponent(generateValidator());

		tokenDisplay = new TokenDisplay();
		tokenDisplay.setSizeFull();
		tokenDisplay.addTokenDoubleClickListener(new TokenDoubleClickListener() {

			@Override
			public void doubleClick(TokenComponent tokenComponent) {
				Token token = tokenComponent.getToken();
				if (token instanceof TokenComparationAnswer) {
					openEditComparationOperatorAnswer(tokenComponent);
				}
				if (token instanceof TokenComparationValue) {
					openEditComparationOperatorValue(tokenComponent);
				}
			}
		});

		checkAndTokens.addComponent(tokenDisplay);
		checkAndTokens.setExpandRatio(tokenDisplay, 1.0f);

		return checkAndTokens;
	}

	protected void openEditComparationOperatorValue(final TokenComponent tokenComponent) {
		WindowTokenOperationValue window = new WindowTokenOperationValue();
		window.setToken((TokenComparationValue) tokenComponent.getToken());
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				try {
					WindowTokenOperationValue thisWindow = (WindowTokenOperationValue) window;
					((TokenComparationValue) tokenComponent.getToken()).setContent(thisWindow.getOperator(),
							thisWindow.getAnswerSubformat(), thisWindow.getDatePeriod(), thisWindow.getValue());
					tokenComponent.refresh();
				} catch (NotValidTokenType e) {
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
				window.close();
			}
		});
		window.showCentered();
	}

	protected void openEditComparationOperatorAnswer(final TokenComponent tokenComponent) {
		WindowTokenOperationAnswer window = new WindowTokenOperationAnswer();
		window.setToken((TokenComparationAnswer) tokenComponent.getToken());
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				try {
					WindowTokenOperationAnswer thisWindow = (WindowTokenOperationAnswer) window;
					((TokenComparationAnswer) tokenComponent.getToken()).setContent(thisWindow.getOperator(),
							thisWindow.getAnswer());
					tokenComponent.refresh();
				} catch (NotValidTokenType e) {
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
				window.close();
			}
		});
		window.showCentered();
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
				isConditionValid();
			}
		});
		status = new StatusLabel("");

		validator.addComponent(validate);
		validator.addComponent(status);
		return validator;
	}

	protected boolean isConditionValid() {
		// Translation
		try {
			WebformsParser parser = new WebformsParser(getTokens().iterator());
			parser.parseCompleteExpression();
			status.setOkText("Condition is valid");
			return true;
		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
				| MissingParenthesisException | ExpressionNotWellFormedException | EmptyParenthesisException e) {
			status.setErrorText(e.getMessage());
			return false;
		}
	}

	public List<Token> getTokens() {
		return tokenDisplay.getTokens();
	}

	/**
	 * Initialize composition default values
	 */
	private void initialize() {
		// TODO
		// clean();
	}

	public void delete() {
		tokenDisplay.remove();
	}

	public void next() {
		tokenDisplay.next();
	}

	public void previous() {
		tokenDisplay.previous();
	}

	public void addToken(Token token) {
		tokenDisplay.addToken(token);
	}

	public void selectReferenceElement(TreeObject treeObject) {
		controls.selectTreeObject(treeObject);
	}

	public void clean() {
		tokenDisplay.clean();
	}

	public void removeFilter(Filter filter) {
		controls.removeFilter(filter);
	}

	public void addFilter(Filter filter) {
		controls.addFilter(filter);
	}
}
