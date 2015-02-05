package com.biit.webforms.utils.exporters.dotgraph;

public abstract class ExporterDot<T> {

	private final static String GREY = "#808080";
	private final static String LIGHT_GRAY = "#d3d3d3";
	private final static String BLACK = "#000000";
	private final static String RED3 = "#cd0000";
	private final static String ROYALBLUE4 = "#27408b";
	private final static String FORESTGREEN = "#228b22";

	private final static String READ_ONLY_ALPHA = "44";

	protected static final String DEFAULT_FILL_COLOR = LIGHT_GRAY;
	protected static final String DELETED_FILL_COLOR = LIGHT_GRAY;
	protected static final String MODIFIED_FILL_COLOR = LIGHT_GRAY;
	protected static final String NEW_FILL_COLOR = LIGHT_GRAY;

	protected static final String DEFAULT_FONT_COLOR = BLACK;
	protected static final String DELETED_FONT_COLOR = RED3;
	protected static final String MODIFIED_FONT_COLOR = ROYALBLUE4;
	protected static final String NEW_FONT_COLOR = FORESTGREEN;

	protected static final String DEFAULT_SHAPE_COLOR = GREY;
	protected static final String DELETED_SHAPE_COLOR = RED3;
	protected static final String MODIFIED_SHAPE_COLOR = ROYALBLUE4;
	protected static final String NEW_SHAPE_COLOR = FORESTGREEN;

	protected final String DEFAULT_LINK_COLOR = BLACK;
	protected final String DELETED_LINK_COLOR = RED3;
	protected final String MODIFIED_LINK_COLOR = ROYALBLUE4;
	protected final String NEW_LINK_COLOR = FORESTGREEN;

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

	public String getFillColor(boolean readOnly) {
		if (readOnly) {
			return "\"" + fillColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + fillColor + "\"";
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

	public String getShapeColor(boolean readOnly) {
		if (readOnly) {
			return "\"" + shapeColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + shapeColor + "\"";
	}

	public void setShapeColor(String shapeColor) {
		this.shapeColor = shapeColor;
	}

	public String getFontColor(boolean readOnly) {
		if (readOnly) {
			return "\"" + fontColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + fontColor + "\"";
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getLinkColor(boolean readOnly) {
		if (readOnly) {
			return "\"" + linkColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + linkColor + "\"";
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
