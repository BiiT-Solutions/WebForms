package com.biit.webforms.gui.components;

import java.util.ArrayList;
import java.util.List;

import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.BottomMenu;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.IconSize;
import com.biit.webforms.gui.webpages.WebMap;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormEditBottomMenu extends BottomMenu {
	private static final long serialVersionUID = 5814263369658754770L;

	private IconButton editFormButton, editFlowButton, validateForm, compareStructure;
	private List<LockFormListener> listeners;

	public FormEditBottomMenu() {
		listeners = new ArrayList<FormEditBottomMenu.LockFormListener>();

		editFormButton = new IconButton(LanguageCodes.CAPTION_EDIT_FORM_DESIGN, ThemeIcons.EDIT_FORM_DESIGN,
				LanguageCodes.TOOLTIP_EDIT_FORM_DESIGN, IconSize.BIG);
		editFlowButton = new IconButton(LanguageCodes.CAPTION_EDIT_FORM_FLOW, ThemeIcons.EDIT_FORM_FLOW,
				LanguageCodes.TOOLTIP_EDIT_FORM_FLOW, IconSize.BIG);
		validateForm = new IconButton(LanguageCodes.CAPTION_VALIDATE_FORM, ThemeIcons.VALIDATE_FORM,
				LanguageCodes.TOOLTIP_VALIDATE_FORM, IconSize.BIG);
		compareStructure = new IconButton(LanguageCodes.CAPTION_COMPARE_STRUCTURE, ThemeIcons.COMPARE_STRUCTURE,
				LanguageCodes.TOOLTIP_COMPARE_STRUCTURE, IconSize.BIG);

		editFormButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 3789769131264938760L;

			@Override
			public void buttonClick(ClickEvent event) {
				lockForm();
				ApplicationUi.navigateTo(WebMap.DESIGNER_EDITOR);
			}
		});
		editFlowButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6946343459263059630L;

			@Override
			public void buttonClick(ClickEvent event) {
				lockForm();
				ApplicationUi.navigateTo(WebMap.FLOW_EDITOR);
			}
		});
		validateForm.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4309381087569767156L;

			@Override
			public void buttonClick(ClickEvent event) {
				lockForm();
				ApplicationUi.navigateTo(WebMap.VALIDATION);
			}
		});
		compareStructure.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 4927787198580829348L;

			@Override
			public void buttonClick(ClickEvent event) {
				lockForm();
				ApplicationUi.navigateTo(WebMap.COMPARE_STRUCTURE);
			}
		});


		addIconButton(editFormButton);
		addIconButton(editFlowButton);
		addIconButton(validateForm);
		addIconButton(compareStructure);

	}

	public IconButton getEditFormButton() {
		return editFormButton;
	}

	public IconButton getEditFlowButton() {
		return editFlowButton;
	}

	public IconButton getValidateForm() {
		return validateForm;
	}

	private void lockForm() {
		if (UserSessionHandler.getController().getFormInUse() == null) {
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

}
