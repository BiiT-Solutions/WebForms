package com.biit.webforms.utils.lexer;

import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.webforms.utils.lexer.exceptions.StringTokenizationError;
import com.biit.webforms.utils.lexer.tokens.TokenAnd;
import com.biit.webforms.utils.lexer.tokens.TokenValueAnswer;
import com.biit.webforms.utils.lexer.tokens.TokenBetween;
import com.biit.webforms.utils.lexer.tokens.TokenValueDate;
import com.biit.webforms.utils.lexer.tokens.TokenValueDatePeriod;
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
import com.biit.webforms.utils.lexer.tokens.TokenValueText;
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
	private final static String REFERENCE_1 = "<category><group><question>";
	private final static String REFERENCE_2 = "<answer>";
	private final static String TEXT_VALID = "\"aanvrager\"";
	private final static String TEXT_NOT_VALID = "andaanvrager";
	private final static String DATE_1 = "01-01-1950";
	private final static String DATE_2 = "01/01/1950";
	private final static String DATE_PERIOD_1 = "15D";
	private final static String DATE_PERIOD_2 = "15M";
	private final static String DATE_PERIOD_3 = "15Y";
	private final static String BETWEEN_1 = "BETWEEN";
	private final static String BETWEEN_2 = "between";
	private final static String ANSWER_VALUE = "'categöry_gory'";
	private final static String TEST_1 = "<category><persondata><birthdate><=18Y";
	private final static String TEST_2 = "<category><persondata><birthdate>>01-01-1950";
	private final static String TEST_3 = "(<category><persondata><birthdate>>01-01-1950) && (<category><persondata><birthdate><=18Y)";
	private final static String TEST_4 = "<new_category><group1><qu1> == 'a'";
	private final static String TEST_ = "";

	@Test
	public void tokenizerTestEmpty() throws StringTokenizationError {

		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(null);
		Assert.assertTrue(tokens.isEmpty());
		tokens = tokenizer.tokenize(EMPTY_RULE);
		Assert.assertTrue(tokens.isEmpty());
	}

	@Test
	public void tokenizerTestWhitespace() throws StringTokenizationError {
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
	public void tokenizerTestAnd() throws StringTokenizationError {
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
	public void tokenizerTestOr() throws StringTokenizationError {
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
	public void tokenizerTestNot() throws StringTokenizationError {
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
	public void tokenizerTestLeftPar() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(LEFT_PAR);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenLeftPar));
	}

	@Test
	public void tokenizerTestRightPar() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(RIGHT_PAR);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenRightPar));
	}

	@Test
	public void tokenizerTestGt() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(GT);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenGt));
	}

	@Test
	public void tokenizerTestLt() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(LT);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenLt));
	}

	@Test
	public void tokenizerTestGe() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(GE);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenGe));
	}

	@Test
	public void tokenizerTestLe() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(LE);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenLe));
	}

	@Test
	public void tokenizerTestNe() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(NE);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenNe));
	}

	@Test
	public void tokenizerTestEq() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(EQ);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenEq));
	}

	@Test
	public void tokenizerTestReference() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(REFERENCE_1);
		Assert.assertTrue((tokens.size() == 3) && (tokens.get(0) instanceof TokenReference)
				&& (tokens.get(1) instanceof TokenReference) && (tokens.get(2) instanceof TokenReference));
		tokens = tokenizer.tokenize(REFERENCE_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenReference));
	}

	@Test
	public void tokenizerTestText() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEXT_VALID);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenValueText));
	}

	@Test(expectedExceptions = { StringTokenizationError.class })
	public void tokenizerTestTextNotValid() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		tokenizer.tokenize(TEXT_NOT_VALID);
	}

	@Test
	public void tokenizerTestDate() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(DATE_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenValueDate));
		tokens = tokenizer.tokenize(DATE_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenValueDate));
	}

	@Test
	public void tokenizerTestDatePeriod() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(DATE_PERIOD_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenValueDatePeriod));
		tokens = tokenizer.tokenize(DATE_PERIOD_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenValueDatePeriod));
		tokens = tokenizer.tokenize(DATE_PERIOD_3);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenValueDatePeriod));
	}

	@Test
	public void tokenizerTestBetween() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(BETWEEN_1);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenBetween));
		tokens = tokenizer.tokenize(BETWEEN_2);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenBetween));
	}

	@Test
	public void tokenizerTestAnswerValue() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(ANSWER_VALUE);
		Assert.assertTrue((tokens.size() == 1) && (tokens.get(0) instanceof TokenValueAnswer));
	}

	@Test
	public void tokenizerTestExpressions() throws StringTokenizationError {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEST_1);

		Assert.assertTrue((!tokens.isEmpty()) && (tokens.get(0) instanceof TokenReference)
				&& (tokens.get(1) instanceof TokenReference) && (tokens.get(2) instanceof TokenReference)
				&& (tokens.get(3) instanceof TokenLe) && (tokens.get(4) instanceof TokenValueDatePeriod));
		tokens = tokenizer.tokenize(TEST_2);
		Assert.assertTrue((!tokens.isEmpty()) && (tokens.get(0) instanceof TokenReference)
				&& (tokens.get(1) instanceof TokenReference) && (tokens.get(2) instanceof TokenReference)
				&& (tokens.get(3) instanceof TokenGt) && (tokens.get(4) instanceof TokenValueDate));
		tokens = tokenizer.tokenize(TEST_3);
		Assert.assertTrue((!tokens.isEmpty()) && (tokens.get(0) instanceof TokenLeftPar)
				&& (tokens.get(1) instanceof TokenReference) && (tokens.get(2) instanceof TokenReference)
				&& (tokens.get(3) instanceof TokenReference) && (tokens.get(4) instanceof TokenGt)
				&& (tokens.get(5) instanceof TokenValueDate) && (tokens.get(6) instanceof TokenRightPar)
				&& (tokens.get(7) instanceof TokenWhitespace) && (tokens.get(8) instanceof TokenAnd)
				&& (tokens.get(9) instanceof TokenWhitespace) && (tokens.get(10) instanceof TokenLeftPar)
				&& (tokens.get(11) instanceof TokenReference) && (tokens.get(12) instanceof TokenReference)
				&& (tokens.get(13) instanceof TokenReference) && (tokens.get(14) instanceof TokenLe)
				&& (tokens.get(15) instanceof TokenValueDatePeriod) && (tokens.get(16) instanceof TokenRightPar));
		tokens = tokenizer.tokenize(TEST_4);
		Assert.assertTrue(!tokens.isEmpty());
	}

	// @Test
	// public void tokenizerTestAnd() throws StringTokenizationError {
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
