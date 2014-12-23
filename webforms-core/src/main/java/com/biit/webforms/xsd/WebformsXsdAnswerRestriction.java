package com.biit.webforms.xsd;

import com.biit.webforms.persistence.entity.Answer;

public class WebformsXsdAnswerRestriction extends XsdEnumeration{

	public WebformsXsdAnswerRestriction(Answer answer) {
		super(answer.getName());
	}

}
