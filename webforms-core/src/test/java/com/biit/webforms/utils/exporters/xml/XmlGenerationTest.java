package com.biit.webforms.utils.exporters.xml;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import com.biit.webforms.persistence.entity.Form;

public class XmlGenerationTest {

	@Test()
	public void testXmlGeneration() throws IOException {
		System.out.println("XmlGeneration test");
		Form form = loadForm("test_text.json");
		
	}

	public Form loadForm(String filename) throws IOException {

		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader
				.getResourceAsStream(filename));

		return Form.fromJson(result);
	}
}
