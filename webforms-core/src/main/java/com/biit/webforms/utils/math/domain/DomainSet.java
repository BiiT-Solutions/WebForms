package com.biit.webforms.utils.math.domain;

import java.util.HashSet;

public abstract class DomainSet implements IDomain{

	private final HashSet<IDomain> domains;

	public DomainSet() {
		domains = new HashSet<IDomain>();
	}

	@Override
	public boolean isComplete() {
		for(IDomain domain: domains){
			if(!domain.isComplete()){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return domains.isEmpty();
	}
	
	public void add(IDomainQuestion domain){
		domains.add(domain);
	}
	
	public void add(HashSet<IDomain> domains) {
		domains.addAll(domains);
	}

	public HashSet<IDomain> getDomains() {
		return domains;
	}
}
