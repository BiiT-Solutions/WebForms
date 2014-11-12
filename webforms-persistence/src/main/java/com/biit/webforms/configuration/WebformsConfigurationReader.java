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

	private static final String DEFAULT_REGEX_PHONE = "\\(\\+[0-9]{2}\\)( *[0-9]{3}){3}";

	private static final String REGEX_IBAN = "regexIban";

	private static final String DEFAULT_REGEX_IBAN = "[A-Z]{2}[0-9]{2}( *[0-9A-Z]){1,34}";

	private static final String REGEX_BSN = "regexBsn";

	private static final String DEFAULT_REGEX_BSN = "[0-9]{9}";

	private static final String REGEX_DATE = "regexDate";

	private static final String DEFAULT_REGEX_DATE = "([0-9]{1,2}[-/]){1,2}[0-9]{4}";

	private static final String REGEX_DATE_PERIOD = "regexDatePeriod";

	private static final String DEFAULT_REGEX_DATE_PERIOD = "[0-9]+[DMY]";

	private static final String REGEX_NUMBER = "regexNumber";

	private static final String DEFAULT_REGEX_NUMBER = "[0-9]+";

	private static final String REGEX_FLOAT = "regexFloat";

	private static final String DEFAULT_REGEX_FLOAT = "[0-9]+\\.[0-9]*";

	private static final String REGEX_POSTAL_CODE = "regexPostalCode";

	private static final String DEFAULT_REGEX_POSTAL_CODE = "[0-9]{4}[a-zA-Z]{2}";

	private static final String ISSUE_MANAGER_URL = "issueManagerUrl";

	private static final String DEFAULT_ISSUE_MANAGER_URL = null;

	private static final String XSD_XMLNS = "xsdXmlns";

	private static final String DEFAULT_XSD_XMLNS = "http://schemas.biit.com/";

	private static final String XSD_NAMESPACE = "xsdNamespace";

	private static final String DEFAULT_XSD_NAMESPACE = "http://schemas.biit.com/";

	private static final String DATE_PATTERN = "dd/MM/yyyy";

	private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

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

	private String xsdXmlns;

	private String xsdNamespace;

	private static WebformsConfigurationReader instance;

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
			xsdXmlns = prop.getProperty(XSD_XMLNS, DEFAULT_XSD_XMLNS);
			xsdNamespace = prop.getProperty(XSD_NAMESPACE, DEFAULT_XSD_NAMESPACE);
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

	public String getXsdXmlns() {
		return xsdXmlns;
	}

	public String getXsdNamespace() {
		return xsdNamespace;
	}

}
