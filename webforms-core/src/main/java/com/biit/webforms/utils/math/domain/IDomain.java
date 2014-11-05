package com.biit.webforms.utils.math.domain;

import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDomainQuestionOperationException;
import com.biit.webforms.utils.math.domain.exceptions.IncompatibleDomainException;

public interface IDomain {

	public Question getQuestion();

	public boolean isComplete();

	public IDomain union(IDomain domain) throws IncompatibleDomainException, DifferentDomainQuestionOperationException;

	public IDomain intersect(IDomain domain) throws DifferentDomainQuestionOperationException, IncompatibleDomainException;

	public IDomain inverse();

	public boolean isEmpty();
}
