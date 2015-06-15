package com.biit.webforms.persistence;

import junit.framework.Assert;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "formReference" })
public class FormReference extends AbstractTransactionalTestNGSpringContextTests {
	private final static String CATEGORY = "RealCategory";

	public void categoriesAdded() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException,
			FlowWithoutSourceException, FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException,
			FlowWithoutDestinyException, NotValidTokenType, ElementIsReadOnly, FlowNotAllowedException {
		Form linkedForm = FormUtils.createCompleteForm();
		
		Form form = new Form();
		form.addChild(FormUtils.createCategory(CATEGORY));
		form.setFormReference(linkedForm);
		CompleteFormView completeFormView = new CompleteFormView(form);
		
		//Two linked categories, plus one.
		Assert.assertEquals(3, completeFormView.getChildren().size());
				
		//First always the linked categories.
		Assert.assertEquals(true, completeFormView.getChildren().get(0).isReadOnly());
		Assert.assertEquals(true, completeFormView.getChildren().get(1).isReadOnly());
		Assert.assertEquals(true, completeFormView.getChildren().get(1).getChildren().get(0).isReadOnly());
		Assert.assertEquals(false, completeFormView.getChildren().get(2).isReadOnly());
		Assert.assertEquals(CATEGORY, completeFormView.getChildren().get(2).getName());
	}
}
