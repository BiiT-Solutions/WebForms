package com.biit.webforms.gui.webpages.floweditor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biit.webforms.enumerations.TokenTypes;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class LineDisplay extends CustomComponent {
	private static final long serialVersionUID = 1358931913511589916L;
	private static final String CLASSNAME = "line-display";
	private static final String LINE_HEIGHT = "32px";

	private HorizontalLayout rootLayout;

	public LineDisplay() {
		super();
		rootLayout = new HorizontalLayout();

		generate();
		initialize();
	}

	public TokenComponent getFirstToken() {
		if (rootLayout.getComponentCount() == 0) {
			return null;
		}
		return (TokenComponent) rootLayout.getComponent(0);
	}

	public TokenComponent getLastToken() {
		if (rootLayout.getComponentCount() == 0) {
			return null;
		}
		return (TokenComponent) rootLayout.getComponent(rootLayout.getComponentCount() - 1);
	}

	public int getTokenCount() {
		return rootLayout.getComponentCount();
	}

	public void addTokens(List<TokenComponent> tokens) {
		for (TokenComponent token : tokens) {
			addToken(token);
		}
	}

	/**
	 * Adds a new token to the line. If the token is a return element then appears always at the beginning of the list.
	 * The return token can only appear as first element. As it is intented to work like a new character than can be
	 * deleted to allow to allow the delete of the line.
	 * 
	 * @param token
	 */
	public void addToken(TokenComponent token) {
		rootLayout.addComponent(token);
	}

	public void addToken(TokenComponent token, int index) {
		rootLayout.addComponent(token, index);
	}

	public boolean addTokenAfter(TokenComponent token, TokenComponent focusedComponent) {
		if (focusedComponent == null) {
			addToken(token);
			return true;
		} else {
			int insertionIndex = rootLayout.getComponentIndex(focusedComponent) + 1;
			if (insertionIndex == rootLayout.getComponentCount()) {
				// Last item selected. Insert at end.
				addToken(token);
				return true;
			} else {
				// Avoid two consecutive pilcrum.
				if (!token.getToken().getType().equals(TokenTypes.RETURN)
						|| (rootLayout.getComponent(insertionIndex) instanceof TokenComponent && !((TokenComponent) rootLayout
								.getComponent(insertionIndex)).getToken().getType().equals(TokenTypes.RETURN))) {
					rootLayout.addComponent(token, insertionIndex);
					return true;
				}
			}
		}
		return false;
	}

	public void removeToken(TokenComponent token) {
		rootLayout.removeComponent(token);
	}

	public List<TokenComponent> getTokens() {
		List<TokenComponent> tokens = new ArrayList<TokenComponent>();
		Iterator<Component> itr = rootLayout.iterator();
		while (itr.hasNext()) {
			tokens.add((TokenComponent) itr.next());
		}
		return tokens;
	}

	private void generate() {
		setStyleName(CLASSNAME);

		rootLayout.setWidth(null);
		rootLayout.setHeight(LINE_HEIGHT);
		rootLayout.setSpacing(true);

		setCompositionRoot(rootLayout);
	}

	private void initialize() {
		setSizeUndefined();
	}

	public List<TokenComponent> getTokensAfter(TokenComponent focusedComponent) {
		List<TokenComponent> tokens = new ArrayList<TokenComponent>();
		int index = rootLayout.getComponentIndex(focusedComponent);
		if (index < 0) {
			return tokens;
		}
		for (int i = index + 1; i < rootLayout.getComponentCount(); i++) {
			tokens.add((TokenComponent) rootLayout.getComponent(i));
		}
		return tokens;
	}

	public void removeTokens(List<TokenComponent> tokens) {
		for (TokenComponent token : tokens) {
			removeToken(token);
		}
	}
}