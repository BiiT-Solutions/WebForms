package com.biit.webforms.utils.math.domain;

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

	public DomainSet(IDomain ... domains) {
		domainQuestions = new HashMap<>();
		domainSets = new HashSet<>();
		for(IDomain domain: domains){
			add(domain);
		}
	}

	public DomainSet(List<IDomain> domains) {
		domainQuestions = new HashMap<>();
		domainSets = new HashSet<>();
		for(IDomain domain: domains){
			add(domain);
		}
	}

	public void add(IDomain domain) {
		if (domain instanceof IDomainQuestion) {
			domainQuestions.put(((IDomainQuestion) domain).getQuestion(), (IDomainQuestion) domain);
		}else{
			domainSets.add((DomainSet) domain);
		}
	}
	
	public Set<IDomain> getDomains(){
		Set<IDomain> allDomains = new HashSet<>();
		allDomains.addAll(domainQuestions.values());
		allDomains.addAll(domainSets);
		return allDomains;
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
	
	protected HashMap<Question, IDomainQuestion> getInverseDomainQuestions(){
		HashMap<Question, IDomainQuestion> inverseDomainQuestions = new HashMap<>();
		for(IDomainQuestion domain : domainQuestions.values()){
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
}
