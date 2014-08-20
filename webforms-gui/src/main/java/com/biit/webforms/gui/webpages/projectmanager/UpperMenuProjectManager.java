package com.biit.webforms.gui.webpages.projectmanager;

import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.common.components.UpperMenu;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickListener;

/**
 * Upper menu for project manager web.
 * @author joriz_000
 *
 */
public class UpperMenuProjectManager extends UpperMenu {
	private static final long serialVersionUID = -3687306989433923394L;

	private IconButton newForm, newFormVersion, editDesign, editFlow;

	public UpperMenuProjectManager() {
		super();

		newForm = new IconButton(LanguageCodes.CAPTION_NEW_FORM, ThemeIcons.FORM_MANAGER_PAGE,
				LanguageCodes.TOOLTIP_NEW_FORM, IconSize.BIG);
		newFormVersion = new IconButton(LanguageCodes.CAPTION_NEW_FORM_VERSION, ThemeIcons.FORM_MANAGER_PAGE,
				LanguageCodes.TOOLTIP_NEW_FORM_VERSION, IconSize.BIG);
		editDesign = new IconButton(LanguageCodes.CAPTION_DESIGN, ThemeIcons.FORM_MANAGER_PAGE,
				LanguageCodes.TOOLTIP_DESIGN, IconSize.BIG);
		editFlow = new IconButton(LanguageCodes.CAPTION_FLOW, ThemeIcons.FORM_MANAGER_PAGE,
				LanguageCodes.TOOLTIP_FLOW, IconSize.BIG);

		addIconButton(newForm);
		addIconButton(newFormVersion);
		addIconButton(editDesign);
		addIconButton(editFlow);
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
}
