package com.biit.webforms.utils.math.domain;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Expression parser)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.biit.webforms.persistence.entity.WebformsBaseQuestion;

/**
 * Specification of the set of domains. In this specification the result is the
 * logic intersection (AND) of the different domains.
 *
 */
public class DomainSetIntersection extends DomainSet {

	public DomainSetIntersection(HashMap<WebformsBaseQuestion, IDomainQuestion> domainQuestions, HashSet<DomainSet> domainSet) {
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
			if (!domain.isComplete()) {
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

	@Override
	public HashMap<WebformsBaseQuestion, String> generateRandomValue() {
		HashMap<WebformsBaseQuestion, String> randomValue = new HashMap<WebformsBaseQuestion, String>();

		for (IDomain domain : getDomains()) {
			randomValue.putAll(domain.generateRandomValue());
		}

		return randomValue;
	}
}
