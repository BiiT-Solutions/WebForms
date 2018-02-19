package com.biit.webforms.gui.webpages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.form.entity.IBaseFormView;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.utils.validation.ValidateReport;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.exporters.xforms.XFormsMultiplesFormsExporter;
import com.biit.webforms.exporters.xforms.XFormsSimpleFormExporter;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
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
import com.biit.webforms.gui.exceptions.BadAbcdLink;
import com.biit.webforms.gui.exceptions.FormWithSameNameException;
import com.biit.webforms.gui.exceptions.NewVersionWithoutFinalDesignException;
import com.biit.webforms.gui.exceptions.NotValidAbcdForm;
import com.biit.webforms.gui.webpages.formmanager.IconProviderFormLinked;
import com.biit.webforms.gui.webpages.formmanager.TreeTableFormVersion;
import com.biit.webforms.gui.webpages.formmanager.UpperMenuProjectManager;
import com.biit.webforms.gui.webpages.formmanager.WindowDownloaderBaseFormMetadataJson;
import com.biit.webforms.gui.webpages.formmanager.WindowDownloaderJson;
import com.biit.webforms.gui.webpages.formmanager.WindowDownloaderXsd;
import com.biit.webforms.gui.webpages.formmanager.WindowGenerateXml;
import com.biit.webforms.gui.webpages.formmanager.WindowImpactAnalysis;
import com.biit.webforms.gui.webpages.formmanager.WindowImportAbcdForms;
import com.biit.webforms.gui.webpages.formmanager.WindowImportJson;
import com.biit.webforms.gui.webpages.formmanager.WindowLinkAbcdForm;
import com.biit.webforms.gui.xforms.OrbeonPreviewFrame;
import com.biit.webforms.gui.xforms.OrbeonUtils;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.pdfgenerator.FormGeneratorPdf;
import com.biit.webforms.pdfgenerator.FormPdfGenerator;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.persistence.entity.exceptions.FormIsUsedAsReferenceException;
import com.biit.webforms.persistence.xforms.XFormsPersistence;
import com.biit.webforms.security.WebformsActivity;
import com.biit.webforms.utils.GraphvizApp;
import com.biit.webforms.utils.GraphvizApp.ImgType;
import com.biit.webforms.utils.ZipTools;
import com.biit.webforms.validators.ValidateFormComplete;
import com.lowagie.text.DocumentException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("deprecation")
public class FormManager extends SecuredWebPage {
	private static final long serialVersionUID = 4853622392162188013L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(Arrays.asList(WebformsActivity.READ));

	private TreeTableFormVersion formTable;
	private UpperMenuProjectManager upperMenu;
	private FormEditBottomMenu bottomMenu;

	public FormManager() {
		super();
	}

