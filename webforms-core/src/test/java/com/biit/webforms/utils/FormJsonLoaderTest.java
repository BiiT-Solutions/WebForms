package com.biit.webforms.utils;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.biit.webforms.persistence.entity.Form;

public class FormJsonLoaderTest {

	protected Form loadForm(String filename) throws IOException {

		ClassLoader classLoader = getClass().getClassLoader();
		String result = IOUtils.toString(classLoader.getResourceAsStream(filename), "UTF-8");

		return Form.fromJson(result);
	}

}
