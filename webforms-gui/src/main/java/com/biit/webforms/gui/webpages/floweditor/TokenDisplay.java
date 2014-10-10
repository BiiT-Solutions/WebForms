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
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class TokenDisplay extends CustomComponent {
	private static final long serialVersionUID = 5091855550351857003L;
	private static final String CLASSNAME = "token-display";
	private static final String LINE_HEIGHT = "32px";
	private static final String FULL = "100%";

	private List<TokenComponent> tokenComponents;
	private TokenComponent focusedComponent;
	private VerticalLayout rootLayout;
	private TokenSingleClickListener autoselectListener;
	private List<TokenDoubleClickListener> tokenDoubleClickListeners;
	private ShortcutListener deleteShotcut;
	private ShortcutListener enterShotcut;
	private ShortcutListener nextShortcut;
	private ShortcutListener previousShortcut;

	public TokenDisplay() {
		super();
		tokenComponents = new ArrayList<>();
		tokenDoubleClickListeners = new ArrayList<>();
		setStyleName(CLASSNAME);
		setSizeFull();
		defineShortcuts();
		enableShortcuts();
		setCompositionRoot(generateComposition());
		initializeComposition();
	}

	private Component generateComposition() {
		rootLayout = new VerticalLayout();
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

	public void addToken(Token newToken) {
		addTokenUi(newToken);
	}

	private void addTokenUi(Token token) {
		TokenComponent tokenUi = new TokenComponent(token);
		tokenUi.addTokenSingleClick(autoselectListener);
		for (TokenDoubleClickListener listener : tokenDoubleClickListeners) {
			tokenUi.addTokenDoubleClick(listener);
		}

		if (token.getType() == TokenTypes.RETURN) {
			HorizontalLayout newLine = addLine();
			addTokenUi(tokenUi, newLine);
		} else {
			addTokenUi(tokenUi,null);
		}
	}

	private void addTokenUi(TokenComponent tokenUi, AbstractOrderedLayout line) {
		if (focusedComponent == null) {
			tokenComponents.add(tokenUi);
		} else {
			tokenComponents.add(tokenComponents.indexOf(focusedComponent)+1,tokenUi);
		}		
		
		if(line !=null){
			line.addComponent(tokenUi);
		}else{
			if (focusedComponent == null) {
				getCurrentLine().addComponent(tokenUi);
			} else {
				AbstractOrderedLayout lineToInsert = getCurrentLine();
				int index = lineToInsert.getComponentIndex(focusedComponent);
				lineToInsert.addComponent(tokenUi, index + 1);
			}
		}
		setFocused(tokenUi);
	}

	private AbstractOrderedLayout getCurrentLine() {
		if(rootLayout.getComponentCount()==0){
			return null;
		}
		if (focusedComponent == null) {
			return (AbstractOrderedLayout) rootLayout.getComponent(rootLayout.getComponentCount()-1);
		} else {
			return (AbstractOrderedLayout) focusedComponent.getParent();
		}
	}

	private void setFocused(TokenComponent tokenUi) {
		if (focusedComponent != null) {
			focusedComponent.loseFocus();
		}
		focusedComponent = tokenUi;
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

	public void delete() {
		if (focusedComponent != null) {
			TokenComponent temp = focusedComponent;
			previous();
			if (focusedComponent == temp) {
				focusedComponent = null;
			}
			deleteTokenComponent(temp);
		}
	}

	private void deleteTokenComponent(TokenComponent tokenComponent) {
		if (tokenComponent.getToken().getType() == TokenTypes.RETURN) {
			removeLine((AbstractOrderedLayout) tokenComponent.getParent());
		}
		((AbstractOrderedLayout) tokenComponent.getParent()).removeComponent(tokenComponent);
		tokenComponents.remove(tokenComponent);
	}

	/**
	 * Removes all elements in line, then inserts in previous line.
	 * 
	 * @param line
	 */
	private void removeLine(AbstractOrderedLayout line) {
		int index = rootLayout.getComponentIndex(line);
		AbstractOrderedLayout previousLine = (AbstractOrderedLayout) rootLayout.getComponent(index - 1);

		List<Component> componentsToMove = new ArrayList<Component>();
		Iterator<Component> componentItr = line.iterator();
		while (componentItr.hasNext()) {
			componentsToMove.add(componentItr.next());
		}
		line.removeAllComponents();

		for (Component componentToMove : componentsToMove) {
			previousLine.addComponent(componentToMove);
		}
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
		if (tokenComponents.isEmpty()) {
			return null;
		}
		if (focusedComponent == null) {
			if (numPosition > 0) {
				return tokenComponents.get(0);
			} else {
				return tokenComponents.get(tokenComponents.size() - 1);
			}
		} else {
			int index = tokenComponents.indexOf(focusedComponent);
			index += numPosition;
			index %= tokenComponents.size();
			return tokenComponents.get(Math.abs(index));
		}
	}

	List<Token> getTokens() {
		List<Token> tokens = new ArrayList<>();
		for (TokenComponent tokenComponent : tokenComponents) {
			tokens.add(tokenComponent.getToken());
		}
		return tokens;
	}

	private HorizontalLayout addLine() {
		HorizontalLayout newLine = new HorizontalLayout();
		newLine.setWidth(null);
		newLine.setHeight(LINE_HEIGHT);
		newLine.setSpacing(true);
		
		if(getCurrentLine()==null){
			rootLayout.addComponent(newLine);
			return newLine;
		}
		
		int currentIndex = rootLayout.getComponentIndex(getCurrentLine());
		if(currentIndex+1<rootLayout.getComponentCount()){
			rootLayout.addComponent(newLine,currentIndex);
		}else{
			rootLayout.addComponent(newLine);
		}

		return newLine;
	}

	public void addTokenDoubleClickListener(TokenDoubleClickListener listener) {
		// Adds to the list for future tokens
		tokenDoubleClickListeners.add(listener);
		// And registres for the already existing tokens.
		for (TokenComponent tokenComponent : tokenComponents) {
			tokenComponent.addTokenDoubleClick(listener);
		}
	}

	public void defineShortcuts() {
		deleteShotcut = new ShortcutListener("DELETE_SHORTCUT", KeyCode.DELETE, null) {
			private static final long serialVersionUID = -71562151456777493L;

			@Override
			public void handleAction(Object sender, Object target) {
				delete();
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
		List<TokenComponent> componentsToDelete = new ArrayList<TokenComponent>(tokenComponents);
		for (TokenComponent tokenComponent : componentsToDelete) {
			deleteTokenComponent(tokenComponent);
		}
		componentsToDelete.clear();
	}
}
