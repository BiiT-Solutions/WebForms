package com.biit.webforms.gui.webpages.compare.content;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

public class UpperMenu extends UpperMenuWebforms {
	private static final long serialVersionUID = 7495354678540982221L;

	private IconButton compareContent;
	private IconButton uploadOriginalXml;
	private IconButton removeOriginalXml;
	private IconButton removeAllOriginalXml;
	private IconButton uploadProcessedXml;
	private IconButton removeProcessedXml;
	private IconButton removeAllProcessedXml;

	public UpperMenu() {

		compareContent = new IconButton(LanguageCodes.CAPTION_COMPARE_XML_CONTENT, ThemeIcons.COMPARE_XML_CONTENT,
				LanguageCodes.TOOLTIP_COMPARE_XML_CONTENT, IconSize.BIG);

		uploadOriginalXml = new IconButton(LanguageCodes.CAPTION_UPLOAD_XML_ORIGINAL, ThemeIcons.UPLOAD_XML_ORIGINAL,
				LanguageCodes.TOOLTIP_UPLOAD_XML_ORIGINAL, IconSize.BIG);
		removeOriginalXml = new IconButton(LanguageCodes.CAPTION_REMOVE_XML_ORIGINAL, ThemeIcons.REMOVE_XML_ORIGINAL,
				LanguageCodes.TOOLTIP_REMOVE_XML_ORIGINAL, IconSize.BIG);
		removeAllOriginalXml = new IconButton(LanguageCodes.CAPTION_REMOVE_ALL_XML_ORIGINAL,
				ThemeIcons.REMOVE_ALL_XML_ORIGINAL, LanguageCodes.TOOLTIP_REMOVE_ALL_XML_ORIGINAL, IconSize.BIG);

		uploadProcessedXml = new IconButton(LanguageCodes.CAPTION_UPLOAD_XML_PROCESSED,
				ThemeIcons.UPLOAD_XML_PROCESSED, LanguageCodes.TOOLTIP_UPLOAD_XML_PROCESSED, IconSize.BIG);
		removeProcessedXml = new IconButton(LanguageCodes.CAPTION_REMOVE_XML_PROCESSED,
				ThemeIcons.REMOVE_XML_PROCESSED, LanguageCodes.TOOLTIP_REMOVE_XML_PROCESSED, IconSize.BIG);
		removeAllProcessedXml = new IconButton(LanguageCodes.CAPTION_REMOVE_ALL_XML_PROCESSED,
				ThemeIcons.REMOVE_ALL_XML_PROCESSED, LanguageCodes.TOOLTIP_REMOVE_ALL_XML_PROCESSED, IconSize.BIG);

		addIconButton(compareContent);
		addIconButton(uploadOriginalXml);
		addIconButton(removeOriginalXml);
		addIconButton(removeAllOriginalXml);
		addIconButton(uploadProcessedXml);
		addIconButton(removeProcessedXml);
		addIconButton(removeAllProcessedXml);

	}

	public void addUploadOriginalListener(ClickListener listener) {
		uploadOriginalXml.addClickListener(listener);
	}

	public void addUploadProcessedListener(ClickListener listener) {
		uploadProcessedXml.addClickListener(listener);
	}

	public void addCompareListener(ClickListener listener) {
		compareContent.addClickListener(listener);
	}

}
