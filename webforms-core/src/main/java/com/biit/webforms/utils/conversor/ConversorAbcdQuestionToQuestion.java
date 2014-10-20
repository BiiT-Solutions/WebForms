package com.biit.webforms.utils.conversor;

import com.biit.abcd.persistence.entity.Question;
import com.biit.form.BaseAnswer;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.webforms.logger.WebformsLogger;

public class ConversorAbcdQuestionToQuestion extends
		ConversorTreeObject<Question, com.biit.webforms.persistence.entity.Question> {

	private ConversorAnswerTypeAbcdToAnswerType conversorAnswerType = new ConversorAnswerTypeAbcdToAnswerType();
	private ConversorAnswerFormatAbcdToAnswerFormat conversorAnswerFormat = new ConversorAnswerFormatAbcdToAnswerFormat();
	private ConversorBaseAnswerToAnswer conversorAnswer = new ConversorBaseAnswerToAnswer();
	
	@Override
	public com.biit.webforms.persistence.entity.Question createDestinyInstance() {
		return new com.biit.webforms.persistence.entity.Question();
	}

	@Override
	public void copyData(Question origin, com.biit.webforms.persistence.entity.Question destiny) {
		//Copy base data
		super.copyData(origin, destiny);

		//Convert and copy answer type and format.
		destiny.setAnswerType(conversorAnswerType.convert(origin.getAnswerType()));
		try {
			destiny.setAnswerFormat(conversorAnswerFormat.convert(origin.getAnswerFormat()));
		} catch (InvalidAnswerFormatException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		
		//Convert and assign children
		for(TreeObject child: origin.getChildren()){
			if(child instanceof BaseAnswer){
				BaseAnswer convertedChild = conversorAnswer.convert((BaseAnswer) child);
				try {
					destiny.addChild(convertedChild);
				} catch (NotValidChildException e) {
					// Impossible
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
			}else{
				WebformsLogger.errorMessage(this.getClass().getName(), new Throwable("Child type not expected"));
			}
		}
	}
}
