package com.biit.webforms.xforms;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.exporters.xforms.XFormsSimpleFormExporter;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
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
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;

@Test(groups = { "xforms" })
public class XFormExporterTests {

	@Test
	public void exportToXForms() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException, InvalidAnswerFormatException,
			InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException, FlowSameOriginAndDestinyException,
			FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, NotValidTreeObjectException, NotExistingDynamicFieldException,
			InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError, ElementIsReadOnly, FlowNotAllowedException, UnsupportedEncodingException {
		Form form = FormUtils.createCompleteForm();
		new XFormsSimpleFormExporter(form, null, new HashSet<>(), false, false).generateXFormsLanguage();
	}
}
