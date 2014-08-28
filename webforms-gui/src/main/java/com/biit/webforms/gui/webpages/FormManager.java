package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.util.List;

import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowStringInput;
import com.biit.webforms.gui.common.components.WindowTextArea;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.EditInfoListener;
import com.biit.webforms.gui.components.TreeTableFormVersion;
import com.biit.webforms.gui.components.utils.RootForm;
import com.biit.webforms.gui.webpages.formmanager.UpperMenuProjectManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.FormPdfGenerator;
import com.biit.webforms.pdfgenerator.NeoFormGeneratorPDF;
import com.biit.webforms.persistence.entity.Form;
import com.lowagie.text.DocumentException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormManager extends SecuredWebPage {

	private static final long serialVersionUID = 4853622392162188013L;

	private TreeTableFormVersion formTable;
	private UpperMenuProjectManager upperMenu;

	@Override
	protected void initContent() {
		UserSessionHandler.getController().clearFormInUse();

		setAsCentralPanel();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);

		formTable = new TreeTableFormVersion();
		formTable.setSizeFull();
		formTable.addEditInfoListener(new EditInfoListener() {
			@Override
			public void editInfo(Form form) {
				openEditInfoWindow(form);
			}
		});
		formTable.setValue(null);
		formTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8544416078101328528L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUpperMenu();
			}
		});
		formTable.selectLastUsedForm();

		getWorkingArea().addComponent(formTable);
	}

	private UpperMenuProjectManager createUpperMenu() {
		UpperMenuProjectManager upperMenu = new UpperMenuProjectManager();
		upperMenu.addNewFormListener(new ClickListener() {
			private static final long serialVersionUID = 8958665495299558548L;

			@Override
			public void buttonClick(ClickEvent event) {
				openNewFormWindow();
			}
		});
		upperMenu.addNewFormVersionListener(new ClickListener() {
			private static final long serialVersionUID = 2014729211949601816L;

			@Override
			public void buttonClick(ClickEvent event) {
				newFormVersion();
			}
		});
		upperMenu.addEditDesignListener(new ClickListener() {
			private static final long serialVersionUID = 3219306519363402501L;

			@Override
			public void buttonClick(ClickEvent event) {
				UserSessionHandler.getController().setFormInUse(getSelectedForm());
				ApplicationUi.navigateTo(WebMap.DESIGNER_EDITOR);
			}
		});
		upperMenu.addEditFlowListener(new ClickListener() {
			private static final long serialVersionUID = -9206268545659478851L;

			@Override
			public void buttonClick(ClickEvent event) {
				UserSessionHandler.getController().setFormInUse(getSelectedForm());
				ApplicationUi.navigateTo(WebMap.FLOW_EDITOR);
			}
		});
		upperMenu.addExportPdf(new ClickListener() {
			private static final long serialVersionUID = 2864457152577148777L;

			@Override
			public void buttonClick(ClickEvent event) {
				Form selectedForm = (Form) formTable.getValue();
				if (selectedForm != null) {
					try {
						NeoFormGeneratorPDF.generatePDF("kiwi.pdf", new FormPdfGenerator(selectedForm));
					} catch (IOException | DocumentException e) {
						WebformsLogger.errorMessage(FormManager.class.getName(), e);
					}
				}
			}
		});
		return upperMenu;
	}

	protected void newFormVersion() {
		Form newForm;
		try {
			RootForm rootForm = formTable.getSelectedRootForm();
			Form currentForm = rootForm.getLastFormVersion();

			newForm = UserSessionHandler.getController().createNewFormVersion(currentForm);
			addFormToTable(newForm);
		} catch (NotValidTreeObjectException e) {
			MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
		}
	}

	private void openNewFormWindow() {
		final WindowStringInput stringWindow = new WindowStringInput(LanguageCodes.COMMON_CAPTION_NAME.translation());
		stringWindow.setCaption(LanguageCodes.CAPTION_NEW_FORM.translation());
		stringWindow.setDefaultValue(LanguageCodes.NULL_VALUE_NEW_FORM.translation());
		stringWindow.showCentered();
		stringWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (stringWindow.getValue() == null || stringWindow.getValue().isEmpty()) {
					MessageManager.showWarning(LanguageCodes.COMMON_WARNING_TITLE_FORM_NOT_CREATED,
							LanguageCodes.COMMON_WARNING_DESCRIPTION_FORM_NEEDS_NAME);
					return;
				}
				try {
					Form newForm = UserSessionHandler.getController().createForm(stringWindow.getValue());
					addFormToTable(newForm);
					stringWindow.close();
				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				}
			}
		});
	}

	protected void openEditInfoWindow(final Form form) {
		final WindowTextArea textWindow = new WindowTextArea(LanguageCodes.COMMON_CAPTION_DESCRIPTION.translation());
		textWindow.setCaption(LanguageCodes.COMMON_CAPTION_EDIT_DESCRIPTION.translation());
		textWindow.setValue(form.getDescription());
		textWindow.showCentered();
		textWindow.addAcceptActionListener(new AcceptActionListener() {
			@Override
			public void acceptAction(WindowAcceptCancel window) {
				try {
					UserSessionHandler.getController().changeFormDescription(form, textWindow.getValue());
					textWindow.close();
				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				}
			}
		});
	}

	private void addFormToTable(Form form) {
		formTable.addForm(form);
		formTable.selectForm(form);
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	public Form getSelectedForm() {
		Form form = (Form) formTable.getValue();
		return form;
	}

	private void updateUpperMenu() {
		upperMenu.getNewForm().setEnabled(true);

		Object row = formTable.getValue();
		if (row == null) {
			upperMenu.getNewFormVersion().setEnabled(false);
			upperMenu.getEditDesign().setEnabled(false);
			upperMenu.getEditFlow().setEnabled(false);
		} else {
			upperMenu.getNewFormVersion().setEnabled(true);
			if (row instanceof RootForm) {
				upperMenu.getEditDesign().setEnabled(false);
				upperMenu.getEditFlow().setEnabled(false);
				upperMenu.getExportPdf().setEnabled(false);
			} else {
				upperMenu.getEditDesign().setEnabled(true);
				upperMenu.getEditFlow().setEnabled(true);
				upperMenu.getExportPdf().setEnabled(true);
			}
		}
	}
}
