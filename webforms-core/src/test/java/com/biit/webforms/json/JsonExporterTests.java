package com.biit.webforms.json;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.IBaseFormView;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.utils.file.FileReader;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.utils.exporters.json.BaseFormToJsonExporter;
import com.biit.webforms.utils.exporters.json.MetadataExporter;

@Test(groups = { "jsonForms" })
public class JsonExporterTests {

	private final static String DUMMY_FORM = "Dummy Form";
	private final static String DUMMY_CATEGORY = "Dummy_Category";
	private final static String DUMMY_CATEGORY_LABEL = "Dummy_Category Label";
	private final static String DUMMY_QUESTION = "Dummy_Question";
	private final static String DUMMY_QUESTION_LABEL = "Dummy_Question Label";

	private final static String METADATA_FILE_PATH = "jsonExporters/metadata.json";

	@Test
	public void exportToBaseForm() throws NotValidStorableObjectException, NotValidChildException, ElementIsReadOnly,
			FieldTooLongException, CharacterNotAllowedException {

		Assert.assertNotNull(BaseFormToJsonExporter.toJson(createSimpleStaticForm()));
	}

	@Test
	public void exportFormMetadata() throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, ElementIsReadOnly, FileNotFoundException {

		Form formToLink = createSimpleStaticForm();
		Form form = createSimpleStaticForm();
		Set<IBaseFormView> formSet = new HashSet<>();
		formSet.add(formToLink);
		form.setLinkedForms(formSet);
		form.addLinkedFormVersion(5);
		form.addLinkedFormVersion(6);
		String actual = MetadataExporter.exportFormMetadata(form);
		String expected = FileReader.getResource(METADATA_FILE_PATH, StandardCharsets.UTF_8);
		Assert.assertEquals(actual.trim(), expected.trim());
	}

	private Form createSimpleStaticForm() throws FieldTooLongException, CharacterNotAllowedException,
			NotValidChildException, ElementIsReadOnly {
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
}
