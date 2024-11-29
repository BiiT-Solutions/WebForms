package com.biit.webforms.gui.components;

import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.BottomMenu;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class FormEditBottomMenu extends BottomMenu {
	private static final long serialVersionUID = 5814263369658754770L;

	private IconButton editFormButton, editFlowButton, editWebserviceCall, validateForm, compareStructure;
	private List<LockFormListener> listeners;

	public FormEditBottomMenu() {
		listeners = new ArrayList<FormEditBottomMenu.LockFormListener>();

		editFormButton = new IconButton(LanguageCodes.CAPTION_EDIT_FORM_DESIGN, ThemeIcons.EDIT_FORM_DESIGN,
				LanguageCodes.TOOLTIP_EDIT_FORM_DESIGN, IconSize.BIG);
		editFlowButton = new IconButton(LanguageCodes.CAPTION_EDIT_FORM_FLOW, ThemeIcons.EDIT_FORM_FLOW,
				LanguageCodes.TOOLTIP_EDIT_FORM_FLOW, IconSize.BIG);
		editWebserviceCall = new IconButton(LanguageCodes.CAPTION_EDIT_WEBSERVICE_CALL, ThemeIcons.EDIT_WEBSERVICE_CALL,
				LanguageCodes.TOOLTIP_EDIT_WEBSERVICE_CALL, IconSize.BIG);
		validateForm = new IconButton(LanguageCodes.CAPTION_VALIDATE_FORM, ThemeIcons.VALIDATE_FORM, LanguageCodes.TOOLTIP_VALIDATE_FORM,
				IconSize.BIG);
		compareStructure = new IconButton(LanguageCodes.CAPTION_COMPARE_STRUCTURE, ThemeIcons.COMPARE_STRUCTURE,
				LanguageCodes.TOOLTIP_COMPARE_STRUCTURE, IconSize.BIG);

		editFormButton.addClickListener((ClickListener) event -> {
            lockForm();
            ApplicationUi.navigateTo(WebMap.DESIGNER_EDITOR);
        });
		editFlowButton.addClickListener((ClickListener) event -> {
            lockForm();
            ApplicationUi.navigateTo(WebMap.FLOW_EDITOR);
        });
		editWebserviceCall.addClickListener((ClickListener) event -> {
            lockForm();
            ApplicationUi.navigateTo(WebMap.WEBSERVICE_CALL_EDITOR);
        });
		validateForm.addClickListener((ClickListener) event -> {
            lockForm();
            ApplicationUi.navigateTo(WebMap.VALIDATION);
        });
		compareStructure.addClickListener((ClickListener) event -> {
            lockForm();
            ApplicationUi.navigateTo(WebMap.COMPARE_STRUCTURE);
        });

		addIconButton(editFormButton);
		addIconButton(editFlowButton);
		addIconButton(editWebserviceCall);
		addIconButton(validateForm);
		addIconButton(compareStructure);
	}

	public IconButton getEditFormButton() {
		return editFormButton;
	}

	public IconButton getEditFlowButton() {
		return editFlowButton;
	}

	public IconButton getEditWebserviceCall() {
		return editWebserviceCall;
	}

	public IconButton getValidateForm() {
		return validateForm;
	}

	private void lockForm() {
		if (ApplicationUi.getController().getFormInUse() == null) {
			fireLockFormListeners();
		}
	}

	public void addLockFormListener(LockFormListener listener) {
		listeners.add(listener);
	}

	private void fireLockFormListeners() {
		for (LockFormListener listener : listeners) {
			listener.lockForm();
		}
	}

	public interface LockFormListener {
		public void lockForm();
	}

	public AbstractComponent getCompareStructureButton() {
		return compareStructure;
	}

}
