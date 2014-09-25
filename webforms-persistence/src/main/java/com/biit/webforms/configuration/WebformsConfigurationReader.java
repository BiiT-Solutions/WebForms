package com.biit.webforms.configuration;

import java.io.IOException;
import java.util.Properties;

import com.biit.utils.file.PropertiesFile;
import com.biit.webforms.logger.WebformsLogger;

public class WebformsConfigurationReader {

	private static final String REGEX_EMAIL = "regexEmail";

	private static final String DEFAULT_REGEX_EMAIL = "[a-zA-Z!#$%&'*+\\-/=?^_`{|}~]+(\\.[a-zA-Z!#$%&'*+\\-/=?^_`{|}~]|[a-zA-Z!#$%&'*+\\-/=?^_`{|}~])*@[a-zA-Z0-9](\\.[a-zA-Z0-9-]|[a-zA-Z0-9-])*[a-zA-Z0-9]";

	private final String DATABASE_CONFIG_FILE = "settings.conf";

	private final String GRAPHVIZ_TAG = "graphvizBinPath";

	private final String DEFAULT_GRAPHVIZ_VALUE = "/usr/bin/dot";

	private final String REGEX_TEXT = "regexText";

	private final String DEFAULT_REGEX_TEXT = "[\\u0000-\\uFFFF]*";

	private final String REGEX_PHONE = "regexPhone";

	private final String DEFAULT_REGEX_PHONE = "\\(\\+[0-9]{2}\\)( [0-9]{3}){3}";

	private final String REGEX_IBAN = "regexIban";

	private final String DEFAULT_REGEX_IBAN = "[A-Z]{2}[0-9]{2}( *[0-9A-Z]){1,34}";

	private final String REGEX_BSN = "regexBsn";

	private final String DEFAULT_REGEX_BSN = "[0-9]{9}";

	private final String REGEX_DATE = "regexDate";

	private final String DEFAULT_REGEX_DATE = "([0-9]{1,2}-){1,2}[0-9]{4}";

	private final String REGEX_POSTAL_CODE = "regexPostalCode";

	private final String DEFAULT_REGEX_POSTAL_CODE = "[0-9]{4}[a-zA-Z]{2}";

	private String graphvizBinPath;

	private String regexText;

	private String regexPhone;

	private String regexIban;

	private String regexBsn;

	private String regexDate;

	private String regexPostalCode;

	private String regexEmail;

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
	 * Read database config from resource and update default connection
	 * parameters.
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

}
