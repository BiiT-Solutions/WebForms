package com.biit.webforms.utils.exporters.xml;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.exporters.xml.exceptions.ElementWithoutNextElement;
import com.biit.webforms.utils.exporters.xml.exceptions.TooMuchIterationsWhileGeneratingPath;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;

@Test(groups = { "xmlGeneration" })
public class XmlGenerationTest {

	@Test()
	public void testXmlGeneration() throws IOException, ElementWithoutNextElement, TooMuchIterationsWhileGeneratingPath, BadFormedExpressions {
		System.out.println("XmlGeneration test");
		//Form form = loadForm("De Haagse Passage.json");
		Form form = loadForm("Test_xml.json");
		XmlExporter exporter = new XmlExporter(form);
		exporter.generate(10);
	}

	public Form loadForm(String filename) throws IOException {

		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader
				.getResourceAsStream(filename));

		return Form.fromJson(result);
	}
}
