package com.biit.webforms.xml;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.biit.webforms.logger.WebformsLogger;

public class XmlUtils {
	public static final String XML_CODIFICATION = "UTF-8";

	public static StringWriter formatToStringWriter(String xml) {
		try {
			final InputStream src = new ByteArrayInputStream(xml.getBytes(XML_CODIFICATION));
			final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src)
					.getDocumentElement();

			StringWriter result = new StringWriter();
			DOMSource ds = new DOMSource(document);
			Transformer t = TransformerFactory.newInstance().newTransformer();

			// Indentation
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "8");
			// XML header
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			// t.setOutputProperty(OutputKeys.STANDALONE, "yes");
			t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			// Create data
			t.transform(ds, new StreamResult(result));
			return result;
		} catch (Exception e) {
			WebformsLogger.warning(XmlUtils.class.getName(), "Unexpected failure while processing xml file. Storing content in temp file");
			try{
				File temp = File.createTempFile("webforms_xml_prettify", "xml");
				BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
	    	    bw.write(xml);
	    	    bw.close();				
			}catch(IOException ioe){				
			}
			throw new RuntimeException(e);			
		}
	}

	public static String format(String xml) {
		return formatToStringWriter(xml).toString();
	}

	public static InputStream formatToInputStream(String xml) {
		StringWriter result = formatToStringWriter(xml);
		return new ByteArrayInputStream(result.getBuffer().toString().getBytes(Charset.forName("UTF-8")));
	}

	public static String normalizeNodeName(String originalString) {
		return originalString.replaceAll(" ", "_");
	}

	public static String validateXml(String xml, String xsd) throws ParserConfigurationException, SAXException,
			IOException {
		String resultString = new String();

		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// load a WXS schema, represented by a Schema instance
		Source schemaFile = new StreamSource(new ByteArrayInputStream(xsd.getBytes(Charset.forName("UTF-8"))));
		Schema schema = factory.newSchema(schemaFile);

		// create a Validator instance, which can be used to validate an
		// instance document
		Validator validator = schema.newValidator();

		// validate the DOM tree
		try {
			validator.validate(new StreamSource(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")))));
		} catch (SAXException e) {
			resultString += e.getMessage() + System.lineSeparator();
		}

		return resultString;
	}

}
