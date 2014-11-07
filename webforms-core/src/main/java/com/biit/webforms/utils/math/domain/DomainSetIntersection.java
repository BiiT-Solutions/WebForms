package com.biit.webforms.utils.math.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.biit.webforms.persistence.entity.Question;

public class DomainSetIntersection extends DomainSet {

	public DomainSetIntersection(HashMap<Question, IDomainQuestion> domainQuestions, HashSet<DomainSet> domainSet) {
		super(domainQuestions, domainSet);
	}

	public DomainSetIntersection(List<IDomain> domains) {
		super(domains);
	}

	public DomainSetIntersection(IDomainQuestion... domains) {
		super(domains);
	}

	@Override
	public IDomain union(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			return union(this, (IDomainQuestion)domain);
		} else {
			if (domain instanceof DomainSetUnion) {
				return union(this, (DomainSetUnion) domain);
			} else {
				return union(this, (DomainSetIntersection)domain);
			}
		}
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
	public boolean isComplete() {
		for (IDomain domain : domainQuestions.values()) {
			if (!domain.isComplete()) {
				return false;
			}
		}
		for (IDomain domain : domainSets) {
			if (domain.isComplete()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		for (IDomainQuestion domainQuestion : domainQuestions.values()) {
			if (domainQuestion.isEmpty()) {
				return true;
			}
		}
		for (DomainSet domainSet : domainSets) {
			if (domainSet.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IDomain inverse() {
		return new DomainSetUnion(getInverseDomainQuestions(), getInverseDomainSet());
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
			sb.append(" && ");
			sb.append(itr1.next().toString());
		}

		if (!domainQuestions.isEmpty() && !domainSets.isEmpty()) {
			sb.append(" && ");
		}

		Iterator<DomainSet> itr2 = domainSets.iterator();
		if (itr2.hasNext()) {
			sb.append(itr2.next().toString());
		}
		while (itr2.hasNext()) {
			sb.append(" && ");
			sb.append(itr2.next().toString());
		}
		sb.append(")");
		return sb.toString();
	}
}
