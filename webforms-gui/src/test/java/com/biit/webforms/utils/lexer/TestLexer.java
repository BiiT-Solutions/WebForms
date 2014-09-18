package com.biit.webforms.utils.lexer;

import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.webforms.utils.lexer.exceptions.TokenizationError;
import com.biit.webforms.utils.lexer.tokens.TokenAnd;
import com.biit.webforms.utils.lexer.tokens.TokenBetween;
import com.biit.webforms.utils.lexer.tokens.TokenDate;
import com.biit.webforms.utils.lexer.tokens.TokenDatePeriod;
import com.biit.webforms.utils.lexer.tokens.TokenEq;
import com.biit.webforms.utils.lexer.tokens.TokenGe;
import com.biit.webforms.utils.lexer.tokens.TokenGt;
import com.biit.webforms.utils.lexer.tokens.TokenLe;
import com.biit.webforms.utils.lexer.tokens.TokenLeftPar;
import com.biit.webforms.utils.lexer.tokens.TokenLt;
import com.biit.webforms.utils.lexer.tokens.TokenNe;
import com.biit.webforms.utils.lexer.tokens.TokenNot;
import com.biit.webforms.utils.lexer.tokens.TokenOr;
import com.biit.webforms.utils.lexer.tokens.TokenReference;
import com.biit.webforms.utils.lexer.tokens.TokenRightPar;
import com.biit.webforms.utils.lexer.tokens.TokenText;
import com.biit.webforms.utils.lexer.tokens.TokenWhitespace;

@Test(groups = { "lexer" })
public class TestLexer {

	private final static String EMPTY_RULE = "";
	private final static String WHITESPACE_1 = " "; // One space
	private final static String WHITESPACE_2 = "\t"; // One tab
	private final static String WHITESPACE_3 = "\n"; // One end line
	private final static String WHITESPACE_4 = "\r"; // One carry return
	private final static String WHITESPACE_5 = "  "; // Several spaces
	private final static String WHITESPACE_6 = "  \n"; // Spaces and end lines
	private final static String AND_1 = "AND";
	private final static String AND_2 = "and";
	private final static String AND_3 = "&&";
	private final static String OR_1 = "OR";
	private final static String OR_2 = "or";
	private final static String OR_3 = "||";
	private final static String NOT_1 = "NOT";
	private final static String NOT_2 = "not";
	private final static String NOT_3 = "!";
	private final static String LEFT_PAR = "(";
	private final static String RIGHT_PAR = ")";
	private final static String GT = ">";
	private final static String LT = "<";
	private final static String GE = ">=";
	private final static String LE = "<=";
	private final static String EQ = "==";
	private final static String NE = "!=";
	private final static String REFERENCE_1 = "${<category><group><question>}";
	private final static String REFERENCE_2 = "${<answer>}";
	private final static String TEXT_VALID = "aanvrager";
	private final static String TEXT_NOT_VALID = "andaanvrager";
	private final static String DATE_1 = "01-01-1950";
	private final static String DATE_2 = "01/01/1950";
	private final static String DATE_PERIOD_1 = "15D";
	private final static String DATE_PERIOD_2 = "15M";
	private final static String DATE_PERIOD_3 = "15Y";
	private final static String DATE_PERIOD_4 = "D";
	private final static String BETWEEN_1 = "BETWEEN";
	private final static String BETWEEN_2 = "between";
	private final static String TEST_1 = "${<category><persondata><birthdate>}<=18Y";
	private final static String TEST_2 = "${<category><persondata><birthdate>}>01-01-1950";
	private final static String TEST_3 = "(${<category><persondata><birthdate>}>01-01-1950 && ${<category><persondata><birthdate>}<=18Y)";
	private final static String TEST_ = "";

	@Test
	public void tokenizerTestEmpty() throws TokenizationError {

		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(null);
		Assert.assertTrue(tokens.isEmpty());
		tokens = tokenizer.tokenize(EMPTY_RULE);
		Assert.assertTrue(tokens.isEmpty());
	}

