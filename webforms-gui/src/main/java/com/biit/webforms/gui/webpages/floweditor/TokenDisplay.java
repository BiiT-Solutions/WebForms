package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.gui.webpages.floweditor.listeners.TokenDoubleClickListener;
import com.biit.webforms.gui.webpages.floweditor.listeners.TokenSingleClickListener;
import com.biit.webforms.persistence.entity.condition.Token;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class TokenDisplay extends CustomComponent {
	private static final long serialVersionUID = 5091855550351857003L;
	private static final String CLASSNAME = "token-display";
	private static final String FULL = "100%";

	private VerticalLayout rootLayout;

	private TokenComponent focusedComponent;

	private TokenSingleClickListener autoselectListener;
	private List<TokenDoubleClickListener> tokenDoubleClickListeners;
	private ShortcutListener deleteShotcut;
	private ShortcutListener enterShotcut;
	private ShortcutListener nextShortcut;
	private ShortcutListener previousShortcut;

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
	}

	private void addToken(TokenComponent token) {
		if (token.getToken().getType() == TokenTypes.RETURN) {
			LineDisplay newLine = addLine();
			newLine.addToken(token);
			setFocused(token);
		} else {
			addToken(token, null);
		}
	}

	/**
	 * Adds a TokenComponent to the end of a specific line or after the current
	 * selected element or adds the element as the last selected element.
	 * 
	 * @param token
	 * @param line
	 */
	private void addToken(TokenComponent token, LineDisplay line) {
		// If a line is defined we add it to the end of the line
		if (line != null) {
			line.addToken(token);
		} else {
			// If not we add after the focused component or the last line.
			if (focusedComponent != null) {
				focusedComponent.getLine().addTokenAfter(token, focusedComponent);
			} else {
				addToken(token, getCurrentLine());
			}
		}
		setFocused(token);
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
			remove(token.getLine());
		} else {
			token.getLine().removeToken(token);
		}
	}

	/**
	 * Removes all elements in line, then inserts in previous line.
	 * 
	 * @param line
	 */
	private void remove(LineDisplay line) {
		List<TokenComponent> tokens = line.getTokens();
		// Return token should always be the first element
		tokens.remove(0);

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
	 * @param position
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
		if(rootLayout.getComponentCount()==0){
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
	
	List<TokenComponent> getTokenComponents(){
		List<TokenComponent> tokens = new ArrayList<TokenComponent>();
		Iterator<Component> itr = rootLayout.iterator();
		while(itr.hasNext()){
			LineDisplay line = (LineDisplay) itr.next();
			tokens.addAll(line.getTokens());
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
		//Remove tokens at the right side of the current selected element. Add them to the new line.
		getCurrentLine().removeTokens(tokens);
		newLine.addTokens(tokens);
		
		int insertIndex = rootLayout.getComponentIndex(getCurrentLine())+1;
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
}
