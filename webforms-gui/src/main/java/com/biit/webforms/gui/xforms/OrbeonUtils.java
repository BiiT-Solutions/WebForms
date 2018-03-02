package com.biit.webforms.gui.xforms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.usermanager.entity.IGroup;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.exporters.xforms.XFormsSimpleFormExporter;
import com.biit.webforms.exporters.xforms.exceptions.InvalidDateException;
import com.biit.webforms.exporters.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.exporters.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.exporters.xforms.exceptions.StringRuleSyntaxError;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.xforms.XFormsPersistence;
import com.biit.webforms.persistence.xforms.exceptions.AccessNotAllowed;
import com.biit.webforms.persistence.xforms.exceptions.DuplicatedXFormException;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class OrbeonUtils {

	/**
	 * Connects to the database and stores the form.
	 * 
	 * @param form
	 */
	public static boolean saveFormInOrbeon(Form form, IGroup<Long> organization, boolean preview, boolean includeImages) {
		// Save it.
		XFormsPersistence xformsConnection = new XFormsPersistence();
		try {
			xformsConnection.connect(WebformsConfigurationReader.getInstance().getXFormsPassword(), WebformsConfigurationReader
					.getInstance().getXFormsUser(), WebformsConfigurationReader.getInstance().getXFormsDatabaseName(),
					WebformsConfigurationReader.getInstance().getXFormsDatabaseHost());
			try {
				String xmlData = getXFormsData(form, organization, includeImages, preview);
				xformsConnection.storeForm(form, UserSession.getUser(), organization, xmlData, preview);
				if (includeImages) {
					xformsConnection.storeImages(form, UserSession.getUser(), organization, form.getAllImages(), preview);
				}
			} catch (DuplicatedXFormException e) {
				MessageManager.showError(LanguageCodes.ERROR_FORM_NOT_PUBLISHED, LanguageCodes.ERROR_FORM_ALREADY_EXISTS);
				return false;
			} catch (NotExistingDynamicFieldException e) {
				MessageManager.showError(e.getMessage());
				return false;
			} catch (InvalidDateException | StringRuleSyntaxError | PostCodeRuleSyntaxError e) {
				MessageManager.showError(LanguageCodes.ERROR_FORM_NOT_PUBLISHED);
				WebformsUiLogger.errorMessage(OrbeonUtils.class.getName(), e);
				return false;
			} catch (Exception e) {
				MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
				WebformsUiLogger.errorMessage(OrbeonUtils.class.getName(), e);
				return false;
			}
			xformsConnection.disconnectDatabase();
		} catch (CommunicationsException ce) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE, LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			WebformsUiLogger.errorMessage(OrbeonUtils.class.getName(), ce);
		} catch (SQLException e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE);
			WebformsUiLogger.errorMessage(OrbeonUtils.class.getName(), e);
			return false;
		} catch (AccessNotAllowed e1) {
			MessageManager.showError(LanguageCodes.ERROR_XFORMS_USER_INVALID);
			WebformsUiLogger.errorMessage(OrbeonUtils.class.getName(), e1);
			return false;
		}
		return true;
	}

	private static String getXFormsData(Form form, IGroup<Long> organization, boolean includeImages, boolean preview) throws NotValidTreeObjectException,
			NotValidChildException, IOException, NotExistingDynamicFieldException, InvalidDateException, StringRuleSyntaxError,
			PostCodeRuleSyntaxError {
		XFormsSimpleFormExporter xformExporter = new XFormsSimpleFormExporter(form, organization, ApplicationUi.getController()
				.getAllWebservices(), includeImages, preview);
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