	@Override
	protected void initContent() {
		ApplicationUi.getController().clearFormInUse();

		setCentralPanelAsWorkingArea();
		createUpperMenu();
		bottomMenu = createBottomMenu();

		setUpperMenu(upperMenu);
		setBottomMenu(bottomMenu);

		formTable = new TreeTableFormVersion(ApplicationUi.getController().getTreeTableFormsProvider(), new IconProviderFormLinked());
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
		upperMenu.addWebformReferenceListener(new ClickListener() {
			private static final long serialVersionUID = -7767871226211072684L;

			@Override
			public void buttonClick(ClickEvent event) {
				linkWebformsForm();
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
				IGroup<Long> organization = getWebformsSecurityService().getOrganization(UserSession.getUser(),
						getSelectedForm().getOrganizationId());
				upperMenu.getOpener().setParameter(OrbeonPreviewFrame.FORM_PARAMETER_TAG,
						XFormsPersistence.formatFormName(getSelectedForm(), organization, true));
				upperMenu.getOpener()
						.setParameter(OrbeonPreviewFrame.FORM_VERSION_PARAMETER_TAG, getSelectedForm().getVersion().toString());
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
		upperMenu.addPreviewXFormsListener(new ClickListener() {
			private static final long serialVersionUID = -8790434440652432643L;

			@Override
			public void buttonClick(ClickEvent event) {
				previewXForms();
			}
		});
		upperMenu.addPublishXFormsListener(new ClickListener() {
			private static final long serialVersionUID = -4167515599696676260L;

			@Override
			public void buttonClick(ClickEvent event) {
				publishXForms();
			}
		});
		upperMenu.addDownloadXFormsListener(new ClickListener() {
			private static final long serialVersionUID = -2359606371849802798L;

			@Override
			public void buttonClick(ClickEvent event) {
				downloadXForms();
			}
		});
		if (upperMenu != null) {
			upperMenu.addDownloadXFormsMultipleListener(new ClickListener() {
				private static final long serialVersionUID = 7112180518114190965L;

				@Override
				public void buttonClick(ClickEvent event) {
					downloadXFormsMultiple();
				}
			});
		}
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
		upperMenu.addRemoveFormListener(new ClickListener() {
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
		try {
			if (formTable.getValue() != null) {
				ApplicationUi.getController().removeForm(((IWebformsFormView) formTable.getValue()).getId());
				formTable.refreshTableData();
			}
		} catch (ElementCannotBeRemovedException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_TITLE);
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		} catch (FormIsUsedAsReferenceException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_TITLE,
					LanguageCodes.ERROR_ELEMENT_CANNOT_BE_REMOVED_LINKED_FORM_DESCRIPTION);
		}
	}

	private void importJsonForm() {
		WindowImportJson window = new WindowImportJson();
		window.showCentered();
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				window.close();
				formTable.refreshTableData();
			}
		});
	}

	private void exportXmlListener() {
		CompleteFormView form = loadAndValidateForm();

		if (form != null) {
			WindowGenerateXml window = new WindowGenerateXml(form);
			window.showCentered();
		}
	}

	/**
	 * Loads a forms and tries to validate. If the form is not validated returns
	 * null
	 * 
	 * @return
	 */
	private CompleteFormView loadAndValidateForm() {
		CompleteFormView form = (CompleteFormView) loadCompleteForm(getSelectedForm());

		// Xforms only can use valid forms.
		ValidateFormComplete validator = new ValidateFormComplete(ApplicationUi.getController().getAllWebservices());
		validator.setStopOnFail(true);

		ValidateReport report = new ValidateReport();
		validator.validate((form), report);

		if (report.isValid()) {
			if (report.hasWarnings()) {
				MessageManager.showWarning(LanguageCodes.WARNING_FORM_VALIDATION, LanguageCodes.WARNING_FORM_VALIDATION_BODY);
			}
			return form;
		} else {
			MessageManager.showError(LanguageCodes.ERROR_FORM_NOT_VALID, LanguageCodes.VALIDATE_FORM);
			return null;
		}
	}

	private void downloadXForms() {
		final CompleteFormView completeFormView = loadAndValidateForm();
		if (completeFormView != null) {
			// Orbeon fails if a form has no categories.
			if (!completeFormView.getChildren().isEmpty()) {
				WindowDownloader window = new WindowDownloader(new WindowDownloaderProcess() {

					@Override
					public InputStream getInputStream() {
						try {
							return new XFormsSimpleFormExporter(completeFormView, getWebformsSecurityService().getOrganization(
									UserSession.getUser(), getSelectedForm().getOrganizationId()), ApplicationUi.getController()
									.getAllWebservices(), false, false).generateXFormsLanguage();
						} catch (NotValidTreeObjectException | NotExistingDynamicFieldException | InvalidDateException
								| StringRuleSyntaxError | PostCodeRuleSyntaxError | NotValidChildException | UnsupportedEncodingException e) {
							MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
							WebformsUiLogger.errorMessage(this.getClass().getName(), e);
							return null;
						}
					}
				});
				window.setIndeterminate(true);
				window.setFilename(completeFormView.getLabel() + ".xml");
				window.showCentered();
			} else {
				MessageManager.showError(LanguageCodes.ERROR_INVALID_FORM_FOR_XFORMS,
						LanguageCodes.ERROR_INVALID_FORM_FOR_XFORMS_DESCRIPTION);
			}
		}
	}

