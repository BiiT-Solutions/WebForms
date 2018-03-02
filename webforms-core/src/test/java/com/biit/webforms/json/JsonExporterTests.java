package com.biit.webforms.json;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.entity.IBaseFormView;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.utils.file.FileReader;
import com.biit.webforms.exporters.json.BaseFormMetadataExporter;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.biit.webforms.utils.FormUtils;
import com.biit.webforms.utils.conversor.abcd.exporter.ConversorFormToAbcdForm;

@Test(groups = { "jsonForms" })
public class JsonExporterTests {

	private final static String DUMMY_FORM = "Dummy Form";
	private final static String DUMMY_CATEGORY = "Dummy_Category";
	private final static String DUMMY_CATEGORY_LABEL = "Dummy_Category Label";
	private final static String DUMMY_QUESTION = "Dummy_Question";
	private final static String DUMMY_QUESTION_LABEL = "Dummy_Question Label";

	private final static String METADATA_FILE_PATH = "jsonExporters/metadata.json";

	private Form createSimpleStaticForm() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, ElementIsReadOnly {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setVersion(1);
		form.setLabel(DUMMY_FORM);
		Category category = new Category();
		category.setName(DUMMY_CATEGORY);
		category.setLabel(DUMMY_CATEGORY_LABEL);
		form.addChild(category);
		Question questionValue = new Question();
		questionValue.setName(DUMMY_QUESTION);
		questionValue.setLabel(DUMMY_QUESTION_LABEL);
		category.addChild(questionValue);
		return form;
	}

	@Test
	public void exportFormMetadata() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, ElementIsReadOnly,
			FileNotFoundException {

		Form formToLink = createSimpleStaticForm();
		Form form = createSimpleStaticForm();
		Set<IBaseFormView> formSet = new HashSet<>();
		formSet.add(formToLink);
		form.setLinkedForms(formSet);
		form.addLinkedFormVersion(5);
		form.addLinkedFormVersion(6);
		String actual = BaseFormMetadataExporter.exportFormMetadata(form);
		String expected = FileReader.getResource(METADATA_FILE_PATH, StandardCharsets.UTF_8);
		Assert.assertEquals(actual.trim(), expected.trim());
	}

	@Test
	public void exportToSimpleJson() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException,
			InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, ElementIsReadOnly, FlowNotAllowedException {
		Form webform = FormUtils.createCompleteForm();

		com.biit.abcd.persistence.entity.Form abcdForm = new com.biit.abcd.persistence.entity.Form();
		ConversorFormToAbcdForm conversor = new ConversorFormToAbcdForm();
		com.biit.abcd.persistence.entity.Form abcdConvertedForm = conversor.convert(webform);
		abcdForm.addChildren(abcdConvertedForm.getChildren());

		Assert.assertEquals(webform.getAllInnerStorableObjects(Category.class).size(),
				abcdForm.getAllInnerStorableObjects(com.biit.abcd.persistence.entity.Category.class).size());
		Assert.assertEquals(webform.getAllInnerStorableObjects(Group.class).size(),
				abcdForm.getAllInnerStorableObjects(com.biit.abcd.persistence.entity.Group.class).size());
		Assert.assertEquals(webform.getAllInnerStorableObjects(Question.class).size(),
				abcdForm.getAllInnerStorableObjects(com.biit.abcd.persistence.entity.Question.class).size());
		Assert.assertEquals(webform.getAllInnerStorableObjects(Answer.class).size(),
				abcdForm.getAllInnerStorableObjects(com.biit.abcd.persistence.entity.Answer.class).size());

	}
}
