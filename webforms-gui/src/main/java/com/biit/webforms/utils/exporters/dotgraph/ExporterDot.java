package com.biit.webforms.utils.exporters.dotgraph;

public abstract class ExporterDot<T> {
	
	protected String fillColor = "lightgray";
	protected String shapeColor = "grey";
	protected String fontColor = "black";
	protected String linkColor = "black";
	protected String penWidth = "1.0";
	
	protected String sizeLimit = "23.30,33.10";
	protected String smallFontSize = "8";

	public abstract String export(T structure);
	
	public abstract String generateDotNodeList(T structure);
	
	public abstract String generateDotNodeFlow(T structure);

	public String getSizeLimit() {
		return sizeLimit;
	}

	public void setSizeLimit(String sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	public String getSmallFontSize() {
		return smallFontSize;
	}

	public void setSmallFontSize(String smallFontSize) {
		this.smallFontSize = smallFontSize;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}
	
	protected String filterDotLanguage(String dotCode) {
		return dotCode.replace("\"", "\\\"");
	}
	
	protected String filterDotLanguageId(String dotCodeId) {
		return dotCodeId.replace("-", "_");
	}

	public String getShapeColor() {
		return shapeColor;
	}

	public void setShapeColor(String shapeColor) {
		this.shapeColor = shapeColor;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getLinkColor() {
		return linkColor;
	}

	public void setLinkColor(String linkColor) {
		this.linkColor = linkColor;
	}

	public String getPenWidth() {
		return penWidth;
	}

	public void setPenWidth(String penWidth) {
		this.penWidth = penWidth;
	}
	
	
}