	private void downloadXFormsMultiple() {
		final Form form = loadAndValidateForm();
		if (form != null) {
			// Orbeon fails if a form has no categories.
			if (!form.getChildren().isEmpty()) {
				WindowDownloader window = new WindowDownloader(new WindowDownloaderProcess() {

					@Override
					public InputStream getInputStream() {
						try {
							List<String> xmlFiles = new ArrayList<>();
							List<String> xmlFileNames = new ArrayList<>();
							XFormsMultiplesFormsExporter formExporter = new XFormsMultiplesFormsExporter(form, getWebformsSecurityService()
									.getOrganization(UserSession.getUser(), getSelectedForm().getOrganizationId()));
							xmlFiles.add(formExporter.getInitialInstancePage());
							xmlFileNames.add("initial-instance.xml");
							xmlFiles.add(formExporter.getCategoriesFlowPage());
							xmlFileNames.add("page-flow.xml");
							xmlFiles.addAll(formExporter.getAllcategoryModelPages());
							xmlFileNames.addAll(formExporter.getAllCategoriesFileNames());

							byte[] zipFile = ZipTools.zipFiles(xmlFiles, xmlFileNames, formExporter.getOrbeonAppFolder());

							return new ByteArrayInputStream(zipFile);
						} catch (IOException | NotValidTreeObjectException | NotValidChildException | NotExistingDynamicFieldException
								| InvalidDateException | StringRuleSyntaxError | PostCodeRuleSyntaxError e) {
							MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
							WebformsUiLogger.errorMessage(this.getClass().getName(), e);
							return null;
						}
					}
				});
				window.setIndeterminate(true);
				window.setFilename(form.getLabel() + ".zip");
				window.showCentered();
			} else {
				MessageManager.showError(LanguageCodes.ERROR_INVALID_FORM_FOR_XFORMS,
						LanguageCodes.ERROR_INVALID_FORM_FOR_XFORMS_DESCRIPTION);
			}
		}
	}

	private void publishXForms() {
		Form form = loadAndValidateForm();
		if (form != null) {
			// Orbeon fails if a form has no categories.
			if (!form.getChildren().isEmpty()) {
				IGroup<Long> organization = getWebformsSecurityService().getOrganization(UserSession.getUser(), form.getOrganizationId());
				if (OrbeonUtils.saveFormInOrbeon(form, organization, false, true)) {
					MessageManager.showInfo(LanguageCodes.XFORM_PUBLISHED);
				}
			} else {
				MessageManager.showError(LanguageCodes.ERROR_INVALID_FORM_FOR_XFORMS,
						LanguageCodes.ERROR_INVALID_FORM_FOR_XFORMS_DESCRIPTION);
			}
		}
	}

	private void previewXForms() {
		Form form = loadAndValidateForm();
		if (form != null) {
			// Orbeon fails if a form has no categories.
			if (!form.getChildren().isEmpty()) {
				IGroup<Long> organization = getWebformsSecurityService().getOrganization(UserSession.getUser(), form.getOrganizationId());
				if (!OrbeonUtils.saveFormInOrbeon(new CompleteFormView(form), organization, true, true)) {
					// If xforms is not generated, close the popup.
					// ((OrbeonPreviewFrame)
					// upperMenu.getOpener().getUI()).closePopUp();
				}
			} else {
				MessageManager.showError(LanguageCodes.ERROR_INVALID_FORM_FOR_XFORMS,
						LanguageCodes.ERROR_INVALID_FORM_FOR_XFORMS_DESCRIPTION);
			}
		}
	}

