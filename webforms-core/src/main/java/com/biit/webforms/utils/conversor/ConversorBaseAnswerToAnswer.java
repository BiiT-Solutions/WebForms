package com.biit.webforms.utils.conversor;

import com.biit.form.BaseAnswer;
import com.biit.webforms.persistence.entity.Answer;

public class ConversorBaseAnswerToAnswer extends ConversorTreeObject<BaseAnswer, Answer>{

	@Override
	public Answer createDestinyInstance() {
		return new Answer();
	}
}
