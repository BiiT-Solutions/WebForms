package com.biit.webforms.configuration;

import java.io.IOException;
import java.util.Properties;

import com.biit.utils.file.PropertiesFile;
import com.biit.webforms.logger.WebformsLogger;

public class WebformsConfigurationReader {

	private static final String REGEX_EMAIL = "regexEmail";

	private static final String DEFAULT_REGEX_EMAIL = "[a-zA-Z!#$%&'*+\\-/=?^_`{|}~]+(\\.[a-zA-Z!#$%&'*+\\-/=?^_`{|}~]|[a-zA-Z!#$%&'*+\\-/=?^_`{|}~])*@[a-zA-Z0-9](\\.[a-zA-Z0-9-]|[a-zA-Z0-9-])*[a-zA-Z0-9]";

	private static final String REGEX_AMOUNT = "regexAmount";

	private static final String DEFAULT_REGEX_AMOUNT = "[0-9]+\\.[0-9]*€";

	private static final String REGEX_DATE_BIRTHDAY = "regexDateBirthday";

	private static final String DEFAULT_REGEX_BIRTHDAY = "([0-9]{1,2}[-/]){1,2}[0-9]{4}";

	private static final String DATABASE_CONFIG_FILE = "settings.conf";

	private static final String GRAPHVIZ_TAG = "graphvizBinPath";

	private static final String DEFAULT_GRAPHVIZ_VALUE = "/usr/bin/dot";

	private static final String REGEX_TEXT = "regexText";

	private static final String DEFAULT_REGEX_TEXT = "[\\u0000-\\uFFFF]*";

	private static final String REGEX_PHONE = "regexPhone";

	private static final String DEFAULT_REGEX_PHONE = "[\\+]{0,1}[0-9]{5,14}";

	private static final String REGEX_IBAN = "regexIban";

	private static final String DEFAULT_REGEX_IBAN = "[A-Z]{2}[0-9]{2}( *[0-9A-Z]){1,34}";

	private static final String REGEX_BSN = "regexBsn";

	private static final String DEFAULT_REGEX_BSN = "[0-9]{9}";

	private static final String REGEX_DATE = "regexDate";

	private static final String DEFAULT_REGEX_DATE = "([0-9]{1,2}[-/]){1,2}[0-9]{4}";

	private static final String REGEX_DATE_PERIOD = "regexDatePeriod";

	private static final String DEFAULT_REGEX_DATE_PERIOD = "[0-9]+";

	private static final String REGEX_NUMBER = "regexNumber";

	private static final String DEFAULT_REGEX_NUMBER = "[0-9]+";

	private static final String REGEX_FLOAT = "regexFloat";

	private static final String DEFAULT_REGEX_FLOAT = "[0-9]+\\.[0-9]*";

	private static final String REGEX_POSTAL_CODE = "regexPostalCode";

	private static final String DEFAULT_REGEX_POSTAL_CODE = "[0-9]{4}[a-zA-Z]{2}";

	private static final String ISSUE_MANAGER_URL = "issueManagerUrl";

	private static final String DEFAULT_ISSUE_MANAGER_URL = null;

	private static final String DATE_PATTERN = "dd/MM/yyyy";

	private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

	private static final String BOOLEAN_SIMPLIFICATION_ENABLED = "booleanSimplificationEnabled";

	private static final String DEFAULT_BOOLEAN_SIMPLIFICATION_ENABLED = "false";

	private static final String XML_BASE_ADDRESS = "xmlBaseAddress";

	private static final String DEFAULT_XML_BASE_ADDRESS = "http://dev.biit-solutions.com/";

	private static final String JSON_EXPORT_ENABLED = "jsonExportEnabled";

	private static final String DEFAULT_JSON_EXPORT_ENABLED = "false";

	// XForms
	private final String XFORMS_USER_TAG = "orbeonUser";
	private final String XFORMS_PASSWORD_TAG = "orbeonPassword";
	private final String XFORMS_DATABASE_TAG = "orbeonDatabase";
	private final String XFORMS_DATABASE_HOST_TAG = "orbeonDatabaseHost";
	private final String XFORMS_FORM_RUNNER_TAG = "orbeonFormRunnerUrl";

	private final String DEFAULT_ORBEON_FORM_RUNNER_URL = "http://127.0.0.1:8080/orbeon/fr";
	private final String DEFAULT_XFORMS_DATABASE = "orbeon";
	private final String DEFAULT_HOST = "localhost";
	private final String DEFAULT_USER = "user";
	private final String DEFAULT_PASSWORD = "pass";

	private String graphvizBinPath;

	private String regexText;

	private String regexPhone;

	private String regexIban;

	private String regexBsn;

	private String regexDate;

	private String regexPostalCode;

	private String regexEmail;

	private String regexDatePeriod;

	private String regexNumber;

	private String regexFloat;

	private String issueManagerUrl;

	private String regexAmount;

	private String regexDateBirthday;

	private String datePattern;

	private boolean booleanSimplificationEnabled;

	private static WebformsConfigurationReader instance;

	private String xFormsUser;
	private String xFormsPassword;
	private String xFormsDatabaseName;
	private String xFormsDatabaseHost;
	private String orbeonFormRunnerUrl;

	private String xmlBaseAddress;

	private boolean jsonExportEnabled;

