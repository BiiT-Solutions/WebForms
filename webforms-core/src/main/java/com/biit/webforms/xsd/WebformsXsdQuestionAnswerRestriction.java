package com.biit.webforms.xsd;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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
