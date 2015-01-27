package com.biit.webforms.gui.xforms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.gui.UserSessionHandler;
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

public class OrbeonUtils {

	/**
	 * Connects to the database and stores the form.
	 * 
	 * @param form
	 */
	public static boolean saveFormInOrbeon(Form form, Organization organization, boolean preview) {
		// Save it.
		XFormsPersistence xformsConnection = new XFormsPersistence();
		try {
			xformsConnection.connect(WebformsConfigurationReader.getInstance().getXFormsPassword(),
					WebformsConfigurationReader.getInstance().getXFormsUser(), WebformsConfigurationReader
							.getInstance().getXFormsDatabaseName(), WebformsConfigurationReader.getInstance()
							.getXFormsDatabaseHost());
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
				WebformsLogger.errorMessage(OrbeonUtils.class.getName(), e);
				return false;
			} catch (Exception e) {
				MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
				WebformsLogger.errorMessage(OrbeonUtils.class.getName(), e);
				return false;
			}
			xformsConnection.disconnectDatabase();
		} catch (SQLException e) {
			MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			WebformsLogger.errorMessage(OrbeonUtils.class.getName(), e);
			return false;
		} catch (AccessNotAllowed e1) {
			MessageManager.showError(LanguageCodes.ERROR_XFORMS_USER_INVALID);
			WebformsLogger.errorMessage(OrbeonUtils.class.getName(), e1);
			return false;
		}
		return true;
	}

	public static String getXFormsData(Form form) throws NotValidTreeObjectException, NotValidChildException, IOException,
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
