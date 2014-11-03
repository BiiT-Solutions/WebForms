package com.biit.webforms.utils.math.domain;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Question;

@Test(groups = { "testDomain" })
public class TestDomain {

	@Test
	public void testDiscreteDomain() {
		System.out.println("testDiscreteDomain");

		DiscreteSet<String> stringDomain = new DiscreteSet<String>();
		stringDomain.add("A", "B", "C", "D");

		System.out.println(stringDomain);
	}

	@Test
	public void testQuestionAnswerDomain() throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, ChildrenNotFoundException {
		System.out.println("testQuestionAnswerDomain");

		Question testQuestion1 = createQuestionAnswers("testQuestion1", "A", "B", "C", "D");
		Answer answerA = (Answer) testQuestion1.getChild(0);
		Answer answerB = (Answer) testQuestion1.getChild(1);
		Answer answerC = (Answer) testQuestion1.getChild(2);
		Answer answerD = (Answer) testQuestion1.getChild(3);
		
		QuestionAnswerDomain questionAnswerDomain = new QuestionAnswerDomain(testQuestion1);
		QuestionAnswerDomain inverseDomain = questionAnswerDomain.inverse();
		
		Assert.assertTrue(questionAnswerDomain.getValue().isEmpty());
		Assert.assertTrue(questionAnswerDomain.isEmpty());
		Assert.assertFalse(inverseDomain.getValue().isEmpty());
		Assert.assertTrue(inverseDomain.isComplete());
		
		questionAnswerDomain.addValue(answerA);
		Assert.assertTrue(questionAnswerDomain.contains(answerA));
		Assert.assertFalse(questionAnswerDomain.contains(answerB));
		Assert.assertFalse(questionAnswerDomain.contains(answerC));
		Assert.assertFalse(questionAnswerDomain.contains(answerD));
		Assert.assertFalse(questionAnswerDomain.isComplete());
		
		QuestionAnswerDomain testDomainA = new QuestionAnswerDomain(testQuestion1);
		testDomainA.addValue(answerA);
		QuestionAnswerDomain testDomainBCD = new QuestionAnswerDomain(testQuestion1);
		testDomainBCD.addValue(answerB);
		testDomainBCD.addValue(answerC);
		testDomainBCD.addValue(answerD);
		QuestionAnswerDomain testDomainABC = new QuestionAnswerDomain(testQuestion1);
		testDomainABC.addValue(answerA);
		testDomainABC.addValue(answerB);
		testDomainABC.addValue(answerC);
		
		Assert.assertTrue(testDomainA.union(testDomainBCD).isComplete());
		Assert.assertTrue(testDomainA.intersect(testDomainBCD).isEmpty());
		Assert.assertFalse(testDomainA.union(testDomainABC).isComplete());
		Assert.assertTrue(testDomainA.union(testDomainABC).getValue().size()==3);
		Assert.assertFalse(testDomainA.intersect(testDomainABC).isEmpty());
		Assert.assertTrue(testDomainA.intersect(testDomainABC).getValue().size()==1);
	}
	
	@Test
	public void testRealRange(){
		RealRange realRange = new RealRange();
		RealRange ltThree = RealRange.lt(3.0f);
		RealRange gtThree = RealRange.gt(3.0f);
		RealRange geThree = RealRange.ge(3.0f);
		RealRange leThree = RealRange.le(3.0f);
		
		Assert.assertTrue(ltThree.compareTo(gtThree)==-1);
		Assert.assertTrue(gtThree.compareTo(ltThree)==1);
		Assert.assertTrue(gtThree.compareTo(gtThree)==0);
		Assert.assertTrue(ltThree.compareTo(geThree)==-1);
		Assert.assertTrue(leThree.compareTo(geThree)==0);
		
		Assert.assertTrue(ltThree.intersection(gtThree).isEmpty());
		System.out.println();
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
