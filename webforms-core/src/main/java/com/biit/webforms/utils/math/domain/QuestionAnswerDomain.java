package com.biit.webforms.utils.math.domain;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDomainQuestionOperationException;
import com.biit.webforms.utils.math.domain.exceptions.IncompatibleDomainException;

public class QuestionAnswerDomain implements IDomainQuestion {

	private final Question question;
	private final DiscreteSet<Answer> completeDomain;
	private DiscreteSet<Answer> value;

	public QuestionAnswerDomain(Question question) {
		this.question = question;
		this.completeDomain = new DiscreteSet<>();
		this.value = new DiscreteSet<>();
		initializeCompleteDomain(question);
	}

	public QuestionAnswerDomain(TokenComparationAnswer tokenQuestionAnswer) {
		this.question = tokenQuestionAnswer.getQuestion();
		this.completeDomain = new DiscreteSet<>();
		this.value = new DiscreteSet<>();
		initializeCompleteDomain(question);
		setValue(tokenQuestionAnswer);
	}

	private void initializeCompleteDomain(Question question) {
		LinkedHashSet<Answer> finalAnswers = question.getFinalAnswers();
		for (Answer finalAnswer : finalAnswers) {
			completeDomain.add(finalAnswer);
		}
	}

	@Override
	public String toString() {
		return question + " " + value;
	}

	public void addValue(Answer answer) {
		value.add(answer);
	}

	public void setValue(DiscreteSet<Answer> value) {
		this.value = value;
	}

	private void setValue(TokenComparationAnswer tokenQuestionAnswer) {
		if (tokenQuestionAnswer.getType() == TokenTypes.EQ) {
			// EQ
			addValue(tokenQuestionAnswer.getAnswer());
		} else {
			// NE
			Set<Answer> answers = new HashSet<>();
			answers.add(tokenQuestionAnswer.getAnswer());

			setValue(completeDomain.difference(answers));
		}
	}

	public Set<Answer> getValue() {
		return value.getDomainMembers();
	}

	public Set<Answer> getInverseValue() {
		Set<Answer> inverseValue = new HashSet<Answer>();
		for (Answer domainAnswer : completeDomain.getDomainMembers()) {
			if (!value.contains(domainAnswer)) {
				inverseValue.add(domainAnswer);
			}
		}
		return inverseValue;
	}

	/**
	 * Returns a QuestionAnswerDomain with the inverse of the current domain
	 * value.
	 * 
	 * @return
	 */
	public QuestionAnswerDomain inverse() {
		QuestionAnswerDomain inverseDomain = new QuestionAnswerDomain(question);

		for (Answer answer : getInverseValue()) {
			inverseDomain.addValue(answer);
		}

		return inverseDomain;
	}

	public boolean contains(Answer answer) {
		return value.contains(answer);
	}

	/**
	 * Returns the true if current value set is equal to the whole dominion
	 * 
	 * @return
	 */
	public boolean isComplete() {
		return (completeDomain.intersection(value)).equals(completeDomain);
	}

	/**
	 * Returns true if the domain doesn't have value.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (completeDomain.intersection(value)).isEmpty();
	}

	/**
	 * Joins two QuestionAnswerDomains and returns a new QuestionAnswerDomain
	 * with the result of the operation. If the domains are not compatible (Not
	 * the same question) a null value is returned.
	 * 
	 * @param domain
	 * @return
	 * @throws IncompatibleDomainException
	 * @throws DifferentDomainQuestionOperationException
	 */
	@Override
	public IDomain union(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			if (this.question.equals(((IDomainQuestion) domain).getQuestion())) {
				return unionSameQuestion((QuestionAnswerDomain) domain);				
			}else{
				return new DomainSetUnion(this, (IDomainQuestion)domain);
			}
		}
		if (domain instanceof DomainSet) {
			return domain.union(this);
		}
		return null;
	}

	public IDomain unionSameQuestion(QuestionAnswerDomain domain) {
		QuestionAnswerDomain newDomain = new QuestionAnswerDomain(question);
		for (Answer answer : getValue()) {
			newDomain.addValue(answer);

		}
		for (Answer answer : domain.getValue()) {
			newDomain.addValue(answer);
		}
		return newDomain;
	}

	/**
	 * Intersects two QuestionAnswerDomains and returs a new
	 * QuestionAnswerDomain with the result of the operation. If the domains are
	 * not compatible (Not the same question) a null value is returned.
	 * 
	 * @param domain
	 * @return
	 * @throws DifferentDomainQuestionOperationException
	 * @throws IncompatibleDomainException
	 */
	public IDomain intersect(IDomain domain){
		if (domain instanceof IDomainQuestion) {
			if (this.question.equals(((IDomainQuestion) domain).getQuestion())) {
				return intersectSameQuestion((QuestionAnswerDomain) domain);				
			}else{
				return new DomainSetIntersection(this, (IDomainQuestion)domain);
			}
		}
		if (domain instanceof DomainSet) {
			return domain.intersect(this);
		}
		return null;
	}
	
	public IDomain intersectSameQuestion(QuestionAnswerDomain domain) {
		QuestionAnswerDomain newDomain = new QuestionAnswerDomain(question);

		for (Answer answer : domain.getValue()) {
			if (contains(answer)) {
				newDomain.addValue(answer);
			}
		}
		return newDomain;
	}

	public Question getQuestion() {
		return question;
	}

}
