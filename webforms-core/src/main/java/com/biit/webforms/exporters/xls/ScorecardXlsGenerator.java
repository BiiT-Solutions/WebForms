package com.biit.webforms.exporters.xls;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.biit.webforms.exporters.xls.exceptions.InvalidXlsElementException;
import com.biit.webforms.logger.XlsExporterLog;
import com.biit.webforms.persistence.entity.Form;

@Component
public class ScorecardXlsGenerator {


    public byte[] generate(ApplicationContext applicationContext, Form form, Locale locale) throws InvalidXlsElementException {
        try {
            if (form == null) {
                return new byte[0];
            }

            HSSFWorkbook workbook = new HSSFWorkbook();
            new ScorecardXls().createXlsDocument(workbook, applicationContext, form, locale);

            ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
            workbook.write(fileOut);

            try {
                return fileOut.toByteArray();
            } finally {
                fileOut.close();
            }

        } catch (Exception e) {
            XlsExporterLog.errorMessage(this.getClass().getName(), e);
            throw new InvalidXlsElementException(e);
        }
    }

    public void createFile(ApplicationContext applicationContext, Form form, String path, Locale locale) throws IOException, InvalidXlsElementException {
        if (!path.endsWith(".xls")) {
            path += ".xls";
        }

        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(generate(applicationContext, form, locale));
        }
    }
}
