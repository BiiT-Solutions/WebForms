package com.biit.webforms.gui.webpages.floweditor;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.StatusLabel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.webpages.floweditor.TokenDisplay.ValidationListener;
import com.biit.webforms.gui.webpages.floweditor.listeners.InsertTokenListener;
import com.biit.webforms.gui.webpages.floweditor.listeners.TokenDoubleClickListener;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.condition.*;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.utils.parser.exceptions.*;
import com.vaadin.data.Container.Filter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.*;

import java.util.List;

/**
 * token conditions. Contains the right side controls and the current token
 * condition display.
 * 
 */
public class ConditionEditor extends CustomComponent {
	private static final long serialVersionUID = 8836808232493250676L;

	private ConditionEditorControls controls;

	private TokenDisplay tokenDisplay;
	private StatusLabel statusLabel;

	public ConditionEditor() {
		super();
		setSizeFull();
		setCompositionRoot(generate());
	}

	private Component generate() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setSpacing(true);

		VerticalLayout checkAndTokens = generateCheckAndTokens();
		controls = generateControls();
		// Text field needs to remove the shortcuts of the tokenDisplay.
		if (controls.getValueField() instanceof TextField) {
			((TextField) controls.getValueField()).addFocusListener(new FocusListener() {
				private static final long serialVersionUID = -5045926489824179125L;

				@Override
				public void focus(FocusEvent event) {
					tokenDisplay.disableShortcuts();
				}
			});
		}
		if (controls.getValueField() instanceof TextField) {
			((TextField) controls.getValueField()).addBlurListener(new BlurListener() {
				private static final long serialVersionUID = 619744746840638011L;

				@Override
				public void blur(BlurEvent event) {
					tokenDisplay.enableShortcuts();
				}
			});
		}

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
				if (token instanceof TokenIn) {
					openEditIn(tokenComponent);
				}
				if (token instanceof TokenBetween) {
					openEditBetween(tokenComponent);
				}
			}
		});

		statusLabel = new StatusLabel();
		tokenDisplay.addValidationListener(new ValidationListener() {

			@Override
			public void validationMessage(String message) {
				// if (message.length() > 0) {
				// statusLabel.setErrorText(message);
				// } else {
				// statusLabel.setValue(message);
				// }
			}
		});

		checkAndTokens.addComponent(tokenDisplay);
		checkAndTokens.setExpandRatio(tokenDisplay, 1.0f);
		checkAndTokens.addComponent(statusLabel);

		return checkAndTokens;
	}

	protected void openEditBetween(final TokenComponent tokenComponent) {
		WindowTokenBetween window = new WindowTokenBetween();
		window.setToken((TokenBetween) tokenComponent.getToken());
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowTokenBetween windowToken = (WindowTokenBetween) window;
				TokenBetween tokenBetween = (TokenBetween) tokenComponent.getToken();
				tokenBetween.setContent(windowToken.getDatePeriodUnit(), windowToken.getValueStart(), windowToken.getValueEnd());

				tokenComponent.refresh();
				window.close();
			}
		});
		window.showCentered();
	}

	protected void openEditIn(final TokenComponent tokenComponent) {
		WindowTokenIn window = new WindowTokenIn();
		window.setToken((TokenIn) tokenComponent.getToken());
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowTokenIn thisWindow = (WindowTokenIn) window;
				((TokenIn) tokenComponent.getToken()).setAnswers(thisWindow.getAnswers());
				tokenComponent.refresh();
				window.close();
			}
		});
		window.showCentered();
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
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
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
					if (thisWindow.getOperator() == null || thisWindow.getAnswer() == null) {
						MessageManager.showError(LanguageCodes.ERROR_INVALID_CONDITION);
					} else {
						((TokenComparationAnswer) tokenComponent.getToken()).setContent(thisWindow.getOperator(), thisWindow.getAnswer());
						tokenComponent.refresh();
					}
				} catch (NotValidTokenType e) {
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				}
				window.close();
			}
		});
		window.showCentered();
	}

	public List<Token> getTokens() {
		return tokenDisplay.getTokens();
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

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		controls.setEnabled(enabled);
	}

	protected boolean isConditionValid() {
		// Translation
		try {
			WebformsParser parser = new WebformsParser(getTokens().iterator());
			parser.parseCompleteExpression();
			return true;
		} catch (ParseException | ExpectedTokenNotFound | NoMoreTokensException | IncompleteBinaryOperatorException
				| MissingParenthesisException | ExpressionNotWellFormedException | EmptyParenthesisException e) {
			return false;
		}
	}
}
