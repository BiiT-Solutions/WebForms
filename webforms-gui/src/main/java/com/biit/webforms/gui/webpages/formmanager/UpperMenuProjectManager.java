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

	private IconButton newForm, newFormVersion, exportPdf;

	public UpperMenuProjectManager() {
		super();

		newForm = new IconButton(LanguageCodes.CAPTION_NEW_FORM, ThemeIcons.FORM_MANAGER_ADD_FORM,
				LanguageCodes.TOOLTIP_NEW_FORM, IconSize.BIG);
		newFormVersion = new IconButton(LanguageCodes.CAPTION_NEW_FORM_VERSION, ThemeIcons.FORM_MANAGER_NEW_VERSION,
				LanguageCodes.TOOLTIP_NEW_FORM_VERSION, IconSize.BIG);
		exportPdf = new IconButton(LanguageCodes.COMMON_CAPTION_EXPORT_TO_PDF, ThemeIcons.EXPORT_FORM_TO_PDF,
				LanguageCodes.COMMON_TOOLTIP_EXPORT_TO_PDF, IconSize.BIG);

		addIconButton(newForm);
		addIconButton(newFormVersion);
		addIconButton(exportPdf);
	}

	public void addNewFormListener(ClickListener listener) {
		newForm.addClickListener(listener);
	}

	public void addNewFormVersionListener(ClickListener listener) {
		newFormVersion.addClickListener(listener);
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

	public IconButton getExportPdf() {
		return exportPdf;
	}
}
