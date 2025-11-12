package com.biit.webforms.utils.math.domain;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Expression parser)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Domain for questions with answers. The complete domain is formed by all the
 * possible answers of a question. In case of subanswers the domain is formed by
 * all possible final answers (All subanswer or answers without children)
 *
 */
public class QuestionAnswerDomain implements IDomainQuestion {

	private final WebformsBaseQuestion question;
	private final DiscreteSet<Answer> completeDomain;
	private DiscreteSet<Answer> value;

	public QuestionAnswerDomain(WebformsBaseQuestion question) {
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

	private void initializeCompleteDomain(WebformsBaseQuestion question) {
		if (question instanceof Question) {
			LinkedHashSet<Answer> finalAnswers = ((Question) question).getFinalAnswers();
			for (Answer finalAnswer : finalAnswers) {
				completeDomain.add(finalAnswer);
			}
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
	 */
	@Override
	public IDomain union(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			if (this.question.equals(((IDomainQuestion) domain).getQuestion())) {
				return unionSameQuestion((QuestionAnswerDomain) domain);
			} else {
				return new DomainSetUnion(this, (IDomainQuestion) domain);
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
	 */
	public IDomain intersect(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			if (this.question.equals(((IDomainQuestion) domain).getQuestion())) {
				return intersectSameQuestion((QuestionAnswerDomain) domain);
			} else {
				return new DomainSetIntersection(this, (IDomainQuestion) domain);
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

	@Override
	public WebformsBaseQuestion getQuestion() {
		return question;
	}

	@Override
	public HashMap<WebformsBaseQuestion, String> generateRandomValue() {
		HashMap<WebformsBaseQuestion, String> randomValue = new HashMap<>();
		if (isEmpty()) {
			return randomValue;
		}
		randomValue.put(question, value.getRandomElement().toString());
		return randomValue;
	}

}
