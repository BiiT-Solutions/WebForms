package com.biit.webforms.gui.webpages.formmanager;

import java.io.IOException;

import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.components.UpperMenuWebforms;
import com.biit.webforms.gui.xforms.OrbeonPreviewFrame;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.security.WebformsActivity;
import com.biit.webforms.theme.ThemeIcons;
import com.biit.webforms.xforms.XFormsSimpleFormExporter;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Upper menu for project manager web.
 * 
 */
public class UpperMenuProjectManager extends UpperMenuWebforms {
	private static final long serialVersionUID = -3687306989433923394L;

	private final IconButton submenuNew, newForm, newFormVersion, webformReference, removeForm, importAbcdForm, importJsonForm;
	private final IconButton linkAbcdForm;
	private final IconButton exportXForms, previewXForms, publishXForms, downloadXForms, downloadXFormsMultiple;
	private final IconButton export, exportPdf, exportFlowPdf, exportXsd, exportJson, exportXml, exportBaseFormMetadataJson;
	private final IconButton impactAnalysis, compareContent;
	private BrowserWindowOpener opener;
	// Neede due to the existence of a second 'Flow' button at the same time in
	// the interface
	private static final String FLOW_BUTTON_ID = "exportFlowButton";

	public UpperMenuProjectManager() {
		super();

		boolean enableExportJson = false;
		boolean enableImportJson = false;
		try {
			enableExportJson = getWebformsSecurityService().isUserAuthorizedInAnyOrganization(UserSessionHandler.getUser(),
					WebformsActivity.EXPORT_JSON);
			enableImportJson = getWebformsSecurityService().isUserAuthorizedInAnyOrganization(UserSessionHandler.getUser(),
					WebformsActivity.IMPORT_JSON);
		} catch (IOException | AuthenticationRequired e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}

		newForm = new IconButton(LanguageCodes.CAPTION_NEW_FORM, ThemeIcons.FORM_MANAGER_ADD_FORM, LanguageCodes.TOOLTIP_NEW_FORM,
				IconSize.BIG);
		newFormVersion = new IconButton(LanguageCodes.CAPTION_NEW_FORM_VERSION, ThemeIcons.FORM_MANAGER_NEW_VERSION,
				LanguageCodes.TOOLTIP_NEW_FORM_VERSION, IconSize.BIG);

		importAbcdForm = new IconButton(LanguageCodes.CAPTION_IMPORT_ABCD_FORM, ThemeIcons.FORM_MANAGER_IMPORT_ABCD_FORM,
				LanguageCodes.TOOLTIP_IMPORT_ABCD_FORM, IconSize.BIG);
		importJsonForm = new IconButton(LanguageCodes.CAPTION_IMPORT_JSON_FORM, ThemeIcons.FORM_MANAGER_IMPORT_JSON_FORM,
				LanguageCodes.TOOLTIP_IMPORT_JSON_FORM, IconSize.BIG);
		importJsonForm.setVisible(enableImportJson);

		webformReference = new IconButton(LanguageCodes.CAPTION_LINK_WEBFORMS_FORM, ThemeIcons.FORM_MANAGER_LINK_WEBFORMS_FORM,
				LanguageCodes.TOOLTIP_LINK_WEBFORMS_FORM, IconSize.BIG);

		linkAbcdForm = new IconButton(LanguageCodes.CAPTION_LINK_ABCD_FORM, ThemeIcons.FORM_MANAGER_LINK_ABCD_FORM,
				LanguageCodes.TOOLTIP_LINK_ABCD_FORM, IconSize.BIG);

		exportPdf = new IconButton(LanguageCodes.COMMON_CAPTION_EXPORT_TO_PDF, ThemeIcons.EXPORT_FORM_TO_PDF,
				LanguageCodes.COMMON_TOOLTIP_EXPORT_TO_PDF, IconSize.BIG);
		exportFlowPdf = new IconButton(LanguageCodes.CAPTION_PRINT_FLOW, ThemeIcons.EXPORT_FORM_TO_PDF, LanguageCodes.TOOLTIP_PRINT_FLOW,
				IconSize.BIG);
		exportFlowPdf.setId(FLOW_BUTTON_ID);
		exportXsd = new IconButton(LanguageCodes.CAPTION_EXPORT_XSD, ThemeIcons.EXPORT_XSD, LanguageCodes.TOOLTIP_EXPORT_XSD, IconSize.BIG);
		exportJson = new IconButton(LanguageCodes.CAPTION_EXPORT_JSON, ThemeIcons.EXPORT_JSON, LanguageCodes.TOOLTIP_EXPORT_JSON,
				IconSize.BIG);
		exportJson.setVisible(enableExportJson);

		exportXml = new IconButton(LanguageCodes.CAPTION_EXPORT_XML, ThemeIcons.EXPORT_XML, LanguageCodes.TOOLTIP_EXPORT_XML, IconSize.BIG);

		exportBaseFormMetadataJson = new IconButton(LanguageCodes.CAPTION_EXPORT_FORM_METADATA, ThemeIcons.EXPORT_JSON,
				LanguageCodes.TOOLTIP_EXPORT_FORM_METADATA, IconSize.BIG);
		exportBaseFormMetadataJson.setVisible(enableExportJson);

		opener = new BrowserWindowOpener(OrbeonPreviewFrame.class);
		opener.setParameter(OrbeonPreviewFrame.APPLICATION_PARAMETER_TAG, XFormsSimpleFormExporter.APP_NAME);
		opener.setFeatures("target=_new");
		previewXForms = new IconButton(LanguageCodes.CAPTION_PREVIEW_XFORMS, ThemeIcons.PREVIEW_XFORMS,
				LanguageCodes.TOOLTIP_PREVIEW_XFORMS, IconSize.BIG);

		publishXForms = new IconButton(LanguageCodes.CAPTION_PUBLISH_XFORMS, ThemeIcons.PUBLISH_XFORMS,
				LanguageCodes.TOOLTIP_PUBLISH_XFORMS, IconSize.BIG);
		downloadXForms = new IconButton(LanguageCodes.CAPTION_DOWNLOAD_XFORMS, ThemeIcons.DOWNLOAD_XFORMS,
				LanguageCodes.TOOLTIP_DOWNLOAD_XFORMS, IconSize.BIG);
		if (WebformsConfigurationReader.getInstance().isXFormsToMultipleFilesEnabled()) {
			downloadXFormsMultiple = new IconButton(LanguageCodes.CAPTION_DOWNLOAD_XFORMS_MULTIPLE, ThemeIcons.DOWNLOAD_XFORMS_MULTIPLE,
					LanguageCodes.TOOLTIP_DOWNLOAD_XFORMS_MULTIPLE, IconSize.BIG);
		} else {
			downloadXFormsMultiple = null;
		}

		impactAnalysis = new IconButton(LanguageCodes.CAPTION_IMPACT_ANALYSIS, ThemeIcons.IMPACT_ANALYSIS,
				LanguageCodes.TOOLTIP_IMPACT_ANALISYS, IconSize.BIG);

		compareContent = new IconButton(LanguageCodes.CAPTION_COMPARE_CONTENT, ThemeIcons.COMPARE_CONTENT,
				LanguageCodes.TOOLTIP_COMPARE_CONTENT, IconSize.BIG);

		submenuNew = addSubMenu(ThemeIcons.NEW, LanguageCodes.CAPTION_NEW, LanguageCodes.TOOLTIP_NEW, newForm, webformReference,
				newFormVersion, importAbcdForm, importJsonForm);

		removeForm = new IconButton(LanguageCodes.CAPTION_REMOVE_FORM, ThemeIcons.DELETE_FORM, LanguageCodes.CAPTION_REMOVE_FORM,
				IconSize.MEDIUM);
		addIconButton(removeForm);

		addIconButton(linkAbcdForm);

		export = addSubMenu(ThemeIcons.EXPORT, LanguageCodes.CAPTION_EXPORT, LanguageCodes.TOOLTIP_EXPORT, exportPdf, exportFlowPdf,
				exportXsd, exportXml, exportJson, exportBaseFormMetadataJson);
		exportXForms = addSubMenu(ThemeIcons.EXPORT_FORM_TO_XFORMS, LanguageCodes.CAPTION_TO_XFORMS, LanguageCodes.TOOLTIP_TO_XFORMS,
				previewXForms, publishXForms, downloadXForms, downloadXFormsMultiple);
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

	public void addWebformReferenceListener(ClickListener listener) {
		webformReference.addClickListener(listener);
	}

	public void addImportAbcdForm(ClickListener listener) {
		importAbcdForm.addClickListener(listener);
	}

	public void addImportJsonForm(ClickListener listener) {
		importJsonForm.addClickListener(listener);
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

	public void addExportBaseFormMetadataJsonListener(ClickListener listener) {
		exportBaseFormMetadataJson.addClickListener(listener);
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

	public void addPreviewXFormsListener(ClickListener listener) {
		previewXForms.addClickListener(listener);
	}

	public void addPublishXFormsListener(ClickListener listener) {
		publishXForms.addClickListener(listener);
	}

	public void addDownloadXFormsListener(ClickListener listener) {
		downloadXForms.addClickListener(listener);
	}

	public void addDownloadXFormsMultipleListener(ClickListener listener) {
		if (downloadXFormsMultiple != null) {
			downloadXFormsMultiple.addClickListener(listener);
		}
	}

	public void addRemoveFormListener(ClickListener listener) {
		removeForm.addClickListener(listener);
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

	public void addExportXmlListener(ClickListener listener) {
		exportXml.addClickListener(listener);
	}

	public BrowserWindowOpener getOpener() {
		return opener;
	}

	public IconButton getSubmenuNew() {
		return submenuNew;
	}

	public IconButton getExportXml() {
		return exportXml;
	}

	public IconButton getRemoveForm() {
		return removeForm;
	}

	public IconButton getWebformReference() {
		return webformReference;
	}
}
