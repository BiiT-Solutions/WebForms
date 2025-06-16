package com.biit.webforms.pdf;

import com.biit.webforms.pdfgenerator.FormGeneratorPdf;
import com.biit.webforms.pdfgenerator.FormPdfGenerator;
import com.biit.webforms.persistence.entity.Form;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;

@Test(groups = {"pdfForm"})
public class ExportToPdfTest {

    private Form loadForm(String filename) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream(filename), "UTF-8");

        return Form.fromJson(result);
    }

    @Test
    public void exportToPdf() throws IOException {
        Form form = loadForm("Sales and Marketing Maturity.json");
        FormGeneratorPdf.generatePdfAsFile("/tmp/Sales and Marketing Maturity.pdf", new FormPdfGenerator(form));
    }
}
