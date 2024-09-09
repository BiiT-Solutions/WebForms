package com.biit.webforms.utils.exporters.xml;

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
