package com.biit.webforms.utils.math.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.biit.webforms.persistence.entity.Question;

public class DomainSetUnion extends DomainSet {

	public DomainSetUnion(HashMap<Question, IDomainQuestion> inverseDomainQuestions, HashSet<DomainSet> inverseDomainSet) {
		super(inverseDomainQuestions, inverseDomainSet);
	}

	public DomainSetUnion(List<IDomain> domains) {
		super(domains);
	}

	public DomainSetUnion(IDomain... domains) {
		super(domains);
	}

	@Override
	public IDomain intersect(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			return intersection(this, (IDomainQuestion) domain);
		} else {
			if (domain instanceof DomainSetUnion) {
				return intersection(this, (DomainSetUnion) domain);
			} else {
				return intersection(this, (DomainSetIntersection) domain);
			}
		}
	}

	@Override
	public IDomain union(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			return union(this, (IDomainQuestion) domain);
		} else {
			if (domain instanceof DomainSetUnion) {
				return union(this, (DomainSetUnion) domain);
			} else {
				return union(this, (DomainSetIntersection) domain);
			}
		}
	}

	@Override
	public IDomain inverse() {
		return new DomainSetIntersection(getInverseDomainQuestions(), getInverseDomainSet());
	}

	@Override
	public boolean isComplete() {
		for (IDomain domain : domainQuestions.values()) {
			if (domain.isComplete()) {
				return true;
			}
		}
		for (IDomain domain : domainSets) {
			if (domain.isComplete()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isEmpty() {	
		for (IDomain domain : domainQuestions.values()) {
			if (domain.isEmpty()) {
				return true;
			}
		}
		for (IDomain domain : domainSets) {
			if (domain.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Iterator<IDomainQuestion> itr1 = domainQuestions.values().iterator();
		if (itr1.hasNext()) {
			sb.append(itr1.next().toString());
		}
		while (itr1.hasNext()) {
			sb.append(" || ");
			sb.append(itr1.next().toString());
		}

		if (!domainQuestions.isEmpty() && !domainSets.isEmpty()) {
			sb.append(" || ");
		}

		Iterator<DomainSet> itr2 = domainSets.iterator();
		if (itr2.hasNext()) {
			sb.append(itr2.next().toString());
		}
		while (itr2.hasNext()) {
			sb.append(" || ");
			sb.append(itr2.next().toString());
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public HashMap<Question, String> generateRandomValue() {		
		List<IDomain> domains = getDomains();
		if(domains== null || domains.isEmpty()){
			return new HashMap<Question, String>();
		}
		for(IDomain domain:domains){
			if(domain.isComplete()){
				continue;
			}
			return domain.generateRandomValue();
		}
		return null;
	}
}
