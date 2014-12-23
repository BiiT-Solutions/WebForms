package com.biit.webforms.xsd;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.xml.XmlNodeProducer;

public class WebformsXsdQuestionAnswerRestriction extends XsdRestriction {

	private final Question question;

	public WebformsXsdQuestionAnswerRestriction(Question question) {
		super(getEnumerationAnswers(question));
		this.question = question;
		putBase(XsdElementType.STRING);
	}

	private static XmlNodeProducer[] getEnumerationAnswers(Question question) {
		List<WebformsXsdAnswerRestriction> restriction = new ArrayList<>(); 
		for(Answer answer: question.getFinalAnswers()){
			restriction.add(new WebformsXsdAnswerRestriction(answer));
		}
		return restriction.toArray(new WebformsXsdAnswerRestriction[]{});
	}

	public Question getQuestion() {
		return question;
	}

}
