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
	
	public DomainSetIntersection(List<IDomain> domains){
		super(domains);
	}

	public DomainSetIntersection(IDomainQuestion... domains) {
		super(domains);
	}

	@Override
	public IDomain union(IDomain domain) {
		System.out.println("Domain set Intersection union... ");
		if (domain instanceof IDomainQuestion) {
			return new DomainSetUnion(this, domain);
		}
		if( domain instanceof DomainSetUnion){
			return new DomainSetUnion(this, domain);
		}
		if (domain instanceof DomainSetIntersection) {
			return new DomainSetUnion(this, domain);
		}
		return null;
	}

	@Override
	public IDomain intersect(IDomain domain) {
		System.out.println("Domain set Intersection intersection... ");
		if(domain instanceof IDomainQuestion){
			intersectDomainQuestion((IDomainQuestion) domain);
			return this;
		}
		if(domain instanceof DomainSetUnion){
			add(domain);
			return this;
		}
		if(domain instanceof DomainSetIntersection){
			return intersectionDomainSet((DomainSetIntersection) domain);
		}
		return null;
	}
	
	private IDomain intersectionDomainSet(DomainSetIntersection domain) {
		for (IDomainQuestion domainQuestion : domain.domainQuestions.values()) {
			intersectDomainQuestion(domainQuestion);
		}
		for (DomainSet domainSet : domain.domainSets) {
			domainSet.add(domainSet);
		}
		return this;
	}
	
	private IDomain intersectDomainQuestion(IDomainQuestion domain) {
		if(domainQuestions.containsKey(domain.getQuestion())){
			domainQuestions.put(domain.getQuestion(), (IDomainQuestion) domainQuestions.get(domain).intersect(domain));
		}else{
			domainQuestions.put(domain.getQuestion(), domain);
		}
		return this;
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
		
		if(!domainQuestions.isEmpty() && !domainSets.isEmpty()){
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
