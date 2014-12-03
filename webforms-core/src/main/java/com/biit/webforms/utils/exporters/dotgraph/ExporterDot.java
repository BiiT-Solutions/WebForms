package com.biit.webforms.utils.exporters.dotgraph;

public abstract class ExporterDot<T> {
	
	
	protected static final String DEFAULT_FILL_COLOR = "lightgray";
	protected static final String DELETED_FILL_COLOR = "lightgray";
	protected static final String MODIFIED_FILL_COLOR = "lightgray";
	protected static final String NEW_FILL_COLOR = "lightgray";
	
	protected static final String DEFAULT_FONT_COLOR = "black";
	protected static final String DELETED_FONT_COLOR = "red3";
	protected static final String MODIFIED_FONT_COLOR = "royalblue4";
	protected static final String NEW_FONT_COLOR = "forestgreen";
	
	protected static final String DEFAULT_SHAPE_COLOR = "grey";
	protected static final String DELETED_SHAPE_COLOR = "red3";
	protected static final String MODIFIED_SHAPE_COLOR = "royalblue4";
	protected static final String NEW_SHAPE_COLOR = "forestgreen";
	
	protected final String DEFAULT_LINK_COLOR = "black";
	protected final String DELETED_LINK_COLOR = "red3";
	protected final String MODIFIED_LINK_COLOR = "royalblue4";
	protected final String NEW_LINK_COLOR = "forestgreen"; 
	
	protected String fillColor = DEFAULT_FILL_COLOR;
	protected String shapeColor = DEFAULT_SHAPE_COLOR;
	protected String fontColor = DEFAULT_FONT_COLOR;
	protected String linkColor = DEFAULT_LINK_COLOR;
	protected String penWidth = "1.0";
	
	protected String sizeLimit = "23.30,33.10";
	protected String smallFontSize = "8";

	public abstract String export(T structure);
	
	public abstract String generateDotNodeList(T structure);
	
	public abstract String generateDotNodeFlow(T structure);
	
	public abstract String generateDotNodeChilds(T structure);

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
