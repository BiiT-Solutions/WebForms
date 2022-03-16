package com.biit.webforms.persistence.xforms;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.utils.annotations.FindBugsSuppressWarnings;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.xforms.exceptions.AccessNotAllowed;
import com.biit.webforms.persistence.xforms.exceptions.DuplicatedKeyException;
import com.biit.webforms.persistence.xforms.exceptions.DuplicatedXFormException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;


public class XFormsPersistence {
    private static final String PREVIEW_PREFIX = "Preview_";
    public final static String APP_NAME = "WebForms";
    private Connection connection;
    private int port = 3306;

    public XFormsPersistence() {

    }

    /**
     * In preview we add a prefix to differentiate the stored forms. For
     * production we add the version of the form.
     *
     * @param form
     * @param preview
     * @return
     */
    public static String formatFormName(IWebformsFormView form, IGroup<Long> organization, boolean preview) {
        if (preview) {
            return PREVIEW_PREFIX + form.getLabel().replace(" ", "_") + "_v" + form.getVersion() + "_" + organization.getUniqueName().replace(" ", "_");
        } else {
            return form.getLabel().replace(" ", "_") + "_v" + form.getVersion() + "_" + organization.getUniqueName().replace(" ", "_");
        }
    }

    /**
     * file_name column of orbeon database
     *
     * @param form
     * @param organization
     * @param image
     * @param preview
     * @return
     */
    public static String imageFileName(IWebformsFormView form, IGroup<Long> organization, TreeObjectImage image, boolean preview) {
        return image.getComparationId();
    }

    /**
     * document_id column of orbeon database
     *
     * @param form
     * @param organization
     * @param image
     * @param preview
     * @return
     */
    public static String imageDocumentId(IWebformsFormView form, IGroup<Long> organization, TreeObjectImage image, boolean preview) {
        if (preview) {
            //Only local images are stored in orbeon.
            if (image.getFileName() != null) {
                return PREVIEW_PREFIX + "_" + image.getFileName().replace(" ", "_");
            }
        } else {
            if (image.getFileName() != null) {
                return image.getFileName().replace(" ", "_");
            }
        }
        // URL images are not stored in orbeon.
        return null;
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
     * @param password database password.
     * @param user     user database.
     * @param database schema.
     * @param server   server IP
     * @return true if the connection is ok.
     * @throws AccessNotAllowed
     */
    public void connect(String password, String user, String database, String server) throws SQLException, AccessNotAllowed {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database
                    + "?allowMultiQueries=true&noAccessToProcedureBodies=true&validationQuery=SELECT%201&testOnBorrow=true", user, password);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1044) {
                throw new AccessNotAllowed("IUser<Long> or password is wrong. Check your configuration file. ");
            }
            WebformsLogger.errorMessage(this.getClass().getName(), ex);
            throw ex;
        }
    }

    @FindBugsSuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
    public void deleteForm(Form form, IGroup<Long> organization, boolean preview) {
        if (form != null && connection != null) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM orbeon_form_definition WHERE app='" + APP_NAME + "' and form='"
                    + formatFormName(form, organization, preview) + "' and form_version=" + form.getVersion() + ";")) {
                stmt.executeUpdate();
            } catch (SQLException ex) {
                WebformsLogger.errorMessage(this.getClass().getName(), ex);
            }
        }
    }

    public void disconnectDatabase() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (NullPointerException | SQLException npe) {
            WebformsLogger.errorMessage(this.getClass().getName(), npe);
        }
    }

    public void storeForm(Form form, IUser<Long> user, IGroup<Long> organization, String xmlData, boolean preview) throws SQLException,
            DuplicatedXFormException {
        if (connection != null) {
            // String xmldata = new
            // XFormsExporter(form).generateXFormsLanguage();
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
    }

    /**
     * Store form images in orbeon database.
     *
     * @param form
     * @param user
     * @param organization
     * @param images
     * @param preview
     * @throws DuplicatedXFormException
     * @throws SQLException
     */
    public void storeImages(Form form, IUser<Long> user, IGroup<Long> organization, Set<TreeObjectImage> images, boolean preview)
            throws DuplicatedXFormException, SQLException {
        for (TreeObjectImage image : images) {
            storeImage(form, user, organization, image, preview);
        }
    }

    /**
     * Store one image as an attached file in orbeon database.
     *
     * @param form
     * @param user
     * @param organization
     * @param image
     * @param preview
     * @throws SQLException
     * @throws DuplicatedXFormException
     */
    public void storeImage(Form form, IUser<Long> user, IGroup<Long> organization, TreeObjectImage image, boolean preview) throws SQLException,
            DuplicatedXFormException {
        if (connection != null && image.getData() != null) {
            // String xmldata = new
            // XFormsExporter(form).generateXFormsLanguage();
            // Delete previous form instance.
            deleteImage(form, organization, image, preview);
            // Add new one.
            try (PreparedStatement stmt = connection
                    .prepareStatement("INSERT INTO orbeon_form_data_attach (`created`, `last_modified_time`, `last_modified_by`,`app`,`form`, `form_version`, `document_id`, `draft`, `deleted`, `file_name`, `file_content`) VALUES (?,?,?,?,?,?,?,?,?,?,?);")) {

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
                stmt.setString(7, imageDocumentId(form, organization, image, preview));
                stmt.setString(8, "N");
                stmt.setString(9, "N");
                stmt.setString(10, imageFileName(form, organization, image, preview));
                stmt.setBytes(11, image.getData());

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
    }

    public void deleteImage(Form form, IGroup<Long> organization, TreeObjectImage image, boolean preview) {
        if (form != null && connection != null) {
            final String sql = String.format("DELETE FROM orbeon_form_data_attach WHERE app='%s' and form='%s' and form_version=%d and document_id='%s' and file_name='%s';",
                    APP_NAME, formatFormName(form, organization, preview), form.getVersion(), imageDocumentId(form, organization, image, preview), imageFileName(form, organization, image, preview));
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (SQLException ex) {
                WebformsLogger.errorMessage(this.getClass().getName(), ex);
            }
        }
    }

    private String getFormMetadata(Form form, IGroup<Long> organization) {
        return "<metadata xmlns:sql=\"http://orbeon.org/oxf/xml/sql\" xmlns:fr=\"http://orbeon.org/oxf/xml/form-runner\" xmlns:ev=\"http://www.w3.org/2001/xml-events\" xmlns:xxf=\"http://orbeon.org/oxf/xml/xforms\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xh=\"http://www.w3.org/1999/xhtml\" xmlns:exf=\"http://www.exforms.org/exf/1-0\" xmlns:saxon=\"http://saxon.sf.net/\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fb=\"http://orbeon.org/oxf/xml/form-builder\" xmlns:xxi=\"http://orbeon.org/oxf/xml/xinclude\" xmlns:xi=\"http://www.w3.org/2001/XInclude\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xf=\"http://www.w3.org/2002/xforms\">"
                + "<application-name>"
                + APP_NAME
                + "</application-name><form-name>"
                + formatFormName(form, organization, false)
                + "</form-name><title>"
                + form.getLabel() + "</title><description>" + form.getDescription() + "</description></metadata>";
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
