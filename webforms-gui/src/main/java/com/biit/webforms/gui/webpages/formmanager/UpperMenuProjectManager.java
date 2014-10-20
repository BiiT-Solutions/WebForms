package com.biit.webforms.gui.webpages.formmanager;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

/**
 * Upper menu for project manager web.
 * 
 */
public class UpperMenuProjectManager extends UpperMenuWebforms {
	private static final long serialVersionUID = -3687306989433923394L;

	private IconButton newForm, newFormVersion, importAbcdForm, linkAbcdForm, exportPdf;

	public UpperMenuProjectManager() {
		super();

		newForm = new IconButton(LanguageCodes.CAPTION_NEW_FORM, ThemeIcons.FORM_MANAGER_ADD_FORM,
				LanguageCodes.TOOLTIP_NEW_FORM, IconSize.BIG);
		newFormVersion = new IconButton(LanguageCodes.CAPTION_NEW_FORM_VERSION, ThemeIcons.FORM_MANAGER_NEW_VERSION,
				LanguageCodes.TOOLTIP_NEW_FORM_VERSION, IconSize.BIG);
		importAbcdForm = new IconButton(LanguageCodes.CAPTION_IMPORT_ABCD_FORM,
				ThemeIcons.FORM_MANAGER_IMPORT_ABCD_FORM, LanguageCodes.TOOLTIP_IMPORT_ABCD_FORM, IconSize.BIG);
		linkAbcdForm = new IconButton(LanguageCodes.CAPTION_LINK_ABCD_FORM,
				ThemeIcons.FORM_MANAGER_LINK_ABCD_FORM, LanguageCodes.TOOLTIP_LINK_ABCD_FORM, IconSize.BIG);
		exportPdf = new IconButton(LanguageCodes.COMMON_CAPTION_EXPORT_TO_PDF, ThemeIcons.EXPORT_FORM_TO_PDF,
				LanguageCodes.COMMON_TOOLTIP_EXPORT_TO_PDF, IconSize.BIG);

		addIconButton(newForm);
		addIconButton(newFormVersion);
		addIconButton(importAbcdForm);
		addIconButton(linkAbcdForm);
		addIconButton(exportPdf);
	}

	public void addNewFormListener(ClickListener listener) {
		newForm.addClickListener(listener);
	}

	public void addNewFormVersionListener(ClickListener listener) {
		newFormVersion.addClickListener(listener);
	}
	
	public void addImportAbcdForm(ClickListener listener){
		importAbcdForm.addClickListener(listener);
	}
	
	public void addLinkAbcdForm(ClickListener listener){
		linkAbcdForm.addClickListener(listener);
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
	
	public IconButton getImportAbcdForm() {
		return importAbcdForm;
	}
	
	public IconButton getLinkAbcdForm() {
		return linkAbcdForm;
	}

	public IconButton getExportPdf() {
		return exportPdf;
	}
}
