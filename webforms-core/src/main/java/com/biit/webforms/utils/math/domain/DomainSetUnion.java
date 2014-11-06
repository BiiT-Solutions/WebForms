package com.biit.webforms.utils.math.domain;

import java.util.HashSet;

import com.biit.webforms.utils.math.domain.exceptions.DifferentDomainQuestionOperationException;
import com.biit.webforms.utils.math.domain.exceptions.IncompatibleDomainException;

public class DomainSetUnion extends DomainSet{

	@Override
	public IDomain union(IDomain domain) throws IncompatibleDomainException,
			DifferentDomainQuestionOperationException {
		if(domain instanceof DomainSetUnion){
			add(((DomainSetUnion) domain).getDomains());
		}
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
		// TODO Auto-generated method stub
		return null;
	}

}