	private void exportXsd() {
		CompleteFormView completeFormView = loadCompleteForm(getSelectedForm());

		ValidateFormComplete validator = new ValidateFormComplete(ApplicationUi.getController().getAllWebservices());
		validator.setStopOnFail(true);

		ValidateReport report = new ValidateReport();
		validator.validate(completeFormView, report);
		if (report.isValid()) {
			new WindowDownloaderXsd(completeFormView, getSelectedForm().getLabel() + ".xsd");
		} else {
			MessageManager.showError(LanguageCodes.ERROR_FORM_NOT_VALID, LanguageCodes.VALIDATE_FORM);
		}
	}

	private void exportJson() {
		CompleteFormView completeFormView = loadCompleteForm(getSelectedForm());
		new WindowDownloaderJson(completeFormView, completeFormView.getLabel() + ".json");
	}

	private void exportBaseFormMetadataJson() {
		Form form = loadForm(getSelectedForm());
		new WindowDownloaderBaseFormMetadataJson(new CompleteFormView(form), getSelectedForm().getLabel() + "_metadata_v"
				+ getSelectedForm().getVersion() + ".json");
	}

	private void exportFlowPdf() {
		WindowDownloader window = new WindowDownloader(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					return new ByteArrayInputStream(GraphvizApp.generateImage(loadCompleteForm(getSelectedForm()), null, ImgType.PDF));
				} catch (IOException | InterruptedException e) {
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
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
					WebformsUiLogger.errorMessage(FormManager.class.getName(), e);
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

		if (form.getLinkedFormLabel() == null || form.getLinkedFormLabel().isEmpty()) {
			// Not linked yet. Show all available forms.
			availableForms = ApplicationUi.getController().getAllSimpleFormViewsFromAbcdForCurrentUser();
		} else {
			// Already linked form, show only the versions of this form.
			availableForms = ApplicationUi.getController().getAllSimpleFormViewsFromAbcdByLabelAndOrganization(form.getLinkedFormLabel(),
					form.getLinkedFormOrganizationId());
		}

		// Let user choose the version.
		WindowLinkAbcdForm linkAbcdForm = new WindowLinkAbcdForm();
		for (com.biit.abcd.persistence.entity.SimpleFormView simpleFormView : availableForms) {
			if (getWebformsSecurityService().isAuthorizedActivity(UserSession.getUser(), simpleFormView.getOrganizationId(),
					WebformsActivity.FORM_EDITING)) {
				linkAbcdForm.add(simpleFormView);
			}
		}

		linkAbcdForm.setValue(ApplicationUi.getController().getLinkedSimpleFormViewsFromAbcd(form));
		linkAbcdForm.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				WindowLinkAbcdForm linkWindow = (WindowLinkAbcdForm) window;

				for (IBaseFormView abcdForm : linkWindow.getValue()) {
					try {
						ApplicationUi.getController().validateCompatibility(form, abcdForm);
					} catch (BadAbcdLink e) {
						MessageManager.showWarning(LanguageCodes.WARNING_ABCD_FORM_LINKED_NOT_VALID,
								LanguageCodes.WARNING_ABCD_FORM_LINKED_NOT_VALID_DESCRIPTION);
						break;
					}
				}
				form.setLinkedForms(linkWindow.getValue());
				try {
					form.setUpdatedBy(UserSession.getUser());
					form.setUpdateTime();
					ApplicationUi.getController().saveForm(form);
					formTable.refreshTableData();
					formTable.selectForm(form);
					window.close();
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
							LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				}
			}
		});
		linkAbcdForm.showCentered();
	}

	private void importAbcdForm() {
		final WindowImportAbcdForms importAbcdForm = new WindowImportAbcdForms(ApplicationUi.getController()
				.getTreeTableSimpleAbcdFormsProvider());
		importAbcdForm.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {

				// Check name and organization
				String newFormName = importAbcdForm.getImportName();

				if (newFormName == null || newFormName.isEmpty()) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_IMPORT_FAILED, LanguageCodes.ERROR_DESCRIPTION_NAME_NOT_VALID);
					return;
				}

				// Try to import the form.
				try {
					com.biit.abcd.persistence.entity.SimpleFormView abcdForm = importAbcdForm.getForm();
					Form importedForm = ApplicationUi.getController().importAbcdForm(abcdForm, newFormName, abcdForm.getOrganizationId());
					formTable.refreshTableData();
					formTable.selectForm(importedForm);
					window.close();
				} catch (NotValidAbcdForm e) {
					MessageManager
							.showError(LanguageCodes.ERROR_CAPTION_IMPORT_FAILED, LanguageCodes.ERROR_DESCRIPTION_NOT_VALID_ABCD_FORM);
				} catch (FieldTooLongException | CharacterNotAllowedException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_IMPORT_FAILED, LanguageCodes.ERROR_DESCRIPTION_NAME_NOT_VALID);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.ERROR_CAPTION_IMPORT_FAILED, LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				} catch (ElementIsReadOnly e) {
					MessageManager.showError(LanguageCodes.ERROR_READ_ONLY_ELEMENT);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
							LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
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
				ApplicationUi.getController().setFormInUse(loadForm(getSelectedForm()));
			}
		});
		return bottomMenu;
	}

	private void linkWebformsForm() {
		final WindowNameGroup newFormWindow = new WindowNameGroup(LanguageCodes.COMMON_CAPTION_NAME.translation(),
				LanguageCodes.COMMON_CAPTION_GROUP.translation(), new IActivity[] { WebformsActivity.FORM_EDITING });
		newFormWindow.setCaption(LanguageCodes.CAPTION_NEW_FORM.translation());
		newFormWindow.setDefaultValue(LanguageCodes.NULL_VALUE_NEW_FORM.translation());
		newFormWindow.showCentered();
		newFormWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (!newFormWindow.isValid()) {
					return;
				}
				if (newFormWindow.getValue() == null || newFormWindow.getValue().isEmpty()) {
					MessageManager.showError(LanguageCodes.COMMON_WARNING_TITLE_FORM_NOT_CREATED,
							LanguageCodes.COMMON_WARNING_DESCRIPTION_FORM_NEEDS_NAME);
					return;
				}
				try {
					if (newFormWindow.getOrganization() != null) {
						Form newForm;
						newForm = ApplicationUi.getController().createNewLinkedForm(loadForm(getSelectedForm()), newFormWindow.getValue(),
								newFormWindow.getOrganization().getId());
						addFormToTable(newForm);
						formTable.selectForm(newForm);
						newFormWindow.close();
					}
				} catch (FieldTooLongException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
				} catch (FormWithSameNameException e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE);
				} catch (CharacterNotAllowedException e) {
					// Impossible
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				} catch (UnexpectedDatabaseException | NotValidStorableObjectException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				}
			}
		});
	}

	private void newFormVersion() {
		Form newForm;
		try {
			newForm = ApplicationUi.getController().createNewFormVersion(loadForm(getSelectedForm()));
			formTable.refreshTableData();
			formTable.defaultSort();
			formTable.selectForm(newForm);
		} catch (NotValidStorableObjectException e) {
			MessageManager.showError(LanguageCodes.COMMON_ERROR_FIELD_TOO_LONG);
		} catch (NewVersionWithoutFinalDesignException e) {
			MessageManager.showError(LanguageCodes.ERROR_CAPTION_NOT_ALLOWED, LanguageCodes.WARNING_DESCRIPTION_NEW_VERSION_WHEN_DESIGN);
		} catch (CharacterNotAllowedException e) {
			// Impossible
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		} catch (UnexpectedDatabaseException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		} catch (ElementCannotBePersistedException e) {
			MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED, LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private Form loadForm(IWebformsFormView formView) {
		Form form = ApplicationUi.getController().loadForm(formView);
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
				if (!newFormWindow.isValid()) {
					return;
				}
				if (newFormWindow.getValue() == null || newFormWindow.getValue().isEmpty()) {
					MessageManager.showError(LanguageCodes.COMMON_WARNING_TITLE_FORM_NOT_CREATED,
							LanguageCodes.COMMON_WARNING_DESCRIPTION_FORM_NEEDS_NAME);
					return;
				}
				try {
					if (newFormWindow.getOrganization() != null) {
						Form newForm = ApplicationUi.getController().createFormAndPersist(newFormWindow.getValue(),
								newFormWindow.getOrganization().getId());
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
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				} catch (UnexpectedDatabaseException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
				} catch (ElementCannotBePersistedException e) {
					MessageManager.showError(LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED,
							LanguageCodes.ERROR_ELEMENT_CANNOT_BE_SAVED_DESCRIPTION);
					WebformsUiLogger.errorMessage(this.getClass().getName(), e);
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
			boolean canCreateForms = getWebformsSecurityService().isUserAuthorizedInAnyOrganization(UserSession.getUser(),
					WebformsActivity.FORM_EDITING);
			boolean canCreateNewVersion = getWebformsSecurityService().isAuthorizedActivity(UserSession.getUser(), selectedForm,
					WebformsActivity.FORM_NEW_VERSION);
			boolean canLinkVersion = getWebformsSecurityService().isAuthorizedActivity(UserSession.getUser(), selectedForm,
					WebformsActivity.FORM_EDITING);

			upperMenu.setEnabled(true);
			upperMenu.getNewForm().setEnabled(canCreateForms);
			upperMenu.getNewFormVersion().setEnabled(
					rowNotNull && canCreateNewVersion && selectedForm.isLastVersion()
							&& !selectedForm.getStatus().equals(FormWorkStatus.DESIGN));

			upperMenu.getWebformReference().setEnabled(rowNotNullAndForm && (((SimpleFormView) row).getFormReferenceId() == null));

			upperMenu.setEnabledImportAbcd(canCreateForms);
			upperMenu.setEnabledLinkAbcd(rowNotNullAndForm && canLinkVersion);

			upperMenu.setEnabledExport(rowNotNullAndForm);
			upperMenu.setEnabledXForms(rowNotNullAndForm);

			upperMenu.getImpactAnalysis().setEnabled(rowNotNullAndForm);

			upperMenu.getRemoveForm().setVisible(
					getWebformsSecurityService().isUserAuthorizedInAnyOrganization(UserSession.getUser(), WebformsActivity.FORM_REMOVE));
			upperMenu.getRemoveForm().setEnabled(
					rowNotNullAndForm
							&& getWebformsSecurityService().isAuthorizedActivity(UserSession.getUser(), selectedForm,
									WebformsActivity.FORM_REMOVE));

			// Bottom menu
			bottomMenu.getEditFormButton().setEnabled(rowNotNullAndForm);
			bottomMenu.getEditFlowButton().setEnabled(rowNotNullAndForm);
			bottomMenu.getValidateForm().setEnabled(rowNotNullAndForm);
			bottomMenu.getEditWebserviceCall().setEnabled(rowNotNullAndForm);
			bottomMenu.getCompareStructureButton().setEnabled(rowNotNullAndForm);

		} catch (IOException | AuthenticationRequired e) {
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			// failsafe, disable everything.
			upperMenu.setEnabled(false);
			// Bottom menu
			bottomMenu.getEditFormButton().setEnabled(false);
			bottomMenu.getEditFlowButton().setEnabled(false);
			bottomMenu.getValidateForm().setEnabled(false);
			bottomMenu.getEditWebserviceCall().setEnabled(false);
		}
	}
}
