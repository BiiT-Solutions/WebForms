package com.biit.webforms.utils.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biit.webforms.utils.lexer.ITokenType;
import com.biit.webforms.utils.lexer.Lexer;
import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.lexer.exceptions.StringTokenizationError;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpressionNotWellFormedException;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.Expression;
import com.biit.webforms.utils.parser.parselets.InfixParselet;
import com.biit.webforms.utils.parser.parselets.PrefixParselet;

public class Parser {

	protected final Iterator<Token> tokens;
	private final List<Token> read = new ArrayList<Token>();
	private final Set<ITokenType> ignore = new HashSet<>();
	private final Map<ITokenType, PrefixParselet> prefixParselets = new HashMap<ITokenType, PrefixParselet>();
	private final Map<ITokenType, InfixParselet> infixParselets = new HashMap<ITokenType, InfixParselet>();

	public Parser(Lexer lexer, String string) throws StringTokenizationError {
		this.tokens = lexer.tokenize(string).iterator();
	}

	public Parser(Iterator<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Register a new prefix parselet.
	 * 
	 * @param tokenType
	 * @param parselet
	 */
	public void register(ITokenType tokenType, PrefixParselet parselet) {
		prefixParselets.put(tokenType, parselet);
	}

	/**
	 * Register a new infix parselet (They are also used for postfix
	 * expressions).
	 * 
	 * @param tokenType
	 * @param parselet
	 */
	public void register(ITokenType tokenType, InfixParselet parselet) {
		infixParselets.put(tokenType, parselet);
	}

	public void registerTokenToSkip(ITokenType tokenType) {
		ignore.add(tokenType);
	}

	public Expression parseExpression(int precedence) throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException, EmptyParenthesisException {
		Token token = consume();
		if (token == null) {
			// EOF reached
			return null;
		}

		PrefixParselet prefix = prefixParselets.get(token.getType());
		if (prefix == null) {
			throw new ParseException("Could not parse \"" + token.getContent() + "\".");
		}

		Expression left = prefix.parse(this, token);

		while (tokens.hasNext() && precedence < getPrecedence()) {
			token = consume();

			InfixParselet infix = infixParselets.get(token.getType());
			left = infix.parse(this, left, token);
		}

		return left;
	}

	public Expression parseExpression() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			IncompleteBinaryOperatorException, MissingParenthesisException, EmptyParenthesisException {
		return parseExpression(0);
	}

	/**
	 * Method to parse the expression fully. It will raise an exception if there
	 * are no consumed tokens
	 * 
	 * @return
	 * @throws MissingParenthesisException 
	 * @throws IncompleteBinaryOperatorException 
	 * @throws NoMoreTokensException 
	 * @throws ExpectedTokenNotFound 
	 * @throws ParseException 
	 * @throws ExpressionNotWellFormedException 
	 * @throws EmptyParenthesisException 
	 */
	public Expression parseCompleteExpression() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException, IncompleteBinaryOperatorException, MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException {
		Expression expression = parseExpression();
		Token nextToken = lookAhead(0);
		if(nextToken!=null){
			throw new ExpressionNotWellFormedException(nextToken);
		}
		return expression;
	}

	public boolean match(ITokenType expected) {
		Token token = lookAhead(0);
		if (token == null || !token.getType().equals(expected)) {
			return false;
		}

		consume();
		return true;
	}

	public Token consume(ITokenType expected) throws ExpectedTokenNotFound, NoMoreTokensException {
		Token token = lookAhead(0);
		if (token == null) {
			throw new NoMoreTokensException("Unexpected consume");
		}
		if (!token.getType().equals(expected)) {
			throw new ExpectedTokenNotFound(expected, token.getType());
		}
		return consume();
	}

	public Token consume() {
		// Make sure we've read the token.
		if (lookAhead(0) != null) {
			return read.remove(0);
		} else {
			return null;
		}
	}

	public Token lookAhead(int distance) {
		// Read in as many as needed.
		while (distance >= read.size()) {
			if (!tokens.hasNext()) {
				return null;
			}

			Token nextToken = tokens.next();
			if (!isIgnored(nextToken)) {
				read.add(nextToken);
			}
		}

		// Get the queued token.
		return read.get(distance);
	}

	private int getPrecedence() {
		InfixParselet parser = infixParselets.get(lookAhead(0).getType());
		if (parser != null)
			return parser.getPrecedence();
		else {
			// INFO - token not recognized with infix parselet
			return 0;
		}
	}

	private boolean isIgnored(Token token) {
		return ignore.contains(token.getType());
	}

}
