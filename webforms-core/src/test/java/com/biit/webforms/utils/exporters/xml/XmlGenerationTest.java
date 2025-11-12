package com.biit.webforms.utils.exporters.xml;

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

import com.biit.webforms.exporters.xml.RandomXmlFormResultExporter;
import com.biit.webforms.exporters.xml.exceptions.ElementWithoutNextElement;
import com.biit.webforms.exporters.xml.exceptions.TooMuchIterationsWhileGeneratingPath;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Test(groups = { "xmlGeneration" })
public class XmlGenerationTest {

	@Test()
	public void testXmlGeneration() throws IOException, ElementWithoutNextElement, TooMuchIterationsWhileGeneratingPath, BadFormedExpressions {
		//Form form = loadForm("De Haagse Passage.json");
		Form form = loadForm("test_xml.json");
		RandomXmlFormResultExporter exporter = new RandomXmlFormResultExporter(new CompleteFormView(form));
		exporter.generate(10);
	}

	public Form loadForm(String filename) throws IOException {

		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader.getResourceAsStream(filename), StandardCharsets.UTF_8);

		return Form.fromJson(result);
	}
}
