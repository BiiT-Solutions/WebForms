package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowTextArea;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.EditInfoListener;
import com.biit.webforms.gui.components.WindowNameGroup;
import com.biit.webforms.gui.components.utils.RootForm;
import com.biit.webforms.gui.webpages.formmanager.TreeTableFormVersion;
import com.biit.webforms.gui.webpages.formmanager.UpperMenuProjectManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.FormGeneratorPdf;
import com.biit.webforms.pdfgenerator.FormPdfGenerator;
import com.biit.webforms.persistence.entity.Form;
import com.lowagie.text.DocumentException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormManager extends SecuredWebPage {

	private static final long serialVersionUID = 4853622392162188013L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

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
		// If it was already null
		updateUpperMenu();

		getWorkingArea().addComponent(formTable);
	}

	private UpperMenuProjectManager createUpperMenu() {
		UpperMenuProjectManager upperMenu = new UpperMenuProjectManager(new StreamSource() {
			private static final long serialVersionUID = -5165661957366294565L;

			@Override
			public InputStream getStream() {
				try {
					return FormGeneratorPdf.generatePdf(new FormPdfGenerator((Form) formTable.getValue()));
				} catch (IOException | DocumentException e) {
					WebformsLogger.errorMessage(FormManager.class.getName(), e);
					MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
				}
				return null;
			}
		}, "default.pdf");
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
		final WindowNameGroup newFormWindow = new WindowNameGroup(LanguageCodes.COMMON_CAPTION_NAME.translation(),
				LanguageCodes.COMMON_CAPTION_GROUP.translation(),new IActivity[]{WebformsActivity.FORM_EDITING});
		newFormWindow.setCaption(LanguageCodes.CAPTION_NEW_FORM.translation());
		newFormWindow.setDefaultValue(LanguageCodes.NULL_VALUE_NEW_FORM.translation());
		newFormWindow.showCentered();
		newFormWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (newFormWindow.getValue() == null || newFormWindow.getValue().isEmpty()) {
					MessageManager.showWarning(LanguageCodes.COMMON_WARNING_TITLE_FORM_NOT_CREATED,
							LanguageCodes.COMMON_WARNING_DESCRIPTION_FORM_NEEDS_NAME);
					return;
				}
				try {
					Form newForm = UserSessionHandler.getController().createForm(newFormWindow.getValue(),
							newFormWindow.getOrganization());
					addFormToTable(newForm);
					newFormWindow.close();
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
		return activityPermissions;
	}

	public Form getSelectedForm() {
		Form form = (Form) formTable.getValue();
		return form;
	}

	private void updateUpperMenu() {
		upperMenu.getNewForm().setEnabled(true);

		Object row = formTable.getValue();

		try {
			boolean rowNotNull = row != null;
			Form selectedForm = (Form) formTable.getValue();
			boolean rowInstanceOfRootForm = row instanceof RootForm;
			boolean rowNotNullAndForm = rowNotNull && !rowInstanceOfRootForm;
			boolean canCreateForms = WebformsAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(
					UserSessionHandler.getUser(), WebformsActivity.FORM_EDITING);
			boolean canCreateNewVersion = WebformsAuthorizationService.getInstance().isAuthorizedActivity(
					UserSessionHandler.getUser(), selectedForm, WebformsActivity.FORM_NEW_VERSION);

			upperMenu.getNewForm().setEnabled(canCreateForms);
			upperMenu.getNewFormVersion().setEnabled(rowNotNull && canCreateNewVersion);
			upperMenu.getEditDesign().setEnabled(rowNotNullAndForm);
			upperMenu.getEditFlow().setEnabled(rowNotNullAndForm);
			upperMenu.getExportPdf().setEnabled(rowNotNullAndForm);
			if (rowNotNull) {
				upperMenu.setExportFormPdfDefaultName(((Form) row).getName() + ".pdf");
			}

		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			// failsafe, disable everything.
			upperMenu.getNewFormVersion().setEnabled(false);
			upperMenu.getEditDesign().setEnabled(false);
			upperMenu.getEditFlow().setEnabled(false);
			upperMenu.getExportPdf().setEnabled(false);
		}
	}
}
