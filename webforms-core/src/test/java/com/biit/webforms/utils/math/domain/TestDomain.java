package com.biit.webforms.utils.math.domain;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.utils.math.domain.exceptions.LimitInsertionException;

@Test(groups = { "testDomain" })
public class TestDomain {

	// @Test
	// public void testDiscreteDomain() {
	// System.out.println("testDiscreteDomain");
	//
	// DiscreteSet<String> stringDomain = new DiscreteSet<String>();
	// stringDomain.add("A", "B", "C", "D");
	//
	// System.out.println(stringDomain);
	// }

	// @Test
	// public void testQuestionAnswerDomain() throws FieldTooLongException,
	// CharacterNotAllowedException,
	// NotValidChildException, ChildrenNotFoundException,
	// IncompatibleDomainException,
	// DifferentDomainQuestionOperationException {
	// System.out.println("testQuestionAnswerDomain");
	//
	// Question testQuestion1 = createQuestionAnswers("testQuestion1", "A", "B",
	// "C", "D");
	// Answer answerA = (Answer) testQuestion1.getChild(0);
	// Answer answerB = (Answer) testQuestion1.getChild(1);
	// Answer answerC = (Answer) testQuestion1.getChild(2);
	// Answer answerD = (Answer) testQuestion1.getChild(3);
	//
	// QuestionAnswerDomain questionAnswerDomain = new
	// QuestionAnswerDomain(testQuestion1);
	// QuestionAnswerDomain inverseDomain = questionAnswerDomain.inverse();
	//
	// Assert.assertTrue(questionAnswerDomain.getValue().isEmpty());
	// Assert.assertTrue(questionAnswerDomain.isEmpty());
	// Assert.assertFalse(inverseDomain.getValue().isEmpty());
	// Assert.assertTrue(inverseDomain.isComplete());
	//
	// questionAnswerDomain.addValue(answerA);
	// Assert.assertTrue(questionAnswerDomain.contains(answerA));
	// Assert.assertFalse(questionAnswerDomain.contains(answerB));
	// Assert.assertFalse(questionAnswerDomain.contains(answerC));
	// Assert.assertFalse(questionAnswerDomain.contains(answerD));
	// Assert.assertFalse(questionAnswerDomain.isComplete());
	//
	// QuestionAnswerDomain testDomainA = new
	// QuestionAnswerDomain(testQuestion1);
	// testDomainA.addValue(answerA);
	// QuestionAnswerDomain testDomainBCD = new
	// QuestionAnswerDomain(testQuestion1);
	// testDomainBCD.addValue(answerB);
	// testDomainBCD.addValue(answerC);
	// testDomainBCD.addValue(answerD);
	// QuestionAnswerDomain testDomainABC = new
	// QuestionAnswerDomain(testQuestion1);
	// testDomainABC.addValue(answerA);
	// testDomainABC.addValue(answerB);
	// testDomainABC.addValue(answerC);
	//
	// Assert.assertTrue(testDomainA.union(testDomainBCD).isComplete());
	// Assert.assertTrue(testDomainA.intersect(testDomainBCD).isEmpty());
	// Assert.assertFalse(testDomainA.union(testDomainABC).isComplete());
	// Assert.assertTrue(((QuestionAnswerDomain)
	// testDomainA.union(testDomainABC)).getValue().size() == 3);
	// Assert.assertFalse(testDomainA.intersect(testDomainABC).isEmpty());
	// Assert.assertTrue(((QuestionAnswerDomain)
	// testDomainA.intersect(testDomainABC)).getValue().size() == 1);
	// }

	@Test
	public void testRealRange() throws LimitInsertionException {
		RealRange emptyRange = new RealRange();
		RealRange realRange = RealRange.realRange();
		RealRange ltThree = RealRange.lt(3.0f);
//		RealRange gtThree = RealRange.gt(3.0f);
//		RealRange geThree = RealRange.ge(3.0f);
		RealRange leThree = RealRange.le(3.0f);

//		System.out.println(emptyRange);
//		System.out.println(realRange);
//		System.out.println(ltThree);
//		System.out.println(gtThree);
//		System.out.println(geThree);
//		System.out.println(leThree);

		System.out.println(emptyRange.union(ltThree));
		System.out.println(realRange.union(ltThree));
		System.out.println(ltThree.union(realRange));
//		Assert.assertTrue(emptyRange.union(ltThree).compareTo(ltThree)==0);
		
		RealRange range2to3 = new RealRange(Closure.INCLUSIVE, 2.0f, 3.0f, Closure.INCLUSIVE);
		RealRange range3to5 = new RealRange(Closure.INCLUSIVE, 3.0f, 5.0f, Closure.INCLUSIVE);
		RealRange range6to8 = new RealRange(Closure.INCLUSIVE, 6.0f, 8.0f, Closure.INCLUSIVE);
		
		System.out.println(range2to3);
		System.out.println(range3to5);
		System.out.println(range6to8);
		System.out.println(range2to3.union(range3to5));
		System.out.println(range2to3.union(range3to5).union(range6to8));
		
		RealRange range3to4LeftOpen = new RealRange(Closure.EXCLUSIVE, 3.0f, 4.0f, Closure.INCLUSIVE);
		System.out.println(range3to4LeftOpen);
		System.out.println(range2to3.union(range3to4LeftOpen));
		
		RealRange range2to3RightOpen = new RealRange(Closure.INCLUSIVE, 2.0f, 3.0f, Closure.EXCLUSIVE);
		System.out.println(range2to3RightOpen);
		System.out.println(range2to3RightOpen.union(range3to4LeftOpen));
		System.out.println(range2to3RightOpen.intersection(range3to4LeftOpen));
		

		// Assert.assertTrue(ltThree.compareTo(gtThree) == -1);
		// Assert.assertTrue(gtThree.compareTo(ltThree) == 1);
		// Assert.assertTrue(gtThree.compareTo(gtThree) == 0);
		// Assert.assertTrue(ltThree.compareTo(geThree) == -1);
		// Assert.assertTrue(leThree.compareTo(geThree) == 0);
		//
		// Assert.assertTrue(ltThree.intersection(gtThree).isEmpty());
		// System.out.println();
	}

	private static Question createQuestionAnswers(String questionName, String... answerNames)
			throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException {
		Question question = new Question(questionName);
		question.setAnswerType(AnswerType.SINGLE_SELECTION_RADIO);

		for (String answerName : answerNames) {
			Answer answer = new Answer(answerName);
			question.addChild(answer);
		}

		return question;
	}
}
