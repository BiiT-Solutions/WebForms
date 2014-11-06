package com.biit.webforms.utils.math.domain;

import java.util.HashSet;

import com.biit.webforms.utils.math.domain.exceptions.DifferentDomainQuestionOperationException;
import com.biit.webforms.utils.math.domain.exceptions.IncompatibleDomainException;

public class DomainSetIntersection extends DomainSet{

	public DomainSetIntersection(HashSet<IDomainQuestion> inverseQuestionDomains, HashSet<DomainSet> inverseDomainSets) {
		super(inverseQuestionDomains,inverseDomainSets);
	}

	@Override
	public IDomain union(IDomain domain) throws IncompatibleDomainException,
			DifferentDomainQuestionOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDomain intersect(IDomain domain)
			throws DifferentDomainQuestionOperationException,
			IncompatibleDomainException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDomain inverse() {
		return new DomainSetUnion(getInverseQuestionDomains(),getInverseDomainSets());
	}

}
