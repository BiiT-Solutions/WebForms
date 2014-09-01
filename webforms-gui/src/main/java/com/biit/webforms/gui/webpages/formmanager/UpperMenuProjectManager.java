package com.biit.webforms.gui.webpages.formmanager;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button.ClickListener;

/**
 * Upper menu for project manager web.
 * 
 * @author joriz_000
 * 
 */
public class UpperMenuProjectManager extends UpperMenuWebforms {
	private static final long serialVersionUID = -3687306989433923394L;

	private IconButton newForm, newFormVersion, editDesign, editFlow, exportPdf;
	private FileDownloader formPdfdownloader;
	private StreamResource formPdfstreamResource;

	public UpperMenuProjectManager(StreamSource pdfStreamSource, String defaultPdfName) {
		super();

		newForm = new IconButton(LanguageCodes.CAPTION_NEW_FORM, ThemeIcons.FORM_MANAGER_PAGE,
				LanguageCodes.TOOLTIP_NEW_FORM, IconSize.BIG);
		newFormVersion = new IconButton(LanguageCodes.CAPTION_NEW_FORM_VERSION, ThemeIcons.FORM_MANAGER_PAGE,
				LanguageCodes.TOOLTIP_NEW_FORM_VERSION, IconSize.BIG);
		editDesign = new IconButton(LanguageCodes.COMMON_CAPTION_DESIGN, ThemeIcons.DESIGNER_EDITOR,
				LanguageCodes.COMMON_TOOLTIP_DESIGN, IconSize.BIG);
		editFlow = new IconButton(LanguageCodes.COMMON_CAPTION_FLOW, ThemeIcons.FLOW_EDITOR,
				LanguageCodes.COMMON_TOOLTIP_FLOW, IconSize.BIG);
		exportPdf = new IconButton(LanguageCodes.COMMON_CAPTION_EXPORT_TO_PDF, ThemeIcons.EXPORT_FORM_TO_PDF,
				LanguageCodes.COMMON_TOOLTIP_EXPORT_TO_PDF, IconSize.BIG);

		addIconButton(newForm);
		addIconButton(newFormVersion);
		addIconButton(editDesign);
		addIconButton(editFlow);
		addIconButton(exportPdf);

		// Add download functionality to button
		formPdfstreamResource = new StreamResource(pdfStreamSource, defaultPdfName);
		formPdfdownloader = new FileDownloader(formPdfstreamResource);
		formPdfdownloader.extend(exportPdf);
	}

	public void addNewFormListener(ClickListener listener) {
		newForm.addClickListener(listener);
	}

	public void addNewFormVersionListener(ClickListener listener) {
		newFormVersion.addClickListener(listener);
	}

	public void addEditDesignListener(ClickListener listener) {
		editDesign.addClickListener(listener);
	}

	public void addEditFlowListener(ClickListener listener) {
		editFlow.addClickListener(listener);
	}

	public void addExportPdf(ClickListener listener) {
		exportPdf.addClickListener(listener);
	}

	public IconButton getNewForm() {
		return newForm;
	}

	public IconButton getNewFormVersion() {
		return newFormVersion;
	}

	public IconButton getEditDesign() {
		return editDesign;
	}

	public IconButton getEditFlow() {
		return editFlow;
	}

	public IconButton getExportPdf() {
		return exportPdf;
	}

	public void setExportFormPdfDefaultName(String name) {
		formPdfstreamResource.setFilename(name);
	}
}
