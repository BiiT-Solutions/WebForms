package com.biit.webforms.persistence.entity.condition;

import com.biit.webforms.persistence.entity.WebformsBaseQuestion;

public interface ITokenQuestion {

	public WebformsBaseQuestion getQuestion();

	public boolean evaluate();

}
