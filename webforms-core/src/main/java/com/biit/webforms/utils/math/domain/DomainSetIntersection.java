package com.biit.webforms.utils.math.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.biit.webforms.persistence.entity.Question;

public class DomainSetIntersection extends DomainSet {

	public DomainSetIntersection(HashMap<Question, IDomainQuestion> domainQuestions, HashSet<DomainSet> domainSet) {
		super(domainQuestions, domainSet);
	}

	public DomainSetIntersection(IDomainQuestion... domains) {
		super(domains);
	}

	@Override
	public IDomain union(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			return intersecDomainQuestion((IDomainQuestion) domain);
		}
		if (domain instanceof DomainSetIntersection) {
			return intersecDomainSet((DomainSetIntersection) domain);
		}
		// if Domain set union
		System.out.println("Here3");
		return null;
	}

	private IDomain intersecDomainQuestion(IDomainQuestion domain) {
		if (domainQuestions.containsKey(domain.getQuestion())) {
			domainQuestions.put(domain.getQuestion(), (IDomainQuestion) domainQuestions.get(domain.getQuestion())
					.intersect(domain));
		} else {
			domainQuestions.put(domain.getQuestion(), domain);
		}
		return this;
	}
	
	private IDomain intersecDomainSet(DomainSetIntersection domain) {
		for (IDomainQuestion domainQuestion : domain.domainQuestions.values()) {
			intersecDomainQuestion(domainQuestion);
		}
		for (DomainSet domainSet : domain.domainSets) {
			domainSet.add(domainSet);
		}
		return this;
	}

	@Override
	public IDomain intersect(IDomain domain) {
		System.out.println("Here2");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
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
