package com.biit.webforms.utils.math.domain;

import java.util.Set;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.form.BaseQuestion;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.condition.parser.WebformsParser;
import com.biit.webforms.condition.parser.expressions.WebformsExpression;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOrigin;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestiny;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSource;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;
import com.biit.webforms.utils.parser.exceptions.EmptyParenthesisException;
import com.biit.webforms.utils.parser.exceptions.ExpectedTokenNotFound;
import com.biit.webforms.utils.parser.exceptions.ExpressionNotWellFormedException;
import com.biit.webforms.utils.parser.exceptions.IncompleteBinaryOperatorException;
import com.biit.webforms.utils.parser.exceptions.MissingParenthesisException;
import com.biit.webforms.utils.parser.exceptions.NoMoreTokensException;
import com.biit.webforms.utils.parser.exceptions.ParseException;

@Test(groups = { "testDomain" })
public class TestDomain {

	private static final String TEST_01 = "[]";
	private static final String TEST_02 = "[(-Infinity,Infinity)]";
	private static final String TEST_03 = "[(-Infinity,3.0)]";
	private static final String TEST_04 = "[(3.0,Infinity)]";
	private static final String TEST_05 = "[[3.0,Infinity)]";
	private static final String TEST_06 = "[(-Infinity,3.0]]";
	private static final String TEST_07 = "[(-Infinity,3.0)(3.0,Infinity)]";
	private static final Object TEST_08 = "[[2.0,4.0]]";
	private static final String TEST_09 = "[[2.0,4.0]]";
	private static final Object TEST_10 = "[[2.0,3.0)(3.0,4.0]]";
	private static final String TEST_11 = "[[2.0,3.0]]";
	private static final Object TEST_12 = "[(-Infinity,3.0)(3.0,5.0][6.0,9.0]]";
	private static final String TEST_13 = "[[0.0,4.0][7.0,8.0][20.0,Infinity)]";
	private static final String TEST_14 = "[(-Infinity,5.0][6.0,9.0][20.0,Infinity)]";
	private static final String TEST_15 = "[[3.0,3.0]]";
	private static final Object TEST_16 = "[]";
	private static final String TEST_17 = "[[0.0,3.0)(3.0,4.0][7.0,8.0]]";
	private static final Object TEST_18 = "[[3.0,3.0](5.0,6.0)(9.0,Infinity)]";
	private static final Object TEST_19 = "[(-Infinity,0.0)(4.0,7.0)(8.0,20.0)[Infinity,Infinity)]";

	@Test
	public void testRealRangeGeneration() {

		RealRange emptyRange = new RealRange();
		RealRange realRange = RealRange.realRange();
		RealRange ltThree = RealRange.lt(3.0f);
		RealRange gtThree = RealRange.gt(3.0f);
		RealRange geThree = RealRange.ge(3.0f);
		RealRange leThree = RealRange.le(3.0f);

		Assert.assertEquals(emptyRange.toString(), TEST_01);
		Assert.assertEquals(realRange.toString(), TEST_02);
		Assert.assertEquals(ltThree.toString(), TEST_03);
		Assert.assertEquals(gtThree.toString(), TEST_04);
		Assert.assertEquals(geThree.toString(), TEST_05);
		Assert.assertEquals(leThree.toString(), TEST_06);
	}

	@Test
	public void testRealRangeGenerationComposition() {
		RealRange composedRange = new RealRange(RealLimitPair.lt(3.0f), RealLimitPair.gt(3.0f));
		Assert.assertEquals(composedRange.toString(), TEST_07);
	}

	@Test
	public void testUnion() throws LimitInsertionException {

		RealRange range2to3 = new RealRange(Closure.INCLUSIVE, 2.0f, 3.0f, Closure.INCLUSIVE);
		RealRange range3to4 = new RealRange(Closure.INCLUSIVE, 3.0f, 4.0f, Closure.INCLUSIVE);

		Assert.assertEquals(range2to3.union(range3to4).toString(), TEST_08);

		RealRange range3to4OpenLeft = new RealRange(Closure.EXCLUSIVE, 3.0f, 4.0f, Closure.INCLUSIVE);

		Assert.assertEquals(range2to3.union(range3to4OpenLeft).toString(), TEST_09);

		RealRange range2to3OpenRight = new RealRange(Closure.INCLUSIVE, 2.0f, 3.0f, Closure.EXCLUSIVE);

		Assert.assertEquals(range2to3OpenRight.union(range3to4OpenLeft).toString(), TEST_10);

		RealRange range3 = new RealRange(Closure.INCLUSIVE, 3.0f, 3.0f, Closure.INCLUSIVE);

		Assert.assertEquals(range2to3OpenRight.union(range3).toString(), TEST_11);

	}

