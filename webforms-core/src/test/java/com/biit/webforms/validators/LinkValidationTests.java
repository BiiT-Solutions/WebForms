package com.biit.webforms.validators;

import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@Test(groups = { "linkValidation" })
public class LinkValidationTests {

	@Test
	public void test() throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException{
		
		System.out.println("link validation test");
		

		System.out.println("Test formed correctly.");
	}
}
