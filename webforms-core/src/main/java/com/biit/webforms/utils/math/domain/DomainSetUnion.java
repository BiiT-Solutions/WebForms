package com.biit.webforms.utils.math.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.biit.webforms.persistence.entity.Question;

public class DomainSetUnion extends DomainSet {

	public DomainSetUnion(HashMap<Question, IDomainQuestion> inverseDomainQuestions, HashSet<DomainSet> inverseDomainSet) {
		super(inverseDomainQuestions, inverseDomainSet);
	}

	public DomainSetUnion(IDomainQuestion... domains) {
		super(domains);
	}

	@Override
	public IDomain union(IDomain domain) {

		if (domain instanceof IDomainQuestion) {
			return unionDomainQuestion((IDomainQuestion) domain);
		}
		if (domain instanceof DomainSetUnion) {
			return unionDomainSet((DomainSetUnion) domain);
		}
		if (domain instanceof DomainSetIntersection) {
			add(domain);
		}

		return null;
	}

	private IDomain unionDomainSet(DomainSetUnion domain) {
		for (IDomainQuestion domainQuestion : domain.domainQuestions.values()) {
			unionDomainQuestion(domainQuestion);
		}
		for (DomainSet domainSet : domain.domainSets) {
			domainSet.add(domainSet);
		}
		return this;
	}

	private IDomain unionDomainQuestion(IDomainQuestion domain) {
		if (domainQuestions.containsKey(domain.getQuestion())) {
			domainQuestions.put(domain.getQuestion(), (IDomainQuestion) domainQuestions.get(domain.getQuestion())
					.union(domain));
		} else {
			domainQuestions.put(domain.getQuestion(), domain);
		}
		return this;
	}

	@Override
	public IDomain inverse() {
		return new DomainSetIntersection(getInverseDomainQuestions(), getInverseDomainSet());
	}

	@Override
	public boolean isComplete() {
		for (IDomain domain : domainQuestions.values()) {
			if (!domain.isComplete()) {
				return false;
			}
		}
		for (IDomain domain : domainSets) {
			if (!domain.isComplete()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		for (IDomain domain : domainQuestions.values()) {
			if (!domain.isEmpty()) {
				return false;
			}
		}
		for (IDomain domain : domainSets) {
			if (!domain.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public IDomain intersect(IDomain domain) {
		System.out.println("Here1");
		// TODO Auto-generated method stub
		return null;
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
}
