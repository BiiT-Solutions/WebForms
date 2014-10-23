package com.biit.webforms.gui.webpages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.interfaces.IBaseFormView;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.authentication.exception.NewVersionWithoutFinalDesignException;
import com.biit.webforms.authentication.exception.NotValidAbcdForm;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.gui.common.components.WindowProceedAction;
import com.biit.webforms.gui.common.components.WindowTextArea;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.components.FormEditBottomMenu.LockFormListener;
import com.biit.webforms.gui.components.WindowNameGroup;
import com.biit.webforms.gui.components.utils.RootForm;
import com.biit.webforms.gui.webpages.formmanager.TreeTableFormVersion;
import com.biit.webforms.gui.webpages.formmanager.UpperMenuProjectManager;
import com.biit.webforms.gui.webpages.formmanager.WindowImportAbcdForms;
import com.biit.webforms.gui.webpages.formmanager.WindowLinkAbcdForm;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.FormGeneratorPdf;
import com.biit.webforms.pdfgenerator.FormPdfGenerator;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.GraphvizApp;
import com.biit.webforms.utils.GraphvizApp.ImgType;
import com.liferay.portal.model.Organization;
import com.lowagie.text.DocumentException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormManager extends SecuredWebPage {

	private static final long serialVersionUID = 4853622392162188013L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

	private TreeTableFormVersion formTable;
	private UpperMenuProjectManager upperMenu;
	private FormEditBottomMenu bottomMenu;

	@Override
	protected void initContent() {
		UserSessionHandler.getController().clearFormInUse();

		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		bottomMenu = createBottomMenu();

		setUpperMenu(upperMenu);
		setBottomMenu(bottomMenu);

		formTable = new TreeTableFormVersion(UserSessionHandler.getController().getTreeTableFormsProvider());
		formTable.setSizeFull();
		formTable.setValue(null);
		formTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8544416078101328528L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateMenus();
			}
		});
		formTable.selectLastUsedForm();
		// If it was already null
		updateMenus();

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
		upperMenu.addFinishListener(new ClickListener() {
			private static final long serialVersionUID = 8869180038869702710L;

			@Override
			public void buttonClick(ClickEvent event) {
				finishForm();
			}
		});
		upperMenu.addNewFormVersionListener(new ClickListener() {
			private static final long serialVersionUID = 2014729211949601816L;

			@Override
			public void buttonClick(ClickEvent event) {
				newFormVersion();
			}
		});
		upperMenu.addImportAbcdForm(new ClickListener() {
			private static final long serialVersionUID = -2591404148252216954L;

			@Override
			public void buttonClick(ClickEvent event) {
				importAbcdForm();
			}
		});
		upperMenu.addLinkAbcdForm(new ClickListener() {
			private static final long serialVersionUID = 2864457152577148777L;

			@Override
			public void buttonClick(ClickEvent event) {
				linkAbcdForm();
			}
		});
		upperMenu.addExportPdf(new ClickListener() {
			private static final long serialVersionUID = 2864457152577148777L;

			@Override
			public void buttonClick(ClickEvent event) {
				WindowDownloader downloader = new WindowDownloader(new WindowDownloaderProcess() {

					@Override
					public InputStream getInputStream() {
						try {
							return FormGeneratorPdf.generatePdf(new FormPdfGenerator((Form) formTable.getValue()));
						} catch (IOException | DocumentException e) {
							WebformsLogger.errorMessage(FormManager.class.getName(), e);
							MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
						}
						return null;
					}
				});
				downloader.setIndeterminate(true);
				downloader.setFilename(((Form) formTable.getValue()).getLabel() + ".pdf");
				downloader.showCentered();
			}
		});
		upperMenu.addExportFlowPdfListener(new ClickListener() {
			private static final long serialVersionUID = -1790801212813909643L;

			@Override
			public void buttonClick(ClickEvent event) {
				WindowDownloader window = new WindowDownloader(new WindowDownloaderProcess() {

					@Override
					public InputStream getInputStream() {
						try {
							return new ByteArrayInputStream(GraphvizApp.generateImage((Form) formTable.getValue(),
									null, ImgType.PDF));
						} catch (IOException | InterruptedException e) {
							WebformsLogger.errorMessage(this.getClass().getName(), e);
							return null;
						}
					}
				});
				window.setIndeterminate(true);
				window.setFilename(((Form) formTable.getValue()).getLabel() + ".pdf");
				window.showCentered();
			}
		});
		return upperMenu;
	}

	protected void finishForm() {
		final Form form = (Form) formTable.getForm();
		new WindowProceedAction(LanguageCodes.TEXT_PROCEED_FORM_CLOSE, new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				UserSessionHandler.getController().finishForm(form);
				formTable.refreshTableData();
				formTable.setValue(form);
			}
		});
	}

	/**
	 * Opens window to link form with abcd form and version.
	 */
	protected void linkAbcdForm() {
		final Form form = (Form)formTable.getForm();
		WindowLinkAbcdForm linkAbcdForm = new WindowLinkAbcdForm();
		for (SimpleFormView simpleFormView : UserSessionHandler.getController().getSimpleFormDaoAbcd().getAll()) {
			linkAbcdForm.add(simpleFormView);
		}
		linkAbcdForm.setValue(UserSessionHandler.getController().getLinkedSimpleAbcdForms(form));
		linkAbcdForm.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowLinkAbcdForm linkWindow = (WindowLinkAbcdForm) window;
				
				form.setLinkedForms(linkWindow.getValue());
				UserSessionHandler.getController().saveForm(form);
				formTable.refreshTableData();
				formTable.setValue(form);
				
				window.close();
			}
		});
		linkAbcdForm.showCentered();
	}

	protected void importAbcdForm() {
		final WindowImportAbcdForms importAbcdForm = new WindowImportAbcdForms(UserSessionHandler.getController()
				.getTreeTableSimpleAbcdFormsProvider());
		importAbcdForm.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {

				// Check name and organization
				String newFormName = importAbcdForm.getImportName();
				Organization newFormOrganization = importAbcdForm.getOrganization();

				if (newFormName == null || newFormName.isEmpty()) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_IMPORT_FAILED,
							LanguageCodes.WARNING_DESCRIPTION_NAME_NOT_VALID);
					return;
				}
				if (newFormOrganization == null) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_IMPORT_FAILED,
							LanguageCodes.WARNING_DESCRIPTION_NULL_ORGANIZATION);
					return;
				}

				// Try to import the form.
				try {
					SimpleFormView abcdForm = importAbcdForm.getForm();
					Form importedForm = UserSessionHandler.getController().importAbcdForm(abcdForm, newFormName,
							newFormOrganization);
					formTable.addForm(importedForm, true);
					formTable.setValue(importedForm);
					window.close();
				} catch (NotValidAbcdForm e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_IMPORT_FAILED,
							LanguageCodes.WARNING_DESCRIPTION_NOT_VALID_ABCD_FORM);
				} catch (FieldTooLongException | CharacterNotAllowedException e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_IMPORT_FAILED,
							LanguageCodes.WARNING_DESCRIPTION_NAME_NOT_VALID);
				} catch (FormWithSameNameException e) {
					MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_IMPORT_FAILED,
							LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				}

			}
		});
		importAbcdForm.showCentered();
	}

	private FormEditBottomMenu createBottomMenu() {
		FormEditBottomMenu bottomMenu = new FormEditBottomMenu();
		bottomMenu.addLockFormListener(new LockFormListener() {

			@Override
			public void lockForm() {
				UserSessionHandler.getController().setFormInUse(getSelectedForm());
			}
		});
		return bottomMenu;
	}

	protected void newFormVersion() {
		Form newForm;
		try {
			RootForm rootForm = formTable.getSelectedRootForm();
			Form currentForm = (Form) rootForm.getLastFormVersion();

			newForm = UserSessionHandler.getController().createNewFormVersion(currentForm);
			addFormToTable(newForm);
			formTable.defaultSort();
		} catch (NotValidStorableObjectException e) {
			MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
		} catch (NewVersionWithoutFinalDesignException e) {
			MessageManager.showWarning(LanguageCodes.WARNING_CAPTION_NOT_ALLOWED,
					LanguageCodes.WARNING_DESCRIPTION_NEW_VERSION_WHEN_DESIGN);
		} catch (CharacterNotAllowedException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private void openNewFormWindow() {
		final WindowNameGroup newFormWindow = new WindowNameGroup(LanguageCodes.COMMON_CAPTION_NAME.translation(),
				LanguageCodes.COMMON_CAPTION_GROUP.translation(), new IActivity[] { WebformsActivity.FORM_EDITING });
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
					Form newForm = UserSessionHandler.getController().createFormAndPersist(newFormWindow.getValue(),
							newFormWindow.getOrganization());
					addFormToTable(newForm);
					newFormWindow.close();
				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				} catch (CharacterNotAllowedException e) {
					// Impossible
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
	}

	private void openEditInfoWindow(final Form form) {
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
		formTable.setValue(form);
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	public Form getSelectedForm() {
		Form form = (Form) formTable.getValue();
		return form;
	}

	private void updateMenus() {
		upperMenu.getNewForm().setEnabled(true);

		Object row = formTable.getValue();

		try {
			// Top menu
			boolean rowNotNull = row != null;
			Form selectedForm = (Form) formTable.getValue();
			boolean rowInstanceOfRootForm = row instanceof RootForm;
			boolean rowNotNullAndForm = rowNotNull && !rowInstanceOfRootForm;
			boolean canCreateForms = WebformsAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(
					UserSessionHandler.getUser(), WebformsActivity.FORM_EDITING);
			boolean canCreateNewVersion = WebformsAuthorizationService.getInstance().isAuthorizedActivity(
					UserSessionHandler.getUser(), selectedForm, WebformsActivity.FORM_NEW_VERSION);
			boolean canLinkVersion = WebformsAuthorizationService.getInstance().isAuthorizedActivity(
					UserSessionHandler.getUser(), selectedForm, WebformsActivity.FORM_EDITING);

			upperMenu.setEnabled(true);
			upperMenu.getNewForm().setEnabled(canCreateForms);
			upperMenu.getFinish().setEnabled(rowNotNullAndForm && canCreateForms);
			upperMenu.getNewFormVersion().setEnabled(rowNotNull && canCreateNewVersion);
			upperMenu.getLinkAbcdForm().setEnabled(rowNotNullAndForm && canLinkVersion);
			upperMenu.getExportPdf().setEnabled(rowNotNullAndForm);
			upperMenu.getExportFlowPdf().setEnabled(rowNotNullAndForm);

			// Bottom menu
			bottomMenu.getEditFormButton().setEnabled(rowNotNullAndForm);
			bottomMenu.getEditFlowButton().setEnabled(rowNotNullAndForm);

		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			// failsafe, disable everything.
			upperMenu.setEnabled(false);
			// Bottom menu
			bottomMenu.getEditFormButton().setEnabled(false);
			bottomMenu.getEditFlowButton().setEnabled(false);
		}
	}
}
