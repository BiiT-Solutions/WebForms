package com.biit.webforms.utils.math.domain;

import java.util.HashSet;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDomainQuestionOperationException;
import com.biit.webforms.utils.math.domain.exceptions.IncompatibleDomainException;

public class DomainSetUnion extends DomainSet {

	public DomainSetUnion(HashSet<IDomainQuestion> questionDomains, HashSet<DomainSet> domainSets) {
		super(questionDomains, domainSets);
	}

	@Override
	public IDomain union(IDomain domain) throws IncompatibleDomainException, DifferentDomainQuestionOperationException {

		if (domain instanceof IDomainQuestion) {
			return unionDomainQuestion((IDomainQuestion) domain);
		}
		if (domain instanceof DomainSetUnion) {
			return unionDomainSet((DomainSet) domain);
		}
		if (domain instanceof DomainSetIntersection) {
			return unionDomainIntersection((DomainSetIntersection) domain);
		}
		
		return null;
	}

	private IDomain unionDomainIntersection(DomainSetIntersection domain) {
		// Union of the question domains is the union of the repeated elements
		// and the joining of question elements not contained
		for (Question question : domain.questionDomains.keySet()) {
			if (questionDomains.containsKey(question)) {
				IDomainQuestion unionOfQuestion;
				try {
					unionOfQuestion = (IDomainQuestion) questionDomains.get(question).union(
							domain.questionDomains.get(question));
					questionDomains.put(question, unionOfQuestion);
				} catch (IncompatibleDomainException | DifferentDomainQuestionOperationException e) {
					// Impossible
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
			} else {
				add(domain.questionDomains.get(question));
			}
		}
		//Union of the sub sets. its the sum of both sub sets
		add(domain.domainSets);
		
		return this;
	}
	
	@Override
	public IDomain intersect(IDomain domain) throws DifferentDomainQuestionOperationException,
			IncompatibleDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	private IDomain unionDomainSet(DomainSet domain) {
		domainSets.add(domain);
		return this;
	}

	private IDomain unionDomainQuestion(IDomainQuestion domain) {
		add(domain);
		return this;
	}

	@Override
	public IDomain inverse() {
		return new DomainSetIntersection(getInverseQuestionDomains(), getInverseDomainSets());
	}
}
