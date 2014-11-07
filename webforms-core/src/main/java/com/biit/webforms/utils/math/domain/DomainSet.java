package com.biit.webforms.utils.math.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.webforms.persistence.entity.Question;

public abstract class DomainSet implements IDomain {

	protected final HashMap<Question, IDomainQuestion> domainQuestions;
	protected final HashSet<DomainSet> domainSets;

	public DomainSet() {
		domainQuestions = new HashMap<>();
		domainSets = new HashSet<>();
	}

	public DomainSet(HashMap<Question, IDomainQuestion> domainQuestions, HashSet<DomainSet> domainSets) {
		this.domainQuestions = domainQuestions;
		this.domainSets = domainSets;
	}

	public DomainSet(IDomain... domains) {
		domainQuestions = new HashMap<>();
		domainSets = new HashSet<>();
		for (IDomain domain : domains) {
			add(domain);
		}
	}

	public DomainSet(List<IDomain> domains) {
		domainQuestions = new HashMap<>();
		domainSets = new HashSet<>();
		for (IDomain domain : domains) {
			add(domain);
		}
	}

	public void add(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			domainQuestions.put(((IDomainQuestion) domain).getQuestion(), (IDomainQuestion) domain);
		} else {
			domainSets.add((DomainSet) domain);
		}
	}

	public List<IDomain> getDomains() {
		List<IDomain> allDomains = new ArrayList<>();
		allDomains.addAll(domainQuestions.values());
		allDomains.addAll(domainSets);
		return allDomains;
	}

	protected Collection<IDomainQuestion> getDomainQuestions() {
		return domainQuestions.values();
	}

	protected Set<DomainSet> getDomainsets() {
		return domainSets;
	}

	// public DomainSet(HashSet<IDomain> domainSets) {
	// this.domains = domainSets;
	// }
	//
	// public DomainSet(IDomain... domains) {
	// this.domains = new HashSet<>();
	// add(Arrays.asList(domains));
	// }
	//
	// protected void add(IDomain domain) {
	// this.domains.add(domain);
	// }
	//
	// protected void add(Collection<IDomain> domains) {
	// this.domains.addAll(domains);
	// }

	protected HashMap<Question, IDomainQuestion> getInverseDomainQuestions() {
		HashMap<Question, IDomainQuestion> inverseDomainQuestions = new HashMap<>();
		for (IDomainQuestion domain : domainQuestions.values()) {
			inverseDomainQuestions.put(domain.getQuestion(), (IDomainQuestion) domain.inverse());
		}
		return inverseDomainQuestions;
	}

	protected HashSet<DomainSet> getInverseDomainSet() {
		HashSet<DomainSet> inverseDomains = new HashSet<DomainSet>();
		for (DomainSet domain : domainSets) {
			inverseDomains.add((DomainSet) domain.inverse());
		}
		return inverseDomains;
	}

	protected static DomainSet intersection(DomainSetIntersection domainA, IDomainQuestion domainB) {
		DomainSetIntersection temp = new DomainSetIntersection(domainA.getDomains());

		if (!domainA.contains(domainB.getQuestion())) {
			// Doesn't contain, just add
			temp.add(domainB);
		} else {
			if (domainA.domainQuestions.containsKey(domainB.getQuestion())) {
				temp.add(domainA.domainQuestions.get(domainB.getQuestion()).intersect(domainB));
			} else {
				// Else intersect with containing minterm
				for (DomainSet domain : domainA.domainSets) {
					if (domain.contains(domainB.getQuestion())) {
						DomainSet newDomain = null;
						if (domain instanceof DomainSetIntersection) {
							newDomain = intersection((DomainSetIntersection) domain, domainB);
						} else {
							newDomain = intersection((DomainSetUnion) domain, domainB);
						}
						temp.domainSets.remove(domain);
						temp.domainSets.add(newDomain);
						break;
					}
				}
			}
		}
		return temp;
	}

	protected static DomainSet intersection(DomainSetUnion domainA, IDomainQuestion domainB) {

		List<IDomain> tempDomains = new ArrayList<IDomain>();
		for (IDomainQuestion domainQuestion : domainA.getDomainQuestions()) {
			tempDomains.add(domainQuestion.intersect(domainB));
		}
		for (DomainSet domainSet : domainA.getDomainsets()) {
			if (domainSet instanceof DomainSetUnion) {
				tempDomains.add(intersection((DomainSetUnion) domainSet, domainB));
			} else {
				tempDomains.add(intersection((DomainSetIntersection) domainSet, domainB));
			}
		}

		return new DomainSetUnion(tempDomains);
	}

	protected static DomainSet intersection(DomainSetIntersection domainA, DomainSetIntersection domainB) {
		System.out.println("Intersection I-I dA" + domainA + " dB" + domainB);
		DomainSet intersectionDomain = new DomainSetIntersection(domainA.getDomains());
		for (IDomainQuestion domain : domainB.getDomainQuestions()) {
			intersectionDomain = intersection((DomainSetIntersection) intersectionDomain, domain);
		}
		for (DomainSet domain : domainB.getDomainsets()) {
			intersectionDomain = (DomainSet) intersectionDomain.intersect(domain);
		}
		System.out.println("Result Intersection I-I " + intersectionDomain);
		return intersectionDomain;
	}

	protected static DomainSet intersection(DomainSetIntersection domainA, DomainSetUnion domainB) {
		System.out.println("Test-1 dA" + domainA + " dB" + domainB);
		List<IDomain> domains = new ArrayList<IDomain>();
		for (IDomainQuestion domainQuestion : domainB.getDomainQuestions()) {
			IDomain temp = intersection(domainA, domainQuestion);
			System.out.println("Temp-1:" + temp);
			domains.add(temp);
		}
		for (DomainSet domainSet : domainB.getDomainsets()) {
			IDomain temp;
			if (domainSet instanceof DomainSetIntersection) {
				temp = intersection(domainA, (DomainSetIntersection) domainSet);
			} else {
				temp = intersection(domainA, (DomainSetUnion) domainSet);
			}
			System.out.println("Temp-2: " + temp);
			domains.add(temp);
		}
		DomainSetUnion unionOfIntersections = new DomainSetUnion(domains);
		System.out.println("Temp-3: " + unionOfIntersections);
		return unionOfIntersections;
	}

	protected static DomainSet intersection(DomainSetUnion domainA, DomainSetUnion domain) {
		System.out.println("Intersection U-U not implemented yet");
		// TODO Auto-generated method stub
		return null;
	}

	protected static DomainSet intersection(DomainSetUnion domainA, DomainSetIntersection domainB) {
		return intersection(domainB, domainA);
	}

	protected static DomainSet union(DomainSetIntersection domainA, IDomainQuestion domainB) {
		System.out.println("Union I-Q");
		List<IDomain> unionDomains = new ArrayList<>();

		for (IDomain domain : domainA.getDomains()) {
			unionDomains.add(domain.union(domainB));
		}

		return new DomainSetIntersection(unionDomains);
	}

	protected static DomainSet union(DomainSetUnion domainA, IDomainQuestion domainB) {
		DomainSetUnion union = new DomainSetUnion(domainA.getDomains());
		
		if(!domainA.contains(domainB.getQuestion())){
			union.add(domainB);
		}else{
			union.add(union.getDomainQuestion(domainB.getQuestion()).union(domainB));
		}
		return union;
	}

	protected static DomainSet union(DomainSetIntersection domainA, DomainSetUnion domainB) {
		List<IDomain> unionDomains = new ArrayList<>();

		for (IDomain domain : domainA.getDomains()) {
			unionDomains.add(domain.union(domainB));
		}

		return new DomainSetIntersection(unionDomains);

		// DomainSet accumulation = new
		// DomainSetIntersection(domainA.getDomains());
		// for (IDomainQuestion domainQuestion : domainB.getDomainQuestions()) {
		// if (accumulation instanceof DomainSetUnion) {
		// accumulation = union((DomainSetUnion) accumulation, domainQuestion);
		// } else {
		// accumulation = union((DomainSetIntersection) accumulation,
		// domainQuestion);
		// }
		// }
		// for(DomainSet domainSet: domainB.getDomainsets()){
		// //TODO check later if this has to change to something more complex.
		// accumulation.add(domainSet);
		// }
		//
		// return accumulation;
	}

	protected static DomainSet union(DomainSetIntersection domainA, DomainSetIntersection domainB) {
		System.out.println("Union I-I");
		
		List<IDomain> unionDomain = new ArrayList<IDomain>();
		
		for(IDomain iDomain: domainA.getDomains()){
			for(IDomain jDomain: domainB.getDomains()){
				unionDomain.add(iDomain.union(jDomain));
			}
		}
		return new DomainSetIntersection(unionDomain);
	}

	protected static DomainSet union(DomainSetUnion domainA, DomainSetIntersection domainB) {
		System.out.println("Union U-I");
		return union(domainB, domainA);
	}

	protected static DomainSet union(DomainSetUnion domainA, DomainSetUnion domainB) {
		System.out.println("Union U-U");
		IDomain unionDomain = domainA;
		for(IDomain domain: domainB.getDomains()){
			unionDomain = unionDomain.union(domain);
		}
		
		System.out.println("Result "+unionDomain);
		return (DomainSet)unionDomain;
	}

	protected boolean contains(Question question) {
		if (domainQuestions.containsKey(question)) {
			return true;
		} else {
			for (DomainSet domain : domainSets) {
				if (domain.contains(question)) {
					return true;
				}
			}
			return false;
		}
	}
	
	protected IDomainQuestion getDomainQuestion(Question question){
		return domainQuestions.get(question);
	}
}
