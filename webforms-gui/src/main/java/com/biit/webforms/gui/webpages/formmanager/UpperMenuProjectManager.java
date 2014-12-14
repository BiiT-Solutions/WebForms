package com.biit.webforms.gui.webpages.formmanager;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.gui.xforms.OrbeonPreviewFrame;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.biit.webforms.xforms.XFormsExporter;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Upper menu for project manager web.
 * 
 */
public class UpperMenuProjectManager extends UpperMenuWebforms {
	private static final long serialVersionUID = -3687306989433923394L;

	private final IconButton submenuNew, newForm, newFormVersion, importAbcdForm;
	private final IconButton linkAbcdForm;
	private final IconButton exportXForms, previewXForms, publishXForms, downloadXForms;
	private final IconButton export, exportPdf, exportFlowPdf, exportXsd, exportJson;
	private final IconButton impactAnalysis, compareContent;
	private BrowserWindowOpener opener;

	public UpperMenuProjectManager() {
		super();

		newForm = new IconButton(LanguageCodes.CAPTION_NEW_FORM, ThemeIcons.FORM_MANAGER_ADD_FORM,
				LanguageCodes.TOOLTIP_NEW_FORM, IconSize.BIG);
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
		exportXsd = new IconButton(LanguageCodes.CAPTION_EXPORT_XSD, ThemeIcons.EXPORT_XSD,
				LanguageCodes.TOOLTIP_EXPORT_XSD, IconSize.BIG);
		exportJson = new IconButton(LanguageCodes.CAPTION_EXPORT_JSON, ThemeIcons.EXPORT_JSON,
				LanguageCodes.TOOLTIP_EXPORT_JSON, IconSize.BIG);

		opener = new BrowserWindowOpener(OrbeonPreviewFrame.class);
		opener.setParameter(OrbeonPreviewFrame.FORM_PARAMETER_TAG,"Preview_Aanvraag_Persoonlijke_Lening_ABCD");
		opener.setParameter(OrbeonPreviewFrame.APPLICATION_PARAMETER_TAG, XFormsExporter.APP_NAME);
		opener.setFeatures("target=_new");
		previewXForms = new IconButton(LanguageCodes.CAPTION_PREVIEW_XFORMS, ThemeIcons.PREVIEW_XFORMS,
				LanguageCodes.TOOLTIP_PREVIEW_XFORMS, IconSize.BIG);

		publishXForms = new IconButton(LanguageCodes.CAPTION_PUBLISH_XFORMS, ThemeIcons.PUBLISH_XFORMS,
				LanguageCodes.TOOLTIP_PUBLISH_XFORMS, IconSize.BIG);
		downloadXForms = new IconButton(LanguageCodes.CAPTION_DOWNLOAD_XFORMS, ThemeIcons.DOWNLOAD_XFORMS,
				LanguageCodes.TOOLTIP_DOWNLOAD_XFORMS, IconSize.BIG);

		impactAnalysis = new IconButton(LanguageCodes.CAPTION_IMPACT_ANALYSIS, ThemeIcons.IMPACT_ANALYSIS,
				LanguageCodes.TOOLTIP_IMPACT_ANALISYS, IconSize.BIG);

		compareContent = new IconButton(LanguageCodes.CAPTION_COMPARE_CONTENT, ThemeIcons.COMPARE_CONTENT,
				LanguageCodes.TOOLTIP_COMPARE_CONTENT, IconSize.BIG);

		submenuNew = addSubMenu(ThemeIcons.NEW, LanguageCodes.CAPTION_NEW, LanguageCodes.TOOLTIP_NEW, newForm,
				newFormVersion, importAbcdForm);
		addIconButton(linkAbcdForm);

		export = addSubMenu(ThemeIcons.EXPORT, LanguageCodes.CAPTION_EXPORT, LanguageCodes.TOOLTIP_EXPORT, exportPdf,
				exportFlowPdf, exportXsd,exportJson);
		exportXForms = addSubMenu(ThemeIcons.EXPORT_FORM_TO_XFORMS, LanguageCodes.CAPTION_TO_XFORMS,
				LanguageCodes.TOOLTIP_TO_XFORMS, previewXForms, publishXForms, downloadXForms);
		opener.extend(previewXForms);

		addIconButton(impactAnalysis);
		addIconButton(compareContent);
	}

	public void addNewFormListener(ClickListener listener) {
		newForm.addClickListener(listener);
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

	public void addExportXsdListener(ClickListener listener) {
		exportXsd.addClickListener(listener);
	}
	
	public void addExportJsonListener(ClickListener listener) {
		exportJson.addClickListener(listener);
	}

	public void addCompareContent(ClickListener listener) {
		compareContent.addClickListener(listener);
	}

	public void addImpactAnalysisListener(ClickListener listener) {
		impactAnalysis.addClickListener(listener);
	}

	public void addPreviewXForms(ClickListener listener) {
		previewXForms.addClickListener(listener);
	}

	public void addPublishXForms(ClickListener listener) {
		publishXForms.addClickListener(listener);
	}

	public void addDownloadXForms(ClickListener listener) {
		downloadXForms.addClickListener(listener);
	}

	public IconButton getNewForm() {
		return newForm;
	}

	public IconButton getNewFormVersion() {
		return newFormVersion;
	}

	public void setEnabledExport(boolean value) {
		export.setEnabled(value);
	}

	public void setEnabledXForms(boolean value) {
		exportXForms.setEnabled(value);
	}

	public IconButton getCompareContent() {
		return compareContent;
	}

	public IconButton getImpactAnalysis() {
		return impactAnalysis;
	}

	public AbstractComponent getPreviewXForms() {
		return previewXForms;
	}

	public void setEnabledImportAbcd(boolean enabled) {
		importAbcdForm.setEnabled(enabled);
	}

	public void setEnabledLinkAbcd(boolean enabled) {
		linkAbcdForm.setEnabled(enabled);
	}

	public void addExportXFormsListener(ClickListener listener) {
		exportXForms.addClickListener(listener);
	}

	public BrowserWindowOpener getOpener() {
		return opener;
	}

	public IconButton getSubmenuNew() {
		return submenuNew;
	}
}
