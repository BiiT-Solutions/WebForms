package com.biit.webforms.exporters.xls;

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
