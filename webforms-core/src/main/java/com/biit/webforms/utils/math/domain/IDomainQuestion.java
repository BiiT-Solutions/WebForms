package com.biit.webforms.utils.math.domain;

import com.biit.webforms.persistence.entity.WebformsBaseQuestion;

/**
 * Interface for domains that reference a question.
 *
 */
public interface IDomainQuestion extends IDomain {

	public WebformsBaseQuestion getQuestion();

}
