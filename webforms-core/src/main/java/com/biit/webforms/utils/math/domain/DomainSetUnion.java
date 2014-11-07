package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.biit.webforms.persistence.entity.Question;

public class DomainSetUnion extends DomainSet {

	public DomainSetUnion(
			HashMap<Question, IDomainQuestion> inverseDomainQuestions,
			HashSet<DomainSet> inverseDomainSet) {
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
			return intersectDomain(domain);
		}
		if (domain instanceof DomainSetUnion) {
			return intersectDomain(domain);
		}
		if (domain instanceof DomainSetIntersection) {
			intersectDomainWithIntersection((DomainSetIntersection) domain);
		}
		return null;
	}

	public IDomain intersectDomainWithIntersection(DomainSetIntersection domain) {

		List<IDomain> intersectDomain = new ArrayList<IDomain>();
		intersectDomain.addAll(getDomains());

		for (IDomain iDomain : domain.getDomains()) {
			List<IDomain> accumDomain = new ArrayList<IDomain>();
			for (IDomain jDomain : intersectDomain) {
				accumDomain.add(jDomain.intersect(iDomain));
			}
			intersectDomain = accumDomain;
		}
		return new DomainSetUnion(intersectDomain);
	}

	public IDomain intersectDomain(IDomain... domain) {
		List<IDomain> intersectDomains = new ArrayList<>();
		for (IDomain iDomain : getDomains()) {
			for (IDomain jDomain : getDomains()) {
				intersectDomains.add(iDomain.intersect(jDomain));
			}
		}

		return new DomainSetUnion(intersectDomains);
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
			domainQuestions.put(domain.getQuestion(),
					(IDomainQuestion) domainQuestions.get(domain.getQuestion())
							.union(domain));
		} else {
			domainQuestions.put(domain.getQuestion(), domain);
		}
		return this;
	}

	@Override
	public IDomain inverse() {
		return new DomainSetIntersection(getInverseDomainQuestions(),
				getInverseDomainSet());
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
}
