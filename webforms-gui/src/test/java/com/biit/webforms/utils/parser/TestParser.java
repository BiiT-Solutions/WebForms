package com.biit.webforms.utils.parser;

import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.webforms.utils.lexer.Token;
import com.biit.webforms.utils.lexer.WebformsLexer;
import com.biit.webforms.utils.lexer.exceptions.StringTokenizationError;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpressionNotWellFormedException;
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
	private final static String TEST_9 = "<new_category><group1><qu1> == 'a'";
	private final static String TEST_INCOMPLETE_BINARY_EXCEPTION = "<new_category><group1><qu1> == 'a' OR ";
	private static final String TEST_9_PARSED = "([<new_category>, <group1>, <qu1>] EQ 'a')";
	private static final String TEST_10 = "\"biit@biit.com\"\"biit@biit.com\"";
	private static final String TEST_11 = "<new_category><group1><qu1> == 'a'";
	private static final String TEST_11_PARSED = "([<new_category>, <group1>, <qu1>] EQ 'a')";
	private static final String TEST_12 = "<new_category><group1><qu1> == \"a\"";
	private static final String TEST_13 = "<new_category><group1><qu1> == 10-05-1985";
	private static final String TEST_14 = "<new_category><group1><qu1> == 10/05/1985";
	private static final String TEST_15 = "<new_category><group1><qu1> == 23Y";
	private static final String TEST_16 = "<new_category><group1><qu1> == 23";
	private static final String TEST_17 = "<new_category><group1><qu1> == 23.";
	private static final String TEST_18 = "<new_category><group1><qu1> == 9999AA";
	private static final String TEST_12_PARSED = "([<new_category>, <group1>, <qu1>] EQ \"a\")";
	private static final String TEST_13_PARSED = "([<new_category>, <group1>, <qu1>] EQ 10-05-1985)";
	private static final String TEST_14_PARSED = "([<new_category>, <group1>, <qu1>] EQ 10/05/1985)";
	private static final String TEST_15_PARSED = "([<new_category>, <group1>, <qu1>] EQ 23Y)";
	private static final String TEST_16_PARSED = "([<new_category>, <group1>, <qu1>] EQ 23)";
	private static final String TEST_17_PARSED = "([<new_category>, <group1>, <qu1>] EQ 23.)";
	private static final String TEST_18_PARSED = "([<new_category>, <group1>, <qu1>] EQ 9999AA)";
	

	@Test
	public void parserTestReference() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEST_1);
		Assert.assertTrue(!tokens.isEmpty());
		Expression expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertTrue(expression instanceof Reference);
		Assert.assertEquals(expression.toString(), TEST_1_PARSED);
	}

	@Test
	public void tokenizerTestComparation() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEST_2);
		Assert.assertTrue(!tokens.isEmpty());
		Expression expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_2_PARSED);
	}

	@Test
	public void tokenizerTestLogic() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEST_3);
		Assert.assertTrue(!tokens.isEmpty());
		Expression expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_3_PARSED);
	}

	@Test
	public void tokenizerTestLogic2() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		Expression expression;

		tokens = tokenizer.tokenize(TEST_3);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_3_PARSED);
		tokens = tokenizer.tokenize(TEST_4);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_4_PARSED);
		tokens = tokenizer.tokenize(TEST_5);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_5_PARSED);
		tokens = tokenizer.tokenize(TEST_6);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_6_PARSED);
		tokens = tokenizer.tokenize(TEST_7);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_7_PARSED);
		tokens = tokenizer.tokenize(TEST_8);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_8_PARSED);
	}

	@Test
	public void tokenizerTestExpressions() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		Expression expression;

		tokens = tokenizer.tokenize(TEST_9);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_9_PARSED);

	}

	@Test(expectedExceptions = { IncompleteBinaryOperatorException.class })
	public void parserTestIncompleteBinaryException() throws ParseException, ExpectedTokenNotFound,
			NoMoreTokensException, StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;

		tokens = tokenizer.tokenize(TEST_INCOMPLETE_BINARY_EXCEPTION);
		Assert.assertTrue(!tokens.isEmpty());
		(new WebformsParser(tokens.listIterator())).parseCompleteExpression();
	}
	
	@Test(expectedExceptions = { ExpressionNotWellFormedException.class })
	public void parserTestExpressionNotValid() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		tokens = tokenizer.tokenize(TEST_10);
		Assert.assertTrue(!tokens.isEmpty());
		(new WebformsParser(tokens.listIterator())).parseCompleteExpression();
	}
	
	@Test
	public void parserTestExpressionValue() throws ParseException, ExpectedTokenNotFound, NoMoreTokensException,
			StringTokenizationError, IncompleteBinaryOperatorException, MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException {
		WebformsLexer tokenizer = new WebformsLexer();

		List<Token> tokens;
		Expression expression;

		tokens = tokenizer.tokenize(TEST_11);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_11_PARSED);
		
		tokens = tokenizer.tokenize(TEST_12);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_12_PARSED);
		
		tokens = tokenizer.tokenize(TEST_13);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_13_PARSED);
		
		tokens = tokenizer.tokenize(TEST_14);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_14_PARSED);
		
		tokens = tokenizer.tokenize(TEST_15);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_15_PARSED);
		
		tokens = tokenizer.tokenize(TEST_16);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_16_PARSED);
		
		tokens = tokenizer.tokenize(TEST_17);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_17_PARSED);
		
		tokens = tokenizer.tokenize(TEST_18);
		Assert.assertTrue(!tokens.isEmpty());
		expression = (new WebformsParser(tokens.listIterator())).parseCompleteExpression();
		Assert.assertEquals(expression.toString(), TEST_18_PARSED);
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