	@Test(dependsOnMethods = { "testUnion" })
	public void testUnionComposition() throws LimitInsertionException {

		// (-Inf,3)
		RealRange rang1 = RealRange.lt(3.0f);
		// (3,5]
		RealRange rang2 = new RealRange(Closure.EXCLUSIVE, 3.0f, 5.0f, Closure.INCLUSIVE);
		// [6,9]
		RealRange rang3 = new RealRange(Closure.INCLUSIVE, 6.0f, 9.0f, Closure.INCLUSIVE);

		RealRange rangeA = rang1.union(rang2).union(rang3);
		Assert.assertEquals(rangeA.toString(), TEST_12);

		// [0,4]
		RealRange rangB1 = new RealRange(Closure.INCLUSIVE, 0.0f, 4.0f, Closure.INCLUSIVE);
		// [7,8]
		RealRange rangB2 = new RealRange(Closure.INCLUSIVE, 7.0f, 8.0f, Closure.INCLUSIVE);
		// [20,+inf)
		RealRange rangB3 = RealRange.ge(20.0f);

		RealRange rangeB = rangB1.union(rangB2).union(rangB3);
		Assert.assertEquals(rangeB.toString(), TEST_13);
		Assert.assertEquals(rangeA.union(rangeB).toString(), TEST_14);

	}

	@Test
	public void testIntersection() throws LimitInsertionException {

		RealRange range2to3 = new RealRange(Closure.INCLUSIVE, 2.0f, 3.0f, Closure.INCLUSIVE);
		RealRange range3to4 = new RealRange(Closure.INCLUSIVE, 3.0f, 4.0f, Closure.INCLUSIVE);

		Assert.assertEquals(range2to3.intersection(range3to4).toString(), TEST_15);

		RealRange range3to4LeftOpen = new RealRange(Closure.EXCLUSIVE, 3.0f, 4.0f, Closure.INCLUSIVE);

		Assert.assertEquals(range2to3.intersection(range3to4LeftOpen).toString(), TEST_16);

	}

	@Test(dependsOnMethods = { "testIntersection" })
	public void testIntersectionComposition() throws LimitInsertionException {

		// (-Inf,3)
		RealRange rang1 = RealRange.lt(3.0f);
		// (3,5]
		RealRange rang2 = new RealRange(Closure.EXCLUSIVE, 3.0f, 5.0f, Closure.INCLUSIVE);
		// [6,9]
		RealRange rang3 = new RealRange(Closure.INCLUSIVE, 6.0f, 9.0f, Closure.INCLUSIVE);

		RealRange rangeA = rang1.union(rang2).union(rang3);

		// [0,4]
		RealRange rangB1 = new RealRange(Closure.INCLUSIVE, 0.0f, 4.0f, Closure.INCLUSIVE);
		// [7,8]
		RealRange rangB2 = new RealRange(Closure.INCLUSIVE, 7.0f, 8.0f, Closure.INCLUSIVE);
		// [20,+inf)
		RealRange rangB3 = RealRange.ge(20.0f);

		RealRange rangeB = rangB1.union(rangB2).union(rangB3);

		Assert.assertEquals(rangeA.intersection(rangeB).toString(), TEST_17);

	}

	@Test()
	public void testInverse() throws LimitInsertionException {

		// (-Inf,3)
		RealRange rang1 = RealRange.lt(3.0f);
		// (3,5]
		RealRange rang2 = new RealRange(Closure.EXCLUSIVE, 3.0f, 5.0f, Closure.INCLUSIVE);
		// [6,9]
		RealRange rang3 = new RealRange(Closure.INCLUSIVE, 6.0f, 9.0f, Closure.INCLUSIVE);

		RealRange rangeAinv = rang1.union(rang2).union(rang3).inverse(RealLimitPair.realRange());
		Assert.assertEquals(rangeAinv.toString(), TEST_18);

		// [0,4]
		RealRange rangB1 = new RealRange(Closure.INCLUSIVE, 0.0f, 4.0f, Closure.INCLUSIVE);
		// [7,8]
		RealRange rangB2 = new RealRange(Closure.INCLUSIVE, 7.0f, 8.0f, Closure.INCLUSIVE);
		// [20,+inf)
		RealRange rangB3 = RealRange.ge(20.0f);

		RealRange rangeBinv = rangB1.union(rangB2).union(rangB3).inverse(RealLimitPair.realRange());
		Assert.assertEquals(rangeBinv.toString(), TEST_19);

	}

//	@Test()
//	public void testDomainSet() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
//			ParseException, ExpectedTokenNotFound, NoMoreTokensException, IncompleteBinaryOperatorException,
//			MissingParenthesisException, ExpressionNotWellFormedException, EmptyParenthesisException,
//			BadFlowContentException, FlowWithoutSource, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOrigin, FlowWithoutDestiny {
//
//		Form form = FormTestUtilities.createFormTest1();
//
//		Set<Flow> flows1 = form.getFlowsFrom((BaseQuestion) form.getChild("cat1","qu1"));
//		
//		IDomain accumDomain = null;
//		for(Flow flow: flows1){
//			System.out.println(flow.getCondition());
//			WebformsParser parser = new WebformsParser(flow.getCondition().iterator());
//			WebformsExpression webformsExpression = (WebformsExpression) parser.parseCompleteExpression();
//			IDomain domain = webformsExpression.getDomain();
//			if(accumDomain ==null){
//				accumDomain = domain;
//			}else{
//				accumDomain = accumDomain.union(domain);
//			}
//			System.out.println("Domain: "+flow.getCondition());
//			
//		}
//		
//		System.out.println("Accumulated Domain: "+accumDomain);
//		System.out.println("Accumulated Complete: "+accumDomain.isComplete());
//		System.out.println("Accumulated Empty: "+accumDomain.isEmpty());
//		
//		
//
//		
//	}
}
