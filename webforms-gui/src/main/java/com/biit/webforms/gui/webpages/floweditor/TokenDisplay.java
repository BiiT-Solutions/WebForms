package com.biit.webforms.gui.webpages.floweditor;

import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.webpages.floweditor.listeners.TokenDoubleClickListener;
import com.biit.webforms.gui.webpages.floweditor.listeners.TokenSingleClickListener;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.condition.Token;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TokenDisplay extends CustomComponent {
	private static final long serialVersionUID = 5091855550351857003L;
	private static final String CLASSNAME = "token-display";
	private static final String FULL = "100%";
	private HorizontalLayout evaluatorLayout;

	private VerticalLayout rootLayout;

	private TokenComponent focusedComponent;

	private TokenSingleClickListener autoselectListener;
	private List<TokenDoubleClickListener> tokenDoubleClickListeners;
	private ShortcutListener deleteShotcut;
	private ShortcutListener enterShotcut;
	private ShortcutListener nextShortcut;
	private ShortcutListener previousShortcut;

	private List<ValidationListener> validationListeners = new ArrayList<>();

	interface ValidationListener {
		void validationMessage(String message);
	}

	public TokenDisplay() {
		super();
		setStyleName(CLASSNAME);
		setSizeFull();

		rootLayout = new VerticalLayout();
		tokenDoubleClickListeners = new ArrayList<>();

		defineShortcuts();
		enableShortcuts();
		setCompositionRoot(generateComposition());
		initializeComposition();
	}

	private Component generateComposition() {
		rootLayout.setWidth(FULL);
		rootLayout.setHeight(null);
		rootLayout.setSpacing(true);
		rootLayout.setMargin(true);

		addLine();

		return rootLayout;
	}

	private void initializeComposition() {
		autoselectListener = new TokenSingleClickListener() {

			@Override
			public void singleClick(TokenComponent tokenComponent) {
				setFocused(tokenComponent);
			}
		};
	}

	public void addToken(Token token) {
		TokenComponent tokenComponent = new TokenComponent(token);
		tokenComponent.addTokenSingleClick(autoselectListener);
		for (TokenDoubleClickListener listener : tokenDoubleClickListeners) {
			tokenComponent.addTokenDoubleClick(listener);
		}
		addToken(tokenComponent);
		updateEvaluator();
	}

	private void addToken(TokenComponent token) {
		boolean added = addToken(token, null);
		if (token.getToken().getType() == TokenTypes.RETURN && added) {
			addLine();
		}
	}

	/**
	 * Adds a TokenComponent to the end of a specific line or after the current
	 * selected element or adds the element as the last selected element.
	 * 
	 * @param token
	 * @param line
	 */
	private boolean addToken(TokenComponent token, LineDisplay line) {
		// If a line is defined we add it to the end of the line
		if (line != null) {
			line.addToken(token);
			return true;
		} else {
			// If not we add after the focused component or the last line.
			if (focusedComponent != null) {
				if (focusedComponent.getToken().getType() == TokenTypes.RETURN) {
					// Avoid two consecutive pilcrum.
					if (token.getToken().getType() != TokenTypes.RETURN) {
						getNextLine(focusedComponent).addToken(token, 0);
						setFocused(token);
						return true;
					}
				} else {
					if (focusedComponent.getLine().addTokenAfter(token, focusedComponent)) {
						setFocused(token);
						return true;
					}
				}
			} else {
				addToken(token, getCurrentLine());
				setFocused(token);
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets currently selected line for work. If a component is selected returns
	 * its line, else returns the last line.
	 * 
	 * @return
	 */
	private LineDisplay getCurrentLine() {
		if (rootLayout.getComponentCount() == 0) {
			return null;
		}
		if (focusedComponent != null) {
			return focusedComponent.getLine();
		} else {
			return (LineDisplay) rootLayout.getComponent(rootLayout.getComponentCount() - 1);
		}
	}

	private void setFocused(TokenComponent token) {
		if (focusedComponent != null) {
			focusedComponent.loseFocus();
		}
		focusedComponent = token;
		if (focusedComponent != null) {
			focusedComponent.focus();
		}
		updateExpressionSelectionStyles();
	}

	/**
	 * The selected expression is white.
	 */
	protected void updateExpressionSelectionStyles() {
		for (int i = 0; i < rootLayout.getComponentCount(); i++) {
			if (rootLayout.getComponent(i) instanceof HorizontalLayout) {
				HorizontalLayout lineLayout = (HorizontalLayout) rootLayout.getComponent(i);
				for (int j = 0; j < lineLayout.getComponentCount(); j++) {
					if (lineLayout.getComponent(j) instanceof TokenComponent) {
						if (lineLayout.getComponent(j).equals(focusedComponent)) {
							((TokenComponent) lineLayout.getComponent(j)).focus();
						} else {
							((TokenComponent) lineLayout.getComponent(j)).loseFocus();
						}
					}
				}
			}
		}
	}

	public void next() {
		TokenComponent next = getElementAhead(1);
		setFocused(next);
	}

	public void previous() {
		TokenComponent previous = getElementAhead(-1);
		setFocused(previous);
	}

	public void remove() {
		if (focusedComponent != null) {
			TokenComponent temp = focusedComponent;
			previous();
			if (focusedComponent == temp) {
				focusedComponent = null;
			}
			remove(temp);
		}
	}

	private void remove(TokenComponent token) {
		if (token.getToken().getType() == TokenTypes.RETURN) {
			LineDisplay lineToRemove = getNextLine(token);
			if (lineToRemove != null) {
				remove(lineToRemove);
			}
		}
		token.getLine().removeToken(token);
		updateEvaluator();
	}

	private LineDisplay getNextLine(TokenComponent token) {
		int nextLineIndex = rootLayout.getComponentIndex(token.getLine()) + 1;
		try {
			return (LineDisplay) rootLayout.getComponent(nextLineIndex);
		} catch (IndexOutOfBoundsException iob) {
			return null;
		}
	}

	/**
	 * Removes all elements in line, then inserts in previous line.
	 * 
	 * @param line
	 */
	private void remove(LineDisplay line) {
		List<TokenComponent> tokens = line.getTokens();

		int previousLineIndex = rootLayout.getComponentIndex(line) - 1;
		// First line can't be removed so we can assume we will always have one
		// line.
		LineDisplay previousLine = (LineDisplay) rootLayout.getComponent(previousLineIndex);
		previousLine.addTokens(tokens);

		rootLayout.removeComponent(line);
	}

	/**
	 * Get element in current position + numPosition if there are no tokens
	 * returns null. If no token is selected it gets the first element or the
	 * last depending on the numPosition defined. Else we get the sum of
	 * currentPosition + numPosition and do the module to get the new position.
	 *
	 */
	public TokenComponent getElementAhead(int numPosition) {
		if (focusedComponent == null) {
			if (numPosition > 0) {
				return getFirstToken();
			} else {
				return getLastToken();
			}
		} else {
			List<TokenComponent> tokens = getTokenComponents();
			int elementIndex = (tokens.size() + (tokens.indexOf(focusedComponent) + numPosition)) % tokens.size();
			return tokens.get(elementIndex);
		}
	}

	private TokenComponent getLastToken() {
		if (rootLayout.getComponentCount() == 0) {
			return null;
		}
		return ((LineDisplay) rootLayout.getComponent(rootLayout.getComponentCount() - 1)).getLastToken();
	}

	private TokenComponent getFirstToken() {
		if (rootLayout.getComponentCount() == 0) {
			return null;
		}
		return ((LineDisplay) rootLayout.getComponent(0)).getFirstToken();
	}

	public int getTokenCount() {
		int count = 0;
		Iterator<Component> itr = rootLayout.iterator();
		while (itr.hasNext()) {
			LineDisplay line = (LineDisplay) itr.next();
			count += line.getTokenCount();
		}
		return count;
	}

	List<Token> getTokens() {
		List<Token> tokens = new ArrayList<>();
		for (TokenComponent tokenComponent : getTokenComponents()) {
			tokens.add(tokenComponent.getToken());
		}
		return tokens;
	}

	List<TokenComponent> getTokenComponents() {
		List<TokenComponent> tokens = new ArrayList<TokenComponent>();
		Iterator<Component> itr = rootLayout.iterator();
		while (itr.hasNext()) {
			Component element = itr.next();
			if (element instanceof LineDisplay) {
				LineDisplay line = (LineDisplay) element;
				tokens.addAll(line.getTokens());
			}
		}
		return tokens;
	}

	private LineDisplay addLine() {
		LineDisplay newLine = new LineDisplay();

		if (getCurrentLine() == null) {
			rootLayout.addComponent(newLine);
			return newLine;
		}

		List<TokenComponent> tokens = getCurrentLine().getTokensAfter(focusedComponent);
		// Remove tokens at the right side of the current selected element. Add
		// them to the new line.
		getCurrentLine().removeTokens(tokens);
		newLine.addTokens(tokens);

		int insertIndex = rootLayout.getComponentIndex(getCurrentLine()) + 1;
		if (insertIndex == rootLayout.getComponentCount()) {
			rootLayout.addComponent(newLine);
		} else {
			rootLayout.addComponent(newLine, insertIndex);
		}
		return newLine;
	}

	public void addTokenDoubleClickListener(TokenDoubleClickListener listener) {
		// Adds to the list for future tokens
		tokenDoubleClickListeners.add(listener);
		// And registres for the already existing tokens.
		for (TokenComponent tokenComponent : getTokenComponents()) {
			tokenComponent.addTokenDoubleClick(listener);
		}
	}

	public void defineShortcuts() {
		deleteShotcut = new ShortcutListener("DELETE_SHORTCUT", KeyCode.DELETE, null) {
			private static final long serialVersionUID = -71562151456777493L;

			@Override
			public void handleAction(Object sender, Object target) {
				remove();
			}
		};

		enterShotcut = new ShortcutListener("ENTER_SHORTCUT", KeyCode.ENTER, null) {
			private static final long serialVersionUID = -6251977494304137847L;

			@Override
			public void handleAction(Object sender, Object target) {
				addToken(Token.getToken(TokenTypes.RETURN));
			}
		};

		nextShortcut = new ShortcutListener("SELECT_NEXT", KeyCode.ARROW_RIGHT, null) {
			private static final long serialVersionUID = 7663105045629599269L;

			@Override
			public void handleAction(Object sender, Object target) {
				next();
			}
		};

		previousShortcut = new ShortcutListener("SELECT_PREVIOUS", KeyCode.ARROW_LEFT, null) {
			private static final long serialVersionUID = 8453120978479798559L;

			@Override
			public void handleAction(Object sender, Object target) {
				previous();
			}
		};
	}

	public void enableShortcuts() {
		addShortcutListener(deleteShotcut);
		addShortcutListener(enterShotcut);
		addShortcutListener(nextShortcut);
		addShortcutListener(previousShortcut);
	}

	public void disableShortcuts() {
		removeShortcutListener(deleteShotcut);
		removeShortcutListener(enterShotcut);
		removeShortcutListener(nextShortcut);
		removeShortcutListener(previousShortcut);
	}

	public void clean() {
		focusedComponent = null;
		rootLayout.removeAllComponents();
		addLine();
	}

	private HorizontalLayout createEvaluatorLayout() {
		HorizontalLayout checkerLayout = new HorizontalLayout();
		checkerLayout.setMargin(false);
		checkerLayout.setSpacing(false);
		checkerLayout.setSizeFull();

		Label evaluatorOutput = new Label();
		evaluatorOutput.setSizeUndefined();
		checkerLayout.addComponent(evaluatorOutput);
		checkerLayout.setComponentAlignment(evaluatorOutput, Alignment.TOP_RIGHT);

		try {
			WebformsParser parser = new WebformsParser(getTokens().iterator());
			parser.parseCompleteExpression();
			evaluatorOutput.setStyleName("expression-valid");
			evaluatorOutput.setValue(LanguageCodes.EXPRESSION_CHECKER_VALID.translation());
			fireValidationListeners("");
		} catch (Exception e) {
			fireValidationListeners(e.getMessage());
			WebformsUiLogger.debug(TokenDisplay.class.getName(), e.getMessage());
			evaluatorOutput.setStyleName("expression-invalid");
			evaluatorOutput.setValue(LanguageCodes.EXPRESSION_CHECKER_INVALID.translation());
		}

		return checkerLayout;
	}

	private void updateEvaluator() {
		if (rootLayout != null) {
			if (evaluatorLayout != null) {
				rootLayout.removeComponent(evaluatorLayout);
			}
			evaluatorLayout = createEvaluatorLayout();
			rootLayout.addComponent(evaluatorLayout, 0);
			// If expand ratio is 0, component is not shown.
			rootLayout.setExpandRatio(evaluatorLayout, 0.00001f);
			rootLayout.setComponentAlignment(evaluatorLayout, Alignment.BOTTOM_RIGHT);
		}
	}

	public void addValidationListener(ValidationListener listener) {
		validationListeners.add(listener);
	}

	public void fireValidationListeners(String message) {
		for (ValidationListener validationListener : validationListeners) {
			validationListener.validationMessage(message);
		}
	}

}
