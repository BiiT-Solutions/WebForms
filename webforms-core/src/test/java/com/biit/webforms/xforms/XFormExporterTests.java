package com.biit.webforms.xforms;

import java.io.UnsupportedEncodingException;

import org.testng.annotations.Test;

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import com.biit.webforms.utils.FormUtils;
import com.biit.webforms.xforms.XFormsSimpleFormExporter;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;

@Test(groups = { "xforms" })
public class XFormExporterTests {

	@Test
	public void exportToXForms() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException,
			FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType,
			NotValidTreeObjectException, NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError, ElementIsReadOnly, FlowNotAllowedException, UnsupportedEncodingException {
		Form form = FormUtils.createCompleteForm();
		new XFormsSimpleFormExporter(form).generateXFormsLanguage();
	}
}
