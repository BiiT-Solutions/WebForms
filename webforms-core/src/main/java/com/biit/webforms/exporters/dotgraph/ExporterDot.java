package com.biit.webforms.exporters.dotgraph;

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

/**
 * Generic class to export dot graph code.
 *
 * @param <T>
 */
public abstract class ExporterDot<T> {

	private static final String GREY = "#808080";
	private static final String LIGHT_GRAY = "#d3d3d3";
	private static final String BLACK = "#000000";
	private static final String RED3 = "#cd0000";
	private static final String ROYALBLUE4 = "#27408b";
	private static final String FORESTGREEN = "#228b22";

	private static final String READ_ONLY_ALPHA = "44";

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

	/**
	 * Return the string version of the dot graph code for a T structure.
	 *
	 * @param structure
	 * @return
	 */
	public abstract String export(T structure);

	/**
	 * Generates the list of nodes for a T structure.
	 * @param structure
	 * @return
	 */
	public abstract String generateDotNodeList(T structure);

	/**
	 * Generates the flow between nodes.
	 * @param structure
	 * @return
	 */
	public abstract String generateDotNodeFlow(T structure);

	/**
	 * Generate the nodes enclosed in a element.
	 * @param structure
	 * @return
	 */
	public abstract String generateDotNodeChilds(T structure);

	/**
	 * Limit sizes of the drawing
	 * @return
	 */
	public String getSizeLimit() {
		return sizeLimit;
	}

	/**
	 * Set the limits of the drawing
	 * @param sizeLimit
	 */
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
		if (readOnly && fillColor.equals(DEFAULT_FILL_COLOR)) {
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
		if (readOnly && shapeColor.equals(DEFAULT_SHAPE_COLOR)) {
			return "\"" + shapeColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + shapeColor + "\"";
	}

	public void setShapeColor(String shapeColor) {
		this.shapeColor = shapeColor;
	}

	public String getFontColor(boolean readOnly) {
		if (readOnly && fontColor.equals(DEFAULT_FONT_COLOR)) {
			return "\"" + fontColor + READ_ONLY_ALPHA + "\"";
		}
		return "\"" + fontColor + "\"";
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getLinkColor(boolean readOnly) {
		if (readOnly && linkColor.equals(DEFAULT_LINK_COLOR)) {
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
