package com.biit.webforms.validators;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.DependencyExistException;
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
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@Test(groups = { "linkValidation" })
public class LinkValidationTests {

	public Form generateTestForm1() throws FieldTooLongException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidChildException, InvalidAnswerSubformatException {
		Form form = new Form();
		form.setLabel("test_1");
		form.setDescription("");
		Category el_1 = new Category();
		el_1.setName("personal");
		el_1.setLabel("Personal");
		Question el_2 = new Question();
		el_2.setName("name");
		el_2.setLabel("name");
		el_2.setAnswerType(AnswerType.INPUT);
		el_2.setAnswerFormat(AnswerFormat.TEXT);
		el_2.setAnswerSubformat(AnswerSubformat.TEXT);
		el_2.setHorizontal(false);
		el_2.setMandatory(true);
		// cat
		el_1.addChild(el_2);
		Question el_3 = new Question();
		el_3.setName("surname");
		el_3.setLabel("Surname");
		el_3.setAnswerType(AnswerType.INPUT);
		el_3.setAnswerFormat(AnswerFormat.TEXT);
		el_3.setAnswerSubformat(AnswerSubformat.TEXT);
		el_3.setHorizontal(false);
		el_3.setMandatory(true);
		// cat
		el_1.addChild(el_3);
		Question el_4 = new Question();
		el_4.setName("Age");
		el_4.setLabel("age");
		el_4.setAnswerType(AnswerType.INPUT);
		el_4.setAnswerFormat(AnswerFormat.NUMBER);
		el_4.setAnswerSubformat(AnswerSubformat.NUMBER);
		el_4.setHorizontal(false);
		el_4.setMandatory(true);
		// cat
		el_1.addChild(el_4);
		Question el_5 = new Question();
		el_5.setName("birthdate");
		el_5.setLabel("Birtdate");
		el_5.setAnswerType(AnswerType.INPUT);
		el_5.setAnswerFormat(AnswerFormat.DATE);
		el_5.setAnswerSubformat(AnswerSubformat.DATE_PAST);
		el_5.setHorizontal(false);
		el_5.setMandatory(true);
		// cat
		el_1.addChild(el_5);
		// form
		form.addChild(el_1);
		Category el_6 = new Category();
		el_6.setName("Family");
		el_6.setLabel("Category");
		Question el_7 = new Question();
		el_7.setName("childs");
		el_7.setLabel("Childs");
		el_7.setAnswerType(AnswerType.SINGLE_SELECTION_RADIO);
		el_7.setHorizontal(false);
		el_7.setMandatory(true);
		Answer el_8 = new Answer();
		el_8.setName("yes");
		el_8.setLabel("yes");
		// ques
		el_7.addChild(el_8);
		Answer el_9 = new Answer();
		el_9.setName("no");
		el_9.setLabel("No");
		// ques
		el_7.addChild(el_9);
		// cat
		el_6.addChild(el_7);
		Text el_10 = new Text();
		el_10.setName("childText");
		el_10.setDescription("For each Child please fill this");
		// cat
		el_6.addChild(el_10);
		Group el_11 = new Group();
		el_11.setName("child_personal");
		el_11.setLabel("Child personal");
		el_11.setRepeatable(false);
		Question el_12 = new Question();
		el_12.setName("name");
		el_12.setLabel("Name");
		el_12.setAnswerType(AnswerType.INPUT);
		el_12.setAnswerFormat(AnswerFormat.TEXT);
		el_12.setAnswerSubformat(AnswerSubformat.TEXT);
		el_12.setHorizontal(false);
		el_12.setMandatory(true);
		// group
		el_11.addChild(el_12);
		Question el_13 = new Question();
		el_13.setName("surname");
		el_13.setLabel("Surname");
		el_13.setAnswerType(AnswerType.INPUT);
		el_13.setAnswerFormat(AnswerFormat.TEXT);
		el_13.setAnswerSubformat(AnswerSubformat.TEXT);
		el_13.setHorizontal(false);
		el_13.setMandatory(true);
		// group
		el_11.addChild(el_13);
		Question el_14 = new Question();
		el_14.setName("age");
		el_14.setLabel("Age");
		el_14.setAnswerType(AnswerType.INPUT);
		el_14.setAnswerFormat(AnswerFormat.TEXT);
		el_14.setAnswerSubformat(AnswerSubformat.TEXT);
		el_14.setHorizontal(false);
		el_14.setMandatory(true);
		// group
		el_11.addChild(el_14);
		Question el_15 = new Question();
		el_15.setName("birthdate");
		el_15.setLabel("Birthdate");
		el_15.setAnswerType(AnswerType.INPUT);
		el_15.setAnswerFormat(AnswerFormat.TEXT);
		el_15.setAnswerSubformat(AnswerSubformat.TEXT);
		el_15.setHorizontal(false);
		el_15.setMandatory(true);
		// group
		el_11.addChild(el_15);
		Question el_16 = new Question();
		el_16.setName("adopted");
		el_16.setLabel("Is Adopted?");
		el_16.setAnswerType(AnswerType.INPUT);
		el_16.setAnswerFormat(AnswerFormat.TEXT);
		el_16.setAnswerSubformat(AnswerSubformat.TEXT);
		el_16.setHorizontal(false);
		el_16.setMandatory(true);
		// group
		el_11.addChild(el_16);
		Question el_17 = new Question();
		el_17.setName("adopted_country");
		el_17.setLabel("In which country?");
		el_17.setAnswerType(AnswerType.SINGLE_SELECTION_LIST);
		el_17.setHorizontal(false);
		el_17.setMandatory(true);
		Answer el_18 = new Answer();
		el_18.setName("maybe.original.country");
		el_18.setLabel("Answer");
		// ques
		el_17.addChild(el_18);
		Answer el_19 = new Answer();
		el_19.setName("maybe.china");
		el_19.setLabel("Answer2");
		// ques
		el_17.addChild(el_19);
		Answer el_20 = new Answer();
		el_20.setName("other.countries");
		el_20.setLabel("Answer2");
		// ques
		el_17.addChild(el_20);
		// group
		el_11.addChild(el_17);
		Question el_21 = new Question();
		el_21.setName("another");
		el_21.setLabel("Another");
		el_21.setAnswerType(AnswerType.MULTIPLE_SELECTION);
		el_21.setHorizontal(false);
		el_21.setMandatory(true);
		Answer el_22 = new Answer();
		el_22.setName("a");
		el_22.setLabel("A");
		// ques
		el_21.addChild(el_22);
		Answer el_23 = new Answer();
		el_23.setName("b");
		el_23.setLabel("B");
		// ques
		el_21.addChild(el_23);
		// group
		el_11.addChild(el_21);
		// cat
		el_6.addChild(el_11);
		// form
		form.addChild(el_6);

		return form;
	}

