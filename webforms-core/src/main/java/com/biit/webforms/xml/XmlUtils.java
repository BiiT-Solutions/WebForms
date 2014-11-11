package com.biit.webforms.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;

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
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			// XML header
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			// t.setOutputProperty(OutputKeys.STANDALONE, "yes");
			t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			// Create data
			t.transform(ds, new StreamResult(result));
			return result;
		} catch (SAXParseException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String format(String xml) {
		return formatToStringWriter(xml).toString();
	}

	public static InputStream formatToInputStream(String xml) {
		StringWriter result = formatToStringWriter(xml);
		return new ByteArrayInputStream(result.getBuffer().toString().getBytes());
	}

	public static String normalizeNodeName(String originalString) {
		return originalString.replaceAll(" ", "_");
	}
}
