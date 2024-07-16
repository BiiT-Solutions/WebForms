package com.biit.webforms.persistence;

import com.biit.webforms.persistence.entity.Form;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test(groups = {"jsonTests"})
public class JsonToFormTests {

    private String readJsonFile(String fileName) throws URISyntaxException, IOException {
        return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                .getResource("json" + File.separator + fileName).toURI()))).trim();
    }

    @Test
    public void readOrganizationForm() throws URISyntaxException, IOException {
        final String json = readJsonFile("Organization.json");

        Form organizationForm = Form.fromJson(json);
        Assert.assertNotNull(organizationForm);
    }
}
