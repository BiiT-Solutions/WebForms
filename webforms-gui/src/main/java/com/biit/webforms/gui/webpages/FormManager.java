package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.util.List;

import com.biit.form.exceptions.FieldTooLongException;
import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.UpperMenu;
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
import com.biit.webforms.pdfconversor.FormPdfGenerator;
import com.biit.webforms.pdfconversor.NeoFormGeneratorPDF;
import com.biit.webforms.persistence.entity.Form;
import com.lowagie.text.DocumentException;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormManager extends SecuredWebPage {

	private static final long serialVersionUID = 4853622392162188013L;

	private TreeTableFormVersion formTable;
	private UpperMenu upperMenu;

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
		formTable.selectLastUsedForm();

		getWorkingArea().addComponent(formTable);
	}

	private UpperMenu createUpperMenu() {
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
				Form selectedForm = formTable.getValue();
				if (selectedForm != null && !(selectedForm instanceof RootForm)) {
					try {
						NeoFormGeneratorPDF.generatePDF("kiwi.pdf", new FormPdfGenerator(formTable.getValue()));
					} catch (IOException | DocumentException e) {
						WebformsLogger.errorMessage(FormManager.class.getName(), e);
					}
				}
			}
		});
		return upperMenu;
	}

	protected void newFormVersion() {
		Form newForm = UserSessionHandler.getController().createNewFormVersion(formTable.getValue());
		addFormToTable(newForm);
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
		Form form = formTable.getValue();

		return form;
	}
}