	private WebformsConfigurationReader() {
		readConfig();
	}

	public static WebformsConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (WebformsConfigurationReader.class) {
				if (instance == null) {
					instance = new WebformsConfigurationReader();
				}
			}
		}
		return instance;
	}

	/**
	 * Read database config from resource and update default connection parameters.
	 */
	private void readConfig() {
		Properties prop = new Properties();
		try {
			prop = PropertiesFile.load(DATABASE_CONFIG_FILE);
			graphvizBinPath = prop.getProperty(GRAPHVIZ_TAG, DEFAULT_GRAPHVIZ_VALUE);
			regexText = prop.getProperty(REGEX_TEXT, DEFAULT_REGEX_TEXT);
			regexPhone = prop.getProperty(REGEX_PHONE, DEFAULT_REGEX_PHONE);
			regexIban = prop.getProperty(REGEX_IBAN, DEFAULT_REGEX_IBAN);
			regexBsn = prop.getProperty(REGEX_BSN, DEFAULT_REGEX_BSN);
			regexDate = prop.getProperty(REGEX_DATE, DEFAULT_REGEX_DATE);
			regexPostalCode = prop.getProperty(REGEX_POSTAL_CODE, DEFAULT_REGEX_POSTAL_CODE);
			regexEmail = prop.getProperty(REGEX_EMAIL, DEFAULT_REGEX_EMAIL);
			regexDatePeriod = prop.getProperty(REGEX_DATE_PERIOD, DEFAULT_REGEX_DATE_PERIOD);
			regexNumber = prop.getProperty(REGEX_NUMBER, DEFAULT_REGEX_NUMBER);
			regexFloat = prop.getProperty(REGEX_FLOAT, DEFAULT_REGEX_FLOAT);
			regexAmount = prop.getProperty(REGEX_AMOUNT, DEFAULT_REGEX_AMOUNT);
			regexDateBirthday = prop.getProperty(REGEX_DATE_BIRTHDAY, DEFAULT_REGEX_BIRTHDAY);
			issueManagerUrl = prop.getProperty(ISSUE_MANAGER_URL, DEFAULT_ISSUE_MANAGER_URL);
			datePattern = prop.getProperty(DATE_PATTERN, DEFAULT_DATE_PATTERN);
			booleanSimplificationEnabled = Boolean.parseBoolean(prop.getProperty(BOOLEAN_SIMPLIFICATION_ENABLED,
					DEFAULT_BOOLEAN_SIMPLIFICATION_ENABLED));
			xFormsUser = prop.getProperty(XFORMS_USER_TAG, DEFAULT_USER);
			xFormsPassword = prop.getProperty(XFORMS_PASSWORD_TAG, DEFAULT_PASSWORD);
			xFormsDatabaseName = prop.getProperty(XFORMS_DATABASE_TAG, DEFAULT_XFORMS_DATABASE);
			xFormsDatabaseHost = prop.getProperty(XFORMS_DATABASE_HOST_TAG, DEFAULT_HOST);
			orbeonFormRunnerUrl = prop.getProperty(XFORMS_FORM_RUNNER_TAG, DEFAULT_ORBEON_FORM_RUNNER_URL);
			xmlBaseAddress = prop.getProperty(XML_BASE_ADDRESS, DEFAULT_XML_BASE_ADDRESS);
			jsonExportEnabled = Boolean
					.parseBoolean(prop.getProperty(JSON_EXPORT_ENABLED, DEFAULT_JSON_EXPORT_ENABLED));
		} catch (IOException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public String getGraphvizBinPath() {
		return graphvizBinPath;
	}

	public String getRegexText() {
		return regexText;
	}

	public String getRegexPhone() {
		return regexPhone;
	}

	public String getRegexIban() {
		return regexIban;
	}

	public String getRegexBsn() {
		return regexBsn;
	}

	public String getRegexDate() {
		return regexDate;
	}

	public String getRegexPostalCode() {
		return regexPostalCode;
	}

	public String getRegexEmail() {
		return regexEmail;
	}

	public String getRegexDatePeriod() {
		return regexDatePeriod;
	}

	public String getRegexNumber() {
		return regexNumber;
	}

	public String getRegexFloat() {
		return regexFloat;
	}

	public String getIssueManagerUrl() {
		return issueManagerUrl;
	}

	public String getRegexAmount() {
		return regexAmount;
	}

	public String getRegexDateBirthday() {
		return regexDateBirthday;
	}

	public String getDatePattern() {
		return datePattern;
	}

	/**
	 * Enable or disable the boolean simplification for reduce the length of Orbeon relevant rules.
	 * 
	 * @return
	 */
	public boolean isBooleanSimplificationEnabled() {
		return booleanSimplificationEnabled;
	}

	public String getXFormsUser() {
		return xFormsUser;
	}

	public String getXFormsPassword() {
		return xFormsPassword;
	}

	public String getXFormsDatabaseName() {
		return xFormsDatabaseName;
	}

	public String getXFormsDatabaseHost() {
		return xFormsDatabaseHost;
	}

	public String getOrbeonFormRunnerUrl() {
		return orbeonFormRunnerUrl;
	}

	public String getXmlBaseAddress() {
		return xmlBaseAddress;
	}

	public boolean isJsonExportEnabled() {
		return jsonExportEnabled;
	}
}
