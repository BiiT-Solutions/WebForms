package com.biit.webforms.utils.math.domain;

import java.util.HashMap;
import java.util.HashSet;

import com.biit.webforms.persistence.entity.Question;

public abstract class DomainSet implements IDomain {

	protected final HashMap<Question, IDomainQuestion> questionDomains;
	protected final HashSet<DomainSet> domainSets;

	public DomainSet() {
		questionDomains = new HashMap<Question, IDomainQuestion>();
		domainSets = new HashSet<DomainSet>();
	}
	
	public DomainSet(HashSet<IDomainQuestion> questionDomains, HashSet<DomainSet> domainSets) {
		this.questionDomains = new HashMap<>(); 
		this.domainSets = domainSets;
		for(IDomainQuestion questionDomain: questionDomains){
			add(questionDomain);
		}
	}
	
	protected void add(IDomainQuestion questionDomain){
		questionDomains.put(questionDomain.getQuestion(), questionDomain);
	}
	
	protected void add(DomainSet domainSet){
		domainSets.add(domainSet);
	}
	
	protected void add(HashSet<DomainSet> domainSets){
		domainSets.addAll(domainSets);
	}

	@Override
	public boolean isComplete() {
		for (IDomainQuestion questionDomain : questionDomains.values()) {
			if (!questionDomain.isComplete()) {
				return false;
			}
		}
		for (DomainSet domainSet : domainSets){
			if(!domainSet.isComplete()){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		for (IDomainQuestion questionDomain : questionDomains.values()) {
			if (!questionDomain.isEmpty()) {
				return false;
			}
		}
		for (DomainSet domainSet : domainSets){
			if(!domainSet.isEmpty()){
				return false;
			}
		}
		return true;
	}
	
	protected HashSet<IDomainQuestion> getInverseQuestionDomains(){
		HashSet<IDomainQuestion> inverseQuestionDomains = new HashSet<IDomainQuestion>();
		for(IDomainQuestion questionDomain: questionDomains.values()){
			inverseQuestionDomains.add((IDomainQuestion)questionDomain.inverse());
		}
		return inverseQuestionDomains;
	}
	
	protected HashSet<DomainSet> getInverseDomainSets(){
		HashSet<DomainSet> inverseDomainSets = new HashSet<>();
		for(DomainSet domainSet: domainSets){
			inverseDomainSets.add((DomainSet) domainSet.inverse());
		}
		return inverseDomainSets;
	}
}
