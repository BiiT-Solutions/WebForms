package com.biit.webforms.utils.parser;

import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.lexer.WebformsLexer;
import com.biit.webforms.utils.lexer.exceptions.StringTokenizationError;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;
import com.biit.webforms.utils.parser.expressions.Expression;
import com.biit.webforms.utils.parser.expressions.Reference;

@Test(groups = { "parser" })
public class TestParser {

	private final static String TEST_1 = "<cat1><gr1><qu1>";
	private final static String TEST_1_PARSED = "[<cat1>, <gr1>, <qu1>]";
	private final static String TEST_2 = "<cat1><gr1><qu1> >= \"123123\"";
	private final static String TEST_2_PARSED = "([<cat1>, <gr1>, <qu1>] GE \"123123\")";
	private final static String TEST_3 = "\"A\" AND \"B\" OR \"C\"";
	private final static String TEST_3_PARSED = "((\"A\" AND \"B\") OR \"C\")";
	private final static String TEST_4 = "!\"A\" AND \"B\" OR \"C\"";
	private final static String TEST_4_PARSED = "(((NOT \"A\") AND \"B\") OR \"C\")";
	private final static String TEST_5 = "\"A\" AND !\"B\" OR \"C\"";
	private final static String TEST_5_PARSED = "((\"A\" AND (NOT \"B\")) OR \"C\")";
	private final static String TEST_6 = "\"A\" AND \"B\" OR !\"C\"";
	private final static String TEST_6_PARSED = "((\"A\" AND \"B\") OR (NOT \"C\"))";
	private final static String TEST_7 = "\"A\" AND NOT(\"B\" OR \"D\") OR \"C\"";
	private final static String TEST_7_PARSED = "((\"A\" AND (NOT (\"B\" OR \"D\"))) OR \"C\")";
	private final static String TEST_8 = "\"A\" AND \"B\" OR NOT(\"C\" OR \"D\")";
	private final static String TEST_8_PARSED = "((\"A\" AND \"B\") OR (NOT (\"C\" OR \"D\")))";
	private final static String TEST_9 = "<new_category><group1><qu1> == %a%";
	private final static String TEST_INCOMPLETE_BINARY_EXCEPTION = "<new_category><group1><qu1> == %a% OR ";

	@Test
	public void parserTestReference() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEST_1);
		Assert.assertTrue(!tokens.isEmpty());
		Expression expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		Assert.assertTrue(expression instanceof Reference);
		Assert.assertEquals(expression.toString(), TEST_1_PARSED);
	}

	@Test
	public void tokenizerTestComparation() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEST_2);
		Assert.assertTrue(!tokens.isEmpty());
		Expression expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		Assert.assertEquals(expression.toString(), TEST_2_PARSED);
	}

	@Test
	public void tokenizerTestLogic() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEST_3);
		Assert.assertTrue(!tokens.isEmpty());
		Expression expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		Assert.assertEquals(expression.toString(), TEST_3_PARSED);
	}

	@Test
	public void tokenizerTestLogic2() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		Expression expression;

		tokens = tokenizer.tokenize(TEST_3);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		Assert.assertEquals(expression.toString(), TEST_3_PARSED);
		tokens = tokenizer.tokenize(TEST_4);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		Assert.assertEquals(expression.toString(), TEST_4_PARSED);
		tokens = tokenizer.tokenize(TEST_5);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		Assert.assertEquals(expression.toString(), TEST_5_PARSED);
		tokens = tokenizer.tokenize(TEST_6);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		Assert.assertEquals(expression.toString(), TEST_6_PARSED);
		tokens = tokenizer.tokenize(TEST_7);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		Assert.assertEquals(expression.toString(), TEST_7_PARSED);
		tokens = tokenizer.tokenize(TEST_8);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		Assert.assertEquals(expression.toString(), TEST_8_PARSED);
	}

	@Test
	public void tokenizerTestExpressions() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		Expression expression;

		tokens = tokenizer.tokenize(TEST_9);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseExpression();
		System.out.println(expression);
		// Assert.assertEquals(expression.toString(), TEST_3_PARSED);

	}

	@Test(expectedExceptions = { IncompleteBinaryOperatorException.class })
	public void parserTestIncompleteBinaryException() throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException, StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;

		tokens = tokenizer.tokenize(TEST_INCOMPLETE_BINARY_EXCEPTION);
		Assert.assertTrue(!tokens.isEmpty());
		(new WebformsParser(tokens.listIterator())).parseExpression();
	}

	// @Test
	// public void tokenizerTestLogic() throws TokenizationError, ParseException
	// {
	// WebformsLexer tokenizer = new WebformsLexer();
	//
	// List<Token> tokens;
	// tokens = tokenizer.tokenize(TEST_3);
	// Assert.assertTrue(!tokens.isEmpty());
	// System.out.println(tokens);
	// Expression expression = (new
	// WebformsParser(tokens.listIterator())).parseExpression();
	// System.out.println(expression.getString());
	// Assert.assertEquals(expression.getString(),TEST_3_PARSED);
	// }

}
