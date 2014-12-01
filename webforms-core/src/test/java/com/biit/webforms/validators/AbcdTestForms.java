package com.biit.webforms.validators;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;


public class AbcdTestForms {

	public static Form generateTestForm1() throws FieldTooLongException, InvalidAnswerFormatException, CharacterNotAllowedException, NotValidChildException{
		Form form = new Form();
		form.setLabel("test_1");
		Category el_1  = new Category();
		el_1.setName("personal");
		el_1.setLabel("Personal");
		Question el_2  = new Question();
		el_2.setName("name");
		el_2.setLabel("name");
		el_2.setAnswerType(AnswerType.INPUT);
		el_2.setAnswerFormat(AnswerFormat.TEXT);
		//cat
		el_1.addChild(el_2);
		Question el_3  = new Question();
		el_3.setName("surname");
		el_3.setLabel("Surname");
		el_3.setAnswerType(AnswerType.INPUT);
		el_3.setAnswerFormat(AnswerFormat.TEXT);
		//cat
		el_1.addChild(el_3);
		Question el_4  = new Question();
		el_4.setName("Age");
		el_4.setLabel("age");
		el_4.setAnswerType(AnswerType.INPUT);
		el_4.setAnswerFormat(AnswerFormat.NUMBER);
		//cat
		el_1.addChild(el_4);
		Question el_5  = new Question();
		el_5.setName("birthdate");
		el_5.setLabel("Birtdate");
		el_5.setAnswerType(AnswerType.INPUT);
		el_5.setAnswerFormat(AnswerFormat.DATE);
		//cat
		el_1.addChild(el_5);
		//form
		form.addChild(el_1);
		Category el_6  = new Category();
		el_6.setName("Family");
		el_6.setLabel("Category");
		Question el_7  = new Question();
		el_7.setName("childs");
		el_7.setLabel("Childs");
		el_7.setAnswerType(AnswerType.RADIO);
		Answer el_8  = new Answer();
		el_8.setName("yes");
		el_8.setLabel("yes");
		//ques
		el_7.addChild(el_8);
		Answer el_9  = new Answer();
		el_9.setName("no");
		el_9.setLabel("No");
		//ques
		el_7.addChild(el_9);
		//cat
		el_6.addChild(el_7);

		//No info text on Abcd
		
		Group el_11  = new Group();
		el_11.setName("child_personal");
		el_11.setLabel("Child personal");
		el_11.setRepeatable(false);
		Question el_12  = new Question();
		el_12.setName("name");
		el_12.setLabel("Name");
		el_12.setAnswerType(AnswerType.INPUT);
		el_12.setAnswerFormat(AnswerFormat.TEXT);
		//group
		el_11.addChild(el_12);
		Question el_13  = new Question();
		el_13.setName("surname");
		el_13.setLabel("Surname");
		el_13.setAnswerType(AnswerType.INPUT);
		el_13.setAnswerFormat(AnswerFormat.TEXT);
		//group
		el_11.addChild(el_13);
		Question el_14  = new Question();
		el_14.setName("age");
		el_14.setLabel("Age");
		el_14.setAnswerType(AnswerType.INPUT);
		el_14.setAnswerFormat(AnswerFormat.TEXT);
		//group
		el_11.addChild(el_14);
		Question el_15  = new Question();
		el_15.setName("birthdate");
		el_15.setLabel("Birthdate");
		el_15.setAnswerType(AnswerType.INPUT);
		el_15.setAnswerFormat(AnswerFormat.TEXT);
		//group
		el_11.addChild(el_15);
		Question el_16  = new Question();
		el_16.setName("adopted");
		el_16.setLabel("Is Adopted?");
		el_16.setAnswerType(AnswerType.INPUT);
		el_16.setAnswerFormat(AnswerFormat.TEXT);
		//group
		el_11.addChild(el_16);
		Question el_17  = new Question();
		el_17.setName("adopted_country");
		el_17.setLabel("In which country?");
		el_17.setAnswerType(AnswerType.RADIO);
		Answer el_18  = new Answer();
		el_18.setName("maybe.original.country");
		el_18.setLabel("Answer");
		//ques
		el_17.addChild(el_18);
		Answer el_19  = new Answer();
		el_19.setName("maybe.china");
		el_19.setLabel("Answer2");
		//ques
		el_17.addChild(el_19);
		Answer el_20  = new Answer();
		el_20.setName("other.countries");
		el_20.setLabel("Answer2");
		//ques
		el_17.addChild(el_20);
		//group
		el_11.addChild(el_17);
		Question el_21  = new Question();
		el_21.setName("another");
		el_21.setLabel("Another");
		el_21.setAnswerType(AnswerType.MULTI_CHECKBOX);
		Answer el_22  = new Answer();
		el_22.setName("a");
		el_22.setLabel("A");
		//ques
		el_21.addChild(el_22);
		Answer el_23  = new Answer();
		el_23.setName("b");
		el_23.setLabel("B");
		//ques
		el_21.addChild(el_23);
		//group
		el_11.addChild(el_21);
		//cat
		el_6.addChild(el_11);
		//form
		form.addChild(el_6);
		
		return form;
	}
	
}