	@Test
	public void testSubsetFunction() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {
		Form formA = generateTestForm1();
		Form formB = generateTestForm1();

		Assert.assertTrue(formA.isSubset(formB));

		formB.addChild(new Category("thisWillMakeItNotSubset"));

		Assert.assertFalse(formA.isSubset(formB));

		// Adding elements to B
		formB = generateTestForm1();
		formB.getChild(0).addChild(new Group("thisWillMakeItNotSubset"));
		Assert.assertFalse(formA.isSubset(formB));

		formB = generateTestForm1();
		formB.getChild(0).addChild(new Question("thisWillMakeItNotSubset"));
		Assert.assertFalse(formA.isSubset(formB));

		formB = generateTestForm1();
		formB.getChild(0).addChild(new Question("thisWillMakeItNotSubset"));
		Assert.assertFalse(formA.isSubset(formB));

		// Removing elements on A
		formA = generateTestForm1();
		formB = generateTestForm1();

		// Category
		formA.removeChild(0);
		Assert.assertFalse(formA.isSubset(formB));

		// Group
		formA = generateTestForm1();
		formA.getChild(1).removeChild(1);
		Assert.assertFalse(formA.isSubset(formB));

		// Question
		formA = generateTestForm1();
		formA.getChild(0).removeChild(0);
		Assert.assertFalse(formA.isSubset(formB));

		// Adding elements to A these won't fail
		formA = generateTestForm1();
		formA.getChild(0).addChild(new Group("stillASubset"));
		Assert.assertTrue(formA.isSubset(formB));

		formA = generateTestForm1();
		formA.getChild(0).addChild(new Question("stillASubset"));
		Assert.assertTrue(formA.isSubset(formB));

		formA = generateTestForm1();
		formA.getChild(0).addChild(new Question("stillASubset"));
		Assert.assertTrue(formA.isSubset(formB));
	}

	@Test
	public void testLinkValidation() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertTrue(validator.validate(abcdForm));
	}

	@Test
	public void testLinkValidationExtraCategory() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		webformsForm.addChild(new Category("wontFail"));

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertTrue(validator.validate(abcdForm));
	}

	@Test
	public void testLinkValidationExtraGroup() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		webformsForm.getChild(0).addChild(new Group("wontFail"));

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertTrue(validator.validate(abcdForm));
	}

	@Test
	public void testLinkValidationExtraQuestion() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		webformsForm.getChild(0).addChild(new Question("wontFail"));

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertTrue(validator.validate(abcdForm));
	}

	@Test
	public void testLinkValidationRemovedCategory() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		webformsForm.getChild(0).remove();

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertFalse(validator.validate(abcdForm));
	}

	@Test
	public void testLinkValidationRemovedQuestion() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		webformsForm.getChild(0).getChild(0).remove();

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertFalse(validator.validate(abcdForm));
	}

	@Test
	public void testLinkValidationRemovedGroup() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		webformsForm.getChild(1).getChild(2).remove();

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertFalse(validator.validate(abcdForm));
	}

	@Test
	public void testLinkValidationChangeGroupName() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		webformsForm.getChild(1).getChild(2).setName("thisWillFail");

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertFalse(validator.validate(abcdForm));
	}

	@Test
	public void testLinkValidationChangeQuestionType() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		((Question) webformsForm.getChild(1).getChild(0)).setAnswerType(AnswerType.MULTIPLE_SELECTION);

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertFalse(validator.validate(abcdForm));
	}

	@Test
	public void testLinkValidationChangeQuestionTypeStillValid() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
			ChildrenNotFoundException, DependencyExistException {

		com.biit.abcd.persistence.entity.Form abcdForm = AbcdTestForms.generateTestForm1();
		Form webformsForm = generateTestForm1();

		// Single selection radio and list match to radio of Abcd.
		((Question) webformsForm.getChild(1).getChild(0)).setAnswerType(AnswerType.SINGLE_SELECTION_LIST);

		ValidateFormAbcdCompatibility validator = new ValidateFormAbcdCompatibility(webformsForm);
		Assert.assertTrue(validator.validate(abcdForm));
	}

}