	@Test
	public void tokenizerTestWhitespace() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(WHITESPACE_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenWhitespace));
		tokens = tokenizer.tokenize(WHITESPACE_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenWhitespace));
		tokens = tokenizer.tokenize(WHITESPACE_3);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenWhitespace));
		tokens = tokenizer.tokenize(WHITESPACE_4);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenWhitespace));
		tokens = tokenizer.tokenize(WHITESPACE_5);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenWhitespace));
		tokens = tokenizer.tokenize(WHITESPACE_6);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenWhitespace));

	}

	@Test
	public void tokenizerTestAnd() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(AND_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenAnd));
		tokens = tokenizer.tokenize(AND_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenAnd));
		tokens = tokenizer.tokenize(AND_3);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenAnd));
	}

	@Test
	public void tokenizerTestOr() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(OR_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenOr));
		tokens = tokenizer.tokenize(OR_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenOr));
		tokens = tokenizer.tokenize(OR_3);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenOr));
	}

	@Test
	public void tokenizerTestNot() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(NOT_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenNot));
		tokens = tokenizer.tokenize(NOT_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenNot));
		tokens = tokenizer.tokenize(NOT_3);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenNot));
	}

	@Test
	public void tokenizerTestLeftPar() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(LEFT_PAR);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenLeftPar));
	}

	@Test
	public void tokenizerTestRightPar() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(RIGHT_PAR);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenRightPar));
	}

	@Test
	public void tokenizerTestGt() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(GT);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenGt));
	}

	@Test
	public void tokenizerTestLt() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(LT);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenLt));
	}

	@Test
	public void tokenizerTestGe() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(GE);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenGe));
	}

	@Test
	public void tokenizerTestLe() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(LE);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenLe));
	}

	@Test
	public void tokenizerTestNe() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(NE);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenNe));
	}

	@Test
	public void tokenizerTestEq() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(EQ);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenEq));
	}

	@Test
	public void tokenizerTestReference() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(REFERENCE_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenReference));
		tokens = tokenizer.tokenize(REFERENCE_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenReference));
	}

	@Test
	public void tokenizerTestText() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEXT_VALID);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenText));
		tokens = tokenizer.tokenize(TEXT_NOT_VALID);
		Assert.assertTrue((tokens.size() != 1));
	}

	@Test
	public void tokenizerTestDate() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(DATE_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenDate));
		tokens = tokenizer.tokenize(DATE_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenDate));
	}

	@Test
	public void tokenizerTestDatePeriod() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(DATE_PERIOD_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenDatePeriod));
		tokens = tokenizer.tokenize(DATE_PERIOD_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenDatePeriod));
		tokens = tokenizer.tokenize(DATE_PERIOD_3);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenDatePeriod));
		tokens = tokenizer.tokenize(DATE_PERIOD_4);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenText));
	}

	@Test
	public void tokenizerTestBetween() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(BETWEEN_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenBetween));
		tokens = tokenizer.tokenize(BETWEEN_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenBetween));
	}

	@Test
	public void tokenizerTestExpressions() throws TokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEST_1);
		Assert.assertTrue((!tokens.isEmpty()) && (tokens.get(0) instanceof TokenReference)
				&& (tokens.get(1) instanceof TokenLe) && (tokens.get(2) instanceof TokenDatePeriod));
		tokens = tokenizer.tokenize(TEST_2);
		Assert.assertTrue((!tokens.isEmpty()) && (tokens.get(0) instanceof TokenReference)
				&& (tokens.get(1) instanceof TokenGt) && (tokens.get(2) instanceof TokenDate));
		tokens = tokenizer.tokenize(TEST_3);
		Assert.assertTrue((!tokens.isEmpty()) && (tokens.get(0) instanceof TokenLeftPar)
				&& (tokens.get(1) instanceof TokenReference) && (tokens.get(2) instanceof TokenGt)
				&& (tokens.get(3) instanceof TokenDate) && (tokens.get(4) instanceof TokenWhitespace)
				&& (tokens.get(5) instanceof TokenAnd) && (tokens.get(6) instanceof TokenWhitespace)
				&& (tokens.get(7) instanceof TokenReference) && (tokens.get(8) instanceof TokenLe)
				&& (tokens.get(9) instanceof TokenDatePeriod) && (tokens.get(10) instanceof TokenRightPar));
	}

	// @Test
	// public void tokenizerTestAnd() throws TokenizationError {
	//
	// WebformsTokenizer tokenizer = new WebformsTokenizer();
	//
	// List<IToken> tokens;
	// tokens = tokenizer.tokenize(AND_1);
	// System.out.println("TokenList: " + tokens);
	// Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof
	// TokenWhitespace));
	//
	// }

}
