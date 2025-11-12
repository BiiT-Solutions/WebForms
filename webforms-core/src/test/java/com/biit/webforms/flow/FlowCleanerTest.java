package com.biit.webforms.flow;

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

import java.io.IOException;

import org.testng.Assert;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import com.biit.webforms.persistence.entity.Form;

@Test(groups = { "flowCleaner" })
public class FlowCleanerTest {

	/**
	 * In this test a form has a simple flow that goes from question1 and question2 (is useless) and other flow that is
	 * defined as 'OTHERS' that goes from question2 to question3. 'OTHERS' must be removed and then, is also a useless
	 * flow.
	 *
	 * @throws IOException
	 */
	@Test
	public void basicFormWithOthersAndUselessFlow() throws IOException {
		Form form = loadForm("FormCleanFlow.json");
		Assert.assertEquals(2, form.getFlows().size());
		FlowCleaner flowCleaner = new FlowCleaner(form);
		flowCleaner.cleanFlow();
		Assert.assertEquals(0, form.getFlows().size());
	}

	private Form loadForm(String filename) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader.getResourceAsStream(filename),"UTF-8");

		return Form.fromJson(result);
	}
}
