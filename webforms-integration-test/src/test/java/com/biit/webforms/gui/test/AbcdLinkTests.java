package com.biit.webforms.gui.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.biit.utils.file.PropertiesFile;

public class AbcdLinkTests extends WebFormsTester {

	public static final String ABCD_SQL_DUMP_FILE_PATH = "./src/test/resources/AbcdFormDB.sql";
	public static final String ABCD_SCRIPTRUNNER_OUTPUT_FILE_PATH = "./src/test/resources/AbcdFormDB.out";
	private static final int TABLE_ROW = 0;
	private static final int TREE_TABLE_ROW = 2;
	public static final String ABCD_LINK_MESSAGE = "Form 'FormTest' version '1' has element 'Category2' and it's not found in form 'FormTest' version '1'.";
	public static final String ABCD_DIFF_MESSAGE = "Form 'FormTest' version '1' has element 'Category1/Group' and it's not found in form 'FormTest' version '1'.";
	private static final String DATABASE_CONFIG_FILE = "./target/classes/database.properties";
	private static final String ABCD_DB_URL_PROPERTY = "abcd.hibernate.connection.url";
	private static final String ABCD_DB_USER_PROPERTY = "abcd.hibernate.connection.username";
	private static final String ABCD_DB_PASS_PROPERTY = "abcd.hibernate.connection.password";

	@AfterClass
	private void createAbcdForm() throws SQLException, ClassNotFoundException {
		try {
			// Read the properties file
			Properties prop = new Properties();
			prop = PropertiesFile.load(DATABASE_CONFIG_FILE);
			// Create MySql Connection
			Connection con = DriverManager.getConnection(prop.getProperty(ABCD_DB_URL_PROPERTY),
					prop.getProperty(ABCD_DB_USER_PROPERTY), prop.getProperty(ABCD_DB_PASS_PROPERTY));
			// Initialize object for ScripRunner
			ScriptRunner sr = new ScriptRunner(con);
			// Give the input file to Reader
			Reader reader = new BufferedReader(new FileReader(ABCD_SQL_DUMP_FILE_PATH));
			// Execute script
			sr.setLogWriter(new PrintWriter(new File(ABCD_SCRIPTRUNNER_OUTPUT_FILE_PATH)));
			sr.runScript(reader);
			sr.closeConnection();

		} catch (IOException e) {
			System.err.println("Failed to Execute" + ABCD_SQL_DUMP_FILE_PATH + " The error is " + e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = "abcdLink")
	public void abcdLinkForm() {
		loginFormAdmin1();
		getFormManagerPage().deleteAllCreatedForms();
		logOut();

		loginFormEdit1();
		// Import Abcd form
		getFormManagerPage().clickNewButton();
		getFormManagerPage().clickFromAbcdButton();
		getFormManagerPage().getImportAbcdFormWindow().waitToShow();
		getFormManagerPage().getImportAbcdFormWindow().clickAccept();
		// Unlink Abcd form
		getFormManagerPage().clickLinkAbcdRulesButton();
		getFormManagerPage().getWindowLinkAbcdFormWindow().clickTableRow(TABLE_ROW);
		getFormManagerPage().getWindowLinkAbcdFormWindow().clickAccept();
		// Make form incompatible
		goToDesignerPage();
		getDesignerPage().clickInTreeTableRow(TREE_TABLE_ROW);
		getDesignerPage().clickDeleteButton();
		getDesignerPage().addNewGroup();
		getDesignerPage().saveDesign();
		closeNotificationIfExists();
		// Try to link the form again
		goToFormManagerPage();
		getFormManagerPage().clickLinkAbcdRulesButton();
		getFormManagerPage().getWindowLinkAbcdFormWindow().clickTableRow(TABLE_ROW);
		getFormManagerPage().getWindowLinkAbcdFormWindow().selectOptionGroupCheckBox();
		getFormManagerPage().getWindowLinkAbcdFormWindow().clickAccept();
		checkNotificationIsWarning(getNotification());
		// Check validation messages
		goToValidationPage();
		getValidationPage().clickAbcdLinkButton();
		getValidationPage().getTextAreaValue().equals(ABCD_LINK_MESSAGE);
		getValidationPage().clickAbcdDiffButton();
		getValidationPage().getWindowLinkAbcdFormWindow().clickAccept();
		getValidationPage().getTextAreaValue().equals(ABCD_DIFF_MESSAGE);
		logOut();
		deleteForm();
	}
}
