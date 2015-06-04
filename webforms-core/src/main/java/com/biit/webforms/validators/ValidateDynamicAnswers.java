package com.biit.webforms.validators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.utils.validation.SimpleValidator;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.validators.reports.DynamicAnswerNullReference;
import com.biit.webforms.validators.reports.DynamicAnswerReferenceInvalid;
import com.biit.webforms.validators.reports.MultipleDynamicAnswersReferenceTheSameQuestion;

public class ValidateDynamicAnswers extends SimpleValidator<TreeObject> {

	public ValidateDynamicAnswers() {
		super(TreeObject.class);
	}

	@Override
	protected void validateImplementation(TreeObject element) {
		if (element instanceof Question) {
			List<TreeObject> dynamicAnswers = ((Question)element).getChildren(DynamicAnswer.class);
			Set<Question> references = new HashSet<>();
			boolean failed = false;
			for(TreeObject dynamicAnswer: dynamicAnswers){
				Question currentQuestion = (Question) element;
				DynamicAnswer currentDynamicAnswer = (DynamicAnswer) dynamicAnswer;
				
				assertTrue(currentDynamicAnswer.getReference()!=null , new DynamicAnswerNullReference(currentQuestion));
				if(currentDynamicAnswer.getReference()!=null){
					assertTrue(currentQuestion.compareTo(currentDynamicAnswer.getReference())>0 , new DynamicAnswerReferenceInvalid(currentQuestion, currentDynamicAnswer.getReference()));
					
					if(references.contains(currentDynamicAnswer.getReference()) && !failed){
						failed = true;
						assertFalse(true, new MultipleDynamicAnswersReferenceTheSameQuestion(currentQuestion,currentDynamicAnswer));
					}
					references.add(currentDynamicAnswer.getReference());
				}				
			}
		} else {
			for (TreeObject child : element.getChildren()) {
				validateImplementation(child);
			}
		}
	}

}
