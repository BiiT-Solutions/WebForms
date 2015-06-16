package com.biit.webforms.xml;

import java.util.ArrayList;
import java.util.List;

public class XpathToXml {

	private List<String> xpaths;
	
	public XpathToXml() {
		xpaths = new ArrayList<String>();
	}
	
	public void addXpath(String xpath){
		xpaths.add(xpath);
	}
	
	public String generateXml(){
		XmlContent content = new XmlContent();
		for(String xpath: xpaths){
			content.addChild(xpath);
		}
		return content.getXml();
	}
	
	public String generateCodifiedXml(){
		XmlContent content = new XmlContent();
		for(String xpath: xpaths){
			content.addChild(xpath);
		}
		return content.getCodifiedXml();
	}
	
}
