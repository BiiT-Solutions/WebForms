package com.biit.webforms.persistence.xforms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.xforms.exceptions.AccessNotAllowed;
import com.biit.webforms.persistence.xforms.exceptions.DuplicatedKeyException;
import com.biit.webforms.persistence.xforms.exceptions.DuplicatedXFormException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class XFormsPersistence {
	private static final String PREVIEW_PREFIX = "Preview_";
	private final static String APP_NAME = "WebForms";
	private Connection connection;
	private int port = 3306;

	public XFormsPersistence() {

	}

	/**
	 * In preview we add a prefix to differenciate the stored forms. For production we add the version of the form.
	 * 
	 * @param form
	 * @param preview
	 * @return
	 */
	public static String formatFormName(IWebformsFormView form, Organization organization, boolean preview) {
		if (preview) {
			return PREVIEW_PREFIX + form.getLabel().replace(" ", "_") + "_v" + form.getVersion() + "_" + organization.getName();
		} else {
			return form.getLabel().replace(" ", "_") + "_v" + form.getVersion() + "_" + organization.getName();
		}
	}

	/**
	 * *******************************************************************
	 * 
	 * CONNECTION
	 * 
	 ******************************************************************** 
	 */
	/**
	 * Connect to the database
	 * 
	 * @param password
	 *            database password.
	 * @param user
	 *            user database.
	 * @param database
	 *            schema.
	 * @param server
	 *            server IP
	 * @return true if the connection is ok.
	 * @throws AccessNotAllowed
	 */
	public boolean connect(String password, String user, String database, String server)
			throws CommunicationsException, SQLException, AccessNotAllowed {
		boolean error = false;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			connection = DriverManager
					.getConnection(
							"jdbc:mysql://"
									+ server
									+ ":"
									+ port
									+ "/"
									+ database
									+ "?allowMultiQueries=true&noAccessToProcedureBodies=true&validationQuery=SELECT%201&testOnBorrow=true",
							user, password);
		} catch (SQLException ex) {
			switch (ex.getErrorCode()) {
			case 1044:
				throw new AccessNotAllowed("User or password is wrong. Check your configuration file. ");
			default:
				ex.printStackTrace();
				WebformsLogger.errorMessage(this.getClass().getName(), ex);
				break;
			}

			error = true;

		}
		return !error;
	}

	public void deleteForm(Form form, Organization organization, boolean preview) {
		if (form != null) {
			try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM orbeon_form_definition WHERE app='"
					+ APP_NAME + "' and form='" + formatFormName(form, organization, preview) + "' and form_version="
					+ form.getVersion() + ";")) {
				stmt.executeUpdate();
			} catch (SQLException ex) {
				WebformsLogger.errorMessage(this.getClass().getName(), ex);
			}
		}
	}

	public void disconnectDatabase() {
		try {
			connection.close();
		} catch (NullPointerException | SQLException npe) {
			npe.printStackTrace();
		}
	}

	public void storeForm(Form form, User user, Organization organization, String xmlData, boolean preview)
			throws SQLException, DuplicatedXFormException {
		// String xmldata = new XFormsExporter(form).generateXFormsLanguage();
		// Delete previous form instance.
		deleteForm(form, organization, preview);
		// Add new one.
		try (PreparedStatement stmt = connection
				.prepareStatement("INSERT INTO orbeon_form_definition (`created`, `last_modified_time`, `last_modified_by`,`app`,`form`, `form_version`, `form_metadata`, `deleted`, `xml`) VALUES (?,?,?,?,?,?,?,?,?);")) {

			stmt.setTimestamp(1, form.getCreationTime());
			// Update time is the date when has been exported to Orbeon.
			stmt.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
			if (user != null) {
				stmt.setString(3, user.getEmailAddress());
			} else {
				stmt.setString(3, "");
			}
			stmt.setString(4, APP_NAME);
			stmt.setString(5, formatFormName(form, organization, preview));
			stmt.setInt(6, form.getVersion());
			stmt.setString(7, getFormMetadata(form, organization));
			stmt.setString(8, "N");
			stmt.setString(9, xmlData);

			stmt.executeUpdate();

		} catch (SQLException ex) {
			try {
				translateSqlErrorMessage(ex);
			} catch (DuplicatedKeyException e) {
				throw new DuplicatedXFormException("Duplicated xform error. Already exist a xform with id '" + "'.");
			}
			WebformsLogger.errorMessage(this.getClass().getName(), ex);
			ex.printStackTrace();
			throw ex;
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			WebformsLogger.errorMessage(this.getClass().getName(), npe);
			throw new SQLException("Database connection fail.");
		}
	}

	private String getFormMetadata(Form form, Organization organization) {
		return "<metadata xmlns:sql=\"http://orbeon.org/oxf/xml/sql\" xmlns:fr=\"http://orbeon.org/oxf/xml/form-runner\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xxf=\"http://orbeon.org/oxf/xml/xforms\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xh=\"http://www.w3.org/1999/xhtml\" xmlns:exf=\"http://www.exforms.org/exf/1-0\" xmlns:saxon=\"http://saxon.sf.net/\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fb=\"http://orbeon.org/oxf/xml/form-builder\" xmlns:xxi=\"http://orbeon.org/oxf/xml/xinclude\" xmlns:xi=\"http://www.w3.org/2001/XInclude\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xf=\"http://www.w3.org/2002/xforms\">"
				+ "<application-name>"
				+ APP_NAME
				+ "</application-name><form-name>"
				+ formatFormName(form, organization, false)
				+ "</form-name><title>"
				+ form.getLabel()
				+ "</title><description>" + form.getDescription() + "</description></metadata>";
	}

	public void translateSqlErrorMessage(SQLException exception) throws DuplicatedKeyException {
		WebformsLogger.errorMessage(this.getClass().getName(), exception);
		int numberError = exception.getErrorCode();
		switch (numberError) {
		case 1062:
			throw new DuplicatedKeyException("Duplicated primary key error");
		}
	}
}
