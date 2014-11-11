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

	private IconButton newForm, finish, newFormVersion, importAbcdForm, linkAbcdForm, exportXForms, exportPdf,
			exportFlowPdf, exportXsd;

	public UpperMenuProjectManager() {
		super();

		newForm = new IconButton(LanguageCodes.CAPTION_NEW_FORM, ThemeIcons.FORM_MANAGER_ADD_FORM,
				LanguageCodes.TOOLTIP_NEW_FORM, IconSize.BIG);
		finish = new IconButton(LanguageCodes.COMMON_CAPTION_FINISH, ThemeIcons.FORM_FINISH,
				LanguageCodes.COMMON_TOOLTIP_FINISH, IconSize.BIG);
		newFormVersion = new IconButton(LanguageCodes.CAPTION_NEW_FORM_VERSION, ThemeIcons.FORM_MANAGER_NEW_VERSION,
				LanguageCodes.TOOLTIP_NEW_FORM_VERSION, IconSize.BIG);
		importAbcdForm = new IconButton(LanguageCodes.CAPTION_IMPORT_ABCD_FORM,
				ThemeIcons.FORM_MANAGER_IMPORT_ABCD_FORM, LanguageCodes.TOOLTIP_IMPORT_ABCD_FORM, IconSize.BIG);
		linkAbcdForm = new IconButton(LanguageCodes.CAPTION_LINK_ABCD_FORM, ThemeIcons.FORM_MANAGER_LINK_ABCD_FORM,
				LanguageCodes.TOOLTIP_LINK_ABCD_FORM, IconSize.BIG);
		exportPdf = new IconButton(LanguageCodes.COMMON_CAPTION_EXPORT_TO_PDF, ThemeIcons.EXPORT_FORM_TO_PDF,
				LanguageCodes.COMMON_TOOLTIP_EXPORT_TO_PDF, IconSize.BIG);
		exportFlowPdf = new IconButton(LanguageCodes.CAPTION_PRINT_FLOW, ThemeIcons.EXPORT_FORM_TO_PDF,
				LanguageCodes.TOOLTIP_PRINT_FLOW, IconSize.BIG);

		exportXForms = new IconButton(LanguageCodes.CAPTION_TO_XFORMS, ThemeIcons.EXPORT_FORM_TO_XFORMS,
				LanguageCodes.TOOLTIP_TO_XFORMS, IconSize.BIG);
		
		exportXsd = new IconButton(LanguageCodes.CAPTION_EXPORT_XSD, ThemeIcons.EXPORT_XSD,
				LanguageCodes.TOOLTIP_EXPORT_XSD, IconSize.BIG);

		addIconButton(newForm);
		addIconButton(finish);
		addIconButton(newFormVersion);
		addIconButton(importAbcdForm);
		addIconButton(linkAbcdForm);
		addIconButton(exportPdf);
		addIconButton(exportFlowPdf);
		addIconButton(exportXForms);
		addIconButton(exportXsd);
	}

	public void addNewFormListener(ClickListener listener) {
		newForm.addClickListener(listener);
	}

	public void addFinishListener(ClickListener listener) {
		finish.addClickListener(listener);
	}

	public void addNewFormVersionListener(ClickListener listener) {
		newFormVersion.addClickListener(listener);
	}

	public void addImportAbcdForm(ClickListener listener) {
		importAbcdForm.addClickListener(listener);
	}

	public void addLinkAbcdForm(ClickListener listener) {
		linkAbcdForm.addClickListener(listener);
	}

	public void addExportPdf(ClickListener listener) {
		exportPdf.addClickListener(listener);
	}

	public void addExportFlowPdfListener(ClickListener listener) {
		exportFlowPdf.addClickListener(listener);
	}

	public void addExportXFormsListener(ClickListener listener) {
		exportXForms.addClickListener(listener);
	}
	
	public void addExportXsdListener(ClickListener listener) {
		exportXsd.addClickListener(listener);
	}

	public IconButton getNewForm() {
		return newForm;
	}

	public IconButton getFinish() {
		return finish;
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

	public IconButton getExportFlowPdf() {
		return exportFlowPdf;
	}

	public IconButton getExportXForms() {
		return exportXForms;
	}
	
	public IconButton getExportXsd() {
		return exportXsd;
	}
}
