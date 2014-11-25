package com.biit.webforms.gui.xforms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.xforms.XFormsPersistence;
import com.biit.webforms.persistence.xforms.exceptions.AccessNotAllowed;
import com.biit.webforms.persistence.xforms.exceptions.DuplicatedXFormException;
import com.biit.webforms.xforms.XFormsExporter;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;
import com.liferay.portal.model.Organization;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WindowXForms extends Window {
	private static final long serialVersionUID = 7318166627971172326L;
	private static final String width = "300px";

	public WindowXForms() {
		setClosable(true);
		setResizable(false);
		setDraggable(false);
		setModal(true);
		center();
		setWidth(width);
		setHeight(null);
		setContent(generateContent());
		setCaption(LanguageCodes.CAPTION_TO_XFORMS.translation());
	}

	private Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);

		final Organization organization = WebformsAuthorizationService.getInstance()
				.getOrganization(UserSessionHandler.getUser(),
						UserSessionHandler.getController().getPreviewedForm().getOrganizationId());

		Button previewButton = new Button(LanguageCodes.CAPTION_PREVIEW_FILE.translation(), new ClickListener() {
			private static final long serialVersionUID = -4996751752953783384L;

			@Override
			public void buttonClick(ClickEvent event) {
				saveFormInOrbeon(UserSessionHandler.getController().getPreviewedForm(), organization, true);
			}
		});

		BrowserWindowOpener opener = new BrowserWindowOpener(OrbeonPreviewFrame.class);
		opener.setParameter(OrbeonPreviewFrame.FORM_PARAMETER_TAG, XFormsPersistence.formatFormName(UserSessionHandler
				.getController().getPreviewedForm(), organization, true));
		opener.setParameter(OrbeonPreviewFrame.APPLICATION_PARAMETER_TAG, XFormsExporter.APP_NAME);
		opener.setFeatures("target=_new");
		opener.extend(previewButton);

		previewButton.setDescription(LanguageCodes.CAPTION_PREVIEW_FILE_DESCRIPTION.translation());
		previewButton.setWidth("100%");
		rootLayout.addComponent(previewButton);

		Button publishButton = new Button(LanguageCodes.CAPTION_PUBLISH_FILE.translation(), new ClickListener() {
			private static final long serialVersionUID = -4996751752953783384L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (saveFormInOrbeon(UserSessionHandler.getController().getPreviewedForm(), organization, false)) {
					MessageManager.showInfo(LanguageCodes.XFORM_PUBLISHED);
				}
			}
		});
		publishButton.setDescription(LanguageCodes.CAPTION_PUBLISH_FILE_DESCRIPTION.translation());
		publishButton.setWidth("100%");
		rootLayout.addComponent(publishButton);

		Button downloadButton = new Button(LanguageCodes.CAPTION_DOWNLOAD_FILE.translation(), new ClickListener() {
			private static final long serialVersionUID = -4996751752953783384L;

			@Override
			public void buttonClick(ClickEvent event) {
				final Form form = UserSessionHandler.getController().getPreviewedForm();
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
				window.setFilename(UserSessionHandler.getController().getPreviewedForm().getLabel() + ".txt");
				window.showCentered();
			}
		});
		downloadButton.setDescription(LanguageCodes.CAPTION_DOWNLOAD_FILE_DESCRIPTION.translation());
		downloadButton.setWidth("100%");
		rootLayout.addComponent(downloadButton);

		Button closeButton = new Button(LanguageCodes.CAPTION_SETTINGS_CLOSE.translation(), new ClickListener() {
			private static final long serialVersionUID = 6644941451552762983L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		closeButton.setWidth("100%");
		// rootLayout.addComponent(closeButton);

		return rootLayout;
	}

	public void show() {
		center();
		UI.getCurrent().addWindow(this);
	}

	/**
	 * Connects to the database and stores the form.
	 * 
	 * @param form
	 */
	private boolean saveFormInOrbeon(Form form, Organization organization, boolean preview) {
		// Save it.
		XFormsPersistence xformsConnection = new XFormsPersistence();
		try {
			xformsConnection.connect(WebformsConfigurationReader.getInstance().getXFormsPassword(),
					WebformsConfigurationReader.getInstance().getXFormsUser(), WebformsConfigurationReader
							.getInstance().getXFormsDatabaseName(), WebformsConfigurationReader.getInstance()
							.getXFormsServer());
			try {
				String xmlData = getXFormsData(form);
				xformsConnection.storeForm(form, UserSessionHandler.getUser(), organization, xmlData, preview);
			} catch (DuplicatedXFormException e) {
				MessageManager.showError(LanguageCodes.ERROR_FORM_NOT_PUBLISHED,
						LanguageCodes.ERROR_FORM_ALREADY_EXISTS);
				return false;
			} catch (NotExistingDynamicFieldException e) {
				MessageManager.showError(e.getMessage());
				return false;
			} catch (InvalidDateException | StringRuleSyntaxError | PostCodeRuleSyntaxError e) {
				MessageManager.showError(LanguageCodes.ERROR_FORM_NOT_PUBLISHED);
				WebformsLogger.errorMessage(this.getClass().getName(), e);
				return false;
			} catch (Exception e) {
				MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
				WebformsLogger.errorMessage(this.getClass().getName(), e);
				return false;
			}
			xformsConnection.disconnectDatabase();
		} catch (SQLException e) {
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return false;
		} catch (AccessNotAllowed e1) {
			MessageManager.showError(LanguageCodes.ERROR_XFORMS_USER_INVALID);
			WebformsLogger.errorMessage(this.getClass().getName(), e1);
			return false;
		}
		return true;
	}

	public String getXFormsData(Form form) throws NotValidTreeObjectException, NotValidChildException, IOException,
			NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError, PostCodeRuleSyntaxError {
		XFormsExporter xformExporter = new XFormsExporter(form);
		BufferedInputStream in = new BufferedInputStream(xformExporter.generateXFormsLanguage());
		byte[] contents = new byte[1024];

		int bytesRead = 0;
		String strFileContents = "";
		while ((bytesRead = in.read(contents)) != -1) {
			strFileContents += new String(contents, 0, bytesRead);
		}
		return strFileContents;
	}

}
