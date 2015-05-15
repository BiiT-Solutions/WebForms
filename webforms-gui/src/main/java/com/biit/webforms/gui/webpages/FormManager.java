package com.biit.webforms.gui.webpages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.security.AbcdAuthorizationService;
import com.biit.form.entity.IBaseFormView;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.IActivity;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.utils.validation.ValidateReport;
import com.biit.webforms.authentication.FormWithSameNameException;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.authentication.exception.BadAbcdLink;
import com.biit.webforms.authentication.exception.NewVersionWithoutFinalDesignException;
import com.biit.webforms.authentication.exception.NotValidAbcdForm;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.gui.common.components.WindowProceedAction;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.components.FormEditBottomMenu.LockFormListener;
import com.biit.webforms.gui.components.WindowNameGroup;
import com.biit.webforms.gui.components.utils.RootForm;
import com.biit.webforms.gui.webpages.formmanager.TreeTableFormVersion;
import com.biit.webforms.gui.webpages.formmanager.UpperMenuProjectManager;
import com.biit.webforms.gui.webpages.formmanager.WindowDownloaderBaseFormMetadataJson;
import com.biit.webforms.gui.webpages.formmanager.WindowDownloaderJson;
import com.biit.webforms.gui.webpages.formmanager.WindowDownloaderXsd;
import com.biit.webforms.gui.webpages.formmanager.WindowImpactAnalysis;
import com.biit.webforms.gui.webpages.formmanager.WindowImportAbcdForms;
import com.biit.webforms.gui.webpages.formmanager.WindowImportJson;
import com.biit.webforms.gui.webpages.formmanager.WindowLinkAbcdForm;
import com.biit.webforms.gui.xforms.OrbeonPreviewFrame;
import com.biit.webforms.gui.xforms.OrbeonUtils;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.pdfgenerator.FormGeneratorPdf;
import com.biit.webforms.pdfgenerator.FormPdfGenerator;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.persistence.xforms.XFormsPersistence;
import com.biit.webforms.utils.GraphvizApp;
import com.biit.webforms.utils.GraphvizApp.ImgType;
import com.biit.webforms.validators.ValidateFormComplete;
import com.biit.webforms.xforms.XFormsExporter;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;
import com.liferay.portal.model.Organization;
import com.lowagie.text.DocumentException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormManager extends SecuredWebPage {
	private static final long serialVersionUID = 4853622392162188013L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

	private TreeTableFormVersion formTable;
	private UpperMenuProjectManager upperMenu;
	private FormEditBottomMenu bottomMenu;

	private IFormDao formDao;

	public FormManager() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("webformsFormDao");
	}

	@Override
	protected void initContent() {
		UserSessionHandler.getController().clearFormInUse();

		setCentralPanelAsWorkingArea();
		createUpperMenu();
		bottomMenu = createBottomMenu();

		setUpperMenu(upperMenu);
		setBottomMenu(bottomMenu);

		formTable = new TreeTableFormVersion(UserSessionHandler.getController().getTreeTableFormsProvider());
		formTable.setImmediate(true);
		formTable.setSizeFull();
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
		upperMenu = new UpperMenuProjectManager();
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
		upperMenu.addImportAbcdForm(new ClickListener() {
			private static final long serialVersionUID = -2591404148252216954L;

			@Override
			public void buttonClick(ClickEvent event) {
				importAbcdForm();
			}
		});
		upperMenu.addImportJsonForm(new ClickListener() {
			private static final long serialVersionUID = -2591404148252216954L;

			@Override
			public void buttonClick(ClickEvent event) {
				importJsonForm();
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
			private static final long serialVersionUID = -3028644296823936596L;

			@Override
			public void buttonClick(ClickEvent event) {
				exportPdf();
			}
		});
		upperMenu.addExportFlowPdfListener(new ClickListener() {
			private static final long serialVersionUID = -1790801212813909643L;

			@Override
			public void buttonClick(ClickEvent event) {
				exportFlowPdf();
			}
		});
		upperMenu.addExportXFormsListener(new ClickListener() {
			private static final long serialVersionUID = -8790434440652432643L;

			@Override
			public void buttonClick(ClickEvent event) {
				Organization organization = WebformsAuthorizationService.getInstance().getOrganization(
						UserSessionHandler.getUser(), getSelectedForm().getOrganizationId());
				upperMenu.getOpener().setParameter(OrbeonPreviewFrame.FORM_PARAMETER_TAG,
						XFormsPersistence.formatFormName(getSelectedForm(), organization, true));
				upperMenu.getOpener().setParameter(OrbeonPreviewFrame.FORM_VERSION_PARAMETER_TAG,
						getSelectedForm().getVersion().toString());
			}
		});
		upperMenu.addExportXmlListener(new ClickListener() {
			private static final long serialVersionUID = -4167515599696676260L;

			@Override
			public void buttonClick(ClickEvent event) {
				exportXmlListener();
			}
		});
		// Add browser window opener to the button.
		upperMenu.addPreviewXForms(new ClickListener() {
			private static final long serialVersionUID = -8790434440652432643L;

			@Override
			public void buttonClick(ClickEvent event) {
				previewXForms();
			}
		});
		upperMenu.addPublishXForms(new ClickListener() {
			private static final long serialVersionUID = -4167515599696676260L;

			@Override
			public void buttonClick(ClickEvent event) {
				publishXForms();
			}
		});
		upperMenu.addDownloadXForms(new ClickListener() {
			private static final long serialVersionUID = -2359606371849802798L;

			@Override
			public void buttonClick(ClickEvent event) {
				downloadXForms();
			}
		});
		upperMenu.addExportXsdListener(new ClickListener() {
			private static final long serialVersionUID = -2359606371849802798L;

			@Override
			public void buttonClick(ClickEvent event) {
				exportXsd();
			}
		});
		upperMenu.addCompareContent(new ClickListener() {
			private static final long serialVersionUID = 1044352586328012252L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationUi.navigateTo(WebMap.COMPARE_CONTENT);
			}
		});
		upperMenu.addImpactAnalysisListener(new ClickListener() {
			private static final long serialVersionUID = 8831217333785785818L;

			@Override
			public void buttonClick(ClickEvent event) {
				impactAnalysis();
			}
		});
		upperMenu.addExportJsonListener(new ClickListener() {
			private static final long serialVersionUID = 2815280910647730087L;

			@Override
			public void buttonClick(ClickEvent event) {
				exportJson();
			}
		});
		upperMenu.addExportBaseFormMetadataJsonListener(new ClickListener() {
			private static final long serialVersionUID = -2359606371849802798L;

			@Override
			public void buttonClick(ClickEvent event) {
				exportBaseFormMetadataJson();
			}
		});
		upperMenu.addRemoveForm(new ClickListener() {
			private static final long serialVersionUID = -3264661636078442579L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (formTable.getValue() != null) {
					new WindowProceedAction(LanguageCodes.WARNING_REMOVE_ELEMENT, new AcceptActionListener() {
						@Override
						public void acceptAction(WindowAcceptCancel window) {
							removeSelectedForm();
							window.close();
						}
					});
				}
			}
		});
		return upperMenu;
	}

	private void removeSelectedForm() {
		Form selectedForm;
		try {
			selectedForm = formDao.get(((IWebformsFormView) formTable.getValue()).getId());
			if (selectedForm != null) {
				// Remove the form.
				formDao.makeTransient(selectedForm);
				WebformsLogger.info(this.getClass().getName(),
						"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has removed form '"
								+ selectedForm.getLabel() + "' (version " + selectedForm.getVersion() + ").");
				formTable.refreshTableData();
			}
		} catch (ElementCannotBeRemovedException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_TITLE);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private void importJsonForm() {
		WindowImportJson window = new WindowImportJson();
		window.showCentered();
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				formTable.refreshTableData();
			}
		});
	}

	private void exportXmlListener() {
		Form form = loadAndValidateForm();

		if (form != null) {
			WindowGenerateXml window = new WindowGenerateXml(form);
			window.showCentered();
		}
	}

	/**
	 * Loads a forms and tries to validate. If the form is not validated returns null
	 * 
	 * @return
	 */
	private CompleteFormView loadAndValidateForm() {
		CompleteFormView form = (CompleteFormView) loadCompleteForm(getSelectedForm());

		// Xforms only can use with valid forms.
		ValidateFormComplete validator = new ValidateFormComplete();
		validator.setStopOnFail(true);

		ValidateReport report = new ValidateReport();
		validator.validate((form), report);

		if (report.isValid()) {
			return form;
		} else {
			MessageManager.showError(LanguageCodes.ERROR_FORM_NOT_VALID, LanguageCodes.VALIDATE_FORM);
			return null;
		}
	}

	private void downloadXForms() {
		final Form form = loadAndValidateForm();
		if (form != null) {
			WindowDownloader window = new WindowDownloader(new WindowDownloaderProcess() {

				@Override
				public InputStream getInputStream() {
					try {
						return new XFormsExporter(form).generateXFormsLanguage();
					} catch (NotValidTreeObjectException | NotExistingDynamicFieldException | InvalidDateException
							| StringRuleSyntaxError | PostCodeRuleSyntaxError | NotValidChildException e) {
						WebformsLogger.errorMessage(this.getClass().getName(), e);
						return null;
					}
				}
			});
			window.setIndeterminate(true);
			window.setFilename(form.getLabel() + ".txt");
			window.showCentered();
		}
	}

	private void publishXForms() {
		Form form = loadAndValidateForm();
		if (form != null) {
			Organization organization = WebformsAuthorizationService.getInstance().getOrganization(
					UserSessionHandler.getUser(), form.getOrganizationId());
			if (OrbeonUtils.saveFormInOrbeon(form, organization, false)) {
				MessageManager.showInfo(LanguageCodes.XFORM_PUBLISHED);
			}
		}
	}

	private void previewXForms() {
		Form form = loadAndValidateForm();
		if (form != null) {
			Organization organization = WebformsAuthorizationService.getInstance().getOrganization(
					UserSessionHandler.getUser(), form.getOrganizationId());
			if (!OrbeonUtils.saveFormInOrbeon(new CompleteFormView(form), organization, true)) {
				// If xforms is not generated, close the popup.
				// ((OrbeonPreviewFrame)
				// upperMenu.getOpener().getUI()).closePopUp();
			}
		}
	}

	private void exportXsd() {
		CompleteFormView form = loadCompleteForm(getSelectedForm());

		ValidateFormComplete validator = new ValidateFormComplete();
		validator.setStopOnFail(true);

		ValidateReport report = new ValidateReport();
		validator.validate(form, report);
		if (report.isValid()) {
			new WindowDownloaderXsd(form, getSelectedForm().getLabel() + ".xsd");
		} else {
			MessageManager.showError(LanguageCodes.ERROR_FORM_NOT_VALID, LanguageCodes.VALIDATE_FORM);
		}
	}

	private void exportJson() {
		CompleteFormView form = loadCompleteForm(getSelectedForm());
		new WindowDownloaderJson(form, form.getLabel() + ".json");
	}

	private void exportBaseFormMetadataJson() {
		Form form = loadForm(getSelectedForm());
		new WindowDownloaderBaseFormMetadataJson(new CompleteFormView(form), getSelectedForm().getLabel()
				+ "_metadata_v" + getSelectedForm().getVersion() + ".json");
	}

	private void exportFlowPdf() {
		WindowDownloader window = new WindowDownloader(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					return new ByteArrayInputStream(GraphvizApp.generateImage(loadCompleteForm(getSelectedForm()),
							null, ImgType.PDF));
				} catch (IOException | InterruptedException e) {
					WebformsLogger.errorMessage(this.getClass().getName(), e);
					return null;
				}
			}
		});
		window.setIndeterminate(true);
		window.setFilename(getSelectedForm().getLabel() + ".pdf");
		window.showCentered();
	}

	private void exportPdf() {
		WindowDownloader downloader = new WindowDownloader(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					return FormGeneratorPdf.generatePdf(new FormPdfGenerator(loadCompleteForm(getSelectedForm())));
				} catch (IOException | DocumentException e) {
					WebformsLogger.errorMessage(FormManager.class.getName(), e);
					MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
				}
				return null;
			}
		});
		downloader.setIndeterminate(true);
		downloader.setFilename(getSelectedForm().getLabel() + ".pdf");
		downloader.showCentered();
	}

	private void impactAnalysis() {
		WindowImpactAnalysis impactAnalysis = new WindowImpactAnalysis();
		impactAnalysis.setForm(getSelectedForm());
		impactAnalysis.showCentered();
	}

	/**
	 * Opens window to link form with abcd form and version.
	 */
	private void linkAbcdForm() {
		final Form form = loadForm(getSelectedForm());

		List<com.biit.abcd.persistence.entity.SimpleFormView> availableForms;
		if (form.getLinkedFormLabel() == null) {
			// Not linked yet. Show all available forms.
			availableForms = UserSessionHandler.getController().getSimpleFormDaoAbcd().getAll();
		} else {
			// Already linked form, show only the versions of this form.
			availableForms = UserSessionHandler
					.getController()
					.getSimpleFormDaoAbcd()
					.getSimpleFormViewByLabelAndOrganization(form.getLinkedFormLabel(),
							form.getLinkedFormOrganizationId());
		}

		// Let user choose the version.
		WindowLinkAbcdForm linkAbcdForm = new WindowLinkAbcdForm();
		for (com.biit.abcd.persistence.entity.SimpleFormView simpleFormView : availableForms) {
			if (AbcdAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
					simpleFormView.getOrganizationId(), AbcdActivity.READ)
					&& WebformsAuthorizationService.getInstance().isAuthorizedActivity(UserSessionHandler.getUser(),
							simpleFormView.getOrganizationId(), WebformsActivity.FORM_EDITING)) {
				linkAbcdForm.add(simpleFormView);
			}
		}

		linkAbcdForm.setValue(UserSessionHandler.getController().getLinkedSimpleAbcdForms(form));
		linkAbcdForm.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowLinkAbcdForm linkWindow = (WindowLinkAbcdForm) window;

				for (IBaseFormView abcdForm : linkWindow.getValue()) {
					try {
						UserSessionHandler.getController().validateCompatibility(form, abcdForm);
					} catch (BadAbcdLink e) {
						MessageManager.showWarning(LanguageCodes.WARNING_ABCD_FORM_LINKED_NOT_VALID,
								LanguageCodes.WARNING_ABCD_FORM_LINKED_NOT_VALID_DESCRIPTION);
						break;
					}
				}
				form.setLinkedForms(linkWindow.getValue());
				try {
					UserSessionHandler.getController().saveForm(form);
					formTable.refreshTableData();
					formTable.selectForm(form);
					window.close();
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
							LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
							LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
		linkAbcdForm.showCentered();
	}

	private void importAbcdForm() {
		final WindowImportAbcdForms importAbcdForm = new WindowImportAbcdForms(UserSessionHandler.getController()
				.getTreeTableSimpleAbcdFormsProvider());
		importAbcdForm.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {

				// Check name and organization
				String newFormName = importAbcdForm.getImportName();

				if (newFormName == null || newFormName.isEmpty()) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_IMPORT_FAILED,
							LanguageCodes.ERROR_DESCRIPTION_NAME_NOT_VALID);
					return;
				}

				// Try to import the form.
				try {
					com.biit.abcd.persistence.entity.SimpleFormView abcdForm = importAbcdForm.getForm();
					Form importedForm = UserSessionHandler.getController().importAbcdForm(abcdForm, newFormName,
							abcdForm.getOrganizationId());
					formTable.refreshTableData();
					formTable.selectForm(importedForm);
					window.close();
				} catch (NotValidAbcdForm e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_IMPORT_FAILED,
							LanguageCodes.ERROR_DESCRIPTION_NOT_VALID_ABCD_FORM);
				} catch (FieldTooLongException | CharacterNotAllowedException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_IMPORT_FAILED,
							LanguageCodes.ERROR_DESCRIPTION_NAME_NOT_VALID);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_IMPORT_FAILED,
							LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
							LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
							LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsLogger.errorMessage(this.getClass().getName(), e);
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
				UserSessionHandler.getController().setFormInUse(loadForm(getSelectedForm()));
			}
		});
		return bottomMenu;
	}

	private void newFormVersion() {
		Form newForm;
		try {
			newForm = UserSessionHandler.getController().createNewFormVersion(loadForm(getSelectedForm()));
			formTable.refreshTableData();
			formTable.defaultSort();
			formTable.selectForm(newForm);
		} catch (NotValidStorableObjectException e) {
			MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
		} catch (NewVersionWithoutFinalDesignException e) {
			MessageManager.showError(LanguageCodes.ERROR_CAPTION_NOT_ALLOWED,
					LanguageCodes.WARNING_DESCRIPTION_NEW_VERSION_WHEN_DESIGN);
		} catch (CharacterNotAllowedException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		} catch (ElementCannotBePersistedException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
					LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private Form loadForm(IWebformsFormView formView) {
		Form form = UserSessionHandler.getController().loadForm(formView);
		form.setLastVersion(formView.isLastVersion());
		return form;
	}

	private CompleteFormView loadCompleteForm(IWebformsFormView formView) {
		return new CompleteFormView(loadForm(formView));
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
					MessageManager.showError(LanguageCodes.COMMON_WARNING_TITLE_FORM_NOT_CREATED,
							LanguageCodes.COMMON_WARNING_DESCRIPTION_FORM_NEEDS_NAME);
					return;
				}
				try {
					if (newFormWindow.getOrganization() != null) {
						Form newForm = UserSessionHandler.getController().createFormAndPersist(
								newFormWindow.getValue(), newFormWindow.getOrganization().getOrganizationId());
						newForm.setLastVersion(true);
						addFormToTable(newForm);
						newFormWindow.close();
					}
				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				} catch (CharacterNotAllowedException e) {
					// Impossible
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
							LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
							LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
	}

	private void addFormToTable(Form form) {
		SimpleFormView simpleForm = SimpleFormView.getSimpleFormView(form);
		formTable.refreshTableData();
		formTable.setValue(simpleForm);
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	public IWebformsFormView getSelectedForm() {
		IWebformsFormView form = (IWebformsFormView) formTable.getValue();
		return form;
	}

	private void updateMenus() {
		upperMenu.getNewForm().setEnabled(true);

		Object row = formTable.getValue();

		try {
			// Top menu
			boolean rowNotNull = row != null;
			IWebformsFormView selectedForm = (IWebformsFormView) formTable.getValue();

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
			upperMenu.getNewFormVersion().setEnabled(
					rowNotNull && canCreateNewVersion && selectedForm.isLastVersion()
							&& !selectedForm.getStatus().equals(FormWorkStatus.DESIGN));

			upperMenu.setEnabledImportAbcd(canCreateForms);
			upperMenu.setEnabledLinkAbcd(rowNotNullAndForm && canLinkVersion);

			upperMenu.setEnabledExport(rowNotNullAndForm);
			upperMenu.setEnabledXForms(rowNotNullAndForm);

			upperMenu.getImpactAnalysis().setEnabled(rowNotNullAndForm);

			upperMenu.getRemoveForm().setVisible(
					WebformsAuthorizationService.getInstance().isUserAuthorizedInAnyOrganization(
							UserSessionHandler.getUser(), WebformsActivity.FORM_REMOVE));
			upperMenu.getRemoveForm().setEnabled(
					rowNotNullAndForm
							&& WebformsAuthorizationService.getInstance().isAuthorizedActivity(
									UserSessionHandler.getUser(), selectedForm, WebformsActivity.FORM_REMOVE));

			// Bottom menu
			bottomMenu.getEditFormButton().setEnabled(rowNotNullAndForm);
			bottomMenu.getEditFlowButton().setEnabled(rowNotNullAndForm);
			bottomMenu.getValidateForm().setEnabled(rowNotNullAndForm);
			bottomMenu.getCompareStructureButton().setEnabled(rowNotNullAndForm);

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
