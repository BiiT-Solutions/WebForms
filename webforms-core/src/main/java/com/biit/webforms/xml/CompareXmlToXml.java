package com.biit.webforms.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CompareXmlToXml {

	private Document xmlDocument1;
	private Document xmlDocument2;
	private String cause;

	public CompareXmlToXml(ByteArrayOutputStream file1Content, ByteArrayOutputStream file2Content)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		System.out.println(new ByteArrayInputStream(file1Content.toByteArray()).toString());
		System.out.println("KIWIIIIIIIIIIIIII");

		xmlDocument1 = dBuilder.parse(new ByteArrayInputStream(file1Content.toByteArray()));
		xmlDocument2 = dBuilder.parse(new ByteArrayInputStream(file2Content.toByteArray()));
		cause = new String();
	}

	public boolean comparate() {

		xmlDocument1.normalizeDocument();
		xmlDocument2.normalizeDocument();

		System.out.println("Doc1");
		System.out.println(xmlDocument1.toString());
		System.out.println("Doc2");
		System.out.println(xmlDocument2.toString());

		Element document1 = xmlDocument1.getDocumentElement();
		Element document2 = xmlDocument2.getDocumentElement();

		return comparate(document1, document2);
	}

	private boolean comparate(Element element1, Element element2) {
		if (!element1.getNodeName().equals(element2.getNodeName())) {
			cause += "Node '" + element1.getNodeName() + "' is different to node '" + element2.getNodeName() + "'"
					+ System.lineSeparator();
			return false;
		}

		List<Element> childElements1 = getChildElements(element1);
		List<Element> childElements2 = getChildElements(element2);

		if (childElements1.size() == childElements2.size()) {
			if (childElements1.isEmpty()) {
				if ((element1.getFirstChild() == null && element2.getFirstChild() != null)
						|| (element1.getFirstChild() != null && element2.getFirstChild() == null)) {
					cause += "Values of node '" + element1.getNodeName() + "' is different." + System.lineSeparator();
					return false;
				}
				if (element1.getFirstChild() != null && element2.getFirstChild() != null) {
					if (!element1.getFirstChild().getNodeValue().equals(element2.getFirstChild().getNodeValue())) {
						cause += "Values of node '" + element1.getNodeName() + "' is different."
								+ System.lineSeparator();
						return false;
					}
				}
				return true;
			} else {
				for (int i = 0; i < childElements1.size(); i++) {
					if (!comparate(childElements1.get(i), childElements2.get(i))) {
						return false;
					}
				}
				return true;
			}
		} else {
			cause += "Node '" + element1.getNodeName() + "' childs don't match '" + printNames(childElements1)
					+ "' and '" + printNames(childElements2) + "'" + System.lineSeparator();
			return false;
		}
	}

	private String printNames(List<Element> elements) {
		String names = new String();
		if (!elements.isEmpty()) {
			names += elements.get(0).getNodeName();
		}
		for (int i = 1; i < elements.size(); i++) {
			names += "," + elements.get(i).getNodeName();
		}
		return names;
	}

	private List<Element> getChildElements(Element element) {
		List<Element> childElements = new ArrayList<>();
		NodeList childList = element.getChildNodes();

		for (int i = 0; i < childList.getLength(); i++) {
			Node node = childList.item(i);
			if (node instanceof Element) {
				childElements.add((Element) node);
			}
		}

		return childElements;
	}

	public String getCause() {
		return cause;
	}
}