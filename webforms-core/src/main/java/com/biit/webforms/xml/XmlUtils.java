package com.biit.webforms.xml;

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

import com.biit.webforms.logger.WebformsLogger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlUtils {

    public static StringWriter formatToStringWriter(String xml) {
        try {
            final OutputFormat format = OutputFormat.createPrettyPrint();
            final Document document = DocumentHelper.parseText(xml);
            StringWriter sw = new StringWriter();
            final XMLWriter writer = new XMLWriter(sw, format);
            writer.write(document);
            return sw;
        } catch (Exception e) {
            WebformsLogger.warning(XmlUtils.class.getName(),
                    "Unexpected failure while processing xml file. Dumping content to a temporaly file 'webforms_xml_prettify'.");
            try {
                File temp = File.createTempFile("webforms_xml_prettify", "xml");
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp, true), StandardCharsets.UTF_8));
                bufferedWriter.write(xml);
                bufferedWriter.close();
            } catch (IOException ioe) {
            }
            throw new RuntimeException(e);
        }
    }

    public static String format(String xml) {
        return formatToStringWriter(xml).toString();
    }

    public static InputStream formatToInputStream(String xml) {
        StringWriter result = formatToStringWriter(xml);
        return new ByteArrayInputStream(result.getBuffer().toString().getBytes(StandardCharsets.UTF_8));
    }

    public static String normalizeNodeName(String originalString) {
        return originalString.replaceAll(" ", "_");
    }

    public static String validateXml(String xml, String xsd) throws ParserConfigurationException, SAXException, IOException {
        String resultString = new String();

        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // load a WXS schema, represented by a Schema instance
        Source schemaFile = new StreamSource(new ByteArrayInputStream(xsd.getBytes(StandardCharsets.UTF_8)));
        Schema schema = factory.newSchema(schemaFile);

        // create a Validator instance, which can be used to validate an
        // instance document
        Validator validator = schema.newValidator();

        // validate the DOM tree
        try {
            validator.validate(new StreamSource(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))));
        } catch (SAXException e) {
            resultString += e.getMessage() + System.lineSeparator();
        }

        return resultString;
    }

}
