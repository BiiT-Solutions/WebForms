package com.biit.webforms.condition.parser;

/**
 * Order of preference of the token on the PRATT parser
 *
 */
public class Precedence {
	public static final int ASSIGNMENT = 1;
	public static final int OR = 2;
	public static final int AND = 3;
	public static final int CONDITIONAL = 4;
	public static final int SUM = 5;
	public static final int PRODUCT = 6;
	public static final int EXPONENT = 7;
	public static final int PREFIX = 8;
	public static final int POSTFIX = 9;
	public static final int CALL = 10;
}
